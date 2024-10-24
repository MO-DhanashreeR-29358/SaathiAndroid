package com.mosl.tabapp.model.bankmeeting;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class BankBranchCodeData implements Serializable, Parcelable {
  public static final Creator<BankBranchCodeData> CREATOR = new Creator<BankBranchCodeData>() {
    @Override
    public BankBranchCodeData createFromParcel(Parcel in) {
      return new BankBranchCodeData(in);
    }

    @Override
    public BankBranchCodeData[] newArray(int size) {
      return new BankBranchCodeData[size];
    }
  };
  private boolean noData;
  private String branchName;
  private String branchCode;

  public BankBranchCodeData(String branchName, String branchCode, boolean noData) {
    this.branchName = branchName;
    this.branchCode = branchCode;
    this.noData = noData;
  }

  private BankBranchCodeData(String employeeName, boolean noData) {
    this.branchName = employeeName;
    this.noData = noData;
  }

  private BankBranchCodeData(Parcel in) {
    noData = in.readByte() != 0;
    branchName = in.readString();
    branchCode = in.readString();
  }

  public static BankBranchCodeData getNoData(String errorMsg) {
    return new BankBranchCodeData(errorMsg, true);
  }

  public boolean isNoData() {
    return noData;
  }

  public void setNoData(boolean noData) {
    this.noData = noData;
  }

  public String getBranchName() {
    return branchName;
  }

  public void setBranchName(String branchName) {
    this.branchName = branchName;
  }

  public String getBranchCode() {
    return branchCode;
  }

  public void setBranchCode(String branchCode) {
    this.branchCode = branchCode;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte((byte) (noData ? 1 : 0));
    dest.writeString(branchName);
    dest.writeString(branchCode);
  }

  @Override
  public int describeContents() {
    return 0;
  }
}
