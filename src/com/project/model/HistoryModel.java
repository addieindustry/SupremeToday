package com.project.model;


import javafx.beans.property.SimpleStringProperty;

public class HistoryModel {

    private SimpleStringProperty Id;
    private SimpleStringProperty title;
    private SimpleStringProperty query;
    private SimpleStringProperty keyword;
    private SimpleStringProperty search_type;
    private SimpleStringProperty created_date;

//    public HistoryModel(String searchType, String parentId, String title) {
//        this.searchType = new SimpleStringProperty(searchType);
//        this.title = new SimpleStringProperty(title);
//    }

    public HistoryModel(String Id, String title, String query, String keyword, String search_type) {
        this.Id = new SimpleStringProperty(Id);
        this.title = new SimpleStringProperty(title);
        this.query = new SimpleStringProperty(query);
        this.keyword = new SimpleStringProperty(keyword);
        this.search_type = new SimpleStringProperty(search_type);
//        this.created_date = new SimpleStringProperty(created_date);
    }

    public HistoryModel(String Id, String title, String query, String keyword, String search_type, String created_date) {
        this.Id = new SimpleStringProperty(Id);
        this.title = new SimpleStringProperty(title);
        this.query = new SimpleStringProperty(query);
        this.keyword = new SimpleStringProperty(keyword);
        this.search_type = new SimpleStringProperty(search_type);
        this.created_date = new SimpleStringProperty(created_date);
    }

//    public HistoryModel(String historyType, String searchType, String fileType, String parentId, String childId, String title) {
//        this.historyType = new SimpleStringProperty(historyType);
//        this.searchType = new SimpleStringProperty(searchType);
//        this.title = new SimpleStringProperty(title);
//    }

    public String getId() {
        return Id.get();
    }

    public SimpleStringProperty IdProperty() {
        return Id;
    }

    public void setId(String Id) {
        this.Id.set(Id);
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

    public String getQuery() {
        return query.get();
    }

    public SimpleStringProperty queryProperty() {
        return query;
    }

    public void setQuery(String query) {
        this.query.set(query);
    }

    public String getKeyword() {
        return keyword.get();
    }

    public SimpleStringProperty keywordProperty() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword.set(keyword);
    }

    public String getsearch_type() {
        return search_type.get();
    }

    public SimpleStringProperty search_typeProperty() {
        return search_type;
    }

    public void setsearch_type(String search_type) {
        this.search_type.set(search_type);
    }

    public String getcreated_date() {
        return created_date.get();
    }

    public SimpleStringProperty created_dateProperty() {
        return created_date;
    }

    public void setcreated_date(String created_date) {
        this.created_date.set(created_date);
    }
}
