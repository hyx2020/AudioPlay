package org.hyx.lib_base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

/**
 * created by hyx on 2021/7/20.
 */
abstract class BaseActivity: AppCompatActivity() {
    @Override
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        StatusBarUtil.setTranslucentForImageViewInFragment(this, null)
    }
}