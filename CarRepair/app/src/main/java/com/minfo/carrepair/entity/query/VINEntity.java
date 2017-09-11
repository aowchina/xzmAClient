package com.minfo.carrepair.entity.query;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 17/5/25.
 */
public class VINEntity implements Serializable {
    //    {
//        "showapi_res_code": 0,
//            "showapi_res_error": "",
//            "showapi_res_body": {
//        "assembly_factory": "",
//                "sale_name": "1.6 手自一体 时尚版", // 销售名称
//                "engine_type": "BWH", //
//                "brand_name": "大众", //
//                "model_name": "宝来", //
//                "car_type": "轿车", //
//                "ret_code": 0, //
//                "vin": "lfv2a2150a3043256", //
//                "power": "74", //
//                "jet_type": "", //
//                "fuel_Type": "汽油", //
//                "transmission_type": "手自一体变速器(AMT)", //
//                "cylinder_number": "4", //
//                "drive_style": "前轮驱动", //
//                "made_year": "2010", // 生产年份
//                "output_volume": "1.6", // 排量(L)
//                "air_bag": "", // 安全气囊
//                "cylinder_form": "", // 气缸形式
//                "seat_num": "5", // 座位数
//                "vehicle_level": "紧凑型车", // 车辆级别
//                "door_num": "四门", // 车门数
//                "car_body": "三厢", // 车身形式
//                "manufacturer": "一汽大众", // 厂家名称
//                "gears_num": "6", // 档位数
//                "car_weight": "1335" // 装备质量(KG)
//        }
//    }
    /**"品牌名称","车型名称","销售名称","车辆类型","17位的车架号VIN","发动机型号","功率/转速(Kw/R)","发动机喷射类型","燃油类型","变速器类型","
     发动机缸数","气缸形式","排量(L)","生产年份","安全气囊","座位数","车辆级别","车门数","车身形式","厂家名称","档位数","装备质量(KG)","组装厂"*/
    @SerializedName("remark")
    private String errormsg;
    @SerializedName("brand_name")
    private String pinpai; // 品牌名称
    @SerializedName("model_name")
    private String chenxing; // 车型名称
    @SerializedName("sale_name")
    private String xsname; // 销售名称
    @SerializedName("car_type")
    private String cheType; // 车辆类型
    private String vin; // 17位的车架号VIN
    @SerializedName("engine_type")
    private String fdjType; // 发动机型号
    @SerializedName("power")
    private String gonglv; // 功率/转速(Kw/R)
    @SerializedName("jet_type")
    private String fdjpsType; // 发动机喷射类型
    @SerializedName("fuel_Type")
    private String ranyou; // 燃油类型
    @SerializedName("transmission_type")
    private String biansu; // 变速器类型
    @SerializedName("cylinder_number")
    private String fdjgangshu; // 发动机缸数
    @SerializedName("cylinder_form")
    private String qigangType; // 气缸形式
    @SerializedName("output_volume")
    private String pailiang; // 排量(L)
    @SerializedName("made_year")
    private String shengchanYear; // 生产年份
    @SerializedName("air_bag")
    private String anquanBag; // 安全气囊
    @SerializedName("seat_num")
    private String zuoweishu; // 座位数
    @SerializedName("vehicle_level")
    private String cheliangLevel; // 车辆级别
    @SerializedName("door_num")
    private String chemenNum; // 车门数
    @SerializedName("car_body")
    private String cheshenType; // 车身形式
    @SerializedName("manufacturer")
    private String changjiaName; // 厂家名称
    @SerializedName("gears_num")
    private String dangweiNum; // 档位数
    @SerializedName("car_weight")
    private String zhuangbeiWeight; // 装备质量(KG)
    @SerializedName("assembly_factory")
    private String zuzhuangFactory; // 组装厂

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public String getPinpai() {
        return pinpai;
    }

    public String getChenxing() {
        return chenxing;
    }

    public String getXsname() {
        return xsname;
    }

    public String getCheType() {
        return cheType;
    }

    public String getVin() {
        return vin;
    }

    public String getFdjType() {
        return fdjType;
    }

    public String getGonglv() {
        return gonglv;
    }

    public String getFdjpsType() {
        return fdjpsType;
    }

    public String getRanyou() {
        return ranyou;
    }

    public String getBiansu() {
        return biansu;
    }

    public String getFdjgangshu() {
        return fdjgangshu;
    }

    public String getQigangType() {
        return qigangType;
    }

    public String getPailiang() {
        return pailiang;
    }

    public String getShengchanYear() {
        return shengchanYear;
    }

    public String getAnquanBag() {
        return anquanBag;
    }

    public String getZuoweishu() {
        return zuoweishu;
    }

    public String getCheliangLevel() {
        return cheliangLevel;
    }

    public String getChemenNum() {
        return chemenNum;
    }

    public String getCheshenType() {
        return cheshenType;
    }

    public String getChangjiaName() {
        return changjiaName;
    }

    public String getDangweiNum() {
        return dangweiNum;
    }

    public String getZhuangbeiWeight() {
        return zhuangbeiWeight;
    }

    public String getZuzhuangFactory() {
        return zuzhuangFactory;
    }

    public void setPinpai(String pinpai) {
        this.pinpai = pinpai;
    }

    public void setChenxing(String chenxing) {
        this.chenxing = chenxing;
    }

    public void setXsname(String xsname) {
        this.xsname = xsname;
    }

    public void setCheType(String cheType) {
        this.cheType = cheType;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public void setFdjType(String fdjType) {
        this.fdjType = fdjType;
    }

    public void setGonglv(String gonglv) {
        this.gonglv = gonglv;
    }

    public void setFdjpsType(String fdjpsType) {
        this.fdjpsType = fdjpsType;
    }

    public void setRanyou(String ranyou) {
        this.ranyou = ranyou;
    }

    public void setBiansu(String biansu) {
        this.biansu = biansu;
    }

    public void setFdjgangshu(String fdjgangshu) {
        this.fdjgangshu = fdjgangshu;
    }

    public void setQigangType(String qigangType) {
        this.qigangType = qigangType;
    }

    public void setPailiang(String pailiang) {
        this.pailiang = pailiang;
    }

    public void setShengchanYear(String shengchanYear) {
        this.shengchanYear = shengchanYear;
    }

    public void setAnquanBag(String anquanBag) {
        this.anquanBag = anquanBag;
    }

    public void setZuoweishu(String zuoweishu) {
        this.zuoweishu = zuoweishu;
    }

    public void setCheliangLevel(String cheliangLevel) {
        this.cheliangLevel = cheliangLevel;
    }

    public void setChemenNum(String chemenNum) {
        this.chemenNum = chemenNum;
    }

    public void setCheshenType(String cheshenType) {
        this.cheshenType = cheshenType;
    }

    public void setChangjiaName(String changjiaName) {
        this.changjiaName = changjiaName;
    }

    public void setDangweiNum(String dangweiNum) {
        this.dangweiNum = dangweiNum;
    }

    public void setZhuangbeiWeight(String zhuangbeiWeight) {
        this.zhuangbeiWeight = zhuangbeiWeight;
    }

    public void setZuzhuangFactory(String zuzhuangFactory) {
        this.zuzhuangFactory = zuzhuangFactory;
    }
}
