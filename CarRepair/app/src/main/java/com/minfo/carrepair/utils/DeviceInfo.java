package com.minfo.carrepair.utils;

public class DeviceInfo {
	/**
	 * 获得设备厂商号
	 * @return 返回设备厂商
	 */
	public static String getManufacturerInfo() {
		return android.os.Build.MANUFACTURER;
	}

	/**
	 * 设备型号
	 * @return 返回设备型号
	 */
	public static String getModleInfo() {
		return android.os.Build.MODEL;
	}

	/**
	 * 系统版本号
	 * @return返回系统版本号
	 */
	public static String getVersionReleaseInfo() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 系统SDK
	 * @return 返回系统SDK
	 */
	public static String getVersionSDKInfo() {
		return android.os.Build.VERSION.SDK_INT + "";
	}
}
