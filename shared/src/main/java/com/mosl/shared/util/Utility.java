package com.mosl.shared.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.provider.Settings;
import androidx.annotation.Nullable;
import java.text.ParseException;
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

	public static Date getCurrentDisplayDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.get(Calendar.YEAR);
		calendar.get(Calendar.MONTH);
		calendar.get(Calendar.DAY_OF_MONTH);
		calendar.get(Calendar.HOUR_OF_DAY);
		calendar.get(Calendar.MINUTE);
		return calendar.getTime();
	}

  public static int[] VORDIPLOM_COLORS = {
      Color.rgb(255, 21, 0), Color.rgb(38, 127, 183), Color.rgb(0, 177, 207),
      Color.rgb(22, 225, 143), Color.rgb(64, 0, 255), Color.rgb(255, 255, 0),
      Color.rgb(254, 46, 46), Color.rgb(255, 21, 0), Color.rgb(38, 127, 183),
      Color.rgb(0, 177, 207),
      Color.rgb(22, 225, 143), Color.rgb(64, 0, 255), Color.rgb(255, 255, 0),
      Color.rgb(254, 46, 46), Color.rgb(64, 0, 255), Color.rgb(255, 255, 0),
      Color.rgb(254, 46, 46)
  };
  public static Date getCurrentDisplayDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.get(Calendar.YEAR);
    calendar.get(Calendar.MONTH);
    calendar.get(Calendar.DAY_OF_MONTH);
    calendar.get(Calendar.HOUR_OF_DAY);
    calendar.get(Calendar.MINUTE);
    return calendar.getTime();
  }

  public static Boolean isTimeInterval(String lastTime, int timeInterval) {
    try {
      Date lastTimeDate = AppDateFormats.DF_JSON_DATE9.parse(lastTime);
      Calendar lastTimeCalendar = Calendar.getInstance();
      lastTimeCalendar.setTime(lastTimeDate);
      lastTimeCalendar.add(Calendar.MINUTE, timeInterval);

      String currentTime = AppDateFormats.DF_JSON_DATE9.format(getCurrentDisplayDate());
      Calendar currentCalendar = Calendar.getInstance();

      Date currentTimeDate = AppDateFormats.DF_JSON_DATE9.parse(currentTime);
      currentCalendar.setTime(currentTimeDate);

      return currentCalendar.getTime().
          after(lastTimeCalendar.getTime());
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return false;
  }
}
