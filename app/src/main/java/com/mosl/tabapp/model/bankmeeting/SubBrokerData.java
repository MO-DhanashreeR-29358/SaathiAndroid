package com.mosl.tabapp.model.bankmeeting;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import java.io.Serializable;

public class SubBrokerData implements Serializable, Parcelable {
  public static final Creator<SubBrokerData> CREATOR = new Creator<SubBrokerData>() {
    @Override
    public SubBrokerData createFromParcel(Parcel in) {
      return new SubBrokerData(in);
    }

    @Override
    public SubBrokerData[] newArray(int size) {
      return new SubBrokerData[size];
    }
  };
  private boolean noData;
  private String subBrokerName;
  private Integer bankSubBrokerId;
  private Integer bankBranchId;
  private Integer bankId;

  public SubBrokerData(String subBrokerName, Integer bankSubBrokerId, Integer bankBranchId,
      Integer bankId, boolean noData) {
    this.subBrokerName = subBrokerName;
    this.bankSubBrokerId = bankSubBrokerId;
    this.bankBranchId = bankBranchId;
    this.bankId = bankId;
    this.noData = noData;
  }

  private SubBrokerData(String subBrokerName, boolean noData) {
    this.subBrokerName = subBrokerName;
    this.noData = noData;
  }

  private SubBrokerData(Parcel in) {
    noData = in.readByte() != 0;
    subBrokerName = in.readString();
    if (in.readByte() == 0) {
      bankSubBrokerId = null;
    } else {
      bankSubBrokerId = in.readInt();
    }
    if (in.readByte() == 0) {
      bankBranchId = null;
    } else {
      bankBranchId = in.readInt();
    }
    if (in.readByte() == 0) {
      bankId = null;
    } else {
      bankId = in.readInt();
    }
  }

  public static SubBrokerData getNoData(String errorMsg) {
    return new SubBrokerData(errorMsg, true);
  }

  public String getSubBrokerName() {
    return subBrokerName;
  }

  public void setSubBrokerName(String subBrokerName) {
    this.subBrokerName = subBrokerName;
  }

  public Integer getBankSubBrokerId() {
    return bankSubBrokerId;
  }

  public void setBankSubBrokerId(Integer bankSubBrokerId) {
    this.bankSubBrokerId = bankSubBrokerId;
  }

  public Integer getBankBranchId() {
    return bankBranchId;
  }

  public void setBankBranchId(Integer bankBranchId) {
    this.bankBranchId = bankBranchId;
  }

  public Integer getBankId() {
    return bankId;
  }

  public void setBankId(Integer bankId) {
    this.bankId = bankId;
  }

  @NonNull @Override
  public String toString() {
    return subBrokerName;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte((byte) (noData ? 1 : 0));
    dest.writeString(subBrokerName);
    if (bankSubBrokerId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(bankSubBrokerId);
    }
    if (bankBranchId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(bankBranchId);
    }
    if (bankId == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(bankId);
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public boolean isNoData() {
    return noData;
  }

  public void setNoData(boolean noData) {
    this.noData = noData;
  }
}
