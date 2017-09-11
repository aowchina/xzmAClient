package com.minfo.carrepairseller.entity.query;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/6/8.
 */

public class ChexiItem implements Serializable {
//    {
//        "serialid": "12",
//            "sname": "奔驰A",
//            "simage": "http://192.168.1.123/zjxzm/images/carserial/2.jpg"
//    }
    @SerializedName("serialid")
    private String id;
    @SerializedName("sname")
    private String name;
    @SerializedName("simage")
    private String icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
