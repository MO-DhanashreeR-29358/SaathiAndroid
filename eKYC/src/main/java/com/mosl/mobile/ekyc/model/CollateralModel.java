package com.mosl.mobile.ekyc.model;

/**
 * Created by pramod on 25-11-2016.
 */

public class CollateralModel {
    int id;
    String section;
    String title;
    String fileName;
    String fileType;
    String summary;
    String localFilePath;
    String localVersion;
    String serverVersion;
    String url;
    String downloaded="N";

    public CollateralModel(int id, String section, String title, String fileName, String fileType, String summary, String localVersion, String serverVersion,String url,String downloaded) {
        this.id = id;
        this.section = section;
        this.title = title;
        this.fileName = fileName;
        this.fileType = fileType;
        this.summary = summary;
        this.localVersion = localVersion;
        this.serverVersion = serverVersion;
        this.downloaded = downloaded;
        url = url.replaceAll(" ", "%20");

        this.url = url;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLocalVersion() {
        return localVersion;
    }

    public void setLocalVersion(String localVersion) {
        this.localVersion = localVersion;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(String downloaded) {
        this.downloaded = downloaded;
    }
}
