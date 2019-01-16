package com.mosl.tabapp.smt;

import android.webkit.JavascriptInterface;

public class SMTInterface {
    private final SMTCallBack callback;

    public SMTInterface(SMTCallBack callback) {
        this.callback = callback;
    }

    @JavascriptInterface public void openEKYC() {

        callback.openEkyc();
    }

}

