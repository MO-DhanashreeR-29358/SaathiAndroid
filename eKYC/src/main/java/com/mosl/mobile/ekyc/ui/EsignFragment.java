package com.mosl.mobile.ekyc.ui;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.digio.in.esign2sdk.Digio;
import com.digio.in.esign2sdk.DigioConfig;
import com.digio.in.esign2sdk.DigioEnvironment;
import com.digio.in.esign2sdk.DigioServiceMode;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.biometric.ui.BioMetricMainActivity;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.CustomTabsHelper;
import com.mosl.mobile.ekyc.common.CustomViewPager;
import com.mosl.mobile.ekyc.common.EkycUtil;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.common.ValidationRule;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.directmodule.DirectResumeApplicationActivity;
import com.mosl.mobile.ekyc.model.CommonModel;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import com.nsdl.egov.esignaar.NsdlEsignActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;
import static com.mosl.mobile.ekyc.common.MOSLCommon.commonMsgDialog;
import static com.mosl.mobile.ekyc.common.MOSLCommon.getDeviceID;
import static com.mosl.mobile.ekyc.common.MOSLCommon.getLocalIpAddress;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isBlank;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isIrisSupported;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isThisSecondProductFlow;
import static com.mosl.mobile.ekyc.common.MOSLCommon.removeKeyboard;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setListViewHeightBasedOnChildren;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.model.CommonModel.*;
import static com.mosl.mobile.ekyc.service.WebServiceHandler.UID_ENV;
import static com.mosl.mobile.ekyc.ui.EkycMainActivity.customProgress;

/**
 * Created by Shefali on 04/08/16.
 */
public class EsignFragment extends Fragment implements MOSLConstants {

    private Context mContext;
    private CustomViewPager viewPager;
    private View esignView;
    //TextView tvPreEsign, tvPostEKYC, tvResendOTP;
    private TextView tvResendOTP;
    private Button btnSubmit, btnRetry, btnSignEkyc;
    public static final int ESIGN_BIOMETRIC_RESULT_OK = 1012;
    private LinearLayout llOTPThumbLayout, llPreEsign, llParentOTPBio, llOTP;
    private RelativeLayout rlOTP, rlClientCode, rlAffix;
    private EditText etOTP, editClienCode, editClienCodeError;
    private ImageView ivOTP, ivThumb;
    private TextView tvAuthenticationMode;
    private LinearLayout llEsignConsent;
    private CheckBox cbEsignConsent;
    private TextView tvEsignTitle, tvClientCodeNote, tvEmendate, tvEmendateTitle, tvEmendateDiscription;
    private LinearLayout llEmendate;
    private CustomTabsClient mClient;
    private Map<String, Map<String, String>> pdfListMap;

    // String userID, aadharNo, eSignPDFURL = "", esignErrorMsg = "", aadharValidationSuccess = "", strType = "", prePdfPath = "", postBioEsignPdf = "", errorMsg = "", strPostEsignPdfPATH = "";
    private String transactionId, userID, aadharNo, esignErrorMsg = "", aadharValidationSuccess = "", strType = "", postBioEsignPdf = "", errorMsg = "", strPostEsignPdfPATH = "";

    private ListView pdfListView;
    private PdfAdapter pdfAdapter;
    private ScrollView scrollView;
    private int brbaMode = 1;
    private int dcMode = 2;
    private int noMode = 0;
    private String nsdlEsignUrl = "";
    //    NSDL APK Package name and other parameters
    static final String ACTION_ESIGNRESPONSE = "com.nsdl.egov.esign.rdservice.fp.CAPTURE";
    private int REQUEST_CODE = 100;
    private String requestId, nsdlMode;
    private boolean otpCalled = false;
    private Uri emendateURL;
    private String bankName, bankIfscCode, bankAccNo, bankAcType, corresMobileNo, corresEmailId, uid, fullName, mandateCreationDateTime, firstCollectionDate,
            finalCollectionDate, digioDocId, txnId, digiomessage, errorCode, lastStateScreen, lastStateCode, lastStateMessage, emendateMode;
    private String nsdlEsignWebview;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {
            esignView = inflater.inflate(R.layout.activity_esign, container, false);
            setUpView(esignView);
            userID = CommonModel.getUserID();
            aadharNo = CommonModel.getAadharNo();
            setUserVisibleHint(true);
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
//            Crashlytics.logException(e);
        }
        return esignView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void setUpView(View esignView) {
        try {
            CustomTabsServiceConnection mConnection = new CustomTabsServiceConnection() {
                @Override
                public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                    mClient = customTabsClient;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    mClient = null;
                }
            };
            String packag = CustomTabsHelper.getPackageNameToUse(getContext());
            CustomTabsClient.bindCustomTabsService(getContext(), packag, mConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  isUploadFinalSubmit = getIntent().getBooleanExtra(UPLOAD_FINAL_SUBMIT, false);
        viewPager = (CustomViewPager) getActivity().findViewById(R.id.vpApply);
        scrollView = (ScrollView) esignView.findViewById(R.id.scrollView);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
        //tvPreEsign = (TextView) esignView.findViewById(R.id.tvPreEsign);
        pdfListView = (ListView) esignView.findViewById(R.id.pdfListView);
        //tvPostEKYC = (TextView) esignView.findViewById(R.id.tvPostEKYC);
        tvResendOTP = (TextView) esignView.findViewById(R.id.tvResendOTP);
        tvEsignTitle = (TextView) esignView.findViewById(R.id.tvEsignTitle);
        llEsignConsent = (LinearLayout) esignView.findViewById(R.id.llEsignConsent);

        btnSubmit = (Button) esignView.findViewById(R.id.btnSubmit);
        btnRetry = (Button) esignView.findViewById(R.id.btnRetry);
//        btnSignEkyc = (Button) esignView.findViewById(R.id.btnSignEkyc);

        btnSubmit.setVisibility(View.GONE);
        btnRetry.setVisibility(View.GONE);

        ivOTP = (ImageView) esignView.findViewById(R.id.ivOTP);
        ivThumb = (ImageView) esignView.findViewById(R.id.ivThumb);

        llOTPThumbLayout = (LinearLayout) esignView.findViewById(R.id.llOTPThumbLayout);
        llParentOTPBio = (LinearLayout) esignView.findViewById(R.id.llParentOTPBio);
        //llPostEKYC = (LinearLayout) esignView.findViewById(R.id.llPostEKYC);
        llPreEsign = (LinearLayout) esignView.findViewById(R.id.llPreEsign);
        llOTP = (LinearLayout) esignView.findViewById(R.id.llOTP);
        tvAuthenticationMode = (TextView) esignView.findViewById(R.id.tvAuthenticationMode);
        tvClientCodeNote = (TextView) esignView.findViewById(R.id.tvClientCodeNote);

        rlOTP = (RelativeLayout) esignView.findViewById(R.id.rlOTP);
        rlClientCode = (RelativeLayout) esignView.findViewById(R.id.rlClientCode);
        rlAffix = (RelativeLayout) esignView.findViewById(R.id.rlAffix);

        etOTP = (EditText) esignView.findViewById(R.id.etOTP);
        editClienCode = (EditText) esignView.findViewById(R.id.editClienCode);
        editClienCodeError = (EditText) esignView.findViewById(R.id.editClienCodeError);
        cbEsignConsent = (CheckBox) esignView.findViewById(R.id.cbEsignConsent);
        llEmendate = (LinearLayout) esignView.findViewById(R.id.llEmendate);
        tvEmendate = (TextView) esignView.findViewById(R.id.tvEmendate);
        tvEmendateTitle = (TextView) esignView.findViewById(R.id.tvEmendateTitle);
        tvEmendateDiscription = (TextView) esignView.findViewById(R.id.tvEmendateDiscription);
        tvEmendate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getAppType().equals(DIRECT)) {
                    getBankDetails(OTP);
                } else {
                    fingerIrisDialog("Emendate");
                }
//                openEmendate(emendateURL);
            }
        });

        etOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strOTP = etOTP.getText().toString();
                if (strOTP.length() == 6) {
                    performEsign();
                    removeKeyboard(mContext, etOTP);
                }
                /*else {
                    informationDialog("OTP should be max 6 ", 2, false);
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editClienCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(editClienCode.getText().toString())) {
                    if (!ValidationRule.validateClientCode(editClienCode.getText().toString())) {
                        editClienCodeError.setText("Invalid client code");
                        editClienCodeError.setVisibility(View.VISIBLE);
                    } else {
                        editClienCodeError.setVisibility(View.GONE);
                    }
                } else {
                    editClienCodeError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //tvPreEsign.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        //tvPostEKYC.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvResendOTP.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        llOTPThumbLayout.setVisibility(View.GONE);
        rlOTP.setVisibility(View.GONE);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strClientCode = editClienCode.getText().toString();
                if (cbEsignConsent.isChecked()) {
                    if (getAppType().equals(CRM) || getAppType().equals(PP)) {
                        if (!ValidationRule.validateClientCode(editClienCode.getText().toString())) {
                            editClienCodeError.setText("Invalid client code");
                            editClienCodeError.setVisibility(View.VISIBLE);
                            editClienCode.requestFocus();
                        } else {
                            editClienCodeError.setVisibility(View.GONE);
                            generateClientCodeBRPP(strClientCode);
                        }
//                    MOSLCommon.showToastMsg(mContext, "Client code validation failed");
                    } else {
                        generateClientCodeForDirect();
                    }
                } else {
                    commonMsgDialog(mContext, "Kindly accept the E-sign consent in order to continue with authentication and application submission.", false, "");
                }
            }
        });
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEsignDetails();
            }
        });
        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*MOSLCommon.alertDialog(mContext, 0, MOSLConstants.MOTILAL_OSWAL,
                        "Resend OTP ?", "YES", "NO", false, new MOSLCommon.DialogListener() {
                            @Override
                            public void alertDialogAction(MOSLCommon.Action action, Object... data) {
                                if (action == MOSLCommon.Action.OK) {
                                    generateOTP();
                                }
                            }
                        });*/

                CustomDialog resendCd = new CustomDialog(mContext, RESEND_OTP_MSG, true, true, false, "", YES, NO, new CustomDialog.CustomDialogListener() {
                    @Override
                    public void dialogAction(MOSLCommon.Action action, Object... data) {
                        if (action == MOSLCommon.Action.OK) {
                            generateOTP();
                        }
                    }
                });
                resendCd.show();

            }

        });

        /*btnSignEkyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strOTP = etOTP.getText().toString();
                if (strOTP.length() == 6) {
                    performEsign();
                } else {
                    informationDialog("OTP should be max 6 ", 2, false);
                }
            }
        });*/
        ivOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpCalled = true;
                ivOTP.setImageResource(R.drawable.ic_otp_select);
                ivThumb.setImageResource(R.drawable.ic_thumb_unselect);
                strType = "OTP";
                if (getEsignService().equalsIgnoreCase(NSDL)) {//Conition for nsdl
                    if (!isBlank(nsdlEsignWebview) && nsdlEsignWebview.equalsIgnoreCase("1"))//Show Web view
                        openWebUrl(Uri.parse(nsdlEsignUrl + EkycUtil.encrypt(getUserID() + "|" + "1|" + "Mobile")), "", "CustomTabs");
                    else if (!isBlank(nsdlEsignWebview) && nsdlEsignWebview.equalsIgnoreCase("0"))//Call Nsdl APK
                        genrateEsignXML(NSDL_OTP);
                } else {
                    llOTPThumbLayout.setVisibility(View.VISIBLE);
                    rlOTP.setVisibility(View.VISIBLE);
//                    sendOTPDialog(SEND_OTP);
                    generateOTP();
                }
            }
        });
        ivThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpCalled = false;
                ivOTP.setImageResource(R.drawable.ic_otp_unselect);
                ivThumb.setImageResource(R.drawable.ic_thumb_select);
                strType = "Thumb";

                /*if (!isIrisSupported(mContext)) {
                    ivOTP.setImageResource(R.drawable.ic_otp_unselect);
                    ivThumb.setImageResource(R.drawable.ic_thumb_unselect);
                    return;
                }*/
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    commonMsgDialog(mContext, getResources().getString(R.string.nsdl_support_msg), false, "");
                } else {
                    if (getEsignService().equalsIgnoreCase(NSDL)) {//Condition for nsdl
                        if (!isIrisSupported(mContext)) {
                            genrateEsignXML(NSDL_FINGER);
                        } else {
                            if (!isBlank(nsdlEsignWebview) && nsdlEsignWebview.equalsIgnoreCase("1"))//Call Nsdl APK for Finger
                                genrateEsignXML(NSDL_FINGER);
                            else if (!isBlank(nsdlEsignWebview) && nsdlEsignWebview.equalsIgnoreCase("0"))//Call Dialog to Ask Iris Or Finger
                                fingerIrisDialog(ESIGN);
                        }
                    } else {
                        llOTPThumbLayout.setVisibility(View.GONE);
                        rlOTP.setVisibility(View.GONE);
                        Intent intent = new Intent(mContext, BioMetricMainActivity.class);
                        intent.putExtra(USER_ID, CommonModel.getUserID());
                        intent.putExtra(AADHAR_NUMBER, getAadharNo());
                        intent.putExtra(SOURCE, ESIGN_ACTIVITY);
                        startActivityForResult(intent, 1);
                    }
                }
            }
        });


       /* tvPostEKYC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MOSLCommon.alertDialog(mContext, 0, MOSLConstants.MOTILAL_OSWAL,
                        "Do you want to download post-esign kyc form", "YES", "NO", false, new MOSLCommon.DialogListener() {
                            @Override
                            public void alertDialogAction(MOSLCommon.Action action, Object... data) {
                                if (action == MOSLCommon.Action.OK) {
                                    if (!eSignPDFURL.equals("")) {
                                        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                                            downloadPDF(eSignPDFURL);
                                        } else {
                                            ((EkycMainActivity) mContext).requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

                                        }

                                    } else {
                                        informationDialog("PDF not available", 2, false);
                                    }
                                }
                            }
                        });

            }
        });
        tvPreEsign.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              MOSLCommon.alertDialog(mContext, 0, MOSLConstants.MOTILAL_OSWAL,
                                                      "Do you want to download pre-esign kyc form", "YES", "NO", false, new MOSLCommon.DialogListener() {
                                                          @Override
                                                          public void alertDialogAction(MOSLCommon.Action action, Object... data) {
                                                              if (action == MOSLCommon.Action.OK) {
                                                                  if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                                                      downloadPDF(prePdfPath);
                                                                  } else {
                                                                      ((EkycMainActivity) mContext).requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                                                                  }

                                                              }*//* else {
                                                                  informationDialog("PDF not available", 2,false);
                                                              }*//*
                                                          }

                                                      }

                                              );

                                          }
                                      }

        );*/
        //     uploadDetail();
       /* if (isUploadFinalSubmit()) {
            btnAffixeSign.setVisibility(View.GONE);
            generateClientCodeForDirect();
        } else {
            getEsignDetails();
        }*/

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && esignView != null && viewPager.getCurrentItem() == getEsignTab()) {
                uploadDetail();
                setPermisson(getActivity(), mContext);
                disableEnableField();
            }
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
//            Crashlytics.logException(e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewPager.getCurrentItem() == getEsignTab()) {
            if (otpCalled)
                uploadDetail();
        }
    }

    public void uploadDetail() {
        if (getInterestedInMF()) {
            if (getAppType().equals(DIRECT)) {
                if (getIsCVLVerified() && !getIsAadharVerified()) {
                    rlAffix.setVisibility(View.GONE);
                    generateClientCodeForDirect();
                } else if (getIsCVLVerified() && getIsAadharVerified()) {
                    rlAffix.setVisibility(View.GONE);
                    generateClientCodeForDirect();
                } else if (!getIsCVLVerified() && getIsAadharVerified()) {
                    rlAffix.setVisibility(View.VISIBLE);
                    tvEsignTitle.setText(getResources().getString(R.string.affix_esign));
                    getEsignDetails();
                } else if (!getIsCVLVerified() && !getIsAadharVerified()) {
                    rlAffix.setVisibility(View.GONE);
                    generateClientCodeForDirect();
                }
            } else if (getAppType().equals(PP) || getAppType().equals(CRM)) {
                if (getIsCVLVerified() && !getIsAadharVerified()) {
                    if (!isThisSecondProductFlow()) {
                        tvClientCodeNote.setVisibility(View.VISIBLE);
                    }
                    rlClientCode.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.VISIBLE);
                    rlAffix.setVisibility(View.VISIBLE);
                    tvEsignTitle.setText(getResources().getString(R.string.create_client_code));
                    getAvailableClientCode();
                } else if (getIsCVLVerified() && getIsAadharVerified()) {
                    if (!isThisSecondProductFlow()) {
                        tvClientCodeNote.setVisibility(View.VISIBLE);
                    }
                    rlClientCode.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.VISIBLE);
                    rlAffix.setVisibility(View.VISIBLE);
                    tvEsignTitle.setText(getResources().getString(R.string.create_client_code));
                    getAvailableClientCode();
                } else if (!getIsCVLVerified() && getIsAadharVerified()) {
                    rlAffix.setVisibility(View.VISIBLE);
                    tvEsignTitle.setText(mContext.getResources().getString(R.string.affix_esign));
                    getEsignDetails();
                }
            }
        } else {
            rlAffix.setVisibility(View.VISIBLE);
            tvEsignTitle.setText(mContext.getResources().getString(R.string.affix_esign));
            getEsignDetails();
        }
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    private void getEsignDetails() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put("UserID", CommonModel.getUserID());

            String flag = "";
            if (getInterestedInMF()) {
                flag = GetEsignDetails_MF;
            } else {
                flag = GETESIGNDETAILS;
            }
            String category = ESIGN;

            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                 /*[
                {
                    "ClientName":"Karuna Vasant Kamble",
                        "ApplicationFrom":"PP",
                        "AadharNo":"613785271500",
                        "EQPayment":"3",
                        "COMMPayment":"0",
                        "PRODUCT":"EQUITY",
                        "PDFPATH":"http:\/\/web20.motilaloswal.com:8000\/PDFUpload\/upload\/proof\/613785271500\/613785271500_EQFinal.pdf"
                }
                ]*/
                    if (statusCode == 200) {
                        if (response.length() != 0) {
                            customProgress.setVisibility(View.VISIBLE);
                            List<PdfModel> pdfList = new ArrayList<PdfModel>();
                            pdfListMap = new HashMap<String, Map<String, String>>();
                            String enableClientCode = "";
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject object = response.getJSONObject(i);
                                    String clientName = (object.isNull("ClientName") || object.getString("ClientName").equals("")) ? "" : object.getString("ClientName");
                                    String product = (object.isNull("PRODUCT") || object.getString("PRODUCT").equals("")) ? "" : object.getString("PRODUCT");
                                    String aadharNo = (object.isNull("AadharNo") || object.getString("AadharNo").equals("")) ? "" : object.getString("AadharNo");
                                    String pdfPATH = (object.isNull("PDFPATH") || object.getString("PDFPATH").equals("")) ? "" : object.getString("PDFPATH");
                                    String postEsignPdfPATH = (object.isNull("POSTESIGNPDFPATH") || object.getString("POSTESIGNPDFPATH").equals("")) ? "" : object.getString("POSTESIGNPDFPATH");
                                    String esignCompleted = (object.isNull("ESignCompleted") || object.getString("ESignCompleted").equals("")) ? "" : object.getString("ESignCompleted");
                                    String city = (object.isNull("CITY") || object.getString("CITY").equals("")) ? "" : object.getString("CITY");
                                    enableClientCode = (object.isNull("ENABLECLIENTCODE") || object.getString("ENABLECLIENTCODE").equals("")) ? "" : object.getString("ENABLECLIENTCODE");
                                    nsdlEsignUrl = (object.isNull("NSDLESIGNURL") || object.getString("NSDLESIGNURL").equals("")) ? "" : object.getString("NSDLESIGNURL");
                                    String esignService = (object.isNull("ESIGNSERVICE") || object.getString("ESIGNSERVICE").equals("")) ? "" : object.getString("ESIGNSERVICE");
                                    String emendateUrl = (object.isNull("EMENDATEURL") || object.getString("EMENDATEURL").equals("")) ? "" : object.getString("EMENDATEURL");
                                    String emendateVisible = (object.isNull("EMENDATEVISIBLE") || object.getString("EMENDATEVISIBLE").equals("")) ? "" : object.getString("EMENDATEVISIBLE");
                                    String emendateTitle = (object.isNull("EMENDATETITLE") || object.getString("EMENDATETITLE").equals("")) ? "" : object.getString("EMENDATETITLE");
                                    String emendateDiscription = (object.isNull("EMENDATEDISCRIPTION") || object.getString("EMENDATEDISCRIPTION").equals("")) ? "" : object.getString("EMENDATEDISCRIPTION");
                                    nsdlEsignWebview = (object.isNull("NSDLESIGNWEBVIEW") || object.getString("NSDLESIGNWEBVIEW").equals("")) ? "" : object.getString("NSDLESIGNWEBVIEW");
                                    String emandateDisable = (object.isNull("EMANDATE") || object.getString("EMANDATE").equals("")) ? "" : object.getString("EMANDATE");
                                    product = product.toUpperCase();
                                    setClientName(clientName);
                                    setAadharNo(aadharNo);
                                    setCity(city);
                                    setEsignService(esignService);
                                    pdfPATH = pdfPATH.replaceAll(" ", "%20");
                                    postEsignPdfPATH = postEsignPdfPATH.replaceAll(" ", "%20");
                                    llPreEsign.setVisibility(View.VISIBLE);
                                    pdfList.add(new PdfModel(product, pdfPATH, postEsignPdfPATH));

                                    Map pdfMap = new HashMap();
                                    pdfMap.put(PRE_PDF_PATH, pdfPATH);
                                    pdfMap.put(POST_PDF_PATH, postEsignPdfPATH);
                                    pdfListMap.put(product, pdfMap);

                                    if (emendateVisible.equalsIgnoreCase("1")) {
                                        emendateURL = Uri.parse(emendateUrl);
                                        tvEmendateTitle.setText(emendateTitle);
//                                        tvEmendateDiscription.setText(emendateDiscription);
                                        llEmendate.setVisibility(View.VISIBLE);
                                        tvEmendate.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                                        if (emandateDisable.equalsIgnoreCase("1"))
                                            tvEmendate.setEnabled(false);
                                        else if (emandateDisable.equalsIgnoreCase("0"))
                                            tvEmendate.setEnabled(true);
                                    } else if (emendateVisible.equalsIgnoreCase("0"))
                                        llEmendate.setVisibility(View.GONE);
//                                    if (completedProductMap.get(product) == null || !completedProductMap.get(product)) {
                                    /*if (!pdfPATH.equals("") && !postEsignPdfPATH.equals("")) {
                                        llPreEsign.setVisibility(View.VISIBLE);
                                        llEsignConsent.setVisibility(View.VISIBLE);
                                        if (product.equalsIgnoreCase(EQUITY)) {
                                            isEquPostPDFGenerated = true;
                                        } else if (product.equalsIgnoreCase(COMMODITY)) {
                                            isCommPostPDFGenerated = true;
                                        } else if (product.equalsIgnoreCase(COMBINE)) {
                                            isCommPostPDFGenerated = true;
                                            isEquPostPDFGenerated = true;
                                        } else if (product.equalsIgnoreCase(MF)) {
                                            isCommPostPDFGenerated = true;
                                            isEquPostPDFGenerated = true;
                                        }
                                        //llPostEKYC.setVisibility(View.VISIBLE);
                                        if (getAppType().equals(CRM) || getAppType().equals(PP)) {
                                            if (!isSegmentExist(product)) {
                                                if (response.length() < 1) {
                                                    if (isCommPostPDFGenerated && isEquPostPDFGenerated) {
                                                        getAvailableClientCode();
                                                    }
                                                } else if (response.length() == 1) {
                                                    getAvailableClientCode();
                                                }
                                            }
                                        } else {
                                            if (response.length() < 1) {
                                                if (isCommPostPDFGenerated && isEquPostPDFGenerated) {
                                                    customProgress.setVisibility(View.GONE);
                                                    showAuthenticationModeMsg(false, noMode);
                                                    llParentOTPBio.setVisibility(View.GONE);
                                                    btnSubmit.setVisibility(View.VISIBLE);
                                                } else {
                                                    prePdfGenerate();
                                                }
                                            } else if (response.length() == 1) {
                                                customProgress.setVisibility(View.GONE);
                                                showAuthenticationModeMsg(false, noMode);
                                                llParentOTPBio.setVisibility(View.GONE);
                                                btnSubmit.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    } else if (!pdfPATH.equals("")) {
                                        if (product.equalsIgnoreCase(EQUITY)) {
                                            isEquPostPDFGenerated = false;
                                        } else if (product.equalsIgnoreCase(COMMODITY)) {
                                            isCommPostPDFGenerated = false;
                                        } else if (product.equalsIgnoreCase(COMBINE)) {
                                            isCommPostPDFGenerated = false;
                                            isEquPostPDFGenerated = false;
                                        } else if (product.equalsIgnoreCase(MF)) {
                                            isCommPostPDFGenerated = true;
                                            isEquPostPDFGenerated = true;
                                        }
                                        //prePdfPath = pdfPATH;
                                        //prePdfPath.replaceAll(" ", "%20");
                                        prePdfGenerate();
                                    } else {
                                        // postPdfGenerate();
                                        noPrePdfGenerated();
                                        btnRetry.setVisibility(View.VISIBLE);
                                        informationDialog("Pre-esign pdf is not generated, plesae retry", 2, false);
                                    }*/

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    customProgress.setVisibility(View.GONE);
                                }
                            }
                            customProgress.setVisibility(View.GONE);
                            pdfAdapter = new PdfAdapter(mContext, pdfList);
                            pdfListView.setAdapter(pdfAdapter);
                            pdfAdapter.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(pdfListView);

                            if (enableClientCode.equalsIgnoreCase("1")) {
                                tvClientCodeNote.setVisibility(View.GONE);
                                editClienCode.setEnabled(false);
                            }
                            if (isPrePdfPending()) {
                                noPrePdfGenerated();
                                btnRetry.setVisibility(View.VISIBLE);
                                informationDialog("Pre-esign pdf is not generated, please retry", 2, false);
                            } else if (!isPrePdfPending() && isPostPdfPending()) {
                                prePdfGenerate();
                            } else if (!isPostPdfPending()) {
                                if (getAppType().equals(CRM) || getAppType().equals(PP)) {
                                    llPreEsign.setVisibility(View.VISIBLE);
                                    llEsignConsent.setVisibility(View.VISIBLE);
                                    getAvailableClientCode();
                                } else {
                                    customProgress.setVisibility(View.GONE);
                                    showAuthenticationModeMsg(false, noMode);
                                    llParentOTPBio.setVisibility(View.GONE);
                                    btnSubmit.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
//                    MOSLCommon.showToastMsg(mContext, responseString);
                    customProgress.setVisibility(View.GONE);
                }

            });
        } else {
            MOSLCommon.alertDInternet(mContext);
        }
    }

    /*
     * Method check if pre pdf is generated or not
     * */
    private boolean isPrePdfPending() {
        Set<String> segmentSet = pdfListMap.keySet();

        if (pdfListMap.size() == 1) {
            String segment = segmentSet.iterator().next();
            Map<String, String> currentStageMap = pdfListMap.get(segment);
            return isBlank(currentStageMap.get(PRE_PDF_PATH));
        } else if (pdfListMap.size() == 2) {
            Iterator<String> iterator = segmentSet.iterator();
            String segment1 = iterator.next().toString();
            String segment2 = iterator.next().toString();

            Map<String, String> currentStageMap1 = pdfListMap.get(segment1);
            Map<String, String> currentStageMap2 = pdfListMap.get(segment2);

            return !(!isBlank(currentStageMap1.get(PRE_PDF_PATH)) && !isBlank(currentStageMap2.get(PRE_PDF_PATH)));
        }
        return true;
    }

    /*
     * Method check if post pdf is generated or not
     * */
    private boolean isPostPdfPending() {
        Set<String> segmentSet = pdfListMap.keySet();

        if (pdfListMap.size() == 1) {
            String segment = segmentSet.iterator().next();
            Map<String, String> currentStageMap = pdfListMap.get(segment);
            return isBlank(currentStageMap.get(POST_PDF_PATH));
        } else if (pdfListMap.size() == 2) {
            Iterator<String> iterator = segmentSet.iterator();
            String segment1 = iterator.next().toString();
            String segment2 = iterator.next().toString();

            Map<String, String> currentStageMap1 = pdfListMap.get(segment1);
            Map<String, String> currentStageMap2 = pdfListMap.get(segment2);

            return !(!isBlank(currentStageMap1.get(POST_PDF_PATH)) && !isBlank(currentStageMap2.get(POST_PDF_PATH)));
        }
        return true;
    }

    public void noPrePdfGenerated() {
        showAuthenticationModeMsg(false, noMode);
        llPreEsign.setVisibility(View.GONE);
        llParentOTPBio.setVisibility(View.GONE);
        llOTPThumbLayout.setVisibility(View.GONE);

    }

    public void prePdfGenerate() {

        llPreEsign.setVisibility(View.VISIBLE);
        llParentOTPBio.setVisibility(View.VISIBLE);
        llEsignConsent.setVisibility(View.VISIBLE);
//        llOTPThumbLayout.setVisibility(View.VISIBLE);
        if (getAppType().equalsIgnoreCase(DIRECT)) {
//            ivThumb.setVisibility(View.GONE);//call showHideOTPBioLayout method
            showHideOTPBioLayout();
            showAuthenticationModeMsg(true, dcMode);
        } else {
            showAuthenticationModeMsg(true, brbaMode);
            showHideOTPBioLayout();
//            ivThumb.setVisibility(View.VISIBLE);//call showHideOTPBioLayout method
        }

    }

    public void postPdfGenerate() {
        //llPostEKYC.setVisibility(View.VISIBLE);
    }

    public void noPostPdfGenerate() {
        // llPostEKYC.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getEsignService().equalsIgnoreCase(NSDL)) {//Conition for nsdl
            // Check which request we're responding to
            if (requestCode == REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    String eSignResponse = data.getStringExtra("signedResponse");
                    Log.d("signedResponse", eSignResponse);
                    esignXMLResponse(eSignResponse);
                }
            } else {
//                signedResponseDone = false;
            }
        } else {
            if (resultCode == ESIGN_BIOMETRIC_RESULT_OK) {
                aadharValidationSuccess = data.getStringExtra(VALIDATION_SUCCESS);
                postBioEsignPdf = data.getStringExtra(ESIGN_PDF_URL);
                errorMsg = data.getStringExtra(ERROR_MESSAGE);

                pdfAdapter.setPostPdf(postBioEsignPdf);
                setListViewHeightBasedOnChildren(pdfListView);

                if (aadharValidationSuccess.equals(VALIDATION_SUCCESS)) {
                    //llPostEKYC.setVisibility(View.VISIBLE);
                    llOTPThumbLayout.setVisibility(View.GONE);
                    if (getAppType().equals(CRM) || getAppType().equals(PP)) {
                        getAvailableClientCode();
                    } else {
                        showAuthenticationModeMsg(false, noMode);
                        llParentOTPBio.setVisibility(View.GONE);
                        btnSubmit.setVisibility(View.VISIBLE);
                    }
                } else {
                    informationDialog(esignErrorMsg, 1, false);
                    //llPostEKYC.setVisibility(View.GONE);
                }
            }
        }
    }

    private void getAvailableClientCode() {
        Map inputMap = new HashMap<>();
        inputMap.put("Userid", CommonModel.getUserID());
        inputMap.put("ClientCode", "");


        String flag = AVAILABLECLIENTCODE;
        String category = ESIGN;

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        customProgress.setVisibility(View.VISIBLE);
                        if (response != null) {
                            String clientCode = (response.isNull("ClientCode") || response.getString("ClientCode").equals("")) ? "" : response.getString("ClientCode");
                            String message = (response.isNull("Message") || response.getString("Message").equals("")) ? "" : response.getString("Message");
                            String isSuccess = (response.isNull("isSuccess") || response.getString("isSuccess").equals("")) ? "" : response.getString("isSuccess");

                            if (isSuccess.equalsIgnoreCase("n")) {

                                informationDialog(message, 1, false);
//                                etOTP.setEnabled(true);
                            } else {
                                if (getAppType().equals(CRM) || getAppType().equals(PP)) {
                                    rlClientCode.setVisibility(View.VISIBLE);
                                    if (!isThisSecondProductFlow()) {
                                        tvClientCodeNote.setVisibility(View.VISIBLE);
                                    }
                                   /* if (getInterestedInMF()) {
                                        tvClientCodeNote.setVisibility(View.VISIBLE);
                                    } else {
                                        tvClientCodeNote.setVisibility(View.GONE);
                                    }*/
                                } else {
                                    rlClientCode.setVisibility(View.GONE);
                                    tvClientCodeNote.setVisibility(View.GONE);
                                }
                                editClienCode.setText(clientCode);
                                showAuthenticationModeMsg(false, noMode);
                                llParentOTPBio.setVisibility(View.GONE);
                                btnSubmit.setVisibility(View.VISIBLE);
                            }
                        }
                        customProgress.setVisibility(View.GONE);

                    } catch (Exception e) {
                        e.printStackTrace();
                        customProgress.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
//                    MOSLCommon.showToastMsg(mContext, responseString);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });

        } else {
            customProgress.setVisibility(View.GONE);
            MOSLCommon.alertDInternet(mContext);
        }
    }

    /*
     * Method use to show hide OTP and Bio option
     * */
    private void showHideOTPBioLayout() {
        try {
            if (getAppType().equalsIgnoreCase(DIRECT)) {
                ivThumb.setVisibility(View.GONE);
                ivOTP.setVisibility(View.VISIBLE);
            } else {
                if (getAppType().equalsIgnoreCase(PP)) {
                    if (isDirectProspect()) { // Hide Bio Option in direct App
                        ivThumb.setVisibility(View.GONE);
                        ivOTP.setVisibility(View.VISIBLE);
                    } else {
                        if (getOtpVisible().equalsIgnoreCase("1")) {//Showhide OTP layout 1 for visible and 0 for hide
                            ivOTP.setVisibility(View.VISIBLE);
                        } else if (getOtpVisible().equalsIgnoreCase("0")) {
                            ivOTP.setVisibility(View.GONE);
                        }
                        if (getBioMetricVisible().equalsIgnoreCase("1")) {//Showhide Bio layout 1 for visible and 0 for hide
                            ivThumb.setVisibility(View.VISIBLE);
                        } else if (getBioMetricVisible().equalsIgnoreCase("0")) {
                            ivThumb.setVisibility(View.GONE);
                        }
                    }
                } else if (getAppType().equalsIgnoreCase(CRM)) {
                    if (getOtpVisibleForBranch().equalsIgnoreCase("1") || getOtpVisibleForBranch().equalsIgnoreCase("")) {//Showhide OTP layout 1 for visible and 0 for hide
                        ivOTP.setVisibility(View.VISIBLE);
                    } else if (getOtpVisibleForBranch().equalsIgnoreCase("0")) {
                        ivOTP.setVisibility(View.GONE);
                    }
                    if (getBioMetricVisibleForBranch().equalsIgnoreCase("1") || getBioMetricVisibleForBranch().equalsIgnoreCase("")) {//Showhide Bio layout 1 for visible and 0 for hide
                        ivThumb.setVisibility(View.VISIBLE);
                    } else if (getBioMetricVisibleForBranch().equalsIgnoreCase("0")) {
                        ivThumb.setVisibility(View.GONE);
                    }
//                    ivThumb.setVisibility(View.VISIBLE);
//                    ivOTP.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAuthenticationModeMsg(boolean showText, int mode) {
        if (showText) {
            tvAuthenticationMode.setVisibility(View.VISIBLE);
        } else {
            tvAuthenticationMode.setVisibility(View.GONE);
        }
        if (mode == brbaMode) {
            tvAuthenticationMode.setText(getResources().getString(R.string.br_ba_authenticate_msg));
        } else if (mode == dcMode) {
            tvAuthenticationMode.setText(getResources().getString(R.string.dc_authenticate_msg));
        } else {
            tvAuthenticationMode.setText("");
        }
    }

    private void openEmendate(Uri url) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                String packag = CustomTabsHelper.getPackageNameToUse(getContext());
                if (packag == null) {
                    Intent intent = new Intent(mContext, WebActivity.class);
                    intent.putExtra(URL_PATH, url.toString());
                    startActivity(intent);
                } else {
                    openCustomTabsIntent(url.toString());
                }
            } else {
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra(URL_PATH, url.toString());
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadPDF(String resumeLink) {
        try {
//            openWebUrl(Uri.parse(resumeLink));
            openWebUrl(Uri.parse(getDownloadPdfUrl() + EkycUtil.encrypt(resumeLink + "|" + getUserID() + "|" + getTokenKey() + "|" + "")),
                    resumeLink.substring(resumeLink.lastIndexOf("\\") + 1), "PDFDownload");
            /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(resumeLink));
            startActivity(browserIntent);*/
          /*  String nameOfFile = URLUtil.guessFileName(String.valueOf(resumeLink), null,
                    MimeTypeMap.getFileExtensionFromUrl(String.valueOf(resumeLink)));
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(resumeLink));
            request.setTitle(nameOfFile);
            request.setDescription("download");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameOfFile);
            DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);*/


        } catch (Exception ex) {
            ex.printStackTrace();
            commonMsgDialog(mContext, "Some error occurred during download", false, "");
//            MOSLCommon.alertDialog(mContext, 0, "Motilal Oswal", "Some error occurred during download", "OK", true, null);
        }

    }

    public void openWebUrl(Uri url, String fileName, String type) {
        if (type.equalsIgnoreCase("PDFDownload")) {
            Intent intent = new Intent(mContext, WebActivityPDFDownload.class);
            intent.putExtra(URL_PATH, url.toString());
            intent.putExtra(FILE_NAME, fileName);
            startActivity(intent);
        } else if (type.equalsIgnoreCase("CustomTabs")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                String packag = CustomTabsHelper.getPackageNameToUse(getContext());
                if (packag == null) {
//                    openWebview(null, url);
                    Intent intent = new Intent(mContext, WebActivity.class);
                    intent.putExtra(URL_PATH, url.toString());
                    startActivity(intent);
                } else {
                    openCustomTabsIntent(url.toString());
                }
            } else {
                openInBrowser(url);
            }
        }
    }

    private void openCustomTabsIntent(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
        builder.setToolbarColor(getResources().getColor(R.color.secondaryBackground));
        builder.setCloseButtonIcon(
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_back_arrow));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
    }

    private CustomTabsSession getSession() {
        return mClient.newSession(new CustomTabsCallback() {
            @Override
            public void onNavigationEvent(int navigationEvent, Bundle extras) {
                super.onNavigationEvent(navigationEvent, extras);
                //   getPaymentStatus();
            }
        });
    }

    public void openInBrowser(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void generateClientCodeBRPP(String clientCode) {
        Map inputMap = new HashMap<>();
        inputMap.put("Userid", CommonModel.getUserID());
        inputMap.put("ClientCode", clientCode);


        String flag = "";
        if (getInterestedInMF()) {
            flag = BRBAOPENACCOUNTMF;
        } else {
            flag = BRBAOPENACCOUNT;
        }
        String category = ESIGN;


        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (response != null) {
                            String clientCode = (response.isNull("ClientCode") || response.getString("ClientCode").equals("")) ? "" : response.getString("ClientCode");
                            String message = (response.isNull("Message") || response.getString("Message").equals("")) ? "" : response.getString("Message");
                            String isSuccess = (response.isNull("isSuccess") || response.getString("isSuccess").equals("")) ? "" : response.getString("isSuccess");

                            if (isSuccess.equalsIgnoreCase("n")) {
                                // failure
                                informationDialog(message, 1, false);
                                if (!isBlank(clientCode))
                                    editClienCode.setText(clientCode);

//                                etOTP.setEnabled(true);
                            } else if (isSuccess.equalsIgnoreCase("y")) {
                                //success
                                // editClienCode.setText(clientCode);
                                informationDialog(message, 2, true);
                            } else {
                                commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        customProgress.setVisibility(View.GONE);
                    }
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
//                    MOSLCommon.showToastMsg(mContext, responseString);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });

        } else {
            MOSLCommon.alertDInternet(mContext);
        }
    }

    private void generateClientCodeForDirect() {
        Map inputMap = new HashMap<>();
        inputMap.put("Userid", CommonModel.getUserID());
        inputMap.put("ClientCode", "");
        String flag = "";
        if (getInterestedInMF()) {
            flag = DIRECTOPENACCOUNTMF;
        } else {
            flag = DIRECTOPENACCOUNT;
        }
        String category = ESIGN;
        customProgress.setVisibility(View.VISIBLE);

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (response != null) {
                            String clientCode = (response.isNull("ClientCode") || response.getString("ClientCode").equals("")) ? "" : response.getString("ClientCode");
                            String message = (response.isNull("Message") || response.getString("Message").equals("")) ? "" : response.getString("Message");
                            String isSuccess = (response.isNull("isSuccess") || response.getString("isSuccess").equals("")) ? "" : response.getString("isSuccess");

                            if (isSuccess.equalsIgnoreCase("n")) {
                                informationDialog(message, 1, false);
                            } else if (isSuccess.equalsIgnoreCase("y")) {
                                if (getInterestedInMF() && !getIsCVLVerified() && !getIsAadharVerified()) {
                                    informationDialog(MF_NOCVL_NOAADHAR_CONGRATULATION_MSG, 2, true);
                                } else {
                                    Intent intent1 = new Intent(mContext, DirectResumeApplicationActivity.class);
                                    startActivity(intent1);
                                    getActivity().finish();
                                    openEmendate(Uri.parse("https://www.motilaloswal.com/open-an-account/congratulations.html"));
                                    //informationDialog(message, 2, true);
                                }

                            } else {
                                commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                            }
                        }

                        customProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        customProgress.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
//                    MOSLCommon.showToastMsg(mContext, responseString);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });

        } else {
            customProgress.setVisibility(View.GONE);
            MOSLCommon.alertDInternet(mContext);
        }
    }

    public void performEsign() {
        Map inputMap = new HashMap<>();
        String mode = "", deviceOROTPValue = "";

        mode = OTP;
        deviceOROTPValue = etOTP.getText().toString();


        inputMap.put("Userid", CommonModel.getUserID());
        inputMap.put("Mode", mode);
        inputMap.put("DeviceOROTPValue", deviceOROTPValue);
        inputMap.put("AadharNumber", CommonModel.getAadharNo());
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
        inputMap.put("IRISAuthXML", "");
        inputMap.put("IRISTimestamp", "");
        inputMap.put("IRISisPreverified", "true");
        inputMap.put("IRISkycResXML", "");
        inputMap.put("UDC", getDeviceID(mContext, mContext.getContentResolver()));
        inputMap.put("IPAddress", getLocalIpAddress());
        inputMap.put("TransactionID", transactionId);
        String flag = "";
        if (getInterestedInMF()) {
            flag = PerformeSignMF;
        } else {

            flag = PERFORMESIGN;

        }
        String category = ESIGN;


        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (response != null) {
                            esignErrorMsg = (response.isNull("ErrorMessage") || response.getString("ErrorMessage").equals("")) ? "" : response.getString("ErrorMessage");
                            String errorCode = (response.isNull("ErrorCode") || response.getString("ErrorCode").equals("")) ? "" : response.getString("ErrorCode");
                            String returnStatus = (response.isNull("ReturnStatus") || response.getString("ReturnStatus").equals("")) ? "" : response.getString("ReturnStatus");
                            String eSignPDFURL = (response.isNull("eSignPDFURL") || response.getString("eSignPDFURL").equals("")) ? "" : response.getString("eSignPDFURL");

                            pdfAdapter.setPostPdf(eSignPDFURL);
                            setListViewHeightBasedOnChildren(pdfListView);

                            if (returnStatus.equalsIgnoreCase("success")) {

                                commonMsgDialog(mContext, ESIGN_SUCCESSFUL, false, "");

                                //llPostEKYC.setVisibility(View.VISIBLE);
                                llOTPThumbLayout.setVisibility(View.GONE);
                                //eSignPDFURL = eSignPDFURL.replaceAll(" ", "%20");
                                if (getAppType().equals(CRM) || getAppType().equals(PP)) {
                                    getAvailableClientCode();
                                } else {
                                    showAuthenticationModeMsg(false, noMode);
                                    llParentOTPBio.setVisibility(View.GONE);
                                    btnSubmit.setVisibility(View.VISIBLE);
                                }
                            } else if (returnStatus.equalsIgnoreCase("")) {
                                commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                            } else {
                                informationDialog(esignErrorMsg, 1, false);
                                //llPostEKYC.setVisibility(View.GONE);
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        customProgress.setVisibility(View.GONE);
                    }
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);

                    customProgress.setVisibility(View.GONE);
                    //MOSLCommon.showToastMsg(mContext, responseString);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
//                    MOSLCommon.alertDialog(mContext, 0, "Oops", SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    //MOSLCommon.showToastMsg(mContext, responseString);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });

        } else {
            MOSLCommon.alertDInternet(mContext);
        }
    }

    public void genrateEsignXML(final int mode) {
        Map inputMap = new HashMap<>();
        inputMap.put("Userid", CommonModel.getUserID());
        inputMap.put("Mode", String.valueOf(mode));// 1=OTP,2=Finger,3=Iris
        nsdlMode = String.valueOf(mode);

        String flag = PERFORM_NSDL_ESIGN;
        String category = ESIGN;

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (response != null) {
                            String errorCode = (response.isNull("ErrorCode") || response.getString("ErrorCode").equals("")) ? "" : response.getString("ErrorCode");
                            String errorMessage = (response.isNull("ErrorMessage") || response.getString("ErrorMessage").equals("")) ? "" : response.getString("ErrorMessage");
                            String returnStatus = (response.isNull("ReturnStatus") || response.getString("ReturnStatus").equals("")) ? "" : response.getString("ReturnStatus");
                            String eSignPDFURL = (response.isNull("eSignPDFURL") || response.getString("eSignPDFURL").equals("")) ? "" : response.getString("eSignPDFURL");
                            String xmlResponse = (response.isNull("Response") || response.getString("Response").equals("")) ? "" : response.getString("Response");
                            requestId = (response.isNull("RequestId") || response.getString("RequestId").equals("")) ? "" : response.getString("RequestId");
//                            xmlResponse = xmlResponse.replace("//", "");
                            if (isBlank(returnStatus) || returnStatus.equalsIgnoreCase("")) {
                                commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                            } else if (returnStatus.equalsIgnoreCase("1")) {
                                if (isBlank(xmlResponse)) {
                                    commonMsgDialog(mContext, DATA_IS_MISSING, false, "");
                                } else {
                                    try {
                                        Intent appStartIntent = new Intent(getContext(), NsdlEsignActivity.class);
                                        appStartIntent.setAction(ACTION_ESIGNRESPONSE);
                                        appStartIntent.putExtra("msg", xmlResponse); // msg contains esign request xml from ASP.
                                        appStartIntent.putExtra("env", WebServiceHandler.UID_ENV_NSDL); //Possible values PREPROD or PROD (case sensitive).
                                        appStartIntent.putExtra("returnUrl", "com.mosl.mobile.ekyc.ui"); // your package name where esign response failure/success will be sent.
                                        if (mode == 3) {
                                            appStartIntent.putExtra("licenseKey", MOSLConstants.KEY);
                                        } // your package name where esign response failure/success will be sent.
                                        startActivityForResult(appStartIntent, REQUEST_CODE);
                                    } catch (ActivityNotFoundException e) {
                                        e.printStackTrace();
                                        CustomDialog dialogMsg = new CustomDialog(mContext, getResources().getString(R.string.download_nsdl_apk), true, false, false, "", "Download", "Cancel", new CustomDialog.CustomDialogListener() {
                                            @Override
                                            public void dialogAction(MOSLCommon.Action action, Object... data) {
                                                if (action == MOSLCommon.Action.OK) {
                                                    Intent intent = new Intent(mContext, WebActivityPDFDownload.class);
                                                    intent.putExtra(URL_PATH, " https://www.motilaloswal.com/open-an-account/NSDL_eGov_ESP.apk");
                                                    intent.putExtra(FILE_NAME, "NSDL_eGov_ESP");
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                        dialogMsg.show();
                                    }
                                }
                            } else {
                                commonMsgDialog(mContext, errorMessage, false, "");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        customProgress.setVisibility(View.GONE);
                    }
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });

        } else {
            MOSLCommon.alertDInternet(mContext);
        }
    }

    public void esignXMLResponse(String eSignResponse) {
        Map inputMap = new HashMap<>();
        inputMap.put("Userid", CommonModel.getUserID());
        inputMap.put("RequestId", requestId);
        inputMap.put("ResponseXML", eSignResponse);
        inputMap.put("SourceMode", "Mobile");
        inputMap.put("Mode", nsdlMode);

        String flag = PERFORM_NSDL_ESIGN_RESPONSE;
        String category = ESIGN;
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (response != null) {
                            String returnStatus = (response.isNull("ReturnStatus") || response.getString("ReturnStatus").equals("")) ? "" : response.getString("ReturnStatus");
                            String eSignPDFURL = (response.isNull("eSignPDFURL") || response.getString("eSignPDFURL").equals("")) ? "" : response.getString("eSignPDFURL");
                            esignErrorMsg = (response.isNull("Message") || response.getString("Message").equals("")) ? "" : response.getString("Message");
                            pdfAdapter.setPostPdf(eSignPDFURL);
                            setListViewHeightBasedOnChildren(pdfListView);

                            if (returnStatus.equalsIgnoreCase("1")) {
                                commonMsgDialog(mContext, ESIGN_SUCCESSFUL, false, "");
                                llOTPThumbLayout.setVisibility(View.GONE);

                                if (getAppType().equals(CRM) || getAppType().equals(PP)) {
                                    getAvailableClientCode();
                                } else {
                                    showAuthenticationModeMsg(false, noMode);
                                    llParentOTPBio.setVisibility(View.GONE);
                                    btnSubmit.setVisibility(View.VISIBLE);
                                }
                            } else if (returnStatus.equalsIgnoreCase("")) {
                                customProgress.setVisibility(View.GONE);
                                commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                            } else {
                                informationDialog(esignErrorMsg, 1, false);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        customProgress.setVisibility(View.GONE);
                    }
//                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });

        } else {
            MOSLCommon.alertDInternet(mContext);
        }
    }

    private void sendOTPDialog(String msg) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setWindowAnimations(R.style.animationName);
        dialog.setContentView(R.layout.layout_dialog_yes_no_option);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
        Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
        Button btnNO = (Button) dialog.findViewById(R.id.btnNO);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);

        tvMsg.setText(msg);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customProgress.setVisibility(View.GONE);
                llOTPThumbLayout.setVisibility(View.GONE);
                ivOTP.setImageResource(R.drawable.ic_otp_unselect);
                ivThumb.setImageResource(R.drawable.ic_thumb_unselect);
                dialog.dismiss();
            }
        });

        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                customProgress.setVisibility(View.GONE);
                llOTPThumbLayout.setVisibility(View.GONE);
                ivOTP.setImageResource(R.drawable.ic_otp_unselect);
                ivThumb.setImageResource(R.drawable.ic_thumb_unselect);
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                customProgress.setVisibility(View.GONE);
//                generateOTP();

            }
        });

        dialog.show();
    }

    public void generateOTP() {
        Map inputMap = new HashMap<>();
        inputMap.put("Userid", CommonModel.getUserID());
        inputMap.put("AadhaarNumber", CommonModel.getAadharNo());
        inputMap.put("OTPValue", "");
//        inputMap.put("UniqueID", getDeviceID(mContext, mContext.getContentResolver()));
//
//        String flag = ESIGNOTP;
//        String category = ESIGN;
        String flag = GENRATEAADHAROTP;
        String category = PANADDHARDETAILS;


        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (response != null) {
                            String kuaResponseStatus = (response.isNull("KuaResponseStatus") || response.getString("KuaResponseStatus").equals("")) ? "" : response.getString("KuaResponseStatus");
                            String errorMsg = (response.isNull("ErrorMessage") || response.getString("ErrorMessage").equals("")) ? "" : response.getString("ErrorMessage");
                            String errorCode = (response.isNull("ErrorCode") || response.getString("ErrorCode").equals("")) ? "" : response.getString("ErrorCode");
                            String transactionIdAPI = (response.isNull("TransactionID") || response.getString("TransactionID").equals("")) ? "" : response.getString("TransactionID");
//                            String errorMsg = response.getString("ErrorMessage");
//                            String errorCode = response.getString("ErrorCode");
//                            String returnStatus = response.getString("ReturnStatus");
                            if (kuaResponseStatus.equalsIgnoreCase("true")) {
                                transactionId = transactionIdAPI;
                                informationDialog(OTP_SEND_TO_REGISTERED_NO_MSG, 1, false);
                            } else {
                                informationDialog(errorMsg, 2, false);
//                                llPostEKYC.setVisibility(View.VISIBLE);
                            }
                            /*if (returnStatus.equalsIgnoreCase("success")) {
                                informationDialog(OTP_SEND_TO_REGISTERED_NO_MSG, 1, false);
//                                etOTP.setEnabled(true);
                            } else {
                                informationDialog(errorMsg, 2, false);
//                                llPostEKYC.setVisibility(View.VISIBLE);
                            }*/
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        customProgress.setVisibility(View.GONE);
                    }
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
//                    MOSLCommon.showToastMsg(mContext, responseString);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });

        } else {
            MOSLCommon.alertDInternet(mContext);
        }
    }

    public void informationDialog(String msg, final int status, final boolean exitApp) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setWindowAnimations(R.style.animationName);
        dialog.setContentView(R.layout.layout_dialog_info);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
        Button btnOK = (Button) dialog.findViewById(R.id.btnOK);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);

        tvMsg.setText(msg);

        if (exitApp) {
            ivClose.setVisibility(View.GONE);
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customProgress.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status == 1) {
                    dialog.dismiss();
                    if (etOTP.getVisibility() == View.VISIBLE) {
                        etOTP.setText("");
                    }

                    customProgress.setVisibility(View.GONE);
                }
                if (status == 2) {
                    dialog.dismiss();
                    customProgress.setVisibility(View.GONE);
                }
                if (exitApp) {
                    dialog.dismiss();
                    if (getAppType().equals(CRM) || (getAppType().equals(PP) && !isDirectProspect())) {
                        Intent intent = new Intent(mContext, PostLoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Intent intent1 = new Intent(mContext, DirectResumeApplicationActivity.class);
                        startActivity(intent1);
                        getActivity().finish();
                        if (!isOffline() && !isDirectProspect()) {
                            Intent intent = mContext.getPackageManager().getLaunchIntentForPackage("com.mosl.mobile");
                            if (intent != null) {
                                // We found the activity now start the activity
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                // Bring user to the market or let them choose an app?
                                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.mosl.mobile"));
                                startActivity(i);
                            }
                        }
                    }
                }

            }
        });

        dialog.show();
    }

    /////////////////////
    private class PdfModel {
        String product;
        String prePdfPath;
        String postPdfPath;

        public PdfModel(String product, String prePdfPath, String postPdfPath) {
            this.product = product;
            this.prePdfPath = prePdfPath;
            this.postPdfPath = postPdfPath;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public String getPrePdfPath() {
            return prePdfPath;
        }

        public void setPrePdfPath(String prePdfPath) {
            this.prePdfPath = prePdfPath;
        }

        public String getPostPdfPath() {
            return postPdfPath;
        }

        public void setPostPdfPath(String postPdfPath) {
            this.postPdfPath = postPdfPath;
        }
    }

    private class PdfAdapter extends BaseAdapter {
        private final List<PdfModel> data;
        private Context context;

        public PdfAdapter(Context context, List<PdfModel> data) {
            this.context = context;
            this.data = data;
        }

        public void setPostPdf(String postPdfUrl) {
            if (isBlank(postPdfUrl)) {
                return;
            }

            for (PdfModel model : data) {
                if (isBlank(model.getPostPdfPath())) {
                    model.setPostPdfPath(postPdfUrl);
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public PdfModel getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            final PdfModel model = getItem(position);
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (view == null) {
                view = vi.inflate(R.layout.list_item_esing_pdf, null);
                holder.tvProductPre = (TextView) view.findViewById(R.id.tvProductPre);
                holder.tvProductPost = (TextView) view.findViewById(R.id.tvProductPost);
                holder.txtClickHerePre = (TextView) view.findViewById(R.id.txtClickHerePre);
                holder.txtClickHerePost = (TextView) view.findViewById(R.id.txtClickHerePost);

                holder.prePdfLayout = (LinearLayout) view.findViewById(R.id.prePdfLayout);
                holder.postPdfLayout = (LinearLayout) view.findViewById(R.id.postPdfLayout);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.tvProductPre.setText(model.getProduct());
            holder.tvProductPost.setText(model.getProduct());

            if (isBlank(model.getPrePdfPath())) {
                holder.prePdfLayout.setVisibility(View.GONE);
            } else {
                if (model.getProduct().equalsIgnoreCase(EQUITY) && isEquitySegmentExist())
                    holder.prePdfLayout.setVisibility(View.GONE);
                else if (model.getProduct().equalsIgnoreCase(COMMODITY) && isCommoditySegmentExist())
                    holder.prePdfLayout.setVisibility(View.GONE);
                else
                    holder.prePdfLayout.setVisibility(View.VISIBLE);
            }
            if (isBlank(model.getPostPdfPath())) {
                holder.postPdfLayout.setVisibility(View.GONE);
            } else {
                holder.postPdfLayout.setVisibility(View.VISIBLE);
            }

            holder.txtClickHerePre.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              /*MOSLCommon.alertDialog(mContext, 0, MOSLConstants.MOTILAL_OSWAL,
                                                                      "Do you want to download pre-esign kyc form", "YES", "NO", false, new MOSLCommon.DialogListener() {
                                                                          @Override
                                                                          public void alertDialogAction(MOSLCommon.Action action, Object... data) {
                                                                              if (action == MOSLCommon.Action.OK) {
                                                                                  if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                                                                      String prePdfUrl = model.getPrePdfPath().replaceAll(" ", "%20");

                                                                                      downloadPDF(prePdfUrl);
                                                                                  } else {
                                                                                      ((EkycMainActivity) mContext).requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                                                                                  }

                                                                              }*//* else {
                                                                  informationDialog("PDF not available", 2,false);
                                                              }*//*
                                                                          }

                                                                      }

                                                              );*/


                                                              /*CustomDialog prePdfCd = new CustomDialog(mContext, "Do you want to download pre-esign kyc form", true, true, false, "", YES, NO, new CustomDialog.CustomDialogListener() {
                                                                  @Override
                                                                  public void dialogAction(MOSLCommon.Action action, Object... data) {
                                                                      if (action == MOSLCommon.Action.OK) {
                                                                          if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                                                              String prePdfUrl = model.getPrePdfPath().replaceAll(" ", "%20");

                                                                              downloadPDF(prePdfUrl);
                                                                          } else {
                                                                              ((EkycMainActivity) mContext).requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                                                                          }

                                                                      }
                                                                  }
                                                              });
                                                              prePdfCd.show();*/
                                                              if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                                                  String prePdfUrl = model.getPrePdfPath().replaceAll(" ", "%20");

                                                                  downloadPDF(prePdfUrl);
                                                              } else {
                                                                  ((EkycMainActivity) mContext).requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                                                              }

                                                          }
                                                      }

            );

            holder.txtClickHerePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*MOSLCommon.alertDialog(mContext, 0, MOSLConstants.MOTILAL_OSWAL,
                            "Do you want to download post-esign kyc form", "YES", "NO", false, new MOSLCommon.DialogListener() {
                                @Override
                                public void alertDialogAction(MOSLCommon.Action action, Object... data) {
                                    if (action == MOSLCommon.Action.OK) {
                                        if (!model.getPostPdfPath().equals("")) {
                                            if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                                String postPdfUrl = model.getPostPdfPath().replaceAll(" ", "%20");
                                                downloadPDF(postPdfUrl);
                                            } else {
                                                ((EkycMainActivity) mContext).requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

                                            }

                                        } else {
                                            informationDialog("PDF not available", 2, false);
                                        }
                                    }
                                }
                            });*/


                    /*CustomDialog postDownloadCd = new CustomDialog(mContext, "Do you want to download post-esign kyc form", true, true, false, "", YES, NO, new CustomDialog.CustomDialogListener() {
                        @Override
                        public void dialogAction(MOSLCommon.Action action, Object... data) {
                            if (action == MOSLCommon.Action.OK) {
                                if (!model.getPostPdfPath().equals("")) {
                                    if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                        String postPdfUrl = model.getPostPdfPath().replaceAll(" ", "%20");
                                        downloadPDF(postPdfUrl);
                                    } else {
                                        ((EkycMainActivity) mContext).requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

                                    }

                                } else {
                                    informationDialog("PDF not available", 2, false);
                                }
                            }
                        }
                    });
                    postDownloadCd.show();*/

                    if (!model.getPostPdfPath().equals("")) {
                        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            String postPdfUrl = model.getPostPdfPath().replaceAll(" ", "%20");
                            downloadPDF(postPdfUrl);
                        } else {
                            ((EkycMainActivity) mContext).requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                        }
                    } else {
                        informationDialog("PDF not available", 2, false);
                    }
                }
            });
            return view;
        }

        private class ViewHolder {
            public LinearLayout prePdfLayout, postPdfLayout;
            public TextView tvProductPre, tvProductPost;
            public TextView txtClickHerePre, txtClickHerePost;
        }
    }

    public void disableEnableField() {
        if (isThisSecondProductFlow() || isOfflineCliencodeGenrated() || getUserType().equalsIgnoreCase(IFAUSER)) {
            tvClientCodeNote.setVisibility(View.GONE);
            editClienCode.setEnabled(false);
        } else {
            editClienCode.setEnabled(true);
        }
    }

    private void fingerIrisDialog(final String type) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_emendate);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);

        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        final RadioButton rbFingerPrint = (RadioButton) dialog.findViewById(R.id.rbFingerPrint);
        final RadioButton rbIris = (RadioButton) dialog.findViewById(R.id.rbIris);

        if (type.equalsIgnoreCase(ESIGN))
            rbIris.setText("Iris");
        else if (type.equalsIgnoreCase("Emendate"))
            rbIris.setText("OTP");

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equalsIgnoreCase(ESIGN)) {
                    if (rbFingerPrint.isChecked())
                        genrateEsignXML(NSDL_FINGER);
                    else if (rbIris.isChecked())
                        genrateEsignXML(NSDL_IRIS);
                } else if (type.equalsIgnoreCase("Emendate"))
                    if (rbFingerPrint.isChecked())
                        getBankDetails(FINGER);
                    else if (rbIris.isChecked())
                        getBankDetails(OTP);
//                    openEmendate(emendateURL);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getBankDetails(final String mode) {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap();
            inputMap.put("UserID", getUserID());
            inputMap.put("SchemeType", "");

            String flag = FILLBANKDETAILS;
            String category = BANKDETAILS;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null && response.length() != 0) {
                        try {
                            if (response.has("BANKUSERDATA")) {
                                JSONArray jsonArray = response.getJSONArray("BANKUSERDATA");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        bankName = (object.isNull("BANK_NAME") || object.getString("BANK_NAME").equals("")) ? "" : object.getString("BANK_NAME");
                                        bankIfscCode = (object.isNull("BANK_IFSC_CODE") || object.getString("BANK_IFSC_CODE").equals("")) ? "" : object.getString("BANK_IFSC_CODE");
                                        bankAccNo = (object.isNull("BANK_ACC_NO") || object.getString("BANK_ACC_NO").equals("")) ? "" : object.getString("BANK_ACC_NO");
                                        bankAcType = (object.isNull("BANK_AC_TYPE") || object.getString("BANK_AC_TYPE").equals("")) ? "" : object.getString("BANK_AC_TYPE");
                                    }
                                }
                            }
                            if (response.has("BANKUSERDATA1")) {
                                JSONArray jsonArray = response.getJSONArray("BANKUSERDATA1");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        corresMobileNo = (object.isNull("CORRES_MOBILE_NO") || object.getString("CORRES_MOBILE_NO").equals("")) ? "" : object.getString("CORRES_MOBILE_NO");
                                        corresEmailId = (object.isNull("CORRES_EMAIL_ID") || object.getString("CORRES_EMAIL_ID").equals("")) ? "" : object.getString("CORRES_EMAIL_ID");
                                        uid = (object.isNull("UID") || object.getString("UID").equals("")) ? "" : object.getString("UID");
                                        fullName = (object.isNull("FULLNAME") || object.getString("FULLNAME").equals("")) ? "" : object.getString("FULLNAME");
                                    }
                                }
                            }
                            getEmendateRequest(mode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });
        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void getEmendateRequest(final String mode) {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap();
            inputMap.put("CustEmailMobile", corresMobileNo);
            inputMap.put("mandate_creation_date_time", "");
            inputMap.put("BankIfscCode", bankIfscCode);
            inputMap.put("BankName", bankName);
            inputMap.put("aadhaar", uid);
            inputMap.put("BankAccType", bankAcType);
            inputMap.put("BankAccNo", bankAccNo);
            inputMap.put("instrument_type", "debit");
            inputMap.put("customer_name", fullName);
            inputMap.put("maximum_amount", "100000");
            inputMap.put("first_collection_date", "");
            inputMap.put("final_collection_date", "");
            inputMap.put("_USERID", getUserID());
            inputMap.put("customer_mobile", corresMobileNo);
            inputMap.put("customer_email", corresEmailId);
            inputMap.put("application", "Mobile");

            String flag = BANK_EMANDATE;
            String category = BANKDETAILS;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null && response.length() != 0) {
                        try {
                            String id = (response.isNull("id") || response.getString("id").equals("")) ? "" : response.getString("id");
                            String enachType = (response.isNull("enach_type") || response.getString("enach_type").equals("")) ? "" : response.getString("enach_type");
                            String status = (response.isNull("status") || response.getString("status").equals("")) ? "" : response.getString("status");
                            mandateCreationDateTime = (response.isNull("mandate_creation_date_time") || response.getString("mandate_creation_date_time").equals("")) ? "" : response.getString("mandate_creation_date_time");
                            String CustEmailMobile = (response.isNull("CustEmailMobile") || response.getString("CustEmailMobile").equals("")) ? "" : response.getString("CustEmailMobile");
                            String customerEmail = (response.isNull("customer_email") || response.getString("customer_email").equals("")) ? "" : response.getString("customer_email");
                            String aadhaar = (response.isNull("aadhaar") || response.getString("aadhaar").equals("")) ? "" : response.getString("aadhaar");
                            String instrumentType = (response.isNull("instrument_type") || response.getString("instrument_type").equals("")) ? "" : response.getString("instrument_type");
                            String customerName = (response.isNull("customer_name") || response.getString("customer_name").equals("")) ? "" : response.getString("customer_name");
                            firstCollectionDate = (response.isNull("first_collection_date") || response.getString("first_collection_date").equals("")) ? "" : response.getString("first_collection_date");
                            finalCollectionDate = (response.isNull("final_collection_date") || response.getString("final_collection_date").equals("")) ? "" : response.getString("final_collection_date");
                            String logo = (response.isNull("logo") || response.getString("logo").equals("")) ? "" : response.getString("logo");

                            digioDocId = id;
                            emendateMode = mode;
                            if (!isBlank(logo))
                                callEmendate(mode, logo, id, customerEmail, corresMobileNo);
                            customProgress.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });
        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void emendateResponse() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap();
            inputMap.put("_USERID", getUserID());
            inputMap.put("CustEmailMobile", corresMobileNo);
            inputMap.put("mandate_creation_date_time", mandateCreationDateTime);
            inputMap.put("BankIfscCode", bankIfscCode);
            inputMap.put("BankName", bankName);
            inputMap.put("aadhaar", uid);
            inputMap.put("BankAccType", bankAcType);
            inputMap.put("BankAccNo", bankAccNo);
            inputMap.put("instrument_type", "debit");
            inputMap.put("customer_name", fullName);
            inputMap.put("maximum_amount", "100000");
            inputMap.put("first_collection_date", firstCollectionDate);
            inputMap.put("final_collection_date", finalCollectionDate);
            inputMap.put("txn_id", txnId);
            inputMap.put("customer_mobile", corresMobileNo);
            inputMap.put("customer_email", corresEmailId);
            inputMap.put("digio_doc_id", digioDocId);
            inputMap.put("message", digiomessage);
            inputMap.put("error_code", errorCode);
            inputMap.put("last_state_screen", lastStateScreen);
            inputMap.put("last_state_code", lastStateCode);
            inputMap.put("last_state_message", lastStateMessage);
            inputMap.put("eMandateAuthMode", emendateMode);
            inputMap.put("application", "Mobile");

            String flag = BANK_EMANDATE_RESPONSE;
            String category = BANKDETAILS;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null && response.length() != 0) {
                        try {
                            if (response.has("EMANDATERES")) {
                                JSONArray jsonArray = response.getJSONArray("EMANDATERES");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String emandate = (object.isNull("eMANDATE") || object.getString("eMANDATE").equals("")) ? "" : object.getString("eMANDATE");
                                        String eMandateMessage = (object.isNull("eMandateMessage") || object.getString("eMandateMessage").equals("")) ? "" : object.getString("eMandateMessage");

                                        if (!isBlank(eMandateMessage))
                                            commonMsgDialog(mContext, eMandateMessage, false, "");
                                        if (!isBlank(emandate) && emandate.equals("1"))
                                            tvEmendate.setEnabled(false);
                                        else
                                            tvEmendate.setEnabled(true);
                                    }
                                }
                            }

                            customProgress.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });
        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void callEmendate(String mode, String logo, String id, String emailId, String mobile) {
        Digio digio = new Digio();
        DigioConfig digioConfig = new DigioConfig();
//        digioConfig.setLogo(" www.your-website/logo-image "); //Your company logo
        digioConfig.setLogo(logo); //Your company logo
        digioConfig.setEnvironment(UID_ENV.equals("P") ? DigioEnvironment.PRODUCTION : DigioEnvironment.PRODUCTION); //Stage is sandbox
        digioConfig.setServiceMode(mode.equalsIgnoreCase(FINGER) ? DigioServiceMode.FP : DigioServiceMode.OTP); //FP is fingerprint
        try {
            digio.init((EkycMainActivity) mContext, digioConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            digio.esign(id, mobile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDigioData(String txnId, String digiomessage, String errorCode, String lastStateScreen, String lastStateCode, String lastStateMessage) {
        this.txnId = txnId;
        this.digiomessage = digiomessage;
        this.errorCode = errorCode;
        this.lastStateScreen = lastStateScreen;
        this.lastStateCode = lastStateCode;
        this.lastStateMessage = lastStateMessage;
        emendateResponse();
    }
}
