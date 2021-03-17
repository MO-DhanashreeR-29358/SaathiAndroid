package com.mosl.tabapp.model.dashboard;

import androidx.fragment.app.Fragment;

import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.model.TypeActionableFilterModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.mosl.tabapp.dashboard.base.DashboardFragment.ACTIONABLE;

/**
 * Created by pramod on 19/04/17.
 */

public class DashboardModel implements Serializable {
    //this will be filled if dashboard is being loaded by searching employee
    private String businessProfileIdForDashboard,productId,employeeCode;

    String id;
    String name;
    String dashboardFor;
    List<DashboardGroupModel> groupList = new ArrayList<>();


    public Fragment getGroupFragment(String systemName) {
        if (!CRMAppUtil.isBlank(systemName)) {
            switch (systemName) {
                case "": {
                    return null;
                }
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDashboardFor() {
        return dashboardFor;
    }

    public void setDashboardFor(String dashboardFor) {
        this.dashboardFor = dashboardFor;
    }

    public List<DashboardGroupModel> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<DashboardGroupModel> groupList) {
        this.groupList = groupList;
    }

    public boolean isGroupExists(String groupSystemName) {
        for (DashboardGroupModel group : groupList) {
            if (group.getSystemName().equalsIgnoreCase(groupSystemName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method will return the groupmodel by systemName
     *
     * @param groupSystemName
     * @return
     */
    public DashboardGroupModel getGroupModel(String groupSystemName) {
      //  DashboardGroupModel groupToReturn = null;
        DashboardGroupModel groupToReturn = new DashboardGroupModel();
        for (DashboardGroupModel group : groupList) {
            if (group.getSystemName().equalsIgnoreCase(groupSystemName)) {
                groupToReturn = group;
                break;
            }
        }
        return groupToReturn;
    }

    /**
     * this method will set the actionable menu counts
     *
     * @param widgetSystemName
     * @param count
     */
    public void setActionableCount(String widgetSystemName, String count, ArrayList<TypeActionableFilterModel> typeList) {
        DashboardGroupModel actionableGroup = getGroupModel(ACTIONABLE);
        if (actionableGroup != null) {
            for (WidgetModel widgetModel : actionableGroup.getWidgetList()) {
                if (widgetModel.getSystemName().equals(widgetSystemName)) {
                    widgetModel.setActionableCount(count);
                   // widgetModel.setSpinnerFilterList(arrayList);
                    widgetModel.setSpinnerFilterList(typeList);
                }
            }
        }
    }

    public String getBusinessProfileIdForDashboard() {
        return businessProfileIdForDashboard;
    }

    public void setBusinessProfileIdForDashboard(String businessProfileIdForDashboard) {
        this.businessProfileIdForDashboard = businessProfileIdForDashboard;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }
}
