package com.minfo.carrepairseller.entity.query;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MinFo021 on 17/6/8.
 */

public class PinpaiModel implements Serializable {
//    "brandlist": []

    @SerializedName("brandlist")
    private List<PinpaiEntity> list;

    public List<PinpaiEntity> getList() {
        return list;
    }

    public void setList(List<PinpaiEntity> list) {
        this.list = list;
    }
}
