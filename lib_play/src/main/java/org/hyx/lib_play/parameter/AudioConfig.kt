package org.hyx.lib_play.parameter

import android.media.AudioDeviceInfo

/**
 * created by hyx on 2021/7/26.
 */
class AudioConfig {
    companion object {
        val AUDIO_API_OPTIONS = arrayOf("Unspecified", "OpenSL ES", "AAudio")

        fun audioTypeToString(type: Int): String {
            when (type) {
                AudioDeviceInfo.TYPE_AUX_LINE -> return "auxiliary line-level connectors"
                AudioDeviceInfo.TYPE_BLUETOOTH_A2DP -> return "Bluetooth device supporting the A2DP profile";
                AudioDeviceInfo.TYPE_BLUETOOTH_SCO -> return "Bluetooth device typically used for telephony";
                AudioDeviceInfo.TYPE_BUILTIN_EARPIECE -> return "built-in earphone speaker";
                AudioDeviceInfo.TYPE_BUILTIN_MIC -> return "built-in microphone";
                AudioDeviceInfo.TYPE_BUILTIN_SPEAKER -> return "built-in speaker";
                AudioDeviceInfo.TYPE_BUS -> return "BUS";
                AudioDeviceInfo.TYPE_DOCK -> return "DOCK";
                AudioDeviceInfo.TYPE_FM -> return "FM";
                AudioDeviceInfo.TYPE_FM_TUNER -> return "FM tuner";
                AudioDeviceInfo.TYPE_HDMI -> return "HDMI";
                AudioDeviceInfo.TYPE_HDMI_ARC -> return "HDMI audio return channel";
                AudioDeviceInfo.TYPE_IP -> return "IP";
                AudioDeviceInfo.TYPE_LINE_ANALOG -> return "line analog";
                AudioDeviceInfo.TYPE_LINE_DIGITAL -> return "line digital";
                AudioDeviceInfo.TYPE_TELEPHONY -> return "telephony";
                AudioDeviceInfo.TYPE_TV_TUNER -> return "TV tuner";
                AudioDeviceInfo.TYPE_USB_ACCESSORY -> return "USB accessory";
                AudioDeviceInfo.TYPE_USB_DEVICE -> return "USB device";
                AudioDeviceInfo.TYPE_WIRED_HEADPHONES -> return "wired headphones";
                AudioDeviceInfo.TYPE_WIRED_HEADSET -> return "wired headset";
                AudioDeviceInfo.TYPE_UNKNOWN -> return "unknown";
            }
            return ""
        }
    }
}