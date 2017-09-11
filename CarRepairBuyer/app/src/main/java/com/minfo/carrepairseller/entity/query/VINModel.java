package com.minfo.carrepairseller.entity.query;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/6/9.
 */

public class VINModel implements Serializable {
//    {
//        "showapi_res_code": 0,
//            "showapi_res_error": "",
//            "showapi_res_body": {}
//    }

    /**品牌名称,车型名称,销售名称,车辆类型,17位的车架号VIN,发动机型号,功率/转速(Kw/R),发动机喷射类型,燃油类型,变速器类型,
   发动机缸数,气缸形式,排量(L),生产年份,安全气囊,座位数,车辆级别,车门数,车身形式,厂家名称,档位数,装备质量(KG),组装厂*/
    @SerializedName("showapi_res_code")
    private int code;
    @SerializedName("showapi_res_error")
    private String error;
    @SerializedName("showapi_res_body")
    private VINEntity entity;

    public int getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

    public VINEntity getEntity() {
        return entity;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setEntity(VINEntity entity) {
        this.entity = entity;
    }
}
