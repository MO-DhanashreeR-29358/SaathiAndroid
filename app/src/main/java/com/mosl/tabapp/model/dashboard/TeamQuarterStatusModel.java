package com.mosl.tabapp.model.dashboard;

/**
 * Created by raju on 6/19/2017.
 */

public class TeamQuarterStatusModel {

    private String name;
    private String incentivePayable;
    private String cuurentSlab;
    private String accounts;
    private String revenue;
    private String sip;
    private String pms;

    public TeamQuarterStatusModel(String name, String incentivePayable, String cuurentSlab, String accounts, String revenue, String sip, String pms) {
        this.name = name;
        this.incentivePayable = incentivePayable;
        this.cuurentSlab = cuurentSlab;
        this.accounts = accounts;
        this.revenue = revenue;
        this.sip = sip;
        this.pms = pms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIncentivePayable() {
        return incentivePayable;
    }

    public void setIncentivePayable(String incentivePayable) {
        this.incentivePayable = incentivePayable;
    }

    public String getCuurentSlab() {
        return cuurentSlab;
    }

    public void setCuurentSlab(String cuurentSlab) {
        this.cuurentSlab = cuurentSlab;
    }

    public String getAccounts() {
        return accounts;
    }

    public void setAccounts(String accounts) {
        this.accounts = accounts;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public String getSip() {
        return sip;
    }

    public void setSip(String sip) {
        this.sip = sip;
    }

    public String getPms() {
        return pms;
    }

    public void setPms(String pms) {
        this.pms = pms;
    }
}
