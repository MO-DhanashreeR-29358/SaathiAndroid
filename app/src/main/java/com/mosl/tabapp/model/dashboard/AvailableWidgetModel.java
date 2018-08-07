package com.mosl.tabapp.model.dashboard;

import java.io.Serializable;

/**
 * Created by pramod on 12/04/17.
 */

public class AvailableWidgetModel implements Serializable{
    private String id;
    private String dashboardId;
    private String widgetId;
    private String widgetName;
    private String dashboardName;
    private WidgetModel widgetModel;
    private int displayOrder;

    public String getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(String dashboardId) {
        this.dashboardId = dashboardId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }

    public String getWidgetName() {
        return widgetName;
    }

    public void setWidgetName(String widgetName) {
        this.widgetName = widgetName;
    }

    public String getDashboardName() {
        return dashboardName;
    }

    public void setDashboardName(String dashboardName) {
        this.dashboardName = dashboardName;
    }

    public WidgetModel getWidgetModel() {
        return widgetModel;
    }

    public void setWidgetModel(WidgetModel widgetModel) {
        this.widgetModel = widgetModel;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
