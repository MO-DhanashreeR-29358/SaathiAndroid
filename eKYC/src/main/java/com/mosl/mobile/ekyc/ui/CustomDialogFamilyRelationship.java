package com.mosl.mobile.ekyc.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.MOSLCommon;

import static com.mosl.mobile.ekyc.constants.MOSLConstants.EQU_MF_PAN_AADHAAR_SUBMIT;

/**
 * Created by Featsystem on 21-06-2017.
 */

public class CustomDialogFamilyRelationship extends Dialog {
    public CustomDialog.CustomDialogListener customDialogListener;
    public String msg;
    private boolean showYesBtn, showNoBtn, showHeading;
    private String txtForYesBtn, txtForNoBtn, txtForHeading, dialogFor = "";
    private Context mContext;
    private LinearLayout llcustomProgress;


    public CustomDialogFamilyRelationship(Context context, String msg, boolean showYesBtn, boolean showNoBtn, boolean showHeading, String txtForHeading, String txtForYesBtn, String txtForNoBtn, CustomDialog.CustomDialogListener customDialogListener) {
        super(context);
        this.mContext = context;
        this.customDialogListener = customDialogListener;
        this.msg = msg;
        this.showYesBtn = showYesBtn;
        this.showHeading = showHeading;
        this.showNoBtn = showNoBtn;
        this.txtForHeading = txtForHeading;
        this.txtForYesBtn = txtForYesBtn;
        this.txtForNoBtn = txtForNoBtn;

    }

    public interface CustomDialogListener {
        /* void onYesButtonClick();

         void onNoButtonClick();

         void onCloseButtonClick();*/
        public void dialogAction(MOSLCommon.Action action, Object... data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_family_declaration_yes_no);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setWindowAnimations(R.style.animationName);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setCanceledOnTouchOutside(false);

        TextView tvMsg = (TextView) findViewById(R.id.tvMsg);
        Button btnYes = (Button) findViewById(R.id.btnYes);
        Button btnNO = (Button) findViewById(R.id.btnNO);
        llcustomProgress = (LinearLayout) findViewById(R.id.customProgress);
        ImageView ivClose = (ImageView) findViewById(R.id.ivClose);
        tvMsg.setText(msg);
        btnYes.setText(txtForYesBtn);
        btnNO.setText(txtForNoBtn);
        if (!showYesBtn) {
            btnYes.setVisibility(View.GONE);
        } else {
            btnYes.setVisibility(View.VISIBLE);
        }
        if (!showNoBtn) {
            btnNO.setVisibility(View.GONE);
        } else {
            btnNO.setVisibility(View.VISIBLE);
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.dialogAction(MOSLCommon.Action.CANCEL);
                dismiss();
            }
        });

        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.dialogAction(MOSLCommon.Action.NO);
                dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.dialogAction(MOSLCommon.Action.OK1);
            }
        });
    }

    public void showProgressBar() {
        llcustomProgress.setVisibility(View.VISIBLE);
    }

    public void hideProgressbar() {
        llcustomProgress.setVisibility(View.GONE);
    }

    public void show() {
        try {
            if (!((Activity) mContext).isFinishing()) {
                super.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}