package org.hyx.audioplay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.hyx.lib_play.JniLib

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        JniLib.static_detection()
    }
}
