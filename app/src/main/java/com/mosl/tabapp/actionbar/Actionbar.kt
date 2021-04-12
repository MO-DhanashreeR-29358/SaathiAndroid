package com.mosl.tabapp.actionbar

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.jakewharton.rxbinding3.widget.textChanges
import com.mosl.app.core.rx.applyUiModel
import com.mosl.app.core.utils.hideKeyBoard
import com.mosl.app.core.utils.obtain
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.CharSequenceToString
import com.mosl.commons.rx.MapStringIsBlank
import com.mosl.mobile.user.login.UserManager
import com.mosl.navigator.AppTitle
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.lead.request.SearchRequest
import com.mosl.saathi.api.model.search.SearchResponse
import com.mosl.tabapp.AppComponent
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants.*
import com.mosl.tabapp.core.CommonActivity.Companion.SEARCH_TYPE_CLIENT_HINT
import com.mosl.tabapp.core.CommonActivity.Companion.SEARCH_TYPE_LEAD_HINT
import com.mosl.tabapp.model.SearchModelNew
import com.mosl.tabapp.util.*
import com.mosl.tabapp.util.EMPLOYEE_CODE
import com.mosl.tabapp.util.INTERESTEDIN
import com.mosl.tabapp.util.MOBILE
import com.mosl.tabapp.util.NAME
import com.mosl.tabapp.util.PROFILE_ID
import com.mosl.tabapp.util.SEARCH_TYPE_DASHBOARD_HINT
import com.mosl.tabapp.util.adapters.AutoCompleteAdapter
import com.mosl.tabapp.util.adapters.AutoCompleteAdapter.Companion.NO_RECORDS_FOUND
import com.mosl.tabapp.util.adapters.AutoCompleteAdapter.SearchResultClickListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.json.JSONObject
import timber.log.Timber
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class Actionbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), SearchResultClickListener {

    @Inject
    lateinit var saathiApi: SaathiApi

    @Inject
    lateinit var userManager: UserManager
    internal lateinit var mTitleView: TextView
    internal lateinit var mHomeBtn: ImageView
    internal lateinit var srchImg: ImageView
    internal lateinit var btnNotification: ImageView
    internal lateinit var searchBoxView: AutoCompleteTextView
    internal lateinit var llSearch: LinearLayout
    internal lateinit var autoCompleteAdapter: AutoCompleteAdapter
    internal lateinit var pbLoadingIndicator: ProgressBar
    internal var disposable: CompositeDisposable

    init {
        context.applicationContext.obtain<AppComponent>(AppComponent::class.java)
            .inject(this)
        LayoutInflater.from(context)
            .inflate(R.layout.crm_actionbar, this, true)
        disposable = CompositeDisposable()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mHomeBtn = findViewById(R.id.btnMenu)
        srchImg = findViewById(R.id.srchImg)
        btnNotification = findViewById(R.id.btnNotification)
        searchBoxView = findViewById(R.id.searchText)

        mTitleView = findViewById(R.id.appTitle)
        pbLoadingIndicator = findViewById(R.id.pbLoadingIndicator)
        searchBoxView.setDropDownBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        llSearch = findViewById(R.id.llSearch)

        autoCompleteAdapter = AutoCompleteAdapter(context, userManager, this)
        searchBoxView.setAdapter(autoCompleteAdapter)

        val searchObs = searchBoxView.textChanges()
            .filter { t -> t.isNotEmpty() && t.isNotBlank() }
            .debounce(500, MILLISECONDS, AndroidSchedulers.mainThread())
            .map(CharSequenceToString)
            .distinctUntilChanged()
            .share()

        searchObs.map(MapStringIsBlank)
            .subscribe(this::toggleSearchState)
            .addTo(disposable)

        searchObs.filter { it.length > 2 }
            .switchMap {
                saathiApi.getSearchData(SearchRequest(getSearchText(), getSearchType()))
                    .map { t: List<SearchResponse> ->
                        if (t.isNotEmpty()) {
                            t.map { searchResponse: SearchResponse ->
                                val jsonObject = JSONObject(searchResponse.metaData)
                                if (getSearchHint().equals(SEARCH_TYPE_DASHBOARD_HINT)) {
                                    val empCode = jsonObject.getString(EMPLOYEE_CODE)
                                    val name = jsonObject.getString(NAME)
                                    val businessProfile = jsonObject.getString(BUSINESS_PROFILE)
                                    val businessProfileId = jsonObject.getString(PROFILE_ID)
                                    val productId = jsonObject.getString(PRODUCT_ID)
                                    SearchModelNew(
                                        getSearchHint(),
                                        empCode,
                                        name,
                                        businessProfileId,
                                        businessProfile,
                                        productId
                                    )
                                } else {
                                    val ucid = jsonObject.getString(UCID)
                                    val leadid = jsonObject.getString(LEADID)
                                    val name = jsonObject.getString(NAME)
                                    val mobile = jsonObject.getString(MOBILE)
                                    val email = jsonObject.getString(EMAIL)
                                    val sourceName = jsonObject.optString(SOURCENAME) ?: ""
                                    val prospectSourceName =
                                        jsonObject.optString(PROSPECTSOURCENAME) ?: ""
                                    val prospectSourceDetails =
                                        jsonObject.optString(PROSPECTSOURCEDETAILS) ?: ""
                                    val interestedIn = jsonObject.optString(INTERESTEDIN) ?: ""
                                    val leadOwner = jsonObject.optString(LEAD_OWNER) ?: ""
                                    val oaoUserId = jsonObject.getInt(OAOUSERID)
                                    val diyCode = jsonObject.getInt(DIY_CODE)
                                    val clientCode = jsonObject.getString(CLIENTCODE) ?: ""
                                    val branchCode = jsonObject.getString(BRANCHCODE) ?: ""
                                    val createMeetingId = jsonObject.optInt(CREATE_MEETING_ID)
                                    SearchModelNew(
                                        getSearchHint(),
                                        leadid,
                                        name,
                                        mobile,
                                        email,
                                        ucid,
                                        leadOwner,
                                        oaoUserId,
                                        sourceName,
                                        prospectSourceName,
                                        prospectSourceDetails,
                                        interestedIn,
                                        clientCode,
                                        createMeetingId,
                                        diyCode,
                                        branchCode
                                    )
                                }
                            }
                        } else {
                            listOf(
                                SearchModelNew(
                                    "", "", NO_RECORDS_FOUND, "", "",
                                    ""
                                )
                            )
                        }
                    }
                    .compose(applyUiModel())
            }
            .subscribe(this::renderSearchResult)
            .addTo(disposable)
    }

    fun getSearchType(): String {
        return when (getSearchHint()) {
            SEARCH_TYPE_LEAD_HINT -> SEARCH_TYPE_LEAD
            SEARCH_TYPE_CLIENT_HINT -> SEARCH_TYPE_CLIENT
            else -> SEARCH_TYPE_EMP
        }
    }

    override fun onSearchResultClick() {
        hideKeyBoard(searchBoxView)
        setSearchText("")
    }

    fun setSearchText(text: String) {
        searchBoxView.setText(text)
    }

    fun renderSearchResult(
        uiModel: UiModel<List<SearchModelNew>>
    ) {
        toggleSearchProgress(uiModel.inProgress)

        uiModel.onFailure {
            Timber.d(uiModel.error)
            autoCompleteAdapter.notifyDataSetInvalidated()
        }

        uiModel.onSuccess {
            autoCompleteAdapter.setResultList(it)
            if (it.isNotEmpty()) {
                searchBoxView.showDropDown()
            }
        }
    }

    fun toggleSearchProgress(show: Boolean) {
        if (show) {
            pbLoadingIndicator.visibility = View.VISIBLE
        } else {
            pbLoadingIndicator.visibility = View.GONE
        }
    }

    fun toggleSearchState(queryEmpty: Boolean) {
        if (queryEmpty) {
            autoCompleteAdapter.setResultList(emptyList())
        }
    }

    fun getSearchText(): String {
        return searchBoxView.text.toString()
            .trim()
    }

    fun setHomeAction(onClickAction: OnClickListener) {
        mHomeBtn.setOnClickListener(onClickAction)
        mHomeBtn.visibility = View.VISIBLE
    }

    fun setNotification(onClickAction: OnClickListener) {
        btnNotification.setOnClickListener(onClickAction)
    }

    fun setSearchHint(hint: CharSequence) {
        searchBoxView.setText("")
        searchBoxView.hint = hint
    }

    fun getSearchHint(): String {
        return searchBoxView.hint.toString()
    }

    fun setTitleAndOverflow(appTitle: AppTitle) {
        if (appTitle.hasTitle()) {
            if (appTitle.hasStringRes()) {
                mTitleView.setText(appTitle.title)
            } else {
                mTitleView.text = appTitle.titleStr
            }
            mTitleView.visibility = View.VISIBLE
        } else {
            hideTitleBar()
        }
    }

    fun hideTitleBar() {
        mTitleView.text = ""
        mTitleView.visibility = View.GONE
        mTitleView.visibility = View.GONE
    }

    override fun onDetachedFromWindow() {
        disposable.clear()
        super.onDetachedFromWindow()
    }

    companion object {
        const val SEARCH_TYPE_EMP = "Employee"
        const val SEARCH_TYPE_LEAD = "Lead"
        const val SEARCH_TYPE_CLIENT = "Client"
    }

}