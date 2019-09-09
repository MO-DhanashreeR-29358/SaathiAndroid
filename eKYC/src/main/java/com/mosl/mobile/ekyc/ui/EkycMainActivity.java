package com.mosl.mobile.ekyc.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.adapter.ApplyNowAdapter;
import com.mosl.mobile.ekyc.bankdetail.BankDetailsFormFragment;
import com.mosl.mobile.ekyc.bankdetail.FundTransferFragment;
import com.mosl.mobile.ekyc.common.CustomViewPager;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.directmodule.DirectResumeApplicationActivity;
import com.mosl.mobile.ekyc.model.CommonModel;
import com.mosl.mobile.ekyc.personaldetail.JointHolderAndNomineeFragment;
import com.mosl.mobile.ekyc.personaldetail.PersonalDetailFragment;
import com.mosl.mobile.ekyc.register.PANAdharRegistActivity;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import com.viewpagerindicator.TabPageIndicator;
import cz.msebera.android.httpclient.Header;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.mosl.mobile.ekyc.common.MOSLCommon.commonMsgDialog;
import static com.mosl.mobile.ekyc.common.MOSLCommon.getPrefs;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isNomineeTabApplicable;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setTabConstant;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppType;
import static com.mosl.mobile.ekyc.model.CommonModel.getBankTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getDematTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getEsignTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsAadharVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsCVLVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getPersonalTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getRegisteredName;
import static com.mosl.mobile.ekyc.model.CommonModel.getUploadTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.isAadhaarByPass;
import static com.mosl.mobile.ekyc.model.CommonModel.isDirectProspect;
import static com.mosl.mobile.ekyc.model.CommonModel.setUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.setUserType;

/**
 * Created by Shefali on 6/13/2016.
 */
public class EkycMainActivity extends FragmentActivity implements MOSLConstants {

    CustomViewPager vpApply;
    TabPageIndicator indicatorDetail;
    ArrayList<Fragment> pageListDetail = new ArrayList<>();
    LinearLayout llDetail, mTabsLinearLayoutDetail;
    Bundle bundle;
    int tabPos = 0;
    String userID;
    UploadDetailsFragment uploadDetailsFragment;
    BankDetailsFormFragment bankDetailsFormFragment;
    //    BankDetailsFragment bankDetailsFragment;
    PersonalDetailFragment personalDetailFragment;
    JointHolderAndNomineeFragment jointHolderAndNomineeFragment;
    EsignFragment esignFragment;
    ImageView ivExit, ivBack;
    TextView tvExitForm, tvAppLoginId, tvUserName;
    LinearLayout rlExitForm, rlLogout;
    public static LinearLayout customProgress;
    Dialog dialogReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            if (getAppType().equals(DIRECT) && !getIsCVLVerified()) {
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN
                                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            }

            setContentView(R.layout.activity_applynow);
            setPermisson(EkycMainActivity.this, getApplicationContext());
            setUpView();
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
//            Crashlytics.logException(e);
        }
    }

    private void setUpView() {
        setTabConstant();
        userID = getUserID();
        vpApply = (CustomViewPager) findViewById(R.id.vpApply);
        indicatorDetail = (TabPageIndicator) findViewById(R.id.indicatorDetail);
        llDetail = (LinearLayout) findViewById(R.id.llDetail);
        customProgress = (LinearLayout) findViewById(R.id.customProgress);
        rlExitForm = (LinearLayout) findViewById(R.id.rlExitForm);
        rlLogout = (LinearLayout) findViewById(R.id.rlLogout);

        ivExit = (ImageView) findViewById(R.id.ivExit);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        //  tvExitForm = (TextView) findViewById(R.id.tvExitForm);
        tvAppLoginId = (TextView) findViewById(R.id.tvAppLoginId);
        tvUserName = (TextView) findViewById(R.id.tvUserName);

//        tvExitForm.setText("Exit Form");
        if (getAppType().equals(DIRECT) || isDirectProspect()) {
            tvAppLoginId.setVisibility(View.GONE);
            tvUserName.setText(getRegisteredName().toUpperCase());
        } else {
            if (!getRegisteredName().equals("")) {
                tvUserName.setText(getRegisteredName().toUpperCase() + " -");
            }
            tvAppLoginId.setText(getAppLoginID());
        }
       /* if (!getRegisteredName().equals("")) {
            tvUserName.setText(getRegisteredName().toUpperCase() + " -");
        }*/
        if (getAppType().equals(CRM)) {
            rlLogout.setVisibility(View.VISIBLE);
        } else {
            rlLogout.setVisibility(View.GONE);
        }
        dialogReview = new Dialog(EkycMainActivity.this);
        dialogReview.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogReview.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogReview.setContentView(R.layout.frag_review_and_edit);
        dialogReview.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogReview.setCanceledOnTouchOutside(false);

        rlExitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formExit();

            }
        });
        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPrefs(EkycMainActivity.this).edit().putBoolean("AUTHENTICATED", false).apply();
                //Util.getPrefs(this).edit().putString("LOGIN_ID", null).apply();
                getPrefs(EkycMainActivity.this).edit().putString("APP_LOGIN_ID", null).apply();
                getPrefs(EkycMainActivity.this).edit().putString("PASSWORD", null).apply();
                getPrefs(EkycMainActivity.this).edit().putString(BOBRANCH, "").apply();
                getPrefs(EkycMainActivity.this).edit().putString(BOSUBBROKER, "").apply();
                getPrefs(EkycMainActivity.this).edit().putString(BOTRADERS, "").apply();
                getPrefs(EkycMainActivity.this).edit().putString(CRMBRANCH, "").apply();
                getPrefs(EkycMainActivity.this).edit().putString(LOGINUSERID, "").apply();
                getPrefs(EkycMainActivity.this).edit().putString(ROLEMAPPINGID, "").apply();
                getPrefs(EkycMainActivity.this).edit().putString(BUSINESSUNITID, "").apply();
                getPrefs(EkycMainActivity.this).edit().putString(TEAMID, "").apply();
                getPrefs(EkycMainActivity.this).edit().putString(ROLEID, "").apply();
               /* Intent intent = new Intent(EkycMainActivity.this, BranchLoginActivity.class);
                startActivity(intent);*/
                finish();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBackPage();
            }
        });

        uploadDetailsFragment = new UploadDetailsFragment();
        uploadDetailsFragment.setContext(EkycMainActivity.this);
        uploadDetailsFragment.setTabPageIndicator(indicatorDetail);

        personalDetailFragment = new PersonalDetailFragment();
        personalDetailFragment.setContext(EkycMainActivity.this);
        personalDetailFragment.setTabPageIndicator(indicatorDetail);
        esignFragment = new EsignFragment();
        esignFragment.setContext(EkycMainActivity.this);
//        esignFragment.setTabPageIndicator(indicatorDetail);
        jointHolderAndNomineeFragment = new JointHolderAndNomineeFragment();
        jointHolderAndNomineeFragment.setContext(EkycMainActivity.this);
        jointHolderAndNomineeFragment.setTabPageIndicator(indicatorDetail);
//        personalDetailFragment.setViewPager(vpApply);
        bankDetailsFormFragment = new BankDetailsFormFragment();
//        bankDetailsFormFragment.setContext(EkycMainActivity.this);
        bankDetailsFormFragment.setViewPager(vpApply);
        bankDetailsFormFragment.setTabPageIndicator(indicatorDetail);
        bankDetailsFormFragment.setReviewDialog(dialogReview);
        FundTransferFragment fundTransferFragment = new FundTransferFragment();
        fundTransferFragment.setViewPager(vpApply);
        fundTransferFragment.setTabPageIndicator(indicatorDetail);

        String[] headingClient = new String[5];
        int[] icons = new int[5];
        if (!isNomineeTabApplicable()) {
            headingClient[0] = "";
//                headingClient[1] = "";
            headingClient[1] = "";
            headingClient[2] = "";
            headingClient[3] = "";
            icons[0] = R.drawable.ic_personal_tab;
//                icons[1] = R.drawable.ic_demat_tab;
            icons[1] = R.drawable.ic_bank_tab;
            icons[2] = R.drawable.ic_upload_tab;
            icons[3] = R.drawable.ic_esign_tab;
            pageListDetail.add(personalDetailFragment);
//                pageListDetail.add(jointHolderAndNomineeFragment);
            pageListDetail.add(bankDetailsFormFragment);
            pageListDetail.add(uploadDetailsFragment);
            pageListDetail.add(esignFragment);
        } else if (getAppType().equals(DIRECT) && !getIsAadharVerified()) {
            if (getInterestedInMF()) {
                headingClient[0] = "";
                headingClient[1] = "";
                headingClient[2] = "";
                headingClient[3] = "";
                headingClient[4] = "";
                icons[0] = R.drawable.ic_personal_tab;
                icons[1] = R.drawable.ic_demat_tab;
                icons[2] = R.drawable.ic_bank_tab;
                icons[3] = R.drawable.ic_upload_tab;
                icons[4] = R.drawable.ic_esign_tab;
                pageListDetail.add(personalDetailFragment);
                pageListDetail.add(jointHolderAndNomineeFragment);
                pageListDetail.add(bankDetailsFormFragment);
                pageListDetail.add(uploadDetailsFragment);
                pageListDetail.add(esignFragment);
            } else {
                headingClient[0] = "";
                headingClient[1] = "";
                headingClient[2] = "";
                icons[0] = R.drawable.ic_personal_tab;
                icons[1] = R.drawable.ic_demat_tab;
                icons[2] = R.drawable.ic_bank_tab;
                pageListDetail.add(personalDetailFragment);
                pageListDetail.add(jointHolderAndNomineeFragment);
                pageListDetail.add(bankDetailsFormFragment);
            }
        } else {
            headingClient[0] = "";
            headingClient[1] = "";
            headingClient[2] = "";
            headingClient[3] = "";
            headingClient[4] = "";
            icons[0] = R.drawable.ic_personal_tab;
            icons[1] = R.drawable.ic_demat_tab;
            icons[2] = R.drawable.ic_bank_tab;
            icons[3] = R.drawable.ic_upload_tab;
            icons[4] = R.drawable.ic_esign_tab;
            pageListDetail.add(personalDetailFragment);
            pageListDetail.add(jointHolderAndNomineeFragment);
            pageListDetail.add(bankDetailsFormFragment);
            pageListDetail.add(uploadDetailsFragment);
            pageListDetail.add(esignFragment);
        }

        final ApplyNowAdapter adapterClient = new ApplyNowAdapter(getSupportFragmentManager(), pageListDetail, headingClient, icons);
        vpApply.setAdapter(adapterClient);
        adapterClient.notifyDataSetChanged();
        vpApply.setPagingEnabled(false);

        indicatorDetail.setViewPager(vpApply);
        mTabsLinearLayoutDetail = ((LinearLayout) indicatorDetail.getChildAt(0));
        if (!isNomineeTabApplicable()) {
            indicatorDetail.disableTab(getBankTab());
            indicatorDetail.disableTab(getUploadTab());
            indicatorDetail.disableTab(getEsignTab());
        } else {
            indicatorDetail.disableTab(getDematTab());
            indicatorDetail.disableTab(getBankTab());
            indicatorDetail.disableTab(getUploadTab());
            indicatorDetail.disableTab(getEsignTab());
        }
        indicatorDetail.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == getPersonalTab()) {
//                    PersonalDetailFragment fragmentToShow = (PersonalDetailFragment) adapterClient.getItem(position);
//                    fragmentToShow.loadPersonalDetails();
                }
                if (position == getDematTab()) {

                }
                if (position == getBankTab()) {
//                    BankDetailsFragment fragmentToShow = (BankDetailsFragment) adapterClient.getItem(position);

//                    BankDetailsFormFragment fragmentToShow = (BankDetailsFormFragment) adapterClient.getItem(position);
//                    fragmentToShow.showBankDetailForm();


                    //  fragmentToShow.getState();


                   /* FundTransferFragment fundTransferFragment = (FundTransferFragment) adapterClient.getItem(position);
                    fundTransferFragment.loadTransferDetails();*/
                }
                if (position == getUploadTab()) {
                    UploadDetailsFragment uploadDetailsFragment = (UploadDetailsFragment) adapterClient.getItem(position);
                    //uploadDetailsFragment.showVideoSection();
//                    UploadFragment fragmentToShow = (UploadFragment) adapterClient.getItem(position);
//                    fragmentToShow.loadUploadDetails();
                    /*if(adapterClient.getItem(position) instanceof UploadDetailsFragment) {
                        UploadDetailsFragment fragmentToShow = (UploadDetailsFragment) adapterClient.getItem(position);
                        fragmentToShow.loadUploadDetails();
                    }
                    else{
                        UploadDetailsVideoFragment fragmentToShow = (UploadDetailsVideoFragment) adapterClient.getItem(position);
                        fragmentToShow.loadUploadDetails();
                    }*/
                }
                if (position == getEsignTab()) {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.getString(PERSONAL_DETAIL) != null && bundle.getString(PERSONAL_DETAIL).equals(MOSLConstants.PERSONAL_DETAIL)) {
                userID = getUserID();
                disableEnableTab(PERSONAL_DETAIL);
                vpApply.setCurrentItem(getPersonalTab());
            } else if (bundle.getString(DEMAT_DETAIL) != null && bundle.getString(DEMAT_DETAIL).equals(DEMAT_DETAIL)) {
                disableEnableTab(DEMAT_DETAIL);
                vpApply.setCurrentItem(getDematTab());
            } else if (bundle.getString(BANK_DETAIL) != null && bundle.getString(BANK_DETAIL).equals(BANK_DETAIL)) {
                disableEnableTab(BANK_DETAIL);
                vpApply.setCurrentItem(getBankTab());
            } else if (bundle.getString(UPLOAD_DETAIL) != null && bundle.getString(UPLOAD_DETAIL).equals(UPLOAD_DETAIL)) {
                disableEnableTab(UPLOAD_DETAIL);
                vpApply.setCurrentItem(getUploadTab());
            } else if (bundle.getString(ESIGN_DETAIL) != null && bundle.getString(ESIGN_DETAIL).equals(ESIGN_DETAIL)) {
                disableEnableTab(ESIGN_DETAIL);
                vpApply.setCurrentItem(getEsignTab());
            }

            if (bundle.getString(TAB_DETAIL) != null && bundle.getString(TAB_DETAIL).equals(PERSONAL_DETAIL)) {
                userID = getUserID();
                disableEnableTab(PERSONAL_DETAIL);
                vpApply.setCurrentItem(getPersonalTab());
            } else if (bundle.getString(TAB_DETAIL) != null && bundle.getString(TAB_DETAIL).equals(DEMAT_DETAIL)) {
                userID = getUserID();
                disableEnableTab(DEMAT_DETAIL);
                vpApply.setCurrentItem(getDematTab());
            } else if (bundle.getString(TAB_DETAIL) != null && bundle.getString(TAB_DETAIL).equals(BANK_DETAIL)) {
                userID = getUserID();
                disableEnableTab(BANK_DETAIL);
                vpApply.setCurrentItem(getBankTab(), false);
            } else if (bundle.getString(TAB_DETAIL) != null && bundle.getString(TAB_DETAIL).equals(UPLOAD_DETAIL)) {
                userID = getUserID();
                disableEnableTab(UPLOAD_DETAIL);
                vpApply.setCurrentItem(getUploadTab());
            } else if (bundle.getString(TAB_DETAIL) != null && bundle.getString(TAB_DETAIL).equals(ESIGN_DETAIL)) {
                userID = getUserID();
                disableEnableTab(ESIGN_DETAIL);
                vpApply.setCurrentItem(getEsignTab());
            }
        }
    }

    public void formExit() {
        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put(APPLICATION_TYPE, getAppType());
            inputMap.put("_userid", userID);
            inputMap.put(APP_LOGIN_ID, CommonModel.getAppLoginID());


            String flag = USER_LOGOUT;
            String category = SIGNUP;

            WebServiceHandler.post(EkycMainActivity.this, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (response != null) {
                        try {
                            String msg = (response.isNull("_msg") || response.getString("_msg").equals("")) ? "" : response.getString("_msg");
                            String status = (response.isNull("_Status") || response.getString("_Status").equals("")) ? "" : response.getString("_Status");
                            if (Integer.valueOf(status) == 1) {
                                setUserID("");
                                setUserType("");
                                if (getAppType().equals(CRM)) {
                                    Intent intent = new Intent(EkycMainActivity.this, PostLoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (getAppType().equals(PP)) {
                                    if (isDirectProspect()) { //goto resume page for Direct prospect user
                                        Intent intent = new Intent(EkycMainActivity.this, DirectResumeApplicationActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(EkycMainActivity.this, PostLoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else if (getAppType().equals(DIRECT)) {
                                    Intent intent = new Intent(EkycMainActivity.this, DirectResumeApplicationActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                commonMsgDialog(EkycMainActivity.this, msg, false, "");
                            }
                            customProgress.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    if (statusCode == 0) {
                        commonMsgDialog(EkycMainActivity.this, CONNECTION_TIMEOUT, false, "");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });

        } else {
            MOSLCommon.alertDInternet(this);
        }
    }

    @Override
    public void onBackPressed() {
        goToBackPage();
        //overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("RestrictedApi") @Override public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT
            || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /*
    * Tab disable enable code
    */
    public void disableEnableTab(String tab) {
        if (tab.equals(PERSONAL_DETAIL)) {
            indicatorDetail.enableTab(getPersonalTab());
            indicatorDetail.disableTab(getDematTab());
            indicatorDetail.disableTab(getBankTab());
            indicatorDetail.disableTab(getUploadTab());
            indicatorDetail.disableTab(getEsignTab());
        } else if (tab.equals(DEMAT_DETAIL)) {
            indicatorDetail.enableTab(getPersonalTab());
            indicatorDetail.enableTab(getDematTab());
            indicatorDetail.disableTab(getBankTab());
            indicatorDetail.disableTab(getUploadTab());
            indicatorDetail.disableTab(getEsignTab());
        } else if (tab.equals(BANK_DETAIL)) {
            indicatorDetail.enableTab(getPersonalTab());
            indicatorDetail.enableTab(getDematTab());
            indicatorDetail.enableTab(getBankTab());
            indicatorDetail.disableTab(getUploadTab());
            indicatorDetail.disableTab(getEsignTab());
        } else if (tab.equals(UPLOAD_DETAIL)) {
            indicatorDetail.enableTab(getPersonalTab());
            indicatorDetail.enableTab(getDematTab());
            indicatorDetail.enableTab(getBankTab());
            indicatorDetail.enableTab(getUploadTab());
            indicatorDetail.disableTab(getEsignTab());
        } else if (tab.equals(ESIGN_DETAIL)) {
            indicatorDetail.enableTab(getPersonalTab());
            indicatorDetail.enableTab(getDematTab());
            indicatorDetail.enableTab(getBankTab());
            indicatorDetail.enableTab(getUploadTab());
            indicatorDetail.enableTab(getEsignTab());
        }
    }

    private void goToBackPage() {
        try {
            if (dialogReview != null && dialogReview.isShowing()) {
                dialogReview.dismiss();
            } else if (vpApply.getCurrentItem() == getPersonalTab()) {
                if (getInterestedInMF()) {
                    if (!getIsAadharVerified()) {
                        if (getAppType().equals(DIRECT)) {
                            Intent intent = new Intent(EkycMainActivity.this, PANAdharRegistActivity.class); // TODO: 28-03-2017
                            intent.putExtra(SOURCE, BACK_FROM_PREVIOUS_SCREEN);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(EkycMainActivity.this, PostLoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else if (isAadhaarByPass() && getIsCVLVerified()) {
                        Intent intent = new Intent(EkycMainActivity.this,
                            PANAdharRegistActivity.class); // TODO: 28-03-2017
                        intent.putExtra(SOURCE, BACK_FROM_PREVIOUS_SCREEN);
                        startActivity(intent);
                        finish();
                    } else if (isAadhaarByPass() && !getIsCVLVerified()) {
                        Intent intent = new Intent(EkycMainActivity.this,
                            PANAdharRegistActivity.class); // TODO: 28-03-2017
                        intent.putExtra(SOURCE, BACK_FROM_PREVIOUS_SCREEN);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(EkycMainActivity.this, AddressDetailsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    if (getAppType().equals(DIRECT)) {
                        if (!getIsAadharVerified() && !getIsCVLVerified()) {
                            Intent intent = new Intent(EkycMainActivity.this,
                                PANAdharRegistActivity.class); // TODO: 28-03-2017
                            intent.putExtra(SOURCE, BACK_FROM_PREVIOUS_SCREEN);
                            startActivity(intent);
                            finish();
                        } else if (isAadhaarByPass() && !getIsCVLVerified()) {
                            Intent intent = new Intent(EkycMainActivity.this, PANAdharRegistActivity.class); // TODO: 28-03-2017
                            intent.putExtra(SOURCE, BACK_FROM_PREVIOUS_SCREEN);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(EkycMainActivity.this, AddressDetailsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else if (isAadhaarByPass() && !getIsCVLVerified()) {
                        Intent intent = new Intent(EkycMainActivity.this,
                            PANAdharRegistActivity.class); // TODO: 28-03-2017
                        intent.putExtra(SOURCE, BACK_FROM_PREVIOUS_SCREEN);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(EkycMainActivity.this, AddressDetailsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            } else if (vpApply.getCurrentItem() == getDematTab()) {
                vpApply.setCurrentItem(getPersonalTab());
            } else if (vpApply.getCurrentItem() == getBankTab()) {
                if (isNomineeTabApplicable()) {
                    vpApply.setCurrentItem(getDematTab());
                } else {
                    vpApply.setCurrentItem(getPersonalTab());
                }
            } else if (vpApply.getCurrentItem() == getUploadTab()) {
                try {
                    ApplyNowAdapter adapterClient = (ApplyNowAdapter) vpApply.getAdapter();
                    UploadDetailsFragment uploadDetailsFragment = (UploadDetailsFragment) adapterClient.getItem(getUploadTab());
                    boolean isVerifierConnected = uploadDetailsFragment.getUploadDetailAdapter().isVerifierConnected();
                    if (!isVerifierConnected) {
                        vpApply.setCurrentItem(getBankTab());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (vpApply.getCurrentItem() == getEsignTab()) {
                vpApply.setCurrentItem(getUploadTab());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Callback listener functions
    public void onSigningSuccess(String documentId) {
        //Toast.makeText(this, documentId + " signed successfully", Toast.LENGTH_SHORT).show();
        esignFragment.setDigioData(documentId, "signed successfully", "", "", "", "");
    }

    public void onSigningFailure(String documentId, int code, String response) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        Log.d("SigningFailure", response);
        try {
            JSONObject obj = new JSONObject(response);
            String txn_id = (obj.isNull("txn_id") || obj.getString("txn_id").equals("")) ? "" : obj.getString("txn_id");
            String digio_doc_id = (obj.isNull("digio_doc_id") || obj.getString("digio_doc_id").equals("")) ? "" : obj.getString("digio_doc_id");
            JSONObject lastStateObj = obj.getJSONObject("last_state");
            String screen = (lastStateObj.isNull("screen") || lastStateObj.getString("screen").equals("")) ? "" : lastStateObj.getString("screen");
            String state_code = (lastStateObj.isNull("state_code") || lastStateObj.getString("state_code").equals("")) ? "" : lastStateObj.getString("state_code");
            String error_code = (lastStateObj.isNull("error_code") || lastStateObj.getString("error_code").equals("")) ? "" : lastStateObj.getString("error_code");
            String message = (lastStateObj.isNull("message") || lastStateObj.getString("message").equals("")) ? "" : lastStateObj.getString("message");

            esignFragment.setDigioData(digio_doc_id, message, error_code, screen, state_code,
                message);
            Log.d("SigningFailure123", txn_id + " " + digio_doc_id + " " + screen + " " + state_code + " " + error_code + " " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
