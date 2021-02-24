package com.project.model;


import javafx.beans.property.SimpleStringProperty;

public class OverruledModel {

    private SimpleStringProperty Id;
    private SimpleStringProperty courtId;
    private SimpleStringProperty white;
    private SimpleStringProperty red;
    private SimpleStringProperty whiteText;
    private SimpleStringProperty redText;
    private SimpleStringProperty overruled;

//bookmarkid, sessionid, searchtype, filetype, parentid, childid, title, remark, createdon, isactive
    public OverruledModel(String Id, String courtId, String white, String red, String whiteText, String redText, String overruled) {
        this.Id = new SimpleStringProperty(Id);
        this.courtId = new SimpleStringProperty(courtId);
        this.white = new SimpleStringProperty(white);
        this.red = new SimpleStringProperty(red);
        this.whiteText = new SimpleStringProperty(whiteText);
        this.redText = new SimpleStringProperty(redText);
        this.overruled = new SimpleStringProperty(overruled);
    }

//    public BookmarkModel( String note, String title, String createDon, String isActive) {
//        this.note = new SimpleStringProperty(note);
//        this.title = new SimpleStringProperty(title);
//        this.createDon = new SimpleStringProperty(createDon);
//        this.isActive = new SimpleStringProperty(isActive);
//    }
//
//    /*Insert*/
//    public BookmarkModel(String note, String title) {
//        this.note = new SimpleStringProperty(note);
//        this.title = new SimpleStringProperty(title);
//    }

    public String getId() {
        return Id.get();
    }

    public SimpleStringProperty idProperty() {
        return Id;
    }

    public void setId(String id) {
        this.Id.set(id);
    }

    public String getCourtId() {
        return courtId.get();
    }

    public SimpleStringProperty courtIdProperty() {
        return courtId;
    }

    public void setCourtId(String courtId) {
        this.courtId.set(courtId);
    }

    public String getWhite() {
        return white.get();
    }

    public SimpleStringProperty whiteProperty() {
        return white;
    }

    public void setWhite(String white) {
        this.white.set(white);
    }

    public String getRed() {
        return red.get();
    }

    public SimpleStringProperty redProperty() {
        return red;
    }

    public void setRed(String red) {this.red.set(red);}

    public String getWhiteText() {
        return whiteText.get();
    }

    public SimpleStringProperty whiteTextProperty() {
        return whiteText;
    }

    public void setWhiteText(String whiteText) {
        this.whiteText.set(whiteText);
    }

    public String getRedText() {
        return redText.get();
    }

    public SimpleStringProperty redTextProperty() {
        return redText;
    }

    public void setRedText(String redText) {
        this.redText.set(redText);
    }

    public String getOverruled() {
        return overruled.get();
    }

    public SimpleStringProperty overruledProperty() {
        return overruled;
    }

    public void setOverruled(String overruled) {
        this.overruled.set(overruled);
    }

}
