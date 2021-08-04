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

        fun getShortArray(): FloatArray {
            return self.getAudioArray()
        }

        fun sendAudio(buffer: FloatArray) {
            self.writeTrack(buffer)
        }

        fun openPlayStream() {
            self.openPlayStream()
        }

        fun closePlayStream() {
            self.closePlayStream()
        }

        init {
            System.loadLibrary("audio-lib-hyx")
            self = AudioEngine()
        }
    }

    fun setDefaultStreamValues(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val myAudioMgr = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
/*            val sampleRateStr = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE)
            val defaultSampleRate = sampleRateStr.toInt()*/
            val defaultSampleRate = 48000
            val framesPerBurstStr = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER)
            val defaultFramesPerBurst = framesPerBurstStr.toInt()
            self.nativeSetDefaultStreamValues(defaultSampleRate, defaultFramesPerBurst)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private external fun create(): Boolean
    private external fun isAAudioRecommended(): Boolean
    private external fun setAPI(apiType: Int): Boolean
    private external fun setEffectOn(isEffectOn: Boolean): Boolean
    private external fun setRecordingDeviceId(deviceId: Int)
    private external fun setPlaybackDeviceId(deviceId: Int)
    private external fun setChannelInput(channel: Int)
    private external fun delete()
    private external fun nativeSetDefaultStreamValues(defaultSampleRate: Int, defaultFramesPerBurst: Int)
    private external fun getAudioArray(): FloatArray
    private external fun openPlayStream()
    private external fun writeTrack(buffer: FloatArray)
    private external fun closePlayStream()
}