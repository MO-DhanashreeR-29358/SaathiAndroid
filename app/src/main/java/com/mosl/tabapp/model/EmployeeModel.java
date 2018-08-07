package com.mosl.tabapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by raju on 3/7/2017.
 */

public class EmployeeModel implements Serializable, Parcelable {
    private boolean noData;
    private String attendeeName;
    private String attendeeCode;
    private String branchCode;
    private String meetingAttendeeCode = "0";

    public EmployeeModel(String attendeeName, String attendeeCode, String branchCode, boolean noData) {
        this.attendeeName = attendeeName;
        this.attendeeCode = attendeeCode;
        this.branchCode = branchCode;
        this.noData = noData;

    }


    public EmployeeModel(String attendeeName, boolean noData) {
        this.attendeeName = attendeeName;

        this.noData = noData;
    }


    public String getBranchcode() {
        return branchCode;
    }

    public void setBranchcode(String branchcode) {
        this.branchCode = branchcode;
    }


    public String getMeetingAttendeeCode() {
        return meetingAttendeeCode;
    }

    public void setMeetingAttendeeCode(String meetingAttendeeCode) {
        this.meetingAttendeeCode = meetingAttendeeCode;
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

    public EmployeeModel(String attendeeName) {

        this.attendeeName = attendeeName;
    }

    @Override
    public String toString() {
        return attendeeName;
    }


    public static EmployeeModel getNoData(String errorMsg) {
        return new EmployeeModel(errorMsg, true);
    }

    //region parceable
    protected EmployeeModel(Parcel in) {
        attendeeName = in.readString();
        attendeeCode = in.readString();
        branchCode = in.readString();
        meetingAttendeeCode = in.readString();
        this.noData = in.readByte() != 0;

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(attendeeName);
        dest.writeString(attendeeCode);
        dest.writeString(branchCode);
        dest.writeString(meetingAttendeeCode);
        dest.writeByte((byte) ( this.noData  ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EmployeeModel> CREATOR = new Creator<EmployeeModel>() {
        @Override
        public EmployeeModel createFromParcel(Parcel in) {
            return new EmployeeModel(in);
        }

        @Override
        public EmployeeModel[] newArray(int size) {
            return new EmployeeModel[size];
        }
    };
    //endregion
    public boolean isNoData() {
        return noData;
    }

    public void setNoData(boolean noData) {
        this.noData = noData;
    }
}
