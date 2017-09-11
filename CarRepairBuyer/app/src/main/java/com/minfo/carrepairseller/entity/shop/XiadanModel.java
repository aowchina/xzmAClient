package com.minfo.carrepairseller.entity.shop;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/6/19.
 */

public class XiadanModel implements Serializable {
//    {
//        "orderid": "zj149786885210057888728",
//            "addtime": 1497868852
//    }
    private String orderid;
    private String addtime;

    public String getOrderid() {
        return orderid;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
