package com.mosl.tabapp.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.mosl.tabapp.R;

import org.sufficientlysecure.htmltextview.HtmlTextView;


/**
 * Created by Sanket Gurav on 10/9/2017.
 */

public class CustomWebViewDialog extends Dialog {
    Context context;
    TextView textViewTitle;
    HtmlTextView textViewEmpty;
    View ibtnClose;
    public CustomWebViewDialog(Context context) {
        super(context);
        this.context = context;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.crm_custom_web);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //region find view

        ibtnClose = findViewById(R.id.ibtnClose);

        textViewEmpty = (HtmlTextView) findViewById(R.id.text);
        textViewTitle = (TextView) findViewById(R.id.appTitle);
        //endregion
        ibtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomWebViewDialog.this.dismiss();
            }
        });

    }

public void setTextHtml(String html){textViewEmpty.setHtml(html);}
public void setTextTitle(CharSequence title){textViewTitle.setText(title);}
}
