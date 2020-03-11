package com.mosl.tabapp.helper.shortcuts;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import com.mosl.tabapp.R;
import com.mosl.tabapp.core.CommonActivity;
import com.mosl.tabapp.login.LoginActivity;
import com.mosl.tabapp.navigation.NavigationConstants;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

@TargetApi(Build.VERSION_CODES.N_MR1) public class ShortcutHelperImpl implements ShortcutHelper {
	private final Context context;
	private final ShortcutManager mShortcutManager;

	ShortcutHelperImpl(Context mContext) {
		this.context = mContext;
		this.mShortcutManager = mContext.getSystemService(ShortcutManager.class);
	}

	private static List<ShortcutInfo> getLoggedInUserShortcuts(Context context) {
		List<ShortcutInfo> shortcutInfos = new ArrayList<>(4);

		shortcutInfos.add(getShortCutInfo(context, "query", "Query", "Query",
        R.drawable.ic_query, NavigationConstants.NAVIGATE_TO_QUERY));
		shortcutInfos.add(getShortCutInfo(context, "calender", "Calendar", "Calendar",
			R.drawable.ic_new_calendar, NavigationConstants.NAVIGATE_TO_CALENDAR));
		shortcutInfos.add(
			getShortCutInfo(context, "birthdayAnniversary", "Birthdays & Anniversary",
				"Birthdays & Anniversary",
          R.drawable.ic_menu_birthday_aniversary,
				NavigationConstants.NAVIGATE_TO_BIRTHDAY_ANNIVERSARY));
		shortcutInfos.add(getShortCutInfo(context, "createLead", "Create Lead", "Create Lead",
			R.drawable.ic_create_lead, NavigationConstants.NAVIGATE_TO_LEAD_CREATION));
		return shortcutInfos;
	}

	private static Intent[] getShortCutIntent(Context context, String tag, int goToPage) {
		return new Intent[] {
			new Intent(Intent.ACTION_MAIN, null, context, CommonActivity.class).putExtra(
				ShortcutConstants.IS_FROM_SHORTCUT, true)
				.putExtra(ShortcutConstants.PAGE_TAG, tag)
				.putExtra(CommonActivity.ARG_PAGE_ID, goToPage).setFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TASK)
		};
	}

	private static List<ShortcutInfo> getLogoutUserShortcuts(Context context) {
		List<ShortcutInfo> shortcutInfos = new ArrayList<>(1);

		shortcutInfos.add(new ShortcutInfo.Builder(context, "login").setShortLabel("Login")
			.setLongLabel("Login")
			.setIcon(Icon.createWithResource(context, R.drawable.ic_login))
				.setIntent(new Intent(Intent.ACTION_VIEW, null, context, LoginActivity.class))
			.build());

		return shortcutInfos;
	}

	private static ShortcutInfo getShortCutInfo(Context context, String shortcutId,
		String shortLabel, String longLabel, int resId, int goToPage) {
		return new ShortcutInfo.Builder(context, shortcutId).setShortLabel(shortLabel)
			.setLongLabel(longLabel)
			.setIcon(Icon.createWithResource(context, resId))
			.setIntents(getShortCutIntent(context, longLabel, goToPage))
			.build();
	}

	@Override public void setLoggedInUserShortcuts() {
		try {
			setShortcuts(getLoggedInUserShortcuts(context));
		} catch (Exception e) {
			Timber.e(e);
		} catch (Error error) {
			//NoSuchMethodError in Xiaomi & Vivo phones
			Timber.e(error);
		}
	}

	@Override public void setLogoutUserShortcuts() {
		try {
			setShortcuts(getLogoutUserShortcuts(context));
		} catch (Exception e) {
			Timber.e(e);
		} catch (Error error) {
			//NoSuchMethodError in Xiaomi & Vivo phones
			Timber.e(error);
		}
	}

	private void setShortcuts(List<ShortcutInfo> shortcuts) {
		if (mShortcutManager == null) return;
		if (mShortcutManager.getDynamicShortcuts().size() != shortcuts.size()) {
			mShortcutManager.setDynamicShortcuts(shortcuts);
		}
	}
}
