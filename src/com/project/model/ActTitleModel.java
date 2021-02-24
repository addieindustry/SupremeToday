package com.project.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class ActTitleModel {

/*    private SimpleStringProperty actTitle;
    private SimpleBooleanProperty checked = new SimpleBooleanProperty(false);

    public ActTitleModel(String actTitle) {
        this.actTitle = new SimpleStringProperty(actTitle);
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

    public String getactTitle() {
        return actTitle.get();
    }*/


    private SimpleStringProperty actTitle;
    private SimpleBooleanProperty checked = new SimpleBooleanProperty(false);

    public ActTitleModel(String actTitle) {
        this.actTitle = new SimpleStringProperty(actTitle);
    }


    public SimpleBooleanProperty checkedProperty(){
        return  this.checked;

    }

    public Boolean getChecked() {
        return this.checkedProperty().get();
    }

    public void setChecked(final Boolean checked) {
        this.checkedProperty().set(checked);
    }

    //    public SimpleBooleanProperty checkedProperty() {
//        return checked;
//    }

 /*  public String getCourtId() {
        return courtId.get();
    }*/

//    public void setCourtId(String courtId) {
//        this.courtId = courtId;
//    }

//    public String getCourtName() {
//        return actTitle.get();
//    }
//
//    public void setCourtName(String courtName) {
//        this.courtName = courtName;
//    }

//    public String getState() {
//        return state.get();
//    }

//    public void setState(String state) {
//        this.state = state;
//    }


    public String getActTitle() {
        return actTitle.get();
    }

    public SimpleStringProperty actTitleProperty() {
        return actTitle;
    }

    public void setActTitle(String actTitle) {
        this.actTitle.set(actTitle);
    }
}
