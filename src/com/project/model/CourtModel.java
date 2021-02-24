package com.project.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class CourtModel {

    private SimpleStringProperty courtId;
    private SimpleStringProperty courtName;
//    private SimpleStringProperty state;
//    private SimpleBooleanProperty checked = new SimpleBooleanProperty(true);

    public CourtModel(String courtId, String courtName) {
        this.courtId = new SimpleStringProperty(courtId);
        this.courtName = new SimpleStringProperty(courtName);
    }

//    public SimpleBooleanProperty checkedProperty(){
//        return  this.checked;
//    }
//
//    public Boolean getChecked() {
//        return this.checkedProperty().get();
//    }
//
//    public void setChecked(final Boolean checked) {
//        this.checkedProperty().set(checked);
//    }

    //    public SimpleBooleanProperty checkedProperty() {
//        return checked;
//    }

    public String getCourtId() {
        return courtId.get();
    }

//    public void setCourtId(String courtId) {
//        this.courtId = courtId;
//    }

    public String getCourtName() {
        return courtName.get();
    }

//    public void setCourtName(String courtName) {
//        this.courtName = courtName;
//    }

//    public String getState() {
//        return state.get();
//    }

//    public void setState(String state) {
//        this.state = state;
//    }
}
