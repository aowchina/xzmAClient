package com.minfo.carrepair.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by min-fo-012 on 17/6/20.
 */
public class WLInfoEntity implements Serializable {
    private String message;
    private String nu;
    private String ischeck;
    private String condition;
    private String com;
    private String status;
    private String state;
    private String comcontact;
    private String comurl;
    private List<WLInfoList>data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
    }

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getComcontact() {
        return comcontact;
    }

    public void setComcontact(String comcontact) {
        this.comcontact = comcontact;
    }

    public String getComurl() {
        return comurl;
    }

    public void setComurl(String comurl) {
        this.comurl = comurl;
    }

    public List<WLInfoList> getData() {
        return data;
    }

    public void setData(List<WLInfoList> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WLInfoEntity{" +
                "message='" + message + '\'' +
                ", nu='" + nu + '\'' +
                ", ischeck='" + ischeck + '\'' +
                ", condition='" + condition + '\'' +
                ", com='" + com + '\'' +
                ", status='" + status + '\'' +
                ", state='" + state + '\'' +
                ", comcontact='" + comcontact + '\'' +
                ", comurl='" + comurl + '\'' +
                ", data=" + data +
                '}';
    }
}
