package com.mosl.tabapp.service;

/**
 * Created by pramod on 04/05/17.
 */

import android.content.SharedPreferences;
import android.os.Looper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.mosl.tabapp.ui.MOSLCommonActivity;
import timber.log.Timber;

import static com.mosl.tabapp.common.CRMAppUtil.getPrefs;

public class CRMAppInstanceIDListenerService extends FirebaseInstanceIdService {
	private String TAG = CRMAppInstanceIDListenerService.class.getCanonicalName();

	@Override
	public void onTokenRefresh() {
		try {
			String refreshedToken = FirebaseInstanceId.getInstance().getToken();

			SharedPreferences.Editor editor = getPrefs(this).edit();
			editor.putString("TOKEN_ID", refreshedToken);
			editor.apply();

			Looper.prepare();
			MOSLCommonActivity.sendRegistrationToServer(this, this.getContentResolver(), false);
			Timber.tag(TAG).d("New firebase token: " + refreshedToken);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
