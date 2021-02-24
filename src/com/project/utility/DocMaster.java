/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.utility;

/**
 *
 * @author Vishal
 */
public class DocMaster {

    private String key;
    private String data;
    private boolean isIndex;
    private boolean isStore;
    private boolean isFacet;
    private String fieldType;
    private boolean isSort;
    private boolean isEncrypt;
    private boolean hl;

    public DocMaster(String key, String data, boolean isIndex, boolean isStore, boolean isFacet, String fieldType, boolean isSort, boolean isEncrypt, boolean hl) {
        this.key = key;
        this.data = data;
        this.isIndex = isIndex;
        this.isStore = isStore;
        this.isFacet = isFacet;
        this.fieldType = fieldType;
        this.isSort = isSort;
        this.isEncrypt = isEncrypt;
        this.hl = hl;
    }

    public boolean isIsEncrypt() {
        return isEncrypt;
    }

    public void setIsEncrypt(boolean isEncrypt) {
        this.isEncrypt = isEncrypt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isIsIndex() {
        return isIndex;
    }

    public void setIsIndex(boolean isIndex) {
        this.isIndex = isIndex;
    }

    public boolean isIsStore() {
        return isStore;
    }

    public void setIsStore(boolean isStore) {
        this.isStore = isStore;
    }

    public boolean isIsFacet() {
        return isFacet;
    }

    public void setIsFacet(boolean isFacet) {
        this.isFacet = isFacet;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public boolean isIsSort() {
        return isSort;
    }

    public void setIsSort(boolean isSort) {
        this.isSort = isSort;
    }

    public boolean isHl() {
        return hl;
    }

    public void setHl(boolean hl) {
        this.hl = hl;
    }

}
