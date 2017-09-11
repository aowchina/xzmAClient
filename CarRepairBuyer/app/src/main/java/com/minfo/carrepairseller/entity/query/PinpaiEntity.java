package com.minfo.carrepairseller.entity.query;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MinFo021 on 17/6/8.
 */

public class PinpaiEntity implements Serializable {
    //    {
//        "fname": "A",
//            "brandid": "13",
//            "info": [
//        {
//            "blogo": "http://192.168.1.123/zjxzm/images/carbrand/4.jpg",
//                "bname": "奔驰",
//                "brandid": "13"
//        }
//        ]
//    },
    @SerializedName("brandid")
    private String id;
    @SerializedName("fname")
    private String name;
    @SerializedName("info")
    private List<PinpaiItem> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public List<PinpaiItem> getList() {
        return list;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setList(List<PinpaiItem> list) {
        this.list = list;
    }
}
