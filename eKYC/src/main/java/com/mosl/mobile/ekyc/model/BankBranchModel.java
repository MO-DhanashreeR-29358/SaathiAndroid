package com.mosl.mobile.ekyc.model;

import java.io.Serializable;

/**
 * Created by Shefali on 11/08/16.
 */
public class BankBranchModel implements Serializable {
    String bankName, branchName, micrCode, ifscCode, bankAdd, eqOnlineBankFlag, commOnlineBankFlag;

    public BankBranchModel(String bankName, String branchName, String micrCode, String ifscCode, String bankAdd, String eqOnlineBankFlag, String commOnlineBankFlag) {
        this.bankName = bankName;
        this.branchName = branchName;
        this.micrCode = micrCode;
        this.ifscCode = ifscCode;
        this.bankAdd = bankAdd;
        this.eqOnlineBankFlag = eqOnlineBankFlag;
        this.commOnlineBankFlag = commOnlineBankFlag;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getMicrCode() {
        return micrCode;
    }

    public void setMicrCode(String micrCode) {
        this.micrCode = micrCode;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBankAdd() {
        return bankAdd;
    }

    public void setBankAdd(String bankAdd) {
        this.bankAdd = bankAdd;
    }

    public String getEqOnlineBankFlag() {
        return eqOnlineBankFlag;
    }

    public void setEqOnlineBankFlag(String eqOnlineBankFlag) {
        this.eqOnlineBankFlag = eqOnlineBankFlag;
    }

    public String getCommOnlineBankFlag() {
        return commOnlineBankFlag;
    }

    public void setCommOnlineBankFlag(String commOnlineBankFlag) {
        this.commOnlineBankFlag = commOnlineBankFlag;
    }
}
