package com.mosl.tabapp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mosl.tabapp.R;

/**
 * Created by Shefali on 24/11/16.
 */

public class CustomDialog extends Dialog {
    public CustomDialogListener customDialogListener;
    public String msg;
    boolean showYesBtn, showNoBtn, showHeading, showBoldHeading1, dismissYesBtn, dismissNoBtn;
    String txtForYesBtn, txtForNoBtn, txtForHeading, txtForBoldHeading1, dialogFor = "";
    Context mContext;

    public CustomDialog(Context context, String msg, boolean showYesBtn, boolean showNoBtn,
        boolean showHeading, String txtForHeading, String txtForYesBtn, String txtForNoBtn,
        CustomDialogListener customDialogListener) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_yes_no_option);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
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
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.dialogAction(MSFDialog.Action.CANCEL);
                dismiss();
            }
        });

        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.dialogAction(MSFDialog.Action.CANCEL);
                dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.dialogAction(MSFDialog.Action.OK);
                dismiss();
            }
        });
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

    public interface CustomDialogListener {
        public void dialogAction(MSFDialog.Action action, Object... data);
    }
}
