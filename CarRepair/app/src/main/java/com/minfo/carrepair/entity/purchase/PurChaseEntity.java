package com.minfo.carrepair.entity.purchase;

import java.io.Serializable;
import java.util.List;

/**
 * Created by min-fo-012 on 17/6/13.
 */
public class PurChaseEntity implements Serializable {
    private List<PurchaseList>wantBuy;

    public List<PurchaseList> getWantBuy() {
        return wantBuy;
    }

    public void setWantBuy(List<PurchaseList> wantBuy) {
        this.wantBuy = wantBuy;
    }

    @Override
    public String toString() {
        return "PurChaseEntity{" +
                "wantBuy=" + wantBuy +
                '}';
    }
}
