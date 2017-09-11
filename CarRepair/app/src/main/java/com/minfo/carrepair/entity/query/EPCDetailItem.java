package com.minfo.carrepair.entity.query;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/6/9.
 */

public class EPCDetailItem implements Serializable {
//    {
//        "position": "zuo ",
//            "name": "eerytry",
//            "oem": "23456"
//    }

    private String id;
    private String position;
    private String name;
    private String oem;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public String getOem() {
        return oem;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOem(String oem) {
        this.oem = oem;
    }
}
