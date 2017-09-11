package com.minfo.carrepair.entity.shop;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AdvertItem implements Serializable {
    @SerializedName("goods_num")
	private String id;
	@SerializedName("img")
	private String url;
	@SerializedName("sowimg_addtime")
	private String addTime;
	@SerializedName("sowimg_desc")
	private String linkUrl;
	@SerializedName("sowimg_ismanagerurl")
	private int isManager;
	@SerializedName("sowimg_isshare")
	private int isShare;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public int getIsManager() {
		return isManager;
	}
	public void setIsManager(int isManager) {
		this.isManager = isManager;
	}
	public int getIsShare() {
		return isShare;
	}
	public void setIsShare(int isShare) {
		this.isShare = isShare;
	}
	
}
