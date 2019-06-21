package com.mosl.tabapp.model;

import android.util.Log;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LeadFieldSectionModel implements Serializable {
    final static String TAG = LeadFieldSectionModel.class.getSimpleName();
    int FieldSectionId = 0;
    String SectionCode = "";
    String SectionName = "";
    String SectionHeader = "";
    List<LeadFieldValueModel> SelectionList;

    public int getFieldSectionId() {
        return FieldSectionId;
    }

    public void setFieldSectionId(int fieldSectionId) {
        FieldSectionId = fieldSectionId;
    }

    public String getSectionCode() {
        return SectionCode;
    }

    public void setSectionCode(String sectionCode) {
        SectionCode = sectionCode;
    }

    public String getSectionName() {
        return SectionName;
    }

    public void setSectionName(String sectionName) {
        SectionName = sectionName;
    }

    public String getSectionHeader() {
        return SectionHeader;
    }

    public void setSectionHeader(String sectionHeader) {
        SectionHeader = sectionHeader;
    }

    public List<LeadFieldValueModel> getSelectionList() {
        return SelectionList;
    }

    public void setSelectionList(List<LeadFieldValueModel> selectionList) {
        SelectionList = selectionList;
    }

    public static LeadFieldSectionModel getSectionFromListWithId(List<LeadFieldSectionModel> mlist,
        int sectionId) {
        if (mlist != null) {
            for (LeadFieldSectionModel a : mlist) {
                if (sectionId == a.getFieldSectionId()) {
                    return a;
                } else {
                    continue;
                }
            }
        }
        Log.w(TAG, "No LeadFieldSectionModel was found for id " + sectionId);
        return null;
    }

    public void addSelectionList(LeadFieldValueModel feild) {
        if (SelectionList == null) {
            SelectionList = new ArrayList<>();
        }
        SelectionList.add(feild);
    }
}
