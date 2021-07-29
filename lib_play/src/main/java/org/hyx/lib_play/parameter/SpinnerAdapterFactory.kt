package org.hyx.lib_play.parameter

import android.content.Context
import android.widget.ArrayAdapter
import org.hyx.lib_play.R
import java.util.*
import kotlin.collections.HashMap

/**
 * created by hyx on 2021/7/27.
 */
class SpinnerAdapterFactory {
    class SimpleAdapterList<T>(context: Context, layout: Int, array: ArrayList<T>) : ArrayAdapter<T>(context, layout, array) {
        constructor(context: Context, array: ArrayList<T>) : this(context, R.layout.base_spinner, array)
    }

    class SimpleAdapterArray<T>(context: Context, layout: Int, array: Array<T>) : ArrayAdapter<T>(context, layout, array) {
        constructor(context: Context, array: Array<T>) : this(context, R.layout.base_spinner, array)
    }

    class ComplexAdapter constructor(context: Context, layout: Int, resourceId: Int, list: List<HashMap<String, String>>, description: String) : android.widget.SimpleAdapter(context, list, layout, arrayOf(description), intArrayOf(resourceId)) {

        constructor(context: Context, layout: Int, resourceId: Int) : this(context, layout, resourceId, audioApiList, itemDescription)

        constructor(context: Context, layout: Int, resourceId: Int, description: String) : this(context, layout, resourceId, audioApiList, description)

        constructor(context: Context) : this(context, R.layout.base_spinner, R.id.base_spinner_text, audioApiList, itemDescription)
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
                option[itemValue] = (i - 1).toString()
                audioApiOptions.add(option)
            }
            return audioApiOptions
        }
    }
}