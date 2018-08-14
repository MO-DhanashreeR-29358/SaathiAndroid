package com.mosl.mobile.ekyc.register;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.biometric.ui.BioMetricMainActivity;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.CustomTabsHelper;
import com.mosl.mobile.ekyc.common.EkycAppUtil;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.common.MOSLCommon.Action;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.directmodule.DirectResumeApplicationActivity;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import com.mosl.mobile.ekyc.ui.AddressDetailsActivity;
import com.mosl.mobile.ekyc.ui.CustomDatePickerDialog;
import com.mosl.mobile.ekyc.ui.CustomDialog;
import com.mosl.mobile.ekyc.ui.DatePickerListener;
import com.mosl.mobile.ekyc.ui.EkycMainActivity;
import com.mosl.mobile.ekyc.ui.PostLoginActivity;
import com.mosl.mobile.ekyc.ui.WebActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

import static com.mosl.mobile.ekyc.common.MOSLCommon.callApplyNowActivity;
import static com.mosl.mobile.ekyc.common.MOSLCommon.commonMsgDialog;
import static com.mosl.mobile.ekyc.common.MOSLCommon.getDeviceID;
import static com.mosl.mobile.ekyc.common.MOSLCommon.getLocalIpAddress;
import static com.mosl.mobile.ekyc.common.MOSLCommon.getPrefs;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isBlank;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isThisSecondProductFlow;
import static com.mosl.mobile.ekyc.common.MOSLCommon.removeKeyboard;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.model.CommonModel.getAadharNo;
import static com.mosl.mobile.ekyc.model.CommonModel.getAadharVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getAddressMode;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppType;
import static com.mosl.mobile.ekyc.model.CommonModel.getBaFranchiseeCode;
import static com.mosl.mobile.ekyc.model.CommonModel.getCvlVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getDob;
import static com.mosl.mobile.ekyc.model.CommonModel.getInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsAadharVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsCVLVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getPANName;
import static com.mosl.mobile.ekyc.model.CommonModel.getPANno;
import static com.mosl.mobile.ekyc.model.CommonModel.getRegisteredName;
import static com.mosl.mobile.ekyc.model.CommonModel.getSipIntent;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.isCommodityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.isDirectProspect;
import static com.mosl.mobile.ekyc.model.CommonModel.isEquityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.isFundTransferDone;
import static com.mosl.mobile.ekyc.model.CommonModel.isMfIpvDone;
import static com.mosl.mobile.ekyc.model.CommonModel.isMfSegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.isOffline;
import static com.mosl.mobile.ekyc.model.CommonModel.isOfflineDocumentUploadedDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setAadhaarVerifiedByBio;
import static com.mosl.mobile.ekyc.model.CommonModel.setAadharNo;
import static com.mosl.mobile.ekyc.model.CommonModel.setAddressMode;
import static com.mosl.mobile.ekyc.model.CommonModel.setAppType;
import static com.mosl.mobile.ekyc.model.CommonModel.setBaFranchiseeCode;
import static com.mosl.mobile.ekyc.model.CommonModel.setDob;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsAadharVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCVLVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsDirectProspect;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsEquitySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.setPANName;
import static com.mosl.mobile.ekyc.model.CommonModel.setPANStatus;
import static com.mosl.mobile.ekyc.model.CommonModel.setPANno;
import static com.mosl.mobile.ekyc.model.CommonModel.setSipIntent;
import static com.mosl.mobile.ekyc.model.CommonModel.setUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.setUserType;

/**
 * Created by shefali on 27-06-2016.
 */
public class PANAdharRegistActivity extends Activity implements MOSLConstants {
    public static int PAN_AADHAAR_BIOMETRIC_RESULT_OK = 1011;

    private Button btnSubmit;
    private LinearLayout linearLayout, llOTPThumbLayout, llBaseOTPBioLayout;
    private RelativeLayout rlOTP;
    private CheckBox cbAadhar, cbSIP;
    private EditText etPan, etPanError, etDOB, etAadhaar, etOTP, etCvlOTP, etEnterBaBranchCode;
    private TextView tvDocCheckList, tvResendOTP, tvMobNotMapped, tvOptional, tvAadharNote, tvOTPHeading;
    private SimpleDateFormat dateFormatter;
    //    private DatePickerDialog fromDatePickerDialog;
    private LinearLayout llImgOTP, llImgThumb, llParentOTPBio;
    private ImageView ivThumb1;
    private Dialog dialogDocList, enterBaBranchCodeDialog;
    private LinearLayout customProgress, rlExitForm, rlLogout, llMobileNotMapped, llSIPIntent, llBABranchCode, customProgressBADetails;
    private boolean isPANChanged = false;
    private boolean isAdharChanged = false;
    private Bundle bundle;
    private String userID;
    private String aadharValidationSuccess;
    private ImageView ivExit, ivBack;
    private TextView tvExitForm, tvAppLoginId, tvUserName, tvAuthenticationMode, tvBABranchCode;
    private ImageView ivThumb, ivOTP;
    private boolean otpFlag, panDOBStatus = false, isCVLOPT = false;
    private int noOTPVerified = 0;
    private int cvlOTPVerified = 1;
    private int aadhaarOTPVerified = 2;
    private int OTPVerified = noOTPVerified;//0 = not verified,1= cvl verified, 2 = aadhaar verified

    private String cvlOtp = "", transactionId;
    private String EquitySegment = "EquitySegment";
    private String MFSegment = "MFSegment";
    private String isSegmentMForEquity = "";

    private LinearLayout llAadhaarOtp, llCvlOtp, llAadhar;

    private String source = "";
    private String segment;
    private boolean aadhaarConsentAgreed = false, isSIPChange = false;
    private String submittedValueSIP;
    private ScrollView scrollview;
    private int brbaMode = 1;
    private int dcMode = 2;
    private int noMode = 0;
    private boolean showDefalutDate;
    private boolean isFinalSubmitPANAdhar;
    private String date = "";
    private CheckBox cbAadharDeclaration;
    private TextView tvReadMore, tvAadharConsent, tvAadharEg, tvAadharMsg, tvVirtualIdInfo;
    private boolean openPaperlessAccount = false;
    private RadioButton rbVirtualId, rbAadhar;
    private TextInputLayout input_layout_aadhar;
    private boolean showOTP, showFinger, showIris, showVirtualOTP, showVirtualFinger, showVirtualIris;
    private String authType = AADHAAR;
    private ImageView ivNote;
    private CustomTabsClient mClient;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_pan_adhar_reg);
            setPermisson(PANAdharRegistActivity.this, this);
            bundle = getIntent().getExtras();
            if (bundle != null) {
                source = bundle.getString(SOURCE);
            }
            if (source == null) {
                source = "";
            }
            userID = getUserID();
            if (getInterestedInMF()) {
                segment = "MF";
            } else {
                segment = "";
            }
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

            dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

            btnSubmit = (Button) findViewById(R.id.btnSubmit);
            btnSubmit.setVisibility(View.GONE);
          /*  btnNext = (Button) findViewById(R.id.btnNext);
            btnNext.setVisibility(View.GONE);*/
            scrollview = (ScrollView) findViewById(R.id.scrollview);
            scrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            scrollview.setFocusable(true);
            scrollview.setFocusableInTouchMode(true);
            scrollview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.requestFocusFromTouch();
                    return false;
                }
            });

            linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
            llOTPThumbLayout = (LinearLayout) findViewById(R.id.llOTPThumbLayout);
            llBaseOTPBioLayout = (LinearLayout) findViewById(R.id.llBaseOTPBioLayout);
            customProgress = (LinearLayout) findViewById(R.id.customProgress);
            rlExitForm = (LinearLayout) findViewById(R.id.rlExitForm);
            rlLogout = (LinearLayout) findViewById(R.id.rlLogout);
            llMobileNotMapped = (LinearLayout) findViewById(R.id.llMobileNotMapped);
            llOTPThumbLayout.setVisibility(View.GONE);
            cbAadhar = (CheckBox) findViewById(R.id.cbAadhar);
            llSIPIntent = (LinearLayout) findViewById(R.id.llSIPIntent);
            cbSIP = (CheckBox) findViewById(R.id.cbSIP);
            llBABranchCode = (LinearLayout) findViewById(R.id.llBABranchCode);
            tvBABranchCode = (TextView) findViewById(R.id.tvBABranchCode);

            ivThumb = (ImageView) findViewById(R.id.ivThumb);
            ivOTP = (ImageView) findViewById(R.id.ivOTP);

            llImgOTP = (LinearLayout) findViewById(R.id.llImgOTP);
            llImgThumb = (LinearLayout) findViewById(R.id.llImgThumb);
            llParentOTPBio = (LinearLayout) findViewById(R.id.llParentOTPBio);

            etPan = (EditText) findViewById(R.id.etPan);
            etPanError = (EditText) findViewById(R.id.etPanError);
            etDOB = (EditText) findViewById(R.id.etDOB);
            etAadhaar = (EditText) findViewById(R.id.etAadhaar);
            etOTP = (EditText) findViewById(R.id.etOTP);
            etCvlOTP = (EditText) findViewById(R.id.etCvlOTP);

            ivThumb1 = (ImageView) findViewById(R.id.ivThumb1);
            rlOTP = (RelativeLayout) findViewById(R.id.rlOTP);

            ivExit = (ImageView) findViewById(R.id.ivExit);
            ivBack = (ImageView) findViewById(R.id.ivBack);

            llAadhaarOtp = (LinearLayout) findViewById(R.id.llAadhaarOtp);
            llCvlOtp = (LinearLayout) findViewById(R.id.llCvlOtp);
            tvAuthenticationMode = (TextView) findViewById(R.id.tvAuthenticationMode);

            ivBack.setVisibility(View.GONE);
            //  tvExitForm = (TextView) findViewById(R.id.tvExitForm);
//        tvExitForm.setText("Exit Form");
            tvAppLoginId = (TextView) findViewById(R.id.tvAppLoginId);
            tvUserName = (TextView) findViewById(R.id.tvUserName);
            tvResendOTP = (TextView) findViewById(R.id.tvResendOTP);
            tvDocCheckList = (TextView) findViewById(R.id.tvDocCheckList);
            tvOTPHeading = (TextView) findViewById(R.id.tvOTPHeading);
            tvMobNotMapped = (TextView) findViewById(R.id.tvMobNotMapped);
            tvOptional = (TextView) findViewById(R.id.tvOptional);
            tvAadharNote = (TextView) findViewById(R.id.tvAadharNote);
            tvAadharEg = (TextView) findViewById(R.id.tvAadharEg);
            tvAadharMsg = (TextView) findViewById(R.id.tvAadharMsg);
            tvVirtualIdInfo = (TextView) findViewById(R.id.tvVirtualIdInfo);

            cbAadharDeclaration = (CheckBox) findViewById(R.id.cbAadharDeclaration);
            tvReadMore = (TextView) findViewById(R.id.tvReadMore);
            tvAadharConsent = (TextView) findViewById(R.id.tvAadharConsent);

            ivNote = (ImageView) findViewById(R.id.ivNote);
            rbAadhar = (RadioButton) findViewById(R.id.rbAadhar);
            rbVirtualId = (RadioButton) findViewById(R.id.rbVirtualId);
            llAadhar = (LinearLayout) findViewById(R.id.llAadhar);
            input_layout_aadhar = (TextInputLayout) findViewById(R.id.input_layout_aadhar);
            rbAadhar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rbAadhar.isChecked()) {
                        rbVirtualId.setChecked(false);
                        input_layout_aadhar.setHint("Aadhaar No");
                        etAadhaar.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                        tvAadharEg.setText("988800001111");
                        tvAadharMsg.setText(getResources().getString(R.string.aadharmsg));
                        tvAadharMsg.setTextColor(getResources().getColor(R.color.market_text));
                        authType = AADHAAR;
                        etAadhaar.setText("");
                        if (!(rbVirtualId.getVisibility() == View.VISIBLE))
                            ivNote.setVisibility(View.GONE);
                        if (getAppType().equalsIgnoreCase(DIRECT))
                            tvAadharMsg.setVisibility(View.GONE);
                        else
                            tvAadharMsg.setVisibility(View.VISIBLE);
                    }
                }
            });
            rbVirtualId.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {
                    if (rbVirtualId.isChecked()) {
                        rbAadhar.setChecked(false);
                        input_layout_aadhar.setHint("Aadhaar Virtual Id");
                        etAadhaar.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                        tvAadharEg.setText("9888000011115555");
                        tvAadharMsg.setVisibility(View.VISIBLE);
                        tvAadharMsg.setText(Html.fromHtml(getResources().getString(R.string.vitrualIdmsg)));
                        tvAadharMsg.setTextColor(getResources().getColor(R.drawable.text_color));
                        authType = VIRTUALID;
                        etAadhaar.setText("");
                        ivNote.setVisibility(View.VISIBLE);
                        btnSubmit.setVisibility(View.GONE);
                        if (getAppType().equalsIgnoreCase(DIRECT)) {
                            if (openPaperlessAccount) {
                                tvVirtualIdInfo.setText(Html.fromHtml(
                                        getResources().getString(R.string.open_paperless_account)));
                            } else {
                                tvVirtualIdInfo.setText(Html.fromHtml(
                                        getResources().getString(R.string.unable_to_genrate_virtualid)));
                            }
                            tvVirtualIdInfo.setVisibility(View.VISIBLE);
                        } else {
                            tvVirtualIdInfo.setVisibility(View.GONE);
                        }
                    }
                }
            });

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
                String packag = CustomTabsHelper.getPackageNameToUse(PANAdharRegistActivity.this);
                CustomTabsClient.bindCustomTabsService(PANAdharRegistActivity.this, packag, mConnection);
            } catch (Exception e) {
                e.printStackTrace();
            }

            tvAadharMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rbVirtualId.isChecked()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            String packag = CustomTabsHelper.getPackageNameToUse(PANAdharRegistActivity.this);
                            if (packag == null) {
                                //   openWebview(null, url);
                                Intent intent = new Intent(PANAdharRegistActivity.this, WebActivity.class);
                                intent.putExtra(URL_PATH, MOSLConstants.UIDAI_URL);
                                startActivity(intent);
                            } else {
                                openCustomTabsIntent(MOSLConstants.UIDAI_URL);
                            }
                        } else {
                            openInBrowser(Uri.parse(MOSLConstants.UIDAI_URL));
                        }
                    }
                }
            });

            ivNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomDialog declarationDialog = new CustomDialog(PANAdharRegistActivity.this, getResources().getString(R.string.vitrualIdDefn), true, false, true, "What is Aadhaar Virtual ID?", OK, "", new CustomDialog.CustomDialogListener() {
                        @Override
                        public void dialogAction(MOSLCommon.Action action, Object... data) {

                        }
                    });
                    declarationDialog.show();
                }
            });

            tvVirtualIdInfo.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (openPaperlessAccount) {
                        llAadhar.setVisibility(View.VISIBLE);
                        openPaperlessAccount = false;
                        rbVirtualId.setChecked(true);
                        rbAadhar.setChecked(false);
                        input_layout_aadhar.setHint("Aadhaar Virtual Id");
                        etAadhaar.setText("");
                        btnSubmit.setVisibility(View.GONE);
                        etAadhaar.setFilters(
                                new InputFilter[] { new InputFilter.LengthFilter(16) });
                        tvAadharEg.setText("9888000011115555");
                        tvAadharMsg.setText(
                                Html.fromHtml(getResources().getString(R.string.vitrualIdmsg)));
                        tvAadharMsg.setTextColor(getResources().getColor(R.color.market_text));
                        authType = VIRTUALID;
                        ivNote.setVisibility(View.VISIBLE);
                        tvVirtualIdInfo.setText(Html.fromHtml(
                                getResources().getString(R.string.unable_to_genrate_virtualid)));
                        tvVirtualIdInfo.setVisibility(View.VISIBLE);
                        tvAadharMsg.setVisibility(View.VISIBLE);
                    } else {
                        if (rbAadhar.isChecked())
                            return;
                        CustomDialog dialogSendOTP = new CustomDialog(PANAdharRegistActivity.this,
                                getResources().getString(R.string.paperless_mess_virtualId), true, true,
                                false, "", YES, NO, new CustomDialog.CustomDialogListener() {
                            @Override public void dialogAction(Action action, Object... data) {

                                if (action == Action.OK) {
                                    CustomDialog dialog =
                                            new CustomDialog(PANAdharRegistActivity.this,
                                                    getResources().getString(
                                                            R.string.physical_account_message), true, false,
                                                    false, "", YES, "",
                                                    new CustomDialog.CustomDialogListener() {
                                                        @Override public void dialogAction(Action action,
                                                                                           Object... data) {
                                                            if (action == Action.OK) {
                                                                rbAadhar.setChecked(true);
                                                                rbVirtualId.setChecked(false);
                                                                rbAadhar.setVisibility(View.VISIBLE);
                                                                rbVirtualId.setVisibility(View.GONE);
                                                                input_layout_aadhar.setHint("Aadhaar No");
                                                                etAadhaar.setText("");
                                                                llBaseOTPBioLayout.setVisibility(View.GONE);
                                                                btnSubmit.setVisibility(View.GONE);
                                                                etAadhaar.setFilters(
                                                                        new InputFilter[] { new InputFilter.LengthFilter(12) });
                                                                tvAadharEg.setText("988800001111");
                                                                tvAadharMsg.setText(
                                                                        getResources().getString(R.string.aadharmsg));
                                                                tvAadharMsg.setTextColor(
                                                                        getResources().getColor(R.color.grey_text));
                                                                authType = AADHAAR;
                                                                ivNote.setVisibility(View.GONE);
                                                                tvVirtualIdInfo.setVisibility(View.GONE);
                                                                if (getAppType().equalsIgnoreCase(DIRECT))
                                                                    tvAadharMsg.setVisibility(View.GONE);
                                                                else
                                                                    tvAadharMsg.setVisibility(View.VISIBLE);
                                                            }
                                                        }
                                                    });
                                    dialog.show();
                                }
                            }
                        });
                        dialogSendOTP.show();
                    }
                }
            });

            uidaiApi();
            tvDocCheckList.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            tvResendOTP.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            tvMobNotMapped.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            tvBABranchCode.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

            tvReadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tvAadharConsent.getVisibility() == View.VISIBLE) {
                        tvReadMore.setText(getResources().getString(R.string.read_more));
                        tvAadharConsent.setVisibility(View.GONE);
                    } else {
                        tvReadMore.setText(getResources().getString(R.string.show_less));
                        tvAadharConsent.setVisibility(View.VISIBLE);
                    }
                }
            });

            if (getAppType().equals(DIRECT) || isDirectProspect()) {
                tvAppLoginId.setVisibility(View.GONE);
                tvUserName.setText(getRegisteredName().toUpperCase());
            } else {
                if (!getRegisteredName().equals("")) {
                    tvUserName.setText(getRegisteredName().toUpperCase() + " -");
                }
                tvAppLoginId.setText(getAppLoginID());
            }
            if (getAppType().equals(CRM)) {
                rlLogout.setVisibility(View.VISIBLE);
                llBABranchCode.setVisibility(View.GONE);//BA branch code layout gone for Branch
            } else {
                rlLogout.setVisibility(View.GONE);
                llBABranchCode.setVisibility(View.VISIBLE); //BA branch code layout visible for direct and BA
            }
            //SIP layout visibility code
            if (getAppType().equals(PP)) {
                llSIPIntent.setVisibility(View.GONE);
                if (isBlank(getBaFranchiseeCode())) {//BA branch code layout gone if code is blank
                    llBABranchCode.setVisibility(View.GONE);
                } else {
                    llBABranchCode.setVisibility(View.VISIBLE);
                }
            } else {
                llSIPIntent.setVisibility(View.VISIBLE);
            }

            if (getSipIntent().equals("1")) {
                cbSIP.setChecked(true);
                submittedValueSIP = "1";
            } else if (isBlank(getSipIntent()) || getSipIntent().equals("0")) {
                cbSIP.setChecked(false);
                submittedValueSIP = "0";
            }

            /*
             *Disable SIP
             * 1)second product is it's already checked
             * 2)For first product after fund transfer
             * */
            if ((isThisSecondProductFlow() || isEquityFundTransfered() || isCommodityFundTransfered() ||
                    (isOffline() ? isOfflineDocumentUploadedDone() : isMfIpvDone())) && cbSIP.isChecked()) {
                cbSIP.setEnabled(false);
            } else {
                cbSIP.setEnabled(true);
            }

            /*
             * Disable BABranchCode
             * 1)after fundtrasfer & for MF after IPV
             * 2)second product
             * */
            if (isThisSecondProductFlow() || isEquityFundTransfered() || isCommodityFundTransfered() ||
                    (isOffline() ? isOfflineDocumentUploadedDone() : isMfIpvDone())) {
                tvBABranchCode.setEnabled(false);
            } else {
                tvBABranchCode.setEnabled(true);
            }

            cbSIP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((submittedValueSIP.equals("1") && !cbSIP.isChecked()) || (submittedValueSIP.equals("0") && cbSIP.isChecked())) {
                        isSIPChange = true;
                    } else {
                        isSIPChange = false;
                    }
                    if (cbSIP.isChecked()) {
                        setSipIntent("1");
                    } else {
                        setSipIntent("0");
                    }
                }
            });

            //Call enterBaBranchCodeDialog
            tvBABranchCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enterBranchCodeDialog();
                }
            });

            rlExitForm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    formExit();
                }
            });

            if (getInterestedInMF()) {
                tvAadharNote.setText(getResources().getString(R.string.note_aadhaar_mf));
//                tvOptional.setVisibility(View.VISIBLE);
                cbAadhar.setVisibility(View.GONE);
            } else {
//                cbAadhar.setVisibility(View.VISIBLE);
                tvOptional.setVisibility(View.GONE);
            }

            rlLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPrefs(PANAdharRegistActivity.this).edit().putBoolean("AUTHENTICATED", false).apply();
                    //Util.getPrefs(this).edit().putString("LOGIN_ID", null).apply();
                    getPrefs(PANAdharRegistActivity.this).edit().putString("APP_LOGIN_ID", null).apply();
                    getPrefs(PANAdharRegistActivity.this).edit().putString("PASSWORD", null).apply();
                    getPrefs(PANAdharRegistActivity.this).edit().putString(BOBRANCH, "").apply();
                    getPrefs(PANAdharRegistActivity.this).edit().putString(BOSUBBROKER, "").apply();
                    getPrefs(PANAdharRegistActivity.this).edit().putString(BOTRADERS, "").apply();
                    getPrefs(PANAdharRegistActivity.this).edit().putString(CRMBRANCH, "").apply();
                    getPrefs(PANAdharRegistActivity.this).edit().putString(LOGINUSERID, "").apply();
                    getPrefs(PANAdharRegistActivity.this).edit().putString(ROLEMAPPINGID, "").apply();
                    getPrefs(PANAdharRegistActivity.this).edit().putString(BUSINESSUNITID, "").apply();
                    getPrefs(PANAdharRegistActivity.this).edit().putString(TEAMID, "").apply();
                    getPrefs(PANAdharRegistActivity.this).edit().putString(ROLEID, "").apply();
                /*    Intent intent = new Intent(PANAdharRegistActivity.this, BranchLoginActivity.class);
                    startActivity(intent);*/
                    finish();
                }
            });
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            etPan.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10), new InputFilter.AllCaps()});
            etDOB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (etDOB.getText().toString().equals("")) {
                            showDefalutDate = true;
                            date = "";


                        } else {
                            showDefalutDate = false;
                            date = etDOB.getText().toString();
                            /*CustomDatePickerDialog customDatePickerDialog = new CustomDatePickerDialog(PANAdharRegistActivity.this, false, etDOB.getText().toString(),true, new DatePickerListener() {
                                @Override
                                public void submitDatePicker(String date) {

                                    if (!date.equals("")) {
                                        Date dob = EkycAppUtil.getDate(date, "dd/MM/yyyy");
                                        Calendar newDate = Calendar.getInstance();
                                        newDate.set(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));
                                        Calendar minAdultAge = new GregorianCalendar();
                                        minAdultAge.add(Calendar.YEAR, -18);
                                        if (minAdultAge.before(newDate)) {
                                            //                    MOSLCommon.showToastMsg(PANAdharRegistActivity.this, getResources().getString(R.string.minor_age_bracket_note));
                                            //                    MweOSLCommon.alertDialog(PANAdharRegistActivity.this, 0, "Motilal Oswal", getResources().getString(R.string.minor_age_bracket_note), "OK", true, null);
                                            //                    valididateDialog(getResources().getString(R.string.minor_age_bracket_note), 5);
                                            CustomDialog dialogMinorAge = new CustomDialog(PANAdharRegistActivity.this, getResources().getString(R.string.minor_age_bracket_note), true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                                                @Override
                                                public void dialogAction(Action action, Object... data) {

                                                }
                                            });
                                            dialogMinorAge.show();
                                        } else {
                                            etDOB.setText(date);
                                            if (!source.equals(ADD_SEGMENT)) {
                                                fetchAddFromPAN();
                                            }
                                        }
                                    }
                                }
                            });
                            customDatePickerDialog.show();*/
                        }

                        Calendar maxCal = Calendar.getInstance();
                        maxCal.set(Calendar.YEAR, maxCal.get(Calendar.YEAR) - 18);

                        CustomDatePickerDialog customDatePickerDialog = new CustomDatePickerDialog(PANAdharRegistActivity.this, showDefalutDate, date, maxCal.getTime(), new DatePickerListener() {
                            @Override
                            public void submitDatePicker(String date) {

                                if (!date.equals("")) {
                                    Date dob = EkycAppUtil.getDate(date, "dd/MM/yyyy");
                                    Calendar newDate = Calendar.getInstance();
                                    newDate.set(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));
                                    Calendar minAdultAge = new GregorianCalendar();
                                    minAdultAge.add(Calendar.YEAR, -18);
                                    if (minAdultAge.before(newDate)) {
                                        //                    MOSLCommon.showToastMsg(PANAdharRegistActivity.this, getResources().getString(R.string.minor_age_bracket_note));
                                        //                    MweOSLCommon.alertDialog(PANAdharRegistActivity.this, 0, "Motilal Oswal", getResources().getString(R.string.minor_age_bracket_note), "OK", true, null);
                                        //                    valididateDialog(getResources().getString(R.string.minor_age_bracket_note), 5);
                                        etDOB.setText("");
                                        etAadhaar.setEnabled(false);
                                        etAadhaar.clearFocus();
                                        CustomDialog dialogMinorAge = new CustomDialog(PANAdharRegistActivity.this, getResources().getString(R.string.minor_age_bracket_note), true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                                            @Override
                                            public void dialogAction(Action action, Object... data) {

                                            }
                                        });
                                        dialogMinorAge.show();
                                    } else {
                                        etDOB.setText(date);
                                        if ((!source.equals(ADD_SEGMENT) && !source.equals(BACK_FROM_PREVIOUS_SCREEN)) ||
                                                (etDOB.isEnabled() && source.equals(BACK_FROM_PREVIOUS_SCREEN)) || (source.equals(ADD_SEGMENT))) {
                                            fetchAddFromPAN();
                                        }
                                    }
                                }
                            }
                        });
                        customDatePickerDialog.show();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }




                    /*try {
                        if (!isBlank(etDOB.getText().toString())) {
                            Date dob = EkycAppUtil.getDate(etDOB.getText().toString(), "dd/MM/yyyy");
                            fromDatePickerDialog.updateDate(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    fromDatePickerDialog.show();*/
                    removeKeyboard(PANAdharRegistActivity.this, etPan);
                }
            });
            etDOB.setInputType(InputType.TYPE_NULL);
//        etDOB.setBackgroundColor(getResources().getColor(R.color.grid_background));
            etDOB.setEnabled(false);
            etAadhaar.setEnabled(false);
            cbAadhar.setEnabled(false);
            btnSubmit.setVisibility(View.GONE);
            //        etDOB.requestFocus();

            etPan.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String strPAN = etPan.getText().toString();

                    if (strPAN.length() == 10) {
                        if (isValidPannumber(strPAN)) {
                            if (!source.equals(ADD_SEGMENT) && !source.equals(BACK_FROM_PREVIOUS_SCREEN)) {
                                validatepan(strPAN, userID);
                                etPanError.setVisibility(View.GONE);
                            } else if (source.equals(BACK_FROM_PREVIOUS_SCREEN) && isPANChanged) {
                                validatepan(strPAN, userID);
                                etPanError.setVisibility(View.GONE);
                            }
                            // removeKeyboard(PANAdharRegistActivity.this, etPan);
                        } else {
                            isPANChanged = true;
                            etPanError.setVisibility(View.VISIBLE);
                            etPanError.setText(getResources().getString(R.string.pan_invalid));
                            //   etDOB.setEnabled(false);
                            etAadhaar.setEnabled(false);
                            cbAadhar.setEnabled(false);
                            etDOB.setText("");
                            etAadhaar.setText("");
                            btnSubmit.setVisibility(View.GONE);
                        }
                    } else if (strPAN.length() < 10) {
                        isPANChanged = true;
                        etDOB.setEnabled(false);
                        etAadhaar.setEnabled(false);
                        cbAadhar.setEnabled(false);
                        etDOB.setText("");
                        etAadhaar.setText("");
                        btnSubmit.setVisibility(View.GONE);
                    } else if (strPAN.length() > 10) {
                        isPANChanged = true;
                        etPanError.setVisibility(View.VISIBLE);
                        etPanError.setText(getResources().getString(R.string.pan_invalid));
                        etDOB.setEnabled(false);
                        etAadhaar.setEnabled(false);
                        cbAadhar.setEnabled(false);
                        etDOB.setText("");
                        etAadhaar.setText("");
                        btnSubmit.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            // etDOB.addTextChangedListener(this);
            etAadhaar.addTextChangedListener(new TextWatcher() {


                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String strAadhar = etAadhaar.getText().toString();
                    if (isBlank(getPANno())) {
                        return;
                    }
                    if (strAadhar.length() != 0 && !cbAadharDeclaration.isChecked()) {
                        commonMsgDialog(PANAdharRegistActivity.this, "Please select Aadhar declaration", false, "");
                        return;
                    }
                    etOTP.setText("");
                    etCvlOTP.setText("");
                    setAadhaarVerifiedByBio(false);
                    if (getInterestedInMF()) {
                        if (rbAadhar.isChecked() ? (strAadhar.length() < 12 && strAadhar.length() != 0) : (strAadhar.length() < 16 && strAadhar.length() != 0)) {
                            llOTPThumbLayout.setVisibility(View.GONE);
                            llAadhaarOtp.setVisibility(View.GONE);
                            llCvlOtp.setVisibility(View.GONE);
                            llParentOTPBio.setVisibility(View.GONE);

                            showAuthenticationModeMsg(false, noMode);
                            llBaseOTPBioLayout.setVisibility(View.GONE);
                            btnSubmit.setVisibility(View.GONE);
                            tvDocCheckList.setVisibility(View.VISIBLE);
                            aadhaarConsentAgreed = false;
                            OTPVerified = noOTPVerified;
                            isSegmentMForEquity = "";
                            setIsAadharVerified(false);
                            isAdharChanged = true;
                        } else if (rbAadhar.isChecked() ? (cbAadharDeclaration.isChecked() && strAadhar.length() == 12) :
                                (cbAadharDeclaration.isChecked() && strAadhar.length() == 16)) {
                            if ((!source.equals(ADD_SEGMENT) && !source.equals(BACK_FROM_PREVIOUS_SCREEN)) ||
                                    (source.equalsIgnoreCase(BACK_FROM_PREVIOUS_SCREEN) && isAdharChanged) || (source.equals(ADD_SEGMENT))) {
                                validateAadharAvailability();
                            }
                            isAdharChanged = true;
                            tvDocCheckList.setVisibility(View.VISIBLE);
                        }
                    } else {

                        if (rbAadhar.isChecked() ? strAadhar.length() != 12 : strAadhar.length() != 16) {
                            aadhaarConsentAgreed = false;
                            isAdharChanged = true;
                            llBaseOTPBioLayout.setVisibility(View.GONE);
                            showAuthenticationModeMsg(false, noMode);
                            llParentOTPBio.setVisibility(View.GONE);
                            btnSubmit.setVisibility(View.GONE);
                            llOTPThumbLayout.setVisibility(View.GONE);
                            if (getAppType().equals(DIRECT)) {
                                if (cbAadhar.isChecked()) {
                                    tvDocCheckList.setVisibility(View.GONE);
                                    if (isValidPannumber(etPan.getText().toString()) && etDOB.getText().toString().length() == 10) {
                                        setIsAadharVerified(false);
                                    } else {
                                        btnSubmit.setVisibility(View.GONE);
                                    }
                                } else {
                                    btnSubmit.setVisibility(View.GONE);
                                }
                            }
                        } else if (rbAadhar.isChecked() ? (cbAadharDeclaration.isChecked() && strAadhar.length() == 12) :
                                (cbAadharDeclaration.isChecked() && strAadhar.length() == 16)) {
                            tvDocCheckList.setVisibility(View.VISIBLE);

                            if ((!source.equals(ADD_SEGMENT) && !source.equals(BACK_FROM_PREVIOUS_SCREEN)) ||
                                    (source.equalsIgnoreCase(BACK_FROM_PREVIOUS_SCREEN) && isAdharChanged) || (source.equals(ADD_SEGMENT))) {
                                validateAadharAvailability();
                            }

                        }
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

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

                        fetchAadharDetailsFromOTP(etOTP.getText().toString());
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etCvlOTP.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String strOTP = etCvlOTP.getText().toString();
                    if (OTPVerified == 1) {
                        if (strOTP.length() == 4) {
                            if (cvlOtp.equals(etCvlOTP.getText().toString())) {
                                tvDocCheckList.setVisibility(View.VISIBLE);
                                llBaseOTPBioLayout.setVisibility(View.GONE);
                                llOTPThumbLayout.setVisibility(View.GONE);
                                CustomDialog resendCd = new CustomDialog(PANAdharRegistActivity.this, USER_AUTHENTICATION_SUCCESSFUL, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                                    @Override
                                    public void dialogAction(MOSLCommon.Action action, Object... data) {
                                        if (action == MOSLCommon.Action.OK) {
                                            submitPANAadhar(true);
                                        }
                                    }
                                });
                                resendCd.show();
//                                commonMsgDialog(PANAdharRegistActivity.this, USER_AUTHENTICATION_SUCCESSFUL, false, "");
//                                submitPANAadhar(true);
//                                btnSubmit.setVisibility(View.VISIBLE);
                            } else {
                                commonMsgDialog(PANAdharRegistActivity.this, ENTER_CORRECT_OTP, false, "");
                            }

                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            ivOTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                    /*llOTPThumbLayout.setVisibility(View.VISIBLE);
                    rlOTP.setVisibility(View.VISIBLE);*/
                        //ivThumb1.setVisibility(View.GONE);
                        ivOTP.setImageResource(R.drawable.ic_otp_select);
                        ivThumb.setImageResource(R.drawable.ic_thumb_unselect);
                        otpFlag = true;
                        etOTP.setText("");
                        //                sendOTPDialog(SEND_OTP);
                        setAadharNo(etAadhaar.getText().toString());
                        generateOTP(etAadhaar.getText().toString());
                    /*CustomDialog dialogSendOTP = new CustomDialog(PANAdharRegistActivity.this, SEND_OTP, true, true, false, "", YES, NO, new CustomDialog.CustomDialogListener() {
                        @Override
                        public void dialogAction(Action action, Object... data) {

                            if (action == Action.CANCEL || action == Action.NO) {

                                llOTPThumbLayout.setVisibility(View.GONE);
                                ivOTP.setImageResource(R.drawable.ic_otp_unselect);
                                ivThumb.setImageResource(R.drawable.ic_thumb_unselect);
                            } else {
                                setAadharNo(etAadhaar.getText().toString());
                                generateOTP(etAadhaar.getText().toString());
                            }
                            customProgress.setVisibility(View.GONE);

                        }
                    });
                    dialogSendOTP.show();*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            ivThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        llOTPThumbLayout.setVisibility(View.GONE);
                        etOTP.setText("");
                        rlOTP.setVisibility(View.GONE);
                        // ivThumb1.setVisibility(View.VISIBLE);
                        ivOTP.setImageResource(R.drawable.ic_otp_unselect);
                        ivThumb.setImageResource(R.drawable.ic_thumb_select);
                        otpFlag = false;

                        /*if (!isIrisSupported(PANAdharRegistActivity.this)) {
                            ivOTP.setImageResource(R.drawable.ic_otp_unselect);
                            ivThumb.setImageResource(R.drawable.ic_thumb_unselect);
                            return;
                        }*/
                        Intent intent = new Intent(PANAdharRegistActivity.this, BioMetricMainActivity.class);
                        intent.putExtra(USER_ID, userID);
                        intent.putExtra(AADHAR_NUMBER, getAadharNo());
                        intent.putExtra(SOURCE, PAN_AADHAR_ACTIVITY);
                        if (rbAadhar.isChecked()) {
                            intent.putExtra(FLAG, showIris);
                        } else if (rbVirtualId.isChecked()) {
                            intent.putExtra(FLAG, showVirtualFinger);
                        }
                        startActivityForResult(intent, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            cbAadhar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        tvDocCheckList.setVisibility(View.GONE);
                        if (getAppType().equals(DIRECT)) {
                            setIsAadharVerified(false);
                            if (isValidPannumber(etPan.getText().toString()) && etDOB.getText().toString().length() == 10) {

                                CustomDialog cd = new CustomDialog(PANAdharRegistActivity.this, getResources().getString(R.string.dc_no_aadhar), true, true, false, "", YES, NO, new CustomDialog.CustomDialogListener() {
                                    @Override
                                    public void dialogAction(Action action, Object... data) {
                                        if (action == Action.OK) {
                                            setAadharNo("0");
                                            submitPANAadhar(true);
                                            cbAadhar.setChecked(false);
                                        } else {
                                            cbAadhar.setChecked(false);
                                        }
                                    }
                                });
                                cd.show();
                                //btnSubmit.setVisibility(View.VISIBLE);
                                etAadhaar.setText("");
                                etAadhaar.setEnabled(false);
                            } else {
                                etAadhaar.setEnabled(false);
                                btnSubmit.setVisibility(View.GONE);
                            }

                        } else {
                            etAadhaar.setText("");
                            etAadhaar.setEnabled(false);
                            llParentOTPBio.setVisibility(View.GONE);
                            showAuthenticationModeMsg(false, noMode);
                            btnSubmit.setVisibility(View.GONE);
                            dialogDocList = new Dialog(PANAdharRegistActivity.this);
                            dialogDocList.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialogDocList.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogDocList.setCanceledOnTouchOutside(false);

                            dialogDocList.getWindow().setWindowAnimations(R.style.animationName);
                            dialogDocList.setContentView(R.layout.dialog_doc_list);
                            dialogDocList.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            dialogDocList.setCanceledOnTouchOutside(false);

                            Button btnAgree = (Button) dialogDocList.findViewById(R.id.btnAgree);
                            TextView tvHeading = (TextView) dialogDocList.findViewById(R.id.tvHeading);
                            TextView tvHeading1 = (TextView) dialogDocList.findViewById(R.id.tvHeading1);

                            tvHeading1.setText(getResources().getString(R.string.no_aadhar));
                            tvHeading.setText(getRegisteredName());

                            btnAgree.setOnClickListener(new Button.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    dialogDocList.dismiss();
                                    cbAadhar.setChecked(false);
                                }
                            });

                            ImageView btnClose = (ImageView) dialogDocList.findViewById(R.id.btnClose);
                            btnClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogDocList.dismiss();
                                    cbAadhar.setChecked(false);
                                }
                            });
                            dialogDocList.show();
                        }
                    } else {
                        if (!isBlank(etDOB.getText().toString())) {
                            //  etDOB.setEnabled(false);
                            setIsAadharVerified(false);
//                            if (getInterestedInMF()) {
                            etAadhaar.setEnabled(true);
//                            }
                            tvDocCheckList.setVisibility(View.VISIBLE);
                            btnSubmit.setVisibility(View.GONE);
                        }
                    }
                }
            });

            tvDocCheckList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDocList = new Dialog(PANAdharRegistActivity.this);
                    dialogDocList.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogDocList.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    dialogDocList.getWindow().setWindowAnimations(R.style.animationName);
                    dialogDocList.setContentView(R.layout.dialog_doc_list);
                    dialogDocList.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialogDocList.setCanceledOnTouchOutside(false);

                    Button btnAgree = (Button) dialogDocList.findViewById(R.id.btnAgree);
                    btnAgree.setOnClickListener(new Button.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            dialogDocList.dismiss();

                        }
                    });


                    ImageView btnClose = (ImageView) dialogDocList.findViewById(R.id.btnClose);
                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogDocList.dismiss();
                        }
                    });

                    dialogDocList.show();
                }
            });
            tvMobNotMapped.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAppType().equals(DIRECT)) {
                        CustomDialog cd = new CustomDialog(PANAdharRegistActivity.this, getResources().getString(R.string.mob_not_mapped_dc), true, true, false, "", YES, NO, new CustomDialog.CustomDialogListener() {
                            @Override
                            public void dialogAction(Action action, Object... data) {
                                if (action == Action.OK) {
//                                    setAadharNo("0");
                                    submitPANAadhar(true);
                                }
                            }
                        });
                        cd.show();

                    } else {
                        MOSLCommon.showPopUpThreeLine(PANAdharRegistActivity.this, tvMobNotMapped, getResources().getString(R.string.mob_not_mapped), "null", false, "", "");
                    }
                }
            });
            //Calendar newCalendar = Calendar.getInstance();

           /* fromDatePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    Calendar minAdultAge = new GregorianCalendar();
                    minAdultAge.add(Calendar.YEAR, -18);
                    if (minAdultAge.before(newDate)) {
                        //                    MOSLCommon.showToastMsg(PANAdharRegistActivity.this, getResources().getString(R.string.minor_age_bracket_note));
                        //                    MweOSLCommon.alertDialog(PANAdharRegistActivity.this, 0, "Motilal Oswal", getResources().getString(R.string.minor_age_bracket_note), "OK", true, null);
                        //                    valididateDialog(getResources().getString(R.string.minor_age_bracket_note), 5);
                        CustomDialog dialogMinorAge = new CustomDialog(PANAdharRegistActivity.this, getResources().getString(R.string.minor_age_bracket_note), true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                            @Override
                            public void dialogAction(Action action, Object... data) {

                            }
                        });
                        dialogMinorAge.show();
                    } else {
                        etDOB.setText(dateFormatter.format(newDate.getTime()));
                        if (!source.equals(ADD_SEGMENT)) {
                            fetchAddFromPAN();
                        }
                    }
                }
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());*/

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAppType().equals(PP)) {
                        setSipIntent("0");
                        submitData();
                    } else {
                        if (cbSIP.isChecked()) {
                            setSipIntent("1");
                            submitData();
                        } else {
                            sipDialog();
                        }
                    }
                }
            });

            tvResendOTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //                                               sendOTPDialog("Resend OTP ?");
                    CustomDialog dialogSendOTP = new CustomDialog(PANAdharRegistActivity.this, RESEND_OTP_MSG, true, true, false, "", YES, NO, new CustomDialog.CustomDialogListener() {
                        @Override
                        public void dialogAction(Action action, Object... data) {

                            if (action == Action.CANCEL || action == Action.NO) {

                                llOTPThumbLayout.setVisibility(View.GONE);

                            } else {
                                if (getInterestedInMF() && getIsCVLVerified() && (getAppType().equals(PP) || getAppType().equals(CRM))) {
                                    cvlOTPApi();

                                } else {
                                    setAadharNo(etAadhaar.getText().toString());
                                    generateOTP(etAadhaar.getText().toString());
                                }
                            }
                            customProgress.setVisibility(View.GONE);
                        }
                    });
                    dialogSendOTP.show();
                }
            });

            if (source.equals(ADD_SEGMENT)) {
                isPANChanged = true;
                panDOBStatus = true;
                etPan.setText(getPANno());
                etDOB.setText(getDob());
                if (isBlank(getAadharNo())) {
                    etAadhaar.setEnabled(true);
                    cbAadhar.setEnabled(true);
                } else if (getAadharNo().equalsIgnoreCase("0")) {
                    etAadhaar.setEnabled(true);
                    cbAadhar.setEnabled(true);
                } else {
                    if (!isMfSegmentExist()) {
                        etAadhaar.setText(getAadharNo());
                        llAadhar.setVisibility(View.GONE);
                    } else if (isMfSegmentExist() && !getAppType().equalsIgnoreCase(DIRECT)){
                        etAadhaar.setText(getAadharNo());
                        llAadhar.setVisibility(View.GONE);
                    }
                    isAdharChanged = true;

                    if (!isFundTransferDone()) {
                        if (getAppType().equalsIgnoreCase(DIRECT) && !getIsAadharVerified()) {
                            llAadhar.setVisibility(View.VISIBLE);
                            authType = VIRTUALID;
                            rbVirtualId.setChecked(true);
                            rbAadhar.setChecked(false);
                            rbVirtualId.setVisibility(View.VISIBLE);
                            rbAadhar.setVisibility(View.GONE);
                            input_layout_aadhar.setHint("Aadhaar Virtual Id");
                            etAadhaar.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                            tvAadharEg.setText("9888000011115555");
                            tvAadharMsg.setText(Html.fromHtml(getResources().getString(R.string.vitrualIdmsg)));
                            tvAadharMsg.setTextColor(getResources().getColor(R.color.market_text));
                            etAadhaar.setText("");
                            btnSubmit.setVisibility(View.GONE);
                            ivNote.setVisibility(View.VISIBLE);
                            if (getAppType().equalsIgnoreCase(DIRECT)) {
                                if (openPaperlessAccount) {
                                    tvVirtualIdInfo.setText(Html.fromHtml(
                                            getResources().getString(R.string.open_paperless_account)));
                                } else {
                                    tvVirtualIdInfo.setText(Html.fromHtml(
                                            getResources().getString(R.string.unable_to_genrate_virtualid)));
                                }
                                tvVirtualIdInfo.setVisibility(View.VISIBLE);
                            } else {
                                tvVirtualIdInfo.setVisibility(View.GONE);
                            }
                        } else {
                            validateAadharAvailability();
                        }
                    }
                    etAadhaar.setEnabled(false);
                    cbAadhar.setEnabled(false);
                }
               /* if (getIsAadharVerified()) {
                    etAadhaar.setText(getAadharNo());
                } else {
                    etAadhaar.setEnabled(true);
                }*/
                etPan.setEnabled(false);
                etDOB.setEnabled(false);
                if (isFundTransferDone()) {
                    etAadhaar.setEnabled(false);
                    cbAadhar.setEnabled(false);
                    showNextBtn();
                    llAadhar.setVisibility(View.GONE);
                } else {
                    //Disable Aadhaar new requirement 15-Nov-2017
                    if (isBlank(getAadharNo())) {
                        etAadhaar.setEnabled(true);
                        cbAadhar.setEnabled(true);
                    } else {
                        etAadhaar.setEnabled(false);
                        cbAadhar.setEnabled(false);
                        if (getIsAadharVerified()) llAadhar.setVisibility(View.GONE);
                    }
                }
                cbAadharDeclaration.setEnabled(false);
            } else if (source.equals(BACK_FROM_PREVIOUS_SCREEN)) {
                // TODO: 25-03-2017
                panDOBStatus = true;// this flag is set to true because its dob is already validated through API
                if (getAppType().equalsIgnoreCase(DIRECT)) {

                    if (getInterestedInMF()) {
                        /*Direct MF */
                        if (!isBlank(getAddressMode())) {
                            etPan.setText(getPANno());
                            etDOB.setText(getDob());
                            etAadhaar.setText(getAadharNo());

                            etPan.setEnabled(false);
                            etDOB.setEnabled(false);
                            etAadhaar.setEnabled(false);
                            llAadhar.setVisibility(View.GONE);
                            if (!isBlank(getAadharNo()) && getIsAadharVerified()) {
                                etAadhaar.setEnabled(false);
                                cbAadhar.setEnabled(false);
                                cbAadharDeclaration.setEnabled(false);
                                showNextBtn();
                                llAadhar.setVisibility(View.GONE);
                            } else {
                                if (isMfIpvDone() || isOfflineDocumentUploadedDone()) {
                                    etAadhaar.setEnabled(false);
                                    cbAadhar.setEnabled(false);
                                    cbAadharDeclaration.setEnabled(false);
                                    showNextBtn();
                                    llAadhar.setVisibility(View.GONE);
                                } else {
                                    if (!getIsAadharVerified()) openPaperlessAccount = true;
                                    etAadhaar.setEnabled(true);
                                    cbAadhar.setEnabled(true);
                                    isAdharChanged = true;
                                    if (!isBlank(getAadharNo()))
                                        validateAadharAvailability();
                                }
                            }
                        } else {
                            etPan.setText(getPANno());
                            etDOB.setText(getDob());
                            etAadhaar.setText(getAadharNo());

                            isPANChanged = true;
                            panDOBStatus = true;
                            isAdharChanged = true;

                            etPan.setEnabled(true);
                            etDOB.setEnabled(true);
                            etAadhaar.setEnabled(true);
                            if (!isBlank(getAadharNo()))
                                validateAadharAvailability();
                        }
                    } else {
                        /*Direct Broking*/

                        if (!isBlank(getAddressMode())) {
                            etPan.setText(getPANno());
                            etDOB.setText(getDob());

                            etPan.setEnabled(false);
                            etDOB.setEnabled(false);
                            llAadhar.setVisibility(View.GONE);
                            if (!isBlank(getAadharNo()) && getIsAadharVerified()) {

                                etAadhaar.setEnabled(false);
                                cbAadhar.setEnabled(false);
                                cbAadharDeclaration.setEnabled(false);

                                etAadhaar.setText(getAadharNo());
                                showNextBtn();
                                llAadhar.setVisibility(View.GONE);
                                tvVirtualIdInfo.setVisibility(View.GONE);

                            } else {
                                etAadhaar.setText(getAadharNo());
                                if (isFundTransferDone()) {
                                    etAadhaar.setEnabled(false);
                                    cbAadhar.setEnabled(false);
                                    cbAadharDeclaration.setEnabled(false);
                                    showNextBtn();
                                    llAadhar.setVisibility(View.GONE);
                                    tvVirtualIdInfo.setVisibility(View.GONE);
                                } else {
                                    if (isBlank(getAadharNo())) { //Disable Aadhaar new requirement 15-Nov-2017
                                        etAadhaar.setEnabled(true);
                                        cbAadhar.setEnabled(true);
                                    } else {
                                        etAadhaar.setEnabled(false);
                                        cbAadhar.setEnabled(false);
                                        llAadhar.setVisibility(View.GONE);
                                    }
                                    cbAadharDeclaration.setEnabled(true);
                                    isAdharChanged = true;
                                    if (!isBlank(getAadharNo()))
                                        validateAadharAvailability();
                                }
                            }
//                            showNextBtn();
                        } else {
                            /*Direct broking address mode blank*/
                            etPan.setText(getPANno());
                            etDOB.setText(getDob());
                            etAadhaar.setText(getAadharNo());
                            llAadhar.setVisibility(View.GONE);
                            openPaperlessAccount = true;

                            cbAadhar.setEnabled(true);

                            etPan.setEnabled(true);
                            etDOB.setEnabled(true);
                            etAadhaar.setEnabled(true);

                            if (!isBlank(getAadharNo())) {
                                if (!getIsAadharVerified()) {
                                    validateAadharAvailability();
                                }
                            }
                        }
                    }
                } else {
                    /*MF BR BA*/
                    if (getInterestedInMF()) {

                        etPan.setText(getPANno());
                        etDOB.setText(getDob());
                        etAadhaar.setText(getAadharNo());
                        if (!isBlank(getAddressMode())) {
                            etPan.setEnabled(false);
                            etDOB.setEnabled(false);
                            etAadhaar.setEnabled(false);
                            showNextBtn();
                            llAadhar.setVisibility(View.GONE);
                        } else {
                            etPan.setEnabled(true);
                            etDOB.setEnabled(true);
                            etAadhaar.setEnabled(true);
                        }
                    } else {

                        /*BR BA broking*/
                        etPan.setText(getPANno());
                        etDOB.setText(getDob());
                        etAadhaar.setText(getAadharNo());
                        if (!isBlank(getAddressMode())) {

                            cbAadhar.setEnabled(false);

                            etPan.setEnabled(false);
                            etDOB.setEnabled(false);
                            etAadhaar.setEnabled(false);
                            showNextBtn();
                            llAadhar.setVisibility(View.GONE);
                        } else {

                            cbAadhar.setEnabled(false);

                            etPan.setEnabled(true);
                            etDOB.setEnabled(true);
                            etAadhaar.setEnabled(true);
                        }
                    }
                }
//                showNextBtn();
            } else if (source.equals(PAN_AADHAR_PENDING)) {
                if (getAppType().equalsIgnoreCase(DIRECT)) {
                    /*Direct MF*/
                    if (getInterestedInMF()) {
                        if (!isBlank(getAddressMode())) {
                            etPan.setText(getPANno());
                            etDOB.setText(getDob());
                            etAadhaar.setText(getAadharNo());

                            etPan.setEnabled(false);
                            etDOB.setEnabled(false);
                            etAadhaar.setEnabled(false);
                        } else {
                            etPan.setText(getPANno());
                            etDOB.setText(getDob());
                            etAadhaar.setText(getAadharNo());

                            isPANChanged = true;
                            panDOBStatus = true;
                            isAdharChanged = true;

                            etPan.setEnabled(true);
                            etDOB.setEnabled(true);
                            etAadhaar.setEnabled(true);
                        }
                    } else {
                        /*Direct Broking*/
                        if (!isBlank(getAddressMode())) {
                            etPan.setText(getPANno());
                            etDOB.setText(getDob());
                            etAadhaar.setText(getAadharNo());

                            etPan.setEnabled(false);
                            etDOB.setEnabled(false);
                            if (!isBlank(getAadharNo()) && getIsAadharVerified()) {

                                etAadhaar.setEnabled(false);
                                cbAadhar.setEnabled(false);

                                etAadhaar.setText(getAadharNo());
                                showNextBtn();

                            } else {
                                etAadhaar.setText(getAadharNo());
                                if (isFundTransferDone()) {
                                    etAadhaar.setEnabled(false);
                                    cbAadhar.setEnabled(false);
                                    showNextBtn();
                                } else {
                                    etAadhaar.setEnabled(true);
                                    cbAadhar.setEnabled(true);
//                                    isAdharChanged = true;
                                }
                                if (!isBlank(getAadharNo())) {
                                    if (!getIsAadharVerified()) {
                                        validateAadharAvailability();
                                    }
                                }

                            }
//                            showNextBtn();
                        } else {
                            /*
                             * set pan and dob if Addressmode is blank 10-may
                             * */
                            etPan.setText(getPANno());
                            if (!isBlank(getPANno())) {
                                etDOB.setText(getDob());
                                etAadhaar.setText(getAadharNo());
                                isPANChanged = true;
                                panDOBStatus = true;

                                etPan.setEnabled(true);
                                etDOB.setEnabled(true);
                                etAadhaar.setEnabled(true);
                                cbAadhar.setEnabled(true);
                                llAadhar.setVisibility(View.GONE);
                                openPaperlessAccount = true;
                            } else {
                                etPan.setEnabled(true);
                                etDOB.setEnabled(false);
                                etAadhaar.setEnabled(false);
                                cbAadhar.setEnabled(false);
                            }
                           /* etPan.setText(getPANno());
                            etDOB.setText(getDob());
                            etAadhaar.setText(getAadharNo());

                            cbAadhar.setEnabled(true);

                            etPan.setEnabled(true);
                            etDOB.setEnabled(true);
                            etAadhaar.setEnabled(true);
                            if (!isBlank(getAadharNo())) {
                                if (!getIsAadharVerified()) {
                                   validateAadharAvailability();
                                }
                            } else {
                                if (isBlank(getPANno())) {
                                    etPan.setEnabled(true);
                                    etDOB.setEnabled(false);
                                    etAadhaar.setEnabled(false);
                                    cbAadhar.setEnabled(false);
                                }
                            }*/
                        }
                    }
                } else {

                    if (getInterestedInMF()) {
                        /*BR/BA MF*/
                        if (!isBlank(getAddressMode())) {
                            etPan.setText(getPANno());
                            etDOB.setText(getDob());
                            etAadhaar.setText(getAadharNo());

                            etPan.setEnabled(false);
                            etDOB.setEnabled(false);
                            etAadhaar.setEnabled(false);
                        } else {
                            etPan.setText(getPANno());
                            etDOB.setText(getDob());
                            etAadhaar.setText(getAadharNo());

                            isPANChanged = true;
                            panDOBStatus = true;
                            isAdharChanged = true;

                            etPan.setEnabled(true);
                            etDOB.setEnabled(true);
                            etAadhaar.setEnabled(true);
                        }
                    } else {
                        /*BR/BA broking*/
                        if (!isBlank(getAddressMode())) {
                            etPan.setText(getPANno());
                            etDOB.setText(getDob());
                            etAadhaar.setText(getAadharNo());

                            etPan.setEnabled(false);
                            etDOB.setEnabled(false);
                            if (!isBlank(getAadharNo()) && getIsAadharVerified()) {

                                etAadhaar.setEnabled(false);
                                cbAadhar.setEnabled(false);

                                etAadhaar.setText(getAadharNo());
                                showNextBtn();

                            } else {
                                etAadhaar.setText(getAadharNo());
                                if (isFundTransferDone()) {
                                    etAadhaar.setEnabled(false);
                                    cbAadhar.setEnabled(false);
                                    showNextBtn();
                                } else {
                                    etAadhaar.setEnabled(true);
                                    cbAadhar.setEnabled(true);
//                                    isAdharChanged = true;
                                }
                                if (!isBlank(getAadharNo())) {
                                    if (!getIsAadharVerified()) {
                                        validateAadharAvailability();
                                    }
                                }

                            }
//                            showNextBtn();
                        } else {
                            /*
                             * set pan and dob if Addressmode is blank 10-may
                             * */
                            etPan.setText(getPANno());
                            if (!isBlank(getPANno())) {
                                etDOB.setText(getDob());
                                etAadhaar.setText(getAadharNo());
                                isPANChanged = true;
                                panDOBStatus = true;

                                etPan.setEnabled(true);
                                etDOB.setEnabled(true);
                                etAadhaar.setEnabled(true);
                                cbAadhar.setEnabled(true);
                            } else {
                                etPan.setEnabled(true);
                                etDOB.setEnabled(false);
                                etAadhaar.setEnabled(false);
                                cbAadhar.setEnabled(false);
                            }
                          /*  etPan.setText(getPANno());
                            etDOB.setText(getDob());
                            if (!isBlank(getAadharNo())) {
                                isAdharChanged = false;

                                etAadhaar.setText(getAadharNo());

                                etPan.setEnabled(true);
                                etDOB.setEnabled(true);
                                etAadhaar.setEnabled(true);
                                if (!getIsAadharVerified()) {
                                   validateAadharAvailability();
                                }
                            } else {
                                if (isBlank(getPANno())) {
                                    etPan.setEnabled(true);
                                    etDOB.setEnabled(false);
                                    etAadhaar.setEnabled(false);
                                    cbAadhar.setEnabled(false);
                                } else {
                                    etPan.setText(getPANno());
                                    etDOB.setText(getDob());
                                    etAadhaar.setText(getAadharNo());

                                    etPan.setEnabled(true);
                                    etDOB.setEnabled(true);
                                    etAadhaar.setEnabled(true);
                                    cbAadhar.setEnabled(true);
                                }
                            }*/
                        }

                    }

                }
            } else {
                etPan.requestFocus();
            }
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
//            Crashlytics.logException(e);
        }

    }

    private CustomTabsSession getSession() {
        return mClient.newSession(new CustomTabsCallback() {
            @Override
            public void onNavigationEvent(int navigationEvent, Bundle extras) {
                super.onNavigationEvent(navigationEvent, extras);
            }
        });
    }

    private void openCustomTabsIntent(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
        builder.setToolbarColor(getResources().getColor(R.color.secondaryBackground));
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_back_arrow));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(PANAdharRegistActivity.this, Uri.parse(url));
    }

    public void openInBrowser(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(PANAdharRegistActivity.this.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void showNextBtn() {
        btnSubmit.setText(NEXT);
        btnSubmit.setVisibility(View.VISIBLE);
    }

    public void allCondSatisfied() {

        setPANno(etPan.getText().toString());
        setAadharNo(etAadhaar.getText().toString());
        setDob(etDOB.getText().toString());

        final Dialog dialog = new Dialog(PANAdharRegistActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_aadhaar);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);

        TextView tvAadhaarNumber = (TextView) dialog.findViewById(R.id.tvAadhaarNumber);
        TextView tvListOfIntermediaries = (TextView) dialog.findViewById(R.id.tvListOfIntermediaries);

        Button btnAgree = (Button) dialog.findViewById(R.id.btnAgree);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
//        final CheckBox cbAgree = (CheckBox) dialog.findViewById(R.id.cbAgree);
//        cbAgree.setChecked(true);
        tvAadhaarNumber.setText(etAadhaar.getText().toString());

        tvListOfIntermediaries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOfIntermediariesDialog();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonMsgDialog(PANAdharRegistActivity.this, "Aadhaar Declaration not accepted", false, "");
                aadhaarConsentAgreed = false;
                dialog.dismiss();
            }
        });
        btnAgree.setOnClickListener(new Button.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            aadhaarConsentAgreed = true;
                                            if (otpFlag && etOTP.getText().toString().trim().equals("")) {
                                                MOSLCommon.showToastMsg(PANAdharRegistActivity.this, "Enter OTP");
                                            } else {
//                                                if (cbAgree.isChecked()) {
                                                dialog.dismiss();

//                                                    if (getAppType().equals(CRM) || getAppType().equals(PP) || getAppType().equals(DIRECT)) {
                                                if (getIsAadharVerified()) {
                                                    if (getAppType().equals(DIRECT)) {
                                                        if (getIsCVLVerified()) {
                                                            submitPANAadhar(true);
                                                        } else {
                                                            submitPANAadhar(true);
                                                        }
                                                    } else if (OTPVerified == noOTPVerified) {
                                                        if (getInterestedInMF() && getIsCVLVerified() && (getAppType().equals(PP) || getAppType().equals(CRM))) {
                                                            showEquityMFOptionDialog(true, false);
                                                        } else {
                                                            submitPANAadhar(true);
                                                        }
                                                    } else if (OTPVerified == cvlOTPVerified) {
                                                        submitPANAadhar(true);

                                                    } else if (OTPVerified == aadhaarOTPVerified && aadhaarConsentAgreed) {
                                                        submitPANAadhar(true);
                                                    }
                                                        /*else if (OTPVerified == aadhaarOTPVerified) {
                                                            aadhaarConsentAgreed = true;
                                                        }*/


                                                            /*if (getInterestedInMF()) {//show Equity message
                                                                showEquityMFOptionDialog(false);

                                                            } else {//Submit pan
                                                                submitPANAadhar();
                                                            }*/
                                                } else {
                                                    if (getInterestedInMF()) {//Submit Pan
                                                        submitPANAadhar(true);
                                                    } else {//Show paperless dialog
                                                        setAadharNo(etAadhaar.getText().toString());
                                                        etAadhaar.setEnabled(false);
                                                        dialogDocList = new Dialog(PANAdharRegistActivity.this);
                                                        dialogDocList.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                        dialogDocList.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                                        dialogDocList.getWindow().setWindowAnimations(R.style.animationName);
                                                        dialogDocList.setContentView(R.layout.dialog_doc_list);
                                                        dialogDocList.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                        dialogDocList.setCanceledOnTouchOutside(false);

                                                        Button btnAgree = (Button) dialogDocList.findViewById(R.id.btnAgree);
                                                        TextView tvHeading = (TextView) dialogDocList.findViewById(R.id.tvHeading);
                                                        TextView tvHeading1 = (TextView) dialogDocList.findViewById(R.id.tvHeading1);

                                                        tvHeading1.setText(getResources().getString(R.string.no_aadhar));
                                                        tvHeading.setText(getRegisteredName());


                                                        btnAgree.setOnClickListener(new Button.OnClickListener() {

                                                            @Override
                                                            public void onClick(View v) {
                                                                // TODO Auto-generated method stub
                                                                dialogDocList.dismiss();

                                                            }
                                                        });

                                                        ImageView btnClose = (ImageView) dialogDocList.findViewById(R.id.btnClose);
                                                        btnClose.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                dialogDocList.dismiss();
                                                            }
                                                        });
                                                        dialogDocList.show();
                                                    }
                                                }



                                                   /* llBaseOTPBioLayout.setVisibility(View.VISIBLE);
                                                    llParentOTPBio.setVisibility(View.VISIBLE);
                                                    if (getAppType().equalsIgnoreCase(DIRECT)) {
                                                        llImgThumb.setVisibility(View.GONE);
                                                    } else {
                                                        llImgThumb.setVisibility(View.VISIBLE);
                                                    }*/
                                                /*} else {
//                        MOSLCommon.showToastMsg(PANAdharRegistActivity.this, "Aadhar Declaration not accepted");
                                                    commonMsgDialog(PANAdharRegistActivity.this, "Aadhar Declaration not accepted", false, "");
//                                                    MOSLCommon.alertDialog(PANAdharRegistActivity.this, 0, "MOSL", "Aadhar Declaration not accepted", "OK", true, null);
                                                }*/

                                            }
                                        }
                                    }

        );

        /*ImageView btnClose = (ImageView) dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener()

                                    {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    }

        );*/
        dialog.show();
    }

    Dialog mfWarningDialog = null;
    Dialog showEquityMFdialog = null;
    Dialog equityMFWarningDialog = null;

    boolean isShowCVLOTP, isShowAadhaarOTP;

    private void showEquityMFOptionDialog(final boolean showCVLOTP, final boolean showAadhaarOTP) {
        isShowCVLOTP = showCVLOTP;
        isShowAadhaarOTP = showAadhaarOTP;
        showEquityMFdialog = new Dialog(PANAdharRegistActivity.this);
        showEquityMFdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showEquityMFdialog.setContentView(R.layout.layout_dialog_equity_mf_option);
        showEquityMFdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        showEquityMFdialog.getWindow().setWindowAnimations(R.style.animationName);
        showEquityMFdialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        showEquityMFdialog.setCanceledOnTouchOutside(false);


        TextView tvMsg = (TextView) showEquityMFdialog.findViewById(R.id.tvMsg);
        TextView tvHeading = (TextView) showEquityMFdialog.findViewById(R.id.tvHeading);
        TextView tvBoldHeading1 = (TextView) showEquityMFdialog.findViewById(R.id.tvBoldHeading1);
        Button btnYes = (Button) showEquityMFdialog.findViewById(R.id.btnYes);
        Button btnNO = (Button) showEquityMFdialog.findViewById(R.id.btnNO);
        LinearLayout llDialogButton = (LinearLayout) showEquityMFdialog.findViewById(R.id.llDialogButton);
        ImageView ivClose = (ImageView) showEquityMFdialog.findViewById(R.id.ivClose);
        tvMsg.setText(getResources().getString(R.string.befits_of_equ_with_mf_msg));
        btnYes.setText(getResources().getString(R.string.open_equ_with_mf_msg));
        btnNO.setText(getResources().getString(R.string.open_only_mf_msg));

        btnYes.setVisibility(View.VISIBLE);
        btnNO.setVisibility(View.VISIBLE);
        tvHeading.setVisibility(View.VISIBLE);
        tvHeading.setText(getResources().getString(R.string.welcome));
        tvBoldHeading1.setVisibility(View.VISIBLE);
        tvBoldHeading1.setText(getResources().getString(R.string.wish_to_open_equity));
        llDialogButton.setOrientation(LinearLayout.VERTICAL);
        tvMsg.setGravity(Gravity.LEFT);
        ivClose.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           showEquityMFdialog.dismiss();
                                       }
                                   }
        );

        btnNO.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         mfWarningDialog = new Dialog(PANAdharRegistActivity.this);
                                         mfWarningDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                         mfWarningDialog.setContentView(R.layout.layout_dialog_yes_no_option);
                                         mfWarningDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                         mfWarningDialog.getWindow().setWindowAnimations(R.style.animationName);
                                         mfWarningDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                         mfWarningDialog.setCanceledOnTouchOutside(false);
                                         TextView tvMsg = (TextView) mfWarningDialog.findViewById(R.id.tvMsg);
                                         TextView tvHeading = (TextView) mfWarningDialog.findViewById(R.id.tvHeading);
                                         TextView tvBoldHeading1 = (TextView) mfWarningDialog.findViewById(R.id.tvBoldHeading1);
                                         Button btnYes = (Button) mfWarningDialog.findViewById(R.id.btnYes);
                                         Button btnNO = (Button) mfWarningDialog.findViewById(R.id.btnNO);
                                         LinearLayout llDialogButton = (LinearLayout) mfWarningDialog.findViewById(R.id.llDialogButton);
                                         ImageView ivClose = (ImageView) mfWarningDialog.findViewById(R.id.ivClose);
                                         tvHeading.setVisibility(View.GONE);
                                         tvBoldHeading1.setVisibility(View.GONE);
                                         btnNO.setVisibility(View.GONE);
                                         llDialogButton.setOrientation(LinearLayout.HORIZONTAL);
                                         tvMsg.setText(getResources().getString(R.string.open_only_mf_msg_warning));
                                         btnYes.setText(OK);
                                         btnNO.setText("");
                                         btnYes.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 isSegmentMForEquity = MFSegment;
                                                 submitPANAadhar(false);
                                                /* mfWarningDialog.dismiss();
                                                 showEquityMFdialog.dismiss();


                                                 isSegmentMForEquity = MFSegment;
//                    isEquitySelected = false;
//                    setIsInterestedInMF(true);
                                                 // etAadhaar.setEnabled(false);

                                                 if (!showCVLOTP && !showAadhaarOTP) {
                                                     allCondSatisfied();
                                                 } else {
                                                     btnSubmit.setVisibility(View.GONE);
//                    isInterestedInMF = true;
                                                     if (showCVLOTP) {
                                                         isCVLOPT = true;
//                        etOTP.setHint(ENTER_4_DIGIT_OTP);

                                                         cvlOTPApi();
                                                     } else {
                                                         isCVLOPT = false;
                                                         submitPANAadhar(true);
                                                     }
                                                     if (showAadhaarOTP) {
                                                         llMobileNotMapped.setVisibility(View.VISIBLE);
                                                         tvDocCheckList.setVisibility(View.VISIBLE);
                                                         llBaseOTPBioLayout.setVisibility(View.VISIBLE);
                                                         llParentOTPBio.setVisibility(View.VISIBLE);
                                                         if (getAppType().equals(DIRECT)) {
                                                             showAuthenticationModeMsg(true, dcMode);
                                                         } else {
                                                             showAuthenticationModeMsg(true, brbaMode);
                                                         }
                                                     }
                                                 }*/
                                             }
                                         });
                                         ivClose.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 mfWarningDialog.dismiss();
                                             }
                                         });
                                         mfWarningDialog.show();
                                     }
                                 }

        );
        btnYes.setOnClickListener(new View.OnClickListener()

                                  {
                                      @Override
                                      public void onClick(View v) {
                                          equityMFWarningDialog = new Dialog(PANAdharRegistActivity.this);
                                          equityMFWarningDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                          equityMFWarningDialog.setContentView(R.layout.layout_dialog_yes_no_option);
                                          equityMFWarningDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                          equityMFWarningDialog.getWindow().setWindowAnimations(R.style.animationName);
                                          equityMFWarningDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                          equityMFWarningDialog.setCanceledOnTouchOutside(false);


                                          TextView tvMsg = (TextView) equityMFWarningDialog.findViewById(R.id.tvMsg);
                                          TextView tvHeading = (TextView) equityMFWarningDialog.findViewById(R.id.tvHeading);
                                          TextView tvBoldHeading1 = (TextView) equityMFWarningDialog.findViewById(R.id.tvBoldHeading1);
                                          Button btnYes = (Button) equityMFWarningDialog.findViewById(R.id.btnYes);
                                          Button btnNO = (Button) equityMFWarningDialog.findViewById(R.id.btnNO);
                                          LinearLayout llDialogButton = (LinearLayout) equityMFWarningDialog.findViewById(R.id.llDialogButton);
                                          ImageView ivClose = (ImageView) equityMFWarningDialog.findViewById(R.id.ivClose);
                                          tvHeading.setVisibility(View.GONE);
                                          tvBoldHeading1.setVisibility(View.GONE);
                                          btnNO.setVisibility(View.GONE);
                                          llDialogButton.setOrientation(LinearLayout.HORIZONTAL);
                                          tvMsg.setText(getResources().getString(R.string.open_equ_with_mf_msg_warning));
                                          btnYes.setText(OK);
                                          btnNO.setText("");
                                          btnYes.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {

                                                  isSegmentMForEquity = EquitySegment;
                                                  submitPANAadhar(false);
                                                 /* equityMFWarningDialog.dismiss();
                                                  showEquityMFdialog.dismiss();


//                    isEquitySelected = true;
//                    setIsInterestedInMF(false);
                                                  // etAadhaar.setEnabled(false);

                                                  if (!showCVLOTP && !showAadhaarOTP) {
                                                      allCondSatisfied();
                                                  } else {
                                                      btnSubmit.setVisibility(View.GONE);
                                                      if (showCVLOTP) {
                                                          tvDocCheckList.setVisibility(View.VISIBLE);
                                                          llBaseOTPBioLayout.setVisibility(View.VISIBLE);
                                                          llParentOTPBio.setVisibility(View.VISIBLE);
                                                          if (getAppType().equalsIgnoreCase(DIRECT)) {
                                                              llImgThumb.setVisibility(View.GONE);
                                                              llImgOTP.setVisibility(View.VISIBLE);
                                                          } else {
                                                              llImgThumb.setVisibility(View.VISIBLE);
                                                              llImgOTP.setVisibility(View.VISIBLE);
                                                          }
                                                      } else {
                                                          submitPANAadhar(true);
                                                      }

                                                      if (showAadhaarOTP) {
                                                          llMobileNotMapped.setVisibility(View.VISIBLE);
                                                          tvDocCheckList.setVisibility(View.VISIBLE);
                                                          llBaseOTPBioLayout.setVisibility(View.VISIBLE);
                                                          llParentOTPBio.setVisibility(View.VISIBLE);
                                                          if (getAppType().equals(DIRECT)) {
                                                              showAuthenticationModeMsg(true, dcMode);
                                                          } else {
                                                              showAuthenticationModeMsg(true, brbaMode);
                                                          }
                                                      }
                                                  }*/
                                              }
                                          });
                                          ivClose.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  equityMFWarningDialog.dismiss();
                                              }
                                          });
                                          equityMFWarningDialog.show();

                                      }
                                  }

        );
        showEquityMFdialog.show();

        /*final CustomDialog submitDialog = new CustomDialog(PANAdharRegistActivity.this, getResources().getString(R.string.befits_of_equ_with_mf_msg), true, true, true, true,
                getResources().getString(R.string.open_equ_with_mf_msg), getResources().getString(R.string.open_only_mf_msg), getResources().getString(R.string.welcome),
                getResources().getString(R.string.wish_to_open_equity), EQU_MF_PAN_AADHAAR_SUBMIT, new CustomDialog.CustomDialogListener() {
            @Override
            public void dialogAction(MOSLCommon.Action action, Object... data) {
                if (action == Action.OK) {*//*Equity is selected*//*

                    CustomDialog cdEquMFWarning = new CustomDialog(PANAdharRegistActivity.this, getResources().getString(R.string.open_equ_with_mf_msg_warning), true, false, true, true, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                        @Override
                        public void dialogAction(Action action, Object... data) {
                            if (action == Action.OK) {
                                isSegmentMForEquity = EquitySegment;
//                    isEquitySelected = true;
//                    setIsInterestedInMF(false);
                                // etAadhaar.setEnabled(false);

                                if (!showCVLOTP && !showAadhaarOTP) {
                                    allCondSatisfied();
                                } else {
                                    btnSubmit.setVisibility(View.GONE);
                                    if (showCVLOTP) {
                                        tvDocCheckList.setVisibility(View.VISIBLE);
                                        llBaseOTPBioLayout.setVisibility(View.VISIBLE);
                                        llParentOTPBio.setVisibility(View.VISIBLE);
                                        if (getAppType().equalsIgnoreCase(DIRECT)) {
                                            llImgThumb.setVisibility(View.GONE);
                                            llImgOTP.setVisibility(View.VISIBLE);
                                        } else {
                                            llImgThumb.setVisibility(View.VISIBLE);
                                            llImgOTP.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        submitPANAadhar();
                                    }

                                    if (showAadhaarOTP) {
                                        llMobileNotMapped.setVisibility(View.VISIBLE);
                                        tvDocCheckList.setVisibility(View.VISIBLE);
                                        llBaseOTPBioLayout.setVisibility(View.VISIBLE);
                                        llParentOTPBio.setVisibility(View.VISIBLE);
                                        if (getAppType().equals(DIRECT)) {
                                            showAuthenticationModeMsg(true, dcMode);
                                        } else {
                                            showAuthenticationModeMsg(true, brbaMode);
                                        }
                                    }
                                }
                            }
                        }
                    });

                    cdEquMFWarning.show();

                } else if (action == Action.NO) {*//*MF is selected*//*
                    CustomDialog cdMfWarning = new CustomDialog(PANAdharRegistActivity.this, getResources().getString(R.string.open_only_mf_msg_warning), true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                        @Override
                        public void dialogAction(Action action, Object... data) {
                            if (action == Action.OK) {
                                isSegmentMForEquity = MFSegment;
//                    isEquitySelected = false;
//                    setIsInterestedInMF(true);
                                // etAadhaar.setEnabled(false);

                                if (!showCVLOTP && !showAadhaarOTP) {
                                    allCondSatisfied();
                                } else {
                                    btnSubmit.setVisibility(View.GONE);
//                    isInterestedInMF = true;
                                    if (showCVLOTP) {
                                        isCVLOPT = true;
//                        etOTP.setHint(ENTER_4_DIGIT_OTP);

                                        cvlOTPApi();
                                    } else {
                                        isCVLOPT = false;
                                        submitPANAadhar();
                                    }
                                    if (showAadhaarOTP) {
                                        llMobileNotMapped.setVisibility(View.VISIBLE);
                                        tvDocCheckList.setVisibility(View.VISIBLE);
                                        llBaseOTPBioLayout.setVisibility(View.VISIBLE);
                                        llParentOTPBio.setVisibility(View.VISIBLE);
                                        if (getAppType().equals(DIRECT)) {
                                            showAuthenticationModeMsg(true, dcMode);
                                        } else {
                                            showAuthenticationModeMsg(true, brbaMode);
                                        }
                                    }
                                }

                            }
                        }
                    });
                    cdMfWarning.show();


                }

            }


        });
        submitDialog.show();*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == PAN_AADHAAR_BIOMETRIC_RESULT_OK) {
            aadharValidationSuccess = data.getStringExtra(VALIDATION_SUCCESS);
            if (aadharValidationSuccess.equals(VALIDATION_SUCCESS)) {
                OTPVerified = aadhaarOTPVerified;
                setIsAadharVerified(true);
                cbAadhar.setEnabled(false);
//                                    valididateDialog("User Authentication Successful", 6);
                llBaseOTPBioLayout.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.VISIBLE);
                btnSubmit.setText(SUBMIT);
                setAadhaarVerifiedByBio(true);
            } else {
                btnSubmit.setVisibility(View.GONE);
            }
        }

    }

    public void formExit() {
        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);

            Map inputMap = new HashMap<>();
            inputMap.put(APPLICATION_TYPE, getAppType());
            inputMap.put("_userid", userID);
            inputMap.put(APP_LOGIN_ID, getAppLoginID());

            String flag = USER_LOGOUT;
            String category = SIGNUP;

            WebServiceHandler.post(PANAdharRegistActivity.this, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (response != null) {
                        try {
                            String msg = (response.isNull("_msg") || response.getString("_msg").equals("")) ? "" : response.getString("_msg");
                            String status = (response.isNull("_Status") || response.getString("_Status").equals("")) ? "" : response.getString("_Status").trim();

                            if (Integer.valueOf(status) == 1) {
                                setUserID("");
                                setUserType("");
                                MOSLCommon.showToastMsg(PANAdharRegistActivity.this, msg);
                                if (getAppType().equals(DIRECT)) {
                                    Intent intent = new Intent(PANAdharRegistActivity.this, DirectResumeApplicationActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (getAppType().equals(CRM)) {
                                    Intent intent = new Intent(PANAdharRegistActivity.this, PostLoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (getAppType().equals(PP)) {
                                    if (isDirectProspect()) { //goto resume page for Direct prospect user
                                        Intent intent = new Intent(PANAdharRegistActivity.this, DirectResumeApplicationActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(PANAdharRegistActivity.this, PostLoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            } else {
                                commonMsgDialog(PANAdharRegistActivity.this, msg, false, "");
                            }
                            customProgress.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(PANAdharRegistActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
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
                    commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
//                MOSLCommon.alertDialog(PANAdharRegistActivity.this, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });
        } else {
            MOSLCommon.alertDInternet(this);
        }

    }

    private void submitPANAadhar(boolean isFinalSubmit) {
        isFinalSubmitPANAdhar = isFinalSubmit;
        setDob(etDOB.getText().toString());
        String seg = "";
        if (isSegmentMForEquity.equals("")) {
            seg = "";
        } else if (isSegmentMForEquity.equals(EquitySegment)) {
            seg = EQUITY;
            setIsEquitySelected(true);
            setIsInterestedInMF(false);
        } else if (isSegmentMForEquity.equals(MFSegment)) {
            seg = "";
            setIsInterestedInMF(true);
            setIsEquitySelected(false);
        }

        Map inputMap = new HashMap<>();
        inputMap.put("_PANNO", getPANno());
        inputMap.put("userid", userID);
        inputMap.put("_DOB", getDob());
        inputMap.put("aadhaarno", getAadharNo());
        inputMap.put("_NameOnPAN", getPANName());
        inputMap.put("_IsCVLverified", getCvlVerified());
        inputMap.put("_IsAadharVerified", getAadharVerified());
        inputMap.put("_IP", getDeviceID(PANAdharRegistActivity.this, getContentResolver()));
        inputMap.put("_Segment", seg);
        inputMap.put("_chkSIPIntent", getSipIntent());

        String flag = UPDATEPANAADHARDETAILS;
        String category = PANADDHARDETAILS;

        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);
            MOSLCommon.removeKeyboard(PANAdharRegistActivity.this, customProgress);
            WebServiceHandler.post(this, flag, category, inputMap, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);

                            if (response != null) {
                       /* {"_PANNO":null,"aadhaarno":null,"otp":null,"userid":null,
                                "_Status":"1","_msg":"","_DOB":null,"_IsCVLverified":null,
                                "_IsAadharVerified":null,"_ApplicationType":null,"_AddressMode":null,"_IP":null,"_Segment":null}*/

                                if (response.length() != 0) {
                                    try {
                                        String status = (response.isNull("_Status") || response.getString("_Status").equals("")) ? "" : response.getString("_Status");
                                        String msg = (response.isNull("_msg") || response.getString("_msg").equals("")) ? "" : response.getString("_msg");
                                        if (!status.equals("")) {
                                            if (Integer.valueOf(status) == 0) {
//                                                valididateDialog(msg, 5);
                                                CustomDialog dialogMsg = new CustomDialog(PANAdharRegistActivity.this, msg, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                                                    @Override
                                                    public void dialogAction(Action action, Object... data) {

                                                    }
                                                });
                                                dialogMsg.show();
                                            } else if (Integer.valueOf(status) == 1) {
                                                if (isFinalSubmitPANAdhar) {
                                                    if (getInterestedInMF()) {
                                                        if (!getIsAadharVerified()) {
                                                            if (getIsCVLVerified()) {
                                                                updateAddressDetails(userID, CVL);
                                                            } else {
                                                                updateAddressDetails(userID, MANUAL);
                                                            }
                                                        } else {
                                                            if (getIsCVLVerified()) {
                                                                updateAddressDetails(userID, CVL);
                                                            } else {
//                                                            getAddress(false, true, true);
                                                                Intent intent = new Intent(PANAdharRegistActivity.this, AddressDetailsActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }
                                                    } else if (!getIsCVLVerified() && !getIsAadharVerified()) {
                                                        if (isThisSecondProductFlow()) {
                                                            callApplyNowActivity(PANAdharRegistActivity.this);
                                                        } else if (getAppType().equals(DIRECT)) {
                                                            updateAddressDetails(userID, MANUAL);
                                                        }
                                                    } else {
                                                        Intent intent = new Intent(PANAdharRegistActivity.this, AddressDetailsActivity.class);
                                                        startActivity(intent);
                                                        finish();

                                                    }
                                                } else {
                                                    if (isSegmentMForEquity.equals(MFSegment)) {
                                                        mfWarningDialog.dismiss();
                                                        showEquityMFdialog.dismiss();


                                                        isSegmentMForEquity = MFSegment;
//                    isEquitySelected = false;
//                    setIsInterestedInMF(true);
                                                        // etAadhaar.setEnabled(false);

                                                        if (!isShowCVLOTP && !isShowAadhaarOTP) {
                                                            allCondSatisfied();
                                                        } else {
                                                            btnSubmit.setVisibility(View.GONE);
//                    isInterestedInMF = true;
                                                            if (isShowCVLOTP) {
                                                                isCVLOPT = true;
//                        etOTP.setHint(ENTER_4_DIGIT_OTP);

                                                                cvlOTPApi();
                                                            } else {
                                                                isCVLOPT = false;
                                                                submitPANAadhar(true);
                                                            }
                                                            if (isShowAadhaarOTP) {
                                                                llMobileNotMapped.setVisibility(View.VISIBLE);
                                                                tvDocCheckList.setVisibility(View.VISIBLE);
                                                                llBaseOTPBioLayout.setVisibility(View.VISIBLE);
                                                                llParentOTPBio.setVisibility(View.VISIBLE);
                                                                if (getAppType().equals(DIRECT)) {
                                                                    showAuthenticationModeMsg(true, dcMode);
                                                                } else {
                                                                    showAuthenticationModeMsg(true, brbaMode);
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        equityMFWarningDialog.dismiss();
                                                        showEquityMFdialog.dismiss();


//                    isEquitySelected = true;
//                    setIsInterestedInMF(false);
                                                        // etAadhaar.setEnabled(false);

                                                        if (!isShowCVLOTP && !isShowAadhaarOTP) {
                                                            allCondSatisfied();
                                                        } else {
                                                            btnSubmit.setVisibility(View.GONE);
                                                            if (isShowCVLOTP) {
                                                                tvDocCheckList.setVisibility(View.VISIBLE);
                                                                llBaseOTPBioLayout.setVisibility(View.VISIBLE);
                                                                llParentOTPBio.setVisibility(View.VISIBLE);
                                                                if (getAppType().equalsIgnoreCase(DIRECT)) {//call showHideOTPBioLayout method
//                                                                    llImgThumb.setVisibility(View.GONE);
//                                                                    llImgOTP.setVisibility(View.VISIBLE);
                                                                    showHideOTPBioLayout();
                                                                } else {//call showHideOTPBioLayout method
//                                                                    llImgThumb.setVisibility(View.VISIBLE);
//                                                                    llImgOTP.setVisibility(View.VISIBLE);
                                                                    showHideOTPBioLayout();
                                                                }
                                                            } else {
                                                                submitPANAadhar(true);
                                                            }

                                                            if (isShowAadhaarOTP) {
                                                                llMobileNotMapped.setVisibility(View.VISIBLE);
                                                                tvDocCheckList.setVisibility(View.VISIBLE);
                                                                llBaseOTPBioLayout.setVisibility(View.VISIBLE);
                                                                llParentOTPBio.setVisibility(View.VISIBLE);
                                                                if (getAppType().equals(DIRECT)) {
                                                                    showAuthenticationModeMsg(true, dcMode);
                                                                } else {
                                                                    showAuthenticationModeMsg(true, brbaMode);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        customProgress.setVisibility(View.GONE);
                                        commonMsgDialog(PANAdharRegistActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                                        MOSLCommon.alertDialog(PANAdharRegistActivity.this, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                                    }
                                }

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
                        public void onFailure(int statusCode, Header[] headers, String
                                responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }
                    }

            );
        } else {
            MOSLCommon.alertDInternet(this);
        }
        /*if (getAppType().equals(PP)) {
            setSipIntent("0");
            submitData(isFinalSubmit);
        } else {
            if (cbSIP.isChecked()) {
                setSipIntent("1");
                submitData(isFinalSubmit);
            } else {
                sipDialog(isFinalSubmit);
            }
        }*/
    }

    private void updateAddressDetails(String userID, final String addressMode) {
        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put(APPLICATION_TYPE, getAppType());
            inputMap.put("userid", userID);
            inputMap.put(ADDRESSMODE, addressMode);

            String flag = UPDATEADDRESSDETAILS;
            String category = ADDRESSDETAILS;

            WebServiceHandler.post(this, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    if (response != null) {
                        try {
                            String status = (response.isNull("_Status") || response.getString("_Status").equals("")) ? "" : response.getString("_Status");
                            String msg = (response.isNull("_msg") || response.getString("_msg").equals("")) ? "" : response.getString("_msg");
                            if (Integer.valueOf(status) == 1) {
                                setAddressMode(addressMode);
                                callApplyNowActivity(PANAdharRegistActivity.this);
                            } else if (Integer.valueOf(status) == 0) {
                                CustomDialog msgDialog = new CustomDialog(PANAdharRegistActivity.this, msg, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                                    @Override
                                    public void dialogAction(Action action, Object... data) {

                                    }
                                });
                                msgDialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            customProgress.setVisibility(View.GONE);

                        }

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
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        } else {
            MOSLCommon.alertDInternet(this);
        }


    }

    private void fetchAddFromPAN() {
        Map inputMap = new HashMap<>();
        inputMap.put("_PANNO", getPANno());
        inputMap.put("userid", userID);
        inputMap.put("_DOB", etDOB.getText().toString());

        String flag = GetPANDetailsFromCVL;
        String category = PANADDHARDETAILS;

        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);
            MOSLCommon.removeKeyboard(PANAdharRegistActivity.this, customProgress);
            WebServiceHandler.post(this, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    if (response != null) {
                        try {
                           /* {"_PANNO":null,"aadhaarno":null,"otp":null,"userid":null,
                                    "_Status":"0","_msg":"INVALID DOB (APP_DOB_INCORP) PROVIDED","_msg2":null,
                                    "_msg3":null,"_DOB":null,"_IsCVLverified":"0","_IsAadharVerified":null,"_ApplicationType":null,
                                    "_AddressMode":null,"_IP":null,"_Segment":null,"_NameOnPAN":null}*/

                            String status = (response.isNull("_Status") || response.getString("_Status").equals("")) ? "" : response.getString("_Status");
                            String msg = (response.isNull("_msg") || response.getString("_msg").equals("")) ? "" : response.getString("_msg");
                            String isCVLverified = (response.isNull("_IsCVLverified") || response.getString("_IsCVLverified").equals("")) ? "" : response.getString("_IsCVLverified");


                            if (!status.equals("")) {
                                setIsCVLVerified(isCVLverified != null && isCVLverified.equals("1") ? true : false);
                                btnSubmit.setVisibility(View.GONE);
                                if (getInterestedInMF() && getIsCVLVerified()) {
                                    rbAadhar.setChecked(true);
                                    rbVirtualId.setChecked(false);
                                    rbAadhar.setVisibility(View.VISIBLE);
                                    rbVirtualId.setVisibility(View.GONE);
                                    input_layout_aadhar.setHint("Aadhaar No");
                                    etAadhaar.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                                    tvAadharEg.setText("988800001111");
                                    tvAadharMsg.setText(getResources().getString(R.string.aadharmsg));
                                    tvAadharMsg.setTextColor(getResources().getColor(R.color.market_text));
                                    authType = AADHAAR;
                                    etAadhaar.setText("");
                                    btnSubmit.setVisibility(View.GONE);
                                    ivNote.setVisibility(View.GONE);
                                    tvVirtualIdInfo.setVisibility(View.GONE);
                                    if (getAppType().equalsIgnoreCase(DIRECT))
                                        tvAadharMsg.setVisibility(View.GONE);
                                    else
                                        tvAadharMsg.setVisibility(View.VISIBLE);
                                } else if (getInterestedInMF()) {
                                    authType = VIRTUALID;
                                    rbVirtualId.setChecked(true);
                                    rbAadhar.setChecked(false);
                                    rbVirtualId.setVisibility(View.VISIBLE);
                                    input_layout_aadhar.setHint("Aadhaar Virtual Id");
                                    etAadhaar.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                                    tvAadharEg.setText("9888000011115555");
                                    tvAadharMsg.setText(Html.fromHtml(getResources().getString(R.string.vitrualIdmsg)));
                                    tvAadharMsg.setTextColor(getResources().getColor(R.color.market_text));
                                    tvAadharMsg.setVisibility(View.VISIBLE);
                                    authType = VIRTUALID;
                                    etAadhaar.setText("");
                                    btnSubmit.setVisibility(View.GONE);
                                    ivNote.setVisibility(View.VISIBLE);
                                    if (getAppType().equalsIgnoreCase(DIRECT)) {
                                        rbAadhar.setVisibility(View.GONE);
                                        if (openPaperlessAccount) {
                                            tvVirtualIdInfo.setText(Html.fromHtml(
                                                    getResources().getString(R.string.open_paperless_account)));
                                        } else {
                                            tvVirtualIdInfo.setText(Html.fromHtml(
                                                    getResources().getString(R.string.unable_to_genrate_virtualid)));
                                        }
                                        tvVirtualIdInfo.setVisibility(View.VISIBLE);
                                    } else {
                                        tvVirtualIdInfo.setVisibility(View.GONE);
                                    }
                                }
                                if (Integer.valueOf(status) == 0) {
                                    panDOBStatus = false;
//                                    dialogPANCVLVerified(msg);
                                    CustomDialog dialog = new CustomDialog(PANAdharRegistActivity.this, msg, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {

                                        @Override
                                        public void dialogAction(Action action, Object... data) {
                                            /*if (action == Action.OK) {
                                                fetchAddFromPAN();
                                            }*/
                                        }
                                    });
                                    dialog.show();
                                    etAadhaar.setText("");
                                    etAadhaar.setEnabled(false);
                                    cbAadhar.setEnabled(true);
                                    tvDocCheckList.setVisibility(View.GONE);
                                } else if (Integer.valueOf(status) == 1) {
                                    panDOBStatus = true;
                                    // etDOB.setEnabled(false);
                                    etAadhaar.setText("");
//                                    if (getInterestedInMF()) {
                                    etAadhaar.setEnabled(true);
                                    cbAadhar.setEnabled(true);
//                                    }
                                    tvDocCheckList.setVisibility(View.VISIBLE);
                                    etAadhaar.requestFocus();

                                    //Aadhar mandatory for all case 03-Oct
                                    /*if (getInterestedInMF()) {
                                        btnSubmit.setVisibility(View.VISIBLE);
                                        btnSubmit.setText(SUBMIT);
                                    }*/
                                } else if (Integer.valueOf(status) == 2) {
                                    commonMsgDialog(PANAdharRegistActivity.this, msg, false, "");
                                    panDOBStatus = true;
                                    // etDOB.setEnabled(false);
                                    etAadhaar.setText("");
//                                    if (getInterestedInMF()) {
                                    etAadhaar.setEnabled(true);
                                    cbAadhar.setEnabled(true);
//                                    }
                                    tvDocCheckList.setVisibility(View.VISIBLE);
                                    etAadhaar.requestFocus();
                                }
                            } else {
                                commonMsgDialog(PANAdharRegistActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            commonMsgDialog(PANAdharRegistActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                            MOSLCommon.alertDialog(PANAdharRegistActivity.this, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                        }

                    }
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });
        } else {
            MOSLCommon.alertDInternet(this);
        }


    }

    private void generateOTP(String adharNum) {

        Map inputMap = new HashMap<>();
        if (authType == AADHAAR) {
            inputMap.put("UIDType", "AADHAAR_NUMBER");
        } else if (authType == VIRTUALID) {
            inputMap.put("UIDType", "VIRTUAL_ID");
        }
        inputMap.put("AadhaarNumber", adharNum);
        inputMap.put("Userid", userID);
        inputMap.put("Otp", "");

        String flag = GENRATEAADHAROTP;
        String category = PANADDHARDETAILS;

        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);
            MOSLCommon.removeKeyboard(PANAdharRegistActivity.this, customProgress);
            WebServiceHandler.post(this, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgress.setVisibility(View.VISIBLE);
                    try {
                        String kuaResponseStatus = (response.isNull("KuaResponseStatus") || response.getString("KuaResponseStatus").equals("")) ? "" : response.getString("KuaResponseStatus");
                        String errorMsg = (response.isNull("ErrorMessage") || response.getString("ErrorMessage").equals("")) ? "" : response.getString("ErrorMessage");
                        String errorCode = (response.isNull("ErrorCode") || response.getString("ErrorCode").equals("")) ? "" : response.getString("ErrorCode");
                        String transactionIdAPI = (response.isNull("TransactionID") || response.getString("TransactionID").equals("")) ? "" : response.getString("TransactionID");
                        if (kuaResponseStatus.equalsIgnoreCase("true")) {
//                            valididateDialog("An OTP is sent to your registered number.", 5);

//                            CustomDialog dialogMsg = new CustomDialog(PANAdharRegistActivity.this, OTP_SEND_TO_REGISTERED_NO_MSG, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                            CustomDialog dialogMsg = new CustomDialog(PANAdharRegistActivity.this, errorMsg, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                                @Override
                                public void dialogAction(Action action, Object... data) {

                                }
                            });
                            dialogMsg.show();
//                            etOTP.setHint(ENTER_6_DIGIT_OTP);
                            transactionId = transactionIdAPI;
                            llOTPThumbLayout.setVisibility(View.VISIBLE);
                            llBaseOTPBioLayout.setVisibility(View.VISIBLE);

                            llAadhaarOtp.setVisibility(View.VISIBLE);
                            llCvlOtp.setVisibility(View.GONE);
                            rlOTP.setVisibility(View.VISIBLE);
                            etOTP.requestFocus();
                        } else {
                            llOTPThumbLayout.setVisibility(View.GONE);
                            rlOTP.setVisibility(View.GONE);
//                            valididateDialog(OTP_COULD_NOT_SEND_MSG, 5);
//                            CustomDialog dialogMsg = new CustomDialog(PANAdharRegistActivity.this, OTP_COULD_NOT_SEND_MSG, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                            CustomDialog dialogMsg = new CustomDialog(PANAdharRegistActivity.this, errorMsg, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                                @Override
                                public void dialogAction(Action action, Object... data) {

                                }
                            });
                            dialogMsg.show();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(PANAdharRegistActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                        MOSLCommon.alertDialog(PANAdharRegistActivity.this, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                    }
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });

        } else {
            MOSLCommon.alertDInternet(this);
        }
    }

    //Pancard number Format
    private boolean isValidPannumber(String pan) {

        String PAN_PATTEN = ("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        Pattern pattern = Pattern.compile(PAN_PATTEN);
        Matcher matcher = pattern.matcher(pan);
        return matcher.matches();
    }

    private void fetchAadharDetailsFromOTP(String otp) {

        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            if (authType == AADHAAR) {
                inputMap.put("UIDType", "AADHAAR_NUMBER");
            } else if (authType == VIRTUALID) {
                inputMap.put("UIDType", "VIRTUAL_ID");
            }
            inputMap.put("AadhaarNumber", getAadharNo());
            inputMap.put("Userid", userID);
            inputMap.put("IPAddress", getLocalIpAddress());
            inputMap.put("Otp", otp);
            inputMap.put("LogStatus", "1");
            inputMap.put("OtptxnId", transactionId);

            String flag = FETCHAADHARDETAILSOTP;
            String category = PANADDHARDETAILS;
            MOSLCommon.removeKeyboard(PANAdharRegistActivity.this, customProgress);

            WebServiceHandler.post(this, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                   /* {"Name":null,"DOB":null,"Email":null,"Gender":null,"Phone":null,"CO":null,
                            "Dist":null,"House":null,"LC":null,"LM":null,"PC":null,"PO":null,"State":null,
                            "Subdist":null,"VTC":null,"Street":null,"PHT":null,"ErrorMessage":"29ff5ae9f8da41c589cc6f973c822fb9",
                            "ErrorCode":"K-100","KuaResponseStatus":false}*/

                    if (response != null) {
                        try {
                            String kuaResponseStatus = (response.isNull("KuaResponseStatus") || response.getString("KuaResponseStatus").equals("")) ? "" : response.getString("KuaResponseStatus");
                            String errorMsg = (response.isNull("ErrorMessage") || response.getString("ErrorMessage").equals("")) ? "" : response.getString("ErrorMessage");
                            String errorCode = (response.isNull("ErrorCode") || response.getString("ErrorCode").equals("")) ? "" : response.getString("ErrorCode");
                            if (errorCode.equalsIgnoreCase("k-100") || kuaResponseStatus.equalsIgnoreCase(FALSE)) {
//                                valididateDialog(ENTER_CORRECT_OTP, 5);
//                                CustomDialog dialogMsg = new CustomDialog(PANAdharRegistActivity.this, ENTER_CORRECT_OTP, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                                CustomDialog dialogMsg = new CustomDialog(PANAdharRegistActivity.this, errorMsg, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                                    @Override
                                    public void dialogAction(Action action, Object... data) {

                                    }
                                });
                                dialogMsg.show();

                            }
                            if (!kuaResponseStatus.equals("")) {
                                if (Boolean.valueOf(kuaResponseStatus)) {
                                    OTPVerified = aadhaarOTPVerified;//2 for aadhaar verified
                                    setIsAadharVerified(true);
                                    cbAadhar.setEnabled(false);
//                                    valididateDialog("User Authentication Successful", 6);
                                    llBaseOTPBioLayout.setVisibility(View.GONE);
                                    tvVirtualIdInfo.setVisibility(View.GONE);
//                                    commonMsgDialog(PANAdharRegistActivity.this, USER_AUTHENTICATION_SUCCESSFUL, false, "");
                                    commonMsgDialog(PANAdharRegistActivity.this, errorMsg, false, "");
                                    btnSubmit.setVisibility(View.VISIBLE);
                                    btnSubmit.setText(SUBMIT);
//                                    submitPANAadhar();
                                } else {
                                    OTPVerified = noOTPVerified;//0 for not verified
                                    btnSubmit.setVisibility(View.GONE);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(PANAdharRegistActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                            MOSLCommon.alertDialog(PANAdharRegistActivity.this, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                        }
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
                    commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });
        } else {
            MOSLCommon.alertDInternet(this);
        }

    }

    private void cvlOTPApi() {

        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put("_userid", userID);

            String flag = CVLOTP;
            String category = PANADDHARDETAILS;
            MOSLCommon.removeKeyboard(PANAdharRegistActivity.this, customProgress);

            WebServiceHandler.post(this, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                   /* {"Name":null,"DOB":null,"Email":null,"Gender":null,"Phone":null,"CO":null,
                            "Dist":null,"House":null,"LC":null,"LM":null,"PC":null,"PO":null,"State":null,
                            "Subdist":null,"VTC":null,"Street":null,"PHT":null,"ErrorMessage":"29ff5ae9f8da41c589cc6f973c822fb9",
                            "ErrorCode":"K-100","KuaResponseStatus":false}*/

                    if (response != null) {
                        try {
                            String otp = (response.isNull("_OTP") || response.getString("_OTP").equals("")) ? "" : response.getString("_OTP");
                            String msg = (response.isNull("_msg") || response.getString("_msg").equals("")) ? "" : response.getString("_msg");
                            String status = (response.isNull("_Status") || response.getString("_Status").equals("")) ? "" : response.getString("_Status");

                            if (status.equals("0")) {
                                commonMsgDialog(PANAdharRegistActivity.this, msg, false, "");
                                OTPVerified = noOTPVerified;//0 for not verified
                            } else if (status.equals("1")) {
                                cvlOtp = otp;
                                llMobileNotMapped.setVisibility(View.GONE);
//                                etOTP.setHint(ENTER_4_DIGIT_OTP);
                                llCvlOtp.setVisibility(View.VISIBLE);
                                llAadhaarOtp.setVisibility(View.GONE);
                                tvDocCheckList.setVisibility(View.VISIBLE);
                                llBaseOTPBioLayout.setVisibility(View.VISIBLE);
                                llOTPThumbLayout.setVisibility(View.VISIBLE);
                                tvOTPHeading.setText(getString(R.string.cvl_otp_heading));
                                commonMsgDialog(PANAdharRegistActivity.this, msg, false, "");
                                OTPVerified = cvlOTPVerified;//1 for cvl opt verified
                                btnSubmit.setVisibility(View.GONE);
                                etCvlOTP.requestFocus();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(PANAdharRegistActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                            MOSLCommon.alertDialog(PANAdharRegistActivity.this, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                        }
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
                    commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });
        } else {
            MOSLCommon.alertDInternet(this);
        }
    }

    private void validateAadharAvailability() {
        if (isFundTransferDone() || isMfIpvDone())
            return;
        if (isAdharChanged || !getIsAadharVerified()) {
            isAdharChanged = false;
            if (MOSLCommon.isNetworkEnabled(this)) {
                customProgress.setVisibility(View.VISIBLE);

                Map inputMap = new HashMap<>();
               /* if (authType == AADHAAR) {
                    inputMap.put("UIDType", "AADHAAR_NUMBER");
                } else if (authType == VIRTUALID) {
                    inputMap.put("UIDType", "VIRTUAL_ID");
                }*/
                inputMap.put("AadharNumber", etAadhaar.getText().toString());
                inputMap.put("Userid", userID);
                inputMap.put("_Segment", segment);
                inputMap.put("_IsCVLverified", getCvlVerified());
                Date dob = EkycAppUtil.getDate(etDOB.getText().toString(), "dd/MM/yyyy");
                inputMap.put("_DOB", String.valueOf(EkycAppUtil.getDateStr(dob, "yyyy-MM-dd")));
                inputMap.put("IPAddress", getLocalIpAddress());

                String flag = VALIDATEAADHAAR;
                String category = PANADDHARDETAILS;
                MOSLCommon.removeKeyboard(PANAdharRegistActivity.this, customProgress);
                WebServiceHandler.post(this, flag, category, inputMap, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                if (response != null) {
                                    try {
                                        String isSuccess = (response.isNull("isSuccess") || response.getString("isSuccess").equals("")) ? "" : response.getString("isSuccess");
                                        String msg = (response.isNull("_msg") || response.getString("_msg").equals("")) ? "" : response.getString("_msg");
                                        //'n' for success
                                        if (isSuccess.equalsIgnoreCase("n")) {
                                            setAadharNo(etAadhaar.getText().toString());
//                                    setIsAadharVerified(true);
                                            removeKeyboard(PANAdharRegistActivity.this, etAadhaar);
                                    /*if (source.equals(ADD_SEGMENT)) {
                                        btnSubmit.setVisibility(View.VISIBLE);
                                    } else {*/
                                            if (getInterestedInMF()) {
                                                if (getAppType().equals(DIRECT)) {

                                                    if (getIsCVLVerified()) {
                                                        btnSubmit.setVisibility(View.VISIBLE);
                                                        btnSubmit.setText(SUBMIT);
                                                        llBaseOTPBioLayout.setVisibility(View.GONE);
                                                        llParentOTPBio.setVisibility(View.GONE);
                                                        showAuthenticationModeMsg(false, noMode);
                                                    } else if (getAadharNo().length() == 12) {
                                                        btnSubmit.setVisibility(View.VISIBLE);
                                                        btnSubmit.setText(SUBMIT);
                                                        llBaseOTPBioLayout.setVisibility(View.GONE);
                                                        llParentOTPBio.setVisibility(View.GONE);
                                                        showAuthenticationModeMsg(false, noMode);
                                                    } else {
                                                        llMobileNotMapped.setVisibility(View.VISIBLE);
                                                        tvDocCheckList.setVisibility(View.VISIBLE);
                                                        llBaseOTPBioLayout.setVisibility(View.VISIBLE);
                                                        llParentOTPBio.setVisibility(View.VISIBLE);
                                                        if (getAppType().equalsIgnoreCase(DIRECT)) {//call showHideOTPBioLayout method
//                                                            llImgThumb.setVisibility(View.GONE);
//                                                            llImgOTP.setVisibility(View.VISIBLE);
                                                            showHideOTPBioLayout();
                                                            showAuthenticationModeMsg(true, dcMode);
                                                        } else {//call showHideOTPBioLayout method
                                                            showAuthenticationModeMsg(true, brbaMode);
                                                            showHideOTPBioLayout();
//                                                            llImgThumb.setVisibility(View.VISIBLE);
//                                                            llImgOTP.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                } else if (getIsCVLVerified() && (getAppType().equals(PP) || getAppType().equals(CRM))) {
                                                    btnSubmit.setVisibility(View.VISIBLE);
                                                    btnSubmit.setText(SUBMIT);
                                                    llBaseOTPBioLayout.setVisibility(View.GONE);
                                                    llParentOTPBio.setVisibility(View.GONE);
                                                    showAuthenticationModeMsg(false, noMode);
                                                } else if (!getIsCVLVerified() && (getAppType().equals(PP) || getAppType().equals(CRM))) {
                                                /*    btnSubmit.setVisibility(View.VISIBLE);
                                                    llBaseOTPBioLayout.setVisibility(View.GONE);
                                                    llParentOTPBio.setVisibility(View.GONE);*/
                                                    llMobileNotMapped.setVisibility(View.VISIBLE);
                                                    tvDocCheckList.setVisibility(View.VISIBLE);
                                                    llBaseOTPBioLayout.setVisibility(View.VISIBLE);
                                                    llParentOTPBio.setVisibility(View.VISIBLE);
                                                    tvAuthenticationMode.setVisibility(View.VISIBLE);
                                                    if (getAppType().equalsIgnoreCase(DIRECT)) {//call showHideOTPBioLayout method
//                                                        llImgThumb.setVisibility(View.GONE);
//                                                        llImgOTP.setVisibility(View.VISIBLE);
                                                        showHideOTPBioLayout();
                                                        showAuthenticationModeMsg(true, dcMode);
                                                    } else {//call showHideOTPBioLayout method
                                                        showAuthenticationModeMsg(true, brbaMode);
                                                        showHideOTPBioLayout();
//                                                        llImgThumb.setVisibility(View.VISIBLE);
//                                                        llImgOTP.setVisibility(View.VISIBLE);
                                                    }

                                                }

                                            } else {
                                                if (etAadhaar.getText().toString().isEmpty())
                                                    return;
                                                llMobileNotMapped.setVisibility(View.VISIBLE);
                                                tvDocCheckList.setVisibility(View.VISIBLE);
                                                llBaseOTPBioLayout.setVisibility(View.VISIBLE);
                                                llParentOTPBio.setVisibility(View.VISIBLE);
                                                if (getAppType().equalsIgnoreCase(DIRECT)) {//call showHideOTPBioLayout method
//                                                    llImgThumb.setVisibility(View.GONE);
//                                                    llImgOTP.setVisibility(View.VISIBLE);
                                                    if (getAadharNo().length() == 12) {
                                                        btnSubmit.setVisibility(View.VISIBLE);
                                                        btnSubmit.setText(SUBMIT);
                                                        llBaseOTPBioLayout.setVisibility(View.GONE);
                                                        llParentOTPBio.setVisibility(View.GONE);
                                                        showAuthenticationModeMsg(false, noMode);
                                                    } else {
                                                        showHideOTPBioLayout();
                                                        showAuthenticationModeMsg(true, dcMode);
                                                    }
                                                } else {//call showHideOTPBioLayout method
                                                    showAuthenticationModeMsg(true, brbaMode);
                                                    showHideOTPBioLayout();
//                                                    llImgThumb.setVisibility(View.VISIBLE);
//                                                    llImgOTP.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        } else if (isSuccess.equalsIgnoreCase("y")) {
                                            //'y' for failure
                                            if (getAppType().equals(DIRECT)) {
                                                setIsAadharVerified(false);

                                            }
                                            showAuthenticationModeMsg(false, noMode);
                                            llParentOTPBio.setVisibility(View.GONE);
                                            setIsAadharVerified(false);
                                            etAadhaar.setText("");
//                                    valididateDialog(AADHAR_NUMBER_ERROR_MSG, 5);
                                    /*CustomDialog dialogMsg = new CustomDialog(PANAdharRegistActivity.this, msg, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                                        @Override
                                        public void dialogAction(Action action, Object... data) {

                                        }
                                    });
                                    dialogMsg.show();*/
                                            etAadhaar.setEnabled(true);
                                            commonMsgDialog(PANAdharRegistActivity.this, msg, false, "");
                                            removeKeyboard(PANAdharRegistActivity.this, etAadhaar);

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        customProgress.setVisibility(View.GONE);
                                        commonMsgDialog(PANAdharRegistActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                                        etAadhaar.setEnabled(true);
                                        cbAadhar.setEnabled(false);
//                                MOSLCommon.alertDialog(PANAdharRegistActivity.this, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                                    }
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
                                etAadhaar.setEnabled(true);
                                cbAadhar.setEnabled(false);
                                commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                customProgress.setVisibility(View.GONE);
                                etAadhaar.setEnabled(true);
                                cbAadhar.setEnabled(false);
                                commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable
                                    throwable, JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                customProgress.setVisibility(View.GONE);
                                etAadhaar.setEnabled(true);
                                cbAadhar.setEnabled(false);
                                commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                            }
                        }

                );
            } else {
                MOSLCommon.alertDInternet(this);
            }
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

    private void validatepan(final String PAN, final String userID) {

        if (isPANChanged) {
            isPANChanged = false;
            if (MOSLCommon.isNetworkEnabled(this)) {
                customProgress.setVisibility(View.VISIBLE);

                Map inputMap = new HashMap<>();
                inputMap.put("_PANNO", PAN);
                inputMap.put("userid", userID);

                String flag = VALIDATEPAN;
                String category = PANADDHARDETAILS;
                MOSLCommon.removeKeyboard(PANAdharRegistActivity.this, customProgress);
                WebServiceHandler.post(this, flag, category, inputMap, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        /*{
                            "_PANNO":"CAFPS8199M",
                                "aadhaarno":null,
                                "otp":null,
                                "userid":"1628052",
                                "_Status":"1",
                                "_msg":"As per IT PAN site your name is SHEFALI MAHENDRA SISODIA",
                                "_msg2":null,
                                "_msg3":"CVL Verfied",
                                "_DOB":null,
                                "_IsCVLverified":"1",
                                "_IsAadharVerified":null,
                                "_ApplicationType":null,
                                "_AddressMode":null,
                                "_IP":null,
                                "_Segment":null,
                                "_NameOnPAN":"SHEFALI MAHENDRA SISODIA"
                        }*/
                        /*{
                            "_PANNO":"AAKPN0667Q",
                                "aadhaarno":null,
                                "otp":null,
                                "userid":"1628052",
                                "_Status":"4",
                                "_msg":"You already have Equity A\/c with MOSL. If you wish to opt for Commodity A\/c. Kindly click on YES and proceed further procedure.",
                                "_msg2":"As per IT PAN site your name is PURUSHOTHAM NEELI",
                                "_msg3":"CVL Verfied",
                                "_DOB":null,
                                "_IsCVLverified":"1",
                                "_IsAadharVerified":null,
                                "_ApplicationType":null,
                                "_AddressMode":null,
                                "_IP":null,
                                "_Segment":"EQ",
                                "_NameOnPAN":"PURUSHOTHAM NEELI"
                        }*/
                        if (response != null) {
                            try {
                                String panNo = (response.isNull("_PANNO") || response.getString("_PANNO").equals("")) ? "" : response.getString("_PANNO");
                                String aadhaarNo = (response.isNull("aadhaarno") || response.getString("aadhaarno").equals("")) ? "" : response.getString("aadhaarno");
                                String otp = (response.isNull("otp") || response.getString("otp").equals("")) ? "" : response.getString("otp");
                                String userid = (response.isNull("userid") || response.getString("userid").equals("")) ? "" : response.getString("userid");
                                String status = (response.isNull("_Status") || response.getString("_Status").equals("")) ? "" : response.getString("_Status");
                                final String msg = (response.isNull("_msg") || response.getString("_msg").equals("")) ? "" : response.getString("_msg");
                                final String msg2 = (response.isNull("_msg2") || response.getString("_msg2").equals("")) ? "" : response.getString("_msg2");
                                final String msg3 = (response.isNull("_msg3") || response.getString("_msg3").equals("")) ? "" : response.getString("_msg3");
                                String nameOnPAN = (response.isNull("_NameOnPAN") || response.getString("_NameOnPAN").equals("")) ? "" : response.getString("_NameOnPAN");
                                String segment = (response.isNull("_Segment") || response.getString("_Segment").equals("")) ? "" : response.getString("_Segment");
                                String isCVLverified = (response.isNull("_IsCVLverified") || response.getString("_IsCVLverified").equals("")) ? "" : response.getString("_IsCVLverified");

                                if (MOSLCommon.isBlank(status)) {
                                    commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                                    customProgress.setVisibility(View.GONE);
                                    return;
                                }

                                final int intStatus = Integer.parseInt(status);
                                msg.replaceAll(" null ", "");
                                setPANStatus(status);
                                setPANno(panNo);
                                setPANName(nameOnPAN);
//                                setIsCVLVerified(isCVLverified != null && isCVLverified.equals("1") ? true : false);
                                if (intStatus == 4) {
                                    //  etDOB.setEnabled(false);
//                                    String msgConcat = msg + ". \n\n CVL status : " + msg3;
                                    CustomDialog dialogForSecondSeg = new CustomDialog(PANAdharRegistActivity.this, msg, true, true, false, "", YES, NO, new CustomDialog.CustomDialogListener() {

                                        @Override
                                        public void dialogAction(Action action, Object... data) {
                                            if (action == Action.OK) {


                                                CustomDialog dialogITName = new CustomDialog(PANAdharRegistActivity.this, msg2, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                                                    @Override
                                                    public void dialogAction(Action action, Object... data) {
                                                        if (action == Action.OK) {
                                                            CustomDialog dialogCVLStatus = new CustomDialog(PANAdharRegistActivity.this, msg3, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                                                                @Override
                                                                public void dialogAction(Action action, Object... data) {

                                                                    if (action == Action.OK) {
                                                                        Intent intent = new Intent(PANAdharRegistActivity.this, EkycMainActivity.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                }
                                                            });
                                                            dialogCVLStatus.show();
                                                        }
                                                    }
                                                });

                                                dialogITName.show();


                                            }

                                        }

                                    });
                                    dialogForSecondSeg.show();
                                    // panValidForAccAvailableDialog(msg, status, msg2, segment);
                                } else if (intStatus == 0) {

                                    CustomDialog dialog = new CustomDialog(PANAdharRegistActivity.this, msg, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {

                                        @Override
                                        public void dialogAction(Action action, Object... data) {
                                            if (action == Action.OK) {
                                                //  etDOB.setEnabled(false);
                                                btnSubmit.setVisibility(View.GONE);
                                                /*if (intStatus == 0) {
                                                    etDOB.setEnabled(false);
                                                    btnSubmit.setVisibility(View.GONE);
                                                } else {
                                                    etDOB.setEnabled(true);
                                                }*/
                                            }
                                        }
                                    });
                                    dialog.show();
//                                    informationDialog(msg, intStatus, msg2, "");
                                } else if (intStatus == 3) {
                                    CustomDialog dialogRetry = new CustomDialog(PANAdharRegistActivity.this, msg3, true, true, false, "", YES, NO, new CustomDialog.CustomDialogListener() {

                                        @Override
                                        public void dialogAction(Action action, Object... data) {

                                            if (action == Action.OK) {
                                                isPANChanged = true;
                                                validatepan(etPan.getText().toString(), userID);

                                            }
                                        }
                                    });
                                    dialogRetry.show();

                                } else {
//                                    etPan.setEnabled(false);
/*
                                    if (getInterestedInMF()) {
                                        etDOB.setEnabled(true);
//                                        etDOB.requestFocus();
                                    }
*/
                                    etDOB.setEnabled(true);
                                    String panMsg;
                                    if (!isBlank(msg)) {
                                        panMsg = msg + " & you are " + msg3;
                                    } else {
                                        panMsg = "You are " + msg3;
                                    }
                                    CustomDialog dialogITName = new CustomDialog(PANAdharRegistActivity.this, panMsg, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                                        @Override
                                        public void dialogAction(Action action, Object... data) {
                                            /*if (action == Action.OK) {
                                                CustomDialog dialogCVLStatus = new CustomDialog(PANAdharRegistActivity.this, msg3, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                                                    @Override
                                                    public void dialogAction(Action action, Object... data) {

                                                    }
                                                });
                                                dialogCVLStatus.show();
                                            }*/
                                        }
                                    });
                                    dialogITName.show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                customProgress.setVisibility(View.GONE);
                                commonMsgDialog(PANAdharRegistActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                                MOSLCommon.alertDialog(PANAdharRegistActivity.this, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                            }
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
                        commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(PANAdharRegistActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                });
            } else {
                MOSLCommon.alertDInternet(this);
            }
        }
    }

    @Override
    public void onBackPressed() {
    }

    /*
     * Method use to show hide OTP and Bio option
     * */
    private void showHideOTPBioLayout() {
        try {
            if (getAppType().equalsIgnoreCase(DIRECT)) {
                llImgThumb.setVisibility(View.GONE);
                llImgOTP.setVisibility(View.VISIBLE);
            } else {
                if (getAppType().equalsIgnoreCase(PP)) {
                    if (isDirectProspect()) { // Hide Bio Option in direct App
                        llImgThumb.setVisibility(View.GONE);
                        llImgOTP.setVisibility(View.VISIBLE);
                    } else {

                        //region old code before virtual id concept
                       /* if (getOtpVisible().equalsIgnoreCase("1")) {//Showhide OTP layout 1 for visible and 0 for hide
                            llImgOTP.setVisibility(View.VISIBLE);
                        } else if (getOtpVisible().equalsIgnoreCase("0")) {
                            llImgOTP.setVisibility(View.GONE);
                        }
                        if (getBioMetricVisible().equalsIgnoreCase("1")) {//Showhide Bio layout 1 for visible and 0 for hide
                            llImgThumb.setVisibility(View.VISIBLE);
                        } else if (getBioMetricVisible().equalsIgnoreCase("0")) {
                            llImgThumb.setVisibility(View.GONE);
                        }*/
                        //endregion
                        if (rbVirtualId.isChecked() && showVirtualOTP) {
                            llImgOTP.setVisibility(View.VISIBLE);
                        } else if (rbAadhar.isChecked() && showOTP) {
                            llImgOTP.setVisibility(View.VISIBLE);
                        } else {
                            llImgOTP.setVisibility(View.GONE);
                        }
                        if (rbVirtualId.isChecked() && showVirtualFinger) {
                            llImgThumb.setVisibility(View.VISIBLE);
                        } else if (rbAadhar.isChecked() && showFinger) {
                            llImgThumb.setVisibility(View.VISIBLE);
                        } else {
                            llImgThumb.setVisibility(View.GONE);
                        }
                    }
                } else if (getAppType().equalsIgnoreCase(CRM)) {
                    //region oldCode
                    /*if (getOtpVisibleForBranch().equalsIgnoreCase("1") || getOtpVisibleForBranch().equalsIgnoreCase("")) {//Showhide OTP layout 1 for visible and 0 for hide
                        llImgOTP.setVisibility(View.VISIBLE);
                    } else if (getOtpVisibleForBranch().equalsIgnoreCase("0")) {
                        llImgOTP.setVisibility(View.GONE);
                    }
                    if (getBioMetricVisibleForBranch().equalsIgnoreCase("1") || getBioMetricVisibleForBranch().equalsIgnoreCase("")) {//Showhide Bio layout 1 for visible and 0 for hide
                        llImgThumb.setVisibility(View.VISIBLE);
                    } else if (getBioMetricVisibleForBranch().equalsIgnoreCase("0")) {
                        llImgThumb.setVisibility(View.GONE);
                    }*/
                    if (rbVirtualId.isChecked() && showVirtualOTP) {
                        llImgOTP.setVisibility(View.VISIBLE);
                    } else if (rbAadhar.isChecked() && showOTP) {
                        llImgOTP.setVisibility(View.VISIBLE);
                    } else {
                        llImgOTP.setVisibility(View.GONE);
                    }
                    if (rbVirtualId.isChecked() && showVirtualFinger) {
                        llImgThumb.setVisibility(View.VISIBLE);
                    } else if (rbAadhar.isChecked() && showFinger) {
                        llImgThumb.setVisibility(View.VISIBLE);
                    } else {
                        llImgThumb.setVisibility(View.GONE);
                    }
                    //endregion
//                    llImgThumb.setVisibility(View.VISIBLE);
//                    llImgOTP.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitData() {
        if (btnSubmit.getText().equals(NEXT)) {
            if (isSIPChange) {
                submitPANAadhar(true);
            } else {
                if (getInterestedInMF()) {
                    if (!getIsAadharVerified()) {
                        if (getIsCVLVerified()) {
//                                                                 updateAddressDetails(userID, CVL);
                            callApplyNowActivity(PANAdharRegistActivity.this);
                        } else {
//                                                                 updateAddressDetails(userID, MANUAL);
                            callApplyNowActivity(PANAdharRegistActivity.this);
                        }
                    } else {
                        if (getIsCVLVerified()) {
//                                                                 updateAddressDetails(userID, CVL);
                            callApplyNowActivity(PANAdharRegistActivity.this);
                        } else {
//                                                            getAddress(false, true, true);
                            Intent intent = new Intent(PANAdharRegistActivity.this, AddressDetailsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                } else if (!getIsCVLVerified() && !getIsAadharVerified()) {
                    if (isThisSecondProductFlow()) {
                        callApplyNowActivity(PANAdharRegistActivity.this);
                    } else if (getAppType().equals(DIRECT)) {
                        callApplyNowActivity(PANAdharRegistActivity.this);
                    }
                } else {
                    Intent intent = new Intent(PANAdharRegistActivity.this, AddressDetailsActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        } else if (getInterestedInMF()) {
            if (etPan.getText().toString().trim().equals("")) {
                etPan.setError("PAN no. required");
            } else if (!isValidPannumber(etPan.getText().toString())) {
                etPan.setError("PAN no. is invalid");
            } else if (etDOB.getText().toString().equals("")) {
                etDOB.setError("DOB is required");
            } else if (etAadhaar.getText().toString().trim().equals("")) {
                setIsAadharVerified(false);
                setPANno(etPan.getText().toString());
                setDob(etDOB.getText().toString());
//                setAadharNo("0");

                if (getAppType().equals(DIRECT)) {
                    submitPANAadhar(true);
                } else if ((getAppType().equals(CRM) || getAppType().equals(PP)) && !getIsCVLVerified()) {
                    CustomDialog cdPhysicalFrom = new CustomDialog(PANAdharRegistActivity.this, PHYSICAL_FORM_FILLING,
                            true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                        @Override
                        public void dialogAction(Action action, Object... data) {

                        }
                    });
                    cdPhysicalFrom.show();
                }
                                                        /* else if (!getIsCVLVerified() && (getAppType().equals(PP) || getAppType().equals(CRM))) {

                                                             submitPANAadhar();

                                                         }*/
                else if (getIsCVLVerified() && (getAppType().equals(PP) || getAppType().equals(CRM))) {
                    cvlOTPApi();
                }
            } else if (etAadhaar.length() < 12) {
                etAadhaar.setError("Aadhar no. is invalid");
            } else {
                if (getAppType().equals(DIRECT)) {
                    if (getIsCVLVerified()) {
                        if (OTPVerified == noOTPVerified) {
//                                                                     showEquityMFOptionDialog(true, false);
                            submitPANAadhar(true);
                        } else if (OTPVerified == aadhaarOTPVerified && aadhaarConsentAgreed) {
                            submitPANAadhar(true);
                        } else if (OTPVerified == aadhaarOTPVerified) {
                            allCondSatisfied();
                        }
                    } else {
                        if (OTPVerified == aadhaarOTPVerified && aadhaarConsentAgreed) {
                            submitPANAadhar(true);
                        } else if (OTPVerified == aadhaarOTPVerified) {
                            showEquityMFOptionDialog(false, false);
                        } else if (getAppType().equalsIgnoreCase(DIRECT) && rbAadhar.isChecked()) {
                            submitPANAadhar(true);
                        }

                                                                /* if (OTPVerified == noOTPVerified) {
                                                                     showEquityMFOptionDialog(false, true);
                                                                 } else {
                                                                     submitPANAadhar();
                                                                 }*/
                    }
                } else if (OTPVerified == noOTPVerified) {
                    if (getInterestedInMF() && getIsCVLVerified() && (getAppType().equals(PP) || getAppType().equals(CRM))) {
                        showEquityMFOptionDialog(true, false);
                    } else if (getInterestedInMF() && !getIsCVLVerified() && (getAppType().equals(PP) || getAppType().equals(CRM))) {
                        showEquityMFOptionDialog(true, false);
                    }
                } else if (OTPVerified == cvlOTPVerified) {
                    submitPANAadhar(true);
                } else if (OTPVerified == aadhaarOTPVerified && aadhaarConsentAgreed) {
                    submitPANAadhar(true);
                } else if (OTPVerified == aadhaarOTPVerified) {
                    showEquityMFOptionDialog(false, false);
                }
            }
        } else {
            if (etPan.getText().toString().trim().equals("")) {
                etPan.setError("PAN no. required");
            } else if (!isValidPannumber(etPan.getText().toString())) {
                etPan.setError("PAN no. is invalid");
            } else if (etDOB.getText().toString().equals("")) {
                etDOB.setError("DOB is required");
            } else if (etAadhaar.getText().toString().trim().equals("")) {
                if (getAppType().equals(DIRECT)) {
                    if (cbAadhar.isChecked()) {
                        etAadhaar.setError(null);
                        if (isValidPannumber(etPan.getText().toString()) && !etDOB.getText().toString().equals("")) {
                            setPANno(etPan.getText().toString());
                            setDob(etDOB.getText().toString());
                            setAadharNo("0");
                            setIsAadharVerified(false);
                            submitPANAadhar(true);
                        }
                    } else {
                        etAadhaar.setError("Aadhar no. required");
                    }
                } else {
                    etAadhaar.setError("Aadhar no. required");
                }
            } else if (rbAadhar.isChecked() ? etAadhaar.length() < 12 : etAadhaar.length() < 16) {
                if (rbAadhar.isChecked()) {
                    etAadhaar.setError("Aadhar no. is invalid");
                } else {
                    etAadhaar.setError("Virtual id is invalid");
                }

            } else if (isValidPannumber(etPan.getText().toString()) && (rbAadhar.isChecked() ? etAadhaar.length() == 12 : etAadhaar.length() == 16) && !etDOB.getText().toString().equals("")) {
                if (panDOBStatus) {
                    if (getAppType().equalsIgnoreCase(DIRECT) && etAadhaar.getText().toString().length() == 12) {
                        setPANno(etPan.getText().toString());
                        setDob(etDOB.getText().toString());
                        setAadharNo(etAadhaar.getText().toString());
                        setIsAadharVerified(false);
                        submitPANAadhar(true);
                    } else {
                        allCondSatisfied();
                    }
                } else {
                    //                                                         dialogPANCVLVerified("DOB is not verified, Retry ?");
                                                          /*   CustomDialog dialogMsg = new CustomDialog(PANAdharRegistActivity.this, "DOB is not verified, Retry ?", true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                                                                 @Override
                                                                 public void dialogAction(Action action, Object... data) {

                                                                 }
                                                             });
                                                             dialogMsg.show();*/
                    if (etDOB.isEnabled()) {
                        MOSLCommon.commonMsgDialog(PANAdharRegistActivity.this, "DOB is not verified, Retry ?", false, "");
                    }
                }
            }
        }
    }

    private void sipDialog() {
        final Dialog dialogSip = new Dialog(PANAdharRegistActivity.this);
        dialogSip.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSip.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSip.setCanceledOnTouchOutside(false);
        dialogSip.getWindow().setWindowAnimations(R.style.animationName);
        dialogSip.setContentView(R.layout.dialog_sip_intent);
        dialogSip.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogSip.setCanceledOnTouchOutside(false);

        Button btnOk = (Button) dialogSip.findViewById(R.id.btnOk);
        Button btnSkip = (Button) dialogSip.findViewById(R.id.btnSkip);
        TextView tvHeading = (TextView) dialogSip.findViewById(R.id.tvHeading);
        CheckBox cbSIPDialog = (CheckBox) dialogSip.findViewById(R.id.cbSIPDialog);
        ImageView ivClose = (ImageView) dialogSip.findViewById(R.id.ivClose);

        btnOk.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setSipIntent("1");//true
                submitData();
                dialogSip.dismiss();
            }
        });

        btnSkip.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setSipIntent("0");//false
                submitData();
                dialogSip.dismiss();
            }
        });

        cbSIPDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSipIntent("1");//true
                submitData();
                dialogSip.dismiss();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSip.dismiss();
            }
        });
        dialogSip.show();
    }

    /*
     *Enter BA branch code dialog code
     * */
    private void enterBranchCodeDialog() {
        enterBaBranchCodeDialog = new Dialog(PANAdharRegistActivity.this);
        enterBaBranchCodeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        enterBaBranchCodeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        enterBaBranchCodeDialog.setCanceledOnTouchOutside(false);

        enterBaBranchCodeDialog.getWindow().setWindowAnimations(R.style.animationName);
        enterBaBranchCodeDialog.setContentView(R.layout.dialog_enter_ba_branch_code);
        enterBaBranchCodeDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        enterBaBranchCodeDialog.setCanceledOnTouchOutside(false);

        ImageView ivClose = (ImageView) enterBaBranchCodeDialog.findViewById(R.id.ivClose);
        Button btnOk = (Button) enterBaBranchCodeDialog.findViewById(R.id.btnOk);
        customProgress = (LinearLayout) enterBaBranchCodeDialog.findViewById(R.id.customProgress);
        etEnterBaBranchCode = (EditText) enterBaBranchCodeDialog.findViewById(R.id.etEnterBaBranchCode);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBlank(etEnterBaBranchCode.getText().toString())) {
//                    getBADetails(etEnterBaBranchCode.getText().toString());
                    if (!etEnterBaBranchCode.getText().toString().equalsIgnoreCase(getBaFranchiseeCode())) {
                        getBADetails(etEnterBaBranchCode.getText().toString());
                    } else {
                        commonMsgDialog(PANAdharRegistActivity.this, getResources().getString(R.string.ba_branch_code_error), false, "");
                        etEnterBaBranchCode.setText("");
                    }
                }
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterBaBranchCodeDialog.dismiss();
            }
        });
        enterBaBranchCodeDialog.show();
    }

    //  GETBADETAILS
    private void getBADetails(final String branchCode) {
        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put("_AppLoginId", getAppLoginID());
            inputMap.put("_BACODE", branchCode);

            String flag = GETBADETAILS;
            String category = SIGNUP;

            WebServiceHandler.post(this, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgress.setVisibility(View.GONE);
                    if (response != null) {
                        try {
                            showBADetailDailog(response, branchCode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                }
            });
        } else {
            MOSLCommon.alertDInternet(this);
        }
    }

    ////
    private void showBADetailDailog(JSONObject response, final String branchCode1) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.animationName);
        dialog.setContentView(R.layout.dialog_ba_branch_code_detail);
        customProgressBADetails = (LinearLayout) dialog.findViewById(R.id.customProgress);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        TextView tvFirmName = (TextView) dialog.findViewById(R.id.tvFirmName);
        TextView tvFirmCode = (TextView) dialog.findViewById(R.id.tvFirmCode);
        TextView tvFirmAddress = (TextView) dialog.findViewById(R.id.tvFirmAddress);
        TextView tvPartnerCode = (TextView) dialog.findViewById(R.id.tvPartnerCode);
        Button btnEdit = (Button) dialog.findViewById(R.id.btnEdit);
        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEnterBaBranchCode.setText("");
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterBaBranchCodeDialog.dismiss();
                if (isDirectProspect() || getAppType().equalsIgnoreCase(DIRECT)) { //call update API if direct
                    updateBADetails(branchCode1, dialog);
                } else { // for PP show Msg on ok click call update API
                    CustomDialog customDialog = new CustomDialog(PANAdharRegistActivity.this, getResources().getString(R.string.ba_branch_code_change_pp), true,
                            false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                        @Override
                        public void dialogAction(Action action, Object... data) {
                            if (action == Action.OK) {
                                updateBADetails(branchCode1, dialog);
                            }
                        }
                    });
                    customDialog.show();
                }
            }
        });
        JSONArray jsonArray = null, jsonArray1 = null;
        try {
            if (response.has("BADETAILS")) {
                jsonArray = response.getJSONArray("BADETAILS");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject object = jsonArray.getJSONObject(i);
                    String loginId = (object.isNull("LoginId") || object.getString("LoginId").equals("")) ? "" : object.getString("LoginId");
                    String userType = (object.isNull("UserType") || object.getString("UserType").equals("")) ? "" : object.getString("UserType");
                    String branchCode = (object.isNull("BranchCode") || object.getString("BranchCode").equals("")) ? "" : object.getString("BranchCode");
                    String subBrokerCode = (object.isNull("SubBrokerCode") || object.getString("SubBrokerCode").equals("")) ? "" : object.getString("SubBrokerCode");
                    String area = (object.isNull("Area") || object.getString("Area").equals("")) ? "" : object.getString("Area");
                    String region = (object.isNull("Region") || object.getString("Region").equals("")) ? "" : object.getString("Region");
                    String TraderCode = (object.isNull("TraderCode") || object.getString("TraderCode").equals("")) ? "" : object.getString("TraderCode");
                    String active = (object.isNull("Active") || object.getString("Active").equals("")) ? "" : object.getString("Active");
                    String product = (object.isNull("Product") || object.getString("Product").equals("")) ? "" : object.getString("Product");
                    String branch_Name = (object.isNull("Branch_Name") || object.getString("Branch_Name").equals("")) ? "" : object.getString("Branch_Name");
                    String clCode = (object.isNull("ClCode") || object.getString("ClCode").equals("")) ? "" : object.getString("ClCode");
                    String emailID = (object.isNull("EmailID") || object.getString("EmailID").equals("")) ? "" : object.getString("EmailID");
                    String city = (object.isNull("City") || object.getString("City").equals("")) ? "" : object.getString("City");
                    String mobileNo = (object.isNull("MobileNo") || object.getString("MobileNo").equals("")) ? "" : object.getString("MobileNo");
                    String default_SubBroker = (object.isNull("Default_SubBroker") || object.getString("Default_SubBroker").equals("")) ? "" : object.getString("Default_SubBroker");
                    tvFirmName.setText(branch_Name);
                    tvFirmCode.setText(branchCode);
                    tvFirmAddress.setText("");
                    tvPartnerCode.setText(clCode);
                    dialog.show();
                }
            }
            if (response.has("BADETAILS1")) {
                String massage = "";
                jsonArray1 = response.getJSONArray("BADETAILS1");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject = jsonArray1.getJSONObject(i);
                    massage = (jsonObject.isNull("Message") || jsonObject.getString("Message").equals("")) ? "" : jsonObject.getString("Message");
                }
                enterBaBranchCodeDialog.dismiss();
                CustomDialog customDialog = new CustomDialog(PANAdharRegistActivity.this, massage, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                    @Override
                    public void dialogAction(Action action, Object... data) {
                        if (action == Action.OK) {
                            enterBranchCodeDialog();
                        }
                    }
                });
                customDialog.show();
//                commonMsgDialog(PANAdharRegistActivity.this, massage, false, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATEBADETAILS
    private void updateBADetails(final String branchCode, final Dialog dialog) {

        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgressBADetails.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put("_userid", userID);
            inputMap.put("_AppLoginId", getAppLoginID());
            inputMap.put("_BACODE", branchCode);
            inputMap.put("_IsDirectBA", isDirectProspect() ? "1" : "0");
            inputMap.put("_ApplicationType", getAppType());
            inputMap.put("_PreviousBACode", getBaFranchiseeCode());
            String flag = UPDATEBADETAILS;
            String category = SIGNUP;

            WebServiceHandler.post(this, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgressBADetails.setVisibility(View.GONE);
                    dialog.dismiss();
                    if (response != null) {
                        try {
                            String status = (response.isNull("_Status") || response.getString("_Status").equals("")) ? "" : response.getString("_Status");
                            if (Integer.parseInt(status) == 1) {
                                if (isDirectProspect() || getAppType().equalsIgnoreCase(DIRECT)) {//Set app type to franchise and isDirectProspect flag true if BA branch code entered
                                    setBaFranchiseeCode(branchCode);
                                    setAppType(PP);
                                    setIsDirectProspect(true);
                                    llSIPIntent.setVisibility(View.GONE);
                                } else {//Goto postLogin Activity as code is changed for Franchise
                                    Intent intent = new Intent(PANAdharRegistActivity.this, PostLoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                commonMsgDialog(PANAdharRegistActivity.this, getResources().getString(R.string.ba_branch_code_error), false, "");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgressBADetails.setVisibility(View.GONE);
                    dialog.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    customProgressBADetails.setVisibility(View.GONE);
                    dialog.dismiss();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgressBADetails.setVisibility(View.GONE);
                    dialog.dismiss();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgressBADetails.setVisibility(View.GONE);
                    dialog.dismiss();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgressBADetails.setVisibility(View.GONE);
                    dialog.dismiss();
                }
            });
        } else {
            MOSLCommon.alertDInternet(this);
        }
    }

    private void listOfIntermediariesDialog() {

        final Dialog dialog = new Dialog(PANAdharRegistActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_aadhaar_intermediaries);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    private void uidaiApi() {
        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put("userid", getUserID());
            inputMap.put("_ApplicationType", getAppType());
            inputMap.put("_IsDirectBA", isDirectProspect() ? "1" : "0");

            WebServiceHandler.post(this, GETAUTHENTICATIONTYPE, PANADDHARDETAILS, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgress.setVisibility(View.GONE);
                    if (statusCode == 200) {
                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject object = response.getJSONObject(i);
                                    String authenticationType = (object.isNull("AUTHENTICATION_TYPE") || object.getString("AUTHENTICATION_TYPE").equals("")) ? "" : object.getString("AUTHENTICATION_TYPE");
                                    String authenticationTypeVisible = (object.isNull("AUTHENTICATION_TYPE_VISIBLE") || object.getString("AUTHENTICATION_TYPE_VISIBLE").equals("")) ? "" : object.getString("AUTHENTICATION_TYPE_VISIBLE");
                                    String otpVisible = (object.isNull("OTP_VISIBLE") || object.getString("OTP_VISIBLE").equals("")) ? "" : object.getString("OTP_VISIBLE");
                                    String bioVisible = (object.isNull("BIO_VISIBLE") || object.getString("BIO_VISIBLE").equals("")) ? "" : object.getString("BIO_VISIBLE");
                                    String irisVisible = (object.isNull("IRIS_VISIBLE") || object.getString("IRIS_VISIBLE").equals("")) ? "" : object.getString("IRIS_VISIBLE");
                                    if (authenticationType.equalsIgnoreCase(AADHAAR) && authenticationTypeVisible.equalsIgnoreCase(VISIBLE)) {

                                        showOTP = otpVisible.equalsIgnoreCase(VISIBLE);
                                        showFinger = bioVisible.equalsIgnoreCase(VISIBLE);
                                        showIris = irisVisible.equalsIgnoreCase(VISIBLE);
                                        rbAadhar.setVisibility(View.VISIBLE);
                                    }
                                    if (authenticationType.equalsIgnoreCase(VIRTUALID) && authenticationTypeVisible.equalsIgnoreCase(VISIBLE)) {
                                        showVirtualOTP = otpVisible.equalsIgnoreCase(VISIBLE);
                                        showVirtualFinger = bioVisible.equalsIgnoreCase(VISIBLE);
                                        showVirtualIris = irisVisible.equalsIgnoreCase(VISIBLE);
                                        rbVirtualId.setVisibility(View.VISIBLE);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            if (llAadhar.getVisibility() == View.VISIBLE && rbAadhar.getVisibility() == View.VISIBLE && rbVirtualId.getVisibility() == View.VISIBLE) {
                                authType = AADHAAR;
                                rbAadhar.setChecked(true);
                                ivNote.setVisibility(View.VISIBLE);
                            } else if (llAadhar.getVisibility() == View.VISIBLE && rbAadhar.getVisibility() == View.VISIBLE) {
                                authType = AADHAAR;
                                rbAadhar.setChecked(true);
                                rbVirtualId.setChecked(false);
                                rbVirtualId.setVisibility(View.GONE);
                                input_layout_aadhar.setHint("Aadhaar No");
                                etAadhaar.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                                tvAadharEg.setText("988800001111");
                                tvAadharMsg.setText(getResources().getString(R.string.aadharmsg));
                                tvAadharMsg.setTextColor(getResources().getColor(R.color.market_text));
                                authType = AADHAAR;
                                etAadhaar.setText("");
                                ivNote.setVisibility(View.GONE);
                                if (getAppType().equalsIgnoreCase(DIRECT))
                                    tvAadharMsg.setVisibility(View.GONE);
                                else
                                    tvAadharMsg.setVisibility(View.VISIBLE);
                            } else if (llAadhar.getVisibility() == View.VISIBLE && rbVirtualId.getVisibility() == View.VISIBLE) {
                                authType = VIRTUALID;
                                rbVirtualId.setChecked(true);
                                rbAadhar.setChecked(false);
                                rbAadhar.setVisibility(View.GONE);
                                input_layout_aadhar.setHint("Aadhaar Virtual Id");
                                etAadhaar.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                                tvAadharEg.setText("9888000011115555");
                                tvAadharMsg.setText(Html.fromHtml(getResources().getString(R.string.vitrualIdmsg)));
                                tvAadharMsg.setTextColor(getResources().getColor(R.color.market_text));
                                tvAadharMsg.setVisibility(View.VISIBLE);
                                authType = VIRTUALID;
                                etAadhaar.setText("");
                                ivNote.setVisibility(View.VISIBLE);
                                if (getAppType().equalsIgnoreCase(DIRECT)) {
                                    if (openPaperlessAccount) {
                                        tvVirtualIdInfo.setText(Html.fromHtml(
                                                getResources().getString(R.string.open_paperless_account)));
                                    } else {
                                        tvVirtualIdInfo.setText(Html.fromHtml(
                                                getResources().getString(R.string.unable_to_genrate_virtualid)));
                                    }
                                    tvVirtualIdInfo.setVisibility(View.VISIBLE);
                                } else {
                                    tvVirtualIdInfo.setVisibility(View.GONE);
                                }
                            }

                            if (getAppType().equalsIgnoreCase(DIRECT) && !getIsAadharVerified()) {
                                if (getAppType().equalsIgnoreCase(DIRECT)) {
                                    if (openPaperlessAccount) {
                                        tvVirtualIdInfo.setText(Html.fromHtml(
                                                getResources().getString(R.string.open_paperless_account)));
                                    } else {
                                        tvVirtualIdInfo.setText(Html.fromHtml(
                                                getResources().getString(R.string.unable_to_genrate_virtualid)));
                                    }
                                    tvVirtualIdInfo.setVisibility(View.VISIBLE);
                                } else {
                                    tvVirtualIdInfo.setVisibility(View.GONE);
                                }
                            } else if (getAadharNo() != null && getAadharNo().length() == 12) {
                                rbAadhar.setChecked(true);
                                rbVirtualId.setChecked(false);
                                input_layout_aadhar.setHint("Aadhaar No");
                                etAadhaar.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                                tvAadharEg.setText("988800001111");
                                tvAadharMsg.setText(getResources().getString(R.string.aadharmsg));
                                tvAadharMsg.setTextColor(getResources().getColor(R.color.market_text));
                                authType = AADHAAR;
                                ivNote.setVisibility(View.GONE);
                                if (getAppType().equalsIgnoreCase(DIRECT))
                                    tvAadharMsg.setVisibility(View.GONE);
                                else
                                    tvAadharMsg.setVisibility(View.VISIBLE);
                            } else if (getAadharNo() != null && getAadharNo().length() == 16) {
                                rbVirtualId.setChecked(true);
                                rbAadhar.setChecked(false);
                                input_layout_aadhar.setHint("Aadhaar Virtual Id");
                                etAadhaar.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                                tvAadharEg.setText("9888000011115555");
                                tvAadharMsg.setText(Html.fromHtml(getResources().getString(R.string.vitrualIdmsg)));
                                tvAadharMsg.setTextColor(getResources().getColor(R.color.market_text));
                                tvAadharMsg.setVisibility(View.VISIBLE);
                                etAadhaar.setText(getAadharNo());
                                authType = VIRTUALID;
                                ivNote.setVisibility(View.VISIBLE);
                                if (getAppType().equalsIgnoreCase(DIRECT)) {
                                    if (openPaperlessAccount) {
                                        tvVirtualIdInfo.setText(Html.fromHtml(
                                                getResources().getString(R.string.open_paperless_account)));
                                    } else {
                                        tvVirtualIdInfo.setText(Html.fromHtml(
                                                getResources().getString(R.string.unable_to_genrate_virtualid)));
                                    }

                                    tvVirtualIdInfo.setVisibility(View.VISIBLE);
                                } else {
                                    tvVirtualIdInfo.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                }
            });
        } else {
            MOSLCommon.alertDInternet(this);
        }
    }
}
