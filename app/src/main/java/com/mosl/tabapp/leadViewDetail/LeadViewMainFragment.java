package com.mosl.tabapp.leadViewDetail;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.mosl.app.core.data.UiModel;
import com.mosl.app.core.data.UiModelHelper;
import com.mosl.saathi.api.model.clientdetail.ClientDetailItem;
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest;
import com.mosl.saathi.api.model.common.SaathiCommonResponse;
import com.mosl.saathi.api.model.lead.Contact;
import com.mosl.saathi.api.model.lead.LeadProductMappingItem;
import com.mosl.saathi.api.model.lead.LeadViewItem;
import com.mosl.saathi.api.model.lead.LeadViewRequest;
import com.mosl.saathi.api.model.leadviewmain.ClientImageRequest;
import com.mosl.saathi.api.model.leadviewmain.RegisterProspectOnlineRequest;
import com.mosl.saathi.api.model.leadviewmain.RegisterProspectOnlineResponse;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.LockedViewPager;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.ClientDetail;
import com.mosl.tabapp.model.NotificationData;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mosl.tabapp.ui.MOSLCommonActivity.getInputBody;

/**
 * Created by Vijay on 10/03/2017.
 */

public class LeadViewMainFragment extends MOSLCommonFragment implements View.OnClickListener {

    private View leadViewMainView;
    private LockedViewPager vpLeadView;
    private TabLayout indicatorLeadView;
    private LeadViewDetailFragment leadViewDetailFragment;
    private LeadViewActivitiesFragment leadViewActivitieFragment;
    private LeadProfilingFragment leadViewProfilingFragment;
    private LeadViewHistoryFragment leadViewHistoryFragment;
    private LeadViewInteractionFragment leadViewInteractionFragment;
    private ArrayList<Fragment> pageListLeadView = new ArrayList<>();
    private ImageView ivLead, ivLeadViewArrow;
    private TextView tvClientCode, tvLeadId, tvBranchCode, tvCurrentStage, tvCurrentStageSince,
        tvName;
    private String leadId;
    private String mobileNo, sourceName, prospectSourceName, prospectSourceDetail, intrestedIn;
    private String email;
    private String mUCID = "0";
    private Integer oaoUserId = 0;
    private String LeadOwner = "";
    private static final String SEARCH_TYPE_LEAD = "Lead";
    private static final String SEARCH_TYPE_CLIENT = "Client";
    private String searchType = SEARCH_TYPE_LEAD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        leadViewMainView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_leadview_main);

        try {
            leadId = this.getArguments().getString(LEAD_ID);
            if (this.getArguments().containsKey(UCID)) {
                mUCID = this.getArguments().getString(UCID, "0").equalsIgnoreCase("") ? "0"
                    : this.getArguments().getString(UCID, "0");
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

        vpLeadView = leadViewDetailView.findViewById(R.id.vpLeadView);
        vpLeadView.setPagingEnabled(false);
        indicatorLeadView = leadViewDetailView.findViewById(R.id.indicatorLeadView);

        ivLead = leadViewDetailView.findViewById(R.id.ivLead);
        if (!leadId.equalsIgnoreCase("0")) {
            CRMAppUtil.setImage(new byte[0], ivLead, getMainActivity());
        }
        ivLeadViewArrow = leadViewDetailView.findViewById(R.id.ivLeadViewArrow);
        ivLeadViewArrow.setVisibility(View.VISIBLE);
        tvClientCode = leadViewDetailView.findViewById(R.id.tvClientCode);
        tvName = leadViewDetailView.findViewById(R.id.tvName);
        tvLeadId = leadViewDetailView.findViewById(R.id.tvLeadId);
        tvBranchCode = leadViewDetailView.findViewById(R.id.tvBranchCode);
        tvCurrentStage = leadViewDetailView.findViewById(R.id.tvCurrentStage);
        tvCurrentStageSince = leadViewDetailView.findViewById(R.id.tvCurrentStageSince);

        ivLeadViewArrow.setOnClickListener(this);
        String[] headingClient = new String[5];
        if (leadId.equalsIgnoreCase("0")) {
            headingClient[0] = DETAILS;
            headingClient[1] = ACTIVITIES;
            headingClient[2] = MAPPING_HISTORY;
            headingClient[3] = MAPPING_INTERACTION;
            leadViewDetailFragment = LeadViewDetailFragment.newInstance(0, DETAILS,
                this.getArguments().getString(LEAD_ID), mUCID);
            leadViewActivitieFragment = LeadViewActivitiesFragment.newInstance(1, ACTIVITIES,
                this.getArguments().getString(LEAD_ID), mUCID);
            leadViewHistoryFragment = LeadViewHistoryFragment.newInstance(2, MAPPING_HISTORY,
                this.getArguments().getString(LEAD_ID), mUCID);
            leadViewInteractionFragment =
                LeadViewInteractionFragment.newInstance(3, MAPPING_INTERACTION,
                    this.getArguments().getString(LEAD_ID), mUCID);
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
            leadViewActivitieFragment =
                LeadViewActivitiesFragment.newInstance(1, ACTIVITIES, leadId, mUCID);
            leadViewProfilingFragment = LeadProfilingFragment.newInstance(2, PROFILING, leadId);
            leadViewHistoryFragment =
                LeadViewHistoryFragment.newInstance(3, MAPPING_HISTORY, leadId, mUCID);
            leadViewInteractionFragment =
                LeadViewInteractionFragment.newInstance(4, MAPPING_INTERACTION, leadId, mUCID);
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

        final LeadViewAdapter adapterClient =
            new LeadViewAdapter(getChildFragmentManager(), pageListLeadView, headingClient);
        vpLeadView.setAdapter(adapterClient);
        indicatorLeadView.setupWithViewPager(vpLeadView);

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
        }
    }

    private void setProfileImage(String ucid) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            ClientImageRequest request =
                new ClientImageRequest(ucid);
            Disposable subscribe = saathiApi.getClientImage(request)
                .compose(UiModelHelper.applyUiModel())
                .subscribe(this::renderClientImage);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderClientImage(UiModel<SaathiCommonResponse<String>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
            CRMAppUtil.setImage(new byte[0], ivLead, getMainActivity());
        }
        SaathiCommonResponse<String> data = uiModel.data;
        if (data != null) {
            String response = data.response;
            byte[] byt = Base64.decode(response, Base64.DEFAULT);
            if (byt != null) {
                CRMAppUtil.setImage(byt, ivLead, getMainActivity());
            } else {
                CRMAppUtil.setImage(new byte[0], ivLead,
                    getMainActivity());
            }
        }
    }

    private void getClientDetail(final String ucid) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            ClientDetailRequest request =
                new ClientDetailRequest(Integer.parseInt(ucid));
            Disposable subscribe = saathiApi.getClientDetail(request)
                .compose(UiModelHelper.applyUiModel())
                .subscribe(this::renderClientViewByCommand);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderClientViewByCommand(UiModel<ClientDetailItem> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }
        ClientDetailItem data = uiModel.data;
        if (data != null) {
            new ClientDetail(data);
            tvName.setText(ClientDetail.getName());
            setTitle(ClientDetail.getName());
            tvLeadId.setText(ClientDetail.getLeadId());
            tvCurrentStage.setText(ClientDetail.getCurrentStage());
            if (!ClientDetail.getCurrentStageSince().isEmpty()) {
                tvCurrentStageSince.setText(
                    ClientDetail.getCurrentStageSince() + " Days");
            }
            tvClientCode.setText(ClientDetail.getClientCode());
            tvBranchCode.setText(ClientDetail.getBranchCode());
            mobileNo = ClientDetail.getMobile();
            oaoUserId = ClientDetail.getOaoUserId();
            searchType = SEARCH_TYPE_CLIENT;
        }
    }

    private void getLeadByIdFromServer(CharSequence strLeadId) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            LeadViewRequest request =
                new LeadViewRequest(String.valueOf(strLeadId));
            Disposable subscribe = saathiApi.getLeadView(request)
                .compose(UiModelHelper.applyUiModel())
                .subscribe(this::renderLeadView);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderLeadView(UiModel<LeadViewItem> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }
        LeadViewItem data = uiModel.data;
        if (data != null) {
            String leadId = stringOrEmpty(String.valueOf(data.leadID));
            String branchCode = stringOrEmpty(data.branchcode);
            Contact contact = data.contact;
            LeadOwner = stringOrEmpty(data.leadOwner);
            sourceName = stringOrEmpty(data.sourceName);
            prospectSourceName = stringOrEmpty(data.prospectSource);
            prospectSourceDetail = stringOrEmpty(String.valueOf(data.leadSourceDetailsID));
            oaoUserId = data.oAOUserid;
            List<LeadProductMappingItem> productMapping = data.leadProductMapping;
            if (productMapping.size() != 0) {
                for (int i = 0; i < productMapping.size(); i++) {
                    LeadProductMappingItem item = productMapping.get(i);
                    String clientCode = stringOrEmpty(item.clientCode);
                    String stageName = stringOrEmpty(item.stageName);
                    String isDefault = stringOrEmpty(String.valueOf(item.isDefault));
                    String pendingDays = stringOrEmpty(String.valueOf(item.pendingDays));
                    intrestedIn = stringOrEmpty(String.valueOf(item.interestedIn));
                    if (isDefault.equalsIgnoreCase("true")) {
                        tvClientCode.setText(clientCode);
                        tvCurrentStage.setText(stageName);
                        tvCurrentStageSince.setText(
                            pendingDays + " Days");
                    }
                }
            }

            if (contact != null) {
                mUCID = stringOrEmpty(contact.uCID);
                mobileNo = stringOrEmpty(contact.mobile);
                email = stringOrEmpty(contact.emailId);
                String name = stringOrEmpty(contact.name);
                setTitle(name);
            }
            if (!mUCID.equalsIgnoreCase("0")) {
                searchType = SEARCH_TYPE_CLIENT;
            } else {
                searchType = SEARCH_TYPE_LEAD;
            }
            tvLeadId.setText(leadId);
            tvBranchCode.setText(branchCode);
        }
    }

    private void leadViewDialog() {
        final Dialog dialog = new Dialog(getMainActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_leadview);
        dialog.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);

        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        ImageView ivCreateMeeting = dialog.findViewById(R.id.ivCreateMeeting);
        ImageView ivSetAppointment = dialog.findViewById(R.id.ivSetAppointment);
        ImageView ivCall = dialog.findViewById(R.id.ivCall);
        ImageView ivSms = dialog.findViewById(R.id.ivSms);
        ImageView ivOpenAccount = dialog.findViewById(R.id.ivOpenAccount);
        ImageView ivOaoForm = dialog.findViewById(R.id.ivOaoForm);
        ImageView ivSuggestMe = dialog.findViewById(R.id.ivSuggestMe);
        if (AccountDetails.isAufsb()) {
            ivCreateMeeting.setVisibility(View.GONE);
            ivSetAppointment.setVisibility(View.GONE);
            ivSuggestMe.setVisibility(View.GONE);
        }
        if (AccountDetails.isSmtEnabled()) {
            ivSuggestMe.setVisibility(View.VISIBLE);
        } else {
            ivSuggestMe.setVisibility(View.GONE);
        }
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
                dialog.dismiss();
                Map inpuMap = new HashMap();
                inpuMap.put(USERID, oaoUserId.toString());
                if (AccountDetails.isNewEkyc()) {
                    HashMap map = new HashMap();
                    map.put(NEW_USERID, oaoUserId.toString());
                    map.put(BRANCHEMPCODE, AccountDetails.getEmployeeCode());
                    map.put(COMMANDTYPE, "RESUME");
                    map.put(APPLICATIONTYPE, "CRM");
                    map.put(OSTYPE, "ANDROID");
                    map.put(APPLOGINID, AccountDetails.getEmployeeCode());
                    map.put(TOKENKEY, AccountDetails.getToken());
                    map.put(ISAUFSB, AccountDetails.isAufsb());
                    gotoNewEkyc(getInputBody(map));
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
                inpuMap.put(ISAUFSB, AccountDetails.isAufsb());
                if (AccountDetails.isNewEkyc()) {
                    gotoEkycPage(CALL_CONVERT_EKYC, inpuMap);
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
                    Toast.makeText(getMainActivity(), "Contact number not available",
                        Toast.LENGTH_LONG).show();
                } else {
                    MSFDialog.alertDialog(getMainActivity(), 0,
                        getResources().getString(R.string.motilal_oswal), "Do you want to call?",
                        "Yes", "No", true, new MSFDialog.DialogListener() {
                            @Override
                            public void alertDialogAction(MSFDialog.Action action, Object... data) {
                                if (action == MSFDialog.Action.OK) {
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                    callIntent.setData(Uri.parse("tel:" + mobileNo));
                                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
                    Intent smsIntent = new Intent("android.intent.action.VIEW");
                    smsIntent.setData(Uri.parse("smsto:" + mobileNo));
                    smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getMainActivity().startActivity(smsIntent);
                } else {
                    Toast.makeText(getMainActivity(), "Contact number not available",
                        Toast.LENGTH_LONG).show();
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
        ivSuggestMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSuggestMe();
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
        tvName.setText(name.toUpperCase());
        hideAppTitle();
    }

    private void getSuggestMe() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            RegisterProspectOnlineRequest request =
                new RegisterProspectOnlineRequest(this.getArguments().getString(LEAD_ID), mUCID);
            Disposable subscribe = saathiApi.getRegisterProspectOnline(request)
                .compose(UiModelHelper.applyUiModel())
                .subscribe(this::renderSuggestMe);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderSuggestMe(
        UiModel<RegisterProspectOnlineResponse> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }
        RegisterProspectOnlineResponse data = uiModel.data;
        if (data != null) {
            String encryptedUrl = stringOrEmpty(data.encryptedURL);
            Bundle bundle = new Bundle();
            bundle.putString(URL_PATH, encryptedUrl);

            navigateTo(NAV_TO_WEB_VIEW, bundle, getFragmentManager(),
                true);
        }
    }
}