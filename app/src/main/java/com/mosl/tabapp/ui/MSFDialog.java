package com.mosl.tabapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.mosl.tabapp.R;


/**
 * Created by Shefali on 1/1/2016.
 */
public class MSFDialog {


    public static AlertDialog alertDialog(Context context, final int id, String title, String message, String buttonText, boolean cancelable, final DialogListener listener) {
        AlertDialog dialog = null;
        try {
            dialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message).setCancelable(cancelable).setNeutralButton(buttonText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (listener != null) listener.alertDialogAction(Action.OK, id);

                }
            }).show();
            dialog.setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    /**
     * It prompts a dialog with default "OK" button. There should not be any
     * action on clicking "OK" button.
     *
     * @param title
     * @param message
     */

    public static AlertDialog alertDialogOnly(Context context, String title, String message, String buttonText) {
        return alertDialog(context, -1, title, message, buttonText, false, null);
    }

    /**
     * @param context
     * @param title
     * @param message MSF_DIALOG_OK -
     */
    public static AlertDialog alertDialogOnly(Context context, String title, String message) {
        return alertDialogOnly(context, title, message, context.getResources().getString(R.string.MSF_DIALOG_OK));
    }

    public static AlertDialog alertDialogWithoutTitle(Context context, String message, String neutralButton) {
        return alertDialogOnly(context, null, message, neutralButton);
    }

    public static AlertDialog alertDialogSingleChoiceItem(Context context, final int id, String title, CharSequence items[], final DialogListener listener) {
        AlertDialog dialog = null;
        try {
            dialog = new AlertDialog.Builder(context).setTitle(title).setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    if (listener != null) listener.alertDialogAction(Action.OK, id, item);
                }
            }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    /**
     * On clicking positive-button {@link alertDialogPositive } and on
     * negative-button {@link alertDialogPositive} will be triggered
     *
     * @param title
     * @param message
     * @param positiveBtnText Value of the positive button
     * @param negativeBtnText Value of negative button
     */

    public static AlertDialog alertDialog(Context context, final int id, String title, String message, String positiveBtnText, String negativeBtnText, boolean cancelable, final DialogListener listener) {
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
    }

    public enum Action {
        OK, CANCEL
    }

    public static interface DialogListener {
        public void alertDialogAction(Action action, Object... data);

    }

}
