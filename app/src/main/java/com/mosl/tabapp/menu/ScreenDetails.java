package com.mosl.tabapp.menu;

public class ScreenDetails {
    private int icon;
    private String title;
    private boolean enabled = true;
    private boolean parentView = false;
    private String details;

    public ScreenDetails(int icon, String title, String details, boolean parentView, boolean status) {
        this.icon = icon;
        this.title = title;
        this.details = details;
        this.enabled = status;
        this.parentView = parentView;
    }

    public ScreenDetails(int icon, String title, String details, boolean parentView) {
        this.icon = icon;
        this.title = title;
        this.details = details;
        this.parentView = parentView;
    }

    public ScreenDetails(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public ScreenDetails(int icon, String title, String details) {
        this.icon = icon;
        this.details = details;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean state) {
        this.enabled = state;
    }

    public String getTitle() {
        return title;

    }

    public void setTitle(String tle) {
        this.title = tle;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isParentView() {
        return parentView;
    }

    public void setParentView(boolean parentView) {
        this.parentView = parentView;
    }
}
