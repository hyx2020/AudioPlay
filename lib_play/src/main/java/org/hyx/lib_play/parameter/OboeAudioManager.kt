package org.hyx.lib_play.parameter

import android.content.Context
import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.widget.ArrayAdapter

/**
 * created by hyx on 2021/7/27.
 */
class OboeAudioManager constructor(context: Context) {
    var handler: Handler? = null
    var callback: AudioDeviceCallback? = null
    private val audioManager: AudioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    var mDirectionType: Int = -1
        set(value) {
            field = value
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                callback = object : AudioDeviceCallback() {
                    override fun onAudioDevicesAdded(addedDevices: Array<AudioDeviceInfo>) {
                        for (deviceInfo in addedDevices) {
                            println(AudioConfig.audioTypeToString(deviceInfo.type) + "," + deviceInfo.isSink + "," + deviceInfo.isSource)
                        }
                    }

                    override fun onAudioDevicesRemoved(removedDevices: Array<AudioDeviceInfo>) {
                        for (deviceInfo in removedDevices) {
                            println(AudioConfig.audioTypeToString(deviceInfo.type))
                        }
                    }
                }
                audioManager.registerAudioDeviceCallback(callback, handler)
            }
        }

    fun unregister() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && callback != null) {
            audioManager.unregisterAudioDeviceCallback(callback)
        }
    }

    companion object {
    }
}