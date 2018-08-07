package com.mosl.tabapp.model;


import java.io.Serializable;

public class NotificationModel implements Serializable {
    public NotificationModel(String message, String date, String time, String month, String additionalData) {
        this.message = message;
        this.date = date;
        this.time = time;
        this.month = month;
        this.additionalData = additionalData;
    }

    String message;
    String date;
    String time;
    String month;
    //AdditionalData
    String additionalData;

    public String getAdditionalData() {
        return additionalData;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
