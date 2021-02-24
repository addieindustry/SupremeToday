///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.project.utility;
//
//import java.util.List;
//
///**
// *
// * @author Vishal
// */
//public class PropertyMaster {
//
//    private List<TableMaster> schema;
//
//    public List<TableMaster> getSchema() {
//        return schema;
//    }
//
//    public void setSchema(List<TableMaster> schema) {
//        this.schema = schema;
//    }
//
//    public class TableMaster {
//
//        private String where;
//
//        private String tableName;
//        private List<ColumnMaster> cols;
//
//        public String getWhere() {
//            return where;
//        }
//
//        public void setWhere(String where) {
//            this.where = where;
//        }
//
//        public String getTableName() {
//            return tableName;
//        }
//
//        public void setTableName(String tableName) {
//            this.tableName = tableName;
//        }
//
//        public List<ColumnMaster> getCols() {
//            return cols;
//        }
//
//        public void setCols(List<ColumnMaster> cols) {
//            this.cols = cols;
//        }
//
//    }
//
//    public class ColumnMaster {
//
//        private String title;
//        private boolean isIndex;
//        private boolean isStore;
//        private boolean isFacet;
//        private String fieldType;
//        private boolean isSort;
//        private boolean isEncrypt;
//        private boolean hl;
//
//
//        public boolean isIsEncrypt() {
//            return isEncrypt;
//        }
//
//        public void setIsEncrypt(boolean isEncrypt) {
//            this.isEncrypt = isEncrypt;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public boolean isIsIndex() {
//            return isIndex;
//        }
//
//        public void setIsIndex(boolean isIndex) {
//            this.isIndex = isIndex;
//        }
//
//        public boolean isIsStore() {
//            return isStore;
//        }
//
//        public void setIsStore(boolean isStore) {
//            this.isStore = isStore;
//        }
//
//        public boolean isIsFacet() {
//            return isFacet;
//        }
//
//        public void setIsFacet(boolean isFacet) {
//            this.isFacet = isFacet;
//        }
//
//        public String getFieldType() {
//            return fieldType;
//        }
//
//        public void setFieldType(String fieldType) {
//            this.fieldType = fieldType;
//        }
//
//        public boolean isIsSort() {
//            return isSort;
//        }
//
//        public void setIsSort(boolean isSort) {
//            this.isSort = isSort;
//        }
//
//        public boolean isHl() {
//            return hl;
//        }
//
//        public void setHl(boolean hl) {
//            this.hl = hl;
//        }
//
//    }
//}
