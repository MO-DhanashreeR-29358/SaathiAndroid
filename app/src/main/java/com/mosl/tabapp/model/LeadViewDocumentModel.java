package com.mosl.tabapp.model;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Vijay on 22/03/2017.
 */

public class LeadViewDocumentModel implements Serializable {

    Uri fileUploadedPath;

    public LeadViewDocumentModel(Uri fileUploadedPath) {
        this.fileUploadedPath = fileUploadedPath;
    }

    public Uri getFileUploadedPath() {
        return fileUploadedPath;
    }

    public void setFileUploadedPath(Uri fileUploadedPath) {
        this.fileUploadedPath = fileUploadedPath;
    }
}
