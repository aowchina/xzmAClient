package com.minfo.carrepairseller.entity.query;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/6/9.
 */

public class EPCItem implements Serializable {
//    {
//        "epcname": "启动电机",
//        "epcimg": "images/car/1.png"
//    },
    @SerializedName("epcid")
    private String id;
    @SerializedName("epcname")
    private String name;
    @SerializedName("epcimg")
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
