package com.mosl.mobile.ekyc.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.directmodule.DirectResumeApplicationActivity;
import com.mosl.mobile.ekyc.model.CommonModel;
import com.mosl.mobile.ekyc.register.PANAdharRegistActivity;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import cz.msebera.android.httpclient.Header;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.mosl.mobile.ekyc.common.MOSLCommon.commonMsgDialog;
import static com.mosl.mobile.ekyc.common.MOSLCommon.disableField;
import static com.mosl.mobile.ekyc.common.MOSLCommon.enableField;
import static com.mosl.mobile.ekyc.common.MOSLCommon.getPrefs;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.model.CommonModel.getAddressMode;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppType;
import static com.mosl.mobile.ekyc.model.CommonModel.getInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsAadharVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsCVLVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getRegisteredName;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.isAadhaarByPass;
import static com.mosl.mobile.ekyc.model.CommonModel.isCommodityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.isDirectProspect;
import static com.mosl.mobile.ekyc.model.CommonModel.isEquityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.isMfIpvDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setAddressMode;
import static com.mosl.mobile.ekyc.model.CommonModel.setUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.setUserType;


/**
 * Created by Vishal on 04-Aug-16.
 */
public class AddressDetailsActivity extends Activity implements MOSLConstants, View.OnClickListener {

    CheckBox cbCVL, cbAadhar, cbNew;
    //    Button btnAddNew;
    public TextView tvCVLName, tvCVLMidName, tvCVLLastName, tvCVLTitle, tvCVLCorrBlgdName, tvCVLCorrFlatName, tvCVLCorrArea, tvCVLCorrAddProof, tvCVLCorrPinCode,
            tvCVLCorrState, tvCVLCorrCity, tvCVLCorrCountry, tvCVLPermBlgdName, tvCVLPermFlatName, tvCVLPermArea,
            tvAdharTitle, tvAdharName, tvAdharMidName, tvAdharLastName, tvCVLPermAddProof, tvCVLPermPinCode, tvCVLPermState, tvCVLPermCity, tvCVLPermCountry, tvAdharCorrBlgdName, tvAdharCorrFlatName, tvAdharCorrArea, tvAdharCorrAddProof, tvAdharCorrPinCode, tvAdharCorrState, tvAdharCorrCity, tvAdharCorrCountry, tvAdharPermBlgdName, tvAdharPermFlatName, tvAdharPermArea, tvAdharPermAddProof, tvAdharPermPinCode, tvAdharPermState, tvAdharPermCity, tvAdharPermCountry;
    public static Boolean CVLIsCheched = false;
    public static Boolean AadharIsCheched = false;
    public static Boolean NewAddressIsCheched = false;
    LinearLayout llCVL, llAadhar;
    TextView tvCVL, tvAadhar, tvNew;
    Button btnSubmit;
//    TextView tvNoCVLAdd,tvNoAadharAdd;


    LinearLayout customProgress;
    //        String userID = "1610209";
    String userID = getUserID();
    //    LinearLayout llCorrespondenceAddress;
    ImageView ivExit, ivBack;
    LinearLayout rlExitForm, rlLogout, llCVLTitleLayout, llAadharTitleLayout, llAddNewTitleLayout;
    TextView tvExitForm, tvAppLoginId, tvUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_address_details);
            setPermisson(AddressDetailsActivity.this, this);
//            btnAddNew = (Button) findViewById(R.id.btnAddNew);
            ivExit = (ImageView) findViewById(R.id.ivExit);
            ivBack = (ImageView) findViewById(R.id.ivBack);
            //  tvExitForm = (TextView) findViewById(R.id.tvExitForm);
//            tvNoCVLAdd = (TextView) findViewById(R.id.tvNoCVLAdd);
//            tvNoAadharAdd = (TextView) findViewById(R.id.tvNoAadharAdd);
            tvCVL = (TextView) findViewById(R.id.tvCVL);
            tvNew = (TextView) findViewById(R.id.tvNew);
            tvAadhar = (TextView) findViewById(R.id.tvAadhar);

            btnSubmit = (Button) findViewById(R.id.btnSubmit);
//        tvExitForm.setText("Exit Form");
            tvAppLoginId = (TextView) findViewById(R.id.tvAppLoginId);
            tvUserName = (TextView) findViewById(R.id.tvUserName);

            rlExitForm = (LinearLayout) findViewById(R.id.rlExitForm);
            rlLogout = (LinearLayout) findViewById(R.id.rlLogout);
            llCVL = (LinearLayout) findViewById(R.id.llCVL);
            llAadhar = (LinearLayout) findViewById(R.id.llAadhar);
            llCVLTitleLayout = (LinearLayout) findViewById(R.id.llCVLTitleLayout);
            llAadharTitleLayout = (LinearLayout) findViewById(R.id.llAadharTitleLayout);
            llAddNewTitleLayout = (LinearLayout) findViewById(R.id.llAddNewTitleLayout);
            if (getAppType().equals(DIRECT) || isDirectProspect()) {
                tvAppLoginId.setVisibility(View.GONE);
                tvUserName.setText(getRegisteredName().toUpperCase());
            } else {
                if (!getRegisteredName().equals("")) {
                    tvUserName.setText(getRegisteredName().toUpperCase() + " -");
                }
                tvAppLoginId.setText(getAppLoginID());
            }

            if (getAppType().equals(CRM)) {
                rlLogout.setVisibility(View.VISIBLE);
            } else {
                rlLogout.setVisibility(View.GONE);
            }
            rlLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPrefs(AddressDetailsActivity.this).edit().putBoolean("AUTHENTICATED", false).apply();
                    //Util.getPrefs(this).edit().putString("LOGIN_ID", null).apply();
                    getPrefs(AddressDetailsActivity.this).edit().putString("APP_LOGIN_ID", null).apply();
                    getPrefs(AddressDetailsActivity.this).edit().putString("PASSWORD", null).apply();
                    getPrefs(AddressDetailsActivity.this).edit().putString(BOBRANCH, "").apply();
                    getPrefs(AddressDetailsActivity.this).edit().putString(BOSUBBROKER, "").apply();
                    getPrefs(AddressDetailsActivity.this).edit().putString(BOTRADERS, "").apply();
                    getPrefs(AddressDetailsActivity.this).edit().putString(CRMBRANCH, "").apply();
                    getPrefs(AddressDetailsActivity.this).edit().putString(LOGINUSERID, "").apply();
                    getPrefs(AddressDetailsActivity.this).edit().putString(ROLEMAPPINGID, "").apply();
                    getPrefs(AddressDetailsActivity.this).edit().putString(BUSINESSUNITID, "").apply();
                    getPrefs(AddressDetailsActivity.this).edit().putString(TEAMID, "").apply();
                    getPrefs(AddressDetailsActivity.this).edit().putString(ROLEID, "").apply();
                    /*Intent intent = new Intent(AddressDetailsActivity.this, BranchLoginActivity.class);
                    startActivity(intent);*/
                    finish();
                }
            });

            rlExitForm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();


                }
            });
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* if (getAppType().equals(DIRECT)) {
                        Intent intent = new Intent(AddressDetailsActivity.this, DirectResumeApplicationActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(AddressDetailsActivity.this, PostLoginActivity.class);
                        startActivity(intent);
                        finish();
                    }*/

                    goToBackPage();
                }
            });
            cbCVL = (CheckBox) findViewById(R.id.cbCVL);
            cbAadhar = (CheckBox) findViewById(R.id.cbAadhar);
            cbNew = (CheckBox) findViewById(R.id.cbNew);
            tvCVLName = (TextView) findViewById(R.id.tvCVLName);
       /* tvCVLMidName = (TextView) findViewById(R.id.tvCVLMidName);
        tvCVLLastName = (TextView) findViewById(R.id.tvCVLLastName);*/
            tvCVLTitle = (TextView) findViewById(R.id.tvCVLTitle);
            tvAdharTitle = (TextView) findViewById(R.id.tvAdharTitle);

            tvCVLCorrBlgdName = (TextView) findViewById(R.id.tvCVLCorrBlgdName);
            tvCVLCorrFlatName = (TextView) findViewById(R.id.tvCVLCorrFlatName);
            tvCVLCorrArea = (TextView) findViewById(R.id.tvCVLCorrArea);
            tvCVLCorrAddProof = (TextView) findViewById(R.id.tvCVLCorrAddProof);
            tvCVLCorrPinCode = (TextView) findViewById(R.id.tvCVLCorrPinCode);
            tvCVLCorrState = (TextView) findViewById(R.id.tvCVLCorrState);
            tvCVLCorrCity = (TextView) findViewById(R.id.tvCVLCorrCity);
            tvCVLCorrCountry = (TextView) findViewById(R.id.tvCVLCorrCountry);

            tvCVLPermBlgdName = (TextView) findViewById(R.id.tvCVLPermBlgdName);
            tvCVLPermFlatName = (TextView) findViewById(R.id.tvCVLPermFlatName);
            tvCVLPermArea = (TextView) findViewById(R.id.tvCVLPermArea);
            tvCVLPermAddProof = (TextView) findViewById(R.id.tvCVLPermAddProof);
            tvCVLPermPinCode = (TextView) findViewById(R.id.tvCVLPermPinCode);
            tvCVLPermState = (TextView) findViewById(R.id.tvCVLPermState);
            tvCVLPermCity = (TextView) findViewById(R.id.tvCVLPermCity);
            tvCVLPermCountry = (TextView) findViewById(R.id.tvCVLPermCountry);


            tvAdharCorrBlgdName = (TextView) findViewById(R.id.tvAdharCorrBlgdName);
            tvAdharCorrFlatName = (TextView) findViewById(R.id.tvAdharCorrFlatName);
            tvAdharCorrArea = (TextView) findViewById(R.id.tvAdharCorrArea);
            tvAdharCorrAddProof = (TextView) findViewById(R.id.tvAdharCorrAddProof);
            tvAdharCorrPinCode = (TextView) findViewById(R.id.tvAdharCorrPinCode);
            tvAdharCorrState = (TextView) findViewById(R.id.tvAdharCorrState);
            tvAdharCorrCity = (TextView) findViewById(R.id.tvAdharCorrCity);
            tvAdharCorrCountry = (TextView) findViewById(R.id.tvAdharCorrCountry);


            tvAdharPermBlgdName = (TextView) findViewById(R.id.tvAdharPermBlgdName);
            tvAdharPermFlatName = (TextView) findViewById(R.id.tvAdharPermFlatName);
            tvAdharPermArea = (TextView) findViewById(R.id.tvAdharPermArea);
            tvAdharPermAddProof = (TextView) findViewById(R.id.tvAdharPermAddProof);
            tvAdharPermPinCode = (TextView) findViewById(R.id.tvAdharPermPinCode);
            tvAdharPermState = (TextView) findViewById(R.id.tvAdharPermState);
            tvAdharPermCity = (TextView) findViewById(R.id.tvAdharPermCity);
            tvAdharPermCountry = (TextView) findViewById(R.id.tvAdharPermCountry);

            tvAdharName = (TextView) findViewById(R.id.tvAdharName);

            customProgress = (LinearLayout) findViewById(R.id.customProgress);
//            llCorrespondenceAddress = (LinearLayout) findViewById(R.id.llCorrespondenceAddress);

            llCVLTitleLayout.setOnClickListener(this);
            llAadharTitleLayout.setOnClickListener(this);
            llAddNewTitleLayout.setOnClickListener(this);
            btnSubmit.setOnClickListener(this);
            cbCVL.setOnClickListener(this);
            cbNew.setOnClickListener(this);
            cbAadhar.setOnClickListener(this);

            if (getInterestedInMF()) {
                if (getIsAadharVerified()) {
                    if (!getIsCVLVerified()) {
                        getAddress(false, true, true);
                    }
                }
            } else if (isAadhaarByPass()) {
                getAddress(true, false, true);
            } else {
                getAddress(true, true, true);
            }
            /*if (getAddressMode().equals("")) {
                handleAAdhaarChek();
            } else {
                switch (getAddressMode()) {
                    case MANUAL:
                        handleNewChek();
                        break;
                    case AADHARADDDETAIL:
                        handleAAdhaarChek();
                        break;
                    case CVL:
                        handleCVLChek();
                        break;
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
//            Crashlytics.logException(e);
        }
    }

    public void logout() {
        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);

            Map inputMap = new HashMap<>();
            inputMap.put(APPLICATION_TYPE, getAppType());
            inputMap.put("_userid", userID);
            inputMap.put(APP_LOGIN_ID, CommonModel.getAppLoginID());


            String flag = USER_LOGOUT;
            String category = SIGNUP;

            WebServiceHandler.post(AddressDetailsActivity.this, flag, category, inputMap, new JsonHttpResponseHandler() {
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
                                if (getAppType().equals(DIRECT)) {
                                    Intent intent = new Intent(AddressDetailsActivity.this, DirectResumeApplicationActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (getAppType().equals(CRM)) {
                                    Intent intent = new Intent(AddressDetailsActivity.this, PostLoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (getAppType().equals(PP)) {
                                    if (isDirectProspect()) { //goto resume page for Direct prospect user
                                        Intent intent = new Intent(AddressDetailsActivity.this, DirectResumeApplicationActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(AddressDetailsActivity.this, PostLoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            } else {
                                commonMsgDialog(AddressDetailsActivity.this, msg, false, "");
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
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(AddressDetailsActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(AddressDetailsActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(AddressDetailsActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });

        } else {
            MOSLCommon.alertDInternet(this);
        }
    }

    public void submitDialog(String msg) {
        final Dialog dialog = new Dialog(AddressDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setWindowAnimations(R.style.animationName);
        dialog.setContentView(R.layout.layout_dialog_info);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);

        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
        Button btnOK = (Button) dialog.findViewById(R.id.btnOK);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);


        tvMsg.setText(msg);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void getAddress(final boolean showCVLAdd, final boolean showAadharAdd, final boolean showManualAdd) {
        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put("userid", userID);

            String flag = GET_ADDRESS_DETAILS;
            String category = ADDRESSDETAILS;
            WebServiceHandler.post(this, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (response != null) {
                        try {
                            if (showCVLAdd) {
                                if (response.has("AddressDetails")) {
                                    //CVL verified detail
                                    JSONArray addressDetailsArray = response.getJSONArray("AddressDetails");

                                    if (addressDetailsArray.length() != 0) {
                                        for (int i = 0; i < addressDetailsArray.length(); i++) {
                                            llCVLTitleLayout.setVisibility(View.VISIBLE);
                                            cbCVL.setEnabled(true);
//                                            tvNoCVLAdd.setVisibility(View.GONE);
                                            llCVL.setVisibility(View.VISIBLE);

                                            JSONObject object = addressDetailsArray.getJSONObject(i);

                                            String gender = (object.isNull("GENDER") || object.getString("GENDER").equals("")) ? "" : object.getString("GENDER");
                                            String name = (object.isNull("NAME") || object.getString("NAME").equals("")) ? "" : object.getString("NAME");
                                            String dob = (object.isNull("DOB") || object.getString("DOB").equals("")) ? "" : object.getString("DOB");
                                            String app_cor_add1 = (object.isNull("APP_COR_ADD1") || object.getString("APP_COR_ADD1").equals("")) ? "" : object.getString("APP_COR_ADD1");
                                            String app_cor_add2 = (object.isNull("APP_COR_ADD2") || object.getString("APP_COR_ADD2").equals("")) ? "" : object.getString("APP_COR_ADD2");
                                            String app_cor_add3 = (object.isNull("APP_COR_ADD3") || object.getString("APP_COR_ADD3").equals("")) ? "" : object.getString("APP_COR_ADD3");
                                            String cor_add_proof = (object.isNull("COR_ADD_PROOF") || object.getString("COR_ADD_PROOF").equals("")) ? "" : object.getString("COR_ADD_PROOF");
                                            String cor_city = (object.isNull("COR_CITY") || object.getString("COR_CITY").equals("")) ? "" : object.getString("COR_CITY");
                                            String cor_pincode = (object.isNull("COR_PINCODE") || object.getString("COR_PINCODE").equals("")) ? "" : object.getString("COR_PINCODE");
                                            String cor_state = (object.isNull("STATE") || object.getString("STATE").equals("")) ? "" : object.getString("STATE");
                                            String cor_country = (object.isNull("COR_COUNTRY") || object.getString("COR_COUNTRY").equals("")) ? "" : object.getString("COR_COUNTRY");
                                            String cor_phone = (object.isNull("PHONE") || object.getString("PHONE").equals("")) ? "" : object.getString("PHONE");
                                            String app_per_add1 = (object.isNull("APP_PER_ADD1") || object.getString("APP_PER_ADD1").equals("")) ? "" : object.getString("APP_PER_ADD1");
                                            String app_per_add2 = (object.isNull("APP_PER_ADD2") || object.getString("APP_PER_ADD2").equals("")) ? "" : object.getString("APP_PER_ADD2");
                                            String app_per_add3 = (object.isNull("APP_PER_ADD3") || object.getString("APP_PER_ADD3").equals("")) ? "" : object.getString("APP_PER_ADD3");
                                            String per_city = (object.isNull("PER_CITY") || object.getString("PER_CITY").equals("")) ? "" : object.getString("PER_CITY");
                                            String per_pincd = (object.isNull("PER_PINCD") || object.getString("PER_PINCD").equals("")) ? "" : object.getString("PER_PINCD");
                                            String per_state = (object.isNull("PER_STATE") || object.getString("PER_STATE").equals("")) ? "" : object.getString("PER_STATE");
                                            String per_country = (object.isNull("PER_COUNTRY") || object.getString("PER_COUNTRY").equals("")) ? "" : object.getString("PER_COUNTRY");
                                            String per_add_proof = (object.isNull("PER_ADD_PROOF") || object.getString("PER_ADD_PROOF").equals("")) ? "" : object.getString("PER_ADD_PROOF");
                                            String email = (object.isNull("EMAIL") || object.getString("EMAIL").equals("")) ? "" : object.getString("EMAIL");

                                            tvCVLTitle.setText("");
                                            tvCVLName.setText(name);
                                            tvCVLCorrBlgdName.setText(app_cor_add1);
                                            tvCVLCorrFlatName.setText(app_cor_add2);
                                            tvCVLCorrArea.setText(app_cor_add3);
                                            tvCVLCorrAddProof.setText(cor_add_proof);
                                            tvCVLCorrPinCode.setText(cor_pincode);
                                            tvCVLCorrState.setText(cor_state);
                                            tvCVLCorrCity.setText(cor_city);
                                            tvCVLCorrCountry.setText(cor_country);
                                            tvCVLPermBlgdName.setText(app_per_add1);
                                            tvCVLPermFlatName.setText(app_per_add2);
                                            tvCVLPermArea.setText(app_per_add3);
                                            tvCVLPermAddProof.setText(per_add_proof);
                                            tvCVLPermPinCode.setText(per_pincd);
                                            tvCVLPermState.setText(per_state);
                                            tvCVLPermCity.setText(per_city);
                                            tvCVLPermCountry.setText(per_country);
                                        }
                                    } else {
                                        cbCVL.setEnabled(false);
                                        llCVLTitleLayout.setEnabled(false);
//                                        tvNoCVLAdd.setVisibility(View.VISIBLE);
                                        llCVL.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                cbCVL.setEnabled(false);
                                llCVLTitleLayout.setEnabled(false);
//                                tvNoCVLAdd.setVisibility(View.VISIBLE);
                                llCVL.setVisibility(View.GONE);
//                                llCVLTitleLayout.setVisibility(View.GONE);
                            }
                            if (showAadharAdd) {
                                if (response.has("AddressDetails1")) {
                                    //Aadhaar Verified detail
                                    JSONArray addressDetails1Array = response.getJSONArray("AddressDetails1");


                                    if (addressDetails1Array.length() != 0) {
                                        for (int i = 0; i < addressDetails1Array.length(); i++) {
                                            cbAadhar.setEnabled(true);
//                                            tvNoAadharAdd.setVisibility(View.GONE);
                                            llAadhar.setVisibility(View.VISIBLE);

                                            JSONObject object = addressDetails1Array.getJSONObject(i);

                                            String gender = (object.isNull("GENDER") || object.getString("GENDER").equals("")) ? "" : object.getString("GENDER");
                                            String name = (object.isNull("NAME") || object.getString("NAME").equals("")) ? "" : object.getString("NAME");
                                            String dob = (object.isNull("DOB") || object.getString("DOB").equals("")) ? "" : object.getString("DOB");
                                            String app_cor_add1 = (object.isNull("APP_COR_ADD1") || object.getString("APP_COR_ADD1").equals("")) ? "" : object.getString("APP_COR_ADD1");
                                            String app_cor_add2 = (object.isNull("APP_COR_ADD2") || object.getString("APP_COR_ADD2").equals("")) ? "" : object.getString("APP_COR_ADD2");
                                            String app_cor_add3 = (object.isNull("APP_COR_ADD3") || object.getString("APP_COR_ADD3").equals("")) ? "" : object.getString("APP_COR_ADD3");
                                            String cor_add_proof = (object.isNull("COR_ADD_PROOF") || object.getString("COR_ADD_PROOF").equals("")) ? "" : object.getString("COR_ADD_PROOF");
                                            String cor_city = (object.isNull("COR_CITY") || object.getString("COR_CITY").equals("")) ? "" : object.getString("COR_CITY");
                                            String cor_pincode = (object.isNull("COR_PINCODE") || object.getString("COR_PINCODE").equals("")) ? "" : object.getString("COR_PINCODE");
                                            String cor_state = (object.isNull("STATE") || object.getString("STATE").equals("")) ? "" : object.getString("STATE");
                                            String cor_country = (object.isNull("COR_COUNTRY") || object.getString("COR_COUNTRY").equals("")) ? "" : object.getString("COR_COUNTRY");
                                            String cor_phone = (object.isNull("PHONE") || object.getString("PHONE").equals("")) ? "" : object.getString("PHONE");
                                            String app_per_add1 = (object.isNull("APP_PER_ADD1") || object.getString("APP_PER_ADD1").equals("")) ? "" : object.getString("APP_PER_ADD1");
                                            String app_per_add2 = (object.isNull("APP_PER_ADD2") || object.getString("APP_PER_ADD2").equals("")) ? "" : object.getString("APP_PER_ADD2");
                                            String app_per_add3 = (object.isNull("APP_PER_ADD3") || object.getString("APP_PER_ADD3").equals("")) ? "" : object.getString("APP_PER_ADD3");
                                            String per_city = (object.isNull("PER_CITY") || object.getString("PER_CITY").equals("")) ? "" : object.getString("PER_CITY");
                                            String per_pincd = (object.isNull("PER_PINCD") || object.getString("PER_PINCD").equals("")) ? "" : object.getString("PER_PINCD");
                                            String per_state = (object.isNull("PER_STATE") || object.getString("PER_STATE").equals("")) ? "" : object.getString("PER_STATE");
                                            String per_country = (object.isNull("PER_COUNTRY") || object.getString("PER_COUNTRY").equals("")) ? "" : object.getString("PER_COUNTRY");
                                            String per_add_proof = (object.isNull("PER_ADD_PROOF") || object.getString("PER_ADD_PROOF").equals("")) ? "" : object.getString("PER_ADD_PROOF");
                                            String email = (object.isNull("EMAIL") || object.getString("EMAIL").equals("")) ? "" : object.getString("EMAIL");

                                            tvAdharTitle.setText("");
                                            tvAdharName.setText(name);
                                            tvAdharCorrBlgdName.setText(app_cor_add1);
                                            tvAdharCorrFlatName.setText(app_cor_add2);
                                            tvAdharCorrArea.setText(app_cor_add3);
                                            tvAdharCorrAddProof.setText(cor_add_proof);
                                            tvAdharCorrPinCode.setText(cor_pincode);
                                            tvAdharCorrState.setText(cor_state);
                                            tvAdharCorrCity.setText(cor_city);
                                            tvAdharCorrCountry.setText(cor_country);
                                            tvAdharPermBlgdName.setText(app_per_add1);
                                            tvAdharPermFlatName.setText(app_per_add2);
                                            tvAdharPermArea.setText(app_per_add3);
                                            tvAdharPermAddProof.setText(per_add_proof);
                                            tvAdharPermPinCode.setText(per_pincd);
                                            tvAdharPermState.setText(per_state);
                                            tvAdharPermCity.setText(per_city);
                                            tvAdharPermCountry.setText(per_country);
                                        }
                                    } else {
                                        cbAadhar.setEnabled(false);
                                        llAadharTitleLayout.setEnabled(false);
//                                        tvNoAadharAdd.setVisibility(View.VISIBLE);
                                        llAadhar.setVisibility(View.GONE);

                                    }
                                }
                            } else {
                                //llAadharTitleLayout.setVisibility(View.GONE);
                                cbAadhar.setEnabled(false);
                                llAadharTitleLayout.setEnabled(false);
                                llAadhar.setVisibility(View.GONE);
                            }

                            if (response.has("AddressDetails2")) {
                                //Aadhaar Verified detail
                                JSONArray addressDetails2Array = response.getJSONArray("AddressDetails2");


                                if (addressDetails2Array.length() != 0) {
                                    for (int i = 0; i < addressDetails2Array.length(); i++) {
                                        JSONObject object = addressDetails2Array.getJSONObject(i);

                                        String addressMode = (object.isNull("ADDRESSMODE") || object.getString("ADDRESSMODE").equals("")) ? "" : object.getString("ADDRESSMODE");
                                        String isAadhaarVerified = (object.isNull("ISADDHARVERIFIED") || object.getString("ISADDHARVERIFIED").equals("")) ? "" : object.getString("ISADDHARVERIFIED");
                                        String isCVLVerified = (object.isNull("ISCVLVERIFIED") || object.getString("ISCVLVERIFIED").equals("")) ? "" : object.getString("ISCVLVERIFIED");

                                        if (addressMode.equals("")) {
                                            if (getInterestedInMF()) {
                                                if (getIsAadharVerified()) {
                                                    if (!getIsCVLVerified()) {
                                                        handleAAdhaarChek();
                                                    }
                                                }
                                            } else {
                                                if (isCVLVerified.equals("1") && isAadhaarVerified.equals("1")) {
                                                    handleAAdhaarChek();

                                                } else if (isCVLVerified.equals("1") && isAadhaarVerified.equals("0")) {
                                                    handleCVLChek();
                                                } else if (isCVLVerified.equals("0") && isAadhaarVerified.equals("1")) {
                                                    handleAAdhaarChek();
                                                }
                                            }

                                        } else {
                                            switch (getAddressMode()) {
                                                case MANUAL:
                                                    handleNewChek();
                                                    break;
                                                case AADHARADDDETAIL:
                                                    handleAAdhaarChek();
                                                    break;
                                                case CVL:
                                                    handleCVLChek();
                                                    break;
                                            }
                                        }
                                    }
                                }
                            }
                            disableEnableField();
                            customProgress.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgress.setVisibility(View.VISIBLE);
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
                    commonMsgDialog(AddressDetailsActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(AddressDetailsActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(AddressDetailsActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

            });
        } else {
            MOSLCommon.alertDInternet(this);
        }
    }

    public void updateAddressDetails(String userID, final String addressMode) {
        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put(APPLICATION_TYPE, getAppType());
            inputMap.put("userid", userID);
            inputMap.put(ADDRESSMODE, addressMode);

            String flag = UPDATEADDRESSDETAILS;
            String category = ADDRESSDETAILS;

            WebServiceHandler.post(this, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    if (response != null) {
                        try {
                            String status = (response.isNull("_Status") || response.getString("_Status").equals("")) ? "" : response.getString("_Status");
                            String msg = (response.isNull("_msg") || response.getString("_msg").equals("")) ? "" : response.getString("_msg");
                            if (Integer.valueOf(status) == 1) {
                                setAddressMode(addressMode);
                                callApplyNowActivity();
                            } else if (Integer.valueOf(status) == 0) {
                                submitDialog(msg);
                                cbCVL.setChecked(false);
                                cbNew.setChecked(false);
                                cbAadhar.setChecked(false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            cbCVL.setChecked(false);
                            cbNew.setChecked(false);
                            cbAadhar.setChecked(false);
                        }

                    }

                    customProgress.setVisibility(View.GONE);

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(AddressDetailsActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(AddressDetailsActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(AddressDetailsActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });
        } else {
            MOSLCommon.alertDInternet(this);
        }


    }

    private void handleCVLChek() {
        cbAadhar.setChecked(false);
        cbNew.setChecked(false);
        CVLIsCheched = true;
        AadharIsCheched = false;
        NewAddressIsCheched = false;
//        setAddressMode("CVL");
        cbCVL.setChecked(true);

        /*CustomDialog addSelDialog = new CustomDialog(AddressDetailsActivity.this, "Are you sure you want to go ahead with CVL Address ?", true, true, true, "Address Selection", YES, NO, new CustomDialog.CustomDialogListener() {
            @Override
            public void dialogAction(MOSLCommon.Action action, Object... data) {
                if (action == MOSLCommon.Action.OK) {
//                                        callApplyNowActivity();
                    updateAddressDetails(userID, CVL);
                } else {
                    cbCVL.setChecked(false);
                }
            }
        });
        addSelDialog.show();*/

    }

    private void handleNewChek() {
        cbAadhar.setChecked(false);
        cbCVL.setChecked(false);
        CVLIsCheched = false;
        AadharIsCheched = false;
        NewAddressIsCheched = true;
        cbNew.setChecked(true);

        /*CustomDialog addSelDialog = new CustomDialog(AddressDetailsActivity.this, "Are you sure you want go ahead with Address  other than address of CVL or Aadhaar ?", true, true, true, "Address Selection", YES, NO, new CustomDialog.CustomDialogListener() {
            @Override
            public void dialogAction(MOSLCommon.Action action, Object... data) {
                if (action == MOSLCommon.Action.OK) {
                    updateAddressDetails(userID, MANUAL);
                } else {
                    cbNew.setChecked(false);
                }
            }
        });
        addSelDialog.show();*/


        //  llCorrespondenceAddress.setVisibility(View.VISIBLE);

    }

    private void handleAAdhaarChek() {
        cbCVL.setChecked(false);
        cbNew.setChecked(false);
        CVLIsCheched = false;
        AadharIsCheched = true;
        NewAddressIsCheched = false;
//        setAddressMode("Aadhar");
        cbAadhar.setChecked(true);

       /* CustomDialog addSelDialog = new CustomDialog(AddressDetailsActivity.this, "Are you sure you want to go ahead with Aadhaar address ?", true, true, true, "Address Selection", YES, NO, new CustomDialog.CustomDialogListener() {
            @Override
            public void dialogAction(MOSLCommon.Action action, Object... data) {
                if (action == MOSLCommon.Action.OK) {
//                                        callApplyNowActivity();

                    updateAddressDetails(userID, AADHARADDDETAIL);

                } else {
                    cbAadhar.setChecked(false);
                }
            }
        });
        addSelDialog.show();*/


    }

    public void callApplyNowActivity() {
        Intent intent = new Intent(AddressDetailsActivity.this, EkycMainActivity.class);
        startActivity(intent);
        finish();
    }

   /* public void showPopUp(String address) {

        MOSLCommon.showPopUpThreeLine(this,cbAadhar,getResources().getString(R.string.acc_open_segment));
    }*/

    @Override
    public void onBackPressed() {
        goToBackPage();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.llCVLTitleLayout) {
            handleCVLChek();
        } else if (i == R.id.llAadharTitleLayout) {
            handleAAdhaarChek();
        } else if (i == R.id.llAddNewTitleLayout) {
            handleNewChek();
        } else if (i == R.id.cbCVL) {
            if (cbCVL.isChecked()) {
                handleCVLChek();
            }
        } else if (i == R.id.cbNew) {
            if (cbNew.isChecked()) {
                handleNewChek();
            }
        } else if (i == R.id.cbAadhar) {
            if (cbAadhar.isChecked()) {
                handleAAdhaarChek();
            }
        } else if (i == R.id.btnSubmit) {
            if (cbNew.isChecked()) {
                if (!getAddressMode().equalsIgnoreCase(MANUAL)) {
                    String msg = "";
                    if (!getIsCVLVerified()) {
                        msg = getResources().getString(R.string.manual_add_other_than_aadhaar_warning);
                    } else {
                        msg = getResources().getString(R.string.manual_add_with_cvl_aadhaar_warning);
                    }
                    CustomDialog addSelDialog = new CustomDialog(AddressDetailsActivity.this, msg, true, true, true, "Address Selection", YES, NO, new CustomDialog.CustomDialogListener() {
                        @Override
                        public void dialogAction(MOSLCommon.Action action, Object... data) {
                            if (action == MOSLCommon.Action.OK) {
                                updateAddressDetails(userID, MANUAL);
                            } else {
                                cbNew.setChecked(false);
                            }
                        }
                    });
                    addSelDialog.show();
                } else {
                    callApplyNowActivity();
                }
            } else if (cbCVL.isChecked()) {
                if (!getAddressMode().equalsIgnoreCase(CVL)) {
                    CustomDialog addSelDialog = new CustomDialog(AddressDetailsActivity.this, getResources().getString(R.string.cvl_add_warning), true, true, true, "Address Selection", YES, NO, new CustomDialog.CustomDialogListener() {
                        @Override
                        public void dialogAction(MOSLCommon.Action action, Object... data) {
                            if (action == MOSLCommon.Action.OK) {
//                                        callApplyNowActivity();
                                updateAddressDetails(userID, CVL);
                            } else {
                                cbCVL.setChecked(false);
                            }
                        }
                    });
                    addSelDialog.show();
                } else {
                    callApplyNowActivity();
                }
            } else if (cbAadhar.isChecked()) {
                if (!getAddressMode().equalsIgnoreCase(AADHARADDDETAIL)) {
                    CustomDialog addSelDialog = new CustomDialog(AddressDetailsActivity.this, getResources().getString(R.string.aadhaar_add_warning), true, true, true, "Address Selection", YES, NO, new CustomDialog.CustomDialogListener() {
                        @Override
                        public void dialogAction(MOSLCommon.Action action, Object... data) {
                            if (action == MOSLCommon.Action.OK) {
//                                        callApplyNowActivity();

                                updateAddressDetails(userID, AADHARADDDETAIL);

                            } else {
                                cbAadhar.setChecked(false);
                            }
                        }
                    });
                    addSelDialog.show();
                } else {
                    callApplyNowActivity();
                }
            } else {
                commonMsgDialog(AddressDetailsActivity.this, "Select Address", false, "");
            }
        }
    }

    private void goToBackPage() {
        if (getAppType().equals(DIRECT)) {

            if (!getIsAadharVerified()) {
                Intent intent = new Intent(AddressDetailsActivity.this, PANAdharRegistActivity.class);
                intent.putExtra(SOURCE, BACK_FROM_PREVIOUS_SCREEN);
                startActivity(intent);
                finish();
            } else {
                if (getInterestedInMF()) {
                    Intent intent = new Intent(AddressDetailsActivity.this, PANAdharRegistActivity.class);
                    intent.putExtra(SOURCE, BACK_FROM_PREVIOUS_SCREEN);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(AddressDetailsActivity.this, PANAdharRegistActivity.class);
                    intent.putExtra(SOURCE, BACK_FROM_PREVIOUS_SCREEN);
                    startActivity(intent);
                    finish();
                }
            }
        } else {
            Intent intent = new Intent(AddressDetailsActivity.this, PANAdharRegistActivity.class);
            intent.putExtra(SOURCE, BACK_FROM_PREVIOUS_SCREEN);
            startActivity(intent);
            finish();
        }
    }

    private void disableEnableField() {
        if (isEquityFundTransfered() || isCommodityFundTransfered() || isMfIpvDone()) {
            disableField(llCVLTitleLayout);
            disableField(llAadharTitleLayout);
            disableField(llAddNewTitleLayout);
        } else {
            enableField(llCVLTitleLayout);
            enableField(llAadharTitleLayout);
            enableField(llAddNewTitleLayout);
        }
    }
}
