package com.mosl.tabapp.model;

import java.io.Serializable;

/**
 * Created by raju on 3/8/2017.
 */

public class AddedAttendeeModel implements Serializable {
    private String attendeeType; //R=Regular  & S=special
    private static String attendeeUserType = "R"; //R=Regular  & S=special
    private boolean attended = true; //fixed as true
    private static boolean attendedUser = true; //fixed as true
    private String attendeeName;
    private String attendeeEmplyeeCode;
    private static String attendeeUserEmplyeeCode;
    private String meetingAttendeeCode="0";

    public static String getAttendeeUserEmplyeeCode() {
        return attendeeUserEmplyeeCode;
    }

    public static void setAttendeeUserEmplyeeCode(String attendeeUserEmplyeeCode) {
        AddedAttendeeModel.attendeeUserEmplyeeCode = attendeeUserEmplyeeCode;
    }

    private String attendeeProfileName;

    public AddedAttendeeModel(String attendeeType, boolean attended, String attendeeName, String attendeeEmplyeeCode) {
        this.attendeeType = attendeeType;
        this.attended = attended;
        this.attendeeName = attendeeName;
        this.attendeeEmplyeeCode = attendeeEmplyeeCode;
    }

    public AddedAttendeeModel(String attendeeType, boolean attended, String attendeeName, String attendeeEmplyeeCode, String meetingAttendeeCode) {
        this.attendeeType = attendeeType;
        this.attended = attended;
        this.attendeeName = attendeeName;
        this.meetingAttendeeCode = meetingAttendeeCode;
        this.attendeeEmplyeeCode = attendeeEmplyeeCode;
    }

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

    public String getAttendeeName() {
        return attendeeName;
    }

    public void setAttendeeName(String attendeeName) {
        this.attendeeName = attendeeName;
    }

    public String getAttendeeEmplyeeCode() {
        return attendeeEmplyeeCode;
    }

    public void setAttendeeEmplyeeCode(String attendeeEmplyeeCode) {
        this.attendeeEmplyeeCode = attendeeEmplyeeCode;
    }

    public String getAttendeeProfileName() {
        return attendeeProfileName;
    }

    public void setAttendeeProfileName(String attendeeProfileName) {
        this.attendeeProfileName = attendeeProfileName;
    }

    public String getMeetingAttendeeCode() {
        return meetingAttendeeCode;
    }

    public void setMeetingAttendeeCode(String meetingAttendeeCode) {
        this.meetingAttendeeCode = meetingAttendeeCode;
    }

    public String getJson() {
        String json = "{";
        json += "\"AttendeeEmpcode\": \"" + getAttendeeEmplyeeCode() + "\",";
        json += "\"AttendeeType\": \"" + getAttendeeType() + "\",";
        json += "\"MeetingAttendeeID\": \"" + getMeetingAttendeeCode() + "\",";
        json += "\"DefaultAttendee\": \"" + false + "\",";
        json += "\"Attended\": " + isAttended();
        json += "}";

        return json;
    }

    public static String getUserJson() {
        String json = "{";
        json += "\"AttendeeEmpcode\": \"" + getAttendeeUserEmplyeeCode() + "\",";
        json += "\"MeetingAttendeeID\": \"" + "0" + "\",";
        json += "\"AttendeeType\": \"" + attendeeUserType + "\",";
        json += "\"DefaultAttendee\": \"" + true + "\",";
        json += "\"Attended\": " + attendedUser;
        json += "}";
        return json;
    }

}
