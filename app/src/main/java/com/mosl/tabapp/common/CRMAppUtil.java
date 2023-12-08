package com.mosl.tabapp.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
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

import timber.log.Timber;

/**
 * Created by Pramod on 20-01-2016.
 */
public class CRMAppUtil {

    public static final InputFilter filterOnlyAlphabetAndNumbers = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
            int dstart,
            int dend) {

            boolean keepOriginal = true;
            StringBuilder sb = new StringBuilder(end - start);
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (isCharAllowed(c)) // put your condition here
                {
                    sb.append(c);
                } else {
                    keepOriginal = false;
                }
            }
            if (keepOriginal) {
                return null;
            } else {
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
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.ENGLISH);
        return df.format(date);
    }

    public static Date getDate(String date, String format) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format, Locale.ENGLISH);
            return df.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getDate(Date date, String format) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format, Locale.ENGLISH);
            return df.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getDateStr(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd, MMM yyyy", Locale.ENGLISH);
        return df.format(date);
    }

    /**
     * @param date input date string
     */
    public static String getDateFromString(String date, String inputFormat, String outputFormat) {
        if (isBlank(date) || isBlank(inputFormat) || isBlank(outputFormat)) {
            return "";
        }

        Date dateObj = getDate(date, inputFormat);
        if (dateObj == null) {
            return "";
        } else {
            return getDateStr(dateObj, outputFormat);
        }
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

    public static String getFirstDateOfCurrentMonth(String format) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,
            Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.ENGLISH);
        return df.format(cal.getTime());
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
        ConnectivityManager cm =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                    enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        //Log.i("IP", "***** IP=" + ip);
                        return Formatter.formatIpAddress(inetAddress.hashCode());
                    }
                }
            }
        } catch (Exception ex) {
            //Log.e("IP", ex.toString());
        }
        return "127.0.0.1";
    }

    /**
     * @param hour in 24 hrs
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
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_TO_SERVER, Locale.ENGLISH);
        return sdf.format(date);
    }

    /**
     * @param hour in 24 hrs
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

    public static String getDBDate(Date date, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        return sdf.format(date);
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

    //"yyyy-MM-dd'T'kk:mm:ssZZZ"
    public static Date getDBDateFrom(String Date, String format) throws java.text.ParseException {

        //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        return sdf.parse(Date);

        //return sdf.format(date);
    }

    //"yyyy-MM-dd'T'kk:mm:ssZZZ"
    public static Date getDBDateFrom(String Date) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss", Locale.ENGLISH);
            return sdf.parse(Date);
        } catch (java.text.ParseException pe) {

            Calendar calendar = Calendar.getInstance();
            calendar.get(Calendar.YEAR);
            calendar.get(Calendar.MONTH);
            calendar.get(Calendar.DAY_OF_MONTH);
            calendar.get(Calendar.HOUR_OF_DAY);
            calendar.get(Calendar.MINUTE);

            return calendar.getTime();
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
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_TO_SERVER, Locale.ENGLISH);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd' 'kk:mm:ss", Locale.ENGLISH);
        return sdf.format(date);
    }

    public static String getFileNameFromPath(String filePath) {
        if (isBlank(filePath)) return "";

        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    /**
     * check if the provided date is less than TODAY date (with TIME= HOUR & MINUTE only)
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
        return fileSizeInMB >= FILE_UPLOAD_SIZE_LIMT;
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
        Glide.with(context)
            .load(byt)
            .asBitmap()
            .placeholder(R.drawable.default_image)
            .centerCrop()
            .into(new BitmapImageViewTarget(ivLead) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    ivLead.setImageDrawable(circularBitmapDrawable);
                }
            });
    }

    public static void setImageUrl(String imageUrl, final ImageView imageView,
        final Context context) {


        Glide.with(context).load(imageUrl).asBitmap().placeholder(R.drawable.ic_white).
            centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .listener(new RequestListener<String, Bitmap>() {
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                    Timber.tag("Glide Image").d("Glide Image Error :%s", e.getMessage());
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {

                    return false;
                }
            })
            .into(new BitmapImageViewTarget(imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    imageView.setImageDrawable(circularBitmapDrawable);
                }
            });
    }

    public static String getRefreshColumnName(String columnName) {
        if (columnName.equalsIgnoreCase("lead_id")) {
            columnName = columnName.toUpperCase();
        }
        columnName = columnName.replace("__KENDOGRP__", " ");
        columnName = columnName.replace("__KENDOPERCENTAGE__", "%");
        columnName = columnName.replace("__KENDOSLASH__", "/");
        columnName = columnName.replace("__KENDOPLUS__", "+");
        columnName = columnName.replace("__KENDODASH__", "-");
        columnName = columnName.replace("__KENDODOT__", ".");
        columnName = columnName.replace("__KENDORNDBRCOPN__", "(");
        columnName = columnName.replace("__KENDORNDBRCCLS__", ")");
        columnName = columnName.replace("__KENDOEMPTY__", " ");
        columnName = columnName.replace("flg_Hierarchy", "Hierarchy");
        columnName = columnName.replace("flt_Hierarchy_empCode", "RM Name");
        columnName = columnName.replace("flt_Hierarchy_Branch_Code", "Branch Code");
        columnName = columnName.replace("_", " ");

        return columnName;
    }

    public static String getRefreshReactivationColumnName(String columnName) {

        columnName = columnName.replace("IsEditable", "Action");
        columnName = columnName.replace("ClientName", "Client Name");
        columnName = columnName.replace("LeadId", "Lead Id");
        columnName = columnName.replace("EmailId", "Email Id");
        columnName = columnName.replace("AssignedDate", "Assigned Date");
        columnName = columnName.replace("LastMeetingDate", "Last Meeting Date");
        columnName = columnName.replace("SMName", "SM Name");
        columnName = columnName.replace("ASMName", "ASM Name");
        columnName = columnName.replace("RHName", "RH Name");
        columnName = columnName.replace("FinalActivationDate", "Final Activation Date");
        columnName = columnName.replace("ClientBranch", "Client Branch");
        columnName = columnName.replace("CurrentStage", "Current Stage");
        columnName = columnName.replace("RefNo", "Ref No");

        columnName = columnName.replace("EmployeeCode", "Employee Code");
        columnName = columnName.replace("EmployeeName", "Employee Name");
        if(columnName.equals("FirstReportingTo")){
            columnName = columnName.replace("FirstReportingTo", "First Reporting To");
        }
        if(columnName.equals("FirstReportingToName")){
            columnName = columnName.replace("FirstReportingToName", "First Reporting To Name");
        }
        if(columnName.equals("ClientCode")){
            columnName = columnName.replace("ClientCode", "Client Code");
        }
        if(columnName.equals("ClientCodeCount")){
            columnName = columnName.replace("ClientCodeCount", "Client Code Count");
        }

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
        a.setDuration(
            (int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
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
                    v.getLayoutParams().height =
                        initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(
            (int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    private static boolean isMockSettingsON(Context context) {
        // returns true if mock location enabled, false if not enabled.
        return !Settings.Secure.getString(context.getContentResolver(),
            Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
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
