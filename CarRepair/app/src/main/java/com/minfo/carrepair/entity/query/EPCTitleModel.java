package com.minfo.carrepair.entity.query;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MinFo021 on 17/6/13.
 * EPC列表左边栏和品牌车系数据model
 */

public class EPCTitleModel implements Serializable {
//    {
//        "list": [
//        {
//            "typeid": "1",
//                "tname": "底盘"
//        },
//        {
//            "typeid": "3",
//                "tname": "保险杠"
//        }
//        ],
//        "object": {
//        "bname": "奔驰",
//                "sname": "奔驰A",
//                "cname": "奔驰A67",
//                "cimage": "images/car/1.jpg"
//        }
//    }
    private List<EPCTitleItem> list;
    private EPCTop object;

    public List<EPCTitleItem> getList() {
        return list;
    }

    public EPCTop getObject() {
        return object;
    }

    public void setList(List<EPCTitleItem> list) {
        this.list = list;
    }

    public void setObject(EPCTop object) {
        this.object = object;
    }
}
