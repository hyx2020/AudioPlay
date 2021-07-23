package org.hyx.lib_base

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

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

    fun applyPermission(permission: String, requestCode: Int) {
        if (ActivityCompat.checkSelfPermission(
                this, permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO), requestCode
            )
        }
    }
}