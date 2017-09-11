package com.minfo.carrepair.entity.query;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/6/8.
 */

public class PinpaiItem implements Serializable {
    //    {
//            "blogo": "http://192.168.1.123/zjxzm/images/carbrand/4.jpg",
//                "bname": "奔驰",
//                "brandid": "13"
//        }
    @SerializedName("brandid")
    private String id;
    @SerializedName("bname")
    private String name;
    @SerializedName("blogo")
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
