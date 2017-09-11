package com.minfo.carrepairseller.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import java.io.InputStream;

;

/**
 * 图片处理类
 * @author wangrui@min-fo.com
 */
public class HdImg {
    private Context context;

    @SuppressLint("SdCardPath")
    public  HdImg(Context context){
        this.context = context;
    }

    public int getRealSize(int a, int b, int c){
        return 0;
    }

    /**
     * 动态控制ImageView的缩放
     * @param v : ImageView对象
     * @param max_w : ImageView的最大宽
     * @param max_h : ImageView的最大高
     * author wangrui
     */
    public void addImg(ImageView v, int max_w, int max_h){
        v.setAdjustViewBounds(true);
        v.setMaxWidth(max_w);
        v.setMaxHeight(max_h);
        v.setScaleType(ScaleType.CENTER_INSIDE);
    }

    /**
     * 根据效果图与屏幕实际宽高计算等比例的宽高
     * @param oldSize : 切图的宽/高
     * @param oldBaseSize : 效果图宽/高
     * @param newBaseSize : 屏幕宽/高
     * author wangrui
     */
     public int getScaleSize(int oldSize, int oldBaseSize, int newBaseSize){
        return (int)(oldSize * newBaseSize / oldBaseSize);
    }

    public int getNewSize(int baseValue, double reValue, int tag){
        int return_value = 0;
        if(tag == 1){
            return_value = (int)(reValue * baseValue / 12.7);
        }else{
            return_value = (int)(reValue * baseValue / 22.5);
        }
        return return_value;
    }

    //以最节省内存的形式获取Bitmap对象
    public Bitmap getBpByRes(int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
    /**
     * 获取屏幕宽
     * @return 屏幕高
     * @author wangrui@min-fo.com
     * @date 2014-05-24
     */
    public int getScreenWidth(){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获取屏幕高
     * @return 屏幕高
     * @author wangrui@min-fo.com
     * @date 2014-05-24
     */
    public int getScreenHeight(){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
    //缩放图片
    public Bitmap getZoomBp(int resId, int new_width, int new_height, int scaleType){
        Bitmap old_bp = getBpByRes(resId);

        int old_width = old_bp.getWidth();   //原图宽
        int old_height = old_bp.getHeight(); //原图高

        Matrix matrix = new Matrix();
        float scale_w;
        float scale_h;

        //其它值表示不失真；2表示按固定宽高(可能会失真)；3表示按宽比例；4表示按高比例
        switch(scaleType){
            case 2:
                scale_w = ((float) new_width) / old_width;
                scale_h = ((float) new_height) / old_height;
                break;
            case 3:
                scale_w = scale_h = ((float) new_width) / old_width;
                break;
            case 4:
                scale_w = scale_h = ((float) new_height) / old_height;
                break;
            default:
                float scale_base;
                if ((new_width / old_width) <= (new_height / old_height)) {
                    scale_base = ((float) new_width) / old_width;
                }else{
                    scale_base = ((float) new_height) / old_height;
                }
                scale_w = scale_h = scale_base;
                break;
        }

        matrix.postScale(scale_w, scale_h);

        Bitmap result_bp = Bitmap.createBitmap(old_bp, 0, 0, old_width, old_height, matrix, true);
        if(result_bp != null){
            return result_bp;
        }

        return null;
    }
    /**
     * 向指定的Handler send massage
     * author wangrui
     * date 2015-03-13
     */
    public void sendMsg(Handler handler, int msgtag){
        Message m = new Message();
        m.what = msgtag;
        handler.sendMessage(m);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}