package com.mosl.mobile.ekyc.model;

import android.widget.ArrayAdapter;
import com.mosl.mobile.ekyc.common.MOSLCommon;

/**
 * Created by Shefali on 2/4/2016.
 */
public class RowData {
    private int position;
    private String key;
    private String value;
    private String schmeID, schename, schemeSelID, marDep;
    private String adminCharges, brokingAmc;

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

    public RowData(int position, String key,  String value,String schmeID, String schename, String schemeSelID, String marDep) {
        this.position = position;
        this.key = key;
        this.value = value;
        this.schmeID = schmeID;
        this.schename = schename;
        this.schemeSelID = schemeSelID;
        this.marDep = marDep;
    }

    public RowData(int position, String key, String value,
        String adminCharges, String brokingAmc) {
        this.position = position;
        this.key = key;
        this.value = value;
        this.adminCharges = adminCharges;
        this.brokingAmc = brokingAmc;
    }

    public String getSchmeID() {
        return schmeID;
    }

    public void setSchmeID(String schmeID) {
        this.schmeID = schmeID;
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

    public String getAdminCharges() {
        return adminCharges;
    }

    public void setAdminCharges(String adminCharges) {
        this.adminCharges = adminCharges;
    }

    public String getBrokingAmc() {
        return brokingAmc;
    }

    public void setBrokingAmc(String brokingAmc) {
        this.brokingAmc = brokingAmc;
    }

    @Override
    public String toString() {
        return value;
    }

    public static RowData getSpinnerRowDataByValue(String state, ArrayAdapter<RowData> arrayAdapter) {
        if (MOSLCommon.isBlank(state)) {
            return null;
        }
        if (arrayAdapter == null) {
            return null;
        }
        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            if (arrayAdapter.getItem(i).getValue().equalsIgnoreCase(state)) {
                return arrayAdapter.getItem(i);
            }
        }
        return null;
    }

     public static RowData getSpinnerRowDataById(String state, ArrayAdapter<RowData> arrayAdapter) {
         if (MOSLCommon.isBlank(state)) {
             return null;
         }
        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            if (arrayAdapter.getItem(i).getKey().equalsIgnoreCase(state)) {
                return arrayAdapter.getItem(i);
            }
        }
        return null;
    }
}
