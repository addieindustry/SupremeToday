package com.project.extrautility;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * @author Vishal
 */
public class PropertyMaster {

    @SerializedName("schema")
    private List<com.project.extrautility.PropertyMaster.TableMaster> schema;

    public List<com.project.extrautility.PropertyMaster.TableMaster> getSchema() {
        return schema;
    }

    public void setSchema(List<com.project.extrautility.PropertyMaster.TableMaster> schema) {
        this.schema = schema;
    }

    public class TableMaster {

        @SerializedName("where")
        private String where;

        @SerializedName("tableName")
        private String tableName;

        @SerializedName("cols")
        private List<com.project.extrautility.PropertyMaster.ColumnMaster> cols;

        public String getWhere() {
            return where;
        }

        public void setWhere(String where) {
            this.where = where;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public List<com.project.extrautility.PropertyMaster.ColumnMaster> getCols() {
            return cols;
        }

        public void setCols(List<com.project.extrautility.PropertyMaster.ColumnMaster> cols) {
            this.cols = cols;
        }

    }

    public class ColumnMaster {

        @SerializedName("title")
        private String title;
        @SerializedName("isIndex")
        private boolean isIndex;
        @SerializedName("isStore")
        private boolean isStore;
        @SerializedName("isFacet")
        private boolean isFacet;
        @SerializedName("fieldType")
        private String fieldType;
        @SerializedName("isSort")
        private boolean isSort;
        @SerializedName("isEncrypt")
        private boolean isEncrypt;
        @SerializedName("hl")
        private boolean hl;


        public boolean isIsEncrypt() {
            return isEncrypt;
        }

        public void setIsEncrypt(boolean isEncrypt) {
            this.isEncrypt = isEncrypt;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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
}
