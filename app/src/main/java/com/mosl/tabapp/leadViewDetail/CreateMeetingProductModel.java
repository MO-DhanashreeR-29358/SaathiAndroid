package com.mosl.tabapp.leadViewDetail;

/**
 * Created by Raju on 20-07-2018.
 */
public class CreateMeetingProductModel {

    private String id;
    private String documentName;

    public CreateMeetingProductModel(String id, String documentName) {
        this.id = id;
        this.documentName = documentName;
    }

    public String getId() {

        return id;
    }

    public CreateMeetingProductModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getDocumentName() {
        return documentName;
    }

    public CreateMeetingProductModel setDocumentName(String documentName) {
        this.documentName = documentName;
        return this;
    }

}
