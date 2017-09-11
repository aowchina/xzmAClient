package com.minfo.carrepairseller.activity.login;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.content.Intent;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.CMethodUtil;
import com.hyphenate.easeui.utils.EaseDialogUtil;
import com.hyphenate.easeui.utils.EnsureCancleInterface;
import com.minfo.carrepairseller.CommonInterface.PermissionListener;
import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.activity.MainActivity;
import com.minfo.carrepairseller.activity.MyApplication;
import com.minfo.carrepairseller.entity.LoginEntity;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.AppStatusManager;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.LG;
import com.minfo.carrepairseller.utils.MyCheck;
import com.minfo.carrepairseller.utils.ToastUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.jpush.android.api.JPushInterface;

public class InitActivity extends BaseActivity {
    private final int REFUSE_PERMISSION_FINISH = 119;
    private final int ACCEPT_PERMISSION_REQ = 120;
    private int userid = 0;
    private LoginEntity loginEntity;
    private boolean isLogHuanxin;
    private String huanPsw;
    String[] permissions = { /*Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECEIVE_SMS,
            Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION,*/
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS/*Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA*/};
    private boolean isSetting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_init);
//        setContentView(R.layout.activity_init);
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void initViews() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
            LG.e("init" , "走了允许1");
            reqDate();
            return;
        }
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
        singleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                requestRunTimePermission(permissions
                        , new PermissionListener() {
                            @Override
                            public void onGranted() {
//                                reqDate();
                                mHandler.sendEmptyMessage(ACCEPT_PERMISSION_REQ);
                                LG.e("init" , "走了允许2");
                            }

                            @Override
                            public void onGranted(List<String> grantedPermission) {
    //                        reqDate();
                            }

                            @Override
                            public void onDenied(List<String> deniedPermission) {
                                LG.e("init" , "走了");
                                for(String str : deniedPermission) {
                                    if(str.equals(Manifest.permission.RECEIVE_SMS)||str.equals(Manifest.permission.READ_PHONE_STATE)){
                                        mHandler.sendEmptyMessage(REFUSE_PERMISSION_FINISH);
//                                        ToastUtils.show(mActivity, "无法获取您手机的识别码，请开启短信（SMS）和手机电话权限");
//                                        finish();
                                        return;
                                    }
                                }
                                LG.e("init" , "走了允许3");
                                mHandler.sendEmptyMessage(ACCEPT_PERMISSION_REQ);
//                                reqDate();
                            }
                        });
            }
        });


    }

    /**
     * 请求数据
     */
    private void reqDate() {
        if (isOnline()) {
            userid = utils.getUserid(mActivity);
            if (userid == 0) {
                Constant.userid = "";
                utils.jumpTo(mActivity, LoginActivity.class);
            } else {
                Constant.userid = userid + "";
                reqServer();
            }
        }
        else {
            utils.jumpTo(mActivity, LoginActivity.class);
        }
    }
    /**
     * 请求init接口
     */
    private void reqServer() {
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + userid);

        final String url = getResources().getString(R.string.api_baseurl) + "user/Init.php";
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                int errorcode = response.getErrorcode();
                Log.e(TAG, errorcode + "");
                //保存用户信息,并跳转至主页面

                if (response != null && !response.equals("")) {
                    loginSuccess(response);
                } else {
                    utils.jumpTo(mActivity, LoginActivity.class);
                }


            }


            @Override
            public void onRequestError(int code, String msg) {
                utils.jumpTo(mActivity, LoginActivity.class);
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                Log.e(TAG, errorcode + "");

                utils.jumpTo(mActivity, LoginActivity.class);

            }
        });
    }

    /**
     * 登录成功之后
     */
    public void loginSuccess(BaseResponse response) {
        cleanWait();
        LG.e("登录", "response=" + response);
        if (response.getData() != null && !response.getData().equals("")) {
            loginEntity = response.getObj(LoginEntity.class);
            if (loginEntity != null) {
                huanPsw = loginEntity.getTel();
                Constant.userid = loginEntity.getUserid();
                LG.e("登录", "userid=" + Constant.userid);
                utils.saveUserid(loginEntity.getUserid());
                utils.intentTo(MainActivity.class);
                loginHuan();
            }
            else {
                utils.jumpTo(mActivity, LoginActivity.class);
            }

        } else {
            utils.jumpTo(mActivity, LoginActivity.class);
        }

    }

    /**
     * 登录环信
     */
    private void loginHuan() {
        Constant.psw = huanPsw;
        EMClient.getInstance().login("buy" + Constant.userid, huanPsw, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.e(TAG, "login: onSuccess");

                isLogHuanxin = true;
                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                finish();
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.e(TAG, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                Log.e(TAG, "login: onError: " + code);
                if (!isLogHuanxin) {
                    isLogHuanxin = true;
                    registerHx();
                    finish();
                }
                else {
                    finish();
                }

            }
        });
    }

    private void registerHx() {
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    LG.e("环信注册", "buy" + Constant.userid + "pwd" + huanPsw);
//                    EMClient.getInstance().createAccount("buy" + Constant.userid, huanPsw);
//                    mHandler.sendEmptyMessage(121);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    LG.e("环信注册", "注册失败！");
//                    mHandler.sendEmptyMessage(121);
//                }
//            }
//        }.start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 121:
                    loginHuan();
//                    LoginActivity.this.finish();
                    break;
                case REFUSE_PERMISSION_FINISH:
//                    ToastUtils.show(mActivity, "无法获取您手机的识别码，请开启您的短信权限和电话权限");
                    //权限被拒绝
                    EaseDialogUtil.showMsgDialog(mActivity, "提示", "无法获取您手机的识别码，请开启您的短信权限和电话权限", "去设置", "取消", new EnsureCancleInterface() {
                        @Override
                        public void ensure() {
                            Intent intent= CMethodUtil.getAppDetailSettingIntent(mActivity);
                            startActivity(intent);
                            isSetting = true;
                        }
                        @Override
                        public void cancle() {
                            finish();
                        }
                    });

                    break;
                case ACCEPT_PERMISSION_REQ:
                    if(!MyCheck.isEmpty(MyApplication.getInstance().registrationId)) {
                        Intent intent = new  Intent();
                        intent.setAction(JPushInterface.ACTION_REGISTRATION_ID);
                        Bundle bundle = new Bundle();
                        bundle.putString(JPushInterface.EXTRA_REGISTRATION_ID, MyApplication.getInstance().registrationId);
                        intent.putExtras(bundle);
                        sendBroadcast(intent);
                    }
                    reqDate();
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(isSetting) {
            isSetting = false;
            initViews();
        }
    }
}
