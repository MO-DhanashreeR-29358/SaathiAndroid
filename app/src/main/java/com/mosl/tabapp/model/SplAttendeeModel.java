package com.mosl.tabapp.model;

import java.io.Serializable;

/**
 * Created by raju on 3/8/2017.
 */

public class SplAttendeeModel implements Serializable {
    public static final String NODatra = "*";

    private String attendeeName;
    private String attendeeCode;
    private String branchCode;

    public SplAttendeeModel(String attendeeName, String attendeeCode, String branchCode) {
        this.attendeeName = attendeeName;
        this.attendeeCode = attendeeCode;
        this.branchCode = branchCode;
    }


    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getAttendeeCode() {

        return attendeeCode;
    }

    public void setAttendeeCode(String attendeeCode) {
        this.attendeeCode = attendeeCode;
    }

    public String getAttendeeName() {
        return attendeeName;
    }

    public void setAttendeeName(String attendeeName) {
        this.attendeeName = attendeeName;
    }

    public SplAttendeeModel(String attendeeName) {

        this.attendeeName = attendeeName;
    }

    @Override
    public String toString() {
        return (attendeeCode.equalsIgnoreCase(NODatra)) ? "" : attendeeName;
    }

    public boolean isNoData() {
        return attendeeCode.equalsIgnoreCase(NODatra);
    }

    public static SplAttendeeModel getNoData(String errorMsg) {
        return new SplAttendeeModel(errorMsg, NODatra, "");
    }
}
