package com.minfo.carrepair.entity.query;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MinFo021 on 17/7/4.
 */

public class VINChangModel implements Serializable {

    private VINModel che;
    @SerializedName("cimage")
    private String img;
    private int biaoshi; //1有，0没有

    public VINModel getChe() {
        return che;
    }

    public String getImg() {
        return img;
    }

    public int getBiaoshi() {
        return biaoshi;
    }

    public void setChe(VINModel che) {
        this.che = che;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setBiaoshi(int biaoshi) {
        this.biaoshi = biaoshi;
    }

    public class VINModel implements Serializable {
        List<Result> Result;
        private Info Info;
        private Additional Additional;

        public VINChangModel.Info getInfo() {
            return Info;
        }

        public void setInfo(VINChangModel.Info info) {
            Info = info;
        }

        public VINChangModel.Additional getAdditional() {
            return Additional;
        }

        public void setAdditional(VINChangModel.Additional additional) {
            Additional = additional;
        }

        public List<VINChangModel.Result> getResult() {
            return Result;
        }

        public void setResult(List<VINChangModel.Result> result) {
            Result = result;
        }
    }

    public class Result implements Serializable {
//        {
//            "LevelId": "CFV0216A0064",
//                "Manufacturers": "一汽大众",
//                "Brand": "大众",
//                "Series": "宝来",
//                "Models": "宝来",
//                "SalesName": "1.6 手自一体 时尚版",
//                "Year": "2011",
//                "EmissionStandard": "国4",
//                "VehicleType": "轿车",
//                "ListingYear": "2010",
//                "ListingMonth": "6",
//                "ProducedYear": "2010",
//                "IdlingYear": "2011",
//                "ProductionStatus": "停产",
//                "Country": "中国",
//                "VehicleAttributes": "合资",
//                "CylinderVolume": "1598",
//                "Displacement": "1.6",
//                "Induction": "自然吸气",
//                "FuelType": "汽油",
//                "FuelGrade": "93#",
//                "Horsepower": "105",
//                "PowerKw": "77",
//                "CylinderArrangement": "L",
//                "Cylinders": "4",
//                "ValvesPerCylinder": "4",
//                "TransmissionType": "自动",
//                "TransmissionDescription": "手自一体变速器(AMT)",
//                "GearNumber": "6",
//                "FrontBrake": "通风盘式",
//                "RearBrake": "盘式",
//                "PowerSteering": "机械液压助力",
//                "EngineLocation": "前置",
//                "DriveMode": "前轮驱动",
//                "Wheelbase": "2610",
//                "Doors": "四门",
//                "Seats": "5",
//                "FrontTyre": "195/65 R15",
//                "RearTyre": "195/65 R15",
//                "FrontRim": "15英寸",
//                "RearRim": "15英寸",
//                "RimsMaterial": "铝合金",
//                "SpareWheel": "非全尺寸",
//                "Sunroof": "无",
//                "PanoramicSunroof": "无",
//                "HidHeadlamp": "无",
//                "FrontFogLamp": "有",
//                "RearWiper": "无",
//                "AC": "有",
//                "AutoAC": "无"
//        }

        /**
         * "品牌名称","车型名称","销售名称","车辆类型","17位的车架号VIN","发动机型号","功率/转速(Kw/R)","发动机喷射类型","燃油类型","变速器类型","
         * 发动机缸数","气缸形式","排量(L)","生产年份","安全气囊","座位数","车辆级别","车门数","车身形式","厂家名称","档位数","装备质量(KG)","组装厂"
         */
        @SerializedName("remark")
        private String errormsg;
        @SerializedName("Brand")
        private String pinpai; // 品牌名称
        @SerializedName("Series")
        private String chexi; // 车系
        @SerializedName("Models")
        private String chenxing; // 车型名称
        @SerializedName("SalesName")
        private String xsname; // 销售名称
        @SerializedName("VehicleType")
        private String cheType; // 车辆类型
        private String vin; // 17位的车架号VIN
        @SerializedName("engine_type")
        private String fdjType; // 发动机型号
        @SerializedName("PowerKw")
        private String gonglv; // 功率/转速(Kw/R)
        @SerializedName("EngineLocation")
        private String fdjpsType; // 发动机喷射类型
        @SerializedName("FuelType")
        private String ranyou; // 燃油类型
        @SerializedName("TransmissionDescription")
        private String biansu; // 变速器类型
        @SerializedName("ValvesPerCylinder")
        private String fdjgangshu; // 发动机缸数
        @SerializedName("Induction")
        private String qigangType; // 气缸形式
        @SerializedName("Displacement")
        private String pailiang; // 排量(L)
        @SerializedName("Year")
        private String shengchanYear; // 生产年份
        @SerializedName("air_bag")
        private String anquanBag; // 安全气囊
        @SerializedName("Seats")
        private String zuoweishu; // 座位数
        @SerializedName("vehicle_level")
        private String cheliangLevel; // 车辆级别
        @SerializedName("Doors")
        private String chemenNum; // 车门数
        @SerializedName("car_body")
        private String cheshenType; // 车身形式
        @SerializedName("Manufacturers")
        private String changjiaName; // 厂家名称
        @SerializedName("GearNumber")
        private String dangweiNum; // 档位数
        @SerializedName("car_weight")
        private String zhuangbeiWeight; // 装备质量(KG)
        @SerializedName("assembly_factory")
        private String zuzhuangFactory; // 组装厂
        private String EmissionStandard;
        private String ListingYear;
        private String ListingMonth;
        private String ProducedYear;
        private String IdlingYear;
        private String ProductionStatus;
        private String Country;
        private String VehicleAttributes;
        private String CylinderVolume;
        private String FuelGrade;
        private String Horsepower;
        private String CylinderArrangement;
        private String Cylinders;
        private String TransmissionType;
        private String FrontBrake;
        private String RearBrake;
        private String PowerSteering;
        private String DriveMode;
        private String Wheelbase;
        private String FrontTyre;
        private String RearTyre;
        private String FrontRim;
        private String RearRim;
        private String RimsMaterial;
        private String SpareWheel;
        private String Sunroof;
        private String PanoramicSunroof;
        private String HidHeadlamp;
        private String FrontFogLamp;
        private String RearWiper;
        private String AC;
        private String AutoAC;
        private String LevelId;


        public String getLevelId() {
            return LevelId;
        }

        public void setLevelId(String levelId) {
            LevelId = levelId;
        }

        public void setChexi(String chexi) {
            this.chexi = chexi;
        }

        public void setEmissionStandard(String emissionStandard) {
            EmissionStandard = emissionStandard;
        }

        public void setListingYear(String listingYear) {
            ListingYear = listingYear;
        }

        public void setListingMonth(String listingMonth) {
            ListingMonth = listingMonth;
        }

        public void setProducedYear(String producedYear) {
            ProducedYear = producedYear;
        }

        public void setIdlingYear(String idlingYear) {
            IdlingYear = idlingYear;
        }

        public void setProductionStatus(String productionStatus) {
            ProductionStatus = productionStatus;
        }

        public void setCountry(String country) {
            Country = country;
        }

        public void setVehicleAttributes(String vehicleAttributes) {
            VehicleAttributes = vehicleAttributes;
        }

        public void setCylinderVolume(String cylinderVolume) {
            CylinderVolume = cylinderVolume;
        }

        public void setFuelGrade(String fuelGrade) {
            FuelGrade = fuelGrade;
        }

        public void setHorsepower(String horsepower) {
            Horsepower = horsepower;
        }

        public void setCylinderArrangement(String cylinderArrangement) {
            CylinderArrangement = cylinderArrangement;
        }

        public void setCylinders(String cylinders) {
            Cylinders = cylinders;
        }

        public void setTransmissionType(String transmissionType) {
            TransmissionType = transmissionType;
        }

        public void setFrontBrake(String frontBrake) {
            FrontBrake = frontBrake;
        }

        public void setRearBrake(String rearBrake) {
            RearBrake = rearBrake;
        }

        public void setPowerSteering(String powerSteering) {
            PowerSteering = powerSteering;
        }

        public void setDriveMode(String driveMode) {
            DriveMode = driveMode;
        }

        public void setWheelbase(String wheelbase) {
            Wheelbase = wheelbase;
        }

        public void setFrontTyre(String frontTyre) {
            FrontTyre = frontTyre;
        }

        public void setRearTyre(String rearTyre) {
            RearTyre = rearTyre;
        }

        public void setFrontRim(String frontRim) {
            FrontRim = frontRim;
        }

        public void setRearRim(String rearRim) {
            RearRim = rearRim;
        }

        public void setRimsMaterial(String rimsMaterial) {
            RimsMaterial = rimsMaterial;
        }

        public void setSpareWheel(String spareWheel) {
            SpareWheel = spareWheel;
        }

        public void setSunroof(String sunroof) {
            Sunroof = sunroof;
        }

        public void setPanoramicSunroof(String panoramicSunroof) {
            PanoramicSunroof = panoramicSunroof;
        }

        public void setHidHeadlamp(String hidHeadlamp) {
            HidHeadlamp = hidHeadlamp;
        }

        public void setFrontFogLamp(String frontFogLamp) {
            FrontFogLamp = frontFogLamp;
        }

        public void setRearWiper(String rearWiper) {
            RearWiper = rearWiper;
        }

        public void setAC(String AC) {
            this.AC = AC;
        }

        public void setAutoAC(String autoAC) {
            AutoAC = autoAC;
        }

        public String getChexi() {
            return chexi;
        }

        public String getEmissionStandard() {
            return EmissionStandard;
        }

        public String getListingYear() {
            return ListingYear;
        }

        public String getListingMonth() {
            return ListingMonth;
        }

        public String getProducedYear() {
            return ProducedYear;
        }

        public String getIdlingYear() {
            return IdlingYear;
        }

        public String getProductionStatus() {
            return ProductionStatus;
        }

        public String getCountry() {
            return Country;
        }

        public String getVehicleAttributes() {
            return VehicleAttributes;
        }

        public String getCylinderVolume() {
            return CylinderVolume;
        }

        public String getFuelGrade() {
            return FuelGrade;
        }

        public String getHorsepower() {
            return Horsepower;
        }

        public String getCylinderArrangement() {
            return CylinderArrangement;
        }

        public String getCylinders() {
            return Cylinders;
        }

        public String getTransmissionType() {
            return TransmissionType;
        }

        public String getFrontBrake() {
            return FrontBrake;
        }

        public String getRearBrake() {
            return RearBrake;
        }

        public String getPowerSteering() {
            return PowerSteering;
        }

        public String getDriveMode() {
            return DriveMode;
        }

        public String getWheelbase() {
            return Wheelbase;
        }

        public String getFrontTyre() {
            return FrontTyre;
        }

        public String getRearTyre() {
            return RearTyre;
        }

        public String getFrontRim() {
            return FrontRim;
        }

        public String getRearRim() {
            return RearRim;
        }

        public String getRimsMaterial() {
            return RimsMaterial;
        }

        public String getSpareWheel() {
            return SpareWheel;
        }

        public String getSunroof() {
            return Sunroof;
        }

        public String getPanoramicSunroof() {
            return PanoramicSunroof;
        }

        public String getHidHeadlamp() {
            return HidHeadlamp;
        }

        public String getFrontFogLamp() {
            return FrontFogLamp;
        }

        public String getRearWiper() {
            return RearWiper;
        }

        public String getAC() {
            return AC;
        }

        public String getAutoAC() {
            return AutoAC;
        }

        //        LevelId
        //                "EmissionStandard": "国4",
//        "ListingYear": "2010",
//                "ListingMonth": "6",
//                "ProducedYear": "2010",
//                "IdlingYear": "2011",
//                "ProductionStatus": "停产",
//                "Country": "中国",
//                "VehicleAttributes": "合资",
// //                "CylinderVolume": "1598",
//                "FuelGrade": "93#",
//                "Horsepower": "105",
        //                "CylinderArrangement": "L",
//                "Cylinders": "4",
//                "TransmissionType": "自动",
//                "FrontBrake": "通风盘式",
//                "RearBrake": "盘式",
//                "PowerSteering": "机械液压助力",
//                "DriveMode": "前轮驱动",
//                "Wheelbase": "2610",
//                "FrontTyre": "195/65 R15",
//                "RearTyre": "195/65 R15",
//                "FrontRim": "15英寸",
//                "RearRim": "15英寸",
//                "RimsMaterial": "铝合金",
//                "SpareWheel": "非全尺寸",
//                "Sunroof": "无",
//                "PanoramicSunroof": "无",
//                "HidHeadlamp": "无",
//                "FrontFogLamp": "有",
//                "RearWiper": "无",
//                "AC": "有",
//                "AutoAC": "无"
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

    public class Additional implements Serializable {
        //        "Additional": {
//            "Vin": "lfv2a2150a3043256",
//                    "VinYear": "2010"
//        }
        private String Vin;
        private String VinYear;

        public String getVin() {
            return Vin;
        }

        public void setVin(String vin) {
            Vin = vin;
        }

        public String getVinYear() {
            return VinYear;
        }

        public void setVinYear(String vinYear) {
            VinYear = vinYear;
        }
    }

    public class Info implements Serializable {
        //        {
//            "Success": true,
//                "Desc": "正常",
//                "Error": "E_NORMAL",
//                "Total": -1,
//                "Records": 6,
//                "Power": "55/99"
//        }
        private boolean Success;
        private String Desc;
        private String Error;

        private String Total;

        private String Records;

        private String Power;

        public boolean isSuccess() {
            return Success;
        }

        public void setSuccess(boolean success) {
            Success = success;
        }

        public String getDesc() {
            return Desc;
        }

        public void setDesc(String desc) {
            Desc = desc;
        }

        public String getError() {
            return Error;
        }

        public void setError(String error) {
            Error = error;
        }

        public String getTotal() {
            return Total;
        }

        public void setTotal(String total) {
            Total = total;
        }

        public String getRecords() {
            return Records;
        }

        public void setRecords(String records) {
            Records = records;
        }

        public String getPower() {
            return Power;
        }

        public void setPower(String power) {
            Power = power;
        }
    }
}
