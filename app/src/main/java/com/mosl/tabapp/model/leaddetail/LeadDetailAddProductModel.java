package com.mosl.tabapp.model.leaddetail;

import java.util.ArrayList;
import java.util.List;

import static com.mosl.tabapp.common.CRMAppUtil.isBlank;


public class LeadDetailAddProductModel {

    private String stageId;
    private String stageValue;
    private String primaryRM;
    private String statusId;
    private String statusValue;
    private String interestedIn;
    private String interestedInValue;
    private String productSpecialist;
    private String clientCode;
    private String assignedBy;
    private String assignedByDate;
    private String leadMappingProductId;
    private boolean isDefault;
    private boolean isCrossSellingProduct;
    private String isProSpeEditDisabled;
    private String isProductDisabled;
    private List<CrossSellingProductsModel> CrossSellingProductsModelList = new ArrayList<>();

    public List<CrossSellingProductsModel> getCrossSellingProductsModelList() {
        return CrossSellingProductsModelList;
    }

    public LeadDetailAddProductModel setCrossSellingProductsModelList(List<CrossSellingProductsModel> crossSellingProductsModelList) {
        CrossSellingProductsModelList = crossSellingProductsModelList;
        return this;
    }

    public LeadDetailAddProductModel(String stageId, String stageValue, String statusId,
        String statusValue, String interestedIn,
        String interestedInValue, String primaryRM, String productSpecialist, String assignedBy,
        String assignedByDate,
        String clientCode, String leadMappingProductId, boolean isDefault,
        List<CrossSellingProductsModel> CrossSellingProductsModelList,
        boolean IsCrossSellingProduct, String isProSpeEditDisabled, String isProductDisabled) {
        this.stageId = stageId;
        this.stageValue = stageValue;
        this.statusId = statusId;
        this.statusValue = statusValue;
        this.interestedIn = interestedIn;
        this.interestedInValue = interestedInValue;
        this.primaryRM = primaryRM;
        this.productSpecialist = productSpecialist;
        this.assignedBy = assignedBy;
        this.assignedByDate = assignedByDate;
        this.clientCode = clientCode;
        this.leadMappingProductId = leadMappingProductId;
        this.isDefault = isDefault;
        this.isCrossSellingProduct = IsCrossSellingProduct;
        this.CrossSellingProductsModelList = CrossSellingProductsModelList;
        this.isProductDisabled = isProductDisabled;
        this.isProSpeEditDisabled = isProSpeEditDisabled;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isCrossSellingProduct() {
        return isCrossSellingProduct;
    }

    public void setCrossSellingProduct(boolean crossSellingProduct) {
        isCrossSellingProduct = crossSellingProduct;
    }
    public String getPrimaryRM() {
        return primaryRM;
    }

    public void setPrimaryRM(String primaryRM) {
        this.primaryRM = primaryRM;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getStageValue() {
        return stageValue;
    }

    public void setStageValue(String stageValue) {
        this.stageValue = stageValue;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
    }

    public String getInterestedIn() {
        return interestedIn;
    }

    public void setInterestedIn(String interestedIn) {
        this.interestedIn = interestedIn;
    }

    public String getInterestedInValue() {
        return interestedInValue;
    }

    public void setInterestedInValue(String interestedInValue) {
        this.interestedInValue = interestedInValue;
    }

    public String getProductSpecialist() {
        return productSpecialist;
    }

    public void setProductSpecialist(String productSpecialist) {
        this.productSpecialist = productSpecialist;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public String getAssignedByDate() {
        return assignedByDate;
    }

    public void setAssignedByDate(String assignedByDate) {
        this.assignedByDate = assignedByDate;
    }

    public String getLeadMappingProductId() {
        return leadMappingProductId;
    }

    public void setLeadMappingProductId(String leadMappingProductId) {
        this.leadMappingProductId = leadMappingProductId;
    }

    public String getIsProSpeEditDisabled() {
        return isProSpeEditDisabled;
    }

    public void setIsProSpeEditDisabled(String isProSpeEditDisabled) {
        this.isProSpeEditDisabled = isProSpeEditDisabled;
    }

    public String getIsProductDisabled() {
        return isProductDisabled;
    }

    public void setIsProductDisabled(String isProductDisabled) {
        this.isProductDisabled = isProductDisabled;
    }

    public String getJson() {
        String json = "{";
        json += "\"InterestedIn\":" + getInterestedIn();
        if (!isBlank(getInterestedIn())) {
            json += ",\"ProductID\":" + getInterestedIn();
        }
        json += ",\"PrimaryRM\":\"" + getPrimaryRM() + "\"";
        json += ",\"ProductSpecialist\":\"" + getProductSpecialist() + "\"";
//        json += ",\"SecondaryRM\":\"" + getSecondaryRM() + "\"";

        if (!isBlank(getClientCode())) {
            json += ",\"ClientCode\":\"" + getClientCode() + "\"";
        }
        if (!isBlank(getAssignedBy())) {
            json += ",\"AssignedBy\":\"" + getAssignedBy() + "\"";
        }
        if (!isBlank(getAssignedByDate())) {
            json += ",\"AssignedByDate\":\"" + getAssignedByDate() + "\"";
        }
        json += ",\"StageID\":" + getStageId();
        json += ",\"StatusID\":" + getStatusId();
        json += ",\"lead\":" + null;
        json += "}";
        return json;
    }
}
