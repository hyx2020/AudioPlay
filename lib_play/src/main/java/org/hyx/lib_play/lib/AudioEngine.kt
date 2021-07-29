package org.hyx.lib_play.lib

/**
 * created by hyx on 2021/7/23.
 */
class AudioEngine {
    companion object {
        private var self: AudioEngine

        fun isAAudioSupport(): Boolean {
            return self.isAAudioRecommended()
        }

        init {
            System.loadLibrary("audio-lib-hyx")
            self = AudioEngine()
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private external fun isAAudioRecommended(): Boolean
}