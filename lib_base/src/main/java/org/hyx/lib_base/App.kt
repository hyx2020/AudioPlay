package org.hyx.lib_base

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter

/**
 * created by hyx on 2021/7/22.
 */
class App: Application(){
    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.IS_DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }

        ARouter.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()

        ARouter.getInstance().destroy()
    }
}