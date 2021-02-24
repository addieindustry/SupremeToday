/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.helper;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author addie
 */
public class ResponseMaster{
    
    @SerializedName("data")
    private Object data;
    
    @SerializedName("code")
    private int code;

    public ResponseMaster() {
    }

    public ResponseMaster(Object data, int code) {
        this.data = data;
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    
}
