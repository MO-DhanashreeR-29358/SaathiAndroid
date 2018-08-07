package com.mosl.mobile.biometric.samsung.uidaiauthentication;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.sec.biometric.license.SecBiometricLicenseManager;

public class Activation extends Activity {

	final static String TAG = "Activation";

	Context mContext;
	Button ActivateBtn;
    SecBiometricLicenseManager mLicenseMgr;
	
	

	BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	System.out.println("Received " + intent.getAction());
			String action = intent.getAction();
			if (action.equals(SecBiometricLicenseManager.ACTION_LICENSE_STATUS)) {
				unregisterReceiver(mReceiver);

				Bundle extras = intent.getExtras();
				String status = extras.getString(SecBiometricLicenseManager.EXTRA_LICENSE_STATUS);
				int err_code = extras.getInt(SecBiometricLicenseManager.EXTRA_LICENSE_ERROR_CODE);
				
				if (status.equals("success")) {
					Toast toast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
					toast.setText("status = " + status + " , " + " License activated");
					toast.show();
					finish();
				} else {
					Toast toast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
					toast.setText("status = " + status + " , " + "error code = " + err_code);
					toast.show();
					ActivateBtn.setEnabled(true);
				}
			}
			
         }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, ">>> himanshu onCreate");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activation);
		mContext = this.getApplicationContext();
        mLicenseMgr = SecBiometricLicenseManager.getInstance(mContext);
	    ActivateBtn = (Button) findViewById(R.id.activate_license);
        ActivateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
            	ActivateBtn.setEnabled(false);
    			if (!activateIrisLicense()) {
    				unregisterReceiver(mReceiver);
    				finish();
    			}
            }
        });
    }
	
	protected Boolean activateIrisLicense() {
		try {
			IntentFilter filter = new IntentFilter();
			filter.addAction(SecBiometricLicenseManager.ACTION_LICENSE_STATUS);
			filter.addAction(SecBiometricLicenseManager.ACTION_LICENSE_STATUS);
			registerReceiver(mReceiver, filter);
			
		//	String key = "0756C05CF8C0AB3C17B0D847549320EB262EA653051E0D0C1ABF4C95D22CC39C2507769AE2BE2DC92819426119EF0ED90D76EB0BF5D54E621D5900456D6835AC";
			String packageName = getApplicationContext().getPackageName();
			Log.i("test ", "packageName: "+packageName);
			mLicenseMgr.activateLicense(MOSLConstants.KEY,packageName);
			return true;
		} catch(Exception e) {
		    e.printStackTrace();
		}

		Toast toast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
		toast.setText("status = fail , error code = unknown error");
		toast.show();

		return false;
	}
	


}
