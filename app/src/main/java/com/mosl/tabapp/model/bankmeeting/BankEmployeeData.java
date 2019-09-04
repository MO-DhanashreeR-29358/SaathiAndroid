package com.mosl.tabapp.model.bankmeeting;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

public class BankEmployeeData implements Serializable, Parcelable {
  public static final Creator<BankEmployeeData> CREATOR = new Creator<BankEmployeeData>() {
    @Override
    public BankEmployeeData createFromParcel(Parcel in) {
      return new BankEmployeeData(in);
    }

    @Override
    public BankEmployeeData[] newArray(int size) {
      return new BankEmployeeData[size];
    }
  };
  private boolean noData;
  private String employeeName;
  private String employeeCode;

  public BankEmployeeData(String employeeName, String employeeCode, boolean noData) {
    this.employeeName = employeeName;
    this.employeeCode = employeeCode;
    this.noData = noData;
  }

  private BankEmployeeData(String employeeName, boolean noData) {
    this.employeeName = employeeName;
    this.noData = noData;
  }

  private BankEmployeeData(Parcel in) {
    noData = in.readByte() != 0;
    employeeName = in.readString();
    employeeCode = in.readString();
  }

  public static BankEmployeeData getNoData(String errorMsg) {
    return new BankEmployeeData(errorMsg, true);
  }

  public boolean isNoData() {
    return noData;
  }

  public void setNoData(boolean noData) {
    this.noData = noData;
  }

  public String getEmployeeName() {
    return employeeName;
  }

  public void setEmployeeName(String employeeName) {
    this.employeeName = employeeName;
  }

  public String getEmployeeCode() {
    return employeeCode;
  }

  public void setEmployeeCode(String employeeCode) {
    this.employeeCode = employeeCode;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte((byte) (noData ? 1 : 0));
    dest.writeString(employeeName);
    dest.writeString(employeeCode);
  }

  @Override
  public int describeContents() {
    return 0;
  }
}
