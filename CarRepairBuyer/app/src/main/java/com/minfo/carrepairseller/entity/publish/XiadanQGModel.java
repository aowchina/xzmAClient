package com.minfo.carrepairseller.entity.publish;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/6/26.
 */

public class XiadanQGModel implements Serializable {
//    qgorderid：订单号
//    qg_addtime：下单时间
    @SerializedName("qgorderid")
    private String orderid;
    @SerializedName("qg_addtime")
    private String time;

    public String getOrderid() {
        return orderid;
    }

    public String getTime() {
        return time;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
