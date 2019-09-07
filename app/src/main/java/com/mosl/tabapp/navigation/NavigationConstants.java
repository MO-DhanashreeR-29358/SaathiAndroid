package com.mosl.tabapp.navigation;

import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE) @IntDef({
	NavigationConstants.NAVIGATE_TO_LOGOUT,
	NavigationConstants.NAVIGATE_TO_NONE
})
public @interface NavigationConstants {
	int NAVIGATE_TO_NONE = -1;
	int NAVIGATE_TO_LOGOUT = 1;
	int NAVIGATE_TO_DASHBOARD = 2;
	int NAVIGATE_TO_FRESH_REG = 3;
	int NAVIGATE_TO_EXISTING_APP = 4;
	int NAVIGATE_TO_CALENDAR = 5;
	int NAVIGATE_TO_PENDING_APPROVAL = 6;
	int NAVIGATE_TO_SALES_BROCHURE = 7;
	int NAVIGATE_TO_SALES_STADIUM = 8;
	int NAVIGATE_TO_BIRTHDAY_ANNIVERSARY = 9;
	int NAVIGATE_TO_SUGGEST_ME = 10;
	int NAVIGATE_TO_QUERY = 11;
}
