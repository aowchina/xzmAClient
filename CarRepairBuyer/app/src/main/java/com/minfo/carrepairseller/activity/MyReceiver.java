package com.minfo.carrepairseller.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.minfo.carrepairseller.CommonInterface.PermissionListener;
import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.login.LoginActivity;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.http.VolleyHttpClient;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by fei on 16/4/21.
 */
public class MyReceiver extends BroadcastReceiver {
    private final int REFUSE_PERMISSION_FINISH = 911;
    private final int ACCEPT_PERMISSION_REQ = 912;
    private static final String TAG = "JPush";
    private String message = "";
    private String ordMessage = "您收到一条新的报价信息";
    private Context context;
    private Utils utils;
    private VolleyHttpClient httpClient;
    private Intent intent;


    static {
        System.loadLibrary("CARREPAIRSELLER");
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        utils = new Utils(context);
        httpClient = new VolleyHttpClient(context);
        this.intent = intent;

        Log.e("Receive", "onReceive");

        if (ContextCompat.checkSelfPermission(context,Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context,Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
            doPushMessage();
        }
        else {
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                Bundle bundle = intent.getExtras();
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Log.e(TAG, "极光ID+[MyReceiver] 接收Registration Id : " + regId);
                MyApplication.getInstance().registrationId = regId;
//                reqSetPushID(regId);
            }
        }
    }

    /**
     * 处理推送消息
     */
    private void doPushMessage() {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.e(TAG, "极光ID+[MyReceiver] 接收Registration Id : " + regId);
            MyApplication.getInstance().registrationId = regId;
            reqSetPushID(regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            processCustomMessage(context, bundle);
            int oldMessage = utils.getOrderMessage();

            message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            if (message != null) {
                utils.setOrderMessage(oldMessage + 1);

                Log.e(TAG, message);
                Intent intentMessage = new Intent();
                intentMessage.putExtra("message", message);
                intentMessage.setAction("receivemessage");
                context.sendBroadcast(intentMessage);
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 用户点击打开了通知");

            //打开自定义的Activity
//            Intent i = new Intent(context, TestActivity.class);
//            i.putExtras(bundle);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//            context.startActivity(i);
//            if (message.equals(ordMessage)) {
//                Constant.initFragment=4;
//            }
            Log.e("Constant.userid", "" + Constant.userid);
            if (Constant.userid == null || Constant.userid.equals("")) {
                Intent toIntentLogin = new Intent(context, LoginActivity.class);
                toIntentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(toIntentLogin);

            } else {
                Intent toIntentMain = new Intent(context, MainActivity.class);  //自定义打开的界面
                toIntentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(toIntentMain);
            }


        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }
    /**
     * 请求极光ID接口
     */
    private void reqSetPushID(String regid) {
        String url = context.getResources().getString(R.string.api_baseurl) + "order/SaveJpush.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + regid);

        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                Log.e(TAG, "极光记录成功");
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                Log.e(TAG, "极光记录 " + errorcode);

            }

            @Override
            public void onRequestError(int code, String msg) {
            }
        });
    }

}
