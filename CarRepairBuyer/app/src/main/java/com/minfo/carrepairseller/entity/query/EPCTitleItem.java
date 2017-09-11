package com.minfo.carrepairseller.entity.query;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by fei on 15/9/29.
 */
public class EPCTitleItem implements Serializable{
//    "typeid": "1",
//    "tname": "底盘"
    @SerializedName("typeid")
    private String id;
    @SerializedName("tname")
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
