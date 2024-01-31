package com.mosl.shared.util;

import androidx.annotation.Nullable;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

/**
 * Created by ankitnagda on 8/22/2016.
 */
public class DateParser {

    @Nullable public static Date parseToMM(@Nullable String getDate) {
        if (getDate == null) return null;

        if (isNumeric(getDate)) return new Date(Long.valueOf(getDate));

        try {
            return AppDateFormats.df_Date3.parse(getDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable public static Date parseToInTime(@Nullable String getDate) {
        if (getDate == null) return null;

        if (isNumeric(getDate)) return new Date(Long.valueOf(getDate));

        try {
            return AppDateFormats.df_Time25.parse(getDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Nullable public static Date holidayDateParse(@Nullable String getDate) {
        if (getDate == null) return null;

        if (isNumeric(getDate)) return new Date(Long.valueOf(getDate));

        try {
            return AppDateFormats.df_Date1.parse(getDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseTOMMYYYYHHAM(String getDate) {
        if (getDate == null) return null;

        if (isNumeric(getDate)) return new Date(Long.valueOf(getDate));

        try {
            return AppDateFormats.DF_JSON_DATE4.parse(getDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable public static String formatTOMMYYYYHHAM(@Nullable Date getDate) {
        String date = "";
        if (getDate != null) date = AppDateFormats.DF_JSON_DATE4.format(getDate);
        return date;
    }

    @Nullable public static String formatToMM(@Nullable Date getDate) {
        String date = "";
        if (getDate != null) date = AppDateFormats.df_Date3.format(getDate);
        return date;
    }

    @Nullable public static Date parseToMMM(@Nullable String getDate) {
        if (getDate == null) return null;
        if (isNumeric(getDate)) return new Date(Long.valueOf(getDate));

        try {
            return AppDateFormats.DF_JSON_DATE5.parse(getDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable public static String formatToMMM(@Nullable Date getDate) {
        String date = "";
        if (getDate != null) date = AppDateFormats.DF_JSON_DATE5.format(getDate);
        return date;
    }

    private static boolean isNumeric(@Nullable String str) {
        if (str == null || str.trim().length() == 0) return false;
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }
}
