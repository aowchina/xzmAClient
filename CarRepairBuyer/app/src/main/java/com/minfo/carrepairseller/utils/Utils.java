package com.minfo.carrepairseller.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.hyphenate.easeui.utils.CMethodUtil;
import com.hyphenate.easeui.utils.EaseDialogUtil;
import com.hyphenate.easeui.utils.EnsureCancleInterface;
import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.login.LoginActivity;
import com.minfo.carrepairseller.jni.JniClient;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;



/**
 * Created by liujing on 15/8/20.
 */
public class Utils {
    private Context context;
    private SharedPreferences sp;


    public String getNickMessage() {
        return sp.getString("nick", "");
    }

    public void setNickMessage(String temp) {
        sp.edit().putString("nick", temp).commit();
    }
    public String getImgMessage() {
        return sp.getString("img", "");
    }

    public void setImgMessage(String temp) {
        sp.edit().putString("img", temp).commit();
    }


    public int getFragrMessage() {
        return sp.getInt("frag", 0);
    }

    public void setOFragMessage(int temp) {
        sp.edit().putInt("frag", temp).commit();
    }
    public int getOrderMessage() {
        return sp.getInt("ordermessage", 0);
    }

    public void setOrderMessage(int temp) {
        sp.edit().putInt("ordermessage", temp).commit();
    }
    public  Utils(Context context){
        this.context = context;
        this.sp = context.getSharedPreferences("SpHd", Activity.MODE_PRIVATE);
    }
    //加密
    public Map<String,String> getParams(String mPostStr){
        LG.e("params", mPostStr);
        int str_counter = (mPostStr.length() / 60);
        if((mPostStr.length() % 60) > 0){
            str_counter = str_counter + 1;
        }
        Map<String,String> params = new HashMap<>();
        params.put("p0", String.valueOf(str_counter + 3));
        params.put("p1", context.getResources().getString(R.string.appid));
        params.put("p2", "2");

        for(int i = 3; i <= (str_counter + 2); i++){
            if(mPostStr.length() < 60){
                params.put("p" + i, JniClient.GetEncodeStr(mPostStr.substring(0, mPostStr.length())));
            }
            else{
                params.put("p" + i, JniClient.GetEncodeStr(mPostStr.substring(0, 60)));
                mPostStr = mPostStr.substring(60);
            }
        }
        return params;
    }
    /**
     * 获取 基本信息 字符串
     * @return appid+deviceid+version+ostype+厂家+机型+SDK+phonenum(或者imsi)
     * author wangrui@min-fo.com
     * date 2014-05-21
     */
    public String getBasePostStr(){
        String postString;

        SIMCardInfo cardInfo = new SIMCardInfo(context);
        String phoneNum = null;
        if (cardInfo.getPhoneNumber().length() == 0 || cardInfo.getPhoneNumber() == null) {
            phoneNum = cardInfo.getPhoneImsi();
        } else {
            phoneNum = cardInfo.getPhoneNumber();
        }

        if (phoneNum == null || phoneNum.equals("")) {
            phoneNum = "000000000000000";
        }

        postString = context.getResources().getString(R.string.appid)
                + "*" + cardInfo.getDeviceId() + "*"
                + context.getResources().getString(R.string.version)
                + "*android" + DeviceInfo.getVersionReleaseInfo() + "*"
                + DeviceInfo.getManufacturerInfo() + "*"
                + DeviceInfo.getModleInfo() + "*"
                + DeviceInfo.getVersionSDKInfo() + "*" + phoneNum;

        return postString;
    }

    /**
     * 判断是否连网,3G or Wifi
     * @param :调用此方法的Activity
     * @author wangrui
     * @date 2014-05-21
     */
    public Boolean isOnLine() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
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

//    public String getServerData(String mURL, String mPostStr) throws JSONException {
//        HttpPost httpRequest = new HttpPost(mURL);
//        try{
//        	/*HTTP request*/
//            httpRequest.setEntity(new UrlEncodedFormEntity(getParam(mPostStr), HTTP.UTF_8));
//        	/*HTTP response*/
//            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
//        	/*状态码200 ok*/
//            if(httpResponse.getStatusLine().getStatusCode() == 200){
//                return EntityUtils.toString(httpResponse.getEntity());
//            }
//        }
//        catch (ClientProtocolException e){
//            e.printStackTrace();
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }
//    public List<NameValuePair> getParam(String mPostStr){
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("minfo", mPostStr));
//        return params;
//    }

    public String getUserid(){
        return sp.getString("userid", "0");
    }
    public void saveUserid(String userid){
        sp.edit().putString("userid",userid).commit();
    }
    public String getAuth(){
        return sp.getString("type", "0");
    }
    public void saveAuth(String auth){
        sp.edit().putString("type", auth).commit();
    }

    public void intentTo(Class<?> clazz){
        Intent intent = new Intent(context,clazz);
        context.startActivity(intent);
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
    public static void jumpTo(Context context,Class<?> clazz){
        Intent intent = new Intent(context,clazz);
        context.startActivity(intent);
    }
    public static void jumpToLoginActivity(Context context){
        ToastUtils.show(context, "账号异常，请重新登录");
        SPUtils.put(context, "userid", 0);
        Intent intent = new Intent(context, LoginActivity.class);

        context.startActivity(intent);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dp(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public  int dp2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public String convertChinese(String str){
        String temp = "";
        try {
            byte[] strTemp = str.getBytes("utf-8");
            for (int i = 0; i < strTemp.length; i++) {
                temp += strTemp[i] + "#";
            }
            return temp;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
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
     * 获取昵称
     * */
    public String getName(){
        return sp.getString("name", "0");
    }
    public void saveName(String userid){
        sp.edit().putString("name",userid).commit();
    }
    /**
     * 获取头像
     * */
    public String getImg(){
        return sp.getString("img", "0");
    }
    public void saveImg(String userid){
        sp.edit().putString("img",userid).commit();
    }
}
