package com.mosl.tabapp.model;

import java.io.Serializable;

/**
 * Created by Piyush on 09-03-2017.
 */
public class AddedSplSetAttendeeModel implements Serializable {
    private String attendeeName;

    public AddedSplSetAttendeeModel(String attendeeName) {
        this.attendeeName = attendeeName;
    }

    public String getAttendeeName() {
        return attendeeName;
    }

    public void setAttendeeName(String attendeeName) {
        this.attendeeName = attendeeName;
    }
}
