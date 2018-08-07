package com.mosl.tabapp.common;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ArrayAdapter;

import java.io.Serializable;

/**
 * Created by pramod on 14/06/17.
 */

public class FilterRowData implements Serializable,Parcelable{
    private String text; //to be displayed
    private String value; //to be sent to server
    private String keyValue; //this will be the key name to be sent to API
    private int position;
    public FilterRowData(int position,String text, String value,String keyValue) {
        this.position = position;
        this.text = text;
        this.value = value;
        this.keyValue = keyValue;
    }



    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    @Override
    public String toString() {
        return this.text;
    }


    ///Parcel***********************
    protected FilterRowData(Parcel in) {
        text = in.readString();
        value = in.readString();
        keyValue = in.readString();
        position = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(value);
        dest.writeString(keyValue);
        dest.writeInt(position);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FilterRowData> CREATOR = new Creator<FilterRowData>() {
        @Override
        public FilterRowData createFromParcel(Parcel in) {
            return new FilterRowData(in);
        }

        @Override
        public FilterRowData[] newArray(int size) {
            return new FilterRowData[size];
        }
    };

    public static FilterRowData getSpinnerFilterDataByValue(String value, ArrayAdapter<FilterRowData> arrayAdapter) {
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


}
