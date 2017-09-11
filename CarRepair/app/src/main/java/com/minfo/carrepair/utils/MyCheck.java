package com.minfo.carrepair.utils;

import java.text.DecimalFormat;

/*
 * 验证类
 * @author wangrui@min-fo.com
 * @date: 2014-05-26
 */
public class MyCheck {
	//姓名
	private static String name_exp ="^[(\u4e00-\u9fa5)|(a-zA-Z)]{1,10}$";
	//作品名称
	private static String proname_exp = "^[(\u4e00-\u9fa5)|a-zA-Z0-9-_]{1,10}$";
	//订单要求
	private static String intro_exp = "^[(\u4e00-\u9fa5)|a-zA-Z0-9-_\\s()（）,，.。]{1,40}$";
	//评论 回复
	private static String comment_exp = "^[(\u4e00-\u9fa5)|a-zA-Z0-9\\-_\\s()（）,，.。]{1,80}$";

	//private static String number_exp = "^[0-9]+$";
	private static String money_exp = "^[0-9]+$";
	private static String eventid_exp = "^(1)[0-9]{3}$";
	//年
	private static String year_exp = "^[0-9]{1,4}+$";
	//月
	private static String month_exp = "^[0-9]{1,2}+$";
	//日
	private static String day_exp = "^[0-9]{1,2}+$";
	private static String eventname_exp = "^[(\u4e00-\u9fa5)|(a-zA-Z0-9)]{2,10}$";
	private static String teamname_exp = "^[(\u4e00-\u9fa5)|(a-zA-Z0-9)]{2,12}$";
	//昵称
	private static String nicheng_exp = "^[(\u4e00-\u9fa5)|a-zA-Z-_\\s]{1,10}$";
	//邮箱
	private static String mail_exp = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	//密码
	private static String psw_exp = "^[0-9a-zA-Z]{1,20}$";
	private static String username_exp = "^[0-9a-zA-Z]{4,15}$";
//	private static String tel_exp = "[1][358]\\d{9}";
	private static String tel_exp = "^(13[0-9]|15[012356789]|17[3678]|18[0-9]|14[57])[0-9]{8}$";
	private static String  shi_exp= "^([0-9][0-9]*)+(.[0-9]{1,2})?$";
	//验证是否符合手机号要求
	public static boolean isTel(String str){
		if(str.matches(tel_exp)){
			return true;
		}
		return false;
	}
	
	//验证是否符合昵称要求
	public static boolean isNiCheng(String str){
		if(str.matches(nicheng_exp)){
			return true;
		}
		return false;
	}
	//验证是否符合姓名要求
	public static boolean isName(String str){
		if(str.matches(name_exp)){
			return true;
		}
		return false;
	}
	//验证作品名称要求
	public static boolean isProName(String str){
		if(str.matches(proname_exp)){
			return true;
		}
		return false;
	}
	//验证简介要求
	public static boolean isIntro(String str){
		if(str.matches(intro_exp)){
			return true;
		}
		return false;
	}
	//验证评价 回复
	public static boolean isComment(String str){
		if(str.matches(comment_exp)){
			return true;
		}
		return false;
	}

	//验证是否符合尺寸 金额要求
	public static boolean isMoney(String str){
		if (str.matches(shi_exp)) {
			return true;
		}
//		if (str.contains(".")&&str.matches(shi_exp)) {
//				return true;
//			}
//
//		if(!str.contains(".")&&!str.matches(zheng_exp) && !str.substring(0, 1).equals("0")){
//			return true;
//		}
		return false;
	}
	//验证是否符合球队名称求要
	public static boolean isTeamName(String str){
		if(str.matches(teamname_exp)){
			return true;
		}
		return false;
	}
	
//	//验证是否符合充值要求
//	public static boolean isMoney(String str){
//		if(str.matches(money_exp) || !str.substring(0, 1).equals("0")){
//			return true;
//		}
//		return false;
//	}
	
	//验证是否符合赛事名称要求
	public static boolean isEventName(String str){
		if(str.matches(eventname_exp)){
			return true;
		}
		return false;
	}
	
	//验证是否符合赛事编号要求
	public static boolean isEventId(String str){
		if(str.matches(eventid_exp)){
			return true;
		}
		return false;
	}
	
	//验证是否符合邮箱要求
	public static boolean isEmail(String str){
		if(str.matches(mail_exp)){
			return true;
		}
		return false;
	}
	
	//验证密码是否符合要求
	public static boolean isPsw(String str){
		if(str.matches(psw_exp)){
			return true;
		}
		return false;
	}
	//验证是否符合年要求
	public static boolean isYear(String str){
		if(str.matches(year_exp)){
			return true;
		}
		return false;
	}
	//验证是否符合月要求
	public static boolean isMonth(String str){
		if(str.matches(month_exp)){
			return true;
		}
		return false;
	}
	//验证是否符合日要求
	public static boolean isDay(String str){
		if(str.matches(day_exp)){
			return true;
		}
		return false;
	}
	//验证用户名是否符合要求
	public static boolean isUserName(String str){
		if(str.matches(username_exp)){
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串
	 * @param str
	 * @return
     */
	public static boolean isEmpty(String str) {
		if(str == null || str.equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * 转换价格格式保留两位小数
	 * @param price
	 * @return
	 */
	public static String priceFormatChange(String price) {
		String str = "";
		try {
			str = new DecimalFormat("#0.00").format(Double.parseDouble(price));
		}
		catch (Exception e) {

		}

		return str;
	}
}
