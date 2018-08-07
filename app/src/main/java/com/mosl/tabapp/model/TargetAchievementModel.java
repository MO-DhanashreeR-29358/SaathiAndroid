package com.mosl.tabapp.model;

import com.mosl.tabapp.model.dashboard.WidgetType;

import java.io.Serializable;

/**
 * Created by raju on 4/28/2017.
 */

public class TargetAchievementModel implements Serializable {
    private String label;
    private String forrmated;
    private String value;
    private WidgetType widgetType;

    public TargetAchievementModel(String label, String forrmated, String value, WidgetType widgetType) {
        this.label = label;
        this.forrmated = forrmated;
        this.value = value;
        this.widgetType = widgetType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getForrmated() {
        return forrmated;
    }

    public void setForrmated(String forrmated) {
        this.forrmated = forrmated;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public WidgetType getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(WidgetType widgetType) {
        this.widgetType = widgetType;
    }
}
