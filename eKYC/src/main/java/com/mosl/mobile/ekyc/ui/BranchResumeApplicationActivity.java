package com.mosl.mobile.ekyc.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.register.PANAdharRegistActivity;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import cz.msebera.android.httpclient.Header;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.mosl.mobile.ekyc.common.MOSLCommon.commonMsgDialog;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isBlank;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setExistingSegment;
import static com.mosl.mobile.ekyc.model.CommonModel.clearAllCommonModelFields;
import static com.mosl.mobile.ekyc.model.CommonModel.getAddressMode;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppType;
import static com.mosl.mobile.ekyc.model.CommonModel.getInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsAadharVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsCVLVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getPANno;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.isAddNewSegment;
import static com.mosl.mobile.ekyc.model.CommonModel.isCommodityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.isCommoditySegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.isEquityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.isEquitySegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.isMfSegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.isOffline;
import static com.mosl.mobile.ekyc.model.CommonModel.setAadhaarByPass;
import static com.mosl.mobile.ekyc.model.CommonModel.setAadharNo;
import static com.mosl.mobile.ekyc.model.CommonModel.setAddNewSegment;
import static com.mosl.mobile.ekyc.model.CommonModel.setAddressMode;
import static com.mosl.mobile.ekyc.model.CommonModel.setBaFranchiseeCode;
import static com.mosl.mobile.ekyc.model.CommonModel.setBioMetricVisibleForBranch;
import static com.mosl.mobile.ekyc.model.CommonModel.setCity;
import static com.mosl.mobile.ekyc.model.CommonModel.setDob;
import static com.mosl.mobile.ekyc.model.CommonModel.setDownloadPdfUrl;
import static com.mosl.mobile.ekyc.model.CommonModel.setEqSchemeId;
import static com.mosl.mobile.ekyc.model.CommonModel.setEsignService;
import static com.mosl.mobile.ekyc.model.CommonModel.setGotoFundTransfer;
import static com.mosl.mobile.ekyc.model.CommonModel.setGotoReviewEdit;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsAadharVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsAllOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsAllSelected;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsBankDetailDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCVLVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCommodityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCommodityOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCommoditySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCurrencyOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsDerivativeOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsEquityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsEquityIpvDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsEquityOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsEquitySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsIPVDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsMfIpvDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsNomineeMandatory;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsOffline;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsOfflineCliencodeGenrated;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsOfflineDocumentUploadedDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsPersonalDetailDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsReviewEditDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsSchemeDetailDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsSegmentSelected;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsWorkDetailDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setOnlineOfflineFlag;
import static com.mosl.mobile.ekyc.model.CommonModel.setOtpVisibleForBranch;
import static com.mosl.mobile.ekyc.model.CommonModel.setPANName;
import static com.mosl.mobile.ekyc.model.CommonModel.setPANno;
import static com.mosl.mobile.ekyc.model.CommonModel.setRegisteredName;
import static com.mosl.mobile.ekyc.model.CommonModel.setSipIntent;
import static com.mosl.mobile.ekyc.model.CommonModel.setTokenKey;
import static com.mosl.mobile.ekyc.model.CommonModel.setUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.setUserType;

/**
 * Created by Vijay on 26/09/2017.
 */

public class BranchResumeApplicationActivity extends FragmentActivity implements MOSLConstants {

    private boolean prospectTable;
    private Map<String, Map<String, String>> allStageMap;
    private Dialog dialog;
    public LinearLayout customProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.branch_resume_application);
        customProgress = (LinearLayout) findViewById(R.id.customProgress);
        Intent intent = getIntent();
        getProspectDetails(intent.getStringExtra("UserId"));
    }

    private void getProspectDetails(final String userID) {
        setBooleanValueToFalse();
        setAddNewSegment(true);

        if (MOSLCommon.isNetworkEnabled(BranchResumeApplicationActivity.this)) {
            customProgress.setVisibility(View.VISIBLE);

            Map inputMap = new HashMap<>();
            inputMap.put("_userid", userID);
            String flag = GETPROSPECTDETAILS;
            String category = LOGIN;
            WebServiceHandler.post(BranchResumeApplicationActivity.this, flag, category, inputMap, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            if (response != null) {
                                prospectTable = false;
                                allStageMap = new HashMap<String, Map<String, String>>();

                                try {
                                    if (response.has("PROSPECTDETAILS3")) {
                                        JSONArray jsonArraProspectDetails3 = response.getJSONArray("PROSPECTDETAILS3");

                                        if (jsonArraProspectDetails3.length() != 0) {
                                            for (int i = 0; i < jsonArraProspectDetails3.length(); i++) {
                                                JSONObject object = jsonArraProspectDetails3.getJSONObject(i);
                                                String product = (object.isNull(PRODUCT) || object.getString(PRODUCT).equals("")) ? "" : object.getString(PRODUCT);
                                                if (product.equalsIgnoreCase(EQUITY) || product.equalsIgnoreCase(PMS)) {
                                                    setIsEquityOriginal(true);
                                                }
                                                if (product.equalsIgnoreCase(COMMODITY)) {
                                                    setIsCommodityOriginal(true);
                                                }
                                                if (product.equalsIgnoreCase(Derivative)) {
                                                    setIsDerivativeOriginal(true);
                                                }
                                                if (product.equalsIgnoreCase(CURRENCY)) {
                                                    setIsCurrencyOriginal(true);
                                                }
                                            }
                                        }
                                    }

                                    if (response.has("PROSPECTDETAILS2")) {
                                        JSONArray jsonArraProspectDetails2 = response.getJSONArray("PROSPECTDETAILS2");
                                        if (jsonArraProspectDetails2.length() != 0) {
                                            for (int i = 0; i < jsonArraProspectDetails2.length(); i++) {
                                                JSONObject object = jsonArraProspectDetails2.getJSONObject(i);
                                                String userId = (object.isNull("USERID") || object.getString("USERID").equals("")) ? "" : object.getString("USERID");
                                                String aadharNo = (object.isNull("AADHARNO") || object.getString("AADHARNO").equals("")) ? "" : object.getString("AADHARNO");
                                                String panNo = (object.isNull("PANNO") || object.getString("PANNO").equals("")) ? "" : object.getString("PANNO");
                                                String iscvlVerified = (object.isNull("ISCVLVERIFIED") || object.getString("ISCVLVERIFIED").equals("")) ? "" : object.getString("ISCVLVERIFIED").trim();
                                                String isAddharVerified = (object.isNull("ISADDHARVERIFIED") || object.getString("ISADDHARVERIFIED").equals("")) ? "" : object.getString("ISADDHARVERIFIED").trim();
                                                String addressMode = (object.isNull("ADDRESSMODE") || object.getString("ADDRESSMODE").equals("")) ? "" : object.getString("ADDRESSMODE");
                                                String dob = (object.isNull("BIRTHDATE") || object.getString("BIRTHDATE").equals("")) ? "" : object.getString("BIRTHDATE");
                                                String city = (object.isNull("CITY") || object.getString("CITY").equals("")) ? "" : object.getString("CITY");
                                                String registerName = (object.isNull("REGISTERNAME") || object.getString("REGISTERNAME").equals("")) ? "" : object.getString("REGISTERNAME");
                                                String interestedIn = (object.isNull("INTRESTEDIN") || object.getString("INTRESTEDIN").equals("")) ? "" : object.getString("INTRESTEDIN");
                                                String nameOnPan = (object.isNull("Name_on_PAN") || object.getString("Name_on_PAN").equals("")) ? "" : object.getString("Name_on_PAN");
                                                String sipIntent = (object.isNull("SIPINTENT") || object.getString("SIPINTENT").equals("")) ? "" : object.getString("SIPINTENT");
                                                String eqSchemeId = (object.isNull("EQSCHEME_ID") || object.getString("EQSCHEME_ID").equals("")) ? "" : object.getString("EQSCHEME_ID");
                                                String userType = (object.isNull("USERTYPE") || object.getString("USERTYPE").equals("")) ? "" : object.getString("USERTYPE");
                                                String baCode = (object.isNull("BACODE") || object.getString("BACODE").equals("")) ? "" : object.getString("BACODE");
                                                String tokenKey = (object.isNull("TOKENKEY") || object.getString("TOKENKEY").equals("")) ? "" : object.getString("TOKENKEY");
                                                String downloadPdfUrl = (object.isNull("DOWNLOADPDFURL") || object.getString("DOWNLOADPDFURL").equals("")) ? "" : object.getString("DOWNLOADPDFURL");
                                                String esignService = (object.isNull("ESIGNSERVICE") || object.getString("ESIGNSERVICE").equals("")) ? "" : object.getString("ESIGNSERVICE");
                                                String otpVisible = (object.isNull("OTPVISIBLE") || object.getString("OTPVISIBLE").equals("")) ? "" : object.getString("OTPVISIBLE");
                                                String biometricVisible = (object.isNull("BIOMETRICVISIBLE") || object.getString("BIOMETRICVISIBLE").equals("")) ? "" : object.getString("BIOMETRICVISIBLE");
                                                String isNomineeMandatory = (object.isNull(NOMINEE_MANDATORY) || object.getString(NOMINEE_MANDATORY).equals("")) ? "" : object.getString(NOMINEE_MANDATORY);
                                                String onlineOffline =
                                                    (object.isNull("ONLINEOFFLINE")
                                                        || object.getString("ONLINEOFFLINE")
                                                        .equals("")) ? ""
                                                        : object.getString("ONLINEOFFLINE");
                                                String isAadhaarByPass =
                                                    (object.isNull("ISAADHAARBYPASS")
                                                        || object.getString("ISAADHAARBYPASS")
                                                        .equals("")) ? ""
                                                        : object.getString("ISAADHAARBYPASS")
                                                            .trim();
                                                if (isBlank(userId)) {
                                                    commonMsgDialog(BranchResumeApplicationActivity.this, DATA_IS_MISSING, false, "");
                                                    return;
                                                } else {
                                                    setUserID(userId);
                                                }
                                                setOnlineOfflineFlag(onlineOffline);
                                                setAadharNo(aadharNo);
                                                setPANno(panNo);
                                                setIsAadharVerified(isAddharVerified.equals("1"));
                                                setIsCVLVerified(iscvlVerified.equals("1"));
                                                setIsNomineeMandatory(
                                                    isNomineeMandatory != null && isNomineeMandatory
                                                        .equals("1"));
                                                setAadhaarByPass(isAadhaarByPass.equals("1"));
                                                setCity(city);
                                                setRegisteredName(registerName);
                                                setAddressMode(addressMode);
                                                setDob(dob);
                                                setPANName(nameOnPan);
                                                setSipIntent(sipIntent);
                                                setEqSchemeId(eqSchemeId);
                                                setUserType(userType);
                                                setBaFranchiseeCode(baCode);
                                                setTokenKey(tokenKey);
                                                setDownloadPdfUrl(downloadPdfUrl);
                                                setEsignService(esignService);
                                                setOtpVisibleForBranch(otpVisible);
                                                setBioMetricVisibleForBranch(biometricVisible);

                                                JSONArray jsonArraProspectDetails3 = response.getJSONArray("PROSPECTDETAILS3");
                                                if ((jsonArraProspectDetails3.length() == 0)) {
                                                    if (interestedIn.equalsIgnoreCase(EQUITY) || interestedIn.equalsIgnoreCase(PMS)) {
                                                        setIsEquitySelected(true);
                                                    }
                                                    if (interestedIn.equalsIgnoreCase(COMMODITY)) {
                                                        setIsCommoditySelected(true);
                                                    }
                                                    if (interestedIn.equalsIgnoreCase(MF)) {
                                                        if (!isMfSegmentExist()) {
                                                            setIsInterestedInMF(true);
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            if (prospectTable == false) {
                                                setGotoReviewEdit(false);
                                                setGotoFundTransfer(false);
                                            }
                                            commonMsgDialog(BranchResumeApplicationActivity.this, DATA_IS_MISSING, false, "");
                                            return;
                                        }
                                    }
                                    if (response.has("PROSPECTDETAILS")) {
                                        JSONArray jsonArraProspectDetails = response.getJSONArray("PROSPECTDETAILS");

                                        if (jsonArraProspectDetails.length() != 0) {
                                            String offlineIpvStatus = null;
                                            for (int i = 0; i < jsonArraProspectDetails.length(); i++) {
                                                prospectTable = true;

                                                JSONObject object = jsonArraProspectDetails.getJSONObject(i);
                                                String segment = (object.isNull(SEGMENT) || object.getString(SEGMENT).equals("")) ? "" : object.getString(SEGMENT);
                                                String registration = (object.isNull(REGISTRATIONPROSPECT) || object.getString(REGISTRATIONPROSPECT).equals("")) ? "" : object.getString(REGISTRATIONPROSPECT);
                                                String segmentSelected = (object.isNull(SEGMENTSELECTEDPROSPECT) || object.getString(SEGMENTSELECTEDPROSPECT).equals("")) ? "" : object.getString(SEGMENTSELECTEDPROSPECT);
                                                String personalDetail = (object.isNull(PERSONALDETAILPROSPECT) || object.getString(PERSONALDETAILPROSPECT).equals("")) ? "" : object.getString(PERSONALDETAILPROSPECT);
                                                String workDetail = (object.isNull(WORKDETAILPROSPECT) || object.getString(WORKDETAILPROSPECT).equals("")) ? "" : object.getString(WORKDETAILPROSPECT);
                                                String demateDetails = (object.isNull(DEMATEDETAILSPROSPECT) || object.getString(DEMATEDETAILSPROSPECT).equals("")) ? "" : object.getString(DEMATEDETAILSPROSPECT);
                                                String bankDetail = (object.isNull(BANKDETAILPROSPECT) || object.getString(BANKDETAILPROSPECT).equals("")) ? "" : object.getString(BANKDETAILPROSPECT);
                                                String schemeDetail = (object.isNull(SCHEMEDETAILPROSPECT) || object.getString(SCHEMEDETAILPROSPECT).equals("")) ? "" : object.getString(SCHEMEDETAILPROSPECT);
                                                String reviewEdit = (object.isNull(REVIEWEDITPROSPECT) || object.getString(REVIEWEDITPROSPECT).equals("")) ? "" : object.getString(REVIEWEDITPROSPECT);
                                                String fundTransfer = (object.isNull(FUNDTRANSFERPROSPECT) || object.getString(FUNDTRANSFERPROSPECT).equals("")) ? "" : object.getString(FUNDTRANSFERPROSPECT);
                                                String documentUploaded = (object.isNull(DOCUMENTUPLOADEDPROSPECT) || object.getString(DOCUMENTUPLOADEDPROSPECT).equals("")) ? "" : object.getString(DOCUMENTUPLOADEDPROSPECT);
                                                String esignCompleted = (object.isNull(ESIGNCOMPLETEDPROSPECT) || object.getString(ESIGNCOMPLETEDPROSPECT).equals("")) ? "" : object.getString(ESIGNCOMPLETEDPROSPECT);
                                                String ipvinitiated = (object.isNull(IPVINITIATEDPROSPECT) || object.getString(IPVINITIATEDPROSPECT).equals("")) ? "" : object.getString(IPVINITIATEDPROSPECT);
                                                String ipvDonebyVerifier = (object.isNull(IPVDONEBYVERIFIERPROSPECT) || object.getString(IPVDONEBYVERIFIERPROSPECT).equals("")) ? "" : object.getString(IPVDONEBYVERIFIERPROSPECT);
                                                String ipvCompleted = (object.isNull(IPVCOMPLETEDPROSPECT) || object.getString(IPVCOMPLETEDPROSPECT).equals("")) ? "" : object.getString(IPVCOMPLETEDPROSPECT);
                                                String client = (object.isNull(CLIENTCODEGENERATEDPROSPECT) || object.getString(CLIENTCODEGENERATEDPROSPECT).equals("")) ? "" : object.getString(CLIENTCODEGENERATEDPROSPECT);
                                                String accountActivated = (object.isNull(ACCOUNTACTIVATEDPROSPECT) || object.getString(ACCOUNTACTIVATEDPROSPECT).equals("")) ? "" : object.getString(ACCOUNTACTIVATEDPROSPECT);
                                                String offlineIpvInitiated = (object.isNull(OFFLINEIPVINITIATEDPROSPECT) || object.getString(OFFLINEIPVINITIATEDPROSPECT).equals("")) ? "" : object.getString(OFFLINEIPVINITIATEDPROSPECT);
                                                offlineIpvStatus = (object.isNull(OFFLINEIPVSTATUSPROSPECT) || object.getString(OFFLINEIPVSTATUSPROSPECT).equals("")) ? "" : object.getString(OFFLINEIPVSTATUSPROSPECT);
                                                String offlineDocumentUploaded = (object.isNull(OFFLINEDOCUMENTUPLOADEDPROSPECT) || object.getString(OFFLINEDOCUMENTUPLOADEDPROSPECT).equals("")) ? "" : object.getString(OFFLINEDOCUMENTUPLOADEDPROSPECT);
                                                String offlineEsignCompleted = (object.isNull(OFFLINEESIGNCOMPLETEDPROSPECT) || object.getString(OFFLINEESIGNCOMPLETEDPROSPECT).equals("")) ? "" : object.getString(OFFLINEESIGNCOMPLETEDPROSPECT);
                                                String offlineIpvCompleted = (object.isNull(OFFLINEIPVCOMPLETEDPROSPECT) || object.getString(OFFLINEIPVCOMPLETEDPROSPECT).equals("")) ? "" : object.getString(OFFLINEIPVCOMPLETEDPROSPECT);
                                                String offlineClientCodeGenerated = (object.isNull(OFFLINECLIENTCODEGENERATEDPROSPECT) || object.getString(OFFLINECLIENTCODEGENERATEDPROSPECT).equals("")) ? "" : object.getString(OFFLINECLIENTCODEGENERATEDPROSPECT);

                                                // Payment status
                                                if (segment.equalsIgnoreCase(EQUITY) || segment.equalsIgnoreCase(PMS)) {
                                                    setIsEquityOriginal(true);
                                                    setIsEquitySelected(true);

                                                    if (fundTransfer.equalsIgnoreCase("Y")) {
                                                        setIsEquityFundTransfered(true);
                                                    } else {
                                                        setIsEquityFundTransfered(false);
                                                    }

                                                } else if (segment.equalsIgnoreCase(COMMODITY)) {
                                                    setIsCommodityOriginal(true);
                                                    setIsCommoditySelected(true);

                                                    if (fundTransfer.equalsIgnoreCase("Y")) {
                                                        setIsCommodityFundTransfered(true);
                                                    } else {
                                                        setIsCommodityFundTransfered(false);
                                                    }
                                                } else if (segment.equalsIgnoreCase(MF)) {
                                                    setIsInterestedInMF(true);
                                                }

                                                if (!isBlank(client) && client.equalsIgnoreCase("Y")) {
                                                    setExistingSegment(segment);
                                                    continue; //segment completed
                                                } else {
                                                    setAddNewSegment(false);
                                                }

                                                Map stageMap = new HashMap();
                                                stageMap.put(REGISTRATION_STAGE, registration);
                                                stageMap.put(SEGMENTSELECTED_STAGE, segmentSelected);
                                                stageMap.put(PERSONALDETAIL_STAGE, personalDetail);
                                                stageMap.put(WORKDETAIL_STAGE, workDetail);
                                                stageMap.put(DEMATEDETAILS_STAGE, demateDetails);
                                                stageMap.put(BANKDETAIL_STAGE, bankDetail);
                                                stageMap.put(SCHEMEDETAIL_STAGE, schemeDetail);
                                                stageMap.put(REVIEWEDIT_STAGE, reviewEdit);
                                                stageMap.put(FUNDTRANSFER_STAGE, fundTransfer);
                                                stageMap.put(DOCUMENTUPLOADED_STAGE, documentUploaded);
                                                stageMap.put(ESIGNCOMPLETED_STAGE, esignCompleted);
                                                stageMap.put(IPVINITIATED_STAGE, ipvinitiated);
                                                stageMap.put(IPVDONEBYVERIFIER_STAGE, ipvDonebyVerifier);
                                                stageMap.put(IPVCOMPLETED_STAGE, ipvCompleted);
                                                stageMap.put(CLIENTCODEGENERATED_STAGE, client);
                                                stageMap.put(ACCOUNTACTIVATED_STAGE, accountActivated);
                                                stageMap.put(OFFLINEIPVINITIATED_STAGE, offlineIpvInitiated);
                                                stageMap.put(OFFLINEIPVSTATUS_STAGE, offlineIpvStatus);
                                                stageMap.put(OFFLINEDOCUMENTUPLOADED_STAGE, offlineDocumentUploaded);
                                                stageMap.put(OFFLINEESIGNCOMPLETED_STAGE, offlineEsignCompleted);
                                                stageMap.put(OFFLINEIPVCOMPLETED_STAGE, offlineIpvCompleted);
                                                stageMap.put(OFFLINECLIENTCODEGENERATED_STAGE, offlineClientCodeGenerated);

                                                if (segment.equalsIgnoreCase(EQUITY) && !isEquitySegmentExist()) {
                                                    allStageMap.put(segment, stageMap);
                                                }
                                                if (segment.equalsIgnoreCase(COMMODITY) && !isCommoditySegmentExist()) {
                                                    allStageMap.put(segment, stageMap);
                                                }
                                                if (segment.equalsIgnoreCase(MF) && !isMfSegmentExist()) {
                                                    allStageMap.put(segment, stageMap);
                                                }
                                            }
                                            setGotoReviewEdit(false);
                                            setGotoFundTransfer(false);
                                        } else {
                                            prospectTable = false;
                                        }
                                    }

                                    if (prospectTable == true) {
                                        setIsOffline(setOffLineStatus());
                                    } else {
                                        setIsOffline(false);
                                    }
                                    if (prospectTable == true && !isBlank(getAddressMode())) {
                                        goToNextPage();
                                    } else {
                                        if (getInterestedInMF()) {
                                            if (getAppType().equalsIgnoreCase(PP) || getAppType().equalsIgnoreCase(CRM)) {

                                                if (isBlank(getPANno())) {
                                                    setGotoReviewEdit(false);
                                                    setGotoFundTransfer(false);
                                                    Intent intent = new Intent(BranchResumeApplicationActivity.this, PANAdharRegistActivity.class);
                                                    intent.putExtra(SOURCE, PAN_AADHAR_PENDING);
                                                    startActivity(intent);
                                                    finish();
                                                } else if (!getIsAadharVerified() && !getIsCVLVerified()) {
                                                    setGotoReviewEdit(false);
                                                    setGotoFundTransfer(false);
                                                    Intent intent = new Intent(BranchResumeApplicationActivity.this, PANAdharRegistActivity.class);
                                                    intent.putExtra(SOURCE, PAN_AADHAR_PENDING);
                                                    startActivity(intent);
                                                    finish();
                                                } else if (isBlank(getAddressMode())) {
                                                    setGotoReviewEdit(false);
                                                    setGotoFundTransfer(false);
//                                                    Intent intent = new Intent(BranchResumeApplicationActivity.this, PANAdharRegistActivity.class);
                                                    Intent intent = new Intent(BranchResumeApplicationActivity.this, AddressDetailsActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    setGotoReviewEdit(false);
                                                    setGotoFundTransfer(false);
                                                    Intent intent = new Intent(BranchResumeApplicationActivity.this, EkycMainActivity.class);
                                                    intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        } else {
                                            //Broking
                                            if (getAppType().equalsIgnoreCase(PP) || getAppType().equalsIgnoreCase(CRM)) {
                                                if (isBlank(getPANno())) {
                                                    setGotoReviewEdit(false);
                                                    setGotoFundTransfer(false);
                                                    Intent intent = new Intent(BranchResumeApplicationActivity.this, PANAdharRegistActivity.class);
                                                    intent.putExtra(SOURCE, PAN_AADHAR_PENDING);
                                                    startActivity(intent);
                                                    finish();
                                                } else if (!getIsAadharVerified()) {
                                                    setGotoReviewEdit(false);
                                                    setGotoFundTransfer(false);
                                                    Intent intent = new Intent(BranchResumeApplicationActivity.this, PANAdharRegistActivity.class);
                                                    intent.putExtra(SOURCE, PAN_AADHAR_PENDING);
                                                    startActivity(intent);
                                                    finish();
                                                } else if (isBlank(getAddressMode())) {
                                                    setGotoReviewEdit(false);
                                                    setGotoFundTransfer(false);
//                                                    Intent intent = new Intent(BranchResumeApplicationActivity.this, PANAdharRegistActivity.class);
                                                    Intent intent = new Intent(BranchResumeApplicationActivity.this, AddressDetailsActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    setGotoReviewEdit(false);
                                                    setGotoFundTransfer(false);
                                                    Intent intent = new Intent(BranchResumeApplicationActivity.this, EkycMainActivity.class);
                                                    intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }

                                        }

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                customProgress.setVisibility(View.GONE);
                            } else {
                                MOSLCommon.showToastMsg(BranchResumeApplicationActivity.this, NO_RESPONSE);
                                customProgress.setVisibility(View.GONE);
                                finish();
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
                        public void onFailure(int statusCode, Header[] headers, String
                                responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            commonMsgDialog(BranchResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                            finish();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable
                                throwable, JSONObject errorResponse) {
                            Log.e("Failure", "no resp in Data");
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(BranchResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                            finish();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable
                                throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            commonMsgDialog(BranchResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                            finish();
                        }
                    }

            );
        } else {
            MOSLCommon.alertDInternet(BranchResumeApplicationActivity.this);
            customProgress.setVisibility(View.GONE);
            finish();
        }
    }

    private void setBooleanValueToFalse() {
        setGotoFundTransfer(false);
        setGotoReviewEdit(false);
        setIsSegmentSelected(false);
        setIsPersonalDetailDone(false);
        setIsWorkDetailDone(false);
        setIsBankDetailDone(false);
        setIsSchemeDetailDone(false);
        setIsReviewEditDone(false);
        setIsIPVDone(false);
        setIsOfflineDocumentUploadedDone(false);
        setIsEquityFundTransfered(false);
        setIsCommodityFundTransfered(false);
        setIsEquityIpvDone(false);
        setIsMfIpvDone(false);
    }

    /*
   * Set Offline flag form Prospect API
   * */
    private boolean setOffLineStatus() {
        Set<String> segmentSet = allStageMap.keySet();
        if (allStageMap.size() == 1) {
            String segment = segmentSet.iterator().next();
            Map<String, String> currentStageMap = (HashMap) allStageMap.get(segment);
            if (currentStageMap.get(OFFLINEIPVSTATUS_STAGE).equals("0")) {
                return true;
            } else {
                return false;
            }
        } else if (allStageMap.size() == 2) {
            Iterator iterator = segmentSet.iterator();
            String segment1 = iterator.next().toString();
            String segment2 = iterator.next().toString();
            Map<String, String> currentStageMap1 = (HashMap) allStageMap.get(segment1);
            Map<String, String> currentStageMap2 = (HashMap) allStageMap.get(segment2);
            if (currentStageMap1.get(OFFLINEIPVSTATUS_STAGE).equals("0") && currentStageMap2.get(OFFLINEIPVSTATUS_STAGE).equals("0")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * will check only in-process segments
     *
     * @param STAGE
     * @return
     */
    private boolean isStagePending(String STAGE) {
        Set<String> segmentSet = allStageMap.keySet();

        if (allStageMap.size() == 1) {

            String segment = segmentSet.iterator().next();
            Map<String, String> currentStageMap = (HashMap) allStageMap.get(segment);
            if (currentStageMap.get(STAGE).equals("Y")) {
                return false;
            } else {
                return true;
            }
        } else if (allStageMap.size() == 2) {
            Iterator iterator = segmentSet.iterator();
            String segment1 = iterator.next().toString();
            String segment2 = iterator.next().toString();


            Map<String, String> currentStageMap1 = (HashMap) allStageMap.get(segment1);
            Map<String, String> currentStageMap2 = (HashMap) allStageMap.get(segment2);

            if (currentStageMap1.get(STAGE).equals("Y") && currentStageMap2.get(STAGE).equals("Y")) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    private void goToNextPage() {
        if (isAddNewSegment()) {
            showSecondProductOption(getUserID());
            return;
        }

        Set<String> segmentSet = allStageMap.keySet();
        for (String segment : segmentSet) {
            if (segment.equalsIgnoreCase(EQUITY) && isEquitySegmentExist()) {
                if (allStageMap.size() == 1) {
                    if (getIsAadharVerified()) {
                        Intent intent = new Intent(BranchResumeApplicationActivity.this, EkycMainActivity.class);
                        intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
                        startActivity(intent);
                        finish();
                        break;
                    } else {
                        Intent intent = new Intent(BranchResumeApplicationActivity.this, PANAdharRegistActivity.class);
                        intent.putExtra(SOURCE, ADD_SEGMENT);
                        startActivity(intent);
                        finish();
                        break;
                    }
                } else {
                    continue;
                }
            } else if (segment.equalsIgnoreCase(COMMODITY) && isCommoditySegmentExist()) {
                if (allStageMap.size() == 1) {
                    if (getIsAadharVerified()) {
                        Intent intent = new Intent(BranchResumeApplicationActivity.this, EkycMainActivity.class);
                        intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
                        startActivity(intent);
                        finish();
                        break;
                    } else {
                        Intent intent = new Intent(BranchResumeApplicationActivity.this, PANAdharRegistActivity.class);
                        intent.putExtra(SOURCE, ADD_SEGMENT);
                        startActivity(intent);
                        finish();
                        break;
                    }
                } else {
                    continue;
                }
            } else if (segment.equalsIgnoreCase(MF) && isMfSegmentExist()) {
                continue;
            }
            Map<String, String> currentStageMap = (HashMap) allStageMap.get(segment);

            if (isStagePending(REGISTRATION_STAGE)) {
                //goto PAN-ADHAAR
                Intent intent = new Intent(BranchResumeApplicationActivity.this, PANAdharRegistActivity.class);
                intent.putExtra(SOURCE, PAN_AADHAR_PENDING);
//                RegisterModel.setUserID(userID);
                startActivity(intent);
                finish();
                return;
            } else if (isStagePending(SEGMENTSELECTED_STAGE)
                    || isStagePending(PERSONALDETAIL_STAGE)
                    || isStagePending(WORKDETAIL_STAGE)) {
                //goto personal detail
                Intent intent = new Intent(BranchResumeApplicationActivity.this, EkycMainActivity.class);
                intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
                setUserID(getUserID());
                startActivity(intent);
                finish();
                return;
            } else if (isStagePending(DEMATEDETAILS_STAGE)) {
                //goto demat detail
                setIsSegmentSelected(true);
                Intent intent = new Intent(BranchResumeApplicationActivity.this, EkycMainActivity.class);
                intent.putExtra(TAB_DETAIL, DEMAT_DETAIL);
                setUserID(getUserID());
                startActivity(intent);
                finish();
                return;
            } else if (isStagePending(BANKDETAIL_STAGE)
                    || (isStagePending(SCHEMEDETAIL_STAGE) && !getInterestedInMF())) {
                //goto Bank detail
                setGotoReviewEdit(false);
                setGotoFundTransfer(false);
                setIsSegmentSelected(true);
                setIsPersonalDetailDone(true);
                setIsWorkDetailDone(true);
                Intent intent = new Intent(BranchResumeApplicationActivity.this, EkycMainActivity.class);
                intent.putExtra(TAB_DETAIL, BANK_DETAIL);
                setUserID(getUserID());
                startActivity(intent);
                finish();
                return;
            } else if (isStagePending(REVIEWEDIT_STAGE) && !getInterestedInMF()) {
                //goto Review
                setGotoReviewEdit(true);
                setIsSegmentSelected(true);
                setIsPersonalDetailDone(true);
                setIsWorkDetailDone(true);
                setIsBankDetailDone(true);
                setIsSchemeDetailDone(true);
                Intent intent = new Intent(BranchResumeApplicationActivity.this, EkycMainActivity.class);
                intent.putExtra(TAB_DETAIL, BANK_DETAIL);
                setUserID(getUserID());
                startActivity(intent);
                finish();
                return;
            } else if (isStagePending(FUNDTRANSFER_STAGE) && !getInterestedInMF()) {
                //goto Fundtransfer
                setGotoFundTransfer(true);
                setIsReviewEditDone(true);
                setIsSegmentSelected(true);
                setIsPersonalDetailDone(true);
                setIsWorkDetailDone(true);
                setIsBankDetailDone(true);
                setIsSchemeDetailDone(true);
                Intent intent = new Intent(BranchResumeApplicationActivity.this, EkycMainActivity.class);
                intent.putExtra(TAB_DETAIL, BANK_DETAIL);
                setUserID(getUserID());
                startActivity(intent);
                finish();
                return;
            } else if (isOffline() ? (isStagePending(OFFLINEDOCUMENTUPLOADED_STAGE) || (!isStagePending(OFFLINEESIGNCOMPLETED_STAGE)
                    && !isStagePending(OFFLINECLIENTCODEGENERATED_STAGE)) && isStagePending(OFFLINEIPVCOMPLETED_STAGE))
                    : (isStagePending(DOCUMENTUPLOADED_STAGE)
                    || isStagePending(IPVCOMPLETED_STAGE))) {//offline condition added
                //goto Upload
                setGotoFundTransfer(true);
                setIsReviewEditDone(true);
                setIsSegmentSelected(true);
                setIsPersonalDetailDone(true);
                setIsWorkDetailDone(true);
                setIsBankDetailDone(true);
                setIsSchemeDetailDone(true);
//                setIsFundTransferDone(true);
                setIsIPVDone(false);
                if (isStagePending(OFFLINEDOCUMENTUPLOADED_STAGE)) {
                    setIsOfflineDocumentUploadedDone(false);
                } else {
                    setIsOfflineDocumentUploadedDone(true);
                }
                if (isStagePending(OFFLINECLIENTCODEGENERATED_STAGE)) {
                    setIsOfflineCliencodeGenrated(false);
                } else {
                    setIsOfflineCliencodeGenrated(true);
                }
                Intent intent = new Intent(BranchResumeApplicationActivity.this, EkycMainActivity.class);
                intent.putExtra(TAB_DETAIL, UPLOAD_DETAIL);
                setUserID(getUserID());
                startActivity(intent);
                finish();
                return;
            } else if (isOffline() ? (isStagePending(OFFLINEESIGNCOMPLETED_STAGE)
                    || isStagePending(OFFLINECLIENTCODEGENERATED_STAGE))
                    : (isStagePending(ESIGNCOMPLETED_STAGE)
                    || isStagePending(CLIENTCODEGENERATED_STAGE)
                    || isStagePending(ACCOUNTACTIVATED_STAGE))) {//offline condition added
                //goto Esign
                setIsReviewEditDone(true);
                setIsSegmentSelected(true);
                setIsPersonalDetailDone(true);
                setIsWorkDetailDone(true);
                setIsBankDetailDone(true);
                setIsSchemeDetailDone(true);
//                setIsFundTransferDone(true);
                setIsIPVDone(true);
                setIsOfflineDocumentUploadedDone(true);
                if (isStagePending(OFFLINECLIENTCODEGENERATED_STAGE)) {
                    setIsOfflineCliencodeGenrated(false);
                } else {
                    setIsOfflineCliencodeGenrated(true);
                }
                if (segment.equalsIgnoreCase(EQUITY)) {
                    setIsEquityIpvDone(true);
                }
                if (segment.equalsIgnoreCase(MF)) {
                    setIsMfIpvDone(true);
                }
                Intent intent = new Intent(BranchResumeApplicationActivity.this, EkycMainActivity.class);
                intent.putExtra(PDF_PATH, "");
                intent.putExtra(TAB_DETAIL, ESIGN_DETAIL);//todo pass from prospect-API
                //intent.putExtra(AADHAR, aadharNo);
                setUserID(getUserID());
                startActivity(intent);
                finish();
                return;
            } else if (!isStagePending(CLIENTCODEGENERATED_STAGE)) {
                setIsMfIpvDone(false);
            }
        }
    }

    private void showSecondProductOption(String userId) {
        if (MOSLCommon.isNetworkEnabled(BranchResumeApplicationActivity.this)) {
            customProgress.setVisibility(View.VISIBLE);

            Map inputMap = new HashMap<>();

            inputMap.put("_userid", userId);
            inputMap.put(APP_LOGIN_ID, getAppLoginID());
            inputMap.put(APPLICATION_TYPE, getAppType());
            String flag = GETAVAILABLEPRODUCT;
            String category = SIGNUP;
            WebServiceHandler.post(BranchResumeApplicationActivity.this, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (response.length() != 0) {
                            JSONArray dataArray = response.getJSONArray("AvailableProduct");
                            List<String> productList = new ArrayList();
                            if (dataArray != null && dataArray.length() != 0) {

                                for (int ctr = 0; ctr < dataArray.length(); ctr++) {
                                    JSONObject dataObject = (JSONObject) dataArray.get(ctr);
                                    String product = dataObject.getString("Product");
                                    productList.add(product);
                                }
                            }
                            secondProductPopup(productList);
                            customProgress.setVisibility(View.GONE);
                        } else {
                            commonMsgDialog(BranchResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                            finish();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        customProgress.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    try {

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(BranchResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(BranchResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(BranchResumeApplicationActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                    finish();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    try {

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    customProgress.setVisibility(View.GONE);
                }
            });
        } else {
            MOSLCommon.alertDInternet(BranchResumeApplicationActivity.this);
            finish();
        }

    }

    private void secondProductPopup(List<String> productList) {
        dialog = new Dialog(BranchResumeApplicationActivity.this, android.R.style.Theme_Light);
        dialog.setContentView(R.layout.second_product_popup);
        dialog.setCanceledOnTouchOutside(false);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(ADD_NEW_SEGMENT);
        ImageView ivEquity = (ImageView) dialog.findViewById(R.id.ivEquity);
        ImageView ivCommodity = (ImageView) dialog.findViewById(R.id.ivCommodity);
        ImageView ivAll = (ImageView) dialog.findViewById(R.id.ivAll);
        ImageView ivTrackApp = (ImageView) dialog.findViewById(R.id.ivTrackApp);
        ImageView btnClose = (ImageView) dialog.findViewById(R.id.btnClose);
        if (productList != null && productList.size() != 0) {
            if (productList.size() > 1) {
                if (!isEquityFundTransfered() && !isCommodityFundTransfered()) {
                    ivAll.setVisibility(View.VISIBLE);
                }
            } else {
                ivAll.setVisibility(View.GONE);
            }

            for (String product : productList) {
                if (product.equalsIgnoreCase(EQUITY) && !isEquityFundTransfered()) {
                    ivEquity.setVisibility(View.VISIBLE);
                } else if (product.equalsIgnoreCase(COMMODITY) && !isCommodityFundTransfered()) {
                    ivCommodity.setVisibility(View.VISIBLE);
                }
            }

        } else {
            tvMessage.setText(ALL_SEGMENT_COMPLETED);
        }
        ivEquity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog.dismiss();
                setIsEquitySelected(true);
                setIsEquityOriginal(true);

                continueWithSecondProduct();
            }
        });
        ivCommodity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.dismiss();
                setIsCommoditySelected(true);
                setIsCommodityOriginal(true);

                continueWithSecondProduct();
            }
        });
        ivAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.dismiss();
                setIsAllSelected(true);
                setIsAllOriginal(true);
                continueWithSecondProduct();
            }
        });
        ivTrackApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.dismiss();
                Intent intent = new Intent(BranchResumeApplicationActivity.this, TrackApplicationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllCommonModelFields();
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }

    private void continueWithSecondProduct() {
        if (getInterestedInMF()) {
            setIsInterestedInMF(false);
        }
        if (!getIsAadharVerified()) {
            Intent intent = new Intent(BranchResumeApplicationActivity.this, PANAdharRegistActivity.class);
            intent.putExtra(SOURCE, ADD_SEGMENT);
            startActivity(intent);
            finish();

        } else {
            Intent intent = new Intent(BranchResumeApplicationActivity.this, EkycMainActivity.class);
            intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
            startActivity(intent);
            finish();
        }
    }
}
