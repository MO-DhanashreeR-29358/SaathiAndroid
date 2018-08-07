package com.mosl.mobile.ekyc.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.mosl.mobile.ekyc.constants.MOSLConstants.CRM;

/**
 * Created by Shefali on 24/10/16.
 */

public class CommonModel implements Serializable {
    private static String appLoginID;
    private static String userID;
    private static String PANno;
    private static String aadharNo;
    private static String dob;
    private static String status;
    private static String PANName;
    private static String PANStatus;
    private static String addressMode;
    private static boolean isAadharVerified;
    private static boolean isCVLVerified;
    private static String clientName;
    //for only crm
    private static String appType=CRM;
    private static boolean isEquitySelected = false;
    private static boolean isCommoditySelected = false;
    private static boolean isDerivativeSelected = false;
    private static boolean isCurrencySelected = false;
    private static boolean isEquityOriginal = false;
    private static boolean isCommodityOriginal = false;
    private static boolean isDerivativeOriginal = false;
    private static boolean isCurrencyOriginal = false;
    private static boolean isAllOriginal = false;
    private static boolean isAllSelected = false;
    private static boolean isPersonalDetailUpdated;
    private static boolean isEquityFundTransfered;
    private static boolean isCommodityFundTransfered;
    private static boolean isEquityIpvDone;
    private static boolean isMfIpvDone;
    private static boolean gotoReviewEdit;
    private static boolean gotoFundTransfer;
    private static boolean isSegmentSelected;
    private static boolean isPersonalDetailDone;
    private static boolean isWorkDetailDone;
    //    private static boolean isFinalSubmitDone;
    private static boolean isBankDetailDone;
    private static boolean isSchemeDetailDone;
    private static boolean isReviewEditDone;
    //    private static boolean isFundTransferDone;
    private static boolean isIPVDone;//Check IPV is done or not form GETPROSPECTDETAILS API
    private static boolean isVerificationDone;//Check IPV is done or not in running process
    private static boolean isOfflineDocumentUploadedDone;//Check OfflineDocumentUploaded is done or not form GETPROSPECTDETAILS API
    private static boolean isOfflineCliencodeGenrated;//Check OfflineClientcodeGenerated is done or not form GETPROSPECTDETAILS API And on Finalsubmit Success of Cliencode esign for offline
    private static String city;
    private static int aadharVerified;
    private static int cvlVerified;
    private static boolean equitySegmentExist;
    private static boolean commoditySegmentExist;
    private static boolean mfSegmentExist;
    private static boolean derivativeSegmentExist;
    private static boolean currencySegmentExist;

    private static Map<String, Boolean> completedProductMap = new HashMap<>();

    //Equity CRM/Direct
    private static String equMarginDeposit;
    private static String equSchemeName;
    private static String equSchSelID;
    private static String equSchID;
    //Commodity CRM/Direct
    private static String comMarginDeposit;
    private static String comSchemeName;
    private static String comSchSelID;
    private static String comSchID;
    //Equity Scheme BA
    private static String equTradeSchSelID;
    private static String equDelSchSelID;
    private static String equFutSchSelID;
    private static String equOptSchSelID;
    private static String equDPSchSelID;
    private static String equCurrFutSchSelID;
    private static String equCurrOptSchSelID;
    //Commodity Scheme BA
    private static String commFutSchSelID;
    private static String commOptSchSelID;
    private static String commDelSchSelID;
    private static String commFutNCXSchSelID;
    private static String commOptNCXSchSelID;
    private static String commDelNCXSchSelID;

    private static String registeredName;
    private static String registerEmail;

    private static boolean isInterestedInMF;
    private static boolean addNewSegment;

    //Constant for Tab
    private static int PersonalTab;
    private static int DematTab;
    private static int BankTab;
    private static int UploadTab;
    private static int EsignTab;

    private static String sipIntent;
    private static String eqSchemeId;
    private static String userType;
    private static String baFranchiseeCode;
    private static String otpVisible;
    private static String bioMetricVisible;
    private static String otpVisibleForBranch;
    private static String bioMetricVisibleForBranch;
    private static boolean isDirectProspect = false; //Used in PANAdharRegisterActivity and EsignFragment to hide Bio Option for direct prospect user in Direct App
    private static boolean isOffline;//Offline verification
    private static String tokenKey;
    private static String downloadPdfUrl;
    private static boolean aadhaarVerifiedByBio;//flag for Aadhaar verified by Bio finger or iris
    private static String esignService;
    private static boolean isNomineeMandatory = false;

    public static boolean isNomineeMandatory() {
        return isNomineeMandatory;
    }

    public static void setIsNomineeMandatory(boolean isNomineeMandatory) {
        CommonModel.isNomineeMandatory = isNomineeMandatory;
    }

    public static boolean getIsCurrencySelected() {
        return isCurrencySelected;
    }

    public static void setIsCurrencySelected(boolean isCurrencySelected) {
        CommonModel.isCurrencySelected = isCurrencySelected;
    }

    public static boolean isMfSegmentExist() {
        return mfSegmentExist;
    }

    public static void setMfSegmentExist(boolean mfSegmentExist) {
        CommonModel.mfSegmentExist = mfSegmentExist;
    }

    public static int getPersonalTab() {
        return PersonalTab;
    }

    public static void setPersonalTab(int personalTab) {
        PersonalTab = personalTab;
    }

    public static int getDematTab() {
        return DematTab;
    }

    public static void setDematTab(int dematTab) {
        DematTab = dematTab;
    }

    public static int getBankTab() {
        return BankTab;
    }

    public static void setBankTab(int bankTab) {
        BankTab = bankTab;
    }

    public static int getUploadTab() {
        return UploadTab;
    }

    public static void setUploadTab(int uploadTab) {
        UploadTab = uploadTab;
    }

    public static int getEsignTab() {
        return EsignTab;
    }

    public static void setEsignTab(int esignTab) {
        EsignTab = esignTab;
    }

    public static boolean getInterestedInMF() {
        return isInterestedInMF;
    }

    public static void setIsInterestedInMF(boolean isInterestedInMF) {
        CommonModel.isInterestedInMF = isInterestedInMF;
    }

    public static String getAddressMode() {
        return addressMode;
    }

    public static void setAddressMode(String addressMode) {
        CommonModel.addressMode = addressMode;
    }

    public static String getEquTradeSchSelID() {
        return equTradeSchSelID;
    }

    public static void setEquTradeSchSelID(String equTradeSchSelID) {
        CommonModel.equTradeSchSelID = equTradeSchSelID;
    }

    public static String getEquDelSchSelID() {
        return equDelSchSelID;
    }

    public static void setEquDelSchSelID(String equDelSchSelID) {
        CommonModel.equDelSchSelID = equDelSchSelID;
    }

    public static String getEquFutSchSelID() {
        return equFutSchSelID;
    }

    public static void setEquFutSchSelID(String equFutSchSelID) {
        CommonModel.equFutSchSelID = equFutSchSelID;
    }

    public static String getEquOptSchSelID() {
        return equOptSchSelID;
    }

    public static void setEquOptSchSelID(String equOptSchSelID) {
        CommonModel.equOptSchSelID = equOptSchSelID;
    }

    public static String getEquDPSchSelID() {
        return equDPSchSelID;
    }

    public static void setEquDPSchSelID(String equDPSchSelID) {
        CommonModel.equDPSchSelID = equDPSchSelID;
    }

    public static String getEquCurrFutSchSelID() {
        return equCurrFutSchSelID;
    }

    public static void setEquCurrFutSchSelID(String equCurrFutSchSelID) {
        CommonModel.equCurrFutSchSelID = equCurrFutSchSelID;
    }

    public static String getEquCurrOptSchSelID() {
        return equCurrOptSchSelID;
    }

    public static void setEquCurrOptSchSelID(String equCurrOptSchSelID) {
        CommonModel.equCurrOptSchSelID = equCurrOptSchSelID;
    }

    public static String getCommFutSchSelID() {
        return commFutSchSelID;
    }

    public static void setCommFutSchSelID(String commFutSchSelID) {
        CommonModel.commFutSchSelID = commFutSchSelID;
    }

    public static String getCommOptSchSelID() {
        return commOptSchSelID;
    }

    public static void setCommOptSchSelID(String commOptSchSelID) {
        CommonModel.commOptSchSelID = commOptSchSelID;
    }

    public static String getCommDelSchSelID() {
        return commDelSchSelID;
    }

    public static void setCommDelSchSelID(String commDelSchSelID) {
        CommonModel.commDelSchSelID = commDelSchSelID;
    }

    public static String getCommFutNCXSchSelID() {
        return commFutNCXSchSelID;
    }

    public static void setCommFutNCXSchSelID(String commFutNCXSchSelID) {
        CommonModel.commFutNCXSchSelID = commFutNCXSchSelID;
    }

    public static String getCommOptNCXSchSelID() {
        return commOptNCXSchSelID;
    }

    public static void setCommOptNCXSchSelID(String commOptNCXSchSelID) {
        CommonModel.commOptNCXSchSelID = commOptNCXSchSelID;
    }

    public static String getCommDelNCXSchSelID() {
        return commDelNCXSchSelID;
    }

    public static void setCommDelNCXSchSelID(String commDelNCXSchSelID) {
        CommonModel.commDelNCXSchSelID = commDelNCXSchSelID;
    }


    public static String getEquSchID() {
        return equSchID;
    }

    public static void setEquSchID(String equSchID) {
        CommonModel.equSchID = equSchID;
    }

    public static String getEquMarginDeposit() {
        return equMarginDeposit;
    }

    public static String getComSchID() {
        return comSchID;
    }

    public static void setComSchID(String comSchID) {
        CommonModel.comSchID = comSchID;
    }

    public static String getComMarginDeposit() {
        return comMarginDeposit;
    }

    public static void setComMarginDeposit(String comMarginDeposit) {
        CommonModel.comMarginDeposit = comMarginDeposit;
    }

    public static String getComSchemeName() {
        return comSchemeName;
    }

    public static void setComSchemeName(String comSchemeName) {
        CommonModel.comSchemeName = comSchemeName;
    }

    public static String getComSchSelID() {
        return comSchSelID;
    }

    public static void setComSchSelID(String comSchSelID) {
        CommonModel.comSchSelID = comSchSelID;
    }

    public static void setEquMarginDeposit(String equMarginDeposit) {
        CommonModel.equMarginDeposit = equMarginDeposit;
    }

    public static String getEquSchemeName() {
        return equSchemeName;
    }

    public static void setEquSchemeName(String equSchemeName) {
        CommonModel.equSchemeName = equSchemeName;
    }

    public static String getEquSchSelID() {
        return equSchSelID;
    }

    public static void setEquSchSelID(String equSchSelID) {
        CommonModel.equSchSelID = equSchSelID;
    }

    public static boolean isEquitySegmentExist() {
        return equitySegmentExist;
    }

    public static void setEquitySegmentExist(boolean equitySegmentExist) {
        CommonModel.equitySegmentExist = equitySegmentExist;
    }

    public static boolean isCommoditySegmentExist() {
        return commoditySegmentExist;
    }

    public static void setCommoditySegmentExist(boolean commoditySegmentExist) {
        CommonModel.commoditySegmentExist = commoditySegmentExist;
    }

    public static boolean isDerivativeSegmentExist() {
        return derivativeSegmentExist;
    }

    public static void setDerivativeSegmentExist(boolean derivativeSegmentExist) {
        CommonModel.derivativeSegmentExist = derivativeSegmentExist;
    }

    public static boolean isCurrencySegmentExist() {
        return currencySegmentExist;
    }

    public static void setCurrencySegmentExist(boolean currencySegmentExist) {
        CommonModel.currencySegmentExist = currencySegmentExist;
    }

    public static int getAadharVerified() {
        if (isAadharVerified) {
            aadharVerified = 1;
        } else {
            aadharVerified = 0;
        }
        return aadharVerified;
    }

    public static int getCvlVerified() {
        if (isCVLVerified) {
            cvlVerified = 1;
        } else {
            cvlVerified = 0;
        }
        return cvlVerified;
    }

    public static String getCity() {
        return city;
    }

    public static void setCity(String city) {
        CommonModel.city = city;
    }

    public static boolean isIPVDone() {
        return isIPVDone;
    }

    public static void setIsIPVDone(boolean isIPVDone) {
        CommonModel.isIPVDone = isIPVDone;
    }

    public static boolean isVerificationDone() {
        return isVerificationDone;
    }

    public static void setIsVerificationDone(boolean isVerificationDone) {
        CommonModel.isVerificationDone = isVerificationDone;
    }

    public static boolean isOfflineDocumentUploadedDone() {
        return isOfflineDocumentUploadedDone;
    }

    public static void setIsOfflineDocumentUploadedDone(boolean isOfflineDocumentUploadedDone) {
        CommonModel.isOfflineDocumentUploadedDone = isOfflineDocumentUploadedDone;
    }

    public static boolean isOfflineCliencodeGenrated() {
        return isOfflineCliencodeGenrated;
    }

    public static void setIsOfflineCliencodeGenrated(boolean isOfflineCliencodeGenrated) {
        CommonModel.isOfflineCliencodeGenrated = isOfflineCliencodeGenrated;
    }

    public static boolean isSegmentSelected() {
        return isSegmentSelected;
    }

    public static void setIsSegmentSelected(boolean isSegmentSelected) {
        CommonModel.isSegmentSelected = isSegmentSelected;
    }

    public static boolean isPersonalDetailDone() {
        return isPersonalDetailDone;
    }

    public static void setIsPersonalDetailDone(boolean isPersonalDetailDone) {
        CommonModel.isPersonalDetailDone = isPersonalDetailDone;
    }

    public static boolean isWorkDetailDone() {
        return isWorkDetailDone;
    }

    public static void setIsWorkDetailDone(boolean isWorkDetailDone) {
        CommonModel.isWorkDetailDone = isWorkDetailDone;
    }

    /*public static boolean isUploadFinalSubmit() {
        return isFinalSubmitDone;
    }

    public static void setIsUploadFinalSubmit(boolean isFinalSubmitDone) {
        CommonModel.isFinalSubmitDone = isFinalSubmitDone;
    }*/
    public static boolean isBankDetailDone() {
        return isBankDetailDone;
    }

    public static void setIsBankDetailDone(boolean isBankDetailDone) {
        CommonModel.isBankDetailDone = isBankDetailDone;
    }

    public static boolean isSchemeDetailDone() {
        return isSchemeDetailDone;
    }

    public static void setIsSchemeDetailDone(boolean isSchemeDetailDone) {
        CommonModel.isSchemeDetailDone = isSchemeDetailDone;
    }

    public static boolean isReviewEditDone() {
        return isReviewEditDone;
    }

    public static void setIsReviewEditDone(boolean isReviewEditDone) {
        CommonModel.isReviewEditDone = isReviewEditDone;
    }

    public static boolean isFundTransferDone() {
        return isEquityFundTransfered || isCommodityFundTransfered;
//        return isFundTransferDone;
    }

    /*public static void setIsFundTransferDone(boolean isFundTransferDone) {
        CommonModel.isFundTransferDone = isFundTransferDone;
    }*/

    public static boolean isGotoFundTransfer() {
        return gotoFundTransfer;
    }

    public static void setGotoFundTransfer(boolean gotoFundTransfer) {
        CommonModel.gotoFundTransfer = gotoFundTransfer;
    }

    public static boolean isGotoReviewEdit() {
        return gotoReviewEdit;
    }

    public static void setGotoReviewEdit(boolean gotoReviewEdit) {
        CommonModel.gotoReviewEdit = gotoReviewEdit;
    }

    public static String getAppType() {
        return appType;
    }

    public static void setAppType(String appType) {
        CommonModel.appType = appType;
    }


    public static String getAppLoginID() {
        return appLoginID;
    }

    public static void setAppLoginID(String appLoginID) {
        CommonModel.appLoginID = appLoginID;
    }

    public static boolean isEquityFundTransfered() {
        return isEquityFundTransfered;
    }

    public static void setIsEquityFundTransfered(boolean isEquityFundTransfered) {
        CommonModel.isEquityFundTransfered = isEquityFundTransfered;
    }

    public static boolean isCommodityFundTransfered() {
        return isCommodityFundTransfered;
    }

    public static void setIsCommodityFundTransfered(boolean isCommodityFundTransfered) {
        CommonModel.isCommodityFundTransfered = isCommodityFundTransfered;
    }

    public static boolean isEquityIpvDone() {
        return isEquityIpvDone;
    }

    public static void setIsEquityIpvDone(boolean isEquityIpvDone) {
        CommonModel.isEquityIpvDone = isEquityIpvDone;
    }

    public static boolean isMfIpvDone() {
        return isMfIpvDone;
    }

    public static void setIsMfIpvDone(boolean isMfIpvDone) {
        CommonModel.isMfIpvDone = isMfIpvDone;
    }

    public static boolean isPersonalDetailUpdated() {
        return isPersonalDetailUpdated;
    }

    public static void setIsPersonalDetailUpdated(boolean isPersonalDetailUpdated) {
        CommonModel.isPersonalDetailUpdated = isPersonalDetailUpdated;
    }

    public static boolean getIsEquitySelected() {
        return isEquitySelected;
    }

    public static void setIsEquitySelected(Boolean isEquitySelected) {
        CommonModel.isEquitySelected = isEquitySelected;
    }

    public static boolean getIsCommoditySelected() {
        return isCommoditySelected;
    }

    public static void setIsCommoditySelected(Boolean isCommoditySelected) {
        CommonModel.isCommoditySelected = isCommoditySelected;
    }

    public static boolean getIsDerivativeSelected() {
        return isDerivativeSelected;
    }

    public static void setIsDerivativeSelected(Boolean isDerivativeSelected) {
        CommonModel.isDerivativeSelected = isDerivativeSelected;
    }

    public static boolean getIsAllSelected() {
        return isAllSelected;
    }

    public static void setIsAllSelected(Boolean isAllSelected) {
        CommonModel.isAllSelected = isAllSelected;
    }

    public static boolean isEquityOriginal() {
        return isEquityOriginal;
    }

    public static void setIsEquityOriginal(boolean isEquityOriginal) {
        CommonModel.isEquityOriginal = isEquityOriginal;
    }

    public static boolean isCommodityOriginal() {
        return isCommodityOriginal;
    }

    public static void setIsCommodityOriginal(boolean isCommodityOriginal) {
        CommonModel.isCommodityOriginal = isCommodityOriginal;
    }

    public static boolean isDerivativeOriginal() {
        return isDerivativeOriginal;
    }

    public static void setIsDerivativeOriginal(boolean isDerivativeOriginal) {
        CommonModel.isDerivativeOriginal = isDerivativeOriginal;
    }

    public static boolean isCurrencyOriginal() {
        return isCurrencyOriginal;
    }

    public static void setIsCurrencyOriginal(boolean isCurrencyOriginal) {
        CommonModel.isCurrencyOriginal = isCurrencyOriginal;
    }

    public static boolean isAllOriginal() {
        return isAllOriginal;
    }

    public static void setIsAllOriginal(boolean isAllOriginal) {
        CommonModel.isAllOriginal = isAllOriginal;
    }

    public static String getClientName() {
        return clientName;
    }

    public static void setClientName(String clientName) {
        CommonModel.clientName = clientName;
    }

    public static boolean getIsAadharVerified() {
        return isAadharVerified;
    }

    public static void setIsAadharVerified(boolean isAadharVerified) {
        CommonModel.isAadharVerified = isAadharVerified;
    }

    public static boolean getIsCVLVerified() {
        return isCVLVerified;
    }

    public static void setIsCVLVerified(boolean isCVLVerified) {
        CommonModel.isCVLVerified = isCVLVerified;
    }

    public static String getPANStatus() {
        return PANStatus;
    }

    public static void setPANStatus(String PANStatus) {
        CommonModel.PANStatus = PANStatus;
    }

    public static String getRegisteredName() {
        return registeredName;
    }

    public static void setRegisteredName(String registeredName) {
        CommonModel.registeredName = registeredName;
    }

    public static String getRegisterEmail() {
        return registerEmail;
    }

    public static void setRegisterEmail(String registerEmail) {
        CommonModel.registerEmail = registerEmail;
    }

    public static String getPANName() {
        return PANName;
    }

    public static void setPANName(String PANName) {
        CommonModel.PANName = PANName;
    }


    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        CommonModel.status = status;
    }

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        CommonModel.userID = userID;
    }

    public static String getPANno() {
        return PANno;
    }

    public static void setPANno(String PANno) {
        CommonModel.PANno = PANno;
    }

    public static String getAadharNo() {
        return aadharNo.equalsIgnoreCase("0") ? "" : aadharNo;
    }

    public static void setAadharNo(String aadharNo) {
        CommonModel.aadharNo = aadharNo;
    }

    public static String getDob() {
        return dob;
    }

    public static void setDob(String dob) {
        CommonModel.dob = dob;
    }

    public static boolean isAddNewSegment() {
        return addNewSegment;
    }

    public static void setAddNewSegment(boolean addNewSegment) {
        CommonModel.addNewSegment = addNewSegment;
    }

    public static String getSipIntent() {
        return sipIntent;
    }

    public static void setSipIntent(String sipIntent) {
        CommonModel.sipIntent = sipIntent;
    }

    public static String getEqSchemeId() {
        return eqSchemeId;
    }

    public static void setEqSchemeId(String eqSchemeId) {
        CommonModel.eqSchemeId = eqSchemeId;
    }

    public static String getUserType() {
        return userType;
    }

    public static void setUserType(String userType) {
        CommonModel.userType = userType;
    }

    public static String getBaFranchiseeCode() {
        return baFranchiseeCode;
    }

    public static void setBaFranchiseeCode(String baFranchiseeCode) {
        CommonModel.baFranchiseeCode = baFranchiseeCode;
    }

    public static String getTokenKey() {
        return tokenKey;
    }

    public static void setTokenKey(String tokenKey) {
        CommonModel.tokenKey = tokenKey;
    }

    public static String getDownloadPdfUrl() {
        return downloadPdfUrl;
    }

    public static void setDownloadPdfUrl(String downloadPdfUrl) {
        CommonModel.downloadPdfUrl = downloadPdfUrl;
    }

    public static String getEsignService() {
        return esignService;
    }

    public static void setEsignService(String esignService) {
        CommonModel.esignService = esignService;
    }

    public static String getOtpVisible() {
        return otpVisible;
    }

    public static void setOtpVisible(String otpVisible) {
        CommonModel.otpVisible = otpVisible;
    }

    public static String getBioMetricVisible() {
        return bioMetricVisible;
    }

    public static void setBioMetricVisible(String bioMetricVisible) {
        CommonModel.bioMetricVisible = bioMetricVisible;
    }

    public static String getOtpVisibleForBranch() {
        return otpVisibleForBranch;
    }

    public static void setOtpVisibleForBranch(String otpVisibleForBranch) {
        CommonModel.otpVisibleForBranch = otpVisibleForBranch;
    }

    public static String getBioMetricVisibleForBranch() {
        return bioMetricVisibleForBranch;
    }

    public static void setBioMetricVisibleForBranch(String bioMetricVisibleForBranch) {
        CommonModel.bioMetricVisibleForBranch = bioMetricVisibleForBranch;
    }

    public static boolean isDirectProspect() {
        return isDirectProspect;
    }

    public static void setIsDirectProspect(boolean isDirectProspect) {
        CommonModel.isDirectProspect = isDirectProspect;
    }

    public static boolean isOffline() {
        return isOffline;
    }

    public static void setIsOffline(boolean isOffline) {
        CommonModel.isOffline = isOffline;
    }

    public static boolean isAadhaarVerifiedByBio() {
        return aadhaarVerifiedByBio;
    }

    public static void setAadhaarVerifiedByBio(boolean aadhaarVerifiedByBio) {
        CommonModel.aadhaarVerifiedByBio = aadhaarVerifiedByBio;
    }

    public static void clearAllCommonModelFields() {
        //setAppLoginID("");
        //setUserID("");
        setPANno("");
        setAadharNo("");
        setDob("");
        setStatus("");
        setPANName("");
        setPANStatus("");
        setAddressMode("");
        setIsAadharVerified(false);
        setIsCVLVerified(false);
        setClientName("");
//        setAppType("");
        setIsEquitySelected(false);
        setIsCommoditySelected(false);
        setIsDerivativeSelected(false);
        setIsEquityOriginal(false);
        setIsCommodityOriginal(false);
        setIsDerivativeOriginal(false);
        setIsCurrencyOriginal(false);
        setIsAllOriginal(false);
        setIsAllSelected(false);
        setIsCurrencySelected(false);
        setIsPersonalDetailUpdated(false);
        setIsEquityFundTransfered(false);
        setIsCommodityFundTransfered(false);
        setIsEquityIpvDone(false);
        setIsMfIpvDone(false);
        setGotoReviewEdit(false);
        setGotoFundTransfer(false);
        setIsSegmentSelected(false);
        setIsPersonalDetailDone(false);
        setIsWorkDetailDone(false);


//    public static boolean isFinalSubmitDone;
        setIsBankDetailDone(false);
        setIsSchemeDetailDone(false);
        setIsReviewEditDone(false);
//        setIsFundTransferDone(false);
        setIsIPVDone(false);
        setIsVerificationDone(false);
        setIsOfflineDocumentUploadedDone(false);
        setIsOfflineCliencodeGenrated(false);
        setCity("");
        setEquitySegmentExist(false);
        setCommoditySegmentExist(false);
        setDerivativeSegmentExist(false);
        setCurrencySegmentExist(false);

        //Equity CRM/Direct
        setEquMarginDeposit("");
        setEquSchemeName("");
        setEquSchSelID("");
        setEquSchID("");

        //Commodity CRM/Direct

        setComMarginDeposit("");
        setComSchemeName("");
        setComSchSelID("");
        setComSchID("");

        //Equity Scheme BA

        setEquTradeSchSelID("");
        setEquDelSchSelID("");
        setEquFutSchSelID("");
        setEquOptSchSelID("");
        setEquDPSchSelID("");
        setEquCurrFutSchSelID("");
        setEquCurrOptSchSelID("");

        //Commodity Scheme BA

        setCommFutSchSelID("");
        setCommOptSchSelID("");
        setCommDelSchSelID("");
        setCommFutNCXSchSelID("");
        setCommOptNCXSchSelID("");
        setCommDelNCXSchSelID("");

        setRegisteredName("");
        setRegisterEmail("");
        setIsInterestedInMF(false);
        setAddNewSegment(false);
        setMfSegmentExist(false);
        aadharVerified = 0;
        cvlVerified = 0;
        setSipIntent("");
        setEqSchemeId("");
        setBaFranchiseeCode("");
        setIsDirectProspect(false);
        setIsOffline(false);
        setTokenKey("");
        setDownloadPdfUrl("");
        setAadhaarVerifiedByBio(false);
        setOtpVisibleForBranch("");
        setBioMetricVisibleForBranch("");
        setEsignService("");
//        setUserType("");
//        setOtpVisible("");
//        setBioMetricVisible("");
    }
}
