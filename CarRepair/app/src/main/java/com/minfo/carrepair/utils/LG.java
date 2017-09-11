package com.minfo.carrepair.utils;

import android.util.Log;

public class LG {
//	public static boolean isDebug = false;
	public static boolean isDebug = true;
	public static final String TAG = "tag";
	public static void i(String msg){
		if(isDebug){
			Log.i(TAG, msg);
		}
	}
	public static void e(String msg){
		if(isDebug){
			Log.e(TAG, msg);
		}
	}
	public static void w(String msg){
		if(isDebug){
			Log.w(TAG, msg);
		}
	}
	
	public static void i(String tag, String msg){
		if(isDebug){
			Log.i(tag, msg);
		}
	}
	public static void e(String tag, String msg){
		if(isDebug){
			Log.e(tag, msg);
		}
	}
}
