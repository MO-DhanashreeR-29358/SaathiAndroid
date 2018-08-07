package com.mosl.mobile.biometric.samsung.uidaiauthentication;

import android.util.Log;

public class Print {

    private static final String TAG = "IrisSampleApp";
    private static final boolean DEBUG = true;

    public static void e(String msg) {
        StackTraceElement ste;
        try {
            ste = new Throwable().getStackTrace()[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e(TAG, msg);
            return;
        }
        String caller = ste.getFileName().replace(".java", " ") + ":: " + ste.getMethodName() + " - ";
        Log.e(TAG, caller + msg);
    }

    public static void d(String msg) {
        if (!DEBUG)
            return;
        StackTraceElement ste;
        try {
            ste = new Throwable().getStackTrace()[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.d(TAG, msg);
            return;
        }
        String caller = ste.getFileName().replace(".java", " ") + ":: " + ste.getMethodName() + " - ";
        Log.d(TAG, caller + msg);
    }

    public static void i(String msg) {
        if (!DEBUG)
            return;
        StackTraceElement ste;
        try {
            ste = new Throwable().getStackTrace()[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.i(TAG, msg);
            return;
        }
        String caller = ste.getFileName().replace(".java", " ") + ":: " + ste.getMethodName() + " - ";
        Log.i(TAG, caller + msg);
    }

    public static void s(String msg) {
        if (!DEBUG)
            return;
        StackTraceElement ste;
        try {
            ste = new Throwable().getStackTrace()[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.d(TAG, msg);
            return;
        }
        String caller = ste.getFileName().replace(".java", " ") + ":: " + ste.getMethodName() + " - ";
        Log.d(TAG, caller + msg);
    }
}

