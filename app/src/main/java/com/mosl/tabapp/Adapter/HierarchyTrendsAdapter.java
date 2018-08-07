package com.mosl.tabapp.Adapter;

/**
 * Created by raju on 2/27/2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

import java.util.ArrayList;

/**
 * Created by raju on 2/24/2017.
 */

public class HierarchyTrendsAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {
    public final int[] icons;
    private final String[] pageTitles;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    public HierarchyTrendsAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList, String[] pageTitles, int[] icons) {
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


