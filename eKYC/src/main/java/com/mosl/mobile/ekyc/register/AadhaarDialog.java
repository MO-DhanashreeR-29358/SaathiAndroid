package com.mosl.mobile.ekyc.register;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.mosl.mobile.ekyc.ui.EkycMainActivity;
import com.mosl.mobile.ekyc.R;

/**
 * Created by shefali on 24-06-2016.
 */
public class AadhaarDialog extends Dialog {
    Context mContext;
    Button btnAgree, btnClose;
    TextView tvHeading, tvHeading1, tvHeading2, tvHeading3;


    public AadhaarDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_aadhaar);
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setUpView();
    }

    private void setUpView() {
        /*TextView txt = (TextView) findViewById(R.id.tvAadhaarno);
        //txt.setPaintFlags(txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnAgree = (Button) findViewById(R.id.btnAgree);
        btnClose = (Button) findViewById(R.id.btnClose);*/

        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent = new Intent(mContext, EkycMainActivity.class);
                mContext.startActivity(intent);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
