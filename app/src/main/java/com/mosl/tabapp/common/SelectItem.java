package com.mosl.tabapp.common;

/**
 * Created by Raju on 19-07-2018.
 */
public class SelectItem {
    private String code;
    private String value;
    private String type;

    public SelectItem(String code, String value, String type) {
        this.code = code;
        this.value = value;
        this.type = type;
    }

    public SelectItem(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

