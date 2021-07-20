package org.hyx.audioplay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import org.hyx.lib_base.BaseActivity
import org.hyx.lib_play.JniLib

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        JniLib.static_detection()
    }
}
