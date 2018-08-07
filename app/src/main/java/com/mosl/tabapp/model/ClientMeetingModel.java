package com.mosl.tabapp.model;

import java.io.Serializable;

/**
 * Created by raju on 3/8/2017.
 */

public class ClientMeetingModel implements Serializable {
    private String day;
    private String date;
    private String hour;
    private String clientCode;
    private String name;
    private String completeDate;
    private String time;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ClientMeetingModel(String day, String date, String hour, String clientCode, String name, String completeDate, String time) {

        this.day = day;
        this.date = date;
        this.hour = hour;
        this.clientCode = clientCode;
        this.name = name;
        this.completeDate = completeDate;
        this.time = time;
    }
}
