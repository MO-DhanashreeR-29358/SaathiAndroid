package com.mosl.tabapp.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mosl.tabapp.R;

import org.droidparts.widget.ClearableEditText;

import timber.log.Timber;

/**
 * Created by Sanket Gurav on 9/27/2017.
 */

public class CustomListPickerDialog extends Dialog implements TextWatcher, AdapterView.OnItemClickListener, ClearableEditText.Listener {
    Context context;
    ClearableEditText mSearch;
    ListView mListView;
    ListAdapter mAdapter;
    ListPickerListneable mListner;
    int mThreshold = 1;
    TextView textViewEmpty;
    TextView textViewTitle;
    View ibtnClose;
    View pbLoadingIndicator;


    public void setmListner(ListPickerListneable mListner) {
        this.mListner = mListner;
    }

    public CustomListPickerDialog(Context context) {
        super(context);
        this.context = context;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.CYAN));
        setContentView(R.layout.crm_custom_list_picker);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //region find view
        mSearch = (ClearableEditText) findViewById(R.id.search);
        mSearch.setListener(this);
        mListView = (ListView) findViewById(R.id.listView);
        //  mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ibtnClose = findViewById(R.id.ibtnClose);
        pbLoadingIndicator = findViewById(R.id.pbLoadingIndicator);
        textViewEmpty = (TextView) findViewById(R.id.textViewEmpty);
        textViewTitle = (TextView) findViewById(R.id.appTitle);
        mSearch.addTextChangedListener(this);

        mListView.setOnItemClickListener(this);
        mListView.setEmptyView(textViewEmpty);

        //endregion
        ibtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomListPickerDialog.this.dismiss();
            }
        });

    }

    public void setAdapter(ListAdapter listAdapter) {
        mAdapter = listAdapter;
        if (mListView != null) {
            // ibtnClose.setVisibility(View.VISIBLE);
//            if(mAdapter.getCount()<=0){
//                ibtnClose.setVisibility(View.GONE);
//            }
            mListView.setAdapter(mAdapter);
        }


    }

    public void clearSearchText(){
        if(mSearch != null){
            mSearch.setListener(this);
            mSearch.addTextChangedListener(this);
            mSearch.setText("");
        }
    }

    public int getThreshold() {
        return mThreshold;
    }

    public void setThreshold(int mThreshold) {
        this.mThreshold = mThreshold;
    }

    //region TextChange listner
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() >= mThreshold) {
            Timber.d("Search count -- "+s.length());
            if (mListner != null) {
                Timber.d("Search mListener count -- "+s.length());
                mListner.onSearchTextChanged(s);
                Timber.d("Search mListner count -- "+s.length());
            } else {
                Timber.d("Search Please assing ListPickerListneable");
            }
        }else{
            //Todo dont show any thing
            if(mAdapter!=null){
                mListView.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1));
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    //endregion

    //region list selection

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Parcelable par = (Parcelable) parent.getItemAtPosition(position);
            if (mListner != null) {
                mListner.onListItemSelect(par);
            } else {
                Timber.w("Please assing ListPickerListneable");
            }
        } catch (Exception e) {
        }
    }

    //Called when clear is called
    @Override
    public void didClearText() {
        mAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
        setAdapter(mAdapter);
        //  ibtnClose.setVisibility(View.VISIBLE);
    }
    //endregion


    public interface ListPickerListneable {
        void onListItemSelect(Parcelable data);

        void onSearchTextChanged(CharSequence data);
    }

    public void setInputType(int inpType) {
        mSearch.setInputType(inpType);
    }

    public void showProgress(int s) {
        pbLoadingIndicator.setVisibility(s);
    }

    public void setCustomTitle(CharSequence title) {
        textViewTitle.setText(title);
    }
}
