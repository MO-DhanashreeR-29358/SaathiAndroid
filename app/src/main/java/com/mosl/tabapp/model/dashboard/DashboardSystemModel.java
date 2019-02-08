package com.mosl.tabapp.model.dashboard;

import java.io.Serializable;

/**
 * Created by raju on 6/2/2017.
 */

public class DashboardSystemModel implements Serializable {

    private static String leadSystemName = "";
    private static String groupSystemName = "";
    private static String clientSystemName = "";
    private static String targetAchivementSystemName = "";
    private static String monthTrends = "";
    private static String businessSnapshotSystemName = "";
    private static String productivitySystemName = "";
    private static String clientTradedSystemName = "";

    public static String getProductivitySystemName() {
        return productivitySystemName;
    }

    public static void setProductivitySystemName(String productivitySystemName) {
        DashboardSystemModel.productivitySystemName = productivitySystemName;
    }


    public static String getClientTradedSystemName() {
        return clientTradedSystemName;
    }

    public static void setClientTradedSystemName(String clientTradedSystemName) {
        DashboardSystemModel.clientTradedSystemName = clientTradedSystemName;
    }


    public static String getLeadSystemName() {
        return leadSystemName;
    }

    public static void setLeadSystemName(String leadSystemName) {
        DashboardSystemModel.leadSystemName = leadSystemName;
    }

    public static String getTargetAchivementSystemName() {
        return targetAchivementSystemName;
    }

    public static void setTargetAchivementSystemName(String targetAchivementSystemName) {
        DashboardSystemModel.targetAchivementSystemName = targetAchivementSystemName;
    }

    public static String getClientSystemName() {
        return clientSystemName;
    }

    public static void setClientSystemName(String clientSystemName) {
        DashboardSystemModel.clientSystemName = clientSystemName;
    }

    public static String getGroupSystemName() {
        return groupSystemName;
    }

    public static void setGroupSystemName(String groupSystemName) {
        DashboardSystemModel.groupSystemName = groupSystemName;
    }

    public static String getMonthTrends() {
        return monthTrends;
    }

    public static void setMonthTrends(String monthTrends) {
        DashboardSystemModel.monthTrends = monthTrends;
    }

    public static String getBusinessSnapshotSystemName() {

        return businessSnapshotSystemName;
    }

    public static void setBusinessSnapshotSystemName(String businessSnapshotSystemName) {
        DashboardSystemModel.businessSnapshotSystemName = businessSnapshotSystemName;
    }
}
