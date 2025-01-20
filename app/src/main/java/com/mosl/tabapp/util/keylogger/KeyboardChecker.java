package com.mosl.tabapp.util.keylogger;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.provider.Settings;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import java.util.List;
public class KeyboardChecker {
    public static boolean isThirdPartyKeyboardEnabled(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> inputMethodList = imm.getEnabledInputMethodList();
        for (InputMethodInfo inputMethodInfo : inputMethodList) {
            String packageName = inputMethodInfo.getPackageName();
            // Check if the package name matches known system keyboards
            if (!isSystemKeyboard(packageName)) {
                return true; // Found a third-party keyboard
            }
        }
        return false; // Only system keyboards are enabled
    }
    private static boolean isSystemKeyboard(String packageName) {
        // List of known system keyboard package names (may vary by device)
        return packageName.equals("com.google.android.inputmethod.latin") || // Gboard
                packageName.equals("com.samsung.android.inputmethod") || // Samsung Keyboard
                packageName.equals("com.android.inputmethodkeyboard"); // AOSP Keyboard
        // Add more as needed
    }
    public static boolean isUsingCustomInputMethod(Context ctx) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> mInputMethodProperties =
                imm.getEnabledInputMethodList();
        final int N = mInputMethodProperties.size();
        for (int i = 0; i < N; i++) {
            InputMethodInfo imi = mInputMethodProperties.get(i);
            if (imi.getId().equals(
                    Settings.Secure.getString(ctx.getContentResolver(),
                            Settings.Secure.DEFAULT_INPUT_METHOD))) {
                if ((imi.getServiceInfo().applicationInfo.flags &
                        ApplicationInfo.FLAG_SYSTEM) == 0) {
                    return true;
                }
            }
        }
        return false;
    }
}