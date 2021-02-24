package com.project.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class ResultViewTableDataModel {

//    private SimpleStringProperty courtId;
//    private SimpleStringProperty courtName;
//    private SimpleStringProperty state;
//    private SimpleBooleanProperty checked;
//
//    public ResultViewTableDataModel(String courtId, String courtName, String state) {
//        this.courtId = new SimpleStringProperty(courtId);
//        this.courtName = new SimpleStringProperty(courtName);
//        this.state = new SimpleStringProperty(state);
//    }
//
//    public String getCourtId() {
//        return courtId.get();
//    }
//
//    public String getCourtName() {
//        return courtName.get();
//    }
//
//    public String getState() {
//        return state.get();
//    }

    private SimpleBooleanProperty checked;
    private SimpleStringProperty overRule;
    private SimpleStringProperty partyName;
    private SimpleStringProperty courtName;
    private SimpleStringProperty judgement;
    private SimpleStringProperty citation;
    private SimpleStringProperty bench;
    private SimpleStringProperty hot;

    public ResultViewTableDataModel(String overRule, String partyName, String courtName, String judgement, String citation, String bench, String hot) {
//        this.checked = new SimpleStringProperty(checked);
        this.overRule = new SimpleStringProperty(overRule);
        this.partyName = new SimpleStringProperty(partyName);
        this.courtName = new SimpleStringProperty(courtName);
        this.judgement = new SimpleStringProperty(judgement);
        this.citation = new SimpleStringProperty(citation);
        this.bench = new SimpleStringProperty(bench);
        this.hot = new SimpleStringProperty(hot);
    }

    public boolean getChecked() {
        return checked.get();
    }

    public SimpleBooleanProperty checkedProperty() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked.set(checked);
    }

    public String getOverRule() {
        return overRule.get();
    }

    public SimpleStringProperty overRuleProperty() {
        return overRule;
    }

    public void setOverRule(String overRule) {
        this.overRule.set(overRule);
    }

    public String getPartyName() {
        return partyName.get();
    }

    public SimpleStringProperty partyNameProperty() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName.set(partyName);
    }

    public String getCourtName() {
        return courtName.get();
    }

    public SimpleStringProperty courtNameProperty() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName.set(courtName);
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

    public String getCitation() {
        return citation.get();
    }

    public SimpleStringProperty citationProperty() {
        return citation;
    }

    public void setCitation(String citation) {
        this.citation.set(citation);
    }

    public String getBench() {
        return bench.get();
    }

    public SimpleStringProperty benchProperty() {
        return bench;
    }

    public void setBench(String bench) {
        this.bench.set(bench);
    }

    public String getHot() {
        return hot.get();
    }

    public SimpleStringProperty hotProperty() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot.set(hot);
    }
}
