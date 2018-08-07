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
import com.mosl.mobile.ekyc.common.MOSLCommon.*;

import static com.mosl.mobile.ekyc.R.id.ivClose;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.EQU_MF_PAN_AADHAAR_SUBMIT;

/**
 * Created by Shefali on 24/11/16.
 */

public class CustomDialog extends Dialog {
    public CustomDialogListener customDialogListener;
    public String msg;
    boolean showYesBtn, showNoBtn, showHeading, showBoldHeading1, dismissYesBtn, dismissNoBtn;
    String txtForYesBtn, txtForNoBtn, txtForHeading, txtForBoldHeading1, dialogFor = "";
    Context mContext;


    public CustomDialog(Context context, String msg, boolean showYesBtn, boolean showNoBtn, boolean showHeading, String txtForHeading, String txtForYesBtn, String txtForNoBtn, CustomDialogListener customDialogListener) {
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
    public CustomDialog(Context context, String msg, boolean showYesBtn, boolean showNoBtn, boolean dismissYesBtn, boolean dismissNoBtn, boolean showHeading, String txtForHeading, String txtForYesBtn, String txtForNoBtn, CustomDialogListener customDialogListener) {
        super(context);
        this.mContext = context;
        this.customDialogListener = customDialogListener;
        this.msg = msg;
        this.showYesBtn = showYesBtn;
        this.showHeading = showHeading;
        this.showNoBtn = showNoBtn;
        this.dismissYesBtn = dismissYesBtn;
        this.dismissNoBtn = dismissNoBtn;
        this.txtForHeading = txtForHeading;
        this.txtForYesBtn = txtForYesBtn;
        this.txtForNoBtn = txtForNoBtn;

    }

    public CustomDialog(Context context, String msg, boolean showYesBtn, boolean showNoBtn, boolean dismissYesBtn, boolean dismissNoBtn, boolean showHeading, boolean showBoldHeading1,
                        String txtForYesBtn, String txtForNoBtn, String txtForHeading, String txtForBoldHeading1, String dialogFor, CustomDialogListener customDialogListener) {
        super(context);
        this.mContext = context;
        this.customDialogListener = customDialogListener;
        this.msg = msg;
        this.showYesBtn = showYesBtn;
        this.showNoBtn = showNoBtn;
        this.dismissYesBtn = dismissYesBtn;
        this.dismissNoBtn = dismissNoBtn;
        this.showHeading = showHeading;
        this.showBoldHeading1 = showBoldHeading1;
        this.txtForYesBtn = txtForYesBtn;
        this.txtForNoBtn = txtForNoBtn;
        this.txtForHeading = txtForHeading;
        this.txtForBoldHeading1 = txtForBoldHeading1;
        this.dialogFor = dialogFor;

    }

    /*public CustomDialog(Context context, String msg1, String msg2, String msg3, boolean showYesBtn, boolean showNoBtn, String txtForYesBtn, String txtForNoBtn, CustomDialogListener customDialogListener) {
        super(context);
        this.customDialogListener = customDialogListener;
        this.msg1 = msg1;
        this.msg2 = msg2;
        this.msg3 = msg3;
        this.showYesBtn = showYesBtn;
        this.showNoBtn = showNoBtn;
        this.txtForYesBtn = txtForYesBtn;
        this.txtForNoBtn = txtForNoBtn;
    }*/


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
        setContentView(R.layout.layout_dialog_yes_no_option);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setWindowAnimations(R.style.animationName);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setCanceledOnTouchOutside(false);

        TextView tvMsg = (TextView) findViewById(R.id.tvMsg);
        TextView tvHeading = (TextView) findViewById(R.id.tvHeading);
        TextView tvBoldHeading1 = (TextView) findViewById(R.id.tvBoldHeading1);
        Button btnYes = (Button) findViewById(R.id.btnYes);
        Button btnNO = (Button) findViewById(R.id.btnNO);
        LinearLayout llDialogButton = (LinearLayout) findViewById(R.id.llDialogButton);
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
        if (showHeading) {
            tvHeading.setVisibility(View.VISIBLE);
            tvHeading.setText(txtForHeading);
        } else {
            tvHeading.setVisibility(View.GONE);
        }
        if (showBoldHeading1) {
            tvBoldHeading1.setVisibility(View.VISIBLE);
            tvBoldHeading1.setText(txtForBoldHeading1);
        } else {
            tvBoldHeading1.setVisibility(View.GONE);
        }

        if (dialogFor.equals(EQU_MF_PAN_AADHAAR_SUBMIT)) {
            llDialogButton.setOrientation(LinearLayout.VERTICAL);
            tvMsg.setGravity(Gravity.LEFT);
        } else {
            llDialogButton.setOrientation(LinearLayout.HORIZONTAL);
            tvMsg.setGravity(Gravity.CENTER);
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.dialogAction(Action.CANCEL);
                dismiss();
            }
        });

        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.dialogAction(Action.NO);
                dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.dialogAction(Action.OK);
                dismiss();
            }
        });
    }

    public void show() {
        try {
            if(!((Activity) mContext).isFinishing()) {
                super.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
