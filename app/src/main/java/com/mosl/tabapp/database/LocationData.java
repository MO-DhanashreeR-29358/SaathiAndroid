package com.mosl.tabapp.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class LocationData {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;
    private String latitude;
    private String longitude;
    private String deviceId;
    private String empCode;
    private String time;
    private String accuracy;
    private String geoArea;
    private String geoPincode;


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }


    public String getGeoArea() {
        return geoArea;
    }

    public void setGeoArea(String geoArea) {
        this.geoArea = geoArea;
    }

    public String getGeoPincode() {
        return geoPincode;
    }

    public void setGeoPincode(String geoPincode) {
        this.geoPincode = geoPincode;
    }

    @Override
    public String toString() {
        return "LocationData{" +
                "id=" + id +
                ", lat='" + latitude + '\'' +
                ", Long='" + longitude + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", empCode='" + empCode + '\'' +
                ", time='" + time + '\'' +
                ", accuracy='" + accuracy + '\'' +
                ", geoArea='" + geoArea + '\'' +
                ", geoPincode='" + geoPincode + '\'' +
                '}';
    }
}