/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.model;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author addie
 */
public class JSONBookmarkModel {
    @SerializedName("category")
    private String category;

    @SerializedName("title")
        private String title;

    @SerializedName("_id")
        private String _id;

    @SerializedName("doc_id")
        private String doc_id;

    @SerializedName("doc_type")
        private String doc_type;

    @SerializedName("note")
        private String note;

    
        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getDoc_id() {
            return doc_id;
        }

        public void setDoc_id(String doc_id) {
            this.doc_id = doc_id;
        }

        public String getDoc_type() {
            return doc_type;
        }

        public void setDoc_type(String doc_type) {
            this.doc_type = doc_type;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        @Override
        public String toString() {
            return "ClassPojo [category = " + category + ", title = " + title + ", _id = " + _id + ", doc_id = " + doc_id + ", doc_type = " + doc_type + ", note = " + note + "]";
        }
}
