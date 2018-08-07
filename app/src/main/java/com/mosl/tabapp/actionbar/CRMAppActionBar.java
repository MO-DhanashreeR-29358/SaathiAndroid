package com.mosl.tabapp.actionbar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mosl.tabapp.R;
import com.mosl.tabapp.clientsearch.DelayAutoCompleteTextView;
import com.mosl.tabapp.ui.MOSLCommonActivity.AutoCompleteAdapter;

import static com.mosl.tabapp.constants.MOSLConstants.SEARCH_LENTH;


public class CRMAppActionBar extends RelativeLayout implements TextView.OnEditorActionListener {
    private int msgCount;
    private ImageView srchImg, mHomeBtn, ivIcon,
            btnNotification;
    private TextView mTitleView, tvMsgCount;
    private DelayAutoCompleteTextView searchBoxView;
    private Context mContext;

    public CRMAppActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout mBarView = (RelativeLayout) mInflater.inflate(R.layout.crm_actionbar, null);
        addView(mBarView);
        setView(mBarView);
        mContext = context;

    }

    public DelayAutoCompleteTextView getSearchBoxView() {
        return searchBoxView;
    }

    private void setView(RelativeLayout mBarView) {
        mHomeBtn = (ImageView) mBarView.findViewById(R.id.btnMenu);
        srchImg = (ImageView) mBarView.findViewById(R.id.srchImg);
        btnNotification = (ImageView) mBarView.findViewById(R.id.btnNotification);
        searchBoxView = (DelayAutoCompleteTextView) mBarView.findViewById(R.id.searchText);

        searchBoxView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchBoxView.setOnEditorActionListener(this);
        mTitleView = (TextView) mBarView.findViewById(R.id.appTitle);
        tvMsgCount = (TextView) mBarView.findViewById(R.id.tvMsgCount);
        searchBoxView.setThreshold(SEARCH_LENTH);
        searchBoxView.setLoadingIndicator((ProgressBar) mBarView.findViewById(R.id.pbLoadingIndicator));
        searchBoxView.setDropDownBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ivIcon = (ImageView) mBarView.findViewById(R.id.ivIcon);
    }


    public void clearSearchText() {
        searchBoxView.setText("");
    }

    public void setHomeAction(OnClickListener onClickAction) {
        mHomeBtn.setOnClickListener(onClickAction);

        mHomeBtn.setVisibility(View.VISIBLE);

    }

    public void updateMsgCount(int count) {
        msgCount += count;
        updateCnt(msgCount);

    }

    public void updateCnt(int cnt) {
        if (cnt <= 0) {
            ivIcon.setVisibility(GONE);
            tvMsgCount.setVisibility(GONE);
            tvMsgCount.setText(cnt + "");
            msgCount = 0;
        } else {
            ivIcon.setVisibility(VISIBLE);
            tvMsgCount.setVisibility(VISIBLE);
            tvMsgCount.setText(cnt + "");
        }
    }

    public void onClickSearch(OnClickListener onClickListener) {
        srchImg.setOnClickListener(onClickListener);
    }

    public void showNotification(OnClickListener onClickListener) {
        btnNotification.setOnClickListener(onClickListener);

    }


    public void visibleBusinessGlobalIcon() {
        btnNotification.setVisibility(View.VISIBLE);
    }


    public void hideBusinessSummaryBtn() {
        btnNotification.setVisibility(View.INVISIBLE);
    }


    public void setTitle(CharSequence title) {
        mTitleView.setText(title);
    }

    public void setTitleVisibility(int state) {
        mTitleView.setVisibility(state);
    }

    public void setAdapter(AutoCompleteAdapter autoCompleteAdapter) {
        searchBoxView.setAdapter(autoCompleteAdapter);
    }


    public void setSearchHint(CharSequence hint) {
        clearSearchText();
        searchBoxView.setHint(hint);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        hideKeyboard(v);
        return false;
    }

    //TODO replace from here to util as ther are multiple copies
    public void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
