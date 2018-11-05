package com.mosl.mobile.ekyc.directmodule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import com.mosl.mobile.ekyc.ui.TrackApplicationActivity;
import cz.msebera.android.httpclient.Header;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;
import timber.log.Timber;

import static com.mosl.mobile.ekyc.common.MOSLCommon.isBlank;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.model.CommonModel.clearAllCommonModelFields;
import static com.mosl.mobile.ekyc.model.CommonModel.getPANno;
import static com.mosl.mobile.ekyc.model.CommonModel.setPANno;

/**
 * Created by Vijay on 07/12/2016.
 */

public class DirectTrackApplicationLogin extends Activity
    implements View.OnClickListener, MOSLConstants {

    EditText etPanNo, etPanNoError, etOTP, etOTPError;
    //    TextInputLayout inputLayoutPanNo;
    ImageView ivSubmit;
    LinearLayout llOTP;
    Button btnVerify, btnResendOTP;
    RelativeLayout customProgress;
    String uniqueId;
    int submitOTP = 1, resendOTP = 2, submit = 0;

    @Override protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.track_application_dc_login);
            setPermisson(DirectTrackApplicationLogin.this, getApplicationContext());
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            setUpView();
        } catch (Exception e) {
            e.printStackTrace();
            //            Crashlytics.setUserIdentifier(getUserID()+"-"+getAppLoginID());
            //            Crashlytics.logException(e);
        }
    }

    private void setUpView() {
        etPanNo = findViewById(R.id.etPanNo);
        etPanNoError = findViewById(R.id.etPanNoError);
        etOTP = findViewById(R.id.etOTP);
        etOTPError = findViewById(R.id.etOTPError);
        llOTP = findViewById(R.id.llOTP);
        btnVerify = findViewById(R.id.btnVerify);
        btnResendOTP = findViewById(R.id.btnResendOTP);
        customProgress = findViewById(R.id.customProgress);
        //        inputLayoutPanNo = (TextInputLayout) findViewById(R.id.inputLayoutPanNo);
        ivSubmit = findViewById(R.id.ivSubmit);

        etPanNo.setFilters(
            new InputFilter[] { new InputFilter.LengthFilter(10), new InputFilter.AllCaps() });
        btnVerify.setOnClickListener(this);
        btnResendOTP.setOnClickListener(this);

        etPanNo.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isBlank(etPanNo.getText().toString()) || etPanNo.length() < 10) {
                    visibilityCode(false);
                }
            }

            @Override public void afterTextChanged(Editable s) {

            }
        });
        clearAllCommonModelFields();
    }

    @Override protected void onResume() {
        super.onResume();
        etPanNo.setText("");
        etOTP.setText("");
    }

    @Override public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnVerify) {
            if (btnVerify.getText().equals(getResources().getString(R.string.button_label_verify))
                && validatePAN()) {
                applicationTracker(submit);
            } else if (btnVerify.getText()
                .equals(getResources().getString(R.string.button_label_verify_otp))
                && validateOTP()) {
                applicationTracker(submitOTP);
            }
        } else if (i == R.id.btnResendOTP) {
            applicationTracker(resendOTP);
        }
    }

    @Override public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    //Pancard number Format
    private boolean isValidPanNumber(String pan) {
        String PAN_PATTEN = ("[A-Z]{5}[0-9]{4}[A-Z]");
        Pattern pattern = Pattern.compile(PAN_PATTEN);
        Matcher matcher = pattern.matcher(pan);
        return matcher.matches();
    }

    private boolean validatePAN() {
        if (etPanNo.getText().toString().equals("")) {
            etPanNoError.setVisibility(View.VISIBLE);
            etPanNoError.setText(
                getResources().getString(R.string.error_text_track_application_login));
            return false;
        } else if (etPanNo.length() < 10) {
            etPanNoError.setVisibility(View.VISIBLE);
            etPanNoError.setText(
                getResources().getString(R.string.error_text_track_application_login));
            return false;
        } else if (!isValidPanNumber(etPanNo.getText().toString())) {
            etPanNoError.setVisibility(View.VISIBLE);
            etPanNoError.setText(
                getResources().getString(R.string.error_text_track_application_login));
            return false;
        } else {
            etPanNoError.setVisibility(View.GONE);
            etOTPError.setVisibility(View.GONE);
            setPANno(etPanNo.getText().toString());
            return true;
        }
    }

    private boolean validateOTP() {
        if (etOTP.getText().toString().equals("") || etOTP.length() < 4) {
            etOTPError.setVisibility(View.VISIBLE);
            etOTPError.setText(getResources().getString(R.string.error_text_otp));
            return false;
        } else {
            etPanNoError.setVisibility(View.GONE);
            etOTPError.setVisibility(View.GONE);
            return true;
        }
    }

    private void visibilityCode(boolean value) {
        etOTP.setText("");
        if (value) {
            llOTP.setVisibility(View.VISIBLE);
            btnResendOTP.setVisibility(View.VISIBLE);
            btnVerify.setText(getResources().getString(R.string.button_label_verify_otp));
        } else {
            llOTP.setVisibility(View.GONE);
            btnResendOTP.setVisibility(View.GONE);
            btnVerify.setText(getResources().getString(R.string.button_label_verify));
        }
    }

    private void gotoNextPage(String userId) {
        Intent intent =
            new Intent(DirectTrackApplicationLogin.this, TrackApplicationActivity.class);
        intent.putExtra(USERID, userId);
        startActivity(intent);
    }

    public void applicationTracker(int type) {
        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);
            Map<String, String> inputMap = new HashMap<>();
            inputMap.put("PanNo", getPANno());
            inputMap.put("UserId", "");
            switch (type) {
                case 0:
                    inputMap.put("_Otp", "");
                    inputMap.put("_uniqueId", "");
                    break;
                case 1:
                    inputMap.put("_Otp", etOTP.getText().toString().trim());
                    inputMap.put("_uniqueId", uniqueId);
                    break;
                case 2:
                    inputMap.put("_Otp", "");
                    inputMap.put("_uniqueId", uniqueId);
                    break;
            }

            WebServiceHandler.post(this, TRACKAPPLICATION, TRACK, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response != null) {
                            try {
                                if (response.has("VERIFICATIONSTATUS")) {

                                    JSONArray jsonArray =
                                        response.getJSONArray("VERIFICATIONSTATUS");
                                    if (jsonArray.length() != 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            String status =
                                                (object.isNull("STATUS") || object.getString(
                                                    "STATUS").equals("")) ? ""
                                                    : object.getString("STATUS");
                                            String message =
                                                (object.isNull("MESSAGE") || object.getString(
                                                    "MESSAGE").equals("")) ? ""
                                                    : object.getString("MESSAGE");
                                            uniqueId =
                                                (object.isNull("UNIQUEID") || object.getString(
                                                    "UNIQUEID").equals("")) ? ""
                                                    : object.getString("UNIQUEID");
                                            String userId =
                                                (object.isNull("USERID") || object.getString(
                                                    "USERID").equals("")) ? ""
                                                    : object.getString("USERID");
                                            switch (status) {
                                                case "0":
                                                    visibilityCode(false);
                                                    MOSLCommon.showToastMsg(
                                                        DirectTrackApplicationLogin.this, message);
                                                    break;
                                                case "1":
                                                    visibilityCode(true);
                                                    MOSLCommon.showToastMsg(
                                                        DirectTrackApplicationLogin.this, message);
                                                    break;
                                                case "2":
                                                    gotoNextPage(userId);
                                                    break;
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            customProgress.setVisibility(View.GONE);
                        } else {
                            MOSLCommon.showToastMsg(DirectTrackApplicationLogin.this, NO_RESPONSE);
                            customProgress.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable throwable) {
                        Timber.e("no resp in Data");
                        customProgress.setVisibility(View.GONE);
                    }
                });
        } else {
            MOSLCommon.alertDInternet(this);
            customProgress.setVisibility(View.GONE);
        }
    }
}
