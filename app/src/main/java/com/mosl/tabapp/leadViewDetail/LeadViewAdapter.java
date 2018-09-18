package com.mosl.tabapp.leadViewDetail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.viewpagerindicator.IconPagerAdapter;
import java.util.ArrayList;

/**
 * Created by Vijay on 14/03/2017.
 */

public class LeadViewAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {

//    public final int[] icons;
    private final String[] pageTitles;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    public LeadViewAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList, String[] pageTitles) {
        super(fm);
        this.fragmentList = fragmentList;
        this.pageTitles = pageTitles;
//        this.icons = icons;
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
        return 0;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
