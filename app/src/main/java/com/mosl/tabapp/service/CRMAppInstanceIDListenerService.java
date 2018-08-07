package com.mosl.tabapp.service;

/**
 * Created by pramod on 04/05/17.
 */

import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.mosl.tabapp.ui.MOSLCommonActivity;

import static com.mosl.tabapp.common.CRMAppUtil.getPrefs;

public class CRMAppInstanceIDListenerService extends FirebaseInstanceIdService {
    private String TAG = CRMAppInstanceIDListenerService.class.getCanonicalName();

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is also called
     * when the InstanceID token is initially generated, so this is where
     * you retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        try {
            // Get updated InstanceID token.
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();

            SharedPreferences.Editor editor = getPrefs(this).edit();
            editor.putString("TOKEN_ID", refreshedToken);
            editor.apply();

            Looper.prepare();
            MOSLCommonActivity.sendRegistrationToServer(this, getContentResolver(), false);
            Log.d(TAG, "New firebase token: " + refreshedToken);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
