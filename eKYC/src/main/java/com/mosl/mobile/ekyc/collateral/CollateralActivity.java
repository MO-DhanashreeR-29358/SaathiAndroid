package com.mosl.mobile.ekyc.collateral;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.adapter.CollateralAdapter;
import com.mosl.mobile.ekyc.common.CustomViewPager;
import com.mosl.mobile.ekyc.database.CollateralDBService;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.COLLATERAL_SECTION_PRODUCT;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.COLLATERAL_SECTION_RESEARCH;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.COLLATERAL_SECTION_TECHNOLOGY;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;


/**
 * Created by Owner on 7/18/2016.
 */
public class CollateralActivity extends FragmentActivity {
    private String TAG="CollateralActivity";
    private CollateralDBService dbService;
    private boolean serviceBound;

    CustomViewPager pagerCollateral;
    TabPageIndicator indicatorCollateral;
    ArrayList<Fragment> pageListCollateral = new ArrayList<>();
    LinearLayout llCollateral, mTabsLinearLayoutCollateral;
    public static LinearLayout customProgress;

    CollateralResearchFragment productFragment,researchFragment,technologyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_collateral);
            setPermisson(CollateralActivity.this,getApplicationContext());
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            initDatabase();
            setUpView();
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID()+"-"+getAppLoginID());
//            Crashlytics.logException(e);
        }
    }

    private void setUpView() {
        customProgress = (LinearLayout) findViewById(R.id.customProgress);
        pagerCollateral = (CustomViewPager) findViewById(R.id.pagerCollateral);
        indicatorCollateral = (TabPageIndicator) findViewById(R.id.indicatorCollateral);
        llCollateral = (LinearLayout) findViewById(R.id.llCollateral);

        String[] headingClient = new String[3];
        headingClient[0] = COLLATERAL_SECTION_PRODUCT;
        headingClient[1] = COLLATERAL_SECTION_RESEARCH;
        headingClient[2] = COLLATERAL_SECTION_TECHNOLOGY;

        productFragment = new CollateralResearchFragment();
        productFragment.setSection(COLLATERAL_SECTION_PRODUCT);

        researchFragment = new CollateralResearchFragment();
        researchFragment.setSection(COLLATERAL_SECTION_RESEARCH);

        technologyFragment = new CollateralResearchFragment();
        technologyFragment.setSection(COLLATERAL_SECTION_TECHNOLOGY);

        pageListCollateral.add(productFragment);
        pageListCollateral.add(researchFragment);
        pageListCollateral.add(technologyFragment);

        final FragmentStatePagerAdapter adapterClient = new CollateralAdapter(getSupportFragmentManager(), pageListCollateral, headingClient);
        pagerCollateral.setAdapter(adapterClient);
        pagerCollateral.setPagingEnabled(false);

        indicatorCollateral.setViewPager(pagerCollateral);

        mTabsLinearLayoutCollateral = ((LinearLayout) indicatorCollateral.getChildAt(0));
        indicatorCollateral.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            CollateralDBService.LocalBinder binder = (CollateralDBService.LocalBinder) service;
            dbService = binder.getService();

            productFragment.setDbService(dbService);
            researchFragment.setDbService(dbService);
            technologyFragment.setDbService(dbService);

            serviceBound = true;
            Log.d(TAG,"DB service connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            serviceBound = false;
            Log.d(TAG,"DB service disconnected");
        }
    };

    private void initDatabase(){
        try {
            Intent intent = new Intent();
            intent.setClass(this, CollateralDBService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

