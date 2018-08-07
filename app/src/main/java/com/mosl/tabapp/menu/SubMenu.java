package com.mosl.tabapp.menu;

/**
 * Created by Shefali on 12/16/2015.
 */
public class SubMenu {

    private String title;
    private boolean enabled = true;
    private String details;

    public SubMenu(String title, String details, boolean status) {
        this.title = title;
        this.details = details;
        this.enabled = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
