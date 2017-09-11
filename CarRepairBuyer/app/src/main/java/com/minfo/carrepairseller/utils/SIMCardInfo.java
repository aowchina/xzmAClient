package com.minfo.carrepairseller.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 获取用户手机信息
 * @author wangrui
 * @date 2014-09-15
 */
public class SIMCardInfo {

	private TelephonyManager telManager;// 手机管理对象
	private String IMSI; //

	public SIMCardInfo(Context context) {
		telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}

	/**
	 * 获取设备ID
	 * @return 返回设备id
	 */
	public String getDeviceId() {
		return telManager.getDeviceId();
	}

	/**
	 * 获得用户电话号码
	 * @return 返回电话号码
	 */
	public String getPhoneNumber() {
		String telnum = telManager.getLine1Number();
		if(telnum == null){
			telnum = "";
		}
		return telnum;
	}

	/**
	 * 获取SIM唯一标识
	 * @return 返回标识
	 */
	public String getPhoneImsi() {
		return telManager.getSubscriberId();
	}

	/**
	 * 获取运营商类别
	 * @return 返回运营商类别
	 */
	public String getProviderName() {
		String providerName = null;
		IMSI = telManager.getSubscriberId();
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
			providerName = "中国移动";
		} else if (IMSI.startsWith("46001")) {
			providerName = "中国联通";
		} else if (IMSI.startsWith("46003")) {
			providerName = "中国电信";
		}
		return providerName;
	}
}
