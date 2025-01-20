package com.mosl.tabapp.saathiIndirect.dashboard

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.getbase.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.itemSelections
import com.mosl.app.core.data.error.ErrorUtils
import com.mosl.app.core.events.DataAction
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.app.core.rx.applyUiModel
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.dashboard.*
import com.mosl.mobile.user.login.user
import com.mosl.mobile.user.model.ProfileInfo
import com.mosl.tabapp.R
import com.mosl.tabapp.R.*
import com.mosl.tabapp.attendancedialog.AttendanceHelper
import com.mosl.tabapp.attendancedialog.UserLocationHelper
import com.mosl.tabapp.attendancedialog.verifyotp.VerifyDialogFragment
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.constants.MOSLConstants.MONTH_ON_MONTH_ACCOUNT_REVENUE
import com.mosl.tabapp.constants.MOSLConstants.PERFORMANCE_INCENTIVE
import com.mosl.tabapp.core.CommonActivity
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.dashboard.actionableinsight.ActionableInsightDialogFragment
import com.mosl.tabapp.dashboard.base.DashboardUiEvent.*
import com.mosl.tabapp.dashboard.base.DashboardUiEvent.ActionableCount
import com.mosl.tabapp.dashboard.clients.ClientsFragment
import com.mosl.tabapp.dashboard.leads.LeadsFragment
import com.mosl.tabapp.dashboard.productivity.ProductivityFragment
import com.mosl.tabapp.dashboard.snapshot.BusinessSnapshotFragment
import com.mosl.tabapp.dashboard.snapshot.advance.BusinessSnapshotAdvFragment
import com.mosl.tabapp.dashboard.target.TargetFragment
import com.mosl.tabapp.dashboard.teamtarget.TeamTargetFragment
import com.mosl.tabapp.dashboard.tradeyesterday.ClientsTradedYesterdayFragment
import com.mosl.tabapp.dashboard.trends.MonthOnMonthTrendsFragment
import com.mosl.tabapp.databinding.FragDashboardAufsbBinding
import com.mosl.tabapp.databinding.FragDashboardBinding
import com.mosl.tabapp.databinding.FragIndirectDashboardBinding
import com.mosl.tabapp.databinding.ItemTableRowBinding
import com.mosl.tabapp.model.TypeActionableFilterModel
import com.mosl.tabapp.model.dashboard.DashboardGroupModel
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.model.dashboard.DashboardSystemModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.mosl.tabapp.ui.MSFDialog.alertDialog
import com.mosl.tabapp.util.SpinnerAdapterFactory
import com.mosl.tabapp.util.downloadImage
import com.mosl.tabapp.util.initialEmail
import com.mosl.tabapp.util.initiateCall
import com.mosl.tabapp.webview.WebviewFragment
import com.mosl.user.data.login.MyZoneLoginRepositoryImpl
import com.mosl.user.data.login.VerifyAction
import com.mosl.user.domain.model.GenerateOTP
import com.mosl.user.login.MyZoneUserManagerImpl
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.subjects.PublishSubject
import org.json.JSONObject
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.set

class IndirectDashboardFragment : CommonFragment(), IndirectDashboardView{
    internal lateinit var binding: FragIndirectDashboardBinding
    @Inject
    lateinit var presenter: IndirectDashboardPresenter
    override fun getLayout() = R.layout.frag_indirect_dashboard
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragIndirectDashboardBinding.bind(view)
        hideAppTitle()
        setupViews()
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
    }

    private fun setupViews() {

    }
    override fun onLeadCreationClicked(): Observable<Unit> {
        return binding.leadcreation.clicks()
    }
    override fun onDestroyView() {
        presenter.stop()
        super.onDestroyView()
    }

}