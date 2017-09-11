package com.minfo.carrepairseller.entity.query;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MinFo021 on 17/6/8.
 */

public class ChexiEntity implements Serializable {
//    "bname": "奔驰",
//    "serialinfo": []
    @SerializedName("bname")
    private String name;
    @SerializedName("serialinfo")
    private List<ChexiItem> list;

    public String getName() {
        return name;
    }

    public List<ChexiItem> getList() {
        return list;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setList(List<ChexiItem> list) {
        this.list = list;
    }
}
