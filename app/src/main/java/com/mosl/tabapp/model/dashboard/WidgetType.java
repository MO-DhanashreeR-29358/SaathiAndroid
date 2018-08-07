package com.mosl.tabapp.model.dashboard;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by raju on 6/16/2017.
 */

public class WidgetType implements Serializable,Parcelable{

    private String widgetTypes="";
    private String title="";

    public WidgetType(String widgetTypes, String title) {
        this.widgetTypes = widgetTypes;
        this.title = title;
    }

    protected WidgetType(Parcel in) {
        widgetTypes = in.readString();
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(widgetTypes);
        dest.writeString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WidgetType> CREATOR = new Creator<WidgetType>() {
        @Override
        public WidgetType createFromParcel(Parcel in) {
            return new WidgetType(in);
        }

        @Override
        public WidgetType[] newArray(int size) {
            return new WidgetType[size];
        }
    };

    public String getWidgetTypes() {

        return widgetTypes;
    }

    public void setWidgetTypes(String widgetTypes) {
        this.widgetTypes = widgetTypes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
