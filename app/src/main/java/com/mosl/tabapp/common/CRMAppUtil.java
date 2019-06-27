package com.mosl.tabapp.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mosl.tabapp.R;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mosl.tabapp.constants.MOSLConstants.DATE_FORMAT_TO_SERVER;
import static com.mosl.tabapp.constants.MOSLConstants.FILE_UPLOAD_SIZE_LIMT;

/**
 * Created by Pramod on 20-01-2016.
 */
public class CRMAppUtil {
    public static SharedPreferences getPrefs(Context ctx) {
        return ctx.getSharedPreferences(ctx.getPackageName(), Context.MODE_PRIVATE);
    }

    public static String getDateStrT(String date, String inputFormat, String outputFormat) {
        try {
            String dt = getDateFromString(date, inputFormat, outputFormat);
            if (isBlank(dt)) {
                return date;
            } else {
                return dt;
            }
        } catch (Exception e) {
            return date;
        }
    }

    public static String getDateStr(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static Date getDate(String date, String format) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format, Locale.US);
            return df.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return null;
    }

    /**
     * input filter which will stop special character
     */
    public static final InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!Character.isLetterOrDigit(source.charAt(i)) || (source.charAt(i) == ' ')) {
                    return "";
                }
            }
            return null;
        }
    };
    /**
     * input filter which will stop special character
     */
    public static final InputFilter filterOnlyAlphabet = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            boolean keepOriginal = true;
            StringBuilder sb = new StringBuilder(end - start);
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (isCharAllowed(c)) // put your condition here
                    sb.append(c);
                else
                    keepOriginal = false;
            }
            if (keepOriginal)
                return null;
            else {
                if (source instanceof Spanned) {
                    SpannableString sp = new SpannableString(sb);
                    TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                    return sp;
                } else {
                    return sb;
                }
            }
        }

        private boolean isCharAllowed(char c) {
            Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
            Matcher ms = ps.matcher(String.valueOf(c));
            return ms.matches();
        }
    };
    public static final InputFilter filterOnlyAlphabetAndNumbers = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            boolean keepOriginal = true;
            StringBuilder sb = new StringBuilder(end - start);
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (isCharAllowed(c)) // put your condition here
                    sb.append(c);
                else
                    keepOriginal = false;
            }
            if (keepOriginal)
                return null;
            else {
                if (source instanceof Spanned) {
                    SpannableString sp = new SpannableString(sb);
                    TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                    return sp;
                } else {
                    return sb;
                }
            }
        }

        private boolean isCharAllowed(char c) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9 ]");
            Matcher ms = ps.matcher(String.valueOf(c));
            return ms.matches();
        }
    };

    public static final InputFilter filterOnlyAlphabetAndNumbersAnsAllowedChar = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            boolean keepOriginal = true;
            StringBuilder sb = new StringBuilder(end - start);
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (isCharAllowed(c)) // put your condition here
                    sb.append(c);
                else
                    keepOriginal = false;
            }
            if (keepOriginal)
                return null;
            else {
                if (source instanceof Spanned) {
                    SpannableString sp = new SpannableString(sb);
                    TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                    return sp;
                } else {
                    return sb;
                }
            }
        }

        private boolean isCharAllowed(char c) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9 ,@!#&()_\\n]");
            Matcher ms = ps.matcher(String.valueOf(c));
            return ms.matches();
        }
    };


    public static String getDate(Date date, String format) {
        try {
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

    /**
     * @param date         input date string
     * @param inputFormat
     * @param outputFormat
     * @return
     */
    public static String getDateFromString(String date, String inputFormat, String outputFormat) {
        if (isBlank(date) || isBlank(inputFormat) || isBlank(outputFormat)) {
            return "";
        }

        Date dateObj = getDate(date, inputFormat);
        if (dateObj == null) {
            return "";
        } else
            return getDateStr(dateObj, outputFormat);
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
    }


    public static String getStartDtFinancialYearGivenReactivation(String format) {
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
        if (input == null) {
            return true;
        }
        if (input.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNetworkEnabled(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        //Log.i("IP", "***** IP=" + ip);
                        return ip;
                    }
                }
            }
        } catch (Exception ex) {
            //Log.e("IP", ex.toString());
        }
        return "127.0.0.1";
    }


    /**
     * @param year
     * @param month
     * @param day
     * @param hour   in 24 hrs
     * @param minute
     * @return @return 2017-03-30T15:21:13.0117439+05:30
     */
    public static String getDBDate(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        Date date = calendar.getTime();
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ssZZZ");
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_TO_SERVER);
        return sdf.format(date);
    }

    /**
     * @param year
     * @param month
     * @param day
     * @param hour   in 24 hrs
     * @param minute
     * @return @return 2017-03-30T15:21:13.0117439+05:30
     */
    public static Date getDBDateObj(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        return calendar.getTime();
       /* SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ssZZZ");
        return sdf.format(date);*/
    }

  /*  public static boolean getWeekObj(int weekNumber, String date) {
        Date dateStr = CRMAppUtil.getDate(date, "dd/M/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(dateStr);
        int weekOfMonth = c.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        if (weekNumber == 5) {
            return false;
        } else if (weekNumber == weekOfMonth) {
            return true;
        }
        return false;
    }*/

    public static String getDBDate(Date date, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    //"yyyy-MM-dd'T'kk:mm:ssZZZ"
    public static Date getDBDateFrom(String Date, String format) throws java.text.ParseException {


        //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(Date);

        //return sdf.format(date);
    }

    //"yyyy-MM-dd'T'kk:mm:ssZZZ"
    public static Date getDBDateFrom(String Date) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss");
            return sdf.parse(Date);
        } catch (java.text.ParseException pe) {

            Calendar calendar = Calendar.getInstance();
            calendar.get(Calendar.YEAR);
            calendar.get(Calendar.MONTH);
            calendar.get(Calendar.DAY_OF_MONTH);
            calendar.get(Calendar.HOUR_OF_DAY);
            calendar.get(Calendar.MINUTE);

            Date date = calendar.getTime();
            return date;
        }
        //return sdf.format(date);
    }

    public static String getDBDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.YEAR);
        calendar.get(Calendar.MONTH);
        calendar.get(Calendar.DAY_OF_MONTH);
        calendar.get(Calendar.HOUR_OF_DAY);
        calendar.get(Calendar.MINUTE);

        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_TO_SERVER);
        return sdf.format(date);
    }

    public static String getCurrentDBDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.YEAR);
        calendar.get(Calendar.MONTH);
        calendar.get(Calendar.DAY_OF_MONTH);
        calendar.get(Calendar.HOUR_OF_DAY);
        calendar.get(Calendar.MINUTE);

        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd' 'kk:mm:ss");
        return sdf.format(date);

    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.YEAR);
        calendar.get(Calendar.MONTH);
        calendar.get(Calendar.DAY_OF_MONTH);
        calendar.get(Calendar.HOUR_OF_DAY);
        calendar.get(Calendar.MINUTE);

        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String getCurrentDisplayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.YEAR);
        calendar.get(Calendar.MONTH);
        calendar.get(Calendar.DAY_OF_MONTH);
        calendar.get(Calendar.HOUR_OF_DAY);
        calendar.get(Calendar.MINUTE);

        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd' 'kk:mm:ss");
        return sdf.format(date);
    }

    public static String getDayMonthYearDate(String date) {
        String format = null;
        try {
            String dateArray[] = date.split("-");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, Integer.parseInt(dateArray[0]));
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]));
            cal.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1);
            format = new SimpleDateFormat("E MMM yy").format(cal.getTime());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return format;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static String getFileNameFromPath(String filePath) {
        if (isBlank(filePath)) return "";

        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    /**
     * check if the provided date is less than TODAY date (with TIME= HOUR & MINUTE only)
     *
     * @param date
     * @return
     */
    public static boolean isBeforeDate(Date date) {
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.set(Calendar.SECOND, 0);
        todayCalendar.set(Calendar.MILLISECOND, 0);

        long todayDate = todayCalendar.getTimeInMillis();
        long inputDate = date.getTime();

        return inputDate < todayDate;
    }

    public static boolean isBeforeandEqualDate(Date date) {
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.set(Calendar.SECOND, 0);
        todayCalendar.set(Calendar.MILLISECOND, 0);

        long todayDate = todayCalendar.getTimeInMillis();
        long inputDate = date.getTime();

        return inputDate <= todayDate;
    }

    /**
     * load
     * returns file size in mb
     */
    public static boolean isSizeAbove(long fileSizeInBytes) {
      /*  long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        return fileSizeInKB / 1024;*/

        // long fileSizeInBytes = f.length();
        long fileSizeInKB = fileSizeInBytes / 1024;
        long fileSizeInMB = fileSizeInKB / 1024;
        if (fileSizeInMB >= FILE_UPLOAD_SIZE_LIMT) {
            return true;
        } else {
            return false;
        }
    }

    public static String getFilenameWithoutExtension(String fileName) {
        try {
            if (fileName == null || fileName.indexOf(".") == -1) {
                return fileName;
            }
            return fileName.substring(0, fileName.indexOf("."));
        } catch (Exception ex) {

        }
        return fileName;
    }

    public static String getJsonSafeString(String data) {
        return data.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public static void setImage(byte[] byt, final ImageView ivLead, final Context context) {
        Glide.with(context).load(byt).asBitmap().placeholder(R.drawable.default_image).centerCrop().into(new BitmapImageViewTarget(ivLead) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivLead.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    public static String getRefreshColumnName(String columnName) {
        columnName = columnName.replace("__KENDOGRP__", " ");
        columnName = columnName.replace("__KENDOSLASH__", "/");
        columnName = columnName.replace("__KENDOPLUS__", "+");
        columnName = columnName.replace("__KENDODASH__", "-");
        columnName = columnName.replace("__KENDODOT__", ".");
        columnName = columnName.replace("__KENDORNDBRCOPN__", "(");
        columnName = columnName.replace("__KENDORNDBRCCLS__", ")");
        columnName = columnName.replace("__KENDOEMPTY__", " ");
        columnName = columnName.replace("_", " ");

        return columnName;
    }

    public static void expand(final View v) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static boolean isMockSettingsON(Context context) {
        // returns true if mock location enabled, false if not enabled.
        if (Settings.Secure.getString(context.getContentResolver(),
            Settings.Secure.ALLOW_MOCK_LOCATION).equals("0")) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isLocationFromMockProvider(Context context, Location location) {
        boolean isMock;
        if (Build.VERSION.SDK_INT >= 18) {
            isMock = location.isFromMockProvider();
        } else {
            isMock = isMockSettingsON(context);
        }
        return isMock;
    }

    public static boolean validateOnlyAlphabetAndNumberWithoutSplChar(String input) {
        String CODE_PATTERN = "^[a-zA-Z0-9 ]*$";

        if (isBlank(input)) {
            return false;
        }
        Pattern pattern = Pattern.compile(CODE_PATTERN);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
