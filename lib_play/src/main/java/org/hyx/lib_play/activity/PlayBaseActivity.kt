package org.hyx.lib_play.activity

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import org.hyx.lib_base.BaseActivity
import org.hyx.lib_play.R
import org.hyx.lib_play.lib.AudioEngine

/**
 * created by hyx on 2021/7/30.
 */
open class PlayBaseActivity : BaseActivity() {
    var isAAudioSupport: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE,  Manifest.permission.MODIFY_AUDIO_SETTINGS
            ), 0x1
        )
    }
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