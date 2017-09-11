package com.minfo.carrepairseller.entity.address;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/5/15.
 */

public class AddressItem implements Serializable {
    /**
     * "id": "3382",
     "userid": "2658",
     "user_name": "saa",
     "user_pid": "130000",
     "user_cid": "130100",
     "user_qid": "130102",
     "user_address": "aad",
     "user_tel": "13123456789",
     "isdefault": "1",
     "pname": "河北省",
     "cname": "石家庄市",
     "aname": "长安区 "
     */
    private String id;
    @SerializedName("user_name")
    private String name;
    private String userid;
    @SerializedName("user_tel")
    private String tel;
    @SerializedName("user_address")
    private String address;
    @SerializedName("isdefault")
    private int state;
    @SerializedName("user_pid")
    private int pid;
    @SerializedName("user_cid")
    private int cid;
    @SerializedName("user_qid")
    private int did;
    @SerializedName("pname")
    private String pname;
    @SerializedName("cname")
    private String cname;
    @SerializedName("aname")
    private String dname;

    public String getUserid() {
        return userid;
    }

    public int getPid() {
        return pid;
    }

    public int getCid() {
        return cid;
    }

    public int getDid() {
        return did;
    }

    public String getPname() {
        return pname;
    }

    public String getCname() {
        return cname;
    }

    public String getDname() {
        return dname;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public String getAddress() {
        return address;
    }

    public int getState() {
        return state;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setState(int state) {
        this.state = state;
    }
}
