package com.project.model;


import javafx.beans.property.SimpleStringProperty;

public class ActSearchResultModel {
//doctype,title,roottitle,childid",

    private SimpleStringProperty id;
    private SimpleStringProperty type;
    private SimpleStringProperty title;
    private SimpleStringProperty rootTitle;
    private SimpleStringProperty childId;

    public ActSearchResultModel(String id,String type, String title, String rootTitle,String childId) {
        this.id = new SimpleStringProperty(id);
        this.type = new SimpleStringProperty(type);
        this.title = new SimpleStringProperty(title);
        this.rootTitle = new SimpleStringProperty(rootTitle);
        this.childId = new SimpleStringProperty(childId);
    }

    public ActSearchResultModel(String id,String type, String title) {
        this.id = new SimpleStringProperty(id);
        this.type = new SimpleStringProperty(type);
        this.title = new SimpleStringProperty(title);
//        this.childId = new SimpleStringProperty(childId);
    }


    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getRootTitle() {
        return rootTitle.get();
    }

    public SimpleStringProperty rootTitleProperty() {
        return rootTitle;
    }

    public void setRootTitle(String rootTitle) {
        this.rootTitle.set(rootTitle);
    }

    public String getChildId() {
        return childId.get();
    }

    public SimpleStringProperty childIdProperty() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId.set(childId);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }
}
