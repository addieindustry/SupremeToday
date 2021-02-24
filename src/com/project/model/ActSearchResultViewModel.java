package com.project.model;

public class ActSearchResultViewModel {
    private String html;
    private String actTitle;
    private String actNo;

    public ActSearchResultViewModel(String html, String actTitle, String actNo) {
        this.html = html;
        this.actTitle = actTitle;
        this.actNo = actNo;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getActTitle() {
        return actTitle;
    }

    public void setActTitle(String actTitle) {
        this.actTitle = actTitle;
    }

    public String getActNo() {
        return actNo;
    }

    public void setActNo(String actNo) {
        this.actNo = actNo;
    }
}
