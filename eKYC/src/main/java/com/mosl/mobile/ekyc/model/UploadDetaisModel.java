package com.mosl.mobile.ekyc.model;

import com.mosl.mobile.ekyc.common.MOSLCommon;

import java.io.Serializable;

import static com.mosl.mobile.ekyc.constants.MOSLConstants.DOCUMENT_VERIFIED_STATUS_APPROVED;

/**
 * Created by Vijay on 10/20/2016.
 */

public class UploadDetaisModel implements Serializable {
    boolean disableSection;

    int srno;
    int proofStatus; //verification status(1: verified, 2: rejected)

    /**
     * 0  Indicates error and need to try again
     * 1 indicates validation message: example document is already approved.
     * 2 indicates file is successfully uploaded.
     */
    int uploadStatus = -1; //upload status

    String key; //FileType
    String displayText, displayNote, Mandatory, UserID, fileUploadedPath, PDFFilePath, verifierRejectReason, eCopyMessage;
    boolean additionalFile, eCopy;
    boolean visible = true;

    //this will be set true when addn doc added during the verifier connected
    boolean newUploaded;

    public UploadDetaisModel(String key, String fileUploadedPath, int proofStatus, String verifierRejectReason, boolean eCopy, String eCopyMessage) {
        this.key = key;
        this.fileUploadedPath = fileUploadedPath;
        this.proofStatus = proofStatus;
        //this.uploadStatus = proofStatus;
        this.verifierRejectReason = verifierRejectReason;
        this.visible = true;
        this.eCopy = eCopy;
        this.eCopyMessage = eCopyMessage;
        if (this.key.startsWith("ADDITIONALPROOF")) {
            this.additionalFile = true;
            if (MOSLCommon.isBlank(fileUploadedPath)) {
                this.visible = false;
            }
        }
    }

    /**
     * @param srno
     * @param key
     * @param displayText: will be displayed on document bar - short name
     * @param mandatory
     * @param displayNote: will be displayed on bottom of bar  - full description
     */
    public UploadDetaisModel(int srno, String key, String displayText, String mandatory, String displayNote) {
        this.srno = srno;
        this.key = key;
        this.displayText = displayText;
        this.displayNote = displayNote == null ? "" : displayNote;
        this.Mandatory = mandatory;
        if (this.key.startsWith("ADDITIONALPROOF")) {
            additionalFile = true;
            if (isMandatory()) {
                visible = true;
            } else {
                visible = false;
            }
        }
    }

    public boolean isDisableSection() {
        if (proofStatus == DOCUMENT_VERIFIED_STATUS_APPROVED) {
            disableSection = true;
        }
        return disableSection;
    }

    public void setDisableSection(boolean disableSection) {
        this.disableSection = disableSection;
    }

    public int getSrno() {
        return srno;
    }

    public void setSrno(int srno) {
        this.srno = srno;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getMandatory() {
        return Mandatory;
    }

    public void setMandatory(String mandatory) {
        Mandatory = mandatory;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getFileUploadedPath() {
        return fileUploadedPath;
    }

    public void setFileUploadedPath(String fileUploadedPath) {
        this.fileUploadedPath = fileUploadedPath;
    }

    public String getPDFFilePath() {
        return PDFFilePath;
    }

    public void setPDFFilePath(String PDFFilePath) {
        this.PDFFilePath = PDFFilePath;
    }

    public int getProofStatus() {
        return proofStatus;
    }

    public void setProofStatus(int proofStatus) {
        this.proofStatus = proofStatus;
    }

    public String getVerifierRejectReason() {
        return verifierRejectReason;
    }

    public void setVerifierRejectReason(String verifierRejectReason) {
        this.verifierRejectReason = verifierRejectReason;
    }

    public boolean isMandatory() {
        return Mandatory.equals("1");
    }

    public int getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(int uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public boolean isAdditionalFile() {
        return additionalFile;
    }

    public void setAdditionalFile(boolean additionalFile) {
        this.additionalFile = additionalFile;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isNewUploaded() {
        return newUploaded;
    }

    public void setNewUploaded(boolean newUploaded) {
        this.newUploaded = newUploaded;
    }

    public String getDisplayNote() {
        return displayNote;
    }

    public void setDisplayNote(String displayNote) {
        this.displayNote = displayNote;
    }

    public boolean iseCopy() {
        return eCopy;
    }

    public void seteCopy(boolean eCopy) {
        this.eCopy = eCopy;
    }

    public String geteCopyMessage() {
        return eCopyMessage;
    }

    public void seteCopyMessage(String eCopyMessage) {
        this.eCopyMessage = eCopyMessage;
    }
}
