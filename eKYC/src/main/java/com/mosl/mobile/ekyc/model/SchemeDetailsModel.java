package com.mosl.mobile.ekyc.model;

import java.io.Serializable;

/**
 * Created by Featsystem on 05-10-2017.
 */

public class SchemeDetailsModel implements Serializable {
    private String brok_VAL;
    private String brok_TEXT;
    private String curr_TYPE;

    public String getBrok_VAL() {
        return brok_VAL;
    }

    public void setBrok_VAL(String brok_VAL) {
        this.brok_VAL = brok_VAL;
    }

    public String getBrok_TEXT() {
        return brok_TEXT;
    }

    public void setBrok_TEXT(String brok_TEXT) {
        this.brok_TEXT = brok_TEXT;
    }

    public String getCurr_TYPE() {
        return curr_TYPE;
    }

    public void setCurr_TYPE(String curr_TYPE) {
        this.curr_TYPE = curr_TYPE;
    }

    public String getCurrFutureSelectedVal() {
        return currFutureSelectedVal;
    }

    public void setCurrFutureSelectedVal(String currFutureSelectedVal) {
        this.currFutureSelectedVal = currFutureSelectedVal;
    }

    public String getCurrFutureShow() {
        return currFutureShow;
    }

    public void setCurrFutureShow(String currFutureShow) {
        this.currFutureShow = currFutureShow;
    }

    public String getCurrOptionSelectedVal() {
        return currOptionSelectedVal;
    }

    public void setCurrOptionSelectedVal(String currOptionSelectedVal) {
        this.currOptionSelectedVal = currOptionSelectedVal;
    }

    public String getCurrOptionShow() {
        return currOptionShow;
    }

    public void setCurrOptionShow(String currOptionShow) {
        this.currOptionShow = currOptionShow;
    }

    private String currFutureSelectedVal;
    private String currFutureShow;
    private String currOptionSelectedVal;
    private String currOptionShow;

}
