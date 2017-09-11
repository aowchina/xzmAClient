package com.minfo.carrepairseller.entity.query;

import java.io.Serializable;

public class PictureItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String imgUrl;
	private String subImgUrl;
	private boolean isAdd;
	private boolean isNet;
	private String address;
	private String times;
//	private String caseId; // 湿档案id
	private String cardId;
	private double longitude;
	private double latitude;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isNet() {
		return isNet;
	}

	public void setNet(boolean isNet) {
		this.isNet = isNet;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getSubImgUrl() {
		return subImgUrl;
	}

	public void setSubImgUrl(String subImgUrl) {
		this.subImgUrl = subImgUrl;
	}

	public boolean isAdd() {
		return isAdd;
	}

	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	
}
