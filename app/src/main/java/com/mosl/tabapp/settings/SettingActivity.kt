package com.mosl.tabapp.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonActivity
import dagger.android.support.DaggerAppCompatActivity

class SettingActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setupPage()
        findViewById<Toolbar>(R.id.toolbar).apply {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                startActivity(Intent(this@SettingActivity, CommonActivity::class.java))
                finish()
            }
            title = getString(R.string.MOSL_MENU_SETTING)
        }
    }

    private fun setupPage() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SettingFragment())
            .commit()
//            .attach(SettingFragment())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@SettingActivity, CommonActivity::class.java))
        finish()
    }
}