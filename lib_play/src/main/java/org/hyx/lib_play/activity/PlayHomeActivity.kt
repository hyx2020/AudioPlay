package org.hyx.lib_play.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.hyx.lib_base.FunShare
import org.hyx.lib_play.R
import org.hyx.lib_play.databinding.ActivityPlayHomeBinding
import org.hyx.lib_play.lib.AudioEngine
import org.hyx.lib_play.parameter.OboeAudioManager
import org.hyx.lib_play.parameter.SpinnerAdapterFactory
import org.hyx.lib_play.parameter.SpinnerAdapterFactory.Companion.itemDescription
import org.hyx.lib_play.parameter.SpinnerAdapterFactory.Companion.itemValue
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PlayHomeActivity : PlayBaseActivity(), AdapterView.OnItemSelectedListener, View.OnClickListener {
    private lateinit var binding: ActivityPlayHomeBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setupAudioApiSpinner()
    }

    private fun init() {
        binding.playRecordStart.setOnClickListener(this)
        binding.playPlaybackStart.setOnClickListener(this)
    }

    private fun setupAudioApiSpinner() {
        binding.playAudioApiSpinner.adapter = SpinnerAdapterFactory.ComplexAdapter(this)
        binding.playChannelOut.adapter = SpinnerAdapterFactory.SimpleAdapterArray(this, arrayOf(1, 2, 3, 4, 5, 6, 7, 8))
        binding.playBufferSize.adapter = SpinnerAdapterFactory.SimpleAdapterArray(this, arrayOf(0, 1, 2, 4, 8))
        binding.playAudioApiSpinner.onItemSelectedListener = this
        binding.playRecordDeviceSpinner.onItemSelectedListener = this
        binding.playPlaybackDeviceSpinner.onItemSelectedListener = this
        binding.playChannelOut.onItemSelectedListener = this
        binding.playBufferSize.onItemSelectedListener = this

        GlobalScope.launch(Dispatchers.IO) {
            OboeAudioManager.get(applicationContext)
            delay(100)
            GlobalScope.launch(Dispatchers.Main) {
                binding.playRecordDeviceSpinner.adapter = SpinnerAdapterFactory.SimpleAdapterList(applicationContext, OboeAudioManager.get(applicationContext).devListStdIn)
                binding.playPlaybackDeviceSpinner.adapter = SpinnerAdapterFactory.SimpleAdapterList(applicationContext, OboeAudioManager.get(applicationContext).devListStdOut)
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            binding.playAudioApiSpinner.id -> {
                val selected = (parent.getItemAtPosition(position) as HashMap<*, *>)[itemDescription].toString()
                val api = Integer.parseInt((parent.getItemAtPosition(position) as HashMap<*, *>)[itemValue].toString())
                AudioEngine.setAudioApi(api)
                println("$selected selected, Api $api")
                if (!isAAudioSupport && api == 1) {
                    Toast.makeText(this, "AAudio not supported", Toast.LENGTH_SHORT).show()
                }
            }
            binding.playRecordDeviceSpinner.id -> {
                println(((binding.playRecordDeviceSpinner.selectedItem) as (OboeAudioManager.DevInfo)).name)
                AudioEngine.setRecordDevId(((binding.playRecordDeviceSpinner.selectedItem) as (OboeAudioManager.DevInfo)).id)
            }
            binding.playPlaybackDeviceSpinner.id -> {
                println(((binding.playPlaybackDeviceSpinner.selectedItem) as (OboeAudioManager.DevInfo)).name)
                AudioEngine.setPlayDevId(((binding.playPlaybackDeviceSpinner.selectedItem) as (OboeAudioManager.DevInfo)).id)
            }
            binding.playChannelOut.id -> {
                val channel = Integer.parseInt(binding.playChannelOut.selectedItem.toString())
                println("channel: $channel")
                AudioEngine.setChannelInput(channel)
            }
            binding.playBufferSize.id -> {
                val bufferSize = Integer.parseInt(binding.playBufferSize.selectedItem.toString())
                println("bufferSize: $bufferSize")
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.playRecordStart.id -> {
                if(binding.playRecordStart.text == getString(R.string.play_audio_record_start)) {
                    AudioEngine.setEffectOn(true)
                    binding.playRecordStart.text = getString(R.string.play_audio_record_stop)
                    binding.playHomeLog.text = getString(R.string.play_audio_record_start)
                    arrayAdd()
                } else {
                    AudioEngine.setEffectOn(false)
                    save()
                    binding.playRecordStart.text = getString(R.string.play_audio_record_start)
                    binding.playHomeLog.text = getString(R.string.play_audio_record_stop)
                }
            }
            binding.playPlaybackStart.id -> {
                if(binding.playPlaybackStart.text == getString(R.string.play_audio_play_start)) {
                    playStart()
                    binding.playPlaybackStart.text = getString(R.string.play_audio_play_stop)
                    binding.playHomeLog.text = getString(R.string.play_audio_play_start)
                } else {
                    playStop()
                    binding.playPlaybackStart.text = getString(R.string.play_audio_play_start)
                    binding.playHomeLog.text = getString(R.string.play_audio_play_stop)
                }
            }
        }
    }

    private lateinit var recordSchedule :ScheduledFuture<*>
    private lateinit var playSchedule :ScheduledFuture<*>
    private val saveArray = ArrayList<FloatArray>()

    private fun arrayAdd() {
        recordSchedule = FunShare.schedule.scheduleWithFixedDelay({
            val array = AudioEngine.getShortArray();
            saveArray.add(array)
            println("add ${array.size}")
        }, 0, 342, TimeUnit.MILLISECONDS);
    }

    private fun save() {
        if(!this::recordSchedule.isInitialized) return
        recordSchedule.cancel(false)

        FunShare.schedule.schedule({
            FunShare.save(saveArray, this)
            saveArray.clear()
        }, 350, TimeUnit.MILLISECONDS)
    }

    private fun playStart() {
        AudioEngine.setEffectOn(true)
        AudioEngine.setPlayFlag(true)
        val audio = FunShare.getAndPlay(this)

        playSchedule = FunShare.schedule.scheduleWithFixedDelay({
            AudioEngine.sendAudio(audio, audio.size)
            println("play once")
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    private fun playStop() {
        if(!this::playSchedule.isInitialized) return
        playSchedule.cancel(false)
        FunShare.schedule.schedule({
            AudioEngine.setEffectOn(false)
        }, 50, TimeUnit.MILLISECONDS)
    }
}