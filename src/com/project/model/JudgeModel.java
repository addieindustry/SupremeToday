package com.project.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class JudgeModel {

    private SimpleStringProperty judgeName;
    private SimpleBooleanProperty checked = new SimpleBooleanProperty(false);

    public JudgeModel(String actName) {
        this.judgeName = new SimpleStringProperty(actName);
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

    public String getJudgeName() {
        return judgeName.get();
    }

    public SimpleStringProperty judgeNameProperty() {
        return judgeName;
    }

    public void setJudgeName(String judgeName) {
        this.judgeName.set(judgeName);
    }
}
