package com.mosl.shared.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.StringRes;

/**
 * @author Vivek Edited Sasikumar
 */
public class MSFDialog {

    /**
     *
     */
    public static AlertDialog alertDialog(Context context, final int id, String title,
        String message, String buttonText, boolean cancelable, final DialogListener listener) {
        AlertDialog dialog = null;
        try {
            dialog = new AlertDialog.Builder(context).setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setNeutralButton(buttonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (listener != null) listener.alertDialogAction(Action.OK, id);
                    }
                })
                .show();
            dialog.setCanceledOnTouchOutside(cancelable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    /**
     *
     */
    public static AlertDialog alertDialog(Context context, final int id, String title,
        String message, @StringRes int buttonText, boolean cancelable,
        final DialogListener listener) {
        AlertDialog dialog = null;
        try {
            dialog = new AlertDialog.Builder(context).setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setNeutralButton(buttonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (listener != null) listener.alertDialogAction(Action.OK, id);
                    }
                })
                .show();
            dialog.setCanceledOnTouchOutside(cancelable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    /**
     * It prompts a dialog with default "OK" button. There should not be any
     * action on clicking "OK" button.
     */

    public static AlertDialog alertDialogOnly(Context context, String title, String message,
        String buttonText) {
        return alertDialog(context, -1, title, message, buttonText, false, null);
    }

    /**
     * @param message MSF_DIALOG_OK -
     */

    /**
     * @param positiveBtnText Value of the positive button
     * @param negativeBtnText Value of negative button
     */

    public static AlertDialog alertDialog(Context context, final int id, String title,
        String message, String positiveBtnText, String negativeBtnText, boolean cancelable,
        final DialogListener listener) {
        AlertDialog dialog = null;
        try {
            dialog = new AlertDialog.Builder(context).setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (listener != null) listener.alertDialogAction(Action.OK, id);
                    }
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
