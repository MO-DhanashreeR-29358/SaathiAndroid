package com.mosl.tabapp.model;

import java.io.Serializable;

import static com.mosl.tabapp.common.CRMAppUtil.isBlank;

/**
 * Created by Vijay on 30/03/2017.
 */

public class AddressDetailModel implements Serializable {
    private int addressTypeID;
    private String address1;
    private String address2;
    private String address3;
    private String stateID;
    private String cityID;
    private String countryID;
    private String pincode;
    private boolean isPrimary;
    private String businessUnitID;

    public AddressDetailModel(int addressTypeID, String address1, String address2, String address3,String countryID, String stateID, String cityID, String pincode, boolean isPrimary, String businessUnitID) {
        this.addressTypeID = addressTypeID;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.stateID = stateID;
        this.cityID = cityID;
        this.countryID = countryID;
        this.pincode = pincode;
        this.isPrimary = isPrimary;
        this.businessUnitID = businessUnitID;
    }

    public int getAddressTypeID() {
        return addressTypeID;
    }

    public void setAddressTypeID(int addressTypeID) {
        this.addressTypeID = addressTypeID;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getStateID() {
        return stateID;
    }

    public void setStateID(String stateID) {
        this.stateID = stateID;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public String getBusinessUnitID() {
        return businessUnitID;
    }

    public void setBusinessUnitID(String businessUnitID) {
        this.businessUnitID = businessUnitID;
    }

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    public String getJson() {
        String json = "{";
        json += "\"AddressTypeID\": \"" + getAddressTypeID() + "\",";
        json += "\"Address1\": \"" + getAddress1() + "\",";
        json += "\"Address2\": \"" + getAddress2() + "\",";
        if (!isBlank(getAddress3())) {
            json += "\"Address3\": \"" + getAddress3() + "\",";
        }
        json += "\"StateID\": \"" + getStateID() + "\",";
        json += "\"CountryID\": \"" + getCountryID() + "\",";
        json += "\"CityID\": \"" + getCityID() + "\",";
        json += "\"Pincode\": \"" + getPincode() + "\",";
        json += "\"isPrimary\": \"" + isPrimary() + "\",";
        json += "\"BusinessUnitID\": \"" + getBusinessUnitID() + "\"";
        json += "}";
        return json;
    }
}
