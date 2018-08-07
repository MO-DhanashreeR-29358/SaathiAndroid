package com.mosl.mobile.ekyc.directmodule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.register.PANAdharRegistActivity;
import com.mosl.mobile.ekyc.register.RegisterActivity;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import com.mosl.mobile.ekyc.ui.AddressDetailsActivity;
import com.mosl.mobile.ekyc.ui.EkycMainActivity;
import com.mosl.mobile.ekyc.ui.CustomDialog;
import com.mosl.mobile.ekyc.ui.TrackApplicationActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

import static com.mosl.mobile.ekyc.common.MOSLCommon.commonMsgDialog;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isBlank;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isBrokingFundTransferDone;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isValidEmail;
import static com.mosl.mobile.ekyc.common.MOSLCommon.removeKeyboard;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setExistingSegment;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.model.CommonModel.getAddressMode;
import static com.mosl.mobile.ekyc.model.CommonModel.getInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.getRegisterEmail;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.isEquitySegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.isMfSegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.setAadharNo;
import static com.mosl.mobile.ekyc.model.CommonModel.setAddressMode;
import static com.mosl.mobile.ekyc.model.CommonModel.setAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.setCity;
import static com.mosl.mobile.ekyc.model.CommonModel.setGotoFundTransfer;
import static com.mosl.mobile.ekyc.model.CommonModel.setGotoReviewEdit;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsAadharVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsAllSelected;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsBankDetailDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCVLVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCommodityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCommoditySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsEquityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsEquitySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsIPVDone;
import static com.mosl.mobile.ekyc.model.CommonModel.*;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsPersonalDetailDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsReviewEditDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsSchemeDetailDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsSegmentSelected;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsWorkDetailDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setPANName;
import static com.mosl.mobile.ekyc.model.CommonModel.setPANno;
import static com.mosl.mobile.ekyc.model.CommonModel.setRegisterEmail;
import static com.mosl.mobile.ekyc.model.CommonModel.setRegisteredName;
import static com.mosl.mobile.ekyc.model.CommonModel.setUserID;

/**
 * Created by Vijay on 9/6/2016.
 */
public class DirectResumeApplicationActivity extends Activity implements View.OnClickListener, MOSLConstants {

    private TextView tvForgotPassword;
    private EditText etMobileNumber, etPassword;
    private Dialog dialog;

    //Dialog
    private ImageView ivClose;
    private Button btnForgotSubmit, ivSubmit, ivResumeApp, ivTrackApp;
    LinearLayout customProgress;
    private EditText etForgotEmail, etForgotMobileNumber;
    private TextView etForgotEmailError, etForgotMobileNumberError;
    Map<String, Map<String, String>> allStageMap;
    boolean prospectTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dc_resume_application);
            setPermisson(DirectResumeApplicationActivity.this, this);
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            setUpView();
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
//            Crashlytics.logException(e);
        }
    }

    private void setUpView() {

        clearAllCommonModelFields();
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        ivSubmit = (Button) findViewById(R.id.ivSubmit);
        ivResumeApp = (Button) findViewById(R.id.ivResumeApp);
        ivTrackApp = (Button) findViewById(R.id.ivTrackApp);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etPassword = (EditText) findViewById(R.id.etPassword);

        customProgress = (LinearLayout) findViewById(R.id.customProgress);

        tvForgotPassword.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        ivSubmit.setOnClickListener(this);
        ivTrackApp.setOnClickListener(this);
        ivResumeApp.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ivSubmit) {
            if (etMobileNumber.getText().toString().trim().equalsIgnoreCase("")) {
                commonMsgDialog(DirectResumeApplicationActivity.this, "Email/Mobile/User ID cannot be left blank", false, "");
//                MOSLCommon.alertDialog(DirectResumeApplicationActivity.this, 0, "Motilal Oswal", "User Id cannot be left blank", "OK", true, null);
            } else if (etPassword.getText().toString().equalsIgnoreCase("")) {
                commonMsgDialog(DirectResumeApplicationActivity.this, "Password cannot be left blank", false, "");
//                MOSLCommon.alertDialog(DirectResumeApplicationActivity.this, 0, "Motilal Oswal", "Password cannot be left blank", "OK", true, null);
            } else {
                customProgress.setVisibility(View.VISIBLE);
                if (MOSLCommon.isNetworkEnabled(DirectResumeApplicationActivity.this)) {
                    performLogin(etMobileNumber.getText().toString().trim(), etPassword.getText().toString());
                    removeKeyboard(DirectResumeApplicationActivity.this, etPassword);
                } else {
                    customProgress.setVisibility(View.GONE);
                    MOSLCommon.alertDInternet(DirectResumeApplicationActivity.this);
                }
            }

            /* Intent intent = new Intent(DirectResumeApplicationActivity.this, EkycMainActivity.class);
            startActivity(intent);*/
        } else if (i == R.id.tvForgotPassword) {
            forgotPasswordDialog();
        }
        if (i == R.id.ivResumeApp) {
            setAppType(DIRECT);
            Intent intent = new Intent(DirectResumeApplicationActivity.this, RegisterActivity.class);
            startActivity(intent);

        } else if (i == R.id.ivTrackApp) {
            setAppType(DIRECT);
            Intent intent = new Intent(DirectResumeApplicationActivity.this, DirectTrackApplicationLogin.class);
            startActivity(intent);
        }
    }

    public void performLogin(final String clientCode, final String password) {
        //login parameters
        if (MOSLCommon.isNetworkEnabled(this)) {
            Map inputMap = new HashMap<>();
            inputMap.put("_userid", clientCode);
            inputMap.put("_password", password);

            String flag = RESUMEAPPLICATIONLOGIN;
            String category = LOGIN;


            WebServiceHandler.post(this, flag, category, inputMap, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    //after success code goes here
                    try {
                        if (statusCode == 200) {
                            if (response.length() != 0) {
                            /*{"_userid":"1450955","_isnewcustomer":null,
                                    "_msg":"Success","_Success":1,
                                    "_Step":11,"_uniqueId":null,
                                    "_Status":null,"_OTP":null,
                                    "_BusinessUnitId":null,"_Role":null,
                                    "_ShowChangePassword":null}*/
                                String success = (response.isNull("_Success") || response.getString("_Success").equals("")) ? "" : response.getString("_Success").trim();
                                String msg = (response.isNull("_msg") || response.getString("_msg").equals("")) ? "" : response.getString("_msg");
                                String userId = (response.isNull("_userid") || response.getString("_userid").equals("")) ? "" : response.getString("_userid");
                                String step = (response.isNull("_Step") || response.getString("_Step").equals("")) ? "" : response.getString("_Step");
                                String uniqueID = (response.isNull("_uniqueId") || response.getString("_uniqueId").equals("")) ? "" : response.getString("_uniqueId");
                                String status = (response.isNull("_Status") || response.getString("_Status").equals("")) ? "" : response.getString("_Status");
                                String showChangePassword = (response.isNull("_ShowChangePassword") || response.getString("_ShowChangePassword").equals("")) ? "" : response.getString("_ShowChangePassword").trim();
                                String emailID = (response.isNull("_EmailID") || response.getString("_EmailID").equals("")) ? "" : response.getString("_EmailID").trim();

                                setUserID(userId);
                                setRegisterEmail(emailID);
                                if (Integer.parseInt(success) == 1) {
                                    if (Integer.parseInt(showChangePassword) == 1) {
//                                    Change Password code
//                                    getProspectDetails(userId);
                                        customProgress.setVisibility(View.GONE);
                                        ChangePassword cp = new ChangePassword(DirectResumeApplicationActivity.this);
                                        cp.show();
                                    } else if (Integer.parseInt(showChangePassword) == 0) {
//                                    Intent intent = new Intent(DirectResumeApplicationActivity.this, EkycMainActivity.class);
//                                    startActivity(intent);
//                                    finish();
                                        getProspectDetails(userId);
//                                    customProgress.setVisibility(View.GONE);
                                        //onLoginSuccess();
                                    }
                                } else if (Integer.parseInt(success) == 0) {
                                    customProgress.setVisibility(View.GONE);
                                    etPassword.setText("");
                                    showDialog(msg);
                                }
                            } else {
                                customProgress.setVisibility(View.GONE);
                                commonMsgDialog(DirectResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
//                                MOSLCommon.showToastMsg(DirectResumeApplicationActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY);
                            }
                            // customProgress.setVisibility(View.GONE);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        commonMsgDialog(DirectResumeApplicationActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                    MOSLCommon.alertDialog(DirectResumeApplicationActivity.this, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                    }
                    // MOSLCommon.showToastMsg(BranchLoginActivity.this, "Status Code :" + statusCode);

                    //  onLoginSuccess();
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    if (statusCode == 0) {
                        CustomDialog customDialog = new CustomDialog(DirectResumeApplicationActivity.this, CONNECTION_TIMEOUT, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                            @Override
                            public void dialogAction(MOSLCommon.Action action, Object... data) {

                            }
                        });
                        customDialog.show();
                    } else {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(DirectResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(DirectResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(DirectResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

            });
        } else {
            MOSLCommon.alertDInternet(this);
        }
    }

    public void forgotPasswordDialog() {
        final Dialog forgotDialog = new Dialog(DirectResumeApplicationActivity.this);
        forgotDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        forgotDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        forgotDialog.getWindow().setWindowAnimations(R.style.animationName);
        forgotDialog.setContentView(R.layout.dc_forgot_password);
        forgotDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ivClose = (ImageView) forgotDialog.findViewById(R.id.ivClose);
        btnForgotSubmit = (Button) forgotDialog.findViewById(R.id.btnForgotSubmit);
        etForgotEmail = (EditText) forgotDialog.findViewById(R.id.etForgotEmail);
        etForgotMobileNumber = (EditText) forgotDialog.findViewById(R.id.etForgotMobileNumber);
        etForgotEmailError = (TextView) forgotDialog.findViewById(R.id.etForgotEmailError);
        etForgotMobileNumberError = (TextView) forgotDialog.findViewById(R.id.etForgotMobileNumberError);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotDialog.dismiss();
            }
        });

        btnForgotSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etForgotEmail.getText().toString().trim().equals("")) {
                    etForgotEmailError.setText("Enter email id");
                    etForgotEmailError.setVisibility(View.VISIBLE);
                } else if (!isValidEmail(etForgotEmail.getText())) {
                    etForgotEmailError.setText("Email is not valid");
                    etForgotEmailError.setVisibility(View.VISIBLE);
                } else if (etForgotMobileNumber.getText().toString().trim().equals("")) {
                    etForgotEmailError.setVisibility(View.GONE);
                    etForgotMobileNumberError.setText("Enter mobile number");
                    etForgotMobileNumberError.setVisibility(View.VISIBLE);
                } else if (etForgotMobileNumber.length() < 10) {
                    etForgotEmailError.setVisibility(View.GONE);
                    etForgotMobileNumberError.setText("Invalid mobile no.");
                    etForgotMobileNumberError.setVisibility(View.VISIBLE);
                } else {
                    removeKeyboard(DirectResumeApplicationActivity.this, etForgotMobileNumber);
                    etForgotEmailError.setVisibility(View.GONE);
                    etForgotMobileNumberError.setVisibility(View.GONE);
                    forgotPassowrd();
                    forgotDialog.dismiss();
                }
            }
        });

        forgotDialog.show();
    }

    public void showDialog(String msg) {
        final Dialog dialog = new Dialog(DirectResumeApplicationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setWindowAnimations(R.style.animationName);
        dialog.setContentView(R.layout.layout_dialog_info);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
        Button btnOK = (Button) dialog.findViewById(R.id.btnOK);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);

        tvMsg.setText(msg);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customProgress.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customProgress.setVisibility(View.GONE);
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    private void forgotPassowrd() {
        if (MOSLCommon.isNetworkEnabled(DirectResumeApplicationActivity.this)) {
            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put("_emailid", etForgotEmail.getText().toString().trim());
            inputMap.put("_mobile", etForgotMobileNumber.getText().toString().trim());
            inputMap.put("_ApplicationType", DIRECT);
            inputMap.put("_AppLoginId", "");
            String flag = GETFORGOTPASSWORD;
            String category = SIGNUP;

            WebServiceHandler.post(this, flag, category, inputMap, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    //after success code goes here
                    try {
                        if (statusCode == 200) {
                            if (response.length() != 0) {
                            /*{
                              "_userid": null,
                              "_isnewcustomer": null,
                              "_msg": "Password sent on your registered Email Id and Mobile No.",
                              "_Success": 0,
                              "_Step": 0,
                              "_uniqueId": null,
                              "_Status": "1",
                              "_OTP": null,
                              "_BusinessUnitId": null,
                              "_Role": null,
                              "_ApplicationType": null,
                              "_AppLoginId": null,
                              "_ShowChangePassword": null
                            }*/
                                String status = (response.isNull(FORGOTSTATUS) || response.getString(FORGOTSTATUS).equals("")) ? "" : response.getString(FORGOTSTATUS).trim();
                                String msg = (response.isNull(FORGOTMSG) || response.getString(FORGOTMSG).equals("")) ? "" : response.getString(FORGOTMSG);

                                commonMsgDialog(DirectResumeApplicationActivity.this, msg, false, "");
//                                MOSLCommon.alertDialog(DirectResumeApplicationActivity.this, 0, "Motilal Oswal", msg, "OK", true, null);

                            } else {
                                customProgress.setVisibility(View.GONE);
                                commonMsgDialog(DirectResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
//                                MOSLCommon.showToastMsg(DirectResumeApplicationActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(DirectResumeApplicationActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                        MOSLCommon.alertDialog(DirectResumeApplicationActivity.this, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                    }
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(DirectResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");

                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(DirectResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(DirectResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

            });
        } else {
            MOSLCommon.alertDInternet(DirectResumeApplicationActivity.this);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void getProspectDetails(final String userID) {
        setBooleanValueToFalse();
        setAddNewSegment(true);

        if (MOSLCommon.isNetworkEnabled(DirectResumeApplicationActivity.this)) {
            customProgress.setVisibility(View.VISIBLE);

            Map inputMap = new HashMap<>();
            inputMap.put("_userid", userID);
            String flag = GETPROSPECTDETAILS;
            String category = LOGIN;
            WebServiceHandler.post(DirectResumeApplicationActivity.this, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null) {
                        customProgress.setVisibility(View.GONE);
                        prospectTable = false;
                        allStageMap = new HashMap<String, Map<String, String>>();

                        try {

                            if (response.has("PROSPECTDETAILS3")) {
                                JSONArray jsonArraProspectDetails3 = response.getJSONArray("PROSPECTDETAILS3");

                                if (jsonArraProspectDetails3.length() != 0) {
                                    for (int i = 0; i < jsonArraProspectDetails3.length(); i++) {
                                        JSONObject object = jsonArraProspectDetails3.getJSONObject(i);
                                        String product = (object.isNull(PRODUCT) || object.getString(PRODUCT).equals("")) ? "" : object.getString(PRODUCT);
                                        if (product.equalsIgnoreCase(EQUITY) || product.equalsIgnoreCase(PMS)) {
                                            setIsEquityOriginal(true);
                                        }
                                        if (product.equalsIgnoreCase(COMMODITY)) {
                                            setIsCommodityOriginal(true);
                                        }
                                        if (product.equalsIgnoreCase(Derivative)) {
                                            setIsDerivativeOriginal(true);
                                        }
                                        if (product.equalsIgnoreCase(CURRENCY)) {
                                            setIsCurrencyOriginal(true);
                                        }
                                    }
                                }
                            }

                            if (response.has("PROSPECTDETAILS2")) {
                                JSONArray jsonArraProspectDetails2 = response.getJSONArray("PROSPECTDETAILS2");
                                if (jsonArraProspectDetails2.length() != 0) {
                                    for (int i = 0; i < jsonArraProspectDetails2.length(); i++) {
                                        JSONObject object = jsonArraProspectDetails2.getJSONObject(i);
                                        String userId = (object.isNull("USERID") || object.getString("USERID").equals("")) ? "" : object.getString("USERID");
                                        String aadharNo = (object.isNull("AADHARNO") || object.getString("AADHARNO").equals("")) ? "" : object.getString("AADHARNO");
                                        String panNo = (object.isNull("PANNO") || object.getString("PANNO").equals("")) ? "" : object.getString("PANNO");
                                        String iscvlVerified = (object.isNull("ISCVLVERIFIED") || object.getString("ISCVLVERIFIED").equals("")) ? "" : object.getString("ISCVLVERIFIED").trim();
                                        String isAddharVerified = (object.isNull("ISADDHARVERIFIED") || object.getString("ISADDHARVERIFIED").equals("")) ? "" : object.getString("ISADDHARVERIFIED").trim();
                                        String addressMode = (object.isNull("ADDRESSMODE") || object.getString("ADDRESSMODE").equals("")) ? "" : object.getString("ADDRESSMODE");
                                        String dob = (object.isNull("BIRTHDATE") || object.getString("BIRTHDATE").equals("")) ? "" : object.getString("BIRTHDATE");
                                        String city = (object.isNull("CITY") || object.getString("CITY").equals("")) ? "" : object.getString("CITY");
                                        String registerName = (object.isNull("REGISTERNAME") || object.getString("REGISTERNAME").equals("")) ? "" : object.getString("REGISTERNAME");
                                        String interestedIn = (object.isNull("INTRESTEDIN") || object.getString("INTRESTEDIN").equals("")) ? "" : object.getString("INTRESTEDIN");
                                        String nameOnPan = (object.isNull("Name_on_PAN") || object.getString("Name_on_PAN").equals("")) ? "" : object.getString("Name_on_PAN");
                                        String sipIntent = (object.isNull("SIPINTENT") || object.getString("SIPINTENT").equals("")) ? "" : object.getString("SIPINTENT");
                                        String eqSchemeId = (object.isNull("EQSCHEME_ID") || object.getString("EQSCHEME_ID").equals("")) ? "" : object.getString("EQSCHEME_ID");
                                        String userType = (object.isNull("USERTYPE") || object.getString("USERTYPE").equals("")) ? "" : object.getString("USERTYPE");
                                        String baCode = (object.isNull("BACODE") || object.getString("BACODE").equals("")) ? "" : object.getString("BACODE");
                                        String applicationType = (object.isNull("ApplicationType") || object.getString("ApplicationType").equals("")) ? "" : object.getString("ApplicationType");
                                        String tokenKey = (object.isNull("TOKENKEY") || object.getString("TOKENKEY").equals("")) ? "" : object.getString("TOKENKEY");
                                        String downloadPdfUrl = (object.isNull("DOWNLOADPDFURL") || object.getString("DOWNLOADPDFURL").equals("")) ? "" : object.getString("DOWNLOADPDFURL");
                                        String esignService = (object.isNull("ESIGNSERVICE") || object.getString("ESIGNSERVICE").equals("")) ? "" : object.getString("ESIGNSERVICE");
                                        String otpVisible = (object.isNull("OTPVISIBLE") || object.getString("OTPVISIBLE").equals("")) ? "" : object.getString("OTPVISIBLE");
                                        String biometricVisible = (object.isNull("BIOMETRICVISIBLE") || object.getString("BIOMETRICVISIBLE").equals("")) ? "" : object.getString("BIOMETRICVISIBLE");
                                        String isNomineeMandatory = (object.isNull(NOMINEE_MANDATORY) || object.getString(NOMINEE_MANDATORY).equals("")) ? "" : object.getString(NOMINEE_MANDATORY);
                                        if (isBlank(userId)) {
                                            commonMsgDialog(DirectResumeApplicationActivity.this, DATA_IS_MISSING, false, "");
                                            return;
                                        } else {
                                            setUserID(userId);
                                        }
                                        setAadharNo(aadharNo);
                                        setPANno(panNo);
                                        setIsAadharVerified(isAddharVerified != null && isAddharVerified.equals("1") ? true : false);
                                        setIsCVLVerified(iscvlVerified != null && iscvlVerified.equals("1") ? true : false);
                                        setIsNomineeMandatory(isNomineeMandatory != null && isNomineeMandatory.equals("1") ? true : false);
                                        setCity(city);
                                        setRegisteredName(registerName);
                                        setAddressMode(addressMode);
                                        setDob(dob);
                                        setPANName(nameOnPan);
                                        setSipIntent(sipIntent);
                                        setEqSchemeId(eqSchemeId);
                                        setUserType(userType);
                                        setBaFranchiseeCode(baCode);
                                        setAppType(applicationType.toUpperCase());
                                        setTokenKey(tokenKey);
                                        setDownloadPdfUrl(downloadPdfUrl);
                                        setEsignService(esignService);
                                        setOtpVisibleForBranch(otpVisible);
                                        setBioMetricVisibleForBranch(biometricVisible);

                                        // set isDirectProspect flag
                                        if (isBlank(getBaFranchiseeCode())) {
                                            setIsDirectProspect(false);
                                        } else {
                                            setIsDirectProspect(true);
                                            setAppLoginID(getBaFranchiseeCode());
                                        }
                                        JSONArray jsonArraProspectDetails3 = response.getJSONArray("PROSPECTDETAILS3");
                                        if ((jsonArraProspectDetails3.length() == 0)) {
                                            if (interestedIn.equalsIgnoreCase(EQUITY) || interestedIn.equalsIgnoreCase(PMS)) {
                                                setIsEquitySelected(true);
                                            }
                                            if (interestedIn.equalsIgnoreCase(COMMODITY)) {
                                                setIsCommoditySelected(true);
                                            }
                                            if (interestedIn.equalsIgnoreCase(MF)) {
                                                if (!isMfSegmentExist()) {
                                                    setIsInterestedInMF(true);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (prospectTable == false) {
                                        setGotoReviewEdit(false);
                                        setGotoFundTransfer(false);
                                    }
                                    commonMsgDialog(DirectResumeApplicationActivity.this, DATA_IS_MISSING, false, "");
                                    return;
                                }
                            }
                            if (response.has("PROSPECTDETAILS")) {
                                JSONArray jsonArraProspectDetails = response.getJSONArray("PROSPECTDETAILS");

                                if (jsonArraProspectDetails.length() != 0) {
                                    String offlineIpvStatus = null;
                                    for (int i = 0; i < jsonArraProspectDetails.length(); i++) {
                                        prospectTable = true;

                                        JSONObject object = jsonArraProspectDetails.getJSONObject(i);
                                        String segment = (object.isNull(SEGMENT) || object.getString(SEGMENT).equals("")) ? "" : object.getString(SEGMENT);
                                        String registration = (object.isNull(REGISTRATIONPROSPECT) || object.getString(REGISTRATIONPROSPECT).equals("")) ? "" : object.getString(REGISTRATIONPROSPECT);
                                        String segmentSelected = (object.isNull(SEGMENTSELECTEDPROSPECT) || object.getString(SEGMENTSELECTEDPROSPECT).equals("")) ? "" : object.getString(SEGMENTSELECTEDPROSPECT);
                                        String personalDetail = (object.isNull(PERSONALDETAILPROSPECT) || object.getString(PERSONALDETAILPROSPECT).equals("")) ? "" : object.getString(PERSONALDETAILPROSPECT);
                                        String workDetail = (object.isNull(WORKDETAILPROSPECT) || object.getString(WORKDETAILPROSPECT).equals("")) ? "" : object.getString(WORKDETAILPROSPECT);
                                        String demateDetails = (object.isNull(DEMATEDETAILSPROSPECT) || object.getString(DEMATEDETAILSPROSPECT).equals("")) ? "" : object.getString(DEMATEDETAILSPROSPECT);
                                        String bankDetail = (object.isNull(BANKDETAILPROSPECT) || object.getString(BANKDETAILPROSPECT).equals("")) ? "" : object.getString(BANKDETAILPROSPECT);
                                        String schemeDetail = (object.isNull(SCHEMEDETAILPROSPECT) || object.getString(SCHEMEDETAILPROSPECT).equals("")) ? "" : object.getString(SCHEMEDETAILPROSPECT);
                                        String reviewEdit = (object.isNull(REVIEWEDITPROSPECT) || object.getString(REVIEWEDITPROSPECT).equals("")) ? "" : object.getString(REVIEWEDITPROSPECT);
                                        String fundTransfer = (object.isNull(FUNDTRANSFERPROSPECT) || object.getString(FUNDTRANSFERPROSPECT).equals("")) ? "" : object.getString(FUNDTRANSFERPROSPECT);
                                        String documentUploaded = (object.isNull(DOCUMENTUPLOADEDPROSPECT) || object.getString(DOCUMENTUPLOADEDPROSPECT).equals("")) ? "" : object.getString(DOCUMENTUPLOADEDPROSPECT);
                                        String esignCompleted = (object.isNull(ESIGNCOMPLETEDPROSPECT) || object.getString(ESIGNCOMPLETEDPROSPECT).equals("")) ? "" : object.getString(ESIGNCOMPLETEDPROSPECT);
                                        String ipvinitiated = (object.isNull(IPVINITIATEDPROSPECT) || object.getString(IPVINITIATEDPROSPECT).equals("")) ? "" : object.getString(IPVINITIATEDPROSPECT);
                                        String ipvDonebyVerifier = (object.isNull(IPVDONEBYVERIFIERPROSPECT) || object.getString(IPVDONEBYVERIFIERPROSPECT).equals("")) ? "" : object.getString(IPVDONEBYVERIFIERPROSPECT);
                                        String ipvCompleted = (object.isNull(IPVCOMPLETEDPROSPECT) || object.getString(IPVCOMPLETEDPROSPECT).equals("")) ? "" : object.getString(IPVCOMPLETEDPROSPECT);
                                        String client = (object.isNull(CLIENTCODEGENERATEDPROSPECT) || object.getString(CLIENTCODEGENERATEDPROSPECT).equals("")) ? "" : object.getString(CLIENTCODEGENERATEDPROSPECT);
                                        String accountActivated = (object.isNull(ACCOUNTACTIVATEDPROSPECT) || object.getString(ACCOUNTACTIVATEDPROSPECT).equals("")) ? "" : object.getString(ACCOUNTACTIVATEDPROSPECT);
                                        String offlineIpvInitiated = (object.isNull(OFFLINEIPVINITIATEDPROSPECT) || object.getString(OFFLINEIPVINITIATEDPROSPECT).equals("")) ? "" : object.getString(OFFLINEIPVINITIATEDPROSPECT);
                                        offlineIpvStatus = (object.isNull(OFFLINEIPVSTATUSPROSPECT) || object.getString(OFFLINEIPVSTATUSPROSPECT).equals("")) ? "" : object.getString(OFFLINEIPVSTATUSPROSPECT);
                                        String offlineDocumentUploaded = (object.isNull(OFFLINEDOCUMENTUPLOADEDPROSPECT) || object.getString(OFFLINEDOCUMENTUPLOADEDPROSPECT).equals("")) ? "" : object.getString(OFFLINEDOCUMENTUPLOADEDPROSPECT);
                                        String offlineEsignCompleted = (object.isNull(OFFLINEESIGNCOMPLETEDPROSPECT) || object.getString(OFFLINEESIGNCOMPLETEDPROSPECT).equals("")) ? "" : object.getString(OFFLINEESIGNCOMPLETEDPROSPECT);
                                        String offlineIpvCompleted = (object.isNull(OFFLINEIPVCOMPLETEDPROSPECT) || object.getString(OFFLINEIPVCOMPLETEDPROSPECT).equals("")) ? "" : object.getString(OFFLINEIPVCOMPLETEDPROSPECT);
                                        String offlineClientCodeGenerated = (object.isNull(OFFLINECLIENTCODEGENERATEDPROSPECT) || object.getString(OFFLINECLIENTCODEGENERATEDPROSPECT).equals("")) ? "" : object.getString(OFFLINECLIENTCODEGENERATEDPROSPECT);

                                        // Payment status
                                        if (segment.equalsIgnoreCase(EQUITY) || segment.equalsIgnoreCase(PMS)) {
                                            setIsEquityOriginal(true);
                                            setIsEquitySelected(true);

                                            if (fundTransfer.equalsIgnoreCase("Y")) {
                                                setIsEquityFundTransfered(true);
                                            } else {
                                                setIsEquityFundTransfered(false);
                                            }

                                        } else if (segment.equalsIgnoreCase(COMMODITY)) {
                                            setIsCommodityOriginal(true);
                                            setIsCommoditySelected(true);

                                            if (fundTransfer.equalsIgnoreCase("Y")) {
                                                setIsCommodityFundTransfered(true);
                                            } else {
                                                setIsCommodityFundTransfered(false);
                                            }
                                        } else if (segment.equalsIgnoreCase(MF)) {
                                            setIsInterestedInMF(true);
                                        }
                                            /*1. for manual case - broking,fund transfered and aadhar not verified show second segent pop up
                                                2. for non manual case if client code is generated and client code  generated stage is 'Y' show second segent pop up */

                                        //broking and without aadhaar (manual)
                                        if (!getInterestedInMF() && !getIsAadharVerified()) {
                                            if (!isBrokingFundTransferDone(segment)) { //in-progress
                                                setAddNewSegment(false);   //NO POPUP
                                            } else { //manual completed
                                                setExistingSegment(segment);
                                            }
                                        }

                                        //all products with aadhaar

                                        else if (!isBlank(client) && client.equalsIgnoreCase("Y")) {
                                            setExistingSegment(segment);
                                            continue; //segment completed
                                        } else {
                                            setAddNewSegment(false); //in-progress
                                        }

                                        /*//////////
                                        if ((!getInterestedInMF() && isFundTransferDone() && !getIsAadharVerified()) || (!isBlank(client) && client.equalsIgnoreCase("Y"))) {
                                            continue;
                                        } else {
                                            setAddNewSegment(false);
                                        }
*/
                                        Map stageMap = new HashMap();
                                        stageMap.put(REGISTRATION_STAGE, registration);
                                        stageMap.put(SEGMENTSELECTED_STAGE, segmentSelected);
                                        stageMap.put(PERSONALDETAIL_STAGE, personalDetail);
                                        stageMap.put(WORKDETAIL_STAGE, workDetail);
                                        stageMap.put(DEMATEDETAILS_STAGE, demateDetails);
                                        stageMap.put(BANKDETAIL_STAGE, bankDetail);
                                        stageMap.put(SCHEMEDETAIL_STAGE, schemeDetail);
                                        stageMap.put(REVIEWEDIT_STAGE, reviewEdit);
                                        stageMap.put(FUNDTRANSFER_STAGE, fundTransfer);
                                        stageMap.put(DOCUMENTUPLOADED_STAGE, documentUploaded);
                                        stageMap.put(ESIGNCOMPLETED_STAGE, esignCompleted);
                                        stageMap.put(IPVINITIATED_STAGE, ipvinitiated);
                                        stageMap.put(IPVDONEBYVERIFIER_STAGE, ipvDonebyVerifier);
                                        stageMap.put(IPVCOMPLETED_STAGE, ipvCompleted);
                                        stageMap.put(CLIENTCODEGENERATED_STAGE, client);
                                        stageMap.put(ACCOUNTACTIVATED_STAGE, accountActivated);
                                        stageMap.put(OFFLINEIPVINITIATED_STAGE, offlineIpvInitiated);
                                        stageMap.put(OFFLINEIPVSTATUS_STAGE, offlineIpvStatus);
                                        stageMap.put(OFFLINEDOCUMENTUPLOADED_STAGE, offlineDocumentUploaded);
                                        stageMap.put(OFFLINEESIGNCOMPLETED_STAGE, offlineEsignCompleted);
                                        stageMap.put(OFFLINEIPVCOMPLETED_STAGE, offlineIpvCompleted);
                                        stageMap.put(OFFLINECLIENTCODEGENERATED_STAGE, offlineClientCodeGenerated);

                                        if (segment.equalsIgnoreCase(EQUITY) && !isEquitySegmentExist()) {
                                            allStageMap.put(segment, stageMap);
                                        }
                                        if (segment.equalsIgnoreCase(COMMODITY) && !isCommoditySegmentExist()) {
                                            allStageMap.put(segment, stageMap);
                                        }
                                        if (segment.equalsIgnoreCase(MF) && !isMfSegmentExist()) {
                                            allStageMap.put(segment, stageMap);
                                        }
                                    }
                                    setGotoReviewEdit(false);
                                    setGotoFundTransfer(false);
                                } else {
                                    prospectTable = false;
                                }
                            }

                            if (prospectTable == true) {
                                setIsOffline(setOffLineStatus());
                            } else {
                                setIsOffline(false);
                            }
                            if (prospectTable == true && !isBlank(getAddressMode())) {
                                goToNextPage();
                            } else {
                                if (getInterestedInMF()) {
                                    if (MOSLCommon.isBlank(getPANno())) {
                                        setGotoReviewEdit(false);
                                        setGotoFundTransfer(false);
                                        Intent intent = new Intent(DirectResumeApplicationActivity.this, PANAdharRegistActivity.class);
                                        intent.putExtra(SOURCE, PAN_AADHAR_PENDING);
                                        startActivity(intent);
                                        finish();
                                    } else if (MOSLCommon.isBlank(getAddressMode())) {
                                        setGotoReviewEdit(false);
                                        setGotoFundTransfer(false);
//                                                Intent intent = new Intent(DirectResumeApplicationActivity.this, PANAdharRegistActivity.class);
                                        Intent intent = new Intent(DirectResumeApplicationActivity.this, AddressDetailsActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else { //goto Personal detail
                                        setGotoReviewEdit(false);
                                        setGotoFundTransfer(false);
                                        Intent intent = new Intent(DirectResumeApplicationActivity.this, EkycMainActivity.class);
                                        intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    //Broking
                                    if (MOSLCommon.isBlank(getPANno())) {
                                        setGotoReviewEdit(false);
                                        setGotoFundTransfer(false);
                                        Intent intent = new Intent(DirectResumeApplicationActivity.this, PANAdharRegistActivity.class);
                                        intent.putExtra(SOURCE, PAN_AADHAR_PENDING);
                                        startActivity(intent);
                                        finish();
                                    } else if (!getIsAadharVerified()) {
                                        if (MOSLCommon.isBlank(getAddressMode())) {
                                            setGotoReviewEdit(false);
                                            setGotoFundTransfer(false);
                                            Intent intent = new Intent(DirectResumeApplicationActivity.this, PANAdharRegistActivity.class);
                                            intent.putExtra(SOURCE, PAN_AADHAR_PENDING);
//                                                Intent intent = new Intent(DirectResumeApplicationActivity.this, AddressDetailsActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            setGotoReviewEdit(false);
                                            setGotoFundTransfer(false);
                                            Intent intent = new Intent(DirectResumeApplicationActivity.this, EkycMainActivity.class);
                                            intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
                                            startActivity(intent);
                                            finish();
                                        }
                                    } else if (MOSLCommon.isBlank(getAddressMode())) { //goto Pan Adhaar
                                        setGotoReviewEdit(false);
                                        setGotoFundTransfer(false);
//                                                Intent intent = new Intent(DirectResumeApplicationActivity.this, PANAdharRegistActivity.class);
                                        Intent intent = new Intent(DirectResumeApplicationActivity.this, AddressDetailsActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else { //goto Personal detail
                                        setGotoReviewEdit(false);
                                        setGotoFundTransfer(false);
                                        Intent intent = new Intent(DirectResumeApplicationActivity.this, EkycMainActivity.class);
                                        intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            commonMsgDialog(DirectResumeApplicationActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                        }

                    } else {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(DirectResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
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
                    commonMsgDialog(DirectResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                    if (statusCode == 0) {
                        commonMsgDialog(DirectResumeApplicationActivity.this, CONNECTION_TIMEOUT, false, "");
                    } else {
                        commonMsgDialog(DirectResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(DirectResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });
        } else {
            MOSLCommon.alertDInternet(DirectResumeApplicationActivity.this);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void setBooleanValueToFalse() {
        setGotoFundTransfer(false);
        setGotoReviewEdit(false);
        setIsSegmentSelected(false);
        setIsPersonalDetailDone(false);
        setIsWorkDetailDone(false);
        setIsBankDetailDone(false);
        setIsSchemeDetailDone(false);
        setIsReviewEditDone(false);
//        setIsFundTransferDone(false);
        setIsIPVDone(false);
        setIsOfflineDocumentUploadedDone(false);
        setIsEquityFundTransfered(false);
        setIsCommodityFundTransfered(false);
        setIsEquityIpvDone(false);
        setIsMfIpvDone(false);
    }

    private void goToNextPage() {
        if (isAddNewSegment()) {
            showSecondProductOption(getUserID());
            return;
        }

        Set<String> segmentSet = allStageMap.keySet();
        for (String segment : segmentSet) {
            if (segment.equalsIgnoreCase(EQUITY) && isEquitySegmentExist()) {
                if (allStageMap.size() == 1) {
                    if (getIsAadharVerified()) {
                        Intent intent = new Intent(DirectResumeApplicationActivity.this, EkycMainActivity.class);
                        intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
                        startActivity(intent);
                        finish();
                        break;
                    } else {
                        Intent intent = new Intent(DirectResumeApplicationActivity.this, PANAdharRegistActivity.class);
                        intent.putExtra(SOURCE, ADD_SEGMENT);
                        startActivity(intent);
                        finish();
                        break;
                    }
                } else {
                    continue;
                }
            } else if (segment.equalsIgnoreCase(COMMODITY) && isCommoditySegmentExist()) {
                if (allStageMap.size() == 1) {
                    if (getIsAadharVerified()) {
                        Intent intent = new Intent(DirectResumeApplicationActivity.this, EkycMainActivity.class);
                        intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
                        startActivity(intent);
                        finish();
                        break;
                    } else {
                        Intent intent = new Intent(DirectResumeApplicationActivity.this, PANAdharRegistActivity.class);
                        intent.putExtra(SOURCE, ADD_SEGMENT);
                        startActivity(intent);
                        finish();
                        break;
                    }
                } else {
                    continue;
                }
            } else if (segment.equalsIgnoreCase(MF) && isMfSegmentExist()) {
                continue;
            }
            Map<String, String> currentStageMap = (HashMap) allStageMap.get(segment);

            if (isStagePending(REGISTRATION_STAGE)) {
                //goto PAN-ADHAAR
                Intent intent = new Intent(DirectResumeApplicationActivity.this, PANAdharRegistActivity.class);
                intent.putExtra(SOURCE, PAN_AADHAR_PENDING);
//                RegisterModel.setUserID(userID);
                startActivity(intent);
                finish();
                return;
            } else if (isStagePending(SEGMENTSELECTED_STAGE)
                    || isStagePending(PERSONALDETAIL_STAGE)
                    || isStagePending(WORKDETAIL_STAGE)) {
                //goto personal detail
                Intent intent = new Intent(DirectResumeApplicationActivity.this, EkycMainActivity.class);
                intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
                setUserID(getUserID());
                startActivity(intent);
                finish();
                return;
            } else if (isStagePending(DEMATEDETAILS_STAGE) && !(getIsAadharVerified() && !getInterestedInMF())) {
                //goto demat detail
                setIsSegmentSelected(true);
                Intent intent = new Intent(DirectResumeApplicationActivity.this, EkycMainActivity.class);
                intent.putExtra(TAB_DETAIL, DEMAT_DETAIL);
                setUserID(getUserID());
                startActivity(intent);
                finish();
                return;
            } else if (isStagePending(BANKDETAIL_STAGE)
                    || (isStagePending(SCHEMEDETAIL_STAGE) && !getInterestedInMF())) {
                //goto Bank detail
                setGotoReviewEdit(false);
                setGotoFundTransfer(false);
                setIsSegmentSelected(true);
                setIsPersonalDetailDone(true);
                setIsWorkDetailDone(true);
                Intent intent = new Intent(DirectResumeApplicationActivity.this, EkycMainActivity.class);
                intent.putExtra(TAB_DETAIL, BANK_DETAIL);
                setUserID(getUserID());
                startActivity(intent);
                finish();
                return;
            } else if (isStagePending(REVIEWEDIT_STAGE) && !getInterestedInMF()) {
                //goto Review
                setGotoReviewEdit(true);
                setIsSegmentSelected(true);
                setIsPersonalDetailDone(true);
                setIsWorkDetailDone(true);
                setIsBankDetailDone(true);
                setIsSchemeDetailDone(true);
                Intent intent = new Intent(DirectResumeApplicationActivity.this, EkycMainActivity.class);
                intent.putExtra(TAB_DETAIL, BANK_DETAIL);
                setUserID(getUserID());
                startActivity(intent);
                finish();
                return;
            } else if (isStagePending(FUNDTRANSFER_STAGE) && !getInterestedInMF()) {
                //goto Fundtransfer
                setGotoFundTransfer(true);
                setIsReviewEditDone(true);
                setIsSegmentSelected(true);
                setIsPersonalDetailDone(true);
                setIsWorkDetailDone(true);
                setIsBankDetailDone(true);
                setIsSchemeDetailDone(true);
                Intent intent = new Intent(DirectResumeApplicationActivity.this, EkycMainActivity.class);
                intent.putExtra(TAB_DETAIL, BANK_DETAIL);
                setUserID(getUserID());
                startActivity(intent);
                finish();
                return;
            } else if (isOffline() ? (isStagePending(OFFLINEDOCUMENTUPLOADED_STAGE) || (!isStagePending(OFFLINEESIGNCOMPLETED_STAGE)
                    && !isStagePending(OFFLINECLIENTCODEGENERATED_STAGE)) && isStagePending(OFFLINEIPVCOMPLETED_STAGE))
                    : (isStagePending(DOCUMENTUPLOADED_STAGE)
                    || isStagePending(IPVCOMPLETED_STAGE))) {//offline condition added
                //goto Upload
                setGotoFundTransfer(true);
                setIsReviewEditDone(true);
                setIsSegmentSelected(true);
                setIsPersonalDetailDone(true);
                setIsWorkDetailDone(true);
                setIsBankDetailDone(true);
                setIsSchemeDetailDone(true);
//                setIsFundTransferDone(true);
                setIsIPVDone(false);
                if (isStagePending(OFFLINEDOCUMENTUPLOADED_STAGE)) {
                    setIsOfflineDocumentUploadedDone(false);
                } else {
                    setIsOfflineDocumentUploadedDone(true);
                }
                if (isStagePending(OFFLINECLIENTCODEGENERATED_STAGE)) {
                    setIsOfflineCliencodeGenrated(false);
                } else {
                    setIsOfflineCliencodeGenrated(true);
                }
                Intent intent = new Intent(DirectResumeApplicationActivity.this, EkycMainActivity.class);
                intent.putExtra(TAB_DETAIL, UPLOAD_DETAIL);
                setUserID(getUserID());
                startActivity(intent);
                finish();
                return;
            } else if (isOffline() ? (isStagePending(OFFLINEESIGNCOMPLETED_STAGE)
                    || isStagePending(OFFLINECLIENTCODEGENERATED_STAGE))
                    : (isStagePending(ESIGNCOMPLETED_STAGE)
                    || isStagePending(CLIENTCODEGENERATED_STAGE)
                    || isStagePending(ACCOUNTACTIVATED_STAGE))) {//offline condition added
                //goto Esign
                setIsReviewEditDone(true);
                setIsSegmentSelected(true);
                setIsPersonalDetailDone(true);
                setIsWorkDetailDone(true);
                setIsBankDetailDone(true);
                setIsSchemeDetailDone(true);
//                setIsFundTransferDone(true);
                setIsIPVDone(true);
                setIsOfflineDocumentUploadedDone(true);
                if (isStagePending(OFFLINECLIENTCODEGENERATED_STAGE)) {
                    setIsOfflineCliencodeGenrated(false);
                } else {
                    setIsOfflineCliencodeGenrated(true);
                }
                if (segment.equalsIgnoreCase(EQUITY)) {
                    setIsEquityIpvDone(true);
                }
                if (segment.equalsIgnoreCase(MF)) {
                    setIsMfIpvDone(true);
                }
                Intent intent = new Intent(getApplicationContext(), EkycMainActivity.class);
                intent.putExtra(PDF_PATH, "");
                intent.putExtra(TAB_DETAIL, ESIGN_DETAIL);//todo pass from prospect-API
                //intent.putExtra(AADHAR, aadharNo);
                setUserID(getUserID());
                startActivity(intent);
                finish();
                return;
            } else if (!isStagePending(CLIENTCODEGENERATED_STAGE)) {
                //CALL FOR SECOND PRODUCT LIST
                setIsMfIpvDone(false);
            }
        }
    }

    /*
    * Set Offline flag form Prospect API
    * */
    private boolean setOffLineStatus() {
        Set<String> segmentSet = allStageMap.keySet();
        if (allStageMap.size() == 1) {
            String segment = segmentSet.iterator().next();
            Map<String, String> currentStageMap = (HashMap) allStageMap.get(segment);
            if (currentStageMap.get(OFFLINEIPVSTATUS_STAGE).equals("0")) {
                return true;
            } else {
                return false;
            }
        } else if (allStageMap.size() == 2) {
            Iterator iterator = segmentSet.iterator();
            String segment1 = iterator.next().toString();
            String segment2 = iterator.next().toString();
            Map<String, String> currentStageMap1 = (HashMap) allStageMap.get(segment1);
            Map<String, String> currentStageMap2 = (HashMap) allStageMap.get(segment2);
            if (currentStageMap1.get(OFFLINEIPVSTATUS_STAGE).equals("0") && currentStageMap2.get(OFFLINEIPVSTATUS_STAGE).equals("0")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * will check only in-process segments
     *
     * @param STAGE
     * @return
     */
    private boolean isStagePending(String STAGE) {
        Set<String> segmentSet = allStageMap.keySet();

        if (allStageMap.size() == 1) {

            String segment = segmentSet.iterator().next();
            Map<String, String> currentStageMap = (HashMap) allStageMap.get(segment);
            if (currentStageMap.get(STAGE).equals("Y")) {
                return false;
            } else {
                return true;
            }
        } else if (allStageMap.size() == 2) {
            Iterator iterator = segmentSet.iterator();
            String segment1 = iterator.next().toString();
            String segment2 = iterator.next().toString();


            Map<String, String> currentStageMap1 = (HashMap) allStageMap.get(segment1);
            Map<String, String> currentStageMap2 = (HashMap) allStageMap.get(segment2);

            if (currentStageMap1.get(STAGE).equals("Y") && currentStageMap2.get(STAGE).equals("Y")) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public class ChangePassword extends Dialog implements MOSLConstants {

        Context mContext;
        private ImageView ivClose;
        RelativeLayout customProgress;
        private Button btnChangePasswordSubmit, btnChangePasswordReset;
        private EditText etChangePasswordUserId, etOldPassword, etNewPassword, etConfirmPassword, etNewPasswordError, etConfirmPasswordError;
        private TextInputLayout input_layout_oldPassword, input_layout_newPassword, input_layout_confirmPassword;

        public ChangePassword(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setWindowAnimations(R.style.animationName);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setContentView(R.layout.dc_change_password);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setCanceledOnTouchOutside(false);
            setUpView();
        }

        private void setUpView() {
            customProgress = (RelativeLayout) findViewById(R.id.customProgress);
            ivClose = (ImageView) findViewById(R.id.ivClose);
            btnChangePasswordSubmit = (Button) findViewById(R.id.btnChangePasswordSubmit);
            btnChangePasswordReset = (Button) findViewById(R.id.btnChangePasswordReset);
            etChangePasswordUserId = (EditText) findViewById(R.id.etChangePasswordUserId);
            etOldPassword = (EditText) findViewById(R.id.etOldPassword);
            etNewPassword = (EditText) findViewById(R.id.etNewPassword);
            etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
            etConfirmPasswordError = (EditText) findViewById(R.id.etConfirmPasswordError);
            etNewPasswordError = (EditText) findViewById(R.id.etNewPasswordError);
            input_layout_oldPassword = (TextInputLayout) findViewById(R.id.input_layout_oldPassword);
            input_layout_newPassword = (TextInputLayout) findViewById(R.id.input_layout_newPassword);
            input_layout_confirmPassword = (TextInputLayout) findViewById(R.id.input_layout_confirmPassword);

            etChangePasswordUserId.setText(getRegisterEmail());
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            btnChangePasswordReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etOldPassword.setText("");
                    etNewPassword.setText("");
                    etConfirmPassword.setText("");
                }
            });

            btnChangePasswordSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!validateEditText(etOldPassword, input_layout_oldPassword, "Please enter Current password")) {
                        return;
                    } else if (!validateEditText(etNewPassword, input_layout_newPassword, "Please enter New password")) {
                        return;
                    } else if (!validateEditText(etConfirmPassword, input_layout_confirmPassword, "Please enter Confirm Password")) {
                        etNewPasswordError.setVisibility(View.GONE);
                        return;
                    } else if (!etNewPassword.getText().toString().trim().equals(etConfirmPassword.getText().toString().trim())) {
                        etConfirmPassword.setText("");
                        etConfirmPasswordError.setText("Confirm password does not match with new password");
                        etConfirmPasswordError.setVisibility(View.VISIBLE);
                    } else {
                        etNewPasswordError.setVisibility(View.GONE);
                        etConfirmPasswordError.setVisibility(View.GONE);
                        removeKeyboard(mContext, etConfirmPassword);
                        changePassword(etNewPassword.getText().toString().trim(), etOldPassword.getText().toString().trim());
                    }
                }
            });
        }

        private void changePassword(String newPassword, String oldPassword) {
            if (MOSLCommon.isNetworkEnabled(getContext())) {
                Map inputMap = new HashMap<>();
                inputMap.put("_USERID", getUserID());
                inputMap.put("_NewPassword", newPassword);
                inputMap.put("_CurrentPassword", oldPassword);

                String flag = CHANGEPASSWORD;
                String category = LOGIN;
                customProgress.setVisibility(View.VISIBLE);
                WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if (statusCode == 200) {
                                if (response != null && response.length() != 0) {
                            /*"_userid": null,
                            "_isnewcustomer": null,
                            "_msg": "Password Update Successfully",
                            "_Success": 1,
                            "_Step": 0,
                            "_uniqueId": null,
                            "_Status": null,
                            "_OTP": null,
                            "_BusinessUnitId": null,
                            "_Role": null,
                            "_ApplicationType": null,
                            "_AppLoginId": null,
                            "_ShowChangePassword": null*/

                                    String success = (response.isNull("_Success") || response.getString("_Success").equals("")) ? "" : response.getString("_Success");
                                    String msg = (response.isNull("_msg") || response.getString("_msg").equals("")) ? "" : response.getString("_msg");

                                    if (Integer.parseInt(success) == 1) {
                                   /*      MOSLCommon.alertDialog(mContext, 0, "Motilal Oswal", msg, "OK", true, new MOSLCommon.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MOSLCommon.Action action, Object... data) {
                                                if (action == MOSLCommon.Action.OK) {
                                                    dismiss();
                                                    getProspectDetails(getUserID());
                                                }
                                            }
                                        });*/
                                        CustomDialog cd = new CustomDialog(DirectResumeApplicationActivity.this, msg, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                                            @Override
                                            public void dialogAction(MOSLCommon.Action action, Object... data) {
                                                if (action == MOSLCommon.Action.OK) {
                                                    dismiss();
                                                    getProspectDetails(getUserID());
                                                }
                                            }
                                        });
                                        cd.show();
                                    } else if (Integer.parseInt(success) == 0) {
                                        commonMsgDialog(DirectResumeApplicationActivity.this, msg, false, "");
//                                        MOSLCommon.alertDialog(mContext, 0, "Motilal Oswal", msg, "OK", true, null);
                                    }
                                } else {
                                    customProgress.setVisibility(View.GONE);
                                    commonMsgDialog(DirectResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            commonMsgDialog(DirectResumeApplicationActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                            MOSLCommon.alertDialog(mContext, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                        }
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(DirectResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(DirectResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(DirectResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                });
            } else {
                customProgress.setVisibility(View.GONE);
            }
        }

        private boolean validateEditText(EditText editText, TextInputLayout textInputLayout, String error) {
            if (editText.getText().toString().trim().isEmpty()) {
                textInputLayout.setError(error);
                editText.requestFocus();
                return false;
            } else {
                textInputLayout.setErrorEnabled(false);
            }

            return true;
        }
    }

    /////
    private void showSecondProductOption(String userId) {
        if (MOSLCommon.isNetworkEnabled(DirectResumeApplicationActivity.this)) {
            customProgress.setVisibility(View.VISIBLE);

            Map inputMap = new HashMap<>();

            inputMap.put("_userid", userId);
            inputMap.put(APP_LOGIN_ID, getAppLoginID());
            inputMap.put(APPLICATION_TYPE, getAppType());
            String flag = GETAVAILABLEPRODUCT;
            String category = SIGNUP;
            WebServiceHandler.post(DirectResumeApplicationActivity.this, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (response != null) {
                            JSONArray dataArray = response.getJSONArray("AvailableProduct");
                            List<String> productList = new ArrayList();
                            if (dataArray != null && dataArray.length() != 0) {

                                for (int ctr = 0; ctr < dataArray.length(); ctr++) {
                                    JSONObject dataObject = (JSONObject) dataArray.get(ctr);
                                    String product = dataObject.getString("Product");
                                    productList.add(product);
                                }
                            }

                           /* MOSLCommon.showPopUpThreeLine(getContext(), lvLeads, ADD_NEW_SEGMENT,
                                    MOSLConstants.PANADHAR_ACTIVITY, false, "", getUserID(), true, productList);*/

                            /*boolean showSecodProductPopup = false;
                            if (productList != null && productList.size() != 0) {

                                for (String prd : productList) {
                                    if (prd.equalsIgnoreCase(EQUITY) && !isEquitySegmentExist() && isEquityFundTransfered()) {
                                        showSecodProductPopup = false;
                                        goToNextPage(showSecodProductPopup);
                                        break;
                                    } else if (prd.equalsIgnoreCase(COMMODITY) && !isCommoditySegmentExist() && isCommodityFundTransfered()) {
                                        showSecodProductPopup = false;
                                        goToNextPage(showSecodProductPopup);
                                        break;
                                    } else {
                                        showSecodProductPopup = true;
                                    }
                                }
                            } else {
                                secondProductPopup(productList);
                            }

                            if (showSecodProductPopup) {
                                secondProductPopup(productList);
                            }*/
                            secondProductPopup(productList);
                        }
                        customProgress.setVisibility(View.GONE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        customProgress.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    try {

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    try {

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    customProgress.setVisibility(View.GONE);
                }
            });
        }

    }

    private void secondProductPopup(List<String> productList) {
        dialog = new Dialog(this, android.R.style.Theme_Light);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.second_product_popup);
        //dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(ADD_NEW_SEGMENT);
        ImageView ivEquity = (ImageView) dialog.findViewById(R.id.ivEquity);
        ImageView ivCommodity = (ImageView) dialog.findViewById(R.id.ivCommodity);
        ImageView ivAll = (ImageView) dialog.findViewById(R.id.ivAll);
        ImageView ivTrackApp = (ImageView) dialog.findViewById(R.id.ivTrackApp);
        ImageView btnClose = (ImageView) dialog.findViewById(R.id.btnClose);


        //show list of 2nd product only if fund_transfer is pending for newly added product
        if (productList != null && productList.size() != 0) {

            if (productList.size() > 1) {
                if (!isEquityFundTransfered() && !isCommodityFundTransfered()) {
                    ivAll.setVisibility(View.VISIBLE);
                }
            } else {
                ivAll.setVisibility(View.GONE);
            }

            for (String product : productList) {
                if (product.equalsIgnoreCase(EQUITY) && !isEquityFundTransfered()) {
                    ivEquity.setVisibility(View.VISIBLE);
                } else if (product.equalsIgnoreCase(COMMODITY) && !isCommodityFundTransfered()) {
                    ivCommodity.setVisibility(View.VISIBLE);
                }
            }

        } else {
            tvMessage.setText(ALL_SEGMENT_COMPLETED);
        }
        ivEquity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.dismiss();
                setIsEquitySelected(true);
                setIsEquityOriginal(true);
                continueWithSecondProduct();

            }
        });
        ivCommodity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.dismiss();
                setIsCommoditySelected(true);
                setIsCommodityOriginal(true);
                continueWithSecondProduct();

            }
        });
        ivAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.dismiss();
                setIsAllSelected(true);
                setIsAllOriginal(true);
                continueWithSecondProduct();

            }
        });
        ivTrackApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), TrackApplicationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllCommonModelFields();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void continueWithSecondProduct() {
        if (allStageMap.size() == 0) {
            prospectTable = false;
        }

        if (!getIsAadharVerified()) {
         /*   if (prospectTable) {
                goToNextPage();
            } else {*/
            if (getInterestedInMF()) {
                setIsInterestedInMF(false);
            }
            Intent intent = new Intent(getApplicationContext(), PANAdharRegistActivity.class);
            intent.putExtra(SOURCE, ADD_SEGMENT);
            startActivity(intent);
            finish();
//            }

        } else {
            /*if (prospectTable) {
                goToNextPage();
            } else {*/
            Intent intent = new Intent(getApplicationContext(), EkycMainActivity.class);
            intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
            startActivity(intent);
            finish();
//            }
        }
    }
}
