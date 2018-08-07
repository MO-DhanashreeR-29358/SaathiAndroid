package com.mosl.tabapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by raju on 7/11/2017.
 */

public class TypeActionableFilterModel implements Parcelable ,Serializable{

    private String type;

    @Override
    public String toString() {
        return  type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TypeActionableFilterModel() {

    }

    public TypeActionableFilterModel(String type) {
        this.type = type;

    }
    ///Parcelel

    protected TypeActionableFilterModel(Parcel in) {
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TypeActionableFilterModel> CREATOR = new Creator<TypeActionableFilterModel>() {
        @Override
        public TypeActionableFilterModel createFromParcel(Parcel in) {
            return new TypeActionableFilterModel(in);
        }

        @Override
        public TypeActionableFilterModel[] newArray(int size) {
            return new TypeActionableFilterModel[size];
        }
    };


}
