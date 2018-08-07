package com.mosl.tabapp.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by pramod on 15/02/17.
 */

public class NetworkStateReceiver extends BroadcastReceiver {
    private String TAG = getClass().getCanonicalName();

    public void onReceive(Context context, Intent intent) {
        Log.d("app","Network connectivity change");

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            try {
                Toast.makeText(context, "Network is connected", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Network is connected");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "Network is changed or reconnected", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Network is changed or reconnected");
        }
    }
}