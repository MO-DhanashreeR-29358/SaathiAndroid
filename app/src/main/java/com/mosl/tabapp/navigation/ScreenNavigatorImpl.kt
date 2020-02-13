package com.mosl.tabapp.navigation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.mosl.annotations.ActivityScoped
import com.mosl.app.core.utils.toast
import com.mosl.mobile.user.login.UserManager
import com.mosl.navigator.*
import com.mosl.tabapp.banner.BannerFragment
import com.mosl.tabapp.banner.BannerNavigator
import com.mosl.tabapp.banner.BannerScreenData
import com.mosl.tabapp.banner.BannerView.Companion.ARG_BANNER_SCREEN_DATA
import com.mosl.tabapp.common.EkycSecurityUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.ekyc.*
import com.mosl.tabapp.navigation.NavigationConstants.NAVIGATE_TO_SALES_BROCHURE
import javax.inject.Inject

@ActivityScoped
class ScreenNavigatorImpl @Inject constructor(
    val activity: AppCompatActivity,
    protected val navigationHelper: NavigationHelper,
    val userManager: UserManager,
    val navigator: Navigator
) : BaseNavigator, EkycNavigator, BannerNavigator {

    override fun openPage(
        pageId: Int,
        args: Bundle,
        addToStack: Boolean,
        forceReload: Boolean
    ) {
        openPage(FragmentNavigationEntry(pageId, args, addToStack, forceReload))
    }

    override fun openNewEkycApp(
        isExistingUser: Boolean,
        userId: String
    ) {
        val commandType = if (isExistingUser) RESUME else NEW
        try {
            val launchIntentForPackage =
                activity.packageManager.getLaunchIntentForPackage(EKYC_PACKAGE_NAME)
            launchIntentForPackage?.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
            launchIntentForPackage?.putExtra(
                OUTPUT, EkycSecurityUtil.encrypt(userManager.getDataForEkyc(commandType, userId))
            )
            activity.startActivity(launchIntentForPackage)
        } catch (e: Exception) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(PLAY_STORE_URL + EKYC_PACKAGE_NAME))
            activity.startActivity(intent)
        }
    }

    override fun goBack() {
        openPage(FragmentNavigationEntry.goBack())
    }

    override fun goBack(result: Bundle) {
        openPage(FragmentNavigationEntry.goBackWithResult(result))
    }

    override fun openHome() {
        openPage(NavigationConstants.NAVIGATE_TO_DASHBOARD, false)
    }

    override fun openPage(entry: NavigationEntry) {
        processNavigation(entry)
    }

    override fun openPage(request: NavigationRequest) {
        handleNavigationRequest(request)
    }

    protected fun handleNavigationRequest(request: NavigationRequest) {
        val pageId = request.pageId
        when {
            /**
             * (pageId < -1) represents pages in debug variant.
             */
            pageId < -1 -> processNavigation(request.asFragmentEntry())
            /**
             * Handles case where work is in progress.
             */
            pageId == NavigationConstants.NAVIGATE_TO_NONE -> activity.toast("Work In Progress")
            /**
             * Handles back navigation on fragments.
             */
            pageId == FragmentNavigationEntry.GoBackPageId -> processNavigation(request.asFragmentEntry())
            else -> processNavigation(request.asFragmentEntry())
            /* LoginFragmentGetter.isHandled(pageId) -> {
               processNavigation(request.createActivityNavigationEntry())
             }
             else -> {
               processNavigation(request.asFragmentEntry())
             }*/
        }
    }

    protected fun processNavigation(entry: NavigationEntry?) {
        entry ?: return
        when (entry) {
            is ActivityNavigationEntry -> {
                activity.navigate(entry)
            }
            is FragmentNavigationEntry -> {
                /**
                 * Setting isAddToStack to false if currentFragment is null,
                 * This prevents crash for on first fragment loaded on activity
                 */
                val navEntry = if (navigationHelper.currentFragment == null) {
                    entry.copy(isAddToStack = false)
                } else entry
                navigator.gotoPage(navEntry)
            }
        }
    }

    private fun AppCompatActivity.navigate(entry: ActivityNavigationEntry?) {
        entry ?: return
        val intent = Intent(this, entry.activity).apply {
            putExtras(entry.bundle)
        }
        val requestCode = entry.requestCode
        if (requestCode != null) {
            startActivityForResult(intent, requestCode)
        } else {
            startActivity(intent)
        }
    }

    override fun openUrlInWebView(url: String) {
        val bundle = Bundle().apply {
            putString(MOSLConstants.URL_PATH, url)
        }
        openPage(NAVIGATE_TO_SALES_BROCHURE, bundle, true)
    }

    override fun openUrlInAppCrome(url: String) {

        try {
            val intent = CustomTabsIntent.Builder()
                .build()
            intent.launchUrl(activity, Uri.parse(url))
        } catch (e: Exception) {
            openUrlInWebView(url)
        }
    }

    override fun openUrlInBrowser(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            activity.startActivity(intent)
        } catch (e: Exception) {
            openUrlInWebView(url)
        }
    }


    override fun openBannerDialog(data: BannerScreenData) {
        val bundle = Bundle().apply {
            putParcelable(ARG_BANNER_SCREEN_DATA, data)
        }

        BannerFragment().apply {
            arguments = bundle
        }
            .show(activity.supportFragmentManager, "Banner Dialog Fragment")
    }

    override fun openPage(
        pageId: Int,
        addToStack: Boolean
    ) {
        openPage(FragmentNavigationEntry(pageId, Bundle.EMPTY, addToStack))
    }

    override fun pageNotAccessible() {
        TODO(
            "not implemented"
        ) //To change body of created functions use File | Settings | File Templates.
    }
}