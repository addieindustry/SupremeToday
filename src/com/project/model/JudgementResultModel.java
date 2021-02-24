package com.project.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class JudgementResultModel {

    private SimpleStringProperty SrNo;
    private SimpleStringProperty isoverruled;
    private SimpleStringProperty topic;
    private SimpleStringProperty judgementid;
    private SimpleStringProperty html;
    private SimpleBooleanProperty checked = new SimpleBooleanProperty(false);

    public JudgementResultModel(String SrNo, String isoverruled, String topic, String judgementid, String html) {
        this.isoverruled = new SimpleStringProperty(isoverruled);
        this.SrNo = new SimpleStringProperty(SrNo);
        this.topic = new SimpleStringProperty(topic);
        this.judgementid = new SimpleStringProperty(judgementid);
        this.html = new SimpleStringProperty(html);
    }

    public String getHtml() {
        return html.get();
    }

    public SimpleStringProperty htmlProperty() {
        return html;
    }

    public void setHtml(String html) {
        this.html.set(html);
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

    public String getJudgementid() {
        return judgementid.get();
    }

    public SimpleStringProperty judgementidProperty() {
        return judgementid;
    }

    public void setJudgementid(String judgementid) {
        this.judgementid.set(judgementid);
    }

    public String getIsoverruled() {
        return isoverruled.get();
    }

    public SimpleStringProperty isoverruledProperty() {
        return isoverruled;
    }

    public void setIsoverruled(String isoverruled) {
        this.isoverruled.set(isoverruled);
    }

    public String getSrNo() {return SrNo.get();}

    public SimpleStringProperty getSrNoProperty() {
        return SrNo;
    }

    public void setSrNo(String SrNo) {this.SrNo.set(SrNo);}

    public String getTopic() {return topic.get();}

    public SimpleStringProperty topicProperty() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic.set(topic);
    }

}
