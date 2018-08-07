package com.mosl.tabapp.model;

import java.io.Serializable;

/**
 * Created by Vijay on 08/03/2017.
 */

public class AdvisoryActionableInsightModel implements Serializable {

    private int actionableInsightImage;
    private String actionableInsight;
    private String actionableInsightCount;

    public AdvisoryActionableInsightModel(int actionableInsightImage, String actionableInsight, String actionableInsightCount) {
        this.actionableInsightImage = actionableInsightImage;
        this.actionableInsight = actionableInsight;
        this.actionableInsightCount = actionableInsightCount;
    }

    public int getActionableInsightImage() {
        return actionableInsightImage;
    }

    public void setActionableInsightImage(int actionableInsightImage) {
        this.actionableInsightImage = actionableInsightImage;
    }

    public String getActionableInsight() {
        return actionableInsight;
    }

    public void setActionableInsight(String actionableInsight) {
        this.actionableInsight = actionableInsight;
    }

    public String getActionableInsightCount() {
        return actionableInsightCount;
    }

    public void setActionableInsightCount(String actionableInsightCount) {
        this.actionableInsightCount = actionableInsightCount;
    }
}
