package com.mosl.tabapp.leadViewDetail;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.ui.BranchResumeApplicationActivity;
import com.mosl.tabapp.Adapter.LeadViewAdapter;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.common.LockedViewPager;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.ClientDetail;
import com.mosl.tabapp.model.NotificationData;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mosl.tabapp.ui.MOSLCommonActivity.getInputBody;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by Vijay on 10/03/2017.
 */

public class LeadViewMainFragment extends MOSLCommonFragment implements View.OnClickListener {

    private View leadViewMainView;
    private LockedViewPager vpLeadView;
    private TabLayout indicatorLeadView;
    private LinearLayout mTabsLinearLayoutTarget;
    private LeadViewDetailFragment leadViewDetailFragment;
    private LeadViewActivitiesFragment leadViewActivitieFragment;
    private LeadProfilingFragment leadViewProfilingFragment;
    private LeadViewHistoryFragment leadViewHistoryFragment;
    private LeadViewInteractionFragment leadViewInteractionFragment;
    private ArrayList<Fragment> pageListLeadView = new ArrayList<>();
    private ImageView ivLead, ivLeadViewArrow;
    private TextView tvClientCode, tvLeadId, tvBranchCode, tvCurrentStage, tvCurrentStageSince, tvName, textView;
    //   private TextView tvLeadNameHead;
    private String leadId;
    private String mobileNo, sourceName, prospectSourceName, prospectSourceDetail, intrestedIn;
    private String email;
    private String mUCID = "0";
    private Integer oaoUserId = 0;
    private String LeadOwner;
    private static final String SEARCH_TYPE_LEAD = "Lead";
    private static final String SEARCH_TYPE_CLIENT = "Client";
    private static final String SEARCH_TYPE_EMP = "Employee";
    private String searchType = SEARCH_TYPE_LEAD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        leadViewMainView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_leadview_main);

        try {
            leadId = this.getArguments().getString(LEAD_ID);
            // mobileNo = this.getArguments().getString(MOBILE);
            // hideAppTitle();
            if (this.getArguments().containsKey(UCID)) {
                mUCID = this.getArguments().getString(UCID, "0").equalsIgnoreCase("") ? "0" : this.getArguments().getString(UCID, "0");
            }
            if (!mUCID.equalsIgnoreCase("0")) {
                getClientDetail(mUCID);
                setProfileImage(mUCID);

            } else {
                getLeadByIdFromServer(leadId);
            }
            setUpView(leadViewMainView);
            setUserVisibleHint(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return leadViewMainView;
    }

    private void setUpView(View leadViewDetailView) {


        vpLeadView = (LockedViewPager) leadViewDetailView.findViewById(R.id.vpLeadView);
        vpLeadView.setPagingEnabled(false);
        indicatorLeadView = (TabLayout) leadViewDetailView.findViewById(R.id.indicatorLeadView);

        ivLead = (ImageView) leadViewDetailView.findViewById(R.id.ivLead);
        if (!leadId.equalsIgnoreCase("0")) {
            CRMAppUtil.setImage(new byte[0], ivLead, getMainActivity());
        }
        ivLeadViewArrow = (ImageView) leadViewDetailView.findViewById(R.id.ivLeadViewArrow);
        ivLeadViewArrow.setVisibility(View.VISIBLE);
        tvClientCode = (TextView) leadViewDetailView.findViewById(R.id.tvClientCode);
        tvName = (TextView) leadViewDetailView.findViewById(R.id.tvName);
        tvLeadId = (TextView) leadViewDetailView.findViewById(R.id.tvLeadId);
        tvBranchCode = (TextView) leadViewDetailView.findViewById(R.id.tvBranchCode);
        tvCurrentStage = (TextView) leadViewDetailView.findViewById(R.id.tvCurrentStage);
        tvCurrentStageSince = (TextView) leadViewDetailView.findViewById(R.id.tvCurrentStageSince);

        ivLeadViewArrow.setOnClickListener(this);
        String[] headingClient = new String[5];
        if (leadId.equalsIgnoreCase("0")) {
            headingClient[0] = DETAILS;
            headingClient[1] = ACTIVITIES;
            headingClient[2] = MAPPING_HISTORY;
            headingClient[3] = MAPPING_INTERACTION;
            leadViewDetailFragment = LeadViewDetailFragment.newInstance(0, DETAILS, this.getArguments().getString(LEAD_ID), mUCID);
            leadViewActivitieFragment = LeadViewActivitiesFragment.newInstance(1, ACTIVITIES, this.getArguments().getString(LEAD_ID), mUCID);
            leadViewHistoryFragment = LeadViewHistoryFragment.newInstance(2, MAPPING_HISTORY, this.getArguments().getString(LEAD_ID), mUCID);
            leadViewInteractionFragment = LeadViewInteractionFragment.newInstance(3, MAPPING_INTERACTION, this.getArguments().getString(LEAD_ID), mUCID);
            leadViewDetailFragment.setViewPager(vpLeadView);
            leadViewActivitieFragment.setViewPager(vpLeadView);
            leadViewHistoryFragment.setViewPager(vpLeadView);
            leadViewInteractionFragment.setViewPager(vpLeadView);
            pageListLeadView.add(leadViewDetailFragment);
            pageListLeadView.add(leadViewActivitieFragment);
            pageListLeadView.add(leadViewHistoryFragment);
            pageListLeadView.add(leadViewInteractionFragment);
            indicatorLeadView.setTabMode(TabLayout.MODE_FIXED);
        } else {
            headingClient[0] = DETAILS;
            headingClient[1] = ACTIVITIES;
            headingClient[2] = PROFILING;
            headingClient[3] = MAPPING_HISTORY;
            headingClient[4] = MAPPING_INTERACTION;
            leadViewDetailFragment = LeadViewDetailFragment.newInstance(0, DETAILS, leadId, mUCID);
            leadViewActivitieFragment = LeadViewActivitiesFragment.newInstance(1, ACTIVITIES, leadId, mUCID);
            leadViewProfilingFragment = LeadProfilingFragment.newInstance(2, PROFILING, leadId);
            leadViewHistoryFragment = LeadViewHistoryFragment.newInstance(3, MAPPING_HISTORY, leadId, mUCID);
            leadViewInteractionFragment = LeadViewInteractionFragment.newInstance(4, MAPPING_INTERACTION, leadId, mUCID);
            leadViewDetailFragment.setViewPager(vpLeadView);
            leadViewActivitieFragment.setViewPager(vpLeadView);
            leadViewProfilingFragment.setViewPager(vpLeadView);
            leadViewHistoryFragment.setViewPager(vpLeadView);
            leadViewInteractionFragment.setViewPager(vpLeadView);
            pageListLeadView.add(leadViewDetailFragment);
            pageListLeadView.add(leadViewActivitieFragment);
            pageListLeadView.add(leadViewProfilingFragment);
            pageListLeadView.add(leadViewHistoryFragment);
            pageListLeadView.add(leadViewInteractionFragment);
            indicatorLeadView.setTabMode(TabLayout.MODE_SCROLLABLE);
        }


        final LeadViewAdapter adapterClient = new LeadViewAdapter(getChildFragmentManager(), pageListLeadView, headingClient);
        vpLeadView.setAdapter(adapterClient);
        indicatorLeadView.setupWithViewPager(vpLeadView);

        mTabsLinearLayoutTarget = ((LinearLayout) indicatorLeadView.getChildAt(0));


        //Go to section
        NotificationData section = this.getArguments().getParcelable(NOTIFICATION_DATA);
        //TODO  change meeting to ACTIVITIES
        if (section == null) {
            vpLeadView.setCurrentItem(0);
        } else {
            if (section.getSection().equalsIgnoreCase("ACTIVITIES")) {
                vpLeadView.setCurrentItem(1);
            } else {
                vpLeadView.setCurrentItem(0);
            }
        }


    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && leadViewMainView != null) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        /*    Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
            Crashlytics.logException(e);*/
        }
    }

    private void setProfileImage(String ucid) {


        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            Map inputMap = new HashMap<>();
            String serviceMethod = "Client/GetClientImage";
            String tmpReq = String.format(ID_OBJECT, ucid);
            //  String serviceMethod = "Client/GetClientImage/" + ucid;
            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), tmpReq, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here

                        try {

                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    String objDetail = response.getString(RESPONSE);

                                    byte[] byt = Base64.decode(objDetail, Base64.DEFAULT);
                                    if (byt != null) {
                                        CRMAppUtil.setImage(byt, ivLead, getMainActivity());

                                    } else {
                                        CRMAppUtil.setImage(new byte[0], ivLead, getMainActivity());
                                    }


                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                    CRMAppUtil.setImage(new byte[0], ivLead, getMainActivity());
                                }

                            } else {
                                hideProgress();
                                CRMAppUtil.setImage(new byte[0], ivLead, getMainActivity());
                            }
                        } catch (Exception e) {
                            CRMAppUtil.setImage(new byte[0], ivLead, getMainActivity());
                            e.printStackTrace();
                            hideProgress();
                            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        hideProgress();
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        hideProgress();
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
                                    @Override
                                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                                        if (action == MSFDialog.Action.OK) {
                                            Intent i = new Intent(getMainActivity(), LoginActivity.class);
                                            startActivity(i);
                                            getActivity().finish();
                                        }
                                    }
                                });
                                return;
                            } else {
                                CRMAppUtil.setImage(new byte[0], ivLead, getMainActivity());
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);
        }

    }


    private void getClientDetail(final String ucid) {

        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            Map inputMap = new HashMap<>();
            inputMap.put(UCID, ucid);
//            inputMap.put("Id", this.getArguments().getString(LEAD_ID));

            String serviceMethod = "Client/GetClientViewByCommand/";
            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here

                        try {

                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {
                                    JSONObject objDetail = response.getJSONObject(RESPONSE);

                                    new ClientDetail(objDetail);
                                    tvName.setText(ClientDetail.getName());
                                    setTitle(ClientDetail.getName());
                                    tvLeadId.setText(ClientDetail.getLeadId());
                                    tvCurrentStage.setText(ClientDetail.getCurrentStage());
                                    if (!ClientDetail.getCurrentStageSince().isEmpty()) {
                                        tvCurrentStageSince.setText(ClientDetail.getCurrentStageSince() + " Days");
                                    }
                                    tvClientCode.setText(ClientDetail.getClientCode());
                                    tvBranchCode.setText(ClientDetail.getBranchCode());
                                    mobileNo = ClientDetail.getMobile();
                                    oaoUserId = ClientDetail.getOaoUserId();
                                    searchType = SEARCH_TYPE_CLIENT;
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgress();
                            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        hideProgress();
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        hideProgress();
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
                                    @Override
                                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                                        if (action == MSFDialog.Action.OK) {
                                            Intent i = new Intent(getMainActivity(), LoginActivity.class);
                                            startActivity(i);
                                            getActivity().finish();
                                        }
                                    }
                                });
                                return;
                            } else {
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);
        }
    }


    private void getLeadByIdFromServer(CharSequence strLeadId) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {

            Map inputMap = new HashMap<>();
            // String serviceMethod = "Lead/GetLeadView/" + strLeadId;
            String serviceMethod = "Lead/GetLeadView";//" + mLeadId;
            String tmpReq = String.format(ID_OBJECT, strLeadId);
            showProgress();

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), tmpReq, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        //    arrInterested = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
//                                hideProgress();
                                if (status.equalsIgnoreCase("true")) {
                                    JSONObject Response_obj = response.getJSONObject(RESPONSE);
                                    String leadID = (Response_obj.isNull(LEADID) || Response_obj.getString(LEADID).equals("")) ? "" : Response_obj.getString(LEADID);
                                    String branchcode = (Response_obj.isNull(BRANCHCODE) || Response_obj.getString(BRANCHCODE).equals("")) ? "" : Response_obj.getString(BRANCHCODE);
                                    JSONObject contact_obj = Response_obj.getJSONObject(CONTACT);
                                    LeadOwner = (Response_obj.isNull(LEAD_OWNER) || Response_obj.getString(LEAD_OWNER).equals("")) ? "" : Response_obj.getString(LEAD_OWNER);
                                    sourceName = (Response_obj.isNull("SourceName") || Response_obj.getString("SourceName").equals("")) ? "" : Response_obj.getString("SourceName");
                                    prospectSourceName = (Response_obj.isNull("ProspectSource") || Response_obj.getString("ProspectSource").equals("")) ? "" : Response_obj.getString("ProspectSource");
                                    prospectSourceDetail = (Response_obj.isNull("LeadSourceDetailsID") || Response_obj.getString("LeadSourceDetailsID").equals("")) ? "" : Response_obj.getString("LeadSourceDetailsID");
                                    oaoUserId = Response_obj.has("OAOUserid") && !Response_obj.isNull("OAOUserid") ? Response_obj.getInt("OAOUserid") : 0;
                                    String Name = (contact_obj.isNull(NAME) || contact_obj.getString(NAME).equals("")) ? "" : contact_obj.getString(NAME);
                                    setTitle(Name);
                                    JSONArray leadProductMapping = Response_obj.getJSONArray(LEADPRODUCTMAPPING);
                                    if (leadProductMapping.length() != 0) {
                                        for (int leadProductMapping_i = 0; leadProductMapping_i < leadProductMapping.length(); leadProductMapping_i++) {
                                            JSONObject leadProductMapping_obj = leadProductMapping.getJSONObject(leadProductMapping_i);
                                            String clientCode = (leadProductMapping_obj.isNull(CLIENTCODE) || leadProductMapping_obj.getString(CLIENTCODE).equals("")) ? "" : leadProductMapping_obj.getString(CLIENTCODE);
                                            String StageName = (leadProductMapping_obj.isNull(STAGENAME) || leadProductMapping_obj.getString(STAGENAME).equals("")) ? "" : leadProductMapping_obj.getString(STAGENAME);
                                            String isDefault = (leadProductMapping_obj.isNull(IS_DEFAULT) || leadProductMapping_obj.getString(IS_DEFAULT).equals("")) ? "" : leadProductMapping_obj.getString(IS_DEFAULT);
                                            String pendingDays = (leadProductMapping_obj.isNull(PENDING_DAYS) || leadProductMapping_obj.getString(PENDING_DAYS).equals("")) ? "" : leadProductMapping_obj.getString(PENDING_DAYS);
                                            intrestedIn = (leadProductMapping_obj.isNull("InterestedIn") || leadProductMapping_obj.getString("InterestedIn").equals("")) ? "" : leadProductMapping_obj.getString("InterestedIn");
                                            if (isDefault.equalsIgnoreCase("true")) {
                                                tvClientCode.setText(clientCode);
                                                tvCurrentStage.setText(StageName);
                                                tvCurrentStageSince.setText(pendingDays + " Days");
                                            }

                                        }
                                    }
                                    if (Response_obj.has("contact")) {
                                        JSONObject contacObj = Response_obj.getJSONObject("contact");

                                        mUCID = (contacObj.isNull(UCID) || contacObj.getString(UCID).equals("")) ? "0" : contacObj.getString(UCID);
                                        mobileNo = (contacObj.isNull(MOBILE) || contacObj.getString(MOBILE).equals("")) ? "" : contacObj.getString(MOBILE);
                                        email = (contacObj.isNull("EmailId") || contacObj.getString("EmailId").equals("")) ? "" : contacObj.getString("EmailId");
                                    }
                                    if (!mUCID.equalsIgnoreCase("0")) {
                                        searchType = SEARCH_TYPE_CLIENT;
                                    } else {
                                        searchType = SEARCH_TYPE_LEAD;
                                    }
                                    // tvName.setText(Name);
                                    tvLeadId.setText(leadID);
                                    tvBranchCode.setText(branchcode);
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }
                            } else {
                                hideProgress();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
//                        hideProgress();
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
                                    @Override
                                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                                        if (action == MSFDialog.Action.OK) {
                                            Intent i = new Intent(getMainActivity(), LoginActivity.class);
                                            startActivity(i);
                                            getActivity().finish();
                                        }
                                    }
                                });
                                return;
                            } else {
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                // Log.e(TAG, e.toString());
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                hideProgress();
            }
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);
        }
    }

    private void leadViewDialog() {
        final Dialog dialog = new Dialog(getMainActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_leadview);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);

        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        ImageView ivCreateMeeting = (ImageView) dialog.findViewById(R.id.ivCreateMeeting);
        ImageView ivSetAppointment = (ImageView) dialog.findViewById(R.id.ivSetAppointment);
        ImageView ivCall = (ImageView) dialog.findViewById(R.id.ivCall);
        ImageView ivSms = (ImageView) dialog.findViewById(R.id.ivSms);
        ImageView ivOpenAccount = (ImageView) dialog.findViewById(R.id.ivOpenAccount);
        ImageView ivOaoForm = (ImageView) dialog.findViewById(R.id.ivOaoForm);
        if (searchType.equalsIgnoreCase(SEARCH_TYPE_LEAD)) {
            if (oaoUserId != null && oaoUserId > 0) {
                ivOpenAccount.setVisibility(View.GONE);
                ivOaoForm.setVisibility(View.VISIBLE);
            } else {
                ivOpenAccount.setVisibility(View.VISIBLE);
                ivOaoForm.setVisibility(View.GONE);
            }
        } else if (searchType.equalsIgnoreCase(SEARCH_TYPE_CLIENT)) {
            if (oaoUserId != null && oaoUserId > 0) {
                ivOpenAccount.setVisibility(View.GONE);
                ivOaoForm.setVisibility(View.VISIBLE);
            } else {
                ivOpenAccount.setVisibility(View.GONE);
                ivOaoForm.setVisibility(View.GONE);
            }
        }

        ivOaoForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  Intent intent = new Intent(getMainActivity(), BranchResumeApplicationActivity.class);
                intent.putExtra("UserId", oaoUserId.toString());
                startActivity(intent);*/
                dialog.dismiss();
                Map inpuMap = new HashMap();
                inpuMap.put(USERID, oaoUserId.toString());
                if (AccountDetails.isNewEkyc()) {
                    HashMap<String, String> map = new HashMap();
                    map.put(NEW_USERID, oaoUserId.toString());
                    map.put(BRANCHEMPCODE, AccountDetails.getEmployeeCode());
                    map.put(COMMANDTYPE, "RESUME");
                    map.put(APPLICATIONTYPE, "CRM");
                    map.put(OSTYPE, "ANDROID");
                    map.put(APPLOGINID, AccountDetails.getEmployeeCode());
                    map.put(TOKENKEY, AccountDetails.getToken());
                    gotoNewEkyc(getInputBody(map));
                } else {
                    gotoEkycVersionCheck(CALL_PROSPECT_DETAILS, inpuMap);
                }

            }
        });
        ivOpenAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Map inpuMap = new HashMap();
                inpuMap.put(MOBILE, mobileNo);
                inpuMap.put(EMAIL, email);
                inpuMap.put(LEAD_OWNER, LeadOwner);
                inpuMap.put(SOURCENAME, sourceName);
                inpuMap.put(PROSPECT_SOURCENAME, prospectSourceName);
                inpuMap.put(PROSPECT_SOURCEDETAIL, prospectSourceDetail);
                inpuMap.put(INTRESTEDIN, intrestedIn);
                if (AccountDetails.isNewEkyc()) {
                    gotoEkycPage(CALL_CONVERT_EKYC, inpuMap);
                } else {
                    gotoEkycVersionCheck(CALL_CONVERT_EKYC, inpuMap);
                }

            }
        });
        ivCreateMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(LEAD_ID, getArguments().getString(LEAD_ID));
                bundle.putString(UCID, mUCID);
                navigateTo(NAV_TO_CREATE_MEETING, bundle, getFragmentManager(), true);
                dialog.dismiss();
            }
        });
        ivSetAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(LEAD_ID, getArguments().getString(LEAD_ID));
                bundle.putString(UCID, mUCID);
                navigateTo(NAV_TO_SET_APPOINTMENT, bundle, getFragmentManager(), true);
                dialog.dismiss();
            }
        });
        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CRMAppUtil.isBlank(mobileNo)) {
                    Toast.makeText(getMainActivity(), "Contact number not available", Toast.LENGTH_LONG).show();
                } else {
                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), "Do you want to call?", "Yes", "No", true, new MSFDialog.DialogListener() {
                        @Override
                        public void alertDialogAction(MSFDialog.Action action, Object... data) {
                            if (action == MSFDialog.Action.OK) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + mobileNo));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (ActivityCompat.checkSelfPermission(getMainActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    Toast.makeText(getMainActivity(), "No permission for call", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                getMainActivity().startActivity(callIntent);
                            }
                        }
                    });
                }
                dialog.dismiss();
            }
        });
        ivSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!CRMAppUtil.isBlank(mobileNo)) {
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                    smsIntent.setData(Uri.parse("smsto:" + mobileNo));
                    smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getMainActivity().startActivity(smsIntent);
                    //                            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    //                            smsIntent.setType("vnd.android-dir/mms-sms");
                    //                            smsIntent.putExtra("address",model.getContactNo() );
                    //                            smsIntent.putExtra("sms_body", model.getTitle().equalsIgnoreCase("birthday") ? MOSLConstants.BIRTHDAY_MESSAGE : MOSLConstants.ANNIVERSARY_MESSAGE);
                    //                            smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //                            getMainActivity().startActivity(smsIntent);
                } else {
                    Toast.makeText(getMainActivity(), "Contact number not available", Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivLeadViewArrow:
                leadViewDialog();
                break;
        }
    }

    private void setTitle(String name) {
        //   setAppTitle(getClass().toString(), name.toUpperCase());
        tvName.setText(name.toUpperCase());
        hideAppTitle();
    }

    private void getClientImageFromServer(CharSequence strLeadId) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            //TODO call progress bar
//            customProgress.setVisibility(View.VISIBLE);
            //   Map inputMap = new HashMap<>();

            //  String serviceMethod = "Client/GetClientImage/" + strLeadId;
            String serviceMethod = "Client/GetClientImage";
            String tmpReq = String.format(ID_OBJECT, strLeadId);
            showProgress();

            try {

                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), tmpReq, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        //    arrInterested = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
//                                hideProgress();
                                if (status.equalsIgnoreCase("true")) {
                                    JSONObject Response_obj = response.getJSONObject(RESPONSE);
                                    String leadID = (Response_obj.isNull(LEADID) || Response_obj.getString(LEADID).equals("")) ? "" : Response_obj.getString(LEADID);
                                    String branchcode = (Response_obj.isNull(BRANCHCODE) || Response_obj.getString(BRANCHCODE).equals("")) ? "" : Response_obj.getString(BRANCHCODE);
                                    JSONObject contact_obj = Response_obj.getJSONObject(CONTACT);
                                    String Name = (contact_obj.isNull(NAME) || contact_obj.getString(NAME).equals("")) ? "" : contact_obj.getString(NAME);
                                    setTitle(Name);
                                    JSONArray leadProductMapping = Response_obj.getJSONArray(LEADPRODUCTMAPPING);
                                    if (leadProductMapping.length() != 0) {
                                        for (int leadProductMapping_i = 0; leadProductMapping_i < leadProductMapping.length(); leadProductMapping_i++) {
                                            JSONObject leadProductMapping_obj = leadProductMapping.getJSONObject(leadProductMapping_i);
                                            String clientCode = (leadProductMapping_obj.isNull(CLIENTCODE) || leadProductMapping_obj.getString(CLIENTCODE).equals("")) ? "" : leadProductMapping_obj.getString(CLIENTCODE);
                                            String StageName = (leadProductMapping_obj.isNull(STAGENAME) || leadProductMapping_obj.getString(STAGENAME).equals("")) ? "" : leadProductMapping_obj.getString(STAGENAME);
                                            String isDefault = (leadProductMapping_obj.isNull(IS_DEFAULT) || leadProductMapping_obj.getString(IS_DEFAULT).equals("")) ? "" : leadProductMapping_obj.getString(IS_DEFAULT);
                                            String pendingDays = (leadProductMapping_obj.isNull(PENDING_DAYS) || leadProductMapping_obj.getString(PENDING_DAYS).equals("")) ? "" : leadProductMapping_obj.getString(PENDING_DAYS);
                                            if (isDefault.equalsIgnoreCase("true")) {
                                                tvClientCode.setText(clientCode);
                                                tvCurrentStage.setText(StageName);
                                                tvCurrentStageSince.setText(pendingDays + " Days");
                                            }

                                        }
                                    }
                                    if (Response_obj.has("contact")) {
                                        JSONObject contacObj = Response_obj.getJSONObject("contact");

                                        mUCID = (contacObj.isNull(UCID) || contacObj.getString(UCID).equals("")) ? "0" : contacObj.getString(UCID);
                                        mobileNo = (contacObj.isNull(MOBILE) || contacObj.getString(MOBILE).equals("")) ? "" : contacObj.getString(MOBILE);
                                    }
                                    // tvName.setText(Name);
                                    tvLeadId.setText(leadID);
                                    tvBranchCode.setText(branchcode);
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }
                            } else {
                                hideProgress();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
//                        hideProgress();
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
                                    @Override
                                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                                        if (action == MSFDialog.Action.OK) {
                                            Intent i = new Intent(getMainActivity(), LoginActivity.class);
                                            startActivity(i);
                                            getActivity().finish();
                                        }
                                    }
                                });
                                return;
                            } else {
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                // Log.e(TAG, e.toString());
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                hideProgress();
            }
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);
        }
    }

    private void ConvertEkycApi(final String mobile, final String email, final String branchEmplyeeCode
            , final String SourceName
            , final String ProspectSourceName
            , final String ProspectSourceDetails
            , final String InterestedIn) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            final Map inputMap = new HashMap<>();

            inputMap.put("Mobile", mobile);
            inputMap.put("Email", email);
            inputMap.put("BranchRm_EmpCode", branchEmplyeeCode);
            inputMap.put("SourceName", SourceName);
            inputMap.put("ProspectSourceName", ProspectSourceName);
            inputMap.put("ProspectSourceDetails", ProspectSourceDetails);
            inputMap.put("InterestedIn", InterestedIn);
            String serviceMethod = "Prospect/RegisterProspectOnline";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        try {

                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                String message = response.getString(MESSAGE);
                                if (status.equalsIgnoreCase("true")) {
                                    hideProgress();
                                    JSONArray jsonArray = response.getJSONArray("Response");
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String RegistrationAllowed = (jsonObject.isNull("RegistrationAllowed") || jsonObject.getString("RegistrationAllowed").equals("")) ? "" : jsonObject.getString("RegistrationAllowed");
                                    if (RegistrationAllowed.equalsIgnoreCase("true")) {
                                        String eKYC_UserID = (jsonObject.isNull("eKYC_UserID") || jsonObject.getString("eKYC_UserID").equals("")) ? "0" : jsonObject.getString("eKYC_UserID");
                                        try {
                                            oaoUserId = Integer.parseInt(eKYC_UserID);
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }
                                        Intent intent = new Intent(getMainActivity(), BranchResumeApplicationActivity.class);
                                        intent.putExtra("UserId", eKYC_UserID);
                                        startActivity(intent);
                                    } else {
                                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), message, "OK", true, null);
                                    }
                                    //TODO flag condition
                                    //   MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), remarks, "Ok", true, null);
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        hideProgress();
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        hideProgress();
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
                                    @Override
                                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                                        if (action == MSFDialog.Action.OK) {
                                            Intent i = new Intent(getMainActivity(), LoginActivity.class);
                                            startActivity(i);
                                            getActivity().finish();
                                        }
                                    }
                                });
                                return;
                            } else {
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);
        }
    }
}