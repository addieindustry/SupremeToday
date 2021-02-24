package com.project.model;


import javafx.beans.property.SimpleStringProperty;

public class SessionModel {

    private SimpleStringProperty sessionId;
    private SimpleStringProperty sessionName;
    private SimpleStringProperty isActive;
    private SimpleStringProperty dateTime;

    public SessionModel(String sessionId, String sessionName,String isActive, String dateTime) {
        this.sessionId = new SimpleStringProperty(sessionId);
        this.sessionName = new SimpleStringProperty(sessionName);
        this.isActive = new SimpleStringProperty(isActive);
        this.dateTime = new SimpleStringProperty(dateTime);
    }

    public SessionModel(String sessionName, String isActive) {
        this.sessionName = new SimpleStringProperty(sessionName);
        this.isActive = new SimpleStringProperty(isActive);
    }

    public String getSessionId() {
        return sessionId.get();
    }

    public SimpleStringProperty sessionIdProperty() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId.set(sessionId);
    }

    public String getSessionName() {
        return sessionName.get();
    }

    public SimpleStringProperty sessionNameProperty() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName.set(sessionName);
    }

    public String getIsActive() {
        return isActive.get();
    }

    public SimpleStringProperty isActiveProperty() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive.set(isActive);
    }

    public String getDateTime() {
        return dateTime.get();
    }

    public SimpleStringProperty dateTimeProperty() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime.set(dateTime);
    }
}
