package com.mosl.tabapp.smt;

import android.webkit.JavascriptInterface;

public class SMTInterface {
    private final SMTCallBack callback;

    public SMTInterface(SMTCallBack callback) {
        this.callback = callback;
    }

    @JavascriptInterface public void openEKYC(String LeadId, String OAOUserId) {
        if (OAOUserId == null || OAOUserId.isEmpty()) return;
        callback.openEkyc(OAOUserId);
    }

}

