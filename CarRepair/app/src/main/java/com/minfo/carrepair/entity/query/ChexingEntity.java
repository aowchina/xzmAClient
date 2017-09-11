package com.minfo.carrepair.entity.query;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MinFo021 on 17/6/8.
 */

public class ChexingEntity implements Serializable {
//    {
//        "issuedate": "1234",
//            "info": [
//        {
//            "carid": "3",
//                "cname": "奔驰A67",
//                "cimage": "images/car/1.jpg",
//                "price": "12434"
//        },
//        {
//            "carid": "4",
//                "cname": "cesi",
//                "cimage": "images/car/1.png",
//                "price": "56"
//        }
//        ]
//    }

    @SerializedName("issuedate")
    private String name;
    private String id;
    @SerializedName("info")
    private List<ChexingItem> list;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ChexingItem> getList() {
        return list;
    }

    public void setList(List<ChexingItem> list) {
        this.list = list;
    }
}
