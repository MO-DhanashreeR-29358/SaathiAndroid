package com.mosl.mobile.ekyc.directmodule;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.constants.MOSLConstants;

import static com.mosl.mobile.ekyc.model.CommonModel.getAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;

/**
 * Created by Vijay on 9/7/2016.
 */
public class DirectIPVActivity extends Activity implements View.OnClickListener, MOSLConstants {

    private LinearLayout llOTP;
    private TextView txtResendOTP, txtNote;
    private ImageView ivSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dc_ipv);
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            setUpView();
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID()+"-"+getAppLoginID());
//            Crashlytics.logException(e);
        }
    }

    private void setUpView() {
        txtResendOTP = (TextView) findViewById(R.id.txtResendOTP);
        txtNote = (TextView) findViewById(R.id.txtNote);
        ivSubmit = (ImageView) findViewById(R.id.ivSubmit);
        llOTP = (LinearLayout) findViewById(R.id.llOTP);
        txtResendOTP.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.txtResendOTP) {

        } else if(i == R.id.ivSubmit) {

        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
