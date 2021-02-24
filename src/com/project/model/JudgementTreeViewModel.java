package com.project.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by chuni on 5/18/2016.
 */
public class JudgementTreeViewModel {

    private SimpleStringProperty judgement;
    private SimpleStringProperty judgementId;

    public JudgementTreeViewModel(String judgement, String judgementId) {
        this.judgement = new SimpleStringProperty(judgement);
        this.judgementId = new SimpleStringProperty(judgementId);
    }

    public String getJudgement() {
        return judgement.get();
    }

    public SimpleStringProperty judgementProperty() {
        return judgement;
    }

    public void setJudgement(String judgement) {
        this.judgement.set(judgement);
    }

    public String getJudgementId() {
        return judgementId.get();
    }

    public SimpleStringProperty judgementIdProperty() {
        return judgementId;
    }

    public void setJudgementId(String judgementId) {
        this.judgementId.set(judgementId);
    }
}
