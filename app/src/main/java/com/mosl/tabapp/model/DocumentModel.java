package com.mosl.tabapp.model;

import java.io.Serializable;

/**
 * Created by Vishal on 06-Apr-17.
 */
public class DocumentModel implements Serializable {

    private String creationDate;
    private String id;
    private String documentName;
    private String documentType;
    private String documentPath;
    private String documentFullPath;


    public DocumentModel(String id, String documentName, String documentType, String documentPath, String creationDate) {
        this.creationDate = creationDate;
        this.id = id;
        this.documentName = documentName;
        this.documentType = documentType;
        this.documentPath = documentPath;
    }

    public DocumentModel(String id, String documentName, String documentType, String documentPath, String creationDate, String documentFullPath) {
        this.creationDate = creationDate;
        this.id = id;
        this.documentName = documentName;
        this.documentType = documentType;
        this.documentPath = documentPath;
        this.documentFullPath = documentFullPath;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public String getDocumentFullPath() {
        if (documentFullPath == null) {
            return "";
        } else{
            return documentFullPath;
			}
    }

    public void setDocumentFullPath(String documentFullPath) {
        this.documentFullPath = documentFullPath;
    }
}