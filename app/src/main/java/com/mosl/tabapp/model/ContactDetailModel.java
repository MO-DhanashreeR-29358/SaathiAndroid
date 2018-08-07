package com.mosl.tabapp.model;

import java.io.Serializable;

/**
 * Created by Vijay on 30/03/2017.
 */

public class ContactDetailModel implements Serializable {
    private int contactTypeID;
    private String contactValue;
    private String contactTypeName;

    public ContactDetailModel(int contactTypeID, String contactValue, String contactTypeName) {
        this.contactTypeID = contactTypeID;
        this.contactValue = contactValue;
        this.contactTypeName = contactTypeName;
    }

    public int getContactTypeID() {
        return contactTypeID;
    }

    public void setContactTypeID(int contactTypeID) {
        this.contactTypeID = contactTypeID;
    }

    public String getContactValue() {
        return contactValue;
    }

    public void setContactValue(String contactValue) {
        this.contactValue = contactValue;
    }

    public String getContactTypeName() {
        return contactTypeName;
    }

    public void setContactTypeName(String contactTypeName) {
        this.contactTypeName = contactTypeName;
    }

    public String getJson() {
        String json = "{";
        json += "\"ContactTypeID\": \"" + getContactTypeID() + "\",";
        json += "\"ContactValue\": \"" + getContactValue() + "\",";
        json += "\"ContactTypeName\": \"" + getContactTypeName() + "\",";
        json += "\"contactSysName\": " + null;
        json += "}";

        return json;
    }
}
