package com.mosl.mobile.ekyc.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mosl.mobile.ekyc.R;
import com.viewpagerindicator.IconPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Owner on 6/13/2016.
 */
public class ApplyNowAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {
    /*public  int[] icons = new int[]{
            R.drawable.ic_personal_tab,
            R.drawable.ic_demat_tab,
            R.drawable.ic_bank_tab,
            R.drawable.ic_upload_tab,
            R.drawable.ic_esign_tab
            };*/
    //R.drawable.ic_ekyc
    public final int[] icons;
    private final String[] pageTitles;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    public ApplyNowAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList, String[] pageTitles, int[] icons) {
        super(fm);
        this.fragmentList = fragmentList;
        this.pageTitles = pageTitles;
        this.icons = icons;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles[position];
    }

    @Override
    public int getIconResId(int index) {
        return icons[index];
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
