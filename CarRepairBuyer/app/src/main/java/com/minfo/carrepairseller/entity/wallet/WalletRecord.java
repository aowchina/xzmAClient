package com.minfo.carrepairseller.entity.wallet;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/6/5.
 */

public class WalletRecord implements Serializable {

    private String id;
    private String userid;
    private String tid;
    private String txmoney;
    private String paytime;
    private String type;
    private String txorderid;
    private String money;
    private String addtime;

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTxmoney() {
        return txmoney;
    }

    public void setTxmoney(String txmoney) {
        this.txmoney = txmoney;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getTxorderid() {
        return txorderid;
    }

    public void setTxorderid(String txorderid) {
        this.txorderid = txorderid;
    }

    @Override
    public String toString() {
        return "WalletRecord{" +
                "id='" + id + '\'' +
                ", userid='" + userid + '\'' +
                ", tid='" + tid + '\'' +
                ", txmoney='" + txmoney + '\'' +
                ", paytime='" + paytime + '\'' +
                ", type=" + type +
                ", txorderid='" + txorderid + '\'' +
                '}';
    }
}
