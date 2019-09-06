package com.mosl.mobile.biometric.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.biometric.mantra.rdsample.model.Opts;
import com.mosl.mobile.biometric.mantra.rdsample.model.PidOptions;
import com.mosl.mobile.biometric.model.AuthRequest;
import com.mosl.mobile.biometric.samsung.uidaiauthentication.Activation;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import com.mosl.mobile.ekyc.ui.CustomDialog;

import org.json.JSONArray;
import org.json.JSONObject;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import timber.log.Timber;

import static com.mosl.mobile.ekyc.common.MOSLCommon.commonMsgDialog;
import static com.mosl.mobile.ekyc.common.MOSLCommon.getDeviceID;
import static com.mosl.mobile.ekyc.common.MOSLCommon.getLocalIpAddress;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isIrisSupported;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.model.CommonModel.getAadharNo;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppType;
import static com.mosl.mobile.ekyc.model.CommonModel.getCity;
import static com.mosl.mobile.ekyc.model.CommonModel.getClientName;
import static com.mosl.mobile.ekyc.model.CommonModel.getInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsAadharVerified;
import static com.mosl.mobile.ekyc.register.PANAdharRegistActivity.PAN_AADHAAR_BIOMETRIC_RESULT_OK;
import static com.mosl.mobile.ekyc.ui.EsignFragment.ESIGN_BIOMETRIC_RESULT_OK;

/**
 * Created by pramod on 27/07/17.
 */

public class BioMetricMainActivity extends Activity implements MOSLConstants {
    private String TAG = BioMetricMainActivity.class.getCanonicalName();

    private static final String MOSLHOST = "motilaloswal.com";
    private boolean fromURI = false;
    private AuthRequest requestData;

    private byte[] isoTemplate;

    CoordinatorLayout coordinator;
    RadioGroup authTypeToggle;
    RadioButton rbFingerPrint;
    RadioButton rbIris;
    LinearLayout fingerLayout;
    ImageView imgFinger, btnClose;
    Button scanFinger;
    Button validateFingerPrint;
    Dialog dialog;
    /**
     * IRIS related view objects
     */
    LinearLayout irisLayout;
    //SurfaceView irisPreview;
    CheckBox singleEye;
    public final int UID_OP_AUTH = 1;

    /**
     * User visible message and progress.
     */

    LinearLayout customProgress;
    TextView userMessage;

    Bundle bundle;
    int source;
    String esignErrorMsg = "", eSignPDFURL = "", IRISWADHValue, BIOWADHValue;
    Button AuthBtn, resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.biometric_activity_main);
            setPermisson(BioMetricMainActivity.this, getApplicationContext());
            bundle = getIntent().getExtras();
            if (bundle != null) {
                source = bundle.getInt(SOURCE);
            }
            setupView();

            rbFingerPrint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fingerLayout.setVisibility(View.VISIBLE);
                    irisLayout.setVisibility(View.GONE);
                    showMessageNProgress("");
                }
            });
            rbIris.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    irisLayout.setVisibility(View.VISIBLE);
                    fingerLayout.setVisibility(View.GONE);
                    AuthBtn.setEnabled(true);

                    if (!hasIRISPermission()) {
                        startActivityForResult(new Intent(BioMetricMainActivity.this, Activation.class), 999);
                    }
                }
            });

            singleEye = (CheckBox) findViewById(R.id.singleEye);

            AuthBtn = (Button) findViewById(R.id.AuthBtn);
            AuthBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    try {
                        int uidOp = UID_OP_AUTH;

                        // Get the package manager
                        PackageManager packageManager = getPackageManager();
                        // Get activities that can handle the intent
                        Intent act = new Intent("in.gov.uidai.rdservice.iris.CAPTURE");
                        List<ResolveInfo> activities = packageManager.queryIntentActivities(act, 0);
                        if (activities == null || activities.size() == 0) {
                            commonMsgDialog(BioMetricMainActivity.this, getResources().getString(R.string.update_tab_for_iris), false, "");
                            return;
                        }
                        for (ResolveInfo activity : activities) {
                            Log.d(TAG, "setOnClickListener: " + activity.activityInfo.packageName + " : " + activity.activityInfo.name);
                        }
                        String pkgName = activities.get(0).activityInfo.packageName; //MAKE SURE YOU HAVE SLECTED SAMSUNG RD SERVICE
                        String activity = activities.get(0).activityInfo.name;
                        Log.d(TAG, "setOnClickListener " + pkgName + " : " + activity);
                        act.setClassName(pkgName, activity);
                        act.putExtra("PID_OPTIONS", getIRISPIDOptions());
                        startActivityForResult(act, 2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            AuthBtn.setEnabled(false);
            getDataFromURI(getIntent());
            loadWadhValues();
        } catch (Throwable e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
//            Crashlytics.logException(e);
        }
    }

    private void setupView() {
        coordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);
        authTypeToggle = (RadioGroup) findViewById(R.id.authTypeToggle);
        rbFingerPrint = (RadioButton) findViewById(R.id.rbFingerPrint);
        rbIris = (RadioButton) findViewById(R.id.rbIris);

        fingerLayout = (LinearLayout) findViewById(R.id.fpLayout);
        imgFinger = (ImageView) findViewById(R.id.finger_image);
        btnClose = (ImageView) findViewById(R.id.btnClose);
        scanFinger = (Button) findViewById(R.id.button_scan_finger);

        customProgress = (LinearLayout) findViewById(R.id.customProgress);

        scanFinger.setEnabled(true);
        validateFingerPrint = (Button) findViewById(R.id.button_validate);
        validateFingerPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestData == null) {
                    commonMsgDialog(BioMetricMainActivity.this, "Scan data not found", false, "");
                } else {
                    validateUser(requestData);
                    validateFingerPrint.setEnabled(false);
                }
            }
        });
        scanFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeAndCaptureFingerScan();
            }
        });
        /**
         * IRIS related view objects
         */
        irisLayout = (LinearLayout) findViewById(R.id.irisLayout);
        //irisPreview = (SurfaceView) findViewById(R.id.preview);
        singleEye = (CheckBox) findViewById(R.id.singleEye);
        resetBtn = (Button) findViewById(R.id.button_reset);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });
        AuthBtn = (Button) findViewById(R.id.AuthBtn);
        AuthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //authenticateUser(); TODO
            }
        });

        userMessage = (TextView) findViewById(R.id.userMessage);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MOSLCommon.removeKeyboard(BioMetricMainActivity.this, btnClose);
                finish();
            }
        });

        if (!isIrisSupported(BioMetricMainActivity.this)) {
            rbIris.setVisibility(View.GONE);
        } else if (!bundle.getBoolean(FLAG)) {
            rbIris.setVisibility(View.GONE);
        } else {
            rbIris.setVisibility(View.VISIBLE);
        }
    }

    public void showMessageNProgress(String message) {
        showMessageNProgress(message, false);
    }

    public void showMessageNProgress(final String message, final boolean show) {
        Timber.i(message);
        userMessage.post(new Runnable() {
            @Override
            public void run() {
                userMessage.setText(message);
                customProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void getDataFromURI(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            Timber.d("Data : %s", data.toString());
            String host = data.getHost();
            List<String> params = data.getPathSegments();
            if (MOSLHOST.equals(host) && params.size() == 3) {
                fromURI = true;
                if (params.get(0).equalsIgnoreCase("iris")) {
                    rbIris.setChecked(true);
                    rbFingerPrint.setVisibility(View.GONE);
                    fingerLayout.setVisibility(View.GONE);
                } else {
                    rbFingerPrint.setChecked(true);
                    rbIris.setVisibility(View.GONE);
                    irisLayout.setVisibility(View.GONE);
                }
                scanFinger.setEnabled(true);
            }
        }
    }

    public void resetFields() {
        if (fromURI) {
            userMessage.setText("");
            customProgress.setVisibility(View.GONE);
            imgFinger.setImageDrawable(null);
            isoTemplate = null;
        } else {
            userMessage.setText("");
            customProgress.setVisibility(View.GONE);
            imgFinger.setImageDrawable(null);
            isoTemplate = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: { //finger scan result
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        if (data != null) {
                            String result = data.getStringExtra("PID_DATA");

                            if (result != null) {
                                boolean isDeviceReady = isFingerScanDeviceReady(result, 1);
                                Log.d("Finger Scan PID_DATA: ", result);

                                if (isDeviceReady) {
                                    validateFingerPrint.setEnabled(true);
                                    result = result.replaceAll("\"", "@@");
                                    requestData = new AuthRequest();
                                    requestData.setUserId(getUserID());
                                    requestData.setAadharNumber(getAadharNo());
                                    requestData.setAuthMode(rbIris.isChecked());
                                    requestData.setPidData(result);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Error", "Error while deserialize pid data", e);
                    }
                }
                break;
            }
            case (2): {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        if (data != null) {
                            String result = data.getStringExtra("PID_DATA");
                            if (result != null) {
                                boolean isDeviceReady = isFingerScanDeviceReady(result, 2);
                                Log.d("Iris Scan PID_DATA: ", result);
                                if (isDeviceReady) {
                                    validateFingerPrint.setEnabled(true);
                                    result = result.replaceAll("\"", "@@");
                                    AuthRequest requestIrisData = new AuthRequest();
                                    requestIrisData.setUserId(getUserID());
                                    requestIrisData.setAadharNumber(getAadharNo());
                                    requestIrisData.setAuthMode(rbIris.isChecked());
                                    requestIrisData.setPidData(result);
                                    validateUser(requestIrisData);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Error", "Error while deserialize pid data", e);
                    }
                    /*String piddata = data
                            .getStringExtra("PID_DATA");
                    Log.d("IndiaIdentityClient", "capture data:"+piddata);*/
                }
                break;
            }
            case (999): { //IRIS Permission
                if (!hasIRISPermission()) {
                    finish();
                }
            }
        }
    }

    private String getMantraPIDOptions() {
        try {
            ArrayList<String> positions = new ArrayList<>();
            int fingerCount = 1;//spinnerTotalFingerCount.getSelectedItemPosition() + 1;
            int fingerType = 0;//spinnerTotalFingerType.getSelectedItemPosition();
            int fingerFormat = 0;//spinnerTotalFingerFormat.getSelectedItemPosition();
            String pidVer = "2.0"; //edtxPidVer.getText().toString();
            String timeOut = "10000"; //edtxTimeOut.getText().toString();
            String posh = "UNKNOWN";
            if (positions.size() > 0) {
                posh = positions.toString().replace("[", "").replace("]", "").replaceAll("[\\s+]", "");
            }

            Opts opts = new Opts();
            opts.fCount = String.valueOf(fingerCount);
            opts.fType = String.valueOf(fingerType);
            opts.iCount = "0";
            opts.iType = "0";
            opts.pCount = "0";
            opts.pType = "0";
            opts.format = String.valueOf(fingerFormat);
            opts.pidVer = pidVer;
            opts.timeout = timeOut;
            opts.wadh = BIOWADHValue;
//            opts.wadh = "hwFhpgsNNAo2+6SNkpA9IWHqOINAV3CeE7ZBDumdNVM=";
            opts.posh = posh;
            String env = WebServiceHandler.UID_ENV;

            opts.env = env;

            PidOptions pidOptions = new PidOptions();
            pidOptions.ver = pidVer;
            pidOptions.Opts = opts;

            Serializer serializer = new Persister();
            StringWriter writer = new StringWriter();
            serializer.write(pidOptions, writer);
            return writer.toString();
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
        return null;
    }

    /**
     * finger scan
     */
    private void initializeAndCaptureFingerScan() {
        try {
            String pidOption = getMantraPIDOptions();

            if (pidOption != null) {
                Intent intent = new Intent();
                intent.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                intent.putExtra("PID_OPTIONS", pidOption);
                startActivityForResult(intent, 1);
            }
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            if (getAppType().equalsIgnoreCase(CRM)) {
                CustomDialog dialogMsg = new CustomDialog(BioMetricMainActivity.this, getResources().getString(R.string.update_tab_for_finger_esales), false, false, false, "", "Download", "Cancel", new CustomDialog.CustomDialogListener() {
                    @Override
                    public void dialogAction(MOSLCommon.Action action, Object... data) {
                        if (action == MOSLCommon.Action.OK) {
                            finish();
                        } else if (action == MOSLCommon.Action.CANCEL) {
                            finish();
                        }
                    }
                });
                dialogMsg.show();
            } else {
                CustomDialog dialogMsg = new CustomDialog(BioMetricMainActivity.this, getResources().getString(R.string.update_tab_for_finger), true, false, false, "", "Download", "Cancel", new CustomDialog.CustomDialogListener() {
                    @Override
                    public void dialogAction(MOSLCommon.Action action, Object... data) {
                        if (action == MOSLCommon.Action.OK) {
                            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.mantra.clientmanagement"));
                            startActivity(i);
                            finish();
                        } else if (action == MOSLCommon.Action.CANCEL) {
                            finish();
                        }
                    }
                });
                dialogMsg.show();
            }
//            commonMsgDialog(BioMetricMainActivity.this, getResources().getString(R.string.update_tab_for_finger), false, "");
//            commonMsgDialog(BioMetricMainActivity.this, "Install RDSERVICE & MANAGEMENT-CLIENT apps from playstore and retry", false, "");
            // https://play.google.com/store/apps/details?id=com.mantra.rdservice
            //https://play.google.com/store/apps/details?id=com.mantra.clientmanagement
        } catch (Exception e) {
            e.printStackTrace();
            commonMsgDialog(BioMetricMainActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
        }
    }

    private boolean isFingerScanDeviceReady(String xmlInput, int irisOrFinger) {
        try {
            InputStream stream = new ByteArrayInputStream(xmlInput.getBytes("UTF-8"));
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();
            myParser.setInput(stream, null);

            int event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.END_TAG:
                        if (name.equals("Resp")) {
                            String errCode = myParser.getAttributeValue(null, "errCode");
                            String errInfo = myParser.getAttributeValue(null, "errInfo");
                            if (!MOSLCommon.isBlank(errCode) && !errCode.equalsIgnoreCase("0")) {
                                commonMsgDialog(BioMetricMainActivity.this, errInfo, false, "");
                                return false;
                            } else {
                                if (irisOrFinger == 2 && errInfo.equalsIgnoreCase("No Error")) {//2 for Iris 1 for Finger
                                    commonMsgDialog(BioMetricMainActivity.this, "Capture Success", false, "");
                                } else {
                                    commonMsgDialog(BioMetricMainActivity.this, errInfo, false, "");
                                }
                            }
                        }
                        break;
                }
                event = myParser.next();
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            commonMsgDialog(BioMetricMainActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
            return false;
        }
        return true;
    }

    //////////////////IRIS\\\\\\\\\\\\\\\\\\\\\\\\\\\
    private boolean hasIRISPermission() {
        try {
            String BIOMETRIC_LICENSE_PERMISSION = "com.sec.enterprise.biometric.permission.IRIS_RECOGNITION";
            PackageManager packageManager = this.getApplicationContext()
                    .getPackageManager();
            if (packageManager.checkPermission(BIOMETRIC_LICENSE_PERMISSION,
                    this.getApplicationContext().getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, ">>> hasPermission : true");
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        Log.d(TAG, ">>> hasPermission : false");
        return false;
    }

    private String getIRISPIDOptions() {

        Log.d(TAG, "getIRISPIDOptions");
        int numeye = 2;
        boolean singleChecked = singleEye.isChecked();
        if (singleChecked) {
            numeye = 1;
        }

        String inputxml = String.format(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                        + "<PidOptions ver=\"" + "1.0" + "\" " + ">" + "<Opts " + "iCount=\""
                        + numeye
                        + "\" "
                        + "format=\""
                        + "0"
                        + "\" "
                        + "iType=\""
                        + "0"
                        + "\" "

                        + "pidVer=\""
                        + "2.0"
                        + "\" "

                        + "wadh=\""
//                        + "Z0ubZzHpEaLZ8v9625njsSVnFOoIbvo/Zg3Z8TYI46c="
                        + IRISWADHValue
                        + "\" "


                        + "env=\""
                        + WebServiceHandler.UID_ENV
                        + "\" "
                        + "timeout=\""
                        + "1200"
                        + "\" />"
                        + "<Demo lang=\"eng\">"
                        /*+ "<Pi "
                        + "name=\""
                        + getPANName()
                        + "\"/>"*/
                        + "</Demo>"
                        + "</PidOptions>");
        Log.d(TAG, "getIRISPIDOptions response: " + inputxml);

        return inputxml;
    }

    //////////////// API CALL \\\\\\\\\\\\\\\\\\\\\\\
    private void validateUser(final AuthRequest model) {
        final String mode = model.getAuthMode();

        showMessageNProgress("Validating " + mode + " ...", true);
        Map inputMap = new HashMap<>();
        if (model.getAadharNumber().length() == 12) {
            inputMap.put("UIDType", "AADHAAR_NUMBER");
        } else if (model.getAadharNumber().length() == 16) {
            inputMap.put("UIDType", "VIRTUAL_ID");
        }
        inputMap.put("AadhaarNumber", model.getAadharNumber());
        inputMap.put("Userid", model.getUserId());
        inputMap.put("AuthenticationMode", model.getAuthMode());
        inputMap.put("PIDData", model.getPidData());
        inputMap.put("TimeStamp", model.getTimeStamp());
        inputMap.put("IPAddress", getLocalIpAddress());

        String flag = FETCHAADHARDETAILSIRISBIOMETRIC;
        String category = PANADDHARDETAILS;

        customProgress.setVisibility(View.VISIBLE);

        WebServiceHandler.post(BioMetricMainActivity.this, flag, category, inputMap, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        requestData = null;
//                        if (source == PAN_AADHAR_ACTIVITY) {
                        try {
                            if (response != null) {

                                String isSuccess = (response.isNull("isSuccess") || response.getString("isSuccess").equals("")) ? "" : response.getString("isSuccess");

                                if (isSuccess.equalsIgnoreCase("N")) {
                                    String msg = (response.isNull("Message") || response.getString("Message").equals("")) ? "" : response.getString("Message");
                                    showMessageNProgress(msg + " " + mode, true);
                                    valididateDialog(msg, isSuccess, "", "");
                                    customProgress.setVisibility(View.GONE);
                                } else if (isSuccess.equalsIgnoreCase("Y")) {
                                    setIsAadharVerified(true);
                                    if (source == ESIGN_ACTIVITY) {
                                        performEsign(model);
                                    } else {
                                        showMessageNProgress("", false);
                                        valididateDialog(VALIDATION_SUCCESS, isSuccess, "", "");
                                    }
                                    customProgress.setVisibility(View.GONE);
                                } else {
                                    commonMsgDialog(BioMetricMainActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                                    customProgress.setVisibility(View.GONE);
                                }
                                customProgress.setVisibility(View.GONE);

                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                            commonMsgDialog(BioMetricMainActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                            customProgress.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        requestData = null;
                        showMessageNProgress(mode + VALIDATION_SUCCESS);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable
                            throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        requestData = null;
                        commonMsgDialog(BioMetricMainActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                        showMessageNProgress(mode + " Validation Failed. Please retry!");
//                        MOSLCommon.alertDialog(BioMetricMainActivity.this, 0, "Oops", SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable
                            throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        commonMsgDialog(BioMetricMainActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                        requestData = null;
                        showMessageNProgress(mode + " Validation Failed. Please retry!");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String
                            responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        commonMsgDialog(BioMetricMainActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                        requestData = null;
                        showMessageNProgress(mode + " Validation Failed. Please retry!");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        requestData = null;
                    }
                }
        );
    }


    public void valididateDialog(String msg, final String status, final String eSignPDFURL, final String errorMsg) {
        dialog = new Dialog(BioMetricMainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setWindowAnimations(R.style.animationName);
        dialog.setContentView(R.layout.layout_dialog_info);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
        Button btnOK = (Button) dialog.findViewById(R.id.btnOK);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);

        tvMsg.setText(msg);
       /* if (status.equalsIgnoreCase("n")) {
            tvMsg.setText(msg);
        }*/
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                customProgress.setVisibility(View.GONE);
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customProgress.setVisibility(View.GONE);
                if (status.equalsIgnoreCase("N")) {
                    dialog.dismiss();

                } else if (status.equalsIgnoreCase("Y")) {
                    //PANADHAAR
                    dialog.dismiss();
                    Intent intent = new Intent();
                    intent.putExtra(VALIDATION_SUCCESS, VALIDATION_SUCCESS);

                    setResult(PAN_AADHAAR_BIOMETRIC_RESULT_OK, intent);
                    finish();
                }

                if (status.equalsIgnoreCase(SUCCESS)) {
                    //ESIGN
                    dialog.dismiss();
                    Intent intent = new Intent();
                    intent.putExtra(VALIDATION_SUCCESS, VALIDATION_SUCCESS);
                    intent.putExtra(ESIGN_PDF_URL, eSignPDFURL);
                    intent.putExtra(ERROR_MESSAGE, errorMsg);

                    setResult(ESIGN_BIOMETRIC_RESULT_OK, intent);
                    finish();
                } else {
                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }

    private void performEsign(final AuthRequest model) {
        final String mode = model.getAuthMode();
//        final String mode = isIris ? "IRIS" : "FINGER";
        showMessageNProgress("Validating " + mode + " ...", true);
        Map inputMap = new HashMap<>();
        String flag = "", category = "";

        inputMap.put("Userid", model.getUserId());
        inputMap.put("Mode", mode);
        inputMap.put("AadharNumber", model.getAadharNumber());
        inputMap.put("PageLevel", "PAGE LEVEL");
        inputMap.put("CoOrdinates", "");
        inputMap.put("AadharHolderName", getClientName());
        inputMap.put("PageNumberNCordinates", "");
        inputMap.put("OpenPassword", "");
        inputMap.put("Watermark", "");
        inputMap.put("SetUserPassword", "");
        inputMap.put("MasterPassword", "");
        inputMap.put("Location", getCity());
        inputMap.put("Reason", "eSign");
        inputMap.put("MergedFileName", "");
        inputMap.put("SaveSignedDocPath", "");
        inputMap.put("IRISTemppath", "");
        inputMap.put("IRISAuthXML", model.getPidData());
        inputMap.put("IRISTimestamp", model.getTimeStamp());
        inputMap.put("IRISisPreverified", "true");
        inputMap.put("IRISkycResXML", "");
        inputMap.put("UDC", getDeviceID(BioMetricMainActivity.this, getContentResolver()));
        inputMap.put("IPAddress", getLocalIpAddress());


        if (getInterestedInMF()) {
            flag = PerformeSignMF;
        } else {

            flag = PERFORMESIGN;

        }
        category = ESIGN;
        customProgress.setVisibility(View.VISIBLE);
        WebServiceHandler.post(BioMetricMainActivity.this, flag, category, inputMap, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            esignErrorMsg = (response.isNull("ErrorMessage") || response.getString("ErrorMessage").equals("")) ? "" : response.getString("ErrorMessage");


                            String errorCode = (response.isNull("ErrorCode") || response.getString("ErrorCode").equals("")) ? "" : response.getString("ErrorCode");
                            String returnStatus = (response.isNull("ReturnStatus") || response.getString("ReturnStatus").equals("")) ? "" : response.getString("ReturnStatus");
                            eSignPDFURL = (response.isNull("eSignPDFURL") || response.getString("eSignPDFURL").equals("")) ? "" : response.getString("eSignPDFURL");
                            if (returnStatus.equalsIgnoreCase("success")) {
                                    /*llPostEKYC.setVisibility(View.VISIBLE);
                                    llOTPThumbLayout.setVisibility(View.GONE);
                                    getAvailableClientCode();*/
                                showMessageNProgress("", false);
                                valididateDialog(VALIDATION_SUCCESS, returnStatus, eSignPDFURL, esignErrorMsg);
                            } else {
                                        /*informationDialog(esignErrorMsg, 1);
                                        llPostEKYC.setVisibility(View.GONE);*/
                                showMessageNProgress(VALIDATION_UNSUCCESS + " " + mode, true);
                                valididateDialog(VALIDATION_UNSUCCESS, returnStatus, "", esignErrorMsg);
                            }
                            customProgress.setVisibility(View.GONE);
                        } catch (Throwable e) {
                            e.printStackTrace();
                            commonMsgDialog(BioMetricMainActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                            customProgress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        toast("success " + response.toString());
                        showMessageNProgress(mode + VALIDATION_SUCCESS);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable
                            throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
//                        toast("failed " + throwable.toString());
                        commonMsgDialog(BioMetricMainActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                        showMessageNProgress(mode + " Validation Failed. Please retry!");
//                        MOSLCommon.alertDialog(BioMetricMainActivity.this, 0, "Oops", SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable
                            throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        commonMsgDialog(BioMetricMainActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
//                        MOSLCommon.alertDialog(BioMetricMainActivity.this, 0, "Oops", SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        showMessageNProgress(mode + " Validation Failed. Please retry!");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String
                            responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        commonMsgDialog(BioMetricMainActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
//                        MOSLCommon.alertDialog(BioMetricMainActivity.this, 0, "Oops", SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        showMessageNProgress(mode + " Validation Failed. Please retry!");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        commonMsgDialog(BioMetricMainActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
//                        MOSLCommon.alertDialog(BioMetricMainActivity.this, 0, "Oops", SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        showMessageNProgress(mode + " Validation Failed. Please retry!");
                    }
                }

        );
    }

    public void toast(String message) {
        Timber.d(message);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void loadWadhValues() {
        Map inputMap = new HashMap<>();
        String flag = "", category = "";

        inputMap.put("KeyValue", "MOSL");

        flag = GETWADHVALUESKEY;
        category = PANADDHARDETAILS;
        customProgress.setVisibility(View.VISIBLE);
        WebServiceHandler.post(BioMetricMainActivity.this, flag, category, inputMap, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    customProgress.setVisibility(View.GONE);
                    String irisWadhApiValue = (response.isNull("IRISWADHValue") || response.getString("IRISWADHValue").equals("")) ? "" : response.getString("IRISWADHValue");
                    String bioWadhApiValue = (response.isNull("BIOWADHValue") || response.getString("BIOWADHValue").equals("")) ? "" : response.getString("BIOWADHValue");
                    IRISWADHValue = irisWadhApiValue;
                    BIOWADHValue = bioWadhApiValue;
                } catch (Throwable e) {
                    e.printStackTrace();
                    commonMsgDialog(BioMetricMainActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                    customProgress.setVisibility(View.GONE);
                }

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                customProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable
                    throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                commonMsgDialog(BioMetricMainActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                customProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable
                    throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                commonMsgDialog(BioMetricMainActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                customProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String
                    responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                commonMsgDialog(BioMetricMainActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                customProgress.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
                commonMsgDialog(BioMetricMainActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                customProgress.setVisibility(View.GONE);
            }
        });
    }
}
