package com.mosl.mobile.ekyc.common;

import android.widget.TextView;

/**
 * Created by raju on 12/10/2016.
 */
public class CustomFieldEquity {

    private TextView view;

    public CustomFieldEquity(String label) {
        this.label = label;
    }

    private String label;
    private String profileName;
    private String profileScheme;  //this will be used as key to send data to server in json
    private String category;  //this will be used as key to send data to server in json
    private String header;
    private String value;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileScheme() {
        return profileScheme;
    }

    public void setProfileScheme(String profileScheme) {
        this.profileScheme = profileScheme;
    }

    public CustomFieldEquity(String category, String profileName, String profileScheme) {
        this.category = category;
        this.profileName = profileName;
        this.profileScheme = profileScheme;
    }
    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }


    public TextView getView() {
        return view;
    }

    public void setView(TextView view) {
        this.view = view;
    }



    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


}
