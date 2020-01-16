package com.mosl.mobile.ekyc.ui;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.adapter.AutoCompleteAdapter;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.model.LeadModel;

import static com.mosl.mobile.ekyc.common.MOSLCommon.getPrefs;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isBlank;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.model.CommonModel.*;

/**
 * Created by Shefali on 16/08/16.
 */
public class PostLoginActivity extends FragmentActivity implements MOSLConstants {
    DelayAutoCompleteTextView searchBoxView;
    AutoCompleteAdapter autoCompleteAdapter;
    SearchDetailFragment searchDetailFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();
    public static int check = 0;
    public static LinearLayout customProgress;
    ImageView ivExit, ivBack;
    TextView tvExitForm, tvAppLoginId;
    LinearLayout rlExitForm, rlLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setPermisson(PostLoginActivity.this, this);
            setContentView(R.layout.activity_post_login);
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            setUpView();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            PostLoginFragment postLoginFragment = new PostLoginFragment();
            fragmentTransaction.replace(R.id.activityFrameLayout, postLoginFragment);
            fragmentManager.popBackStack();
            fragmentTransaction.commit();
            clearAllCommonModelFields();
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
//            Crashlytics.logException(e);
        }

    }

    /*private void setPermisson() {
        if (
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            PostLoginActivity.this.requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO,android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            return;
        }
    }*/
    public DelayAutoCompleteTextView getSearchBoxView() {
        return searchBoxView;
    }

    public AutoCompleteAdapter getSearchAdapter() {
        return autoCompleteAdapter;
    }

    private void setUpView() {

        rlExitForm = (LinearLayout) findViewById(R.id.rlExitForm);
        rlExitForm.setVisibility(View.GONE);
        rlLogout = (LinearLayout) findViewById(R.id.rlLogout);
        rlLogout.setVisibility(View.GONE);
        ivExit = (ImageView) findViewById(R.id.ivExit);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        //   tvExitForm = (TextView) findViewById(R.id.tvExitForm);
        tvAppLoginId = (TextView) findViewById(R.id.tvAppLoginId);
        tvAppLoginId.setText(getAppLoginID());
//        tvExitForm.setText("Logout");
        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getAppType().equals(CRM)) {
                    getPrefs(PostLoginActivity.this).edit().putBoolean("AUTHENTICATED", false).apply();
                    //Util.getPrefs(this).edit().putString("LOGIN_ID", null).apply();
                    getPrefs(PostLoginActivity.this).edit().putString("APP_LOGIN_ID", null).apply();
                    getPrefs(PostLoginActivity.this).edit().putString("PASSWORD", null).apply();
                    getPrefs(PostLoginActivity.this).edit().putString(BOBRANCH, "").apply();
                    getPrefs(PostLoginActivity.this).edit().putString(BOSUBBROKER, "").apply();
                    getPrefs(PostLoginActivity.this).edit().putString(BOTRADERS, "").apply();
                    getPrefs(PostLoginActivity.this).edit().putString(CRMBRANCH, "").apply();
                    getPrefs(PostLoginActivity.this).edit().putString(LOGINUSERID, "").apply();
                    getPrefs(PostLoginActivity.this).edit().putString(ROLEMAPPINGID, "").apply();
                    getPrefs(PostLoginActivity.this).edit().putString(BUSINESSUNITID, "").apply();
                    getPrefs(PostLoginActivity.this).edit().putString(TEAMID, "").apply();
                    getPrefs(PostLoginActivity.this).edit().putString(ROLEID, "").apply();
                    /*Intent intent = new Intent(PostLoginActivity.this, BranchLoginActivity.class);
                    startActivity(intent);*/
                    finish();
                }else {
                    finish();
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchBoxView = (DelayAutoCompleteTextView) findViewById(R.id.searchText);
        searchBoxView.setThreshold(3);
        autoCompleteAdapter = new AutoCompleteAdapter(this);
        searchBoxView.setAdapter(autoCompleteAdapter);
        searchBoxView.setLoadingIndicator((ProgressBar) findViewById(R.id.pbLoadingIndicator));
        customProgress = (LinearLayout) findViewById(R.id.customProgress);

        getSearchBoxView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final LeadModel leadModel = getSearchAdapter().getItem(position);
                if (isBlank(leadModel.getId())) {
                    return;
                }

                searchDetailFragment = new SearchDetailFragment();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentManager.popBackStack();
                Bundle bundle = new Bundle();
                bundle.putString(LEADID, leadModel.getId());
                bundle.putString(CLIENT_NAME, leadModel.getLeadName());
                bundle.putString(CLIENT_CODE, leadModel.getLeadCode());
                bundle.putString(DATE, leadModel.getDate());
                bundle.putString(STATUS, leadModel.getStatus());
                bundle.putString(MOB_NUM, leadModel.getMobNum());
                bundle.putString(BRANCH, leadModel.getBranch());
                searchDetailFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.activityFrameLayout, searchDetailFragment).commit();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (check == 0) {
            finish();
        } else {
            searchDetailFragment.onBackPressed();
        }
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
