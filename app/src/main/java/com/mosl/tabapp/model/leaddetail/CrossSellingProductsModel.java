package com.mosl.tabapp.model.leaddetail;

/**
 * Created by Raju on 23-04-2018.
 */

public class CrossSellingProductsModel {
    private String LeadCrossSellingProductId;
    private String CrossSellingProductId;
    private String CrossSellingStatusId;
    private String CrossSellingProductName;
    private String CrossSellingStatusName;

    public CrossSellingProductsModel(String LeadCrossSellingProductId,String CrossSellingProductId,String CrossSellingStatusId,
                              String CrossSellingProductName,String CrossSellingStatusName){
        this.LeadCrossSellingProductId=LeadCrossSellingProductId;
                this.CrossSellingProductId=CrossSellingProductId;
                this.CrossSellingStatusId=CrossSellingStatusId;
                this.CrossSellingProductName=CrossSellingProductName;
                this.CrossSellingStatusName=CrossSellingStatusName;
    }
    public String getLeadCrossSellingProductId() {
        return LeadCrossSellingProductId;
    }

    public CrossSellingProductsModel setLeadCrossSellingProductId(String leadCrossSellingProductId) {
        LeadCrossSellingProductId = leadCrossSellingProductId;
        return this;
    }

    public String getCrossSellingProductId() {
        return CrossSellingProductId;
    }

    public CrossSellingProductsModel setCrossSellingProductId(String crossSellingProductId) {
        CrossSellingProductId = crossSellingProductId;
        return this;
    }

    public String getCrossSellingStatusId() {
        return CrossSellingStatusId;
    }

    public CrossSellingProductsModel setCrossSellingStatusId(String crossSellingStatusId) {
        CrossSellingStatusId = crossSellingStatusId;
        return this;
    }

    public String getCrossSellingProductName() {
        return CrossSellingProductName;
    }

    public CrossSellingProductsModel setCrossSellingProductName(String crossSellingProductName) {
        CrossSellingProductName = crossSellingProductName;
        return this;
    }

    public String getCrossSellingStatusName() {
        return CrossSellingStatusName;
    }

    public CrossSellingProductsModel setCrossSellingStatusName(String crossSellingStatusName) {
        CrossSellingStatusName = crossSellingStatusName;
        return this;
    }
}
