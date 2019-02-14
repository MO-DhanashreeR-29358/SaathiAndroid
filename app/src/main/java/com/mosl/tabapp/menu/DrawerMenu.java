package com.mosl.tabapp.menu;

import android.content.Context;

import com.mosl.tabapp.R;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.MenuReport;
import com.mosl.tabapp.ui.SplashActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class DrawerMenu implements MOSLConstants {
    public static List<ScreenDetails> createGroupList(Context context) {
        List<ScreenDetails> groupList = new ArrayList<>();
        if (AccountDetails.isAufsb()) {
            groupList.add(new ScreenDetails(R.drawable.ic_menu_dashboard,
                    context.getString(R.string.MOSL_MENU_My_DASHBOARD_TXT), MOSL_MENU_My_DASHBOARD_TXT,
                    false));
            groupList.add(new ScreenDetails(R.drawable.ic_update_ekyc,
                    context.getString(R.string.MOSL_MENU_EKYC_TXT), MOSL_MENU_EKYC_TXT, true));
            groupList.add(new ScreenDetails(R.drawable.ic_new_logout,
                    context.getString(R.string.MOSL_MENU_LOGOUT_TXT), MOSL_MENU_LOGOUT_TXT, false));
        } else {
            groupList.add(new ScreenDetails(R.drawable.ic_menu_dashboard,
                    context.getString(R.string.MOSL_MENU_My_DASHBOARD_TXT), MOSL_MENU_My_DASHBOARD_TXT,
                    false));
            groupList.add(new ScreenDetails(R.drawable.ic_update_ekyc,
                    context.getString(R.string.MOSL_MENU_EKYC_TXT), MOSL_MENU_EKYC_TXT, true));
            groupList.add(new ScreenDetails(R.drawable.ic_my_reports,
                    context.getString(R.string.MOSL_MENU_MY_REPORTS_TXT), MOSL_MENU_MY_REPORTS_TXT, true));
            groupList.add(new ScreenDetails(R.drawable.ic_menu_calendar,
                    context.getString(R.string.MOSL_MENU_CALENDAR_TXT), MOSL_MENU_CALENDAR_TXT, false));
            groupList.add(new ScreenDetails(R.drawable.ic_my_pending_approvals,
                    context.getString(R.string.MOSL_MENU_PENDING_APPROVALS_TXT),
                    MOSL_MENU_PENDING_APPROVALS_TXT, false));
            groupList.add(new ScreenDetails(R.drawable.ic_sales_brochure,
                    context.getString(R.string.MOSL_MENU_KNOWLEDGE_CENTRE_TXT),
                    MOSL_MENU_KNOWLEDGE_CENTRE_TXT, false));
            groupList.add(new ScreenDetails(R.drawable.ic_menu_birthday_aniversary,
                    context.getString(R.string.MOSL_MENU_BIRTHDAY_ANIVERSARY_TXT),
                    MOSL_MENU_BIRTHDAY_ANIVERSARY_TXT, false));
            if (AccountDetails.isSmtEnabled()) {
                groupList.add(new ScreenDetails(R.drawable.ic_menu_suggest_me_tool,
                        context.getString(R.string.MOSL_MENU_SUGGEST_ME_TXT), MOSL_MENU_SUGGEST_ME_TXT,
                        false));
            }
            groupList.add(new ScreenDetails(R.drawable.ic_new_query,
                    context.getString(R.string.MOSL_MENU_QUERY_TXT), MOSL_MENU_QUERY_TXT, false));
            groupList.add(new ScreenDetails(R.drawable.ic_new_logout,
                    context.getString(R.string.MOSL_MENU_LOGOUT_TXT), MOSL_MENU_LOGOUT_TXT, false));
        }


        return groupList;
    }

    public static Map<String, Vector<SubMenu>> createCollection(Context context,
                                                                List<ScreenDetails> groupList) {
        Vector<SubMenu> ekycSubMenu = new Vector<>();
        Vector<SubMenu> reportSubMenu = new Vector<>();

        ekycSubMenu.add(new SubMenu(context.getString(R.string.MOSL_MENU_FRESH_REGISTRATION_TXT),
                MOSL_MENU_FRESH_REGISTRATION_TXT, true));
        ekycSubMenu.add(new SubMenu(context.getString(R.string.MOSL_MENU_EXISTING_APPLICATIONS_TXT),
                MOSL_MENU_EXISTING_APPLICATIONS_TXT, true));

        if (SplashActivity.menuReportArrayList != null
                && !SplashActivity.menuReportArrayList.isEmpty()) {
            for (MenuReport menuName : SplashActivity.menuReportArrayList) {

                reportSubMenu.add(
                        new SubMenu(menuName.getMenuName(), menuName.getMenuName(), true));
            }
        }

        Map<String, Vector<SubMenu>> subMenuCollections = new LinkedHashMap<>();
        for (ScreenDetails scrdts : groupList) {
            if (scrdts.getTitle().equals(context.getString(R.string.MOSL_MENU_EKYC_TXT))) {
                subMenuCollections.put(scrdts.getTitle(), ekycSubMenu);
            } else if (scrdts.getTitle()
                    .equals(context.getString(R.string.MOSL_MENU_MY_REPORTS_TXT))) {
                subMenuCollections.put(scrdts.getTitle(), reportSubMenu);
            } else {
                subMenuCollections.put(scrdts.getTitle(), new Vector<SubMenu>());
            }
        }
        return subMenuCollections;
    }
}
