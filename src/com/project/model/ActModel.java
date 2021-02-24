package com.project.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class ActModel {

    private SimpleStringProperty actName;
    private SimpleBooleanProperty checked = new SimpleBooleanProperty(false);

    public ActModel(String actName) {
        this.actName = new SimpleStringProperty(actName);
    }

    public SimpleBooleanProperty checkedProperty() {
        return this.checked;
    }

    public Boolean getChecked() {
        return this.checkedProperty().get();
    }

    public void setChecked(final Boolean checked) {
        this.checkedProperty().set(checked);
    }


    public String getActName() {
        return actName.get();
    }

    public SimpleStringProperty actNameProperty() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName.set(actName);
    }
}
