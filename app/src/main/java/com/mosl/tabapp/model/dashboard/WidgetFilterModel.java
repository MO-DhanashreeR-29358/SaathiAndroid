package com.mosl.tabapp.model.dashboard;

import android.os.Parcel;
import android.os.Parcelable;

import com.mosl.tabapp.common.FilterRowData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pramod on 14/06/17.
 */

public class WidgetFilterModel implements Serializable, Parcelable {
    private String filterSysName;
    private String keyValue;
    private List<FilterRowData> filterData; //"text": display   "value":send, default

    public WidgetFilterModel(String filterSysName, String keyValue, List<FilterRowData> filterData) {
        this.filterSysName = filterSysName;
        this.keyValue = keyValue;
        this.filterData = filterData;
    }



    public String getFilterSysName() {
        return filterSysName;
    }

    public void setFilterSysName(String filterSysName) {
        this.filterSysName = filterSysName;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public List<FilterRowData> getFilterData() {
        return filterData;
    }

    public void setFilterData(List<FilterRowData> filterData) {
        this.filterData = filterData;
    }
//Parcel


    protected WidgetFilterModel(Parcel in) {
        filterSysName = in.readString();
        keyValue = in.readString();
        filterData = in.createTypedArrayList(FilterRowData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filterSysName);
        dest.writeString(keyValue);
        dest.writeTypedList(filterData);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WidgetFilterModel> CREATOR = new Creator<WidgetFilterModel>() {
        @Override
        public WidgetFilterModel createFromParcel(Parcel in) {
            return new WidgetFilterModel(in);
        }

        @Override
        public WidgetFilterModel[] newArray(int size) {
            return new WidgetFilterModel[size];
        }
    };


}
