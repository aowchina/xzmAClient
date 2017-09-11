package com.minfo.carrepair.entity.query;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MinFo021 on 17/6/8.
 */

public class ChexingModel implements Serializable {
//    "sname": "奔驰A",
//    "carinfo": []
    @SerializedName("sname")
    private String name;
    @SerializedName("carinfo")
    private List<ChexingEntity> list;

    public String getName() {
        return name;
    }

    public List<ChexingEntity> getList() {
        return list;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setList(List<ChexingEntity> list) {
        this.list = list;
    }
}
