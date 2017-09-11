package com.minfo.carrepairseller.activity.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.CMethodUtil;
import com.hyphenate.easeui.utils.EaseDialogUtil;
import com.hyphenate.easeui.utils.EnsureCancleInterface;
import com.minfo.carrepairseller.CommonInterface.PermissionListener;
import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.ConstantUrl;
import com.minfo.carrepairseller.utils.LG;
import com.minfo.carrepairseller.utils.MyCheck;
import com.minfo.carrepairseller.utils.ToastUtils;

import java.util.List;
import java.util.Map;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_login, tvCode;
    private Button btLogin;


    private boolean isExit = false;
    private String usernameStr = "", passwordStr = "", passwordAgainStr = "", codeStr = "";
    private Map<String, String> params;

    private int userid = 0;
    private EditText etName, etPwd, etCode, etPwdAgain;
    private LinearLayout dsfLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_register);
        if(savedInstanceState != null) {
            checkAppStatus();
        }
    }


    @Override
    protected void findViews() {
        btLogin = (Button) findViewById(R.id.bt_register);
        tv_login = (TextView) findViewById(R.id.tv_login);
        etName = ((EditText) findViewById(R.id.ed_username));
        etPwd = ((EditText) findViewById(R.id.ed_pwd));
        etCode = ((EditText) findViewById(R.id.ed_code));
        etPwdAgain = ((EditText) findViewById(R.id.ed_pwd_again));
        tvCode = (TextView) findViewById(R.id.tv_code);

        tvCode.setOnClickListener(this);
        btLogin.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }

    @Override
    protected void initViews() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
            LG.e("注册", "走了1");
        }
        else {
            requestRunTimePermission(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS}
                    , new PermissionListener() {
                        @Override
                        public void onGranted() {
                            LG.e("注册", "走了2");
                        }

                        @Override
                        public void onGranted(List<String> grantedPermission) {
                            LG.e("注册", "走了3");
                        }

                        @Override
                        public void onDenied(List<String> deniedPermission) {
                            LG.e("注册", "走了4");
                            for (String str : deniedPermission) {
                                if (str.equals(Manifest.permission.RECEIVE_SMS) || str.equals(Manifest.permission.READ_PHONE_STATE)) {
                                    //权限被拒绝
                                    EaseDialogUtil.showMsgDialog(mActivity, "提示", "无法获取您手机的识别码，请开启您的短信权限和电话权限", "去设置", "取消", new EnsureCancleInterface() {
                                        @Override
                                        public void ensure() {
                                            Intent intent= CMethodUtil.getAppDetailSettingIntent(mActivity);
                                            startActivity(intent);
                                        }
                                        @Override
                                        public void cancle() {
                                            finish();
                                        }
                                    });
                                    return;
                                }
                            }
                        }
                    });
        }
    }

    /**
     * 请求注册接口
     */
    private void reqRedister() {
        String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_USER_REGISTER;

        try {
            params = utils.getParams(utils.getBasePostStr() + "*" + usernameStr + "*" + passwordStr + "*" + passwordAgainStr + "*" + codeStr + "*" + 0);
        }
        catch (SecurityException e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(121);
            return;
        }


        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                ToastUtils.show(RegisterActivity.this, "注册成功");

                if (!response.equals("") && response != null) {
                    registerSuccess(response);
                } else {
                    ToastUtils.show(RegisterActivity.this, "服务器繁忙");

                }
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                // 9 13 14 18(两次密码不一致) 50(验证码非法) 51(验证码不匹配) 52(用户类型非法) 15 19 300 301 302
                switch (errorcode) {
                    case 9:
                        break;
                    case 13: // 电话号非法
                        ToastUtils.show(mActivity, "电话号码非法");
                        break;
                    case 15: // 电话打锁失败
                        ToastUtils.show(mActivity, "电话号码非法");
                        break;
                    case 18:
                        ToastUtils.show(mActivity, "两次密码不一致");
                        break;
                    case 19:
                        ToastUtils.show(mActivity, "你的手机已注册");
                        break;
                    case 50:
                    case 51:
                        ToastUtils.show(mActivity, "请输入有效的验证码");
                        break;
                    case 63:
                    case 631:
                        LG.e("注册", "errorcode="+errorcode);
                        ToastUtils.show(mActivity, "注册失败，请稍后再试");
                        break;
                    case 49:
                    case 300:
                    case 301:
                    default:
                        LG.e("注册", "errorcode="+errorcode);
                        ToastUtils.show(mActivity, "注册失败，请稍后再试");
                        break;

                }
            }


            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity, "服务器请求失败，请检查您的网络");
            }
        });
    }

    /**
     * 注册成功之后
     */
    public void registerSuccess(BaseResponse response) {

        cleanWait();
        //utils.ClearPageFlagToShared(con);

//        utils.setUserid(this, Integer.parseInt(response.getData()));
//        Constant.userid = response.getData();
        Log.e("registerSuccess",response.getData());
        ToastUtils.show(mActivity, "注册成功");
        utils.intentTo(LoginActivity.class);
//        RegisterActivity.this.finish();
        registerHx();

    }

    /**
     * 注册环信
     */
    private void registerHx() {
        RegisterActivity.this.finish();
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    EMClient.getInstance().createAccount("buy"+userid, usernameStr);
//                    mHandler.sendEmptyMessage(120);
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_register:

                if (isOnline()) {
                    if (checkInput()) {
                        reqRedister();
                    }
                } else {
                    ToastUtils.show(this, "暂无网络,请重新连接");
                }
                break;
            case R.id.tv_login:
                finish();
                break;
            case R.id.tv_code:

                if (checkPhone()) {
                    tvCode.setEnabled(false);
                    new CountDownTimer(60000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            tvCode.setText("" + millisUntilFinished / 1000 + "s");

                        }

                        public void onFinish() {
                            tvCode.setText("获取验证码");
                            tvCode.setEnabled(true);
                        }
                    }.start();
                    reqCode();
                }
                break;

        }
    }

    private boolean checkPhone() {
        usernameStr = etName.getText().toString();

        if (TextUtils.isEmpty(usernameStr)) {
            ToastUtils.show(this, "请输入手机号");
            return false;
        }

        if (!MyCheck.isTel(usernameStr)) {
            ToastUtils.show(this, "输入的手机号格式不正确");
            return false;
        }

        return true;
    }

    /**
     * 输入的基本验证
     */
    private boolean checkInput() {
        usernameStr = etName.getText().toString();
        passwordStr = etPwd.getText().toString();
        codeStr = etCode.getText().toString();
        passwordAgainStr = etPwdAgain.getText().toString();
        if (TextUtils.isEmpty(usernameStr)) {
            ToastUtils.show(this, "请输入手机号");
            return false;
        }
        if (!MyCheck.isTel(usernameStr)) {
            ToastUtils.show(this, "输入的手机号格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(codeStr)) {
            ToastUtils.show(this, "请输入验证码");
            return false;
        }
        if (!MyCheck.isPsw(codeStr)) {
            ToastUtils.show(this, "输入的验证码格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(passwordStr)) {
            ToastUtils.show(this, "请输入密码");
            return false;
        }
        if (!MyCheck.isPsw(passwordStr)) {
            ToastUtils.show(this, "输入的密码格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(passwordAgainStr)) {
            ToastUtils.show(this, "请输入确认密码");
            return false;
        }

        if (!MyCheck.isPsw(passwordStr)) {
            ToastUtils.show(this, "输入的确认密码格式不正确");
            return false;
        }
        if (!passwordStr.equals(passwordAgainStr)) {
            ToastUtils.show(this, "两次密码不一样");
            return false;
        }
        return true;
    }

    // duanxin/SendCode.php

    /**
     * 获取验证码
     */
    private void reqCode() {
        Map<String, String> params;
        try {
            params = utils.getParams(utils.getBasePostStr() + "*" + usernameStr);//+ "*" + Constant.bangID
        }
        catch (SecurityException e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(121);
            return;
        }
        final String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_DUANXIN_GETCODE;
        Log.e("555", getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_DUANXIN_GETCODE);
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();

                int errorcode = response.getErrorcode();
                if (errorcode == 0) {
                    ToastUtils.show(mActivity, "验证码已发到你手机");
                }
//                ToastUtils.show(mActivity, "申请成功，请等待审核...");
//                utils.jumpTo(mActivity, MainActivity.class);
//                LG.e("注册", response.getData());
//                tvGetCode.setEnabled(true);
            }


            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                // 9 13 15 300 301 49(发送短信失败)
                switch (errorcode) {

                    case 13: // 电话号非法
                        ToastUtils.show(mActivity, "电话号码非法");
                        break;
                    case 49:
                        ToastUtils.show(mActivity, "短信发送失败");
                        break;
                    case 300:
                    case 301:
                    default:
                        ToastUtils.show(mActivity, "服务器请求失败");
                        break;

                }
                Log.e("k", errorcode + "");

            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 120:
                    RegisterActivity.this.finish();
                    break;
                case 121:
                    //权限被拒绝
                    EaseDialogUtil.showMsgDialog(mActivity, "提示", "无法获取您手机的识别码，请开启您的短信权限和电话权限", "去设置", "取消", new EnsureCancleInterface() {
                        @Override
                        public void ensure() {
                            Intent intent= CMethodUtil.getAppDetailSettingIntent(mActivity);
                            startActivity(intent);
//                            isSetting = true;
                        }
                        @Override
                        public void cancle() {
                            finish();
                        }
                    });
                    break;
            }
        }
    };
}
