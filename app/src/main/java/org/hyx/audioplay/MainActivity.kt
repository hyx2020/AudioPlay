package org.hyx.audioplay

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import org.hyx.lib_base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ARouter.getInstance().build("/base/test").withString("name", "小姜").withInt("age", 23)
            .navigation()
    }
}
