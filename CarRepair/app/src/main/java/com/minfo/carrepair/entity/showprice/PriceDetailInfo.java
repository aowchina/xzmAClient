package com.minfo.carrepair.entity.showprice;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 17/6/24.
 */
public class PriceDetailInfo implements Serializable {
    private PriceDetailEntity info;

    public PriceDetailEntity getInfo() {
        return info;
    }

    public void setInfo(PriceDetailEntity info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "PriceDetailInfo{" +
                "info=" + info +
                '}';
    }
}
