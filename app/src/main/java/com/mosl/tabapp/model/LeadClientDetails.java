package com.mosl.tabapp.model;

import com.mosl.saathi.api.model.leadviewmain.ClientViewByCommandResponse;
import com.mosl.saathi.api.model.leadviewmain.ProductsItem;
import com.mosl.tabapp.constants.MOSLConstants;
import java.util.List;

public class LeadClientDetails implements MOSLConstants {
    //region members
    private static String name = "";
    private static String branchCode = "";
    private static String clientCode = "";
    private static String currentStage = "";
    private static String currentStageSince = "";
    private static String leadId = "";
    private static String mobile = "";
    private static Integer oaoUserId;

    //endregion
    //region constructor
    public LeadClientDetails() {
    }

    public LeadClientDetails(ClientViewByCommandResponse response) {

        try {
            List<ProductsItem> products = response.products;
            for (int i = 0; i < products.size(); i++) {
                if (i > 0) {
                    break;
                }
                ProductsItem item = products.get(i);
                branchCode = item.branchCode;
                clientCode = item.holdingNumber;
            }

            name = response.clientName;
            mobile = response.mobile;
            if (response.leadID != null) {
                leadId = response.leadID;
            }

            if (response.stageName != null) {
                currentStage = response.stageName;
            }

            if (response.pendingDays != null) {
                currentStageSince = response.pendingDays;
            }

            oaoUserId = response.oAOUserid;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Integer getOaoUserId() {
        return oaoUserId;
    }

    public static void setOaoUserId(Integer oaoUserId) {
        LeadClientDetails.oaoUserId = oaoUserId;
    }

    //endregion
    //region getter setter
    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        LeadClientDetails.name = name;
    }

    public static String getBranchCode() {
        return branchCode;
    }

    public static void setBranchCode(String branchCode) {
        LeadClientDetails.branchCode = branchCode;
    }

    public static String getClientCode() {
        return clientCode;
    }

    public static void setClientCode(String clientCode) {
        LeadClientDetails.clientCode = clientCode;
    }

    public static String getMobile() {
        return mobile;
    }

    public static void setMobile(String mobile) {
        LeadClientDetails.mobile = mobile;
    }
    //endregion

    public static String getCurrentStage() {
        return currentStage;
    }

    public static void setCurrentStage(String currentStage) {
        LeadClientDetails.currentStage = currentStage;
    }

    public static String getCurrentStageSince() {
        return currentStageSince;
    }

    public static void setCurrentStageSince(String currentStageSince) {
        LeadClientDetails.currentStageSince = currentStageSince;
    }

    public static String getLeadId() {
        return leadId;
    }

    public static void setLeadId(String leadId) {
        LeadClientDetails.leadId = leadId;
    }
}

