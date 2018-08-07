package com.mosl.mobile.biometric.samsung;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import timber.log.Timber;

/**
 * Created by lalkishore on 8/24/2016.
 */
public class Utility {
    private static HashMap<String, Boolean> mSupportedDeviceList;

    static {
        mSupportedDeviceList = new HashMap<>();
        mSupportedDeviceList.put("SM-T116IR", true);
    }

    public static boolean isSupported(String devName) {
        return mSupportedDeviceList.get(devName) != null;
    }

    public static boolean networkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static Properties loadPreferences(Context context, String prefsFile) {
        InputStream is = null;
        Properties prefs = null;
        try {
            is = context.getAssets().open(prefsFile);
            if (is != null) {
                prefs = new Properties();
                prefs.load(is);
            }
        } catch (IOException ex) {
            Timber.d("Error accessing preferences file");
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                Timber.d("Error closing preferences file");
            }
        }
        return prefs;
    }

    public static byte[] getPIDXML(String ts) {
        Timber.d(" :: getPIDXML | ENTER");
        String pid = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<ns2:Pid xmlns:ns2=\"http://www.uidai.gov.in/authentication/uid-auth-request-data/1.0\" " +
                "ts=\"" + ts + "\" >" +
                "<Demo>" + "<Pi " + "name=\"" + "" + "\"/>" + "</Demo>" + "<Bios>" + "</Bios>" + "</ns2:Pid>";
        byte[] piddata = pid.getBytes();
        Timber.d(" :: pidxml is: %s", pid);
        return piddata;
    }

    public static boolean hasPermission(Context context) {
        try {
            String BIOMETRIC_LICENSE_PERMISSION = "com.sec.enterprise.biometric.permission.IRIS_RECOGNITION";
            PackageManager packageManager = context.getApplicationContext().getPackageManager();
            if (packageManager.checkPermission(BIOMETRIC_LICENSE_PERMISSION, context.getApplicationContext().getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                Timber.d(">>> hasPermission : true");
                return true;
            }
        } catch (Exception e) {
        }
        Timber.d(">>> hasPermission : false");
        return false;
    }
}
