package com.mosl.tabapp.model;

import java.io.Serializable;

/**
 * Created by raju on 3/2/2017.
 */

public class SecondClickHeaderModel implements Serializable {
    private String name;
    private String profileName;
    private String profile;
    private String idName;
    private String id;
    private String currentStageName;
    private String currentStage;

    public SecondClickHeaderModel(String name, String profileName, String profile, String idName, String id, String currentStageName, String currentStage) {
        this.name = name;
        this.profileName = profileName;
        this.profile = profile;
        this.idName = idName;
        this.id = id;
        this.currentStageName = currentStageName;
        this.currentStage = currentStage;
    }

    public String getName() {
        return name;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getProfile() {
        return profile;
    }

    public String getIdName() {
        return idName;
    }

    public String getId() {
        return id;
    }

    public String getCurrentStageName() {
        return currentStageName;
    }

    public String getCurrentStage() {
        return currentStage;
    }
}
