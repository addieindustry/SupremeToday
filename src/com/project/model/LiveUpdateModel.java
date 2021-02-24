package com.project.model;


import javafx.beans.property.SimpleStringProperty;

public class LiveUpdateModel {

//    bookmarkid, sessionid, searchtype, filetype, parentid, childid, title, remark, createdon

    private SimpleStringProperty Id;
    private SimpleStringProperty version;
    private SimpleStringProperty courtId;
    private SimpleStringProperty courts;
    private SimpleStringProperty status;

    public LiveUpdateModel(String version, String courtId, String courts, String status) {
//        this.Id = new SimpleStringProperty(Id);
        this.version = new SimpleStringProperty(version);
        this.courtId = new SimpleStringProperty(courtId);
        this.courts = new SimpleStringProperty(courts);
        this.status = new SimpleStringProperty(status);
    }

    public String getId() {
        return Id.get();
    }

    public SimpleStringProperty idProperty() {
        return Id;
    }

    public void setId(String id) {
        this.Id.set(id);
    }

    public String getVersion() {
        return version.get();
    }

    public SimpleStringProperty versionProperty() {
        return version;
    }

    public void setVersion(String version) {
        this.version.set(version);
    }

    public String getCourtId() {
        return courtId.get();
    }

    public SimpleStringProperty courtIdProperty() {
        return courtId;
    }

    public void setCourtId(String courtId) {
        this.courtId.set(courtId);
    }

    public String getCourts() {
        return courts.get();
    }

    public SimpleStringProperty courtsProperty() {
        return courts;
    }

    public void setCourts(String courts) {
        this.courts.set(courts);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

}
