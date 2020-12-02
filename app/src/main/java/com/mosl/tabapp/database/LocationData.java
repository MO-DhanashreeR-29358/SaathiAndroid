package com.mosl.tabapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class LocationData {

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
    private String version;

    public LocationData(String latitude, String longitude, String deviceId,
                        String empCode, String time, String accuracy, String geoArea, String geoPincode, String version) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.deviceId = deviceId;
        this.empCode = empCode;
        this.time = time;
        this.accuracy = accuracy;
        this.geoArea = geoArea;
        this.geoPincode = geoPincode;
        this.version = version;
    }

    public String getVersion() {
        return version;
    }


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

    public void setVersion(String version) {
        this.version = version;
    }

    @NotNull
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
                ", version='" + version + '\'' +
                ", geoPincode='" + geoPincode + '\'' +
                '}';
    }
}