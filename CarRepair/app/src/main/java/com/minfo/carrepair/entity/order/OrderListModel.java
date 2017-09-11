package com.minfo.carrepair.entity.order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MinFo021 on 17/6/26.
 */

public class OrderListModel implements Serializable {
    private List<OrderListEntity> shop;
    private List<OrderListEntity> qiugou;

    public List<OrderListEntity> getShop() {
        return shop;
    }

    public List<OrderListEntity> getQiugou() {
        return qiugou;
    }

    public void setShop(List<OrderListEntity> shop) {
        this.shop = shop;
    }

    public void setQiugou(List<OrderListEntity> qiugou) {
        this.qiugou = qiugou;
    }
}
