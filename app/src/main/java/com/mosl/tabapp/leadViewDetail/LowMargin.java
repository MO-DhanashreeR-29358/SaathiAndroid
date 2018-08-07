package com.mosl.tabapp.leadViewDetail;


import android.widget.EditText;

/**
 * Created by Bhavesh on 12-01-2018.
 */

class LowMargin {

    private EditText editText;
    private boolean isMandatory;
    private boolean isDefaultProduct;
    private String keyName;
    private String viewName;

    public String getKeyName() {
        return keyName;
    }

    public LowMargin setKeyName(String keyName) {
        this.keyName = keyName;
        return this;
    }

    public LowMargin(String viewName, EditText editText, String key, boolean isMandatory, boolean isDefaultProduct) {
        this.viewName = viewName;
        this.editText = editText;
        this.keyName = key;
        this.isMandatory = isMandatory;
        this.isDefaultProduct = isDefaultProduct;

    }
    public LowMargin(String viewName, String key, boolean isMandatory, boolean isDefaultProduct) {
        this.viewName = viewName;
        this.keyName = key;
        this.isMandatory = isMandatory;
        this.isDefaultProduct = isDefaultProduct;
    }



    public EditText getEditText() {
        return editText;
    }

    public LowMargin setEditText(EditText editText) {
        this.editText = editText;
        return this;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public LowMargin setMandatory(boolean mandatory) {
        isMandatory = mandatory;
        return this;
    }


    public boolean isDefaultProduct() {
        return isDefaultProduct;
    }

    public LowMargin setDefaultProduct(boolean defaultProduct) {
        isDefaultProduct = defaultProduct;
        return this;
    }

    public LowMargin setViewName(String viewName) {
        this.viewName = viewName;
        return this;
    }

    public String getViewName() {
        return viewName;
    }
}
