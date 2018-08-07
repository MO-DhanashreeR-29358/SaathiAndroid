package com.mosl.tabapp.model.dashboard;

import android.os.Parcel;
import android.os.Parcelable;

import com.mosl.tabapp.model.TypeActionableFilterModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pramod on 12/04/17.
 */

public class WidgetModel implements Serializable, Parcelable {
    private String Id = "";
    private String detailReportStoredProcedureParameters = "";
    private Map widgetStoredProcedureParameters = new HashMap();
    private String apiUrl = "";
    private String systemName = "";
    private String detailReportStoredProcedure = "";
    private String name = "";
    private String sidgetStoredProcedure = "";
    private String apiParameters = "";
    private String iconUrl1 = "";
    private String iconUrl2 = "";
    private boolean selected = false;
    private String actionableCount = ""; //count for actionable menuItem
    private List<WidgetFilterModel> widgetFilterList = new ArrayList<>();
    private ArrayList<TypeActionableFilterModel> spinnerFilterList = new ArrayList<>();
    ;

    private List<WidgetType> widgetTypeList = new ArrayList<>();

    private boolean showActionableCount = false;
    private Integer previousSelectedPosition = 0;

    public List<WidgetType> getWidgetTypeList() {
        return widgetTypeList;
    }

    public void setWidgetTypeList(List<WidgetType> widgetTypeList) {
        this.widgetTypeList = widgetTypeList;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDetailReportStoredProcedureParameters() {
        return detailReportStoredProcedureParameters;
    }

    public void setDetailReportStoredProcedureParameters(String detailReportStoredProcedureParameters) {
        this.detailReportStoredProcedureParameters = detailReportStoredProcedureParameters;
    }

    public Map getWidgetStoredProcedureParameters() {
        return widgetStoredProcedureParameters;
    }

    public void setWidgetStoredProcedureParameters(Map widgetStoredProcedureParameters) {
        this.widgetStoredProcedureParameters = widgetStoredProcedureParameters;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getDetailReportStoredProcedure() {
        return detailReportStoredProcedure;
    }

    public void setDetailReportStoredProcedure(String detailReportStoredProcedure) {
        this.detailReportStoredProcedure = detailReportStoredProcedure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSidgetStoredProcedure() {
        return sidgetStoredProcedure;
    }

    public void setSidgetStoredProcedure(String sidgetStoredProcedure) {
        this.sidgetStoredProcedure = sidgetStoredProcedure;
    }

    public String getApiParameters() {
        return apiParameters;
    }

    public void setApiParameters(String apiParameters) {
        this.apiParameters = apiParameters;
    }

    public String getIconUrl1() {
        return iconUrl1;
    }

    public void setIconUrl1(String iconUrl1) {
        this.iconUrl1 = iconUrl1;
    }

    public String getIconUrl2() {
        return iconUrl2;
    }

    public void setIconUrl2(String iconUrl2) {
        this.iconUrl2 = iconUrl2;
    }

    public String getActionableCount() {
        return actionableCount;
    }

    public void setActionableCount(String actionableCount) {
        this.actionableCount = actionableCount;
    }

    public List<WidgetFilterModel> getWidgetFilterList() {
        return widgetFilterList;
    }

    public void setWidgetFilterList(List<WidgetFilterModel> widgetFilterList) {
        this.widgetFilterList = widgetFilterList;
    }

    public ArrayList<TypeActionableFilterModel> getSpinnerFilterList() {
        return spinnerFilterList;
    }

    public void setSpinnerFilterList(ArrayList<TypeActionableFilterModel> spinnerFilterList) {
        this.spinnerFilterList = spinnerFilterList;
    }

    public WidgetModel() {
    }

    ////Parcelw
    protected WidgetModel(Parcel in) {
        Id = in.readString();
        detailReportStoredProcedureParameters = in.readString();
        apiUrl = in.readString();
        systemName = in.readString();
        detailReportStoredProcedure = in.readString();
        name = in.readString();
        sidgetStoredProcedure = in.readString();
        apiParameters = in.readString();
        iconUrl1 = in.readString();
        iconUrl2 = in.readString();
        selected = in.readByte() != 0;
        actionableCount = in.readString();
        widgetFilterList = in.createTypedArrayList(WidgetFilterModel.CREATOR);
        spinnerFilterList = in.createTypedArrayList(TypeActionableFilterModel.CREATOR);
        widgetTypeList = in.createTypedArrayList(WidgetType.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(detailReportStoredProcedureParameters);
        dest.writeString(apiUrl);
        dest.writeString(systemName);
        dest.writeString(detailReportStoredProcedure);
        dest.writeString(name);
        dest.writeString(sidgetStoredProcedure);
        dest.writeString(apiParameters);
        dest.writeString(iconUrl1);
        dest.writeString(iconUrl2);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeString(actionableCount);
        dest.writeTypedList(widgetFilterList);
        dest.writeTypedList(spinnerFilterList);
        dest.writeTypedList(widgetTypeList);
    }

    public boolean isShowActionableCount() {
        return showActionableCount;
    }

    public void setShowActionableCount(boolean showActionableCount) {
        this.showActionableCount = showActionableCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WidgetModel> CREATOR = new Creator<WidgetModel>() {
        @Override
        public WidgetModel createFromParcel(Parcel in) {
            return new WidgetModel(in);
        }

        @Override
        public WidgetModel[] newArray(int size) {
            return new WidgetModel[size];
        }
    };

    public Integer getPreviousSelectedPosition() {
        return previousSelectedPosition;
    }

    public void setPreviousSelectedPosition(Integer previousSelectedPosition) {
        this.previousSelectedPosition = previousSelectedPosition;
    }
}
