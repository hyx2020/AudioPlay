package org.hyx.audioplay

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.alibaba.android.arouter.launcher.ARouter
import org.hyx.audioplay.databinding.ActivityMainBinding
import org.hyx.lib_base.BaseActivity

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

/*        ARouter.getInstance().build("/base/test").withString("name", "小姜").withInt("age", 23)
            .navigation()*/

        setNavController()
    }

    private fun setNavController() {
        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController

        binding.bottomNavView.setupWithNavController(navController)
    }
}
