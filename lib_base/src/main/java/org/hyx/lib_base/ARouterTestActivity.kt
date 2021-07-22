package org.hyx.lib_base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = "/base/test")
class ARouterTestActivity : AppCompatActivity() {
    @Autowired
    @JvmField var name: String? = null
    @Autowired
    @JvmField var age: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_arouter_test)

        Log.e("testRes", "name = $name, age = $age")
    }
}