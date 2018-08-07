package com.mosl.tabapp.model;

import java.io.Serializable;

/**
 * Created by raju on 3/8/2017.
 */

public class AddedSplAttendeeModel implements Serializable {
    private String attendeeType; //R=Regular  & S=special
    private boolean attended = true; //fixed as true
    private String meetingAttendeeCode = "0";
    private String attendeeName;
    private String attendeeEmplyeeCode;
    private String attendeeProfileName;

    public String getAttendeeType() {
        return attendeeType;
    }

    public void setAttendeeType(String attendeeType) {
        this.attendeeType = attendeeType;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    public String getAttendeeProfileName() {
        return attendeeProfileName;
    }

    public void setAttendeeProfileName(String attendeeProfileName) {
        this.attendeeProfileName = attendeeProfileName;
    }

    public AddedSplAttendeeModel(String attendeeName, String attendeeEmplyeeCode, String attendeeProfileName) {

        this.attendeeName = attendeeName;
        this.attendeeEmplyeeCode = attendeeEmplyeeCode;
        this.attendeeProfileName = attendeeProfileName;
    }

    public AddedSplAttendeeModel(String attendeeName, String attendeeEmplyeeCode) {
        this.attendeeName = attendeeName;
        this.attendeeEmplyeeCode = attendeeEmplyeeCode;
    }

    public String getAttendeeEmplyeeCode() {

        return attendeeEmplyeeCode;
    }

    public String getMeetingAttendeeCode() {
        return meetingAttendeeCode;
    }

    public void setMeetingAttendeeCode(String meetingAttendeeCode) {
        this.meetingAttendeeCode = meetingAttendeeCode;
    }

    public AddedSplAttendeeModel(String attendeeType, boolean attended, String attendeeName, String attendeeEmplyeeCode) {
        this.attendeeType = attendeeType;
        this.attended = attended;
        this.attendeeName = attendeeName;
        this.attendeeEmplyeeCode = attendeeEmplyeeCode;

    }

    public AddedSplAttendeeModel(String attendeeType, boolean attended, String attendeeName, String attendeeEmplyeeCode, String meetingAttendeeCode) {
        this.attendeeType = attendeeType;
        this.attended = attended;
        this.attendeeName = attendeeName;
        this.attendeeEmplyeeCode = attendeeEmplyeeCode;
        this.meetingAttendeeCode = meetingAttendeeCode;
    }

    public void setAttendeeEmplyeeCode(String attendeeEmplyeeCode) {
        this.attendeeEmplyeeCode = attendeeEmplyeeCode;
    }


    public String getAttendeeName() {
        return attendeeName;
    }

    public void setAttendeeName(String attendeeName) {
        this.attendeeName = attendeeName;
    }

    public AddedSplAttendeeModel(String attendeeName) {

        this.attendeeName = attendeeName;
    }

    public String getJson() {
        String json = "{";
        json += "\"AttendeeEmpcode\": \"" + getAttendeeEmplyeeCode() + "\",";
        json += "\"AttendeeType\": \"" + getAttendeeType() + "\",";
        json += "\"MeetingAttendeeID\": \"" + getMeetingAttendeeCode() + "\",";
        json += "\"Attended\": " + isAttended();
        json += "}";

        return json;
    }
}
