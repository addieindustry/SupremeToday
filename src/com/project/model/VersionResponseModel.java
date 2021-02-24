package com.project.model;

public class VersionResponseModel {
    public final long code;
    public final Data data[];

    public VersionResponseModel(long code, Data[] data) {
        this.code = code;
        this.data = data;
    }

    public class Data {

        public final String version;
        public final String[] courts;

        public Data(String version, String[] courts) {
            this.version = version;
            this.courts = courts;
        }
    }
}
