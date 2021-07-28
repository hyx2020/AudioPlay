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
        private var mEngineHandle: Long = 0

        fun createEngine(context: Context): Boolean {
            if (mEngineHandle == 0L) {
                setDefaultStreamValues(context)
                mEngineHandle = self.native_createEngine()
            }

            return mEngineHandle != 0L
        }

        fun startEngine(): Int {
            return if (mEngineHandle != 0L) {
                self.native_startEngine(mEngineHandle)
            } else {
                -1
            }
        }

        fun stopEngine(): Int {
            return if (mEngineHandle != 0L) {
                self.native_stopEngine(mEngineHandle)
            } else {
                -1
            }
        }

        fun deleteEngine() {
            if (mEngineHandle != 0L) {
                self.native_deleteEngine(mEngineHandle)
            }
            mEngineHandle = 0L
        }

        fun setAudioApi(audioApi: Int) {
            if(mEngineHandle != 0L) self.native_setAudioApi(mEngineHandle, audioApi)
        }

        private fun setDefaultStreamValues(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                val myAudioMgr = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val sampleRateStr = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE)
                val defaultSampleRate = sampleRateStr.toInt()
                //val sampleRate = 48000
                val framesPerBurstStr =
                    myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER)
                val defaultFramesPerBurst = framesPerBurstStr.toInt()
                self.native_setDefaultStreamValues(defaultSampleRate, defaultFramesPerBurst)
            }
        }

        init {
            System.loadLibrary("audio-lib-hyx")
            self = AudioEngine()
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private external fun native_createEngine(): Long
    private external fun native_startEngine(engineHandle: Long): Int
    private external fun native_stopEngine(engineHandle: Long): Int
    private external fun native_deleteEngine(engineHandle: Long)
    private external fun native_setDefaultStreamValues(sampleRate: Int, framesPerBurst: Int)
    private external fun native_setAudioApi(engineHandle: Long, audioApi: Int)
}