package org.hyx.lib_play.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import org.hyx.lib_base.BaseActivity
import org.hyx.lib_play.R
import org.hyx.lib_play.databinding.ActivityPlayHomeBinding
import org.hyx.lib_play.lib.AudioEngine
import org.hyx.lib_play.parameter.SpinnerAdapterFactory
import kotlin.collections.HashMap

class PlayHomeActivity : BaseActivity() {
    private lateinit var binding: ActivityPlayHomeBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AudioEngine.createEngine(this)

        setupAudioApiSpinner()
    }

    fun setupAudioApiSpinner() {
        binding.audioApiSpinner.adapter = SpinnerAdapterFactory.complexAdapter(this, R.layout.base_spinner, R.id.audioApiOption)

        binding.audioApiSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    println((parent!!.getItemAtPosition(position) as HashMap<*, *>)[getString(R.string.play_buffer_size_description_key)].toString())
                    AudioEngine.setAudioApi(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }
}