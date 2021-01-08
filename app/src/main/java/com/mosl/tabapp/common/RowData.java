package com.mosl.tabapp.common;

import android.widget.ArrayAdapter;

import java.io.Serializable;

/**
 * Created by Vijay on 28/03/2017.
 */

public class RowData implements Serializable {
    private int position;
    private String key;
    private String value;
    private String showPotentialPopup, schename, schemeSelID, marDep;

    public RowData(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public RowData(int position, String value) {
        this.position = position;
        this.value = value;
    }

    public RowData(int position, String key, String value) {
        this.position = position;
        this.key = key;
        this.value = value;
    }

    public RowData(int position, String key, String value, String showPotentialPopup) {
        this.position = position;
        this.key = key;
        this.value = value;
        this.showPotentialPopup = showPotentialPopup;
    }

    public RowData(int position, String key, String value, String showPotentialPopup, String schename, String schemeSelID, String marDep) {
        this.position = position;
        this.key = key;
        this.value = value;
        this.showPotentialPopup = showPotentialPopup;
        this.schename = schename;
        this.schemeSelID = schemeSelID;
        this.marDep = marDep;
    }

    public String getShowPotentialPopup() {
        return showPotentialPopup;
    }

    public void setShowPotentialPopup(String showPotentialPopup) {
        this.showPotentialPopup = showPotentialPopup;
    }

    public String getSchename() {
        return schename;
    }

    public void setSchename(String schename) {
        this.schename = schename;
    }

    public String getSchemeSelID() {
        return schemeSelID;
    }

    public void setSchemeSelID(String schemeSelID) {
        this.schemeSelID = schemeSelID;
    }

    public String getMarDep() {
        return marDep;
    }

    public void setMarDep(String marDep) {
        this.marDep = marDep;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static RowData getSpinnerRowDataByValue(String value, ArrayAdapter<RowData> arrayAdapter) {
        if (CRMAppUtil.isBlank(value)) {
            return null;
        }
        if (arrayAdapter == null) {
            return null;
        }
        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            if (arrayAdapter.getItem(i).getValue().equalsIgnoreCase(value)) {
                return arrayAdapter.getItem(i);
            }
        }
        return null;
    }

    public static RowData getSpinnerRowDataById(String id, ArrayAdapter<RowData> arrayAdapter) {
        if (CRMAppUtil.isBlank(id)) {
            return null;
        }
        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            if (arrayAdapter.getItem(i).getKey().equalsIgnoreCase(id)) {
                return arrayAdapter.getItem(i);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
