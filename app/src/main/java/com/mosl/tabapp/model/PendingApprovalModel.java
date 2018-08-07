package com.mosl.tabapp.model;

import java.io.Serializable;

/**
 * Created by sun on 3/8/2017.
 */

public class PendingApprovalModel implements Serializable {
        private String mPendingLead;
        private String mPendingLeadCount;

    public PendingApprovalModel(String mPendingLead, String mPendingLeadCount, String entityField) {
        this.mPendingLead = mPendingLead;
        this.mPendingLeadCount = mPendingLeadCount;
        this.entityField = entityField;
    }

    private String entityField;

    public String getmPendingLead() {
        return mPendingLead;
    }

    public void setmPendingLead(String mPendingLead) {
        this.mPendingLead = mPendingLead;
    }

    public String getmPendingLeadCount() {
        return mPendingLeadCount;
    }

    public void setmPendingLeadCount(String mPendingLeadCount) {
        this.mPendingLeadCount = mPendingLeadCount;
    }

    public String getEntityField() {
        return entityField;
    }

    public void setEntityField(String entityField) {
        this.entityField = entityField;
    }
}
