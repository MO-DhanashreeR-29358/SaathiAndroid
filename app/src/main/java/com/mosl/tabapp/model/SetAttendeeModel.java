package com.mosl.tabapp.model;

import java.io.Serializable;

/**
 * Created by Piyush on 08-03-2017.
 */
public class SetAttendeeModel implements Serializable {
    private String attendeeName;

    public String getAttendeeName() {
        return attendeeName;
    }

    public void setAttendeeName(String attendeeName) {
        this.attendeeName = attendeeName;
    }

    public SetAttendeeModel(String attendeeName) {

        this.attendeeName = attendeeName;
    }
}
