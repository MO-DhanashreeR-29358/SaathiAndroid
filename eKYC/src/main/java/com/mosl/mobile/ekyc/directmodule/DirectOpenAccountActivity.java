package com.mosl.mobile.ekyc.directmodule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.register.RegisterActivity;

import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.model.CommonModel.clearAllCommonModelFields;
import static com.mosl.mobile.ekyc.model.CommonModel.setAppType;

/**
 * Created by Vijay on 9/6/2016.
 */
public class DirectOpenAccountActivity extends Activity implements View.OnClickListener, MOSLConstants {

    private ImageView ivApplyNow, ivResumeApp, ivTrackApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

        setPermisson(DirectOpenAccountActivity.this,getApplicationContext());
            setContentView(R.layout.dc_open_account);
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            setUpView();
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID()+"-"+getAppLoginID());
//            Crashlytics.logException(e);
        }
    }



    private void setUpView() {
        ivApplyNow = (ImageView) findViewById(R.id.ivApplyNow);
        ivResumeApp = (ImageView) findViewById(R.id.ivResumeApp);
        ivTrackApp = (ImageView) findViewById(R.id.ivTrackApp);

        ivApplyNow.setOnClickListener(this);
        ivResumeApp.setOnClickListener(this);
        ivTrackApp.setOnClickListener(this);
        clearAllCommonModelFields();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ivApplyNow) {
            setAppType(DIRECT);
            Intent intent = new Intent(DirectOpenAccountActivity.this, RegisterActivity.class);
            startActivity(intent);

        } else if (i == R.id.ivResumeApp) {
            setAppType(DIRECT);
            Intent intent = new Intent(DirectOpenAccountActivity.this, DirectResumeApplicationActivity.class);
            startActivity(intent);

        } else if (i == R.id.ivTrackApp) {
            setAppType(DIRECT);
            Intent intent = new Intent(DirectOpenAccountActivity.this, DirectTrackApplicationLogin.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
