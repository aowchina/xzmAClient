/**
 * @author Administrator
 * @date 2013-10-11 上午10:49:30
 * @desc ImageOptions
 */
package minfo.com.albumlibrary.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import minfo.com.albumlibrary.R;

/*DisplayImageOptions options;  
options = new DisplayImageOptions.Builder()  
 .showImageOnLoading(R.drawable.ic_launcher) //设置图片在下载期间显示的图片  
 .showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片  
.showImageOnFail(R.drawable.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
.cacheInMemory(true)//设置下载的图片是否缓存在内存中  
.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中  
.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示  
.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//  
.decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置  
//.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
//设置图片加入缓存前，对bitmap进行设置  
//.preProcessor(BitmapProcessor preProcessor)  
.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位  
.displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少  
.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间  
.build();//构建完成  
*/

/**
 * @author zhoukai
 * @date 2013-10-11 上午10:49:30
 * @description 图片显示参数
 */

public class ImageOptions {

	public static DisplayImageOptions getAlbumPictureOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.qixiu_logo)
				.showImageForEmptyUri(R.drawable.qixiu_logo)
				.showImageOnFail(R.drawable.qixiu_logo).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}
	

}
