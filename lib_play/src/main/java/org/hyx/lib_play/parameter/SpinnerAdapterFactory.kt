package org.hyx.lib_play.parameter

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import org.hyx.lib_play.R
import java.util.ArrayList

/**
 * created by hyx on 2021/7/27.
 */
class SpinnerAdapterFactory {
    class simpleAdapter(context: Context, layout: Int, array: Array<String>) :
        ArrayAdapter<String>(context, layout, array)

    class complexAdapter constructor(
        context: Context,
        layout: Int,
        resourceId: Int,
        list: List<HashMap<String, String>>,
        description: String
    ) : SimpleAdapter(
        context, list, layout, arrayOf(description), intArrayOf(resourceId)
    ) {
        constructor(
            context: Context,
            layout: Int,
            resourceId: Int,
        ) : this(
            context, layout, resourceId, audioApiList, itemDescription
        )
        constructor(
            context: Context,
            layout: Int,
            resourceId: Int,
            description: String
        ) : this(
            context, layout, resourceId, audioApiList, description
        )
    }

    companion object {
        const val itemDescription = "description"
        const val itemValue = "value"

        val audioApiList = createAudioApisOptionsList()

        private fun createAudioApisOptionsList(): List<HashMap<String, String>> {
            val audioApiOptions = ArrayList<HashMap<String, String>>()
            for (i in AudioConfig.AUDIO_API_OPTIONS.indices) {
                val option = HashMap<String, String>()
                option[itemDescription] = AudioConfig.AUDIO_API_OPTIONS[i]
                option[itemValue] = i.toString()
                audioApiOptions.add(option)
            }
            return audioApiOptions
        }
    }
}