package com.project.model;

import javafx.beans.property.SimpleStringProperty;

public class TitleIdListModel {

    //id,title,authorname,crosscitations_s

    private SimpleStringProperty id;
    private SimpleStringProperty title;
//    private SimpleStringProperty hindiContent;

    public TitleIdListModel(String id, String title) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
//        this.hindiContent = new SimpleStringProperty(hindiContent);
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

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

}
