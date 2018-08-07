package com.mosl.mobile.ekyc.common;

import android.util.Base64;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Vijay on 10/19/2016.
 */

public class EkycAppUtil {
    public static String getDateStr(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static Date getDate(String date, String format) {
        try {
            if (isBlank(date)) {
                return null;
            }
            SimpleDateFormat df = new SimpleDateFormat(format);
            return df.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static int getYear(Date date) {
        try {
            if (date == null) {
                return 1900;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.YEAR);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 1900;
    }

    public static int getMonth(Date date) {
        try {
            if (date == null) {
                return 1;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return (calendar.get(Calendar.MONTH) + 1);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 1;
    }

    public static int getDay(Date date) {
        try {
            if (date == null) {
                return 1;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.DATE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 1;
    }

    public static String getDate(Date date, String format) {
        try {
            if (date == null) {
                return "";
            }
            SimpleDateFormat df = new SimpleDateFormat(format);
            return df.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getDateStr(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd, MMM yyyy");
        return df.format(date);
    }

    public static String getMonthYearStr(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("MMM yyyy");
        return df.format(date);
    }

    public static String getWeekDateStr(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Calendar first = (Calendar) cal.clone();
        first.add(Calendar.DAY_OF_WEEK,
                first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));

        Calendar last = (Calendar) first.clone();
        last.add(Calendar.DAY_OF_YEAR, 6);

        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        SimpleDateFormat df = new SimpleDateFormat("dd, MMM yyyy");

        return (dayFormat.format(first.getTime()) + " - " + df.format(last.getTime()));
    }

    public static String getYearStr(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        return df.format(date);
    }

    public static String getDayOfMonthSuffix(final int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static String decodeBase64(String data) {
        try {
            byte[] dataByte = Base64.decode(data, Base64.DEFAULT);
            return new String(dataByte, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static Date createDate(int day, int month, int year) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    public static String getFirstDateOfCurrentMonth(String format) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(cal.getTime());
    }

    public static String getLastDateOfCurrentMonth(String format) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(cal.getTime());
    }

    public static String getStartDtFinancialYear(String format) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        if (month >= 3) {
            calendar.set(Calendar.YEAR, year);
        } else {
            calendar.set(Calendar.YEAR, year - 1);
        }
        calendar.set(Calendar.MONTH, 3);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(calendar.getTime());
//        return calendar;
    }

    public static String getAdultDate(String format) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.YEAR, year - 18);
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(calendar.getTime());
    }

    public boolean isNumber(String value) {
        try {
            Double.parseDouble(value);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public static boolean isBlank(String input) {
        if (input == null)
            return true;
        if (input.trim().length() == 0)
            return true;
        return false;
    }
}
