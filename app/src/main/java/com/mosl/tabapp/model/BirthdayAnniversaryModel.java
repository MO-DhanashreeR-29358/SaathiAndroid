package com.mosl.tabapp.model;

import java.io.Serializable;

/**
 * Created by Vijay on 10/03/2017.
 */

public class BirthdayAnniversaryModel implements Serializable {

    private String clientName;
    private String clientCode;
    private String ucid;
    /*private String date;
    private String month;*/
    private String birthday;
    private String email;
    private String contactNo;
    private String totalYears;
    private String title;
    private String eventType;
    private boolean giftFlag;

    public BirthdayAnniversaryModel(String clientName, String clientCode, String ucid, String birthday, String email, String contactNo, String totalYears, String title, boolean giftFlag, String eventType) {
        this.clientName = clientName;
        this.clientCode = clientCode;
        this.ucid = ucid;
//        this.date = date;
//        this.month = month;
        this.birthday = birthday;
        this.email = email;
        this.contactNo = contactNo;
        this.totalYears = totalYears;
        this.title = title;
        this.giftFlag = giftFlag;
        this.eventType = eventType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getTotalYears() {
        return totalYears;
    }

    public void setTotalYears(String totalYears) {
        this.totalYears = totalYears;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isGiftFlag() {
        return giftFlag;
    }

    public void setGiftFlag(boolean giftFlag) {
        this.giftFlag = giftFlag;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
