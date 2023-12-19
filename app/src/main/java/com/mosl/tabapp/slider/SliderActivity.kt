package com.mosl.tabapp.slider

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.mosl.app.coreui.base.LeanBaseActivity
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.R
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryUiEvent
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.databinding.ActivitySliderBinding
import com.mosl.tabapp.splash.SplashActivity
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.util.RootDeviceHelper.isEmulator
import com.mosl.tabapp.util.RootDeviceHelper.isRooted
import com.mosl.tabapp.util.rootbeer.RootBeer
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject

class SliderActivity : LeanBaseActivity() {

  lateinit var binding: ActivitySliderBinding
  @Inject
  lateinit var userManager: UserManager

  override fun getLayout(): Int {
    return R.layout.activity_slider
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Timber.d("Something SliderActivity oncreate")
    AndroidInjection.inject(this)
    if (Build.VERSION.SDK_INT >= 21) {
      window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    binding = ActivitySliderBinding.bind(findViewById(R.id.slider_root))

     val rootBeer = RootBeer(this)

  /*  if (!isEmulator() && rootBeer.isRooted || rootBeer.isRootedWithBusyBoxCheck) {
      showRootedMessage()
      return
    }*/

    if (userManager.isLoggedIn()) {
      launchSplash()
    }

    val imageList = arrayOf(
        R.drawable.slider_first_new, R.drawable.slider_second_new, R.drawable.slider_third_new,
        R.drawable.slider_fourth_new
    )

    binding.btnDone.setOnClickListener {
      if (getItem() < imageList.size) {
        binding.viewPager.setCurrentItem(getItem())
      } else {
        launchSplash()
      }
    }

    binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
      override fun onPageScrollStateChanged(state: Int) {

      }

      override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
      ) {

      }

      override fun onPageSelected(position: Int) {
        if (getItem() < imageList.size) {
          binding.btnDone.setText(getString(R.string.NEXT))
        } else {
          binding.btnDone.setText(getString(R.string.DONE))
        }
      }
    }
    )
    val imageSliderAdapter = ImageSliderAdapter(this, imageList)

    binding.viewPager.adapter = imageSliderAdapter
  }

  private fun showRootedMessage() {
    MSFDialog.alertDialog(
      this@SliderActivity, 0,
      applicationContext.resources.getString(R.string.motilal_oswal),
      MOSLConstants.ROOTED_DEVICE_MESSAGE, MOSLConstants.OK, false
    ) { action: MSFDialog.Action, _: Array<Any?>? ->
      if (action == MSFDialog.Action.OK) {
        finish()
      }
    }
  }

  private fun getItem(): Int {
    return binding.viewPager.currentItem + 1
  }

  private fun launchSplash() {
    val intent = Intent(this@SliderActivity, SplashActivity::class.java)
    startActivity(intent)
    finish()
  }
}




