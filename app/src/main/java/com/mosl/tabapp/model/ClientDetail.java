package com.mosl.tabapp.model;

import com.mosl.tabapp.constants.MOSLConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sanket Gurav on 8/14/2017.
 */

public class ClientDetail implements MOSLConstants {
    //region members
    private static String name="";
    private static String branchCode="";
    private static String clientCode="";
    private static String currentStage="";
    private static String currentStageSince="";
    private static String leadId="";
    private static String mobile="";


    public static Integer getOaoUserId() {
        return oaoUserId;
    }

    public static void setOaoUserId(Integer oaoUserId) {
        ClientDetail.oaoUserId = oaoUserId;
    }

    private static Integer oaoUserId;

    //endregion
    //region constructor
    public ClientDetail() {
    }

    public ClientDetail(JSONObject objDetail) {


        try {
            //String strFamilyUCID = objDetail.getString(FAMILYUCID);
            String strFamilyUCID = objDetail.getString(FAMILYUCID) == null ? "" : objDetail.getString(FAMILYUCID);
            JSONArray productsArray = objDetail.getJSONArray(PRODUCTS);
            for (int productsI = 0; productsI < productsArray.length(); productsI++) {
                //getting out only iterate first element
                if (productsI > 0) {
                    break;
                }
                JSONObject productsObj = productsArray.getJSONObject(productsI);
                branchCode = (productsObj.isNull(BRANCH_Code) || productsObj.getString(BRANCH_Code) == null) ? "" : productsObj.getString(BRANCH_Code);
                clientCode = (productsObj.isNull("HoldingNumber") || productsObj.getString("HoldingNumber") == null) ? "" : productsObj.getString("HoldingNumber");
            }

            name = (objDetail.isNull(CLIENT_NAME) || objDetail.getString(CLIENT_NAME) == null) ? "" : objDetail.getString(CLIENT_NAME);
            mobile = (objDetail.isNull("Mobile") || objDetail.getString("Mobile") == null) ? "" : objDetail.getString("Mobile");
            if (objDetail.has("LeadID")) {
                leadId = (objDetail.isNull("LeadID") || objDetail.getString("LeadID") == null) ? "" : objDetail.getString("LeadID");

            }
            if (objDetail.has("StageName")) {
                currentStage = (objDetail.isNull("StageName") || objDetail.getString("StageName") == null) ? "" : objDetail.getString("StageName");

            }
            if (objDetail.has("PendingDays")) {
                currentStageSince = (objDetail.isNull("PendingDays") || objDetail.getString("PendingDays") == null) ? "" : objDetail.getString("PendingDays");

            }
            oaoUserId = objDetail.has("OAOUserid") && !objDetail.isNull("OAOUserid") ? objDetail.getInt("OAOUserid") : 0;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //endregion
    //region getter setter
    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        ClientDetail.name = name;
    }

    public static String getBranchCode() {
        return branchCode;
    }

    public static void setBranchCode(String branchCode) {
        ClientDetail.branchCode = branchCode;
    }

    public static String getClientCode() {
        return clientCode;
    }

    public static void setClientCode(String clientCode) {
        ClientDetail.clientCode = clientCode;
    }

    public static String getMobile() {
        return mobile;
    }

    public static void setMobile(String mobile) {
        ClientDetail.mobile = mobile;
    }
    //endregion

    public static String getCurrentStage() {
        return currentStage;
    }

    public static void setCurrentStage(String currentStage) {
        ClientDetail.currentStage = currentStage;
    }

    public static String getCurrentStageSince() {
        return currentStageSince;
    }

    public static void setCurrentStageSince(String currentStageSince) {
        ClientDetail.currentStageSince = currentStageSince;
    }

    public static String getLeadId() {
        return leadId;
    }

    public static void setLeadId(String leadId) {
        ClientDetail.leadId = leadId;
    }
}
