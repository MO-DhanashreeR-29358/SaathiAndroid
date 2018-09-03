package com.mosl.tabapp.leadViewDetail;


public class CreateMeetingTravellingModel {

    private String travellingId;
    private String travellingName;

    public CreateMeetingTravellingModel(String travellingid, String travellingName) {
        this.travellingId = travellingid;
        this.travellingName = travellingName;
    }

    public String getTravellingId() {
        return travellingId;
    }

    public void setTravellingId(String travellingid) {
        this.travellingId = travellingid;
    }

    public String getTravellingName() {
        return travellingName;
    }

    public void setTravellingName(String travellingName) {
        this.travellingName = travellingName;
    }
}