package org.hyx.lib_play.activity

import android.os.Bundle
import android.widget.Toast
import org.hyx.lib_base.BaseActivity
import org.hyx.lib_play.R
import org.hyx.lib_play.lib.AudioEngine

/**
 * created by hyx on 2021/7/30.
 */
open class PlayBaseActivity : BaseActivity() {
    var isAAudioSupport: Boolean = false

    override fun onResume() {
        super.onResume()
        if (AudioEngine.create(this)) {
            isAAudioSupport = AudioEngine.isAAudioSupport()
        } else {
            Toast.makeText(this, getString(R.string.play_audio_create_failed), Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        AudioEngine.delete()
        super.onDestroy()
    }
}