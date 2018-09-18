package com.mosl.tabapp.model;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.Serializable;

public class LeadFieldValueModel implements Serializable {
    private String LeadField_Value = "";
    private static String leadid = "";
    private String LF_SysName = "";
    private String FieldName = "";
    private String ControlType = "";
    private boolean Mandatory = false;
    private String DropdownValueArr[];
    private TextView mValidationTextView;
    private Object ControlObject;
    private int leadFieldId;
    //Mandatory
    boolean isMandatory;

    public static String getLeadid() {
        return leadid;
    }


    public int getLeadFieldId() {
        return leadFieldId;
    }

    public void setLeadFieldId(int leadFieldId) {
        this.leadFieldId = leadFieldId;
    }


    public String getFieldName() {
        return FieldName;
    }

    public void setFieldName(String fieldName) {
        FieldName = fieldName;
    }

    public String getControlType() {
        return ControlType;
    }

    public void setControlType(String controlType) {
        ControlType = controlType;
    }

    public boolean isMandatory() {
        return Mandatory;
    }

    public void setMandatory(boolean mandatory) {
        Mandatory = mandatory;
    }

    public String[] getDropdownValueArr() {
        return DropdownValueArr;
    }

    /***
     * givesposition for select value
     * @return -1 if not found
     * */
    public int getDropdownValueArrPosition() {
        if (DropdownValueArr != null && LeadField_Value != null) {
          //  for (int i = 0; i <= DropdownValueArr.length; i++) {
            for (int i = 0; i < DropdownValueArr.length; i++) {
                if (DropdownValueArr[i].equalsIgnoreCase(LeadField_Value)) {
                    return i;
                } else {
                    continue;
                }
            }
            return -1;
        } else
            return -1;
    }

    public void setDropdownValueArr(String[] dropdownValueArr) {
        DropdownValueArr = dropdownValueArr;
    }

    public void setDropdownValueArr(String dropdownValueArr, String seperator) {
        DropdownValueArr = dropdownValueArr.split(seperator);
    }

//    public int getFieldSectionId() {
//        return FieldSectionId;
//    }
//
//    public void setFieldSectionId(int fieldSectionId) {
//        FieldSectionId = fieldSectionId;
//    }

    public String getLeadField_Value() {
        return LeadField_Value;
    }

    public void setLeadField_Value(String leadField_Value) {
        LeadField_Value = leadField_Value;
    }

    public CharSequence getJson() {
        StringBuilder sB = new StringBuilder(20);
        sB.append("{");
        //"LF_SysName": "F0000001",
        sB.append("\"LF_SysName\":\"").append(LF_SysName).append("\",");
        sB.append("\"LeadField_Value\":\"").append(getUiValue()).append("\",");
        sB.append("\"LeadFieldId\":").append(leadFieldId).append(",");
        //LeadID
        sB.append("\"LeadID\":\"").append(leadid).append("\"");
        sB.append("}");
        return sB;
    }

    public Object getControlObject() {
        return ControlObject;
    }

    public String getLF_SysName() {
        return LF_SysName;
    }

    public void setLF_SysName(String LF_SysName) {
        this.LF_SysName = LF_SysName;
    }

    public void setControlObject(Object controlObject) {
        ControlObject = controlObject;
    }

    private CharSequence getUiValue() {
        if (ControlObject == null) {
            return "";
        } else if (ControlObject instanceof EditText) {
            return ((EditText) ControlObject).getText();
        } else if (ControlObject instanceof Spinner) {
            return ((Spinner) ControlObject).getSelectedItem().toString();

        }
        return "";
    }

    public static void setLeadid(String leadid) {
        LeadFieldValueModel.leadid = leadid;
    }

    public void setmValidationTextView(TextView mValidationTextView) {
        this.mValidationTextView = mValidationTextView;
    }


}
