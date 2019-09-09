package com.mosl.tabapp.menu;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.dashboard.DashboardFragment;
import com.mosl.tabapp.ui.MOSLCommonFragment;


public class MenuGetter implements MOSLConstants {

    public static Fragment getFragment(String menuCode) {
        Bundle args = new Bundle();
        //USER USER_TYPE = MOSLCommonActivity.USER_TYPE;
        MOSLCommonFragment frag = null;
        args.clear();

        switch (menuCode) {
            case MOSL_MENU_My_DASHBOARD_TXT:
                args.putInt(SOURCE, MOSL_MENU_DASHBOARD);
                frag = new DashboardFragment();
                frag.setArguments(args);
                break;

        }

        return frag;
    }
}
