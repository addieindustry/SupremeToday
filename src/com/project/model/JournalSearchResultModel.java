package com.project.model;

public class JournalSearchResultModel {
    private String html;
    private String title;
    private String id;

    public JournalSearchResultModel(String html, String title, String id) {
        this.html = html;
        this.title = title;
        this.id = id;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
