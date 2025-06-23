package com.mosl.tabapp.model;

import com.mosl.tabapp.leadViewDetail.CreateMeetingProductModel;
import com.mosl.tabapp.leadViewDetail.CreateMeetingTravellingModel;
import java.io.Serializable;
import java.util.List;

/**
 * Created by pramod on 29/03/17.
 */

public class MeetingApptModel implements Serializable {
    private final String MEETING_TYPE_APPT = "A";
    private final String MEETING_TYPE_MEETING = "M";
    private final String DISPLAY_TYPE_MEETING = "U";

    public MeetingApptModel() {
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }


    //Meeting fields
    private int meetingReasonID;
    private int meetingDurationID;
    //MeetingObjectiveID
    private String meetingObjectiveID = "0";
    private String displayType;
    List<AddedAttendeeModel> meetingAttendee;
    List<AddedSplAttendeeModel> splMeetingAttendee;


    //Appointment fields
    private String repeatEveryWeekCode;
    private String repeatEveryDayCode;
    private String appointmentStartTime;

    public String getAppointmentStartTime() {
        return appointmentStartTime;
    }

    public void setAppointmentStartTime(String appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
    }

    public String getAppointmentEndTime() {
        return appointmentEndTime;
    }

    public void setAppointmentEndTime(String appointmentEndTime) {
        this.appointmentEndTime = appointmentEndTime;
    }

    private String appointmentEndTime;

    //Common Fields
    private String meetingID;
    private String meetingType;
    private String leadId;
    private String meetingDate;
    private String activityTypeID;
    private String product;
    private String meetingDuration;
    private String meetingReason;
    private String meetingDes;
    private String CustomerComment;
    private String actionables;
    private String category;
    private String etCategory;
    private String subCategory;
    private String etSubCategory;
    private String baClient;
    private String outStation;
    private String clientCode;
    private String meetingAddressTypeId;
    private String meetingAddress;

    private String meetingLongitude;

    private String meetingLatitude;
    private String amount;
    private String geoArea;
    private String geoLongitude;
    private String geoLatitude;
    private String pincode;
    private String isPotentialPMSProduct;
    private String touchedUntouchedId;
    private String feedbackId;
    private String meetingEventId;

    public String getMeetingEventId() {
        return meetingEventId;
    }

    public void setMeetingEventId(String meetingEventId) {
        this.meetingEventId = meetingEventId;
    }

    public String getTouchedUntouchedId() {
        return touchedUntouchedId;
    }

    public void setTouchedUntouchedId(String touchedUntouchedId) {
        this.touchedUntouchedId = touchedUntouchedId;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public MeetingApptModel setProductList(List<CreateMeetingProductModel> productList) {
        this.productList = productList;
        return this;
    }

    private List<CreateMeetingProductModel> productList;
    private List<CreateMeetingTravellingModel> travellingList;

    public List<CreateMeetingTravellingModel> getTravellingList() {
        return travellingList;
    }

    public void setTravellingList(List<CreateMeetingTravellingModel> travellingList) {
        this.travellingList = travellingList;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    private String clientName;
    private String ucid;

    //    private String repeatWeekCode;
//    private String weekDayCode;
    public String getMeetingDes() {
        return meetingDes;
    }

    public void setMeetingDes(String meetingDes) {
        this.meetingDes = meetingDes;
    }

    public String getCustomerComment() {
        return CustomerComment;
    }

    public void setCustomerComment(String customerComment) {
        CustomerComment = customerComment;
    }

    public String getActionables() {
        return actionables;
    }

    public void setActionables(String actionables) {
        this.actionables = actionables;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEtCategory() {
        return etCategory;
    }

    public void setEtCategory(String etCategory) {
        this.etCategory = etCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getEtSubCategory() {
        return etSubCategory;
    }

    public void setEtSubCategory(String etSubCategory) {
        this.etSubCategory = etSubCategory;
    }


    public String getRepeatOnCode() {
        return repeatOnCode;
    }

    public void setRepeatOnCode(String repeatOnCode) {
        if (repeatOnCode != null) {
            this.repeatOnCode = repeatOnCode;
        } else {
            this.repeatOnCode = "";
        }
    }

    private String repeatOnCode;

    public String getMeetingReason() {
        return meetingReason;
    }

    public void setMeetingReason(String meetingReason) {
        this.meetingReason = meetingReason;
    }

    public String getMeetingDuration() {
        return meetingDuration;
    }

    public void setMeetingDuration(String meetingDuration) {
        this.meetingDuration = meetingDuration;
    }


    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public String getIsPotentialPMSProduct() {
        return isPotentialPMSProduct;
    }

    public void setIsPotentialPMSProduct(String isPotentialPMSProduct) {
        this.isPotentialPMSProduct = isPotentialPMSProduct;
    }

    public List<AddedSplAttendeeModel> getSplMeetingAttendee() {
        return splMeetingAttendee;
    }

    public void setSplMeetingAttendee(List<AddedSplAttendeeModel> splMeetingAttendee) {
        this.splMeetingAttendee = splMeetingAttendee;
    }

    public List<AddedAttendeeModel> getMeetingAttendee() {
        return meetingAttendee;
    }

    public void setMeetingAttendee(List<AddedAttendeeModel> meetingAttendee) {
        this.meetingAttendee = meetingAttendee;
    }

    public int getMeetingReasonID() {
        return meetingReasonID;
    }

    public void setMeetingReasonID(int meetingReasonID) {
        this.meetingReasonID = meetingReasonID;
    }

    public int getMeetingDurationID() {
        return meetingDurationID;
    }

    public void setMeetingDurationID(int meetingDurationID) {
        this.meetingDurationID = meetingDurationID;
    }

    public String getRepeatEveryWeekCode() {
        return repeatEveryWeekCode;
    }

    public void setRepeatEveryWeekCode(String repeatEveryWeekCode) {
        this.repeatEveryWeekCode = repeatEveryWeekCode;
    }

    public String getRepeatEveryDayCode() {
        return repeatEveryDayCode;
    }

    public void setRepeatEveryDayCode(String repeatEveryDayCode) {
        this.repeatEveryDayCode = repeatEveryDayCode;
    }

    public String getMeetingID() {
        return meetingID;
    }

    public void setMeetingID(String meetingID) {
        this.meetingID = meetingID;
    }


    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getActivityTypeID() {
        return activityTypeID;
    }

    public void setActivityTypeID(String activityTypeID) {
        this.activityTypeID = activityTypeID;
    }

    public String getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(String meetingType) {
        this.meetingType = meetingType;
    }

    public String getBaClient() {
        return baClient;
    }

    public void setBaClient(String baClient) {
        this.baClient = baClient;
    }

    public String getOutStation() {
        return outStation;
    }

    public void setOutStation(String outStation) {
        this.outStation = outStation;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getMeetingAddressTypeId() {
        return meetingAddressTypeId;
    }

    public void setMeetingAddressTypeId(String meetingAddressTypeId) {
        this.meetingAddressTypeId = meetingAddressTypeId;
    }

    public String getMeetingAddress() {
        return meetingAddress.replace("\"", "'");
    }

    public void setMeetingAddress(String meetingAddress) {
        this.meetingAddress = meetingAddress;
    }

    public String getMeetingLongitude() {
        return meetingLongitude;
    }

    public void setMeetingLongitude(String meetingLongitude) {
        this.meetingLongitude = meetingLongitude;
    }

    public String getMeetingLatitude() {
        return meetingLatitude;
    }

    public void setMeetingLatitude(String meetingLatitude) {
        this.meetingLatitude = meetingLatitude;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getGeoArea() {
        return geoArea;
    }

    public void setGeoArea(String geoArea) {
        this.geoArea = geoArea;
    }

    public String getGeoLongitude() {
        return geoLongitude;
    }

    public void setGeoLongitude(String geoLongitude) {
        this.geoLongitude = geoLongitude;
    }

    public String getGeoLatitude() {
        return geoLatitude;
    }

    public void setGeoLatitude(String geoLatitude) {
        this.geoLatitude = geoLatitude;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    /**
     * @return
     */
    public String getJson() {

        String json = "{";

        if (meetingType.equalsIgnoreCase(MEETING_TYPE_MEETING)) {
            json += "\"meetingAttendee\":" + getAttendeeJson();
            json += ",\"ActivityTypeID\":" + getActivityTypeID();
            json += ",\"MeetingDate\":\"" + getMeetingDate() + "\"";
            json += ",\"MeetingReasonID\":" + getMeetingReason();
            json += ",\"MeetingDurationID\":" + getMeetingDuration();
            json += ",\"MeetingDescription\":\"" + getMeetingDes() + "\"";
            json += ",\"CustomerRemark\":\"" + getCustomerComment() + "\"";
            json += ",\"ActionableID\":" + getActionables();
            json += ",\"CategoryID\":" + getCategory();
            json += ",\"CategoryDescription\":\"" + getEtCategory() + "\"";
            json += ",\"SubCategoryID\":" + getSubCategory();
            json += ",\"SubCategoryDescription\":\"" + getEtSubCategory() + "\"";
            json += ",\"BAClientMeetingID \":\"" + getBaClient() + "\"";
            json += ",\"OutstationMeetingID \":\"" + getOutStation() + "\"";
            json += ",\"Clientcode \":\"" + getClientCode() + "\"";
            json += ",\"Clientname \":\"" + getClientName() + "\"";
            json += ",\"MeetingObjectiveID\":" + getMeetingObjectiveID();
        }
        if (meetingType.equalsIgnoreCase(MEETING_TYPE_APPT)) {
            json += "\"meetingAttendee\":" + getAttendeeJson();
            json += ",\"MeetingDate\":\"" + getMeetingDate() + "\"";
            json += ",\"ActivityTypeID\":" + getActivityTypeID();
            json += ",\"repeatationStartOn\":\"" + getAppointmentStartTime() + "\"";
            json += ",\"repeatationEndsOn\":\"" + getAppointmentEndTime() + "\"";
            json += ",\"repeatEveryWeekCode\":\"" + getRepeatEveryWeekCode() + "\"";
            json += ",\"repeatEveryDayCode\":\"" + getRepeatEveryDayCode() + "\"";
            json += ",\"repeatOnCode\":\"" + getRepeatOnCode() + "\"";
            if (getMeetingID() != null && getMeetingID().length() > 1) {
                json += ",\"MeetingID\":" + getMeetingID();
            }

        }
        if (displayType.equalsIgnoreCase(DISPLAY_TYPE_MEETING)) {
            json += ",\"MeetingID\":" + getMeetingID();
        }
        if (displayType.equalsIgnoreCase("A") && meetingType.equalsIgnoreCase(MEETING_TYPE_MEETING)) {
            json += ",\"MeetingAddressTypeId\":\"" + getMeetingAddressTypeId() + "\"";
            json += ",\"MeetingAddress\":\"" + getMeetingAddress() + "\"";
            json += ",\"MeetingLatitude\":\"" + getMeetingLatitude() + "\"";
            json += ",\"MeetingLongitude\":\"" + getMeetingLongitude() + "\"";
            json += ",\"TravellingModeIds\":\"" + getTravellingJson() + "\"";
            json += ",\"Amount\":\"" + getAmount() + "\"";
            json += ",\"Products\":" + getProductJson();
            json += ",\"GeoArea\":\"" + getGeoArea() + "\"";
            json += ",\"GeoLongitude\":\"" + getGeoLongitude() + "\"";
            json += ",\"GeoLatitude\":\"" + getGeoLatitude() + "\"";
            json += ",\"IsPotentialPMSProduct\":\"" + getIsPotentialPMSProduct() + "\"";
            json += ",\"Pincode\":\"" + getPincode() + "\"";
        }
        //common part
        json += ",\"LeadId\":\"" + getLeadId() + "\"";
        json += ",\"MeetingType\":\"" + meetingType + "\"";
        json += ",\"DisplayType\":\"" + displayType + "\"";
        json += ",\"UCID\":\"" + getUcid() + "\"";
        json += ",\"ProductTypeID\":" + getProduct();
        json += ",\"TouchedUntouchedID\":" + getTouchedUntouchedId();
        json += ",\"FeedbackID\":" + getFeedbackId();
        json += ",\"MeetingEventId\":\"" + getMeetingEventId()+ "\"";
        json += "}";

        return json;
    }

   /* private String getProductJson() {
        String json = "[";
        for (CreateMeetingProductModel model : productList) {
            String meetingJson = model.getId();
            json += meetingJson + ",";
        }

        json = json.substring(0, json.length() - 1); //remove lash comma
        json += "]";
        return json;
    }*/

    private String getProductJson() {
       if (productList == null || productList.isEmpty()) {
            return "[]"; // Return empty array JSON if no products
        }

        StringBuilder json = new StringBuilder("[");
        for (CreateMeetingProductModel model : productList) {
            // Assuming getId() returns the product ID as a string,
            // you need to add quotes around it for valid JSON strings:
            json.append(model.getId()).append(",");
        }

        json.deleteCharAt(json.length() - 1); // Remove last comma
        json.append("]");
        return json.toString();
    }

    private String getTravellingJson() {
        String json = "";
        for (CreateMeetingTravellingModel model : travellingList) {
            String meetingJson = model.getTravellingId();
            json += meetingJson + ",";
        }

        if (!json.isEmpty()) {
            json = json.substring(0, json.length() - 1); //remove lash comma
        }
        json += "";
        return json;
    }

    private String getAttendeeJson() {
        String json = "[";
        for (AddedAttendeeModel model : meetingAttendee) {
            String meetingJson = model.getJson();
            json += meetingJson + ",";
        }
        for (AddedSplAttendeeModel model : splMeetingAttendee) {
            String meetingJson = model.getJson();
            json += meetingJson + ",";
        }
        if (!getDisplayType().equalsIgnoreCase("U")) {
            json += AddedAttendeeModel.getUserJson() + ",";
        }
        json = json.substring(0, json.length() - 1); //remove lash comma
        if (getDisplayType().equalsIgnoreCase("U")) {
            if (json.isEmpty()) {
                json += "[";
            }
        }

        json += "]";
        return json;
    }

    public String getMeetingObjectiveID() {
        return meetingObjectiveID;
    }

    public void setMeetingObjectiveID(String meetingObjectiveID) {
        this.meetingObjectiveID = meetingObjectiveID;
    }

}
