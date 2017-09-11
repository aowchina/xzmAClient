package com.minfo.carrepairseller.entity.showprice;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 17/6/15.
 */
public class ShowPriceDetailInfo implements Serializable {
    private ShowPriceDetail info;

    public ShowPriceDetail getInfo() {
        return info;
    }

    public void setInfo(ShowPriceDetail info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "ShowPriceDetailInfo{" +
                "info=" + info +
                '}';
    }
}
