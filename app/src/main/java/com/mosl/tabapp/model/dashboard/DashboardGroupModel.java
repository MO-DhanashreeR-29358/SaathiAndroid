package com.mosl.tabapp.model.dashboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pramod on 19/04/17.
 */

public class DashboardGroupModel implements Serializable {
    private String id="";
    private String displayName="";
    private String systemName="";
    private String iconUrl1="";
    private String iconUrl2="";
    private boolean selected;
    private List<DashboardGroupModel> subGroupList = new ArrayList<>();
    private List<WidgetModel> widgetList = new ArrayList<>();
    private List<WidgetFilterModel> groupFilterList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public List<WidgetModel> getWidgetList() {
        return widgetList;
    }

    public void setWidgetList(List<WidgetModel> widgetList) {
        this.widgetList = widgetList;
    }

    public String getIconUrl1() {
        return iconUrl1;
    }

    public void setIconUrl1(String iconUrl1) {
        this.iconUrl1 = iconUrl1;
    }

    public String getIconUrl2() {
        return iconUrl2;
    }

    public void setIconUrl2(String iconUrl2) {
        this.iconUrl2 = iconUrl2;
    }

    public List<DashboardGroupModel> getSubGroupList() {
        return subGroupList;
    }

    public void setSubGroupList(List<DashboardGroupModel> subGroupList) {
        this.subGroupList = subGroupList;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public  DashboardGroupModel getGroupModel(String groupSystemName){
        DashboardGroupModel groupToReturn = null;
        for(DashboardGroupModel group : subGroupList){
            if(group.getSystemName().equalsIgnoreCase(groupSystemName)){
                groupToReturn = group;
                break;
            }
        }
        return groupToReturn;
    }

    public List<WidgetFilterModel> getGroupFilterList() {
        return groupFilterList;
    }

    public void setGroupFilterList(List<WidgetFilterModel> groupFilterList) {
        this.groupFilterList = groupFilterList;
    }
}
