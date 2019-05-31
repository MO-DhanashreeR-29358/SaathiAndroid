package com.mosl.tabapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.mosl.app.core.exception.ServiceFailureException;
import com.mosl.app.core.rx.Dispatchers;
import com.mosl.app.core.rx.DispatchersKt;
import com.mosl.saathi.api.SaathiApi;
import com.mosl.saathi.data.MOSLResponseWrapper;
import com.mosl.tabapp.R;
import com.mosl.tabapp.calendar.CalendarAppointsListFragment;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.dashboard.DashboardFragment;
import com.mosl.tabapp.dashboard.DashboardHierarchyFragment;
import com.mosl.tabapp.dashboard.DashboardSecondLevelFragment;
import com.mosl.tabapp.leadViewDetail.CreateMeetingFragment;
import com.mosl.tabapp.leadViewDetail.DocumentFragment;
import com.mosl.tabapp.leadViewDetail.LeadActivitiesDetailFragment;
import com.mosl.tabapp.leadViewDetail.LeadViewMainFragment;
import com.mosl.tabapp.leadViewDetail.SetAppointmentFragment;
import com.mosl.tabapp.leadViewDetail.UpdateMeetingFragment;
import com.mosl.tabapp.leadcreation.LeadCreationFragment;
import com.mosl.tabapp.pendingApproval.PendingApprovalFragment;
import com.mosl.tabapp.pendingApproval.PendingForApprovalFragment;
import com.mosl.tabapp.smt.SMTFragment;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import io.reactivex.disposables.CompositeDisposable;
import java.util.Map;

public class MOSLCommonFragment extends Fragment implements MOSLConstants {
    public MOSLCommonFragment previousFragment;
    protected FragmentActivity context;
    private FrameLayout mLinearContainerView;
    public CompositeDisposable compositeDisposable;
    public final Dispatchers dispatchers = DispatchersKt.RxDispatchers;
    public final SaathiApi saathiApi = CRMWebServiceHandler.getSaathiApi();

    public MOSLCommonFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mCommonview = inflater.inflate(R.layout.common_base_layout, container, false);
        setupViews(mCommonview);
        compositeDisposable = new CompositeDisposable();
        //remove cache if login done

        return mCommonview;
    }

    private void setupViews(View mCommonview) {
        mLinearContainerView = (FrameLayout) mCommonview.findViewById(R.id.commonContainerView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (FragmentActivity) context;
    }

    public void setAppTitle(String fragment, String title) {
        ((MOSLCommonActivity) getMainActivity()).setAppTitle(fragment, title);
    }

    public void hideAppTitle() {
        ((MOSLCommonActivity) getMainActivity()).hideAppTitle();
    }

    public void hideAppTitle(String className) {
        ((MOSLCommonActivity) getMainActivity()).hideAppTitle(className);
    }

    public FragmentActivity getMainActivity() {
        if (context != null) {
            return context;
        } else {
            return super.requireActivity();
        }
    }

    protected View attachLayout(int layoutId) {
        View view = null;
        try {
            view = LayoutInflater.from(getMainActivity()).inflate(layoutId, null, false);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mLinearContainerView.addView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public void navigateTo(int id, Bundle bundle, FragmentManager fragmentManager,
                           final boolean addStack) {
        InputMethodManager inputManager =
                (InputMethodManager) getMainActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager.isAcceptingText() && getMainActivity().getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(
                    getMainActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        MOSLCommonFragment fragment = null;
        switch (id) {
            case SECOND_CLICK_DASHBORD:
                fragment = new DashboardSecondLevelFragment();
                fragment.setArguments(bundle);
                break;
            case NAV_TO_CREATE_MEETING:
                fragment = new CreateMeetingFragment();
                fragment.setArguments(bundle);
                break;
            case NAV_TO_CLIENT_MEETING:
                fragment = new CalendarAppointsListFragment();
                fragment.setArguments(bundle);
                break;
            case NAV_TO_SET_APPOINTMENT:
                fragment = new SetAppointmentFragment();
                fragment.setArguments(bundle);
                break;
            case NAV_TO_UPDATE_MEETING:
                fragment = new UpdateMeetingFragment();
                fragment.setArguments(bundle);
                break;
            case NAV_TO_LEAD_VIEW_MAIN:
                fragment = new LeadViewMainFragment();
                fragment.setArguments(bundle);
                break;
            case NAV_TO_SECOND_LEVEL_HIERARCHY_REPORT:
                fragment = new DashboardHierarchyFragment();
                fragment.setArguments(bundle);
                break;
            case NAV_TO_LEAD_CREATION:
                fragment = new LeadCreationFragment();
                fragment.setArguments(bundle);
                break;
            case NAV_TO_LEAD_ACTIVITIES_DETAIL:
                fragment = new LeadActivitiesDetailFragment();
                fragment.setArguments(bundle);
                break;
            case NAV_TO_LEAD_DOCUMENT:
                fragment = new DocumentFragment();
                fragment.setArguments(bundle);
                break;
            case NAV_TO_PENDING_APPROVAL:
                fragment = new PendingApprovalFragment();
                fragment.setArguments(bundle);
                break;
            case NAV_TO_PENDING_FOR_APPROVAL:
                fragment = new PendingForApprovalFragment();
                fragment.setArguments(bundle);
                break;
            case NAV_TO_ADVISORY_DASHBOARD:
                fragment = new DashboardFragment();
                fragment.setArguments(bundle);
                break;
            case NAV_TO_SECOND_LEVEL_REPORT:
                fragment = new SecondLevelReportFragment();
                fragment.setArguments(bundle);
                break;
            case NAV_TO_WEB_VIEW:
                fragment = new SMTFragment();
                fragment.setArguments(bundle);
                break;
        }
        if (fragment != null) {
            fragment.previousFragment = this;
            fragment.setArguments(bundle);
            addFragment(R.id.activityFrameLayout, fragment, addStack);
        }
    }

    private void addFragment(int containerViewId, Fragment fragment, boolean addStack) {
        ((MOSLCommonActivity) getMainActivity()).addFragment(containerViewId, fragment, addStack,
                false);
    }

    public void showProgress() {
        if (getMainActivity() != null) ((MOSLCommonActivity) getMainActivity()).showProgress();
    }

    public void hideProgress() {
        if (getMainActivity() != null) ((MOSLCommonActivity) getMainActivity()).hideProgress();
    }

    public void toggleProgress(boolean show) {
        if (show) {
            showProgress();
        } else {
            hideProgress();
        }
    }

    public void  showOnFailurePopup(Throwable t) {
        String message;
        if (t instanceof ServiceFailureException) {
            message = ((ServiceFailureException) t).errorMessage;
        } else {
            message = MOSLResponseWrapper.SERVER_ERROR_OCCURRED;
        }
        MSFDialog.alertDialog(getMainActivity(), 0,
            getResources().getString(R.string.motilal_oswal),
            message, "OK", true, null);
    }

    public void noInternetConnectionDialog() {
        MSFDialog.alertDialog(getMainActivity(), 0,
            getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
            null);
    }

    public String stringOrEmpty(@Nullable String input) {
        if (input != null) {
            return input;
        } else {
            return "";
        }
    }

    public void dashbordFragment() {
        ((MOSLCommonActivity) getMainActivity()).setupDefaultFragment(
                getMainActivity().getIntent());
    }

    public void gotoEkycVersionCheck(int gotoEkyc, Map hashMap) {
        ((MOSLCommonActivity) getMainActivity()).ekycVersionCheck(gotoEkyc, hashMap);
    }

    public void gotoEkycPage(int gotoEkyc, Map hashMap) {
        ((MOSLCommonActivity) getMainActivity()).gotoEkycPage(gotoEkyc, hashMap);
    }

    public void gotoNewEkyc(String data) {
        ((MOSLCommonActivity) getMainActivity()).newEkyc(data);
    }

    public void notificationFragment(String additionalParametrs) {
        ((MOSLCommonActivity) getMainActivity()).navigateToFromNotification(additionalParametrs);
    }

    public String getString(String key) {
        return "";
    }

    @Override
    public void onDetach() {
        if (previousFragment != null) previousFragment.onFragmentResult(null);
        super.onDetach();
    }

    public void onFragmentResult(Object data) {
        // configureActionBar(actionBar);
    }

    public void onFragmentResume() {
    }

    public void onFragmentPause() {

    }

    protected void toggleSectionExpansion(View parent, TextView icon) {
        if (parent.getVisibility() == View.VISIBLE) {
            parent.setVisibility(View.GONE);
            icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_plus, 0);
        } else {
            parent.setVisibility(View.VISIBLE);
            icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0);
        }
    }

    //openSectionExpansion
    protected void openSectionExpansion(View parent, TextView icon) {
        parent.setVisibility(View.VISIBLE);
        icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0);
    }

    //closedSectionExpansion
    protected void closedSectionExpansion(View parent, TextView icon) {
        parent.setVisibility(View.GONE);
        icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_plus, 0);
    }

    protected void setIcon(View parent, TextView icon) {
        if (parent.getVisibility() == View.VISIBLE) {
            icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0);
        } else {
            icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_plus, 0);
        }
    }

    //Enable field
    protected static void enableField(ViewGroup layout) {
        layout.setEnabled(true);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                enableField((ViewGroup) child);
            } else {
                child.setEnabled(true);
            }
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager in =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public Map getHeader() {
        return MOSLCommonActivity.getHeader(context);
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        compositeDisposable.dispose();
        super.onDestroyView();
    }
}

