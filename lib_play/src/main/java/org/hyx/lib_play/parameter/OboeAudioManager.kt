package org.hyx.lib_play.parameter

import android.content.Context
import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import org.hyx.lib_play.R
import kotlin.collections.ArrayList

/**
 * created by hyx on 2021/7/27.
 */
class OboeAudioManager constructor(context: Context) {
    private var handler: Handler? = null
    private var callback: AudioDeviceCallback? = null
    private val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    var mDirectionType: Int = -1

    var devListStdIn: ArrayList<DevInfo> = ArrayList()
    var devListStdOut: ArrayList<DevInfo> = ArrayList()

    fun unregister() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && callback != null) {
            audioManager.unregisterAudioDeviceCallback(callback)
        }
    }

    fun createListFrom(devices: Array<AudioDeviceInfo>, directionType: Int): ArrayList<DevInfo> {
        val deviceList: ArrayList<DevInfo> = ArrayList()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (info in devices) {
                if (directionType == AudioManager.GET_DEVICES_ALL || directionType == AudioManager.GET_DEVICES_OUTPUTS && info.isSink || directionType == AudioManager.GET_DEVICES_INPUTS && info.isSource) {
                    deviceList.add(DevInfo("${info.productName} ${AudioConfig.audioTypeToString(info.type)}", info.id))
                }
            }
        }
        return deviceList;
    }

    init {
        devListStdIn.add(DevInfo(context.getString(R.string.play_auto_select), 0))
        devListStdOut.add(DevInfo(context.getString(R.string.play_auto_select), 0))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            callback = object : AudioDeviceCallback() {
                override fun onAudioDevicesAdded(addedDevices: Array<AudioDeviceInfo>) {
                    devListStdIn.addAll(createListFrom(addedDevices, AudioManager.GET_DEVICES_INPUTS))
                    devListStdOut.addAll(createListFrom(addedDevices, AudioManager.GET_DEVICES_OUTPUTS))
                }

                override fun onAudioDevicesRemoved(removedDevices: Array<AudioDeviceInfo>) {
                    val listStdIn = createListFrom(removedDevices, AudioManager.GET_DEVICES_INPUTS)
                    val listStdOut = createListFrom(removedDevices, AudioManager.GET_DEVICES_OUTPUTS)

                    for (dev in listStdIn) {
                        devListStdIn.remove(dev)
                    }
                    for (dev in listStdOut) {
                        devListStdOut.remove(dev)
                    }
                }
            }
            audioManager.registerAudioDeviceCallback(callback, handler)
        }
    }

    companion object {
        private var instance: OboeAudioManager? = null

        fun get(context: Context): OboeAudioManager {
            if (instance == null) {
                instance = OboeAudioManager(context)
            }
            return instance!!
        }
    }

    class DevInfo(val name: String, val id: Int) {
        override fun toString(): String {
            return name
        }
    }
}