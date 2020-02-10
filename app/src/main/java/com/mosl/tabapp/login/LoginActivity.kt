package com.mosl.tabapp.login

import android.content.Intent
import android.location.Location
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.editorActions
import com.mosl.app.core.error.getErrorOnFailure
import com.mosl.app.core.events.DataAction
import com.mosl.app.coreui.base.BaseActivity
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.model.ReportType
import com.mosl.mobile.user.model.User
import com.mosl.navigator.AppTitle
import com.mosl.navigator.AuthorizationState
import com.mosl.navigator.FragmentNavigationEntry
import com.mosl.navigator.Navigator
import com.mosl.tabapp.Permisssion.PermissionCheckHelper
import com.mosl.tabapp.R
import com.mosl.tabapp.common.FusedLocation
import com.mosl.tabapp.common.FusedLocation.LatLongDispatcher
import com.mosl.tabapp.core.CommonActivity
import com.mosl.tabapp.databinding.ActivityLoginNewBinding
import com.mosl.tabapp.helper.shortcuts.ShortcutHelper
import dagger.android.AndroidInjection
import io.reactivex.Observable
import javax.inject.Inject

class LoginActivity : BaseActivity(), Navigator, LoginView, LatLongDispatcher {

    lateinit var binding: ActivityLoginNewBinding
    @Inject
    lateinit var presenter: LoginPresenter
    @Inject
    lateinit var userManager: UserManager
    @Inject
    lateinit var shortcutHelper: ShortcutHelper

    @Inject
    lateinit var permissionCheckHelper: PermissionCheckHelper

    @Inject
    lateinit var fusedLocation: FusedLocation

    override fun getLayout(): Int {
        return R.layout.activity_login_new
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            permissionCheckHelper.requestAllPermissions()
        }
        fusedLocation.initDispatcher(this)
        binding = ActivityLoginNewBinding.bind(findViewById(R.id.login_root))
        presenter.start()
    }

    override fun onLoginClicked(): Observable<Unit> {
        return binding.btnLogin.clicks()
    }

    override fun onDoneClicked(): Observable<Int> {
        return binding.edPassword.editorActions { event -> event == EditorInfo.IME_ACTION_DONE }
    }

    override fun getEmpCode(): String {
        return binding.edEmpCode.text?.trim()
            .toString()
    }

    override fun getPassword(): String {
        return binding.edPassword.text.toString()
    }

    override fun renderLogin(uiModel: UiModel<User>) {
        toggleProgressBar(uiModel.inProgress)
//    Timber.d("User cache %s", userManager.user)

        uiModel.onFailure { error ->
            error.showOnFailurePopup()
        }
    }

    override fun renderReportsData(uiModel: UiModel<List<ReportType>>) {

        uiModel.onSuccess {
            if (it.size > 0) {
                for (reportTypeResponse in it) {
                    if (reportTypeResponse.isPreDefinedReports()) {
                        val childMenuList = reportTypeResponse.childMenuList
                        for (childMenu in childMenuList) {
                            if (childMenu.isBasicReports()) {
                                userManager.setReportMenuList(childMenu.ChildMenuList)
//                AccountDetails.setMenuReportList(childMenu.ChildMenuList)
                                openCommonActivity()
                            }
                        }
                    }
                }
            }
        }
        uiModel.onFailure {
            openCommonActivity()
        }
    }

    private fun openCommonActivity() {
        shortcutHelper.setLoggedInUserShortcuts()
        val intent = Intent(this, CommonActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun Throwable.showOnFailurePopup() {
        val errorOnFailure = this.getErrorOnFailure()
        alert(message = errorOnFailure.getResolvedMessage(applicationContext))
    }

    fun toggleProgressBar(inProgress: Boolean) {
        if (inProgress) {
            showProgress()
        } else {
            hideProgress()
        }
    }

    override fun isDataEmpty(): Boolean {
        TODO(
            "not implemented"
        ) //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDataAction(): Observable<DataAction> {
        TODO(
            "not implemented"
        ) //To change body of created functions use File | Settings | File Templates.
    }

    override fun authorizationDialog(listener: (AuthorizationState) -> Unit) {
        TODO(
            "not implemented"
        ) //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearTitles() {
        TODO(
            "not implemented"
        ) //To change body of created functions use File | Settings | File Templates.
    }

    override fun getContainerViewId(): Int {
        return 0
    }

    override fun getFragment(pageId: Int): Fragment? {
        TODO(
            "not implemented"
        ) //To change body of created functions use File | Settings | File Templates.
    }

    override fun gotoPage(fragmentEntry: FragmentNavigationEntry) {
        TODO(
            "not implemented"
        ) //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideAppTitle() {
        TODO(
            "not implemented"
        ) //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }

    override fun setAppTitle(
        page: String,
        appTitle: AppTitle?
    ) {
        TODO(
            "not implemented"
        ) //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onLatLongChanged(location: Location?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}