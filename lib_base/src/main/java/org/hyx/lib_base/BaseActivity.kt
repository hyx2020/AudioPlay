package org.hyx.lib_base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter

/**
 * created by hyx on 2021/7/21.
 */
abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatus()
    }

    private fun setStatus() {
        StatusBarUtil.setTranslucentForImageView(this, null)
    }
}