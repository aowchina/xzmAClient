package com.minfo.carrepair.entity;

public class ShareInfo {
	private String title; // 分享标题
	private String titleUrl; // 分享标题链接
	private String content; // 分享内容
	private String imagePath; // 分享图片本地路径
	private String imageUrl; // 分享图片网络路径
	private String contentUrl; // 分享内容URL,仅在微信（包括好友和朋友圈）中使用
	private String comment; // 我对这条分享的评论，仅在人人网和QQ空间使用
	private String site; // 分享内容的网站名，仅在人人网和QQ空间使用
	private String siteUrl; // 分享内容的网站地址，仅在人人网和QQ空间使用
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return the titleUrl
	 */
	public String getTitleUrl() {
		return titleUrl;
	}

	/**
	 * @param titleUrl the titleUrl to set
	 */
	public void setTitleUrl(String titleUrl) {
		this.titleUrl = titleUrl;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}
	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}
	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	/**
	 * @return the contentUrl
	 */
	public String getContentUrl() {
		return contentUrl;
	}
	/**
	 * @param contentUrl the contentUrl to set
	 */
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @return the site
	 */
	public String getSite() {
		return site;
	}
	/**
	 * @param site the site to set
	 */
	public void setSite(String site) {
		this.site = site;
	}
	/**
	 * @return the siteUrl
	 */
	public String getSiteUrl() {
		return siteUrl;
	}
	/**
	 * @param siteUrl the siteUrl to set
	 */
	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}
	
}	
