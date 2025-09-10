package com.mosl.tabapp.saathiIndirect.LeadCreation.data;

import com.google.gson.annotations.SerializedName;
import com.mosl.tabapp.model.AddressDetailModel;

import java.util.ArrayList;
import java.util.List;

public class IndirectLeadCreationContact {
    @SerializedName("DOB") private String dob;
    @SerializedName("Name") private String name;
    @SerializedName("EmailId") private String eMail;
    @SerializedName("AlternateEmailId") private String alternateEmail;
    @SerializedName("AlternateMobileNo") private String alternateMobileNo;
    @SerializedName("Mobile") private String mobileNo;

    @SerializedName("MobileRi") private String mobileNoRI;
    @SerializedName("LandlineNo") private String landLineNo;

    public String getMobileNoRI() {
        return mobileNoRI;
    }

    public void setMobileNoRI(String mobileNoRI) {
        this.mobileNoRI = mobileNoRI;
    }

    @SerializedName("EKYCMobileNo") private String ekycMobileNo;
    @SerializedName("PanNumber") private String panNo;
    @SerializedName("AadharNumber") private String aadharNo;
    @SerializedName("IsSameAsAbove") private boolean sameAsAbove;
    @SerializedName("contactAddress") private List<AddressDetailModel> addresses;

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getAlternateEmail() {
        return alternateEmail;
    }

    public void setAlternateEmail(String alternateEmail) {
        this.alternateEmail = alternateEmail;
    }

    public String getAlternateMobileNo() {
        return alternateMobileNo;
    }

    public void setAlternateMobileNo(String alternateMobileNo) {
        this.alternateMobileNo = alternateMobileNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getLandLineNo() {
        return landLineNo;
    }

    public void setLandLineNo(String landLineNo) {
        this.landLineNo = landLineNo;
    }

    public String getEkycMobileNo() {
        return ekycMobileNo;
    }

    public void setEkycMobileNo(String ekycMobileNo) {
        this.ekycMobileNo = ekycMobileNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public boolean isSameAsAbove() {
        return sameAsAbove;
    }

    public void setSameAsAbove(boolean sameAsAbove) {
        this.sameAsAbove = sameAsAbove;
    }

    public AddressDetailModel getResidenceAddress() {
        return addresses.get(1);
    }

    public void setAddresses(AddressDetailModel officeAddress,
                             AddressDetailModel residenceAddress) {
        addresses = new ArrayList<>();
        addresses.add(officeAddress);
        addresses.add(residenceAddress);
    }

    public AddressDetailModel getOfficeAddress() {
        return addresses.get(0);
    }

    public List<AddressDetailModel> getAddress() {
        return addresses;
    }

    public String getContactJson() {
        String json = "{";
        json += "\"Salutation\":" + null;
        json += ",\"Name\":\"" + getName() + "\"";
        json += ",\"EmailId\":\"" + geteMail() + "\"";
        json += ",\"AlternateEmailId\":\"" + getAlternateEmail() + "\"";
        json += ",\"AlternateMobileNo\":\"" + getAlternateMobileNo() + "\"";
        json += ",\"Mobile\":\"" + getMobileNo() + "\"";
        json += ",\"LandlineNo\":\"" + getLandLineNo() + "\"";
        json += ",\"EKYCMobileNo\":\"" + getEkycMobileNo() + "\"";
        json += ",\"PanNumber\":\"" + getPanNo() + "\"";
        json += ",\"AadharNumber\":\"" + getAadharNo() + "\"";
        json += ",\"IsSameAsAbove\":\"" + isSameAsAbove() + "\"";
        json += ",\"contactAddress\":" + getAddressJson();
        json += "}";
        return json;
    }

    private String getAddressJson() {
        String json = "[";
        String meetingJson = getOfficeAddress().getJson();
        json += meetingJson + ",";

        meetingJson = getResidenceAddress().getJson();
        json += meetingJson + ",";
        json = json.substring(0, json.length() - 1); //remove lash comma
        json += "]";
        return json;
    }
}
