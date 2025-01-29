package com.mosl.tabapp.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.mosl.tabapp.R
import com.mosl.tabapp.common.FusedLocation
import com.mosl.tabapp.core.CommonActivity
import dagger.android.support.DaggerAppCompatActivity

class SettingActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
//        setupPage()
        findViewById<Toolbar>(R.id.toolbar).apply {
            setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
            setNavigationOnClickListener {
//                startActivity(Intent(this@SettingActivity, CommonActivity::class.java))
                finish()
            }
            title = getString(R.string.MOSL_MENU_SETTING)
            setTitleTextColor(ContextCompat.getColor(this@SettingActivity, R.color.white))
        }
    }

    private fun setupPage() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SettingFragment())
            .commit()
//            .attach(SettingFragment())
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
//        startActivity(Intent(this@SettingActivity, CommonActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FusedLocation.REQUEST_PERMISSIONS_REQUEST_CODE)
            supportFragmentManager.findFragmentById(R.id.fragment_container)
                ?.onRequestPermissionsResult(
                    requestCode,
                    permissions,
                    grantResults
                )
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FusedLocation.REQUEST_CHECK_SETTINGS)
            supportFragmentManager.findFragmentById(R.id.fragment_container)?.onActivityResult(
                requestCode,
                resultCode,
                data
            )
    }
}