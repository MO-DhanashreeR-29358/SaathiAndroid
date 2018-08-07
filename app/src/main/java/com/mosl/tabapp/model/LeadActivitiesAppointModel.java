package com.mosl.tabapp.model;

import java.io.Serializable;

/**
 * Created by raju on 3/14/2017.
 */

public class LeadActivitiesAppointModel implements Serializable {

    private String status;
    private String meetingid;
    private String leadid;
    private String name;
    private String email;
    private String mobNo;
    private String date;
    private String time;
    private String attendeeEmp;
    private String product;
    private String statusName;
    private String activityType;
    private String meetingReason;
    private String meetingDuration;
    private String category;
    private String actionable;
    private String comments;
    private String stage;
    private String callback;
    private String queryDesc;
    private String ucid;
    private String createdBy;
    private String repeatOnCode;


    public String getStatus() {
        return status;
    }

    public LeadActivitiesAppointModel(String createdBy,String status, String meetingid, String leadid, String name, String email, String mobNo, String date, String time, String attendeeEmp, String product, String statusName, String activityType,String ucid,String repeatOnCode/*, String meetingReason, String meetingDuration, String category, String actionable, String comments, String stage, String callback, String queryDesc*/) {
        this.status = status;
        this.meetingid = meetingid;
        this.leadid = leadid;
        this.name = name;
        this.email = email;
        this.mobNo = mobNo;
        this.date = date;
        this.time = time;
        this.attendeeEmp = attendeeEmp;
        this.product = product;
        this.statusName = statusName;
        this.activityType = activityType;
        this.ucid = ucid;
        this.createdBy = createdBy;
        this.repeatOnCode = repeatOnCode;
          /*  this.meetingReason = meetingReason;
            this.meetingDuration = meetingDuration;
            this.category = category;
            this.actionable = actionable;
            this.comments = comments;
            this.stage = stage;
            this.callback = callback;
            this.queryDesc = queryDesc;*/
    }

    public LeadActivitiesAppointModel(String status, String meetingid, String leadid, String name, String email, String mobNo, String date, String time, String attendeeEmp, String product, String statusName, String activityType, String meetingReason, String meetingDuration, String category, String actionable, String comments, String stage, String callback, String queryDesc) {
        this.status = status;
        this.meetingid = meetingid;
        this.leadid = leadid;
        this.name = name;
        this.email = email;
        this.mobNo = mobNo;
        this.date = date;
        this.time = time;
        this.attendeeEmp = attendeeEmp;
        this.product = product;
        this.statusName = statusName;
        this.activityType = activityType;
        this.meetingReason = meetingReason;
        this.meetingDuration = meetingDuration;
        this.category = category;
        this.actionable = actionable;
        this.comments = comments;
        this.stage = stage;
        this.callback = callback;
        this.queryDesc = queryDesc;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public String getMeetingid() {
        return meetingid;
    }

    public void setMeetingid(String meetingid) {
        this.meetingid = meetingid;
    }

    public String getLeadid() {
        return leadid;
    }

    public void setLeadid(String leadid) {
        this.leadid = leadid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAttendeeEmp() {
        return attendeeEmp;
    }

    public void setAttendeeEmp(String attendeeEmp) {
        this.attendeeEmp = attendeeEmp;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getActionable() {
        return actionable;
    }

    public void setActionable(String actionable) {
        this.actionable = actionable;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getQueryDesc() {
        return queryDesc;
    }

    public void setQueryDesc(String queryDesc) {
        this.queryDesc = queryDesc;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getRepeatOnCode() {
        return repeatOnCode;
    }

    public void setRepeatOnCode(String repeatOnCode) {
        this.repeatOnCode = repeatOnCode;
    }
}



