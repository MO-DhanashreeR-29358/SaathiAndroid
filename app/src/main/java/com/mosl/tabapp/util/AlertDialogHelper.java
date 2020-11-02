package com.mosl.tabapp.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.mosl.tabapp.R;

/**
 * Created by snehaganapuram on 06-09-2017.
 */

public class AlertDialogHelper {
    public static AlertDialog alertDialogOnly(Context context, String title, String message) {
        return alertDialogOnly(context, title, message,
            context.getString(R.string.dialog_ok));
    }

    public static AlertDialog alertDialogOnly(Context context, String title, String message,
        String buttonText) {
        return alertDialog(context, -1, title, message, buttonText, false, null);
    }

    public static AlertDialog alertDialog(Context context, final int id, String title,
        String message, String buttonText, boolean cancelable, final DialogListener listener) {
        AlertDialog dialog = null;
        try {
            dialog = new AlertDialog.Builder(context).setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setNeutralButton(buttonText, (dialog1, whichButton) -> {
                    if (listener != null) listener.alertDialogAction(Action.OK, id);
                })
                .show();
            dialog.setCanceledOnTouchOutside(cancelable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    public static AlertDialog alertDialog(Context context, final int id, String title,
        String message, String positiveBtnText, String negativeBtnText, boolean cancelable,
        final DialogListener listener) {
        AlertDialog dialog = null;
        try {
            dialog = new AlertDialog.Builder(context).setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton(positiveBtnText, (dialog1, whichButton) -> {
                    if (listener != null) listener.alertDialogAction(Action.OK, id);
                })
                .setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (listener != null) listener.alertDialogAction(Action.CANCEL, id);
                    }
                })
                .show();
            dialog.setCanceledOnTouchOutside(cancelable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }
    public enum Action {
        OK, CANCEL
    }

    public interface DialogListener {
        void alertDialogAction(Action action, Object... data);
    }
}
