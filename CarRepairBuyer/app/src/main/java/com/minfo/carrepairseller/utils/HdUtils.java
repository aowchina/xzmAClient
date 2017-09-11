package com.minfo.carrepairseller.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.minfo.carrepairseller.R;


/**
 * 公共方法类
 * @author wangrui@min-fo.com
 */
public class HdUtils {
    private Context context;

    public  HdUtils(Context context){
        this.context = context;
    }
    /**
     * 实例化AlertDialog对象
     * @param isSetWindow:是否调整alert的位置;  y:纵向偏移量
     * @return AlertDialog对象
     * @author wangrui@min-fo.com
     * @date 2014-05-24
     */
    public AlertDialog insDialog(boolean isSetWindow, int y){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        if(isSetWindow){
            //设置窗口显示位置,默认居中,y为纵向偏移量(正下负上)
            Window win = dialog.getWindow();
            WindowManager.LayoutParams params = win.getAttributes();
            params.y = y;
            win.setAttributes(params);
        }
        dialog.show();
        return dialog;
    }

    //将float的数保留一位小叔
    public float getOneFloat(float a){
        return (float)(Math.round(a * 10))/10;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */
    public int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 获取userID
     * */
    public int getUserid(Context context){
        return (int) SPUtils.get(context, "userid", 0);
    }
    public void setUserid(Context context,int userid){
        SPUtils.put(context, "userid", userid);
    }
    /**
     * 获取 基本信息 字符串
     * @return appid+deviceid+version+ostype+厂家+机型+SDK+phonenum(或者imsi)
     * author wangrui@min-fo.com
     * date 2014-05-21
     */
    public String getBasePostStr(){
        SIMCardInfo cardInfo = new SIMCardInfo(context);
        String phoneNum = null;
        if (cardInfo.getPhoneNumber().length() == 0 || cardInfo.getPhoneNumber() == null) {
            phoneNum = cardInfo.getPhoneImsi();
        } else {
            phoneNum = cardInfo.getPhoneNumber();
        }

        if(phoneNum == null || phoneNum.equals("")){
            phoneNum = "000000000000000";
        }

        String postString = context.getResources().getString(R.string.appid)
                + "*" + cardInfo.getDeviceId() + "*"
                + context.getResources().getString(R.string.version)
                + "*android" + DeviceInfo.getVersionReleaseInfo() + "*"
                + DeviceInfo.getManufacturerInfo() + "*"
                + DeviceInfo.getModleInfo() + "*"
                + DeviceInfo.getVersionSDKInfo() + "*" + phoneNum;
        return postString;
    }
    /**
     * 向指定的Handler send massage
     * @param handler: 监听线程的handler对象
     * @param msgtag: 发送的message
     * author wangrui
     * date 2015-03-13
     */
    public void sendMsg(Handler handler, int msgtag){
        Message m = new Message();
        m.what = msgtag;
        handler.sendMessage(m);
    }

    /**
     * 转换字符串，当传递有汉字的内容时需转化
     * author wangrui
     */
    public String changeStr(String mStr) {
        String str = "";
        byte[] strByte = mStr.getBytes();
        for (int i = 0; i < strByte.length; i++) {
            str += strByte[i] + "#";
        }
        return str.trim();
    }

}
