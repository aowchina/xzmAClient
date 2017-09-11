package com.minfo.carrepair.jni;
/**
 * Description: Jni加密解密
 * Date : 2012-11-22
 * @author guoning@min-fo.com
 */
public class JniClient {
	static public native String GetEncodeStr(String str);
	static public native String GetDecodeStr(String str);
}