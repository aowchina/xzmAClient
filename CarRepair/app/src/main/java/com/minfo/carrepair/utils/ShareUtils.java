package com.minfo.carrepair.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.minfo.carrepair.R;
import com.minfo.carrepair.entity.ShareInfo;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import minfo.com.albumlibrary.utils.LG;

import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.EXTRA_TEXT;
//import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * Utilities for creating a share intent
 */
public class ShareUtils {

	/**
	 * 演示调用ShareSDK执行分享
	 * @param info
	 * @param context
	 * @param platformToShare  指定直接分享平台名称（一旦设置了平台名称，则九宫格将不会显示）
	 */

	public static void showShare(Context context, ShareInfo info, String platformToShare) {
		showShare(context, info, platformToShare, true);
	}
	/**
	 * 演示调用ShareSDK执行分享
	 *
	 * @param context
	 * @param platformToShare  指定直接分享平台名称（一旦设置了平台名称，则九宫格将不会显示）
	 * @param showContentEdit  是否显示编辑页
	 */
	public static void showShare(Context context, ShareInfo info, String platformToShare, boolean showContentEdit) {
		if(info == null) {
			Toast.makeText(context, "分享信息不能为空", Toast.LENGTH_SHORT).show();
			return;
		}

		OnekeyShare oks = new OnekeyShare();
		oks.setShareType(Platform.SHARE_WEBPAGE);
		oks.setSilent(!showContentEdit);
		if (platformToShare != null) {
			oks.setPlatform(platformToShare);
		}
		//ShareSDK快捷分享提供两个界面第一个是九宫格 CLASSIC  第二个是SKYBLUE
		oks.setTheme(OnekeyShareTheme.CLASSIC);
		// 在自动授权时可以禁用SSO方式
		oks.disableSSOWhenAuthorize();
		//oks.setAddress("12345678901"); //分享短信的号码和邮件的地址
		if(info.getTitle() != null && !"".equals(info.getTitle())){
			oks.setTitle(info.getTitle());
		}
		if(info.getTitleUrl() != null && !"".equals(info.getTitleUrl())){
			oks.setTitleUrl(info.getTitleUrl());
		}
//        oks.setTitleUrl("http://mob.com");
		if(info.getContent() != null && !"".equals(info.getContent())){
			oks.setText(info.getContent());
		}
//        oks.setText(context.getString(R.string.app_share_text));
		//oks.setImagePath("/sdcard/test-pic.jpg");  //分享sdcard目录下的图片
		if(info.getImageUrl() != null && !"".equals(info.getImageUrl())){
			oks.setImageUrl(info.getImageUrl());
		}
//        oks.setImageUrl("http://f1.webshare.mob.com/dimgs/1c950a7b02087bf41bc56f07f7d3572c11dfcf36.jpg");
		if(info.getContentUrl() != null && !"".equals(info.getContentUrl())){
			oks.setUrl(info.getContentUrl());
		}
//        oks.setUrl("http://www.mob.com"); //微信不绕过审核分享链接
//        oks.setFilePath(testVideo);  //filePath用于视频分享
//        oks.setComment(context.getString(R.string.app_share_comment)); //我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
		if(info.getSite() != null && !"".equals(info.getSite())){
			oks.setSite(info.getSite());
		}
//        oks.setSite("ShareSDK");  //QZone分享完之后返回应用时提示框上显示的名称
		if(info.getSiteUrl() != null && !"".equals(info.getSiteUrl())){
			oks.setSiteUrl(info.getSiteUrl());
		}
//        oks.setSiteUrl("http://mob.com");//QZone分享参数
//        oks.setVenueName("ShareSDK");
//        oks.setVenueDescription("This is a beautiful place!");
//        oks.setLatitude(23.169f);
//        oks.setLongitude(112.908f);
		// 将快捷分享的操作结果将通过OneKeyShareCallback回调
		// oks.setCallback(new OneKeyShareCallback());
		// 去自定义不同平台的字段内容
		// oks.setShareContentCustomizeCallback(new
		// ShareContentCustomizeDemo());
		// 在九宫格设置自定义的图标
//        Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
//        String label = "ShareSDK";
//        View.OnClickListener listener = new View.OnClickListener() {
//            public void onClick(View v) {
//
//            }
//        };
//        oks.setCustomerLogo(logo, label, listener);

		// 为EditPage设置一个背景的View
		//oks.setEditPageBackground(getPage());
		// 隐藏九宫格中的新浪微博
		// oks.addHiddenPlatform(SinaWeibo.NAME);

		// String[] AVATARS = {
		// 		"http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
		// 		"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
		// 		"http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
		// 		"http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
		// 		"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
		// 		"http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg" };
		// oks.setImageArray(AVATARS);              //腾讯微博和twitter用此方法分享多张图片，其他平台不可以

		// 启动分享
		oks.show(context);
	}
}
