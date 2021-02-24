package com.project.model;


import javafx.beans.property.SimpleStringProperty;

public class BookmarkModel {

//    bookmarkid, sessionid, searchtype, filetype, parentid, childid, title, remark, createdon

    private SimpleStringProperty Id;
    private SimpleStringProperty DocId;
    private SimpleStringProperty title;
    private SimpleStringProperty doc_type;
    private SimpleStringProperty category;
    private SimpleStringProperty note;
    private SimpleStringProperty created_date;

//bookmarkid, sessionid, searchtype, filetype, parentid, childid, title, remark, createdon, isactive
    public BookmarkModel(String Id, String DocId, String title, String doc_type, String category, String note) {
        this.Id = new SimpleStringProperty(Id);
        this.DocId = new SimpleStringProperty(DocId);
        this.title = new SimpleStringProperty(title);
        this.doc_type = new SimpleStringProperty(doc_type);
        this.category = new SimpleStringProperty(category);
        this.note = new SimpleStringProperty(note);
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

    public String getDocId() {
        return DocId.get();
    }

    public SimpleStringProperty docidProperty() {
        return DocId;
    }

    public void setDocId(String DocId) {
        this.DocId.set(DocId);
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

    public String getDoc_Type() {
        return doc_type.get();
    }

    public SimpleStringProperty doc_typeProperty() {
        return doc_type;
    }

    public void setDoc_Type(String doc_type) {
        this.doc_type.set(doc_type);
    }

    public String getCategory() {
        return category.get();
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public String getNote() {
        return note.get();
    }

    public SimpleStringProperty noteProperty() {
        return note;
    }

    public void setNote(String note) {
        this.note.set(note);
    }


    public String getCreated_DateDon() {
        return created_date.get();
    }

    public SimpleStringProperty Created_DateProperty() {
        return created_date;
    }

    public void setCreated_Date(String created_date) {
        this.created_date.set(created_date);
    }

}
