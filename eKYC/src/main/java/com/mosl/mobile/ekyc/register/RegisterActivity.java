package com.mosl.mobile.ekyc.register;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.common.SearchableSpinner;
import com.mosl.mobile.ekyc.common.ValidationRule;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.model.RoleSelectionModel;
import com.mosl.mobile.ekyc.model.RowData;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import com.mosl.mobile.ekyc.ui.CustomDialog;
import cz.msebera.android.httpclient.Header;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.mosl.mobile.ekyc.common.MOSLCommon.commonMsgDialog;
import static com.mosl.mobile.ekyc.common.MOSLCommon.getDeviceID;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isBlank;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isValidEmail;
import static com.mosl.mobile.ekyc.common.MOSLCommon.removeKeyboard;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.model.CommonModel.clearAllCommonModelFields;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppType;
import static com.mosl.mobile.ekyc.model.CommonModel.getInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.getRegisterEmail;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.setAadhaarByPass;
import static com.mosl.mobile.ekyc.model.CommonModel.setAppType;
import static com.mosl.mobile.ekyc.model.CommonModel.setBaFranchiseeCode;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsDirectProspect;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.setRegisterEmail;
import static com.mosl.mobile.ekyc.model.CommonModel.setRegisteredName;
import static com.mosl.mobile.ekyc.model.CommonModel.setUserID;

/**
 * Created by Owner on 6/24/2016.
 */
public class RegisterActivity extends Activity implements MOSLConstants {

    public static String userIDStr;
    private Spinner spInterestedIn;
    private SearchableSpinner spState, spCity;
    private Button btnProceed, btnSendRepresentative;
    private EditText etName, etEmail, etMobile, etPincode, etBABranchCode;
    private LinearLayout customProgress;
    private Dialog dialog;
    private ArrayList<RowData> arrState, arrCity, arrInterested;
    private ArrayAdapter<RowData> arrAdapState, arrAdapCity, arrAdapInterested;
    private CheckBox cbTC;
    private TextView tvTC, tvMsg, tvMsg1, tvMsg2, tvMsg3;
    private String stateCodeStr, cityCodeStr, stateNameStr, cityNameStr, interestedInStr;
    private Button btnOK;
    private ImageView ivClose, ivMotilalOswal;
    private EditText etStateError, etCityError, etInterestedInError, etMobileError, etPincodeError,
        etNameError, etEmailError;
    private TextInputLayout input_layout_name, input_layout_email, input_layout_mobile,
        inputLayoutPincode, inputLayoutBABranchCode;
    private LinearLayout llPincode, llCity, llState, llBABranchCode, llEditText;
    private ScrollView scrollview;
    private AdapterView.OnItemSelectedListener interestedInListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                interestedInStr = ((RowData) spInterestedIn.getSelectedItem()).getKey();
                if (!interestedInStr.equals("")) {
                    if (interestedInStr.equals(MF) || interestedInStr.equalsIgnoreCase("SIP")) {
                        setIsInterestedInMF(true);
                    } else {
                        setIsInterestedInMF(false);
                    }
                }
                etInterestedInError.setVisibility(View.GONE);
                //getCity(stateCodeStr);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private AdapterView.OnItemSelectedListener cityListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityCodeStr = ((RowData) spCity.getSelectedItem()).getKey();
                cityNameStr = ((RowData) spCity.getSelectedItem()).getValue();
                etCityError.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private AdapterView.OnItemSelectedListener stateListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateCodeStr = ((RowData) spState.getSelectedItem()).getKey();
                stateNameStr = ((RowData) spState.getSelectedItem()).getValue();
                etStateError.setVisibility(View.GONE);
                getCity(stateCodeStr);
                if (!stateCodeStr.equals("") && !stateCodeStr.equals("-1")) {
                    spCity.setEnabled(true);
                } else {
                    spCity.setEnabled(false);
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);
            setPermisson(RegisterActivity.this, this);
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            setUpView();
        } catch (Exception e) {
            e.printStackTrace();
            //            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
            //            Crashlytics.logException(e);
        }
    }

    private void sendRepresentative(final String name, final String emailId, final String mob,
        String otp, String uniqueId) {

        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put("_name", name);
            inputMap.put("_emailid", emailId);
            inputMap.put("_mobile", mob);
            inputMap.put("_state", stateNameStr);
            inputMap.put("_pincode", etPincode.getText().toString());
            inputMap.put("_city", cityNameStr);
            inputMap.put("_strPSource", OAO);
            inputMap.put("_strPSourceDetails", OAO);
            inputMap.put("_strSource", ONLINE);
            inputMap.put("_interestedin", interestedInStr);
            inputMap.put("_myIP", getDeviceID(RegisterActivity.this, getContentResolver()));
            inputMap.put(APPLICATION_TYPE, getAppType());
            inputMap.put("_Otp", otp);
            inputMap.put("_uniqueId", uniqueId);

            inputMap.put("_applicationFrom", "Mobile");
            if (getAppType().equals(CRM)) {
                inputMap.put(ROLE_MAPPING_ID, RoleSelectionModel.getRoleMappingID());
            } else {
                inputMap.put(ROLE_MAPPING_ID, "");
            }
            if (getAppType().equals(DIRECT)) {
                inputMap.put("_AppLoginId", "");//told to set empty
            } else {
                inputMap.put("_AppLoginId", getAppLoginID());
            }

            String flag = SENDREPRESENTATIVE;
            String category = SIGNUP;
            WebServiceHandler.post(this, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    /*{"_userid":"","_isnewcustomer":"0","_msg":"Please enter correct OTP",
                            "_Success":0,"_Step":0,"_uniqueId":"","_Status":"1","_OTP":"",
                            "_BusinessUnitId":null,"_Role":null,"_ShowChangePassword":"0"}*/
                    if (response.length() != 0) {
                        try {
                            //                            dialog.dismiss();
                            String userID =
                                (response.isNull("_userid") || response.getString("_userid")
                                    .equals("")) ? "" : response.getString("_userid");
                            String isNewCustomer =
                                (response.isNull("_isnewcustomer") || response.getString(
                                    "_isnewcustomer").equals("")) ? ""
                                    : response.getString("_isnewcustomer");
                            String msg =
                                (response.isNull("_msg") || response.getString("_msg").equals(""))
                                    ? "" : response.getString("_msg");
                            String success =
                                (response.isNull("_Success") || response.getString("_Success")
                                    .equals("")) ? "" : response.getString("_Success");
                            String step =
                                (response.isNull("_Step") || response.getString("_Step").equals(""))
                                    ? "" : response.getString("_Step");
                            String uniqueID =
                                (response.isNull("_uniqueId") || response.getString("_uniqueId")
                                    .equals("")) ? "" : response.getString("_uniqueId");
                            String status =
                                (response.isNull("_Status") || response.getString("_Status")
                                    .equals("")) ? "" : response.getString("_Status").trim();
                            String otp =
                                (response.isNull("_OTP") || response.getString("_OTP").equals(""))
                                    ? "" : response.getString("_OTP");
                            String showChangePassword =
                                (response.isNull("_ShowChangePassword") || response.getString(
                                    "_ShowChangePassword").equals("")) ? ""
                                    : response.getString("_ShowChangePassword").trim();
                            int intStatus = Integer.parseInt(status);

                            if (intStatus == 0 || intStatus == 4 || intStatus == 1) {
                                submitDialog(msg, intStatus, SENDREPRESENTATIVE,
                                    showChangePassword);
                            }

                            if (intStatus == 2) {
                                sendOTP(name, emailId, mob, otp, SENDREPRESENTATIVE, uniqueID);
                            }
                            if (intStatus == 3) {
                                commonMsgDialog(RegisterActivity.this, msg, false, "");
                                //                                MOSLCommon.alertDialog(RegisterActivity.this, 0, "Motilal Oswal", msg, "OK", true, null);
                            }
                            customProgress.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            commonMsgDialog(RegisterActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY,
                                false, "");
                            //                            MOSLCommon.alertDialog(RegisterActivity.this, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
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
                public void onFailure(int statusCode, Header[] headers, String responseString,
                    Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                    JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                    JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });
        } else {
            MOSLCommon.alertDInternet(RegisterActivity.this);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void setUpView() {
        clearAllCommonModelFields();
        scrollview = findViewById(R.id.scrollview);
        scrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollview.setFocusable(true);
        scrollview.setFocusableInTouchMode(true);
        scrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });

        llEditText = findViewById(R.id.llEditText);
        spState = findViewById(R.id.spState);
        spCity = findViewById(R.id.spCity);
        spInterestedIn = findViewById(R.id.spInterestedIn);

        btnProceed = findViewById(R.id.btnProceed);
        //        btnSendRepresentative = findViewById(R.id.btnSendRepresentative);

        tvTC = findViewById(R.id.tvTC);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etPincode = findViewById(R.id.etPincode);
        etStateError = findViewById(R.id.etStateError);
        etCityError = findViewById(R.id.etCityError);
        etNameError = findViewById(R.id.etNameError);
        etEmailError = findViewById(R.id.etEmailError);
        etInterestedInError = findViewById(R.id.etInterestedInError);
        etMobileError = findViewById(R.id.etMobileError);
        etPincodeError = findViewById(R.id.etPincodeError);
        input_layout_name = findViewById(R.id.input_layout_name);
        input_layout_email = findViewById(R.id.input_layout_email);
        input_layout_mobile = findViewById(R.id.input_layout_mobile);
        inputLayoutPincode = findViewById(R.id.inputLayoutPincode);
        llPincode = findViewById(R.id.llPincode);
        llCity = findViewById(R.id.llCity);
        llState = findViewById(R.id.llState);
        llBABranchCode = findViewById(R.id.llBABranchCode);
        etBABranchCode = findViewById(R.id.etBABranchCode);
        inputLayoutBABranchCode = findViewById(R.id.inputLayoutBABranchCode);
        ivMotilalOswal = findViewById(R.id.ivMotilalOswal);
        if (getIntent().getBooleanExtra("isSaathiAufsb", false)) {
            ivMotilalOswal.setImageResource(R.drawable.aufsb_logo);
        }
        if (getAppType().equals(DIRECT)) {
            llPincode.setVisibility(View.GONE);//Registration Change 26-10-2018 before its Visible
            llBABranchCode.setVisibility(View.VISIBLE);//BA branch code layout visible for Direct
        } else {
            llPincode.setVisibility(View.GONE);
            llBABranchCode.setVisibility(View.GONE);//BA branch code layout gone for Branch and BA
        }
        if (getAppType().equals(PP)) {
            llCity.setVisibility(View.GONE);
            llState.setVisibility(View.GONE);
        } else {
            llCity.setVisibility(View.VISIBLE);
            llState.setVisibility(View.GONE);//Registration Change 26-10-2018 before its Visible
        }
        customProgress = findViewById(R.id.customProgress);

       /* etCountryCode = findViewById(R.id.etCountryCode);
        etCountryCode.setText("91");*/

        cbTC = findViewById(R.id.cbTC);
        cbTC.setChecked(true);

        etPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etPincode.getText().toString())) {
                    inputLayoutPincode.setErrorEnabled(false);
                    if (etPincode.length() < 6) {
                        etPincodeError.setVisibility(View.VISIBLE);
                        etPincodeError.setText("Invalid pincode.");
                        etPincode.requestFocus();
                    } else {
                        inputLayoutPincode.setErrorEnabled(false);
                        etPincodeError.setVisibility(View.GONE);
                        if (spState.getSelectedItem() == null
                            || ((RowData) spState.getSelectedItem()).getKey().equals("-1")) {
                            etStateError.setVisibility(View.VISIBLE);
                            etStateError.setText("Select your state");
                            etStateError.requestFocus();
                        } else if (spCity.getSelectedItem() == null
                            || ((RowData) spCity.getSelectedItem()).getKey().equals("-1")) {
                            etCityError.setVisibility(View.VISIBLE);
                            etCityError.setText("Select your city");
                            etCityError.requestFocus();
                        } else {
                            getPincodeDetails();
                        }
                    }
                } else {
                    inputLayoutPincode.setErrorEnabled(false);
                    etPincodeError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etMobile.getText().toString())) {
                    input_layout_mobile.setErrorEnabled(false);
                    if (!ValidationRule.validateMobile(etMobile.getText().toString())) {
                        etMobileError.setVisibility(View.VISIBLE);
                        etMobileError.setText("Invalid mobile no.");
                        etMobile.requestFocus();
                    } else {
                        etMobileError.setVisibility(View.GONE);
                        input_layout_mobile.setErrorEnabled(false);
                    }
                } else {
                    etMobileError.setVisibility(View.GONE);
                    input_layout_mobile.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etName.getText().toString());
                if (!isBlank(etName.getText().toString())) {
                    input_layout_name.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etName.setText("");
                        etName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(
                        etName.getText().toString().trim())) {
                        etNameError.setVisibility(View.VISIBLE);
                        etNameError.setText(ErrorMsgName);
                        etName.requestFocus();
                    } else {
                        etNameError.setVisibility(View.GONE);
                    }
                } else {
                    etNameError.setVisibility(View.GONE);
                    input_layout_name.setErrorEnabled(false);
                }
            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etEmail.getText().toString())) {
                    input_layout_email.setErrorEnabled(false);
                    if (!isValidEmail(etEmail.getText())) {
                        etEmailError.setVisibility(View.VISIBLE);
                        etEmailError.setText("Invalid email id");
                        etEmail.requestFocus();
                    } else {
                        etEmailError.setVisibility(View.GONE);
                        input_layout_email.setErrorEnabled(false);

                        //                      checkMobileEmail("Email",etEmail.getText().toString());
                    }
                } else {
                    etEmailError.setVisibility(View.GONE);
                    input_layout_email.setErrorEnabled(false);

                    //                      checkMobileEmail("Email",etEmail.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getCity("0");//Registration Change 26-10-2018 before its getState()
        getInterestedIn();

        spCity.setAdapter(arrAdapCity);
        tvTC.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEditText(etName, input_layout_name, "Enter your name")) {
                    return;
                } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(
                    etName.getText().toString().trim())) {
                    etNameError.setVisibility(View.VISIBLE);
                    etNameError.setText(ErrorMsgName);
                    etName.requestFocus();
                    return;
                } else if (llEditText.getVisibility() == View.VISIBLE && !validateEditText(etEmail,
                    input_layout_email, "Enter email id")) {
                    return;
                } else if (llEditText.getVisibility() == View.VISIBLE && !isValidEmail(
                    etEmail.getText())) {
                    etEmail.requestFocus();
                    return;
                } else if (!validateEditText(etMobile, input_layout_mobile, "Enter mobile no.")) {
                    return;
                } else if (etMobile.length() < 10) {
                    etMobile.requestFocus();
                    return;
                } else if (!ValidationRule.validateMobile(etMobile.getText().toString())) {
                    etMobile.requestFocus();
                    return;
                } else if (llState.getVisibility() == View.VISIBLE && !getAppType().equals(PP) && (
                    spState.getSelectedItem() == null
                        || ((RowData) spState.getSelectedItem()).getKey().equals("-1"))) {
                    etStateError.setVisibility(View.VISIBLE);
                    etStateError.setText("Select your state");
                    etStateError.requestFocus();
                    return;
                } else if (!getAppType().equals(PP) && (spCity.getSelectedItem() == null
                    || ((RowData) spCity.getSelectedItem()).getKey().equals("-1"))) {
                    etCityError.setVisibility(View.VISIBLE);
                    etCityError.setText("Select your city");
                    etCityError.requestFocus();
                    return;
                } else if (spInterestedIn.getSelectedItem() == null
                    || ((RowData) spInterestedIn.getSelectedItem()).getKey().equals("-1")) {
                    etInterestedInError.setVisibility(View.VISIBLE);
                    etInterestedInError.setText("Select your interest from list");
                    etInterestedInError.requestFocus();
                    return;
                }/* else if (getAppType().equals(DIRECT) && !validateEditText(etPincode, inputLayoutPincode, "Enter pincode")) {
                    return;
                }*/ else if (etPincode.getText().toString().trim().length() > 0
                    && getAppType().equals(DIRECT)
                    && etPincode.length() < 6) {
                    etPincode.requestFocus();
                    return;
                } else if (!cbTC.isChecked()) {
                    commonMsgDialog(RegisterActivity.this, "Terms & Conditions not accepted", false,
                        "");
                    return;
                } else {
                    input_layout_mobile.setErrorEnabled(false);
                    input_layout_email.setErrorEnabled(false);
                    setRegisteredName(etName.getText().toString());
                    setRegisterEmail(etEmail.getText().toString());
                    removeKeyboard(RegisterActivity.this, etEmail);
                    if (getAppType().equalsIgnoreCase(DIRECT) && !isBlank(etBABranchCode.getText()
                        .toString()
                        .trim())) { //check enter BA code valid or not call GETBADETAILS API
                        getBADetails();
                    } else { // call sign up API
                        signUp(etName.getText().toString(), etEmail.getText().toString(),
                            etMobile.getText().toString(), "", "");
                    }
                }
            }
        });
      /*  btnSendRepresentative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendRepresent();
                if (!validateEditText(etName, input_layout_name, "Enter your name")) {
                    return;
                }else if(!ValidationRule.validateOnlyAlphabetWithoutSplChar(etName.getText().toString().trim())){
                    etNameError.setVisibility(View.VISIBLE);
                    etNameError.setText(ErrorMsgName);
                    etName.requestFocus();
                } else if (!isValidEmail(etEmail.getText())) {
                    input_layout_email.setError("Email is not valid");
                    etEmail.requestFocus();
                } else if (!validateEditText(etMobile, input_layout_mobile, "Enter mobile no.")) {
                    return;
                } else if (etMobile.length() < 10) {
                    etMobile.requestFocus();
                } else if (spState.getSelectedItem() == null || ((RowData) spState.getSelectedItem()).getKey().equals("-1")) {
                    etStateError.setVisibility(View.VISIBLE);
                    etStateError.setText("Select your state");
                    etStateError.requestFocus();
                } else if (spCity.getSelectedItem() == null || ((RowData) spCity.getSelectedItem()).getKey().equals("-1")) {
                    etCityError.setVisibility(View.VISIBLE);
                    etCityError.setText("Select your city");
                    etCityError.requestFocus();
                } else if (!validateEditText(etPincode, inputLayoutPincode, "Enter pincode")) {
                    return;
                } else if (etPincode.length() < 6) {
                    etPincode.requestFocus();
                } else if (spInterestedIn.getSelectedItem() == null || ((RowData) spInterestedIn.getSelectedItem()).getKey().equals("-1")) {
                    etInterestedInError.setVisibility(View.VISIBLE);
                    etInterestedInError.setText("Select your interest from list");
                    etInterestedInError.requestFocus();
                } else if (!cbTC.isChecked()) {
//                    MOSLCommon.showToastMsg(RegisterActivity.this, "Terms & Conditions not accepted");
                    commonMsgDialog(RegisterActivity.this, "Terms & Conditions not accepted", false, "");
//                    MOSLCommon.alertDialog(RegisterActivity.this, 0, "MOSL", "Terms & Conditions not accepted", "OK", true, null);
                } else {
                    input_layout_mobile.setErrorEnabled(false);
                    input_layout_email.setErrorEnabled(false);
                    setRegisteredName(etName.getText().toString());
                    sendRepresentative(etName.getText().toString(), etEmail.getText().toString(), etMobile.getText().toString(), "", "");
                }
            }
        });*/
        tvTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MOSLCommon.showPopUpThreeLine(RegisterActivity.this, tvTC,
                    MOSLConstants.TERMS_N_COND, "", false, "", "");
            }
        });
    }

    public void signUp(final String name, final String emailId, final String mob, String otp,
        String uniqueId) {
        Map<String, String> inputMap = new HashMap<>();
        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);
            inputMap.put("_name", name);
            inputMap.put("_emailid", emailId);
            inputMap.put("_mobile", mob);
            if (getAppType().equals(PP)) {
                inputMap.put("_state", "");
                inputMap.put("_city", "");
            } else {
                inputMap.put("_state", "");//Registration Change 26-10-2018 before its stateNameStr
                inputMap.put("_city", cityNameStr);
            }
            if (getAppType().equals(DIRECT)) {
                inputMap.put("_pincode", etPincode.getText().toString());
                inputMap.put("_BACODE", etBABranchCode.getText().toString());
            } else {
                inputMap.put("_pincode", "");
            }
            inputMap.put("_strPSource", MOBILE_APP);
            if (getAppType().equals(DIRECT)) {
                inputMap.put("_strPSourceDetails", PSOURCEDETAILDIRECT);
            } else if (getAppType().equals(CRM)) {
                inputMap.put("_strPSourceDetails", PSOURCEDETAILBRANCH);
            } else if (getAppType().equals(PP)) {
                inputMap.put("_strPSourceDetails", PSOURCEDETAILUPPERMOST);
            }
            inputMap.put("_strSource", ONLINE);
            inputMap.put("_interestedin", interestedInStr);
            inputMap.put("_myIP", getDeviceID(RegisterActivity.this, getContentResolver()));
            if (!isBlank(etBABranchCode.getText().toString())) {
                inputMap.put(APPLICATION_TYPE, PP);
            } else {
                inputMap.put(APPLICATION_TYPE, getAppType());
            }
            inputMap.put("_Otp", otp);
            inputMap.put("_uniqueId", uniqueId);
            inputMap.put("_applicationFrom", "Mobile");
            if (getAppType().equals(CRM)) {
                inputMap.put(ROLE_MAPPING_ID, RoleSelectionModel.getRoleMappingID());
            } else {
                inputMap.put(ROLE_MAPPING_ID, "");
            }
            if (getAppType().equals(DIRECT)) {
                inputMap.put("_AppLoginId", "");//told to set empty
            } else {
                inputMap.put("_AppLoginId", getAppLoginID());
            }

            WebServiceHandler.post(this, SIGNUP, SIGNUP, inputMap, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                            /*{
                                "_userid":null,
                                    "_isnewcustomer":null,
                                    "_msg":"Some error has occurred, please try again",
                                    "_Success":0,
                                    "_Step":0,
                                    "_uniqueId":"",
                                    "_Status":"1",
                                    "_OTP":"",
                                    "_BusinessUnitId":null,
                                    "_Role":null,
                                    "_ShowChangePassword":null
                            }*/
                        if (statusCode == 200) {
                            if (response.length() != 0) {
                                try {
                                    String msg = (response.isNull("_msg") || response.getString("_msg")
                                        .equals("")) ? "" : response.getString("_msg");
                                    String success =
                                        (response.isNull("_Success") || response.getString("_Success")
                                            .equals("")) ? "" : response.getString("_Success");
                                    String status =
                                        (response.isNull("_Status") || response.getString("_Status")
                                            .equals("")) ? "" : response.getString("_Status").trim();
                                    String userID =
                                        (response.isNull("_userid") || response.getString("_userid")
                                            .equals("")) ? "" : response.getString("_userid");
                                    String otp = (response.isNull("_OTP") || response.getString("_OTP")
                                        .equals("")) ? "" : response.getString("_OTP");
                                    String uniqueId =
                                        (response.isNull("_uniqueId") || response.getString("_uniqueId")
                                            .equals("")) ? "" : response.getString("_uniqueId");
                                    String showChangePassword =
                                        (response.isNull("_ShowChangePassword") || response.getString(
                                            "_ShowChangePassword").equals("")) ? ""
                                            : response.getString("_ShowChangePassword");
                                    String isAadhaarByPass = (response.isNull("ISAADHAARBYPASS")
                                        || response.getString("ISAADHAARBYPASS").equals(""))
                                        ? "" : response.getString("ISAADHAARBYPASS").trim();
                                    int intStatus = Integer.parseInt(status);

                                    if (intStatus == 4 && isBlank(userID)) {
                                        commonMsgDialog(RegisterActivity.this, DATA_IS_MISSING, false,
                                            "");
                                        finish();
                                        return;
                                    } else {
                                        setUserID(userID);
                                    }
                                    setAadhaarByPass(isAadhaarByPass.equals("1"));
                                    if (intStatus == 0 || intStatus == 4 || intStatus == 1) {
                                        if (intStatus == 1) {
                                            sendOTP(name, emailId, mob, otp, SIGNUP, uniqueId);
                                        } else if (intStatus == 4) {
                                            //Set app type to franchise and isDirectProspect flag true if BA branch code entered
                                            if (getAppType().equalsIgnoreCase(DIRECT) && !isBlank(
                                                etBABranchCode.getText().toString().trim())) {
                                                setAppType(PP);
                                                setIsDirectProspect(true);
                                                setBaFranchiseeCode(
                                                    etBABranchCode.getText().toString().trim());
                                            }
                                        }
                                        submitDialog(msg, intStatus, SIGNUP, showChangePassword);
                                    }
                                    customProgress.setVisibility(View.GONE);
                                    if (intStatus == 2) {
                                        sendOTP(name, emailId, mob, otp, SIGNUP, uniqueId);
                                    }

                                    if (intStatus == 3) {
                                        commonMsgDialog(RegisterActivity.this, msg, false, "");
                                    }
                                } catch (Exception e) {
                                    commonMsgDialog(RegisterActivity.this,
                                        SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                                    e.printStackTrace();
                                    customProgress.setVisibility(View.GONE);
                                }
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
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable
                            throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject
                            errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray
                            errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                }
            );
        } else {
            MOSLCommon.alertDInternet(RegisterActivity.this);
            customProgress.setVisibility(View.GONE);
        }
    }

    public void submitDialog(String msg, final int status, String flag, final String showPassword) {
        dialog = new Dialog(RegisterActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setWindowAnimations(R.style.animationName);
        dialog.setContentView(R.layout.layout_dialog_info);
        dialog.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        tvMsg = dialog.findViewById(R.id.tvMsg);
        tvMsg1 = dialog.findViewById(R.id.tvMsg1);
        tvMsg2 = dialog.findViewById(R.id.tvMsg2);
        tvMsg3 = dialog.findViewById(R.id.tvMsg3);

        btnOK = dialog.findViewById(R.id.btnOK);
        ivClose = dialog.findViewById(R.id.ivClose);

        if (flag.equalsIgnoreCase(SIGNUP)) {
            if (status == 0) {
                tvMsg.setText(msg);
            } else if (status == 4) {
                switch (getAppType()) {
                    case CRM:
                        if (getInterestedInMF()) {
                            tvMsg.setVisibility(View.GONE);

                            tvMsg1.setVisibility(View.VISIBLE);
                            tvMsg2.setVisibility(View.VISIBLE);
                            tvMsg3.setVisibility(View.VISIBLE);
                            tvMsg1.setText("Thank You for choosing ");
                            tvMsg2.setText("MOTILAL OSWAL.");
                            tvMsg2.setTextColor(
                                getResources().getColor(R.color.secondaryBackground));
                            tvMsg3.setText(getResources().getString(R.string.mf_success_reg));
                            //                            tvMsg.setText(getResources().getString(R.string.mf_success_reg));
                        } else {
                            tvMsg.setVisibility(View.GONE);

                            tvMsg1.setVisibility(View.VISIBLE);
                            tvMsg2.setVisibility(View.VISIBLE);
                            tvMsg3.setVisibility(View.VISIBLE);
                            tvMsg1.setText("Thank You for choosing ");
                            tvMsg2.setText("MOTILAL OSWAL.");
                            tvMsg2.setTextColor(
                                getResources().getColor(R.color.secondaryBackground));
                            tvMsg3.setText(getResources().getString(R.string.crm_success_reg));
                        }
                        break;
                    case PP:
                        if (getInterestedInMF()) {
                            tvMsg.setVisibility(View.GONE);

                            tvMsg1.setVisibility(View.VISIBLE);
                            tvMsg2.setVisibility(View.VISIBLE);
                            tvMsg3.setVisibility(View.VISIBLE);
                            tvMsg1.setText("Thank You for choosing ");
                            tvMsg2.setText("MOTILAL OSWAL.");
                            tvMsg2.setTextColor(
                                getResources().getColor(R.color.secondaryBackground));
                            tvMsg3.setText(getResources().getString(R.string.mf_success_reg));

                            //                            tvMsg.setText(getResources().getString(R.string.mf_success_reg));
                        } else {
                            tvMsg.setVisibility(View.GONE);

                            tvMsg1.setVisibility(View.VISIBLE);
                            tvMsg2.setVisibility(View.VISIBLE);
                            tvMsg3.setVisibility(View.VISIBLE);
                            tvMsg1.setText("Thank You for choosing ");
                            tvMsg2.setText("MOTILAL OSWAL.");
                            tvMsg2.setTextColor(
                                getResources().getColor(R.color.secondaryBackground));
                            tvMsg3.setText(getResources().getString(R.string.pp_success_reg));
                        }
                        break;
                    case DIRECT:
                        if (getInterestedInMF()) {
                            tvMsg.setVisibility(View.VISIBLE);
                            tvMsg1.setVisibility(View.GONE);
                            tvMsg2.setVisibility(View.GONE);
                            tvMsg3.setVisibility(View.GONE);

                            tvMsg.setText(getResources().getString(R.string.mf_direct_success_reg));
                        } else {
                            tvMsg.setVisibility(View.VISIBLE);
                            tvMsg1.setVisibility(View.GONE);
                            tvMsg2.setVisibility(View.GONE);
                            tvMsg3.setVisibility(View.GONE);

                            tvMsg.setText(getResources().getString(R.string.direct_success_reg));
                        }
                        break;
                }
            } else if (status == 1) {
                tvMsg.setText(msg);
            }
        } else if (flag.equalsIgnoreCase(SENDREPRESENTATIVE)) {
            tvMsg.setText(msg);
        }

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == 4) {
                    dialog.dismiss();
                    if (Integer.parseInt(showPassword) == 1) { //show change password dialog
                        //                        Intent intent = new Intent(RegisterActivity.this, PANAdharRegistActivity.class);
                        //                        startActivity(intent);
                        if (getAppType().equals(DIRECT)) {
                            ChangePassword cp = new ChangePassword(RegisterActivity.this);
                            cp.show();
                        } else {
                            Intent intent =
                                new Intent(RegisterActivity.this, PANAdharRegistActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent intent =
                            new Intent(RegisterActivity.this, PANAdharRegistActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    dialog.dismiss();
                }
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == 4) {
                    dialog.dismiss();
                    if (Integer.parseInt(showPassword) == 1) { //show change password dialog
                        //                        Intent intent = new Intent(RegisterActivity.this, PANAdharRegistActivity.class);
                        //                        startActivity(intent);
                        if (getAppType().equals(DIRECT)) {
                            ChangePassword cp = new ChangePassword(RegisterActivity.this);
                            cp.show();
                        } else {
                            Intent intent =
                                new Intent(RegisterActivity.this, PANAdharRegistActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent intent =
                            new Intent(RegisterActivity.this, PANAdharRegistActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public void sendOTP(final String name, final String emailId, final String mob, final String otp,
        final String flag, final String uniqueId) {
        final Dialog dialog = new Dialog(RegisterActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dc_otp);
        dialog.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        TextView tvEditMobileNumber = dialog.findViewById(R.id.tvEditMobileNumber);
        final EditText etOTP = dialog.findViewById(R.id.etOTP);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        Button btnResendOTP = dialog.findViewById(R.id.btnResendOTP);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etOTP.getText().toString().equals("")) {
                    //                    if (etOTP.getText().toString().equalsIgnoreCase(otp)) {
                    if (flag.equalsIgnoreCase(SIGNUP)) {
                        signUp(name, emailId, mob, etOTP.getText().toString(), uniqueId);
                        removeKeyboard(RegisterActivity.this, etOTP);
                    } else if (flag.equalsIgnoreCase(SENDREPRESENTATIVE)) {
                        //                            sendRepresentative(name, emailId, mob, etOTP.getText().toString(), uniqueId);
                    }
                    dialog.dismiss();
                    /*} else {
//                        Toast.makeText(RegisterActivity.this, ENTER_CORRECT_OTP, Toast.LENGTH_SHORT).show();
                        commonMsgDialog(RegisterActivity.this, ENTER_CORRECT_OTP, false, "");
//                        MOSLCommon.alertDialog(RegisterActivity.this, 0, "MOSL", ENTER_CORRECT_OTP, "OK", true, null);
                        etOTP.setText("");
                    }*/
                }
            }
        });

        btnResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag.equalsIgnoreCase(SIGNUP)) {
                    //                    signUp(name, emailId, mob, "", "");
                    resendOTP(name, emailId, mob, SIGNUP, uniqueId);
                } else if (flag.equalsIgnoreCase(SENDREPRESENTATIVE)) {
                    //                    sendRepresentative(name, emailId, mob, "", "");
                    resendOTP(name, emailId, mob, SENDREPRESENTATIVE, uniqueId);
                }
                dialog.dismiss();
            }
        });
        tvEditMobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMobile.requestFocus();
                etMobile.setCursorVisible(true);
                etMobile.setText("");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void resendOTP(final String name, final String emailId, final String mob,
        final String type, final String uniqueId) {
        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);
            Map<String, String> inputMap = new HashMap<>();
            inputMap.put("_name", name);
            inputMap.put("_emailid", emailId);
            inputMap.put("_mobile", mob);
            inputMap.put("_uniqueId", uniqueId);

            WebServiceHandler.post(this, SIGNUPRESENDOTP, SIGNUP, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response.length() != 0) {
                            try {
                                String msg = (response.isNull("_msg") || response.getString("_msg")
                                    .equals("")) ? "" : response.getString("_msg");
                                String uniqueID =
                                    (response.isNull("_uniqueId") || response.getString("_uniqueId")
                                        .equals("")) ? "" : response.getString("_uniqueId");
                                String status =
                                    (response.isNull("_Status") || response.getString("_Status")
                                        .equals("")) ? "" : response.getString("_Status").trim();
                                String otp = (response.isNull("_OTP") || response.getString("_OTP")
                                    .equals("")) ? "" : response.getString("_OTP");

                                if (Integer.parseInt(status) == 1) {
                                    sendOTP(name, emailId, mob, otp, type, uniqueID);
                                    commonMsgDialog(RegisterActivity.this, msg, false, "");
                                    //                                MOSLCommon.alertDialog(RegisterActivity.this, 0, "Motilal Oswal", msg, "OK", true, null);
                                } else {
                                    commonMsgDialog(RegisterActivity.this, msg, false, "");
                                    //                                MOSLCommon.alertDialog(RegisterActivity.this, 0, "Motilal Oswal", msg, "OK", true, null);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                commonMsgDialog(RegisterActivity.this,
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                                //                            MOSLCommon.alertDialog(RegisterActivity.this, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
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
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false,
                            "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false,
                            "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false,
                            "");
                    }
                });
        }
    }

    public void getState() {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("", "");
        if (MOSLCommon.isNetworkEnabled(RegisterActivity.this)) {
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(this, BINDSTATE, SIGNUP, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        arrState = new ArrayList<>();
                        customProgress.setVisibility(View.VISIBLE);
                        if (statusCode == 200) {
                            if (response != null) {
                                String stateCode, stateName;
                                arrState.clear();
                                arrState.add(new RowData(0, "-1", "Select"));
                                try {
                                    JSONArray jsonArray = response.getJSONArray(STATE_DETAILS);
                                    if (jsonArray.length() != 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            stateCode = object.getString("STATE_CODE");
                                            stateName = object.getString("STATE_NAME");
                                            arrState.add(new RowData(i + 1, stateCode, stateName));
                                        }
                                    }
                                    arrAdapState = new ArrayAdapter<>(RegisterActivity.this,
                                        R.layout.simple_spinner_item, arrState);
                                    arrAdapState.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spState.setAdapter(arrAdapState);
                                    spState.setOnItemSelectedListener(stateListener);
                                    customProgress.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    commonMsgDialog(RegisterActivity.this,
                                        SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                                    //                                        MOSLCommon.alertDialog(RegisterActivity.this, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                                }
                            } else {
                                customProgress.setVisibility(View.GONE);
                                MOSLCommon.showToastMsg(RegisterActivity.this, "No state");
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
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable
                            throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false,
                            "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject
                            errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false,
                            "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray
                            errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false,
                            "");
                    }
                }

            );
        } else {
            customProgress.setVisibility(View.GONE);
            MOSLCommon.alertDInternet(RegisterActivity.this);
        }
    }

    private void getCity(String stateCodeStr) {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("_stateid", stateCodeStr);

        String input = "";

        if (MOSLCommon.isNetworkEnabled(RegisterActivity.this)) {
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(this, BINDCITY, SIGNUP, inputMap, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //  customProgress.setVisibility(View.VISIBLE);
                        arrCity = new ArrayList<>();
                        if (statusCode == 200) {
                            if (response.length() != 0 || response != null) {
                                String cityCode, cityName;
                                arrCity.add(new RowData(0, "-1", "Select"));
                                try {
                                    JSONArray jsonArray = response.getJSONArray(CITY_DETAILS);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        cityCode = object.getString("CITY_CODE");
                                        cityName = object.getString("CITY_NAME");
                                        arrCity.add(new RowData(i + 1, cityCode, cityName));
                                    }
                                    arrAdapCity = new ArrayAdapter<>(RegisterActivity.this,
                                        R.layout.simple_spinner_item, arrCity);
                                    arrAdapCity.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spCity.setAdapter(arrAdapCity);

                                    customProgress.setVisibility(View.GONE);

                                    spCity.setOnItemSelectedListener(cityListener);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    customProgress.setVisibility(View.GONE);
                                    commonMsgDialog(RegisterActivity.this,
                                        SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                                    //                                        MOSLCommon.alertDialog(RegisterActivity.this, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                                }
                            } else {
                                customProgress.setVisibility(View.GONE);
                                MOSLCommon.showToastMsg(RegisterActivity.this, "NO CITY");
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
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable
                            throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject
                            errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray
                            errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                }

            );
        } else {
            MOSLCommon.alertDInternet(RegisterActivity.this);
        }
    }

    private void getInterestedIn() {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(APPLICATION_TYPE, getAppType());
        inputMap.put(COMMAND_TEXT, FILL_INTERESTEDIN_REGISTRATION);
        inputMap.put(APP_LOGIN_ID, getAppLoginID());

        if (MOSLCommon.isNetworkEnabled(RegisterActivity.this)) {
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(this, BINDINTERESTEDIN, SIGNUP, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        customProgress.setVisibility(View.VISIBLE);
                        arrInterested = new ArrayList<>();

                        if (statusCode == 200) {
                            if (response.length() != 0) {
                                String interestedID, interestedText;
                                arrInterested.add(new RowData(0, "-1", "Interested In"));
                                try {
                                    JSONArray jsonArray =
                                        response.getJSONArray(INTERESTED_IN_DETAILS);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        interestedID = object.getString("ID");
                                        interestedText = object.getString("TEXT");
                                        arrInterested.add(
                                            new RowData(i + 1, interestedID, interestedText));
                                    }
                                    arrAdapInterested = new ArrayAdapter<>(RegisterActivity.this,
                                        R.layout.simple_spinner_item, arrInterested);
                                    arrAdapInterested.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spInterestedIn.setAdapter(arrAdapInterested);
                                    customProgress.setVisibility(View.GONE);

                                    spInterestedIn.setOnItemSelectedListener(interestedInListener);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    customProgress.setVisibility(View.GONE);
                                    commonMsgDialog(RegisterActivity.this,
                                        SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                                    //                                        MOSLCommon.alertDialog(RegisterActivity.this, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                                }
                            } else {
                                customProgress.setVisibility(View.GONE);
                                MOSLCommon.showToastMsg(RegisterActivity.this,
                                    "NO Interested fields");
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
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable
                            throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false,
                            "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject
                            errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false,
                            "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray
                            errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false,
                            "");
                    }
                }

            );
        } else {
            MOSLCommon.alertDInternet(RegisterActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

  /*  @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (etMobile.length() == 1) {
            validateEditText(etMobile, input_layout_mobile, "");
        }
        if (etMobile.length() < 10) {
            etMobileError.setVisibility(View.VISIBLE);
            etMobileError.setText("Invalid mobile no.");
            etMobile.requestFocus();
        } else {
            etMobileError.setVisibility(View.GONE);
        }
        if (etPincode.length() < 6) {
            etPincodeError.setVisibility(View.VISIBLE);
            etPincodeError.setText("Invalid pincode.");
            etPincode.requestFocus();
        } else {
            etPincodeError.setVisibility(View.GONE);
            if (spState.getSelectedItem() == null || ((RowData) spState.getSelectedItem()).getKey().equals("-1")) {
                etStateError.setVisibility(View.VISIBLE);
                etStateError.setText("Select your state");
                etStateError.requestFocus();
            } else if (spCity.getSelectedItem() == null || ((RowData) spCity.getSelectedItem()).getKey().equals("-1")) {
                etCityError.setVisibility(View.VISIBLE);
                etCityError.setText("Select your city");
                etCityError.requestFocus();
            } else {
                getPincodeDetails();

            }
        }
    }*/

    private void getPincodeDetails() {
        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);
            Map<String, String> inputMap = new HashMap<>();
            inputMap.put("_state", stateNameStr);
            inputMap.put("_city", cityNameStr);
            inputMap.put("_pincode", etPincode.getText().toString());

            MOSLCommon.removeKeyboard(RegisterActivity.this, customProgress);

            WebServiceHandler.post(this, GETPINCODEDETAILS, SIGNUP, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        /* {"STATE":"MAHARASHTRA","CITY":"MUMBAI","PINCODE":"400068"}*/

                        if (response != null) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("PincodeDetails");
                                if (jsonArray.length() == 0) {
                                    //                                etPincode.setText("");
                                    CustomDialog dialogMsg =
                                        new CustomDialog(RegisterActivity.this, INVALID_PIN, true,
                                            false, false, "", OK, "",
                                            new CustomDialog.CustomDialogListener() {
                                                @Override
                                                public void dialogAction(MOSLCommon.Action action,
                                                    Object... data) {

                                                }
                                            });
                                    dialogMsg.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                customProgress.setVisibility(View.GONE);
                                commonMsgDialog(RegisterActivity.this,
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                                //                            MOSLCommon.alertDialog(RegisterActivity.this, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
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
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false,
                            "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false,
                            "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED, false,
                            "");
                    }
                });
        } else {
            MOSLCommon.alertDInternet(this);
        }
    }

    private boolean validateEditText(EditText editText, TextInputLayout textInputLayout,
        String error) {
        if (editText.getText().toString().trim().isEmpty()) {
            textInputLayout.setError(error);
            editText.requestFocus();
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    //  get Franchise details
    private void getBADetails() {
        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);
            Map<String, String> inputMap = new HashMap<>();
            //            inputMap.put("_AppLoginId", getAppLoginID());
            inputMap.put("_AppLoginId", etBABranchCode.getText().toString().trim());
            inputMap.put("_BACODE", etBABranchCode.getText().toString().trim());

            WebServiceHandler.post(this, GETBADETAILS, SIGNUP, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        customProgress.setVisibility(View.GONE);
                        if (response != null) {
                            try {
                                showBADetailDailog(response);
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
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                    }
                });
        } else {
            MOSLCommon.alertDInternet(this);
        }
    }

    private void showBADetailDailog(JSONObject response) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.animationName);
        dialog.setContentView(R.layout.dialog_ba_branch_code_detail);
        dialog.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);

        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        TextView tvFirmName = dialog.findViewById(R.id.tvFirmName);
        TextView tvFirmCode = dialog.findViewById(R.id.tvFirmCode);
        TextView tvFirmAddress = dialog.findViewById(R.id.tvFirmAddress);
        TextView tvPartnerCode = dialog.findViewById(R.id.tvPartnerCode);
        Button btnEdit = dialog.findViewById(R.id.btnEdit);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();
                etBABranchCode.requestFocus();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();
                signUp(etName.getText().toString(), etEmail.getText().toString(),
                    etMobile.getText().toString(), "", "");
            }
        });

        JSONArray jsonArray = null, jsonArray1 = null;
        try {
            if (response.has("BADETAILS")) {
                jsonArray = response.getJSONArray("BADETAILS");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String loginId =
                        (object.isNull("LoginId") || object.getString("LoginId").equals("")) ? ""
                            : object.getString("LoginId");
                    String userType =
                        (object.isNull("UserType") || object.getString("UserType").equals("")) ? ""
                            : object.getString("UserType");
                    String branchCode =
                        (object.isNull("BranchCode") || object.getString("BranchCode").equals(""))
                            ? "" : object.getString("BranchCode");
                    String subBrokerCode =
                        (object.isNull("SubBrokerCode") || object.getString("SubBrokerCode")
                            .equals("")) ? "" : object.getString("SubBrokerCode");
                    String area =
                        (object.isNull("Area") || object.getString("Area").equals("")) ? ""
                            : object.getString("Area");
                    String region =
                        (object.isNull("Region") || object.getString("Region").equals("")) ? ""
                            : object.getString("Region");
                    String TraderCode =
                        (object.isNull("TraderCode") || object.getString("TraderCode").equals(""))
                            ? "" : object.getString("TraderCode");
                    String active =
                        (object.isNull("Active") || object.getString("Active").equals("")) ? ""
                            : object.getString("Active");
                    String product =
                        (object.isNull("Product") || object.getString("Product").equals("")) ? ""
                            : object.getString("Product");
                    String branch_Name =
                        (object.isNull("Branch_Name") || object.getString("Branch_Name").equals(""))
                            ? "" : object.getString("Branch_Name");
                    String clCode =
                        (object.isNull("ClCode") || object.getString("ClCode").equals("")) ? ""
                            : object.getString("ClCode");
                    String emailID =
                        (object.isNull("EmailID") || object.getString("EmailID").equals("")) ? ""
                            : object.getString("EmailID");
                    String city =
                        (object.isNull("City") || object.getString("City").equals("")) ? ""
                            : object.getString("City");
                    String mobileNo =
                        (object.isNull("MobileNo") || object.getString("MobileNo").equals("")) ? ""
                            : object.getString("MobileNo");
                    String default_SubBroker =
                        (object.isNull("Default_SubBroker") || object.getString("Default_SubBroker")
                            .equals("")) ? "" : object.getString("Default_SubBroker");
                    tvFirmName.setText(branch_Name);
                    tvFirmCode.setText(branchCode);
                    tvFirmAddress.setText("");
                    tvPartnerCode.setText(clCode);
                    dialog.show();
                }
            }

            if (response.has("BADETAILS1")) {
                String massage = "";
                jsonArray1 = response.getJSONArray("BADETAILS1");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject = jsonArray1.getJSONObject(i);
                    massage =
                        (jsonObject.isNull("Message") || jsonObject.getString("Message").equals(""))
                            ? "" : jsonObject.getString("Message");
                }
                commonMsgDialog(RegisterActivity.this, massage, false, "");
                etBABranchCode.setText("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class ChangePassword extends Dialog implements MOSLConstants {

        Context mContext;
        RelativeLayout customProgress;
        private ImageView ivClose;
        private Button btnChangePasswordSubmit, btnChangePasswordReset;
        private EditText etChangePasswordUserId, etOldPassword, etNewPassword, etConfirmPassword,
            etNewPasswordError, etConfirmPasswordError;
        private TextInputLayout input_layout_oldPassword, input_layout_newPassword,
            input_layout_confirmPassword;

        public ChangePassword(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setWindowAnimations(R.style.animationName);
            //        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setContentView(R.layout.dc_change_password);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
            setCanceledOnTouchOutside(false);
            setUpView();
        }

        private void setUpView() {
            customProgress = findViewById(R.id.customProgress);
            ivClose = findViewById(R.id.ivClose);
            btnChangePasswordSubmit = findViewById(R.id.btnChangePasswordSubmit);
            btnChangePasswordReset = findViewById(R.id.btnChangePasswordReset);
            etChangePasswordUserId = findViewById(R.id.etChangePasswordUserId);
            etOldPassword = findViewById(R.id.etOldPassword);
            etNewPassword = findViewById(R.id.etNewPassword);
            etConfirmPassword = findViewById(R.id.etConfirmPassword);
            etConfirmPasswordError = findViewById(R.id.etConfirmPasswordError);
            etNewPasswordError = findViewById(R.id.etNewPasswordError);
            input_layout_oldPassword = findViewById(R.id.input_layout_oldPassword);
            input_layout_newPassword = findViewById(R.id.input_layout_newPassword);
            input_layout_confirmPassword = findViewById(R.id.input_layout_confirmPassword);

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
                    if (!validateEditText(etOldPassword, input_layout_oldPassword,
                        "Enter old password")) {
                        return;
                    } else if (!validateEditText(etNewPassword, input_layout_newPassword,
                        "Enter new password")) {
                        return;
                    } else if (etNewPassword.length() < 8) {
                        //                    etNewPassword.setError("New password min length must be 8 digit");
                        etNewPasswordError.setText("New password min length must be 8 digit");
                        etNewPasswordError.setVisibility(View.VISIBLE);
                    } else if (!validateEditText(etConfirmPassword, input_layout_confirmPassword,
                        "Please enter password")) {
                        etNewPasswordError.setVisibility(View.GONE);
                        return;
                    } else if (!etNewPassword.getText()
                        .toString()
                        .trim()
                        .equals(etConfirmPassword.getText().toString().trim())) {
                        etConfirmPassword.setText("");
                        etConfirmPasswordError.setText(
                            "Confirm password and new password should be same");
                        etConfirmPasswordError.setVisibility(View.VISIBLE);
                    } else {
                        etNewPasswordError.setVisibility(View.GONE);
                        etConfirmPasswordError.setVisibility(View.GONE);
                        removeKeyboard(mContext, etConfirmPassword);
                        changePassword(etNewPassword.getText().toString().trim(),
                            etOldPassword.getText().toString().trim());
                        //                    changePWdialog.dismiss();
                    }
                }
            });
        }

        private void changePassword(String newPassword, String oldPassword) {
            if (MOSLCommon.isNetworkEnabled(RegisterActivity.this)) {
                customProgress.setVisibility(View.VISIBLE);
                Map<String, String> inputMap = new HashMap<>();
                inputMap.put("_USERID", getUserID());
                inputMap.put("_NewPassword", newPassword);
                inputMap.put("_CurrentPassword", oldPassword);

                WebServiceHandler.post(mContext, CHANGEPASSWORD, LOGIN, inputMap,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
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

                                        String success =
                                            (response.isNull("_Success") || response.getString(
                                                "_Success").equals("")) ? ""
                                                : response.getString("_Success");
                                        String msg =
                                            (response.isNull("_msg") || response.getString("_msg")
                                                .equals("")) ? "" : response.getString("_msg");

                                        if (Integer.parseInt(success) == 1) {
                                       /* MOSLCommon.alertDialog(mContext, 0, "Motilal Oswal", msg, "OK", true, new MOSLCommon.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MOSLCommon.Action action, Object... data) {
                                                if (action == MOSLCommon.Action.OK) {
                                                    dismiss();
                                                    Intent intent = new Intent(RegisterActivity.this, PANAdharRegistActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });*/
                                            CustomDialog dialog =
                                                new CustomDialog(RegisterActivity.this, msg, true,
                                                    false, false, "", OK, "",
                                                    new CustomDialog.CustomDialogListener() {
                                                        @Override
                                                        public void dialogAction(
                                                            MOSLCommon.Action action,
                                                            Object... data) {
                                                            if (action == MOSLCommon.Action.OK) {
                                                                dismiss();
                                                                Intent intent = new Intent(
                                                                    RegisterActivity.this,
                                                                    PANAdharRegistActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }
                                                    });
                                            dialog.show();
                                        } else if (Integer.parseInt(success) == 0) {
                                            commonMsgDialog(RegisterActivity.this, msg, false, "");
                                            //                                        MOSLCommon.alertDialog(mContext, 0, "Motilal Oswal", msg, "OK", true, null);
                                        }
                                    } else {
                                        customProgress.setVisibility(View.GONE);
                                        commonMsgDialog(RegisterActivity.this,
                                            SOME_SERVER_ERROR_OCCURRED, false, "");
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                commonMsgDialog(RegisterActivity.this,
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                                //                            MOSLCommon.alertDialog(mContext, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                            }
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED,
                                false, "");
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED,
                                false, "");
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            MOSLCommon.showToastMsg(mContext, responseString);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(RegisterActivity.this, SOME_SERVER_ERROR_OCCURRED,
                                false, "");
                        }
                    });
            } else {
                MOSLCommon.alertDInternet(RegisterActivity.this);
                customProgress.setVisibility(View.GONE);
            }
        }

        private boolean validateEditText(EditText edietText, TextInputLayout textInputLayout,
            String error) {
            if (edietText.getText().toString().trim().isEmpty()) {
                textInputLayout.setError(error);
                edietText.requestFocus();
                return false;
            } else {
                textInputLayout.setErrorEnabled(false);
            }

            return true;
        }
    }
}
