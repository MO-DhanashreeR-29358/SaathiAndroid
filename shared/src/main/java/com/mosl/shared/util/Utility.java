package com.mosl.shared.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import androidx.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utility {

    public static Date addMinute(Date date, int minute) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + minute);
            return calendar.getTime();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getUuid(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getDate(Date date, String format) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            return df.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getCurrentDate() {
        Date currentDate = new Date();
        return AppDateFormats.DF_JSON_DATE4.format(currentDate);
    }

    public static int getAppVersionCode(Context mContext) {
        try {
            PackageInfo
                pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

	public static String stringOrEmpty(@Nullable String input) {
		if (input != null) {
			return input;
		} else {
			return "";
		}
	}
}
