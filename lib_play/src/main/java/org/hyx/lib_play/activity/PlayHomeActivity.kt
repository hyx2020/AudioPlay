package org.hyx.lib_play.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.hyx.lib_base.BaseActivity
import org.hyx.lib_play.databinding.ActivityPlayHomeBinding
import org.hyx.lib_play.parameter.OboeAudioManager
import org.hyx.lib_play.parameter.SpinnerAdapterFactory
import org.hyx.lib_play.parameter.SpinnerAdapterFactory.Companion.itemDescription
import org.hyx.lib_play.parameter.SpinnerAdapterFactory.Companion.itemValue
import kotlin.collections.HashMap

class PlayHomeActivity : BaseActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityPlayHomeBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //AudioPlayEngine.createEngine(this)

        setupAudioApiSpinner()
    }

    fun setupAudioApiSpinner() {
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
                println("$selected selected, Api $api")
                //AudioPlayEngine.setAudioApi(api)
            }
            binding.playRecordDeviceSpinner.id -> {
                println(((binding.playRecordDeviceSpinner.selectedItem) as (OboeAudioManager.DevInfo)).name)
                //AudioPlayEngine.setAudioDeviceId(((binding.playRecordDeviceSpinner.selectedItem) as (OboeAudioManager.DevInfo)).id)
            }
            binding.playPlaybackDeviceSpinner.id -> {
                println(((binding.playRecordDeviceSpinner.selectedItem) as (OboeAudioManager.DevInfo)).name)
                //AudioPlayEngine.setAudioDeviceId(((binding.playRecordDeviceSpinner.selectedItem) as (OboeAudioManager.DevInfo)).id)
            }
            binding.playChannelOut.id -> {
                val channel = Integer.parseInt(binding.playChannelOut.selectedItem.toString())
                println("channel: $channel")
                //AudioPlayEngine.setAudioDeviceId(((binding.playRecordDeviceSpinner.selectedItem) as (OboeAudioManager.DevInfo)).id)
            }
            binding.playBufferSize.id -> {
                val bufferSize = Integer.parseInt(binding.playBufferSize.selectedItem.toString())
                println("bufferSize: $bufferSize")
                //AudioPlayEngine.setAudioDeviceId(((binding.playRecordDeviceSpinner.selectedItem) as (OboeAudioManager.DevInfo)).id)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}