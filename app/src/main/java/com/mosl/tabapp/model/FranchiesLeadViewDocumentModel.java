package com.mosl.tabapp.model;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Vijay on 24/03/2017.
 */

public class FranchiesLeadViewDocumentModel implements Serializable {

    Uri fileUploadedPath;

    public FranchiesLeadViewDocumentModel(Uri fileUploadedPath) {
        this.fileUploadedPath = fileUploadedPath;
    }

    public Uri getFileUploadedPath() {
        return fileUploadedPath;
    }

    public void setFileUploadedPath(Uri fileUploadedPath) {
        this.fileUploadedPath = fileUploadedPath;
    }
}
