package com.mosl.tabapp.model;

import java.io.Serializable;

/**
 * Created by Vijay on 21/03/2017.
 */

public class TeamActionableInsightModel implements Serializable {

    private int actionableInsightImage;
    private String actionableInsight;
    private String actionableInsightCount;

    public TeamActionableInsightModel(int actionableInsightImage, String actionableInsight, String actionableInsightCount) {
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
