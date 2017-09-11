package com.minfo.carrepairseller.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.MyApplication;
import com.minfo.carrepairseller.widget.CircleDisplayer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by liujing on 15/8/25.
 */
public class UniversalImageUtils {
    private ImageLoader mImgLoader;
    private static UniversalImageUtils instance = new UniversalImageUtils();

    public static void displayImage(String url, ImageView imageView, DisplayImageOptions options) {


        getInstance().getImgLoader().displayImage(url, imageView, options);
    }

    /**
     * 播放广告条
     * @param url
     * @param imageView
     */
    public static void displayImageAdvert(String url, ImageView imageView) {


        getInstance().getImgLoader().displayImage(url, imageView, getDisplayOptionsAdvert());
    }
    public static void displayImageCircle(String url, ImageView imageView, int round) {


        getInstance().getImgLoader().displayImage(url, imageView, getDisplayOptionsCircle(round));
    }
    public static UniversalImageUtils getInstance() {
        return instance;
    }


    public static void displayImage(String url, ImageView imageView) {


        getInstance().getImgLoader().displayImage(url, imageView);
    }

    public static void displayImageUseDefOptions(String url, ImageView imageView) {


        getInstance().getImgLoader().displayImage(url, imageView, getDisplayOptions());
    }

    public static void disCircleImage(String url, ImageView imageView) {
        getInstance().getImgLoader().displayImage(url, imageView, getDisplayCircleOptions());
    }

    public static void loadDefImage(String url, ImageLoadingListener listener) {
        getInstance().getImgLoader().loadImage(url, listener);
    }

    /**
     * 清除单张图片缓存
     * @param url
     */
    public static void removeFromCache(String url) {
        DiskCacheUtils.removeFromCache(url, getInstance().getImgLoader().getDiskCache());
        MemoryCacheUtils.removeFromCache(url, getInstance().getImgLoader().getMemoryCache());
    }
    public static DisplayImageOptions getDisplayOptions() {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.qixiu_logo) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.qixiu_logo)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.qixiu_logo)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.NONE)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
//                .displayer(new RoundedBitmapDisplayer(1))
                .build();//构建完成

        return options;
    }
    public static DisplayImageOptions getDisplayOptionsCircle(int round) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.qixiu_logo) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.qixiu_logo)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.qixiu_logo)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.NONE)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(round))
                .build();//构建完成

        return options;
    }
    public static DisplayImageOptions getDisplayCircleOptions() {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .displayer(new CircleDisplayer())
                .showImageOnLoading(R.mipmap.qixiu_logo) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.qixiu_logo)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.qixiu_logo)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.NONE)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .build();//构建完成

        return options;
    }

    public com.nostra13.universalimageloader.core.ImageLoader getImgLoader() {
        if (mImgLoader == null) {
            synchronized (UniversalImageUtils.class) {
                if (mImgLoader == null) {
                    mImgLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
                    mImgLoader.init(initImgloadConf());
                }
            }
        }
        return mImgLoader;
    }


    private ImageLoaderConfiguration initImgloadConf() {

        File cacheDir = StorageUtils.getOwnCacheDirectory(MyApplication.getInstance(), "img/cache");

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(MyApplication.getInstance())
                .memoryCacheExtraOptions(1024, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                        // .diskCacheExtraOptions(480, 800,null)  // Can slow ImageLoader, use it carefully (Better don't use it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)//线程池内加载的数量,最好是1-5
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()

                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(20)
//
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100) //缓存的文件数量
                .diskCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径

                        // 将缓存下来的文件以什么方式命名
                        // 里面可以调用的方法有
                        // 1.new Md5FileNameGenerator() //使用MD5对UIL进行加密命名
                        // 2.new HashCodeFileNameGenerator()//使用HASHCODE对UIL进行加密命名
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)

                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(MyApplication.getInstance(), 20 * 1000, 30 * 1000)) // connectTimeout (20 s), readTimeout (30 s)超时时间
                .imageDecoder(new BaseImageDecoder(false)) // default
                .writeDebugLogs() // Remove for release app
                .build();//开始构建

        return config;

    }

    public static DisplayImageOptions getDisplayOptionsAdvert() {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.test_guanggao) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.test_guanggao)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.test_guanggao)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.NONE)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(1))
                .build();//构建完成

        return options;
    }
}
