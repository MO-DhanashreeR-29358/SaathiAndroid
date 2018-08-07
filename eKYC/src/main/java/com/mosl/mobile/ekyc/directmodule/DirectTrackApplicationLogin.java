package com.mosl.mobile.ekyc.directmodule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.register.RegisterActivity;
import com.mosl.mobile.ekyc.ui.TrackApplicationActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.model.CommonModel.clearAllCommonModelFields;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.setPANno;

/**
 * Created by Vijay on 07/12/2016.
 */

public class DirectTrackApplicationLogin extends Activity implements View.OnClickListener, MOSLConstants {

    EditText etPanNo, etPanNoError;
//    TextInputLayout inputLayoutPanNo;
    ImageView ivSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.track_application_dc_login);
            setPermisson(DirectTrackApplicationLogin.this,getApplicationContext());
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            setUpView();
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID()+"-"+getAppLoginID());
//            Crashlytics.logException(e);
        }
    }

    private void setUpView() {
        etPanNo = (EditText) findViewById(R.id.etPanNo);
        etPanNoError = (EditText) findViewById(R.id.etPanNoError);
//        inputLayoutPanNo = (TextInputLayout) findViewById(R.id.inputLayoutPanNo);
        ivSubmit = (ImageView) findViewById(R.id.ivSubmit);

        etPanNo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10), new InputFilter.AllCaps()});
        ivSubmit.setOnClickListener(this);
        clearAllCommonModelFields();
    }

    @Override
    protected void onResume() {
        super.onResume();
        etPanNo.setText("");
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ivSubmit) {
            if (etPanNo.getText().toString().equals("")) {
                etPanNoError.setVisibility(View.VISIBLE);
                etPanNoError.setText("Please enter valid PAN No.");
            } else if (!isValidPannumber(etPanNo.getText().toString())) {
                etPanNoError.setVisibility(View.VISIBLE);
                etPanNoError.setText("Please enter valid PAN No.");
            } else if (etPanNo.length() < 10) {
                etPanNoError.setVisibility(View.VISIBLE);
                etPanNoError.setText("Please enter valid PAN No.");
            } else {
                etPanNoError.setVisibility(View.GONE);
                Intent intent = new Intent(DirectTrackApplicationLogin.this, TrackApplicationActivity.class);
                setPANno(etPanNo.getText().toString());
//                etPanNo.setText("");
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    //Pancard number Format
    private boolean isValidPannumber(String pan) {

        String PAN_PATTEN = ("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        Pattern pattern = Pattern.compile(PAN_PATTEN);
        Matcher matcher = pattern.matcher(pan);
        return matcher.matches();
    }

    /*private boolean validateEditText(EditText edietText, TextInputLayout textInputLayout, String error) {
        if (edietText.getText().toString().trim().isEmpty()) {
            textInputLayout.setError(error);
            edietText.requestFocus();
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }*/
}
