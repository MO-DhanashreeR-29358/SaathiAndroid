package com.mosl.tabapp.model;

/**
 * Created by Bhavesh on 09-10-2017.
 */

public class MenuReport {

    private String menuName;

    public MenuReport(String menuName, String systemsName) {
        this.menuName = menuName;
        this.systemsName = systemsName;
    }

    private String systemsName;



    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getSystemsName() {
        return systemsName;
    }

    public void setSystemsName(String systemsName) {
        this.systemsName = systemsName;
    }




}
