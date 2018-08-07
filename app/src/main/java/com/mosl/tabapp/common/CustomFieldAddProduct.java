package com.mosl.tabapp.common;

/**
 * Created by Vijay on 05/04/2017.
 */

public class CustomFieldAddProduct {

    private String category;
    private String value;

    public CustomFieldAddProduct(String category, String value) {
        this.category = category;
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
