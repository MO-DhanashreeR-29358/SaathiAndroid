package com.mosl.tabapp.model;



import java.io.Serializable;
import java.util.List;

/**
 * Created by raju on 4/5/2017.
 */

public class CalenderModel implements Serializable {
    private static List<LeadActivitiesAppointModel> calenderList = null;

    public static List<LeadActivitiesAppointModel> getCalenderList() {
        return calenderList;
    }

    public static void setCalenderList(List<LeadActivitiesAppointModel> calenderList) {
        CalenderModel.calenderList = calenderList;
    }
}
