//package org.hyx.lib_play.lib
//
//import android.content.Context
//import android.media.AudioManager
//import android.os.Build
//
///**
// * created by hyx on 2021/7/23.
// */
//class AudioRecordEngine {
//    companion object {
//        private var self: AudioRecordEngine
//        private var mEngineHandle: Long = 0
//
///*        private fun setDefaultStreamValues(context: Context) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                val myAudioMgr = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
//                val sampleRateStr = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE)
//                val defaultSampleRate = sampleRateStr.toInt()
//                //val sampleRate = 48000
//                val framesPerBurstStr = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER)
//                val defaultFramesPerBurst = framesPerBurstStr.toInt()
//                self.native_setDefaultStreamValues(defaultSampleRate, defaultFramesPerBurst)
//            }
//        }*/
//
//        init {
//            System.loadLibrary("audio-lib-hyx")
//            self = AudioRecordEngine()
//        }
//    }
//
//    ////////////////////////////////////////////////////////////////////////////////////////////////
//
//}