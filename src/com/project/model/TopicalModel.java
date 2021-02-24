package com.project.model;

import javafx.beans.property.SimpleStringProperty;

public class TopicalModel {

    private SimpleStringProperty topical;


    public TopicalModel(String topical) {
        this.topical = new SimpleStringProperty(topical);

    }

    public String getTopical() {
        return topical.get();
    }

    public SimpleStringProperty topicalProperty() {
        return topical;
    }

    public void setTopical(String topical) {
        this.topical.set(topical);
    }
}
