package com.mosl.mobile.ekyc.common;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.register.PANAdharRegistActivity;
import com.mosl.mobile.ekyc.register.RegisterActivity;
import com.mosl.mobile.ekyc.ui.CustomDialog;
import com.mosl.mobile.ekyc.ui.EkycMainActivity;
import com.mosl.mobile.ekyc.ui.PostLoginFragment;
import com.mosl.mobile.ekyc.ui.TrackApplicationActivity;
import com.sec.biometric.iris.SecIrisManager;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import static com.mosl.mobile.ekyc.common.EkycAppUtil.getStartDtFinancialYear;
import static com.mosl.mobile.ekyc.model.CommonModel.getAadharNo;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppType;
import static com.mosl.mobile.ekyc.model.CommonModel.getInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsAadharVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsCVLVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsCommoditySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsEquitySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.isAadhaarVerifiedByBio;
import static com.mosl.mobile.ekyc.model.CommonModel.isCommodityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.isCommoditySegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.isEquityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.isEquitySegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.isIPVDone;
import static com.mosl.mobile.ekyc.model.CommonModel.isMfSegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.isOffline;
import static com.mosl.mobile.ekyc.model.CommonModel.isOfflineDocumentUploadedDone;
import static com.mosl.mobile.ekyc.model.CommonModel.isVerificationDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setBankTab;
import static com.mosl.mobile.ekyc.model.CommonModel.setCommoditySegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.setDematTab;
import static com.mosl.mobile.ekyc.model.CommonModel.setEquitySegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.setEsignTab;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.setMfSegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.setPersonalTab;
import static com.mosl.mobile.ekyc.model.CommonModel.setUploadTab;


/**
 * Created by Shefali on 03/08/16.
 */
public class MOSLCommon implements MOSLConstants {
    public static PopupWindow popupWindow;
    public static Dialog dialog;
    static DatePickerDialog fromDatePickerDialog;
    public static String date_1;
    private static ArrayList<String> array = new ArrayList();
    private static ArrayList<String> fatcaArray = new ArrayList<>();
    static boolean isCountryList;

    public static String PP_APP_ID = "RETAILDOM";

    /*public static AlertDialog alertDialog(Context context, final int id, String title, String message, String buttonText, boolean cancelable, final DialogListener listener) {
        AlertDialog dialog = null;
        try {
            dialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message).setCancelable(cancelable).setNeutralButton(buttonText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (listener != null) listener.alertDialogAction(Action.OK, id);

                }
            }).show();
            dialog.setCanceledOnTouchOutside(cancelable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }*/

    public static SharedPreferences getPrefs(Context ctx) {
        return ctx.getSharedPreferences(ctx.getPackageName(), Context.MODE_PRIVATE);
    }

    public static int getAppVersionCode(Context mContext) {
        try {
            PackageInfo pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void alertDInternet(Context context) {

        commonMsgDialog(context, INTERNET_CONNECTION, false, "");
        /*AlertDialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Motilal Oswal");
        builder.setMessage(INTERNET_CONNECTION);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {

                    }
                });
        dialog = builder.show();
        return dialog;*/
    }

    /*public static AlertDialog alertDialog(Context context, final int id, String title, String message, String positiveBtnText, String negativeBtnText, boolean cancelable, final DialogListener listener) {
        AlertDialog dialog = null;
        try {
            dialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message).setCancelable(cancelable).setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (listener != null) listener.alertDialogAction(Action.OK, id);

                }
            }).setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (listener != null) listener.alertDialogAction(Action.CANCEL, id);

                }
            }).show();
            dialog.setCanceledOnTouchOutside(cancelable);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }*/

    public static DatePickerDialog datePickerDialog(Context context) {
        DatePickerDialog pickerDialog = null;
        Calendar newCalendar = Calendar.getInstance();
        final Calendar newDate = Calendar.getInstance();

        pickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDate.set(year, monthOfYear, dayOfMonth);
                final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                // String date = dateFormatter.format(newDate.getTime());
                getDate(year, monthOfYear, dayOfMonth);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        return pickerDialog;
    }

    public static String getDate(int year, int monthOfYear, int dayOfMonth) {
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, monthOfYear, dayOfMonth);
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String date = dateFormatter.format(newDate.getTime());

        return date;
    }


    public static void showToastMsg(Context context, String msg) {

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

    }

    public enum Action {
        OK, CANCEL, NO, OK1
    }

    public static interface DialogListener {
        public void alertDialogAction(Action action, Object... data);

    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i("IP", "***** IP=" + ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("IP", ex.toString());
        }
        return "127.0.0.1";
    }

    public static Dialog showPopUpThreeLine(final Context context, View view, String msg,
                                            final String navigateTo, final Boolean initiateToLead, final String leadID,
                                            final String userID) {
        return showPopUpThreeLine(context, view, msg, navigateTo, initiateToLead, leadID, userID, false, null);
    }

    public static Dialog showPopUpThreeLine(final Context context, View view, String msg,
                                            final String navigateTo, final Boolean initiateToLead, final String leadID,
                                            final String userID, boolean isAddNewProduct,
                                            List secondProductList) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        switch (msg) {
            case MOSLConstants.TERMS_N_COND:
                dialog.setContentView(R.layout.frag_term_and_condition);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                Button btnNext = (Button) dialog.findViewById(R.id.btnClose);

                btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;
            default:
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.three_line_popup);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                Button btnRight = (Button) dialog.findViewById(R.id.btnRight);
                Button btnLeft = (Button) dialog.findViewById(R.id.btnLeft);
                TextView tvHeading = (TextView) dialog.findViewById(R.id.tvHeading);
                ImageView btnClose = (ImageView) dialog.findViewById(R.id.btnClose);
                btnLeft.setText(TRACK_APP);
                btnRight.setText(OK);


                if (navigateTo.equals(MOSLConstants.LOGIN_ACTIVITY) || !initiateToLead || initiateToLead) {
                    btnLeft.setVisibility(View.GONE);
                }

                tvHeading.setText(msg);
                if (msg.equals(context.getResources().getString(R.string.convert_to_ekyc))) {
                    btnLeft.setVisibility(View.GONE);
                }

                if (isAddNewProduct && secondProductList.size() > 0) {
                    btnRight.setText(secondProductList.get(0).toString());
                    btnLeft.setVisibility(View.VISIBLE);
                } else {
                    btnLeft.setVisibility(View.GONE);
                }

                btnLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                showToastMsg(context, "Coming Soon");
                        Intent intent = new Intent(context, TrackApplicationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                });

                btnRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        if (navigateTo.equals(MOSLConstants.LOGIN_ACTIVITY)) {
                            intent = new Intent(context, RegisterActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else if (navigateTo.equals(MOSLConstants.PANADHAR_ACTIVITY)) {
                            if (initiateToLead) {
                                PostLoginFragment postLoginFragment = new PostLoginFragment();
                                postLoginFragment.initiateLeadInCRM(leadID, userID);
                            } else {
                                if (MOSLCommon.isBlank(getAadharNo())) {
                                    intent = new Intent(context, PANAdharRegistActivity.class);
                                    intent.putExtra(SOURCE, ADD_SEGMENT);
                                    context.startActivity(intent);
                                } else {
                                    intent = new Intent(context, EkycMainActivity.class);
                                    intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
                                    context.startActivity(intent);
                                }
                            }

                        } else {
                            dialog.dismiss();
                        }
                        dialog.dismiss();
                    }
                });

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;
        }
        dialog.show();
        return dialog;
    }

   /* public static void showDatePickerDialog(Context context) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        Calendar newCalendar = Calendar.getInstance();
        final Calendar newDate = Calendar.getInstance();

        fromDatePickerDialog = new CustomDatePickerDialog(context, new CustomDatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                newDate.set(year, monthOfYear, dayOfMonth);
                getDate(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();

    }

    public static String getDate(String date) {
        date_1 = date;
        return date;
    }*/

    public static boolean isNetworkEnabled(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
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

    public static void removeKeyboard(Context context, View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*public static String getDeviceID(Context context, ContentResolver contentResolver) {
        String deviceId;
        UUID deviceUuid;
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String tmDevice = null, tmSerial = null, androidId = null;
        try {
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(contentResolver, android.provider.Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isBlank(tmDevice)) {
            deviceUuid = new UUID(androidId.hashCode(), tmSerial.hashCode());
        } else {
            deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        }
        deviceId = deviceUuid.toString();
        return deviceId;
    }*/

    public static String getDeviceID(Context context, ContentResolver contentResolver) {
        String deviceId;
        deviceId = "" + android.provider.Settings.Secure.getString(contentResolver, android.provider.Settings.Secure.ANDROID_ID);
        return deviceId;
    }

    public static void downloadPDF(String resumeLink, Context context) {
        try {
            String nameOfFile = URLUtil.guessFileName(String.valueOf(resumeLink), null,
                    MimeTypeMap.getFileExtensionFromUrl(String.valueOf(resumeLink)));
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(resumeLink));
            request.setTitle(nameOfFile);
            request.setDescription("download");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameOfFile);
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);


        } catch (Exception ex) {
//            MOSLCommon.alertDialog(context, 0, "Motilal Oswal", "Some error occurred during download", "OK", true, null);
            CustomDialog cdException = new CustomDialog(context, SOME_ERROR_OCCURED_DURING_DOWNLOAD, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                @Override
                public void dialogAction(Action action, Object... data) {

                }
            });
            cdException.show();
        }

    }

    public static boolean fundTransferStatus() {
        if (getIsEquitySelected() && getIsCommoditySelected()) {
            if (isEquityFundTransfered() && isCommodityFundTransfered()) {
                return true;
            }
        } else if (getIsEquitySelected() && isEquityFundTransfered()) {
            return true;
        } else if (getIsCommoditySelected() && isCommodityFundTransfered()) {
            return true;
        }
        return false;
    }

    //set Listview height
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(0, 0);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void setPermisson(Activity activity, Context context) {
        try {
            ArrayList<String> permissionArrayList = new ArrayList();

            if (getAppType().equals(DIRECT) || getAppType().equals(PP)) {
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    permissionArrayList.add(android.Manifest.permission.RECORD_AUDIO);
                    //activity.requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO}, 100);
                    // return;
                }
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
                    permissionArrayList.add(android.Manifest.permission.MODIFY_AUDIO_SETTINGS);
                }
            }
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissionArrayList.add(android.Manifest.permission.CAMERA);
            }

            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissionArrayList.add(android.Manifest.permission.READ_PHONE_STATE);
            }
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionArrayList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionArrayList.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                permissionArrayList.add(android.Manifest.permission.INTERNET);
            }
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissionArrayList.add(android.Manifest.permission.ACCESS_WIFI_STATE);
            }
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissionArrayList.add(android.Manifest.permission.ACCESS_NETWORK_STATE);
            }
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
                permissionArrayList.add(android.Manifest.permission.MODIFY_AUDIO_SETTINGS);
            }

            String[] permissionString = new String[permissionArrayList.size()];
            permissionString = permissionArrayList.toArray(permissionString);
            if (permissionString.length != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.requestPermissions(permissionString, 100);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void commonMsgDialog(Context context, String msg, boolean showHeading, String headingMsg) {
        final CustomDialog customDialog = new CustomDialog(context, msg, true, false, showHeading, headingMsg, OK, "", new CustomDialog.CustomDialogListener() {
            @Override
            public void dialogAction(MOSLCommon.Action action, Object... data) {

            }
        });

        try {
            if (!((Activity) context).isFinishing()) {
                customDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * return country list
     *
     * @param mContext
     * @return
     */
    public static ArrayList loadCountryArray(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(BINDCOUNTRY, 0);
        int countrySize = prefs.getInt(BINDCOUNTRY + "_size", 0);
        if (countrySize != 0) {
            array.clear();
            for (int i = 0; i < countrySize; i++) {
                String country = prefs.getString(BINDCOUNTRY + "_" + i, null).toString();


                array.add(country);
            }

        }
        return array;

//        return loadCountryArray(mContext,false);
    }

    /**
     * return country list
     *
     * @param mContext
     * @param skipIndia: true: exclude india from country list
     * @return
     */
    public static ArrayList loadCountryArray(Context mContext, boolean skipIndia) {

        SharedPreferences prefs = mContext.getSharedPreferences(BINDCOUNTRY, 0);
        int countrySize = prefs.getInt(BINDCOUNTRY + "_size", 0);
        if (countrySize != 0) {
            fatcaArray.clear();
            for (int i = 0; i < countrySize; i++) {
                String country = prefs.getString(BINDCOUNTRY + "_" + i, null).toString();

                if (skipIndia) {
                    if (!country.equalsIgnoreCase("india")) {
                        fatcaArray.add(country);
                    }
                } else {
                    fatcaArray.add(country);
                }
            }
        }
        return fatcaArray;
    }

    public static void callApplyNowActivity(Context context) {
        Intent intent = new Intent(context, EkycMainActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public static boolean isIrisSupported(Context context) {
        String deviceName = android.os.Build.MODEL;
//        if (Utility.isSupported(deviceName)) {
        try {
            SecIrisManager.getInstance();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
//                commonMsgDialog(context, "Biometric SDK feature not supported", false, "");
            return false;
        }

//        } else {
//            commonMsgDialog(context, "Biometric SDK feature not supported in " + deviceName + " Device", false, "");
//            return false;
//        }
    }

    //Disable field
    public static void disableField(ViewGroup layout) {
        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                disableField((ViewGroup) child);
            } else {
                child.setEnabled(false);
            }
        }
    }

    //Enable field
    public static void enableField(ViewGroup layout) {
        layout.setEnabled(true);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                enableField((ViewGroup) child);
            } else {
                child.setEnabled(true);
            }
        }
    }

    public static boolean isFutureDate(String NWdate) {
        Date date = EkycAppUtil.getDate(NWdate, "dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        // and get that as a Date
        Date today = c.getTime();

        return date.after(today);
    }

    public static boolean isFinacialDate(String NWdate) {
        String financial = getStartDtFinancialYear("dd/MM/yyyy");//get Financial date
        Date financialDate = EkycAppUtil.getDate(financial, "dd/MM/yyyy");//financial date
        Date date = EkycAppUtil.getDate(NWdate, "dd/MM/yyyy");
        return date.before(financialDate);
    }

    public static boolean isDateIWthinYear(String NWDate) {
        Calendar newDate = Calendar.getInstance();
        Date dob = EkycAppUtil.getDate(NWDate, "dd/MM/yyyy");
        newDate.set(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));
        Date date = newDate.getTime();


        // current date before 12 months
        Calendar currentDateBefore12Months = Calendar.getInstance();
//        currentDateBefore12Months.add(Calendar.MONTH, -12);
        currentDateBefore12Months.add(Calendar.DATE, -366);
        return date.after(currentDateBefore12Months.getTime());
    }

    //Return nominee is applicable or not
    public static boolean isNomineeTabApplicable() {
        if (getAppType().equalsIgnoreCase(DIRECT) && getIsAadharVerified() && !getInterestedInMF()) {
            return false;
        } else {
            return true;
        }
    }

    public static void setTabConstant() {
        if (isNomineeTabApplicable()) {
            setPersonalTab(0);
            setDematTab(1);
            setBankTab(2);
            setUploadTab(3);
            setEsignTab(4);
        } else if (!isNomineeTabApplicable()) {
            setPersonalTab(0);
//            setDematTab(1);
            setBankTab(1);
            setUploadTab(2);
            setEsignTab(3);
        }
    }

    public static void setExistingSegment(String segment) {
        if (segment.equalsIgnoreCase(EQUITY) || segment.equalsIgnoreCase(PMS)) {
            setEquitySegmentExist(true);
        }
        if (segment.equalsIgnoreCase(COMMODITY)) {
            setCommoditySegmentExist(true);
        }
        if (segment.equalsIgnoreCase(MF)) {
            setMfSegmentExist(true);
            setIsInterestedInMF(false);
        }
    }

    /*
    * In prospect if client code is created, we are setting exist flag of respective segment to true, which is checked in isSegmentExist()*/
    public static boolean isSegmentExist(String segment) {
        if (segment.equalsIgnoreCase(EQUITY) || segment.equalsIgnoreCase(PMS)) {
            return isEquitySegmentExist();
        } else if (segment.equalsIgnoreCase(COMMODITY)) {
            return isCommoditySegmentExist();
        } else if (segment.equalsIgnoreCase(MF)) {
            return isMfSegmentExist();
        }
        return false;
    }

    public static boolean isThisSecondProductFlow() {
        return isEquitySegmentExist() || isCommoditySegmentExist() || isMfSegmentExist();
    }

    //Return IPV status for offline Or online
    public static boolean isIpvORDocumentStageDone() {
        return isOffline() ? isVerificationDone() || isOfflineDocumentUploadedDone() : isVerificationDone() || isIPVDone();
    }

	/*
	 * Return Minimum value for margin deposit in case of other amount option in scheme
	 * for direct 25000
	 * for branch 50000
	 * */
	public static int minMarginValueForOtherAmount() {
		//return getAppType().equalsIgnoreCase(DIRECT) ? 25000 : 50000;
		return getAppType().equalsIgnoreCase(DIRECT) ? 1000 : 50000;
	}

    public static boolean isBrokingFundTransferDone(String segment) {
        if (segment.equalsIgnoreCase(EQUITY) || segment.equalsIgnoreCase(PMS)) {
            return isEquityFundTransfered();
        } else if (segment.equalsIgnoreCase(COMMODITY)) {
            return isCommodityFundTransfered();
        }
        return false;
    }

    public static boolean isVideoVerificationRequired() {
        if (getIsCVLVerified())
            return false;
        else if (!getIsCVLVerified() && isAadhaarVerifiedByBio())
            return false;
        return true;
    }
	
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String removeExponential(String data) {
        if (isBlank(data)) {
            return data;
        }
        BigDecimal bd = new BigDecimal(data);
        return String.valueOf(bd.longValue());
    }
}
