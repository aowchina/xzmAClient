package com.minfo.carrepairseller.entity.purchase;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 17/6/13.
 */
public class PurchaseDetail implements Serializable {
    private PurchaseItem info;

    public PurchaseItem getInfo() {
        return info;
    }

    public void setInfo(PurchaseItem info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "PurchaseDetail{" +
                "info=" + info +
                '}';
    }
}
