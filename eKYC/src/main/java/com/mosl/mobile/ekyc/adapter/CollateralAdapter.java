package com.mosl.mobile.ekyc.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.mosl.mobile.ekyc.R;
import com.viewpagerindicator.IconPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Owner on 7/18/2016.
 */
public class CollateralAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {
    private final int[] icons = new int[]{R.drawable.ic_iconhungup,
            R.drawable.ic_iconhungup,
            R.drawable.ic_iconhungup};
    private final String[] pageTitles;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    public CollateralAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList, String[] pageTitles) {
        super(fm);
        this.fragmentList = fragmentList;
        this.pageTitles = pageTitles;
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
//        return icons[index];
        return 0;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
