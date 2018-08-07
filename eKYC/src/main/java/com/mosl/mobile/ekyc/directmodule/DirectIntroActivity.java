package com.mosl.mobile.ekyc.directmodule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.register.RegisterActivity;

import static com.mosl.mobile.ekyc.constants.MOSLConstants.DIRECT;
import static com.mosl.mobile.ekyc.model.CommonModel.setAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.setAppType;

/**
 * Created by raju on 1/18/2017.
 */

public class DirectIntroActivity extends Activity implements View.OnClickListener {

    private Button btnStart, btnNewCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.direct_intro);
        setUpView();
    }

    private void setUpView() {
        btnNewCustomer = (Button) findViewById(R.id.btnNewCustomer);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);
        btnNewCustomer.setOnClickListener(this);
        setAppLoginID("");
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnStart) {
            setAppType(DIRECT);
            Intent intent = new Intent(DirectIntroActivity.this, DirectResumeApplicationActivity.class);
            startActivity(intent);
        } else if (i == R.id.btnNewCustomer) {
            setAppType(DIRECT);
            Intent intent = new Intent(DirectIntroActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }
}
