package com.mosl.tabapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


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
