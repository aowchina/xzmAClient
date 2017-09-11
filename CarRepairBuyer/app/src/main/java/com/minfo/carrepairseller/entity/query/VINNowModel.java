package com.minfo.carrepairseller.entity.query;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/6/29.
 */

public class VINNowModel implements Serializable {
    private VINModel che;
    @SerializedName("cimage")
    private String img;
    private int biaoshi; //1有，0没有

    public VINModel getChe() {
        return che;
    }

    public String getImg() {
        return img;
    }

    public int getBiaoshi() {
        return biaoshi;
    }

    public void setChe(VINModel che) {
        this.che = che;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setBiaoshi(int biaoshi) {
        this.biaoshi = biaoshi;
    }
}
