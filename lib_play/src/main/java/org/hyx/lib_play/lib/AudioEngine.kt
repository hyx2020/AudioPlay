package org.hyx.lib_play.lib

import android.content.Context
import android.media.AudioManager
import android.os.Build

/**
 * created by hyx on 2021/7/23.
 */
class AudioEngine {
    companion object {
        private var self: AudioEngine

        fun create(context: Context): Boolean {
            self.setDefaultStreamValues(context)
            return self.create()
        }

        fun delete() {
            self.delete()
        }

        fun isAAudioSupport(): Boolean {
            return self.isAAudioRecommended()
        }

        fun setAudioApi(apiType: Int): Boolean {
            return self.setAPI(apiType)
        }

        fun setRecordDevId(deviceId: Int) {
            self.setRecordingDeviceId(deviceId)
        }

        fun setPlayDevId(deviceId: Int) {
            self.setPlaybackDeviceId(deviceId)
        }

        // true open audio, false close;
        fun setEffectOn(isEffectOn: Boolean): Boolean {
            return self.setEffectOn(isEffectOn)
        }

        fun setChannelInput(count: Int) {
            self.setChannelInput(count)
        }

        init {
            System.loadLibrary("audio-lib-hyx")
            self = AudioEngine()
        }
    }

    fun setDefaultStreamValues(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val myAudioMgr = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val sampleRateStr = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE)
            val defaultSampleRate = sampleRateStr.toInt()
            val framesPerBurstStr = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER)
            val defaultFramesPerBurst = framesPerBurstStr.toInt()
            self.native_setDefaultStreamValues(defaultSampleRate, defaultFramesPerBurst)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    external fun create(): Boolean
    external fun isAAudioRecommended(): Boolean
    external fun setAPI(apiType: Int): Boolean
    external fun setEffectOn(isEffectOn: Boolean): Boolean
    external fun setRecordingDeviceId(deviceId: Int)
    external fun setPlaybackDeviceId(deviceId: Int)
    external fun setChannelInput(channel: Int)
    external fun delete()
    external fun native_setDefaultStreamValues(defaultSampleRate: Int, defaultFramesPerBurst: Int)
}