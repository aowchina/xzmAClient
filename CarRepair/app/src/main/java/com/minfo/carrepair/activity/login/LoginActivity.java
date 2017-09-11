package com.minfo.carrepair.activity.login;

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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.CMethodUtil;
import com.hyphenate.easeui.utils.EaseDialogUtil;
import com.hyphenate.easeui.utils.EnsureCancleInterface;
import com.minfo.carrepair.CommonInterface.PermissionListener;
import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.activity.MainActivity;
import com.minfo.carrepair.dialog.TipView;
import com.minfo.carrepair.dialog.login.DsfLogin;
import com.minfo.carrepair.entity.LoginEntity;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.AppManager;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.ConstantUrl;
import com.minfo.carrepair.utils.DialogUtil;
import com.minfo.carrepair.utils.Emoji;
import com.minfo.carrepair.utils.LG;
import com.minfo.carrepair.utils.MyCheck;
import com.minfo.carrepair.utils.ToastUtils;
import com.mob.MobSDK;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends BaseActivity implements View.OnClickListener, PlatformActionListener {
    private static final int SSO_COMPLETE = 0;
    private static final int SSO_CANCLE = 1;
    private static final int SSO_ERROR = 2;

    private TextView tv_login, tvCode;
    private Button login_btn;

    private TextView registered;

    private boolean isExit = false;
    private String usernameStr = "", passwordStr = "";
    private Map<String, String> params;

    private int userid = 0;
    private EditText etName, etPwd;
    private LinearLayout dsfLogin;

    //登录方式
    private boolean pwdFlag = true;// 密码  true   验证码false
    private ImageView ivCode;

    private boolean isClick = false;
    private PlatformDb platformDb;
    private boolean isComplete;
    private String unionid;
    private LoginEntity loginEntity;
    private int LoginFlag = 2;//1 init 2 login 3 dsf
    private boolean isLogHuanxin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_login);
        MobSDK.init(this);
        if(savedInstanceState != null) {
            checkAppStatus();
        }
    }

    @Override
    protected void findViews() {
        registered = (TextView) findViewById(R.id.tv_registered_btn);
        login_btn = (Button) findViewById(R.id.login_button);
        tv_login = (TextView) findViewById(R.id.tv_login);
        etName = ((EditText) findViewById(R.id.ed_username));
        etPwd = ((EditText) findViewById(R.id.ed_pwd));
        tvCode = (TextView) findViewById(R.id.tv_code);
        dsfLogin = ((LinearLayout) findViewById(R.id.ll_dsf_login));
        ivCode = ((ImageView) findViewById(R.id.iv_code));

        tvCode.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        registered.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        dsfLogin.setOnClickListener(this);


    }

    @Override
    protected void initViews() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
            if (isOnline()) {
                showWait();
                userid = utils.getUserid(LoginActivity.this);
                if (userid == 0) {
                    cleanWait();
                    Constant.userid = "";
                } else {
                    cleanWait();
                    Constant.userid = userid + "";

                    reqServer();
                }
            }
            return;
        }
        else {
            requestRunTimePermission(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS}
                    , new PermissionListener() {
                        @Override
                        public void onGranted() {

                            if (isOnline()) {
                                showWait();
                                userid = utils.getUserid(LoginActivity.this);
                                if (userid == 0) {
                                    cleanWait();
                                    Constant.userid = "";
                                } else {
                                    cleanWait();
                                    Constant.userid = userid + "";

                                    reqServer();
                                }
                            }
                        }

                        @Override
                        public void onGranted(List<String> grantedPermission) {

                        }

                        @Override
                        public void onDenied(List<String> deniedPermission) {
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
                cleanWait();

                if (response != null && !response.equals("")) {
                    LoginFlag = 1;
                    loginSuccess(response);
                } else {
//                    ToastUtils.show(LoginActivity.this, "服务器繁忙");
                }


            }


            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
//                ToastUtils.show(LoginActivity.this, msg);
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                Log.e(TAG, errorcode + "");
                if (errorcode != 0) {
//                    ToastUtils.show(LoginActivity.this, "服务器繁忙");
                }
            }
        });
    }

    /**
     * 请求密码登录接口
     */
    private void reqLogin() {
        String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_USER_LOGIN;
        try {
            params = utils.getParams(utils.getBasePostStr() + "*" + usernameStr + "*" + passwordStr);
            Log.e(TAG, params.toString());
        }
        catch (SecurityException e) {
            mHandler.sendEmptyMessage(120);
            return;
        }

        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                ToastUtils.show(LoginActivity.this, "登录成功");

                if (response != null && !response.equals("")) {
                    loginSuccess(response);
                } else {
                    ToastUtils.show(LoginActivity.this, "服务器繁忙");
                }
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                // 9 13 14 15 16 17(密码非法)  300 301
                int errorcode = response.getErrorcode();
                switch (errorcode) {
                    case 13:
                        ToastUtils.show(mActivity, "请输入正确的手机号");
                        break;
                    case 14:
                        ToastUtils.show(mActivity, "密码格式不对");
                        break;
                    case 15:
                        ToastUtils.show(mActivity, "账号异常");
                        break;
                    case 16:
                        ToastUtils.show(mActivity, "用户不存在");
                        break;
                    case 17:
                        ToastUtils.show(mActivity, "密码不对");
                        break;
                    case 49:
                        ToastUtils.show(mActivity, "验证码格式有误");
                        break;
                    case 50:
                    case 51:
                        ToastUtils.show(mActivity, "验证码不匹配");
                        break;
                    case 300:
                    case 301:
                    case 9:
                    default:
                        ToastUtils.show(mActivity, "服务器异常，请稍后再试");
                        LG.e("登录", "errorcode="+errorcode);
                        break;

                }

            }


            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity, "连接服务器失败，请稍后再试");
            }
        });
    }

    /**
     * 请求验证码登录接口
     */
    private void reqCodeLogin() {
        String url = getResources().getString(R.string.api_baseurl) + "user/LoginByCode.php";
        try {
            params = utils.getParams(utils.getBasePostStr() + "*" + usernameStr + "*" + passwordStr);
            Log.e(TAG, params.toString());
        }
        catch (SecurityException e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(120);
        }

        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                ToastUtils.show(LoginActivity.this, "登录成功");

                if (response != null && !response.equals("")) {
                    loginSuccess(response);
                } else {
                    ToastUtils.show(LoginActivity.this, "服务器繁忙");
                }
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                // 9 13 14 15 16 17(密码非法)  300 301
                int errorcode = response.getErrorcode();
                switch (errorcode) {
                    case 13:
                        ToastUtils.show(mActivity, "请输入正确的手机号");
                        break;
                    case 15:
                        ToastUtils.show(mActivity, "账号异常");
                        break;
                    case 16:
                        ToastUtils.show(mActivity, "用户不存在");
                        break;
                    case 49:
                        ToastUtils.show(mActivity, "验证码格式有误");
                        break;
                    case 50:
                    case 51:
                        ToastUtils.show(mActivity, "短信发送失败");
                        break;
                    case 300:
                    case 301:
                    case 9:
                    default:
                        ToastUtils.show(mActivity, "服务器异常，请稍后再试");
                        break;
                }

            }


            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
            }
        });
    }

    private String huanPsw;

    /**
     * 登录成功之后
     */
    public void loginSuccess(BaseResponse response) {
        cleanWait();
        LG.e("登录", "response=" + response);
        if (response.getData() != null && !response.getData().equals("")) {
            loginEntity = response.getObj(LoginEntity.class);
            if (loginEntity != null) {
                LG.e("登录", "response=" + loginEntity.toString());
                if(LoginFlag != 3) {
                    utils.setUserid(LoginActivity.this, Integer.parseInt(loginEntity.getUserid()));
                }
                utils.saveImg(loginEntity.getPicture());
                utils.saveName(loginEntity.getNickname());
                if (LoginFlag == 1) {
                    huanPsw = loginEntity.getTel();
                } else if (LoginFlag == 2) {
                    huanPsw = usernameStr;
                } else if (LoginFlag == 3) {
                    Constant.userid = loginEntity.getUserid();
                    huanPsw = platformDb.getUserId();
                    registerHx();
                    LG.e("登录", "userid=" + Constant.userid);
                    utils.intentTo(MainActivity.class);
                    loginHuan();
                    return;
                }

                Constant.userid = loginEntity.getUserid();
                LG.e("登录", "userid=" + Constant.userid);
                utils.intentTo(MainActivity.class);
                loginHuan();
            }


        } else {
            ToastUtils.show(mActivity, "服务器异常，请稍后再试");
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_registered_btn:
                utils.intentTo(RegisterActivity.class);
                break;

            case R.id.login_button:
                if (isOnline()) {
                    if (pwdFlag) {
                        if (checkInput()) {
                            showWait();

                            reqLogin();//请求手机号密码登录
                        }
                    } else {
                        if (checkPhoneCode()) {
                            showWait();

                            reqCodeLogin();//请求手机号验证码登录
                        }
                    }
                } else {
                    ToastUtils.show(this, "暂无网络,请重新连接");
                }
                break;
            case R.id.tv_login:
                if (!pwdFlag) {
                    pwdFlag = true;
                    tvCode.setVisibility(View.GONE);
                    tv_login.setText("验证码登录");
                    etPwd.setHint("请输入密码");
                    ivCode.setImageResource(R.mipmap.qixiu_mima);
                } else {
                    pwdFlag = false;
                    tvCode.setVisibility(View.VISIBLE);
                    ivCode.setImageResource(R.mipmap.skin_yanzhengma);

                    tv_login.setText("密码登录");
                    etPwd.setHint("请输入验证码");

                }
                break;
            case R.id.tv_code:
                if (checkPhone()) {
                    new CountDownTimer(60000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            tvCode.setText("" + millisUntilFinished / 1000 + "s");
                            tvCode.setEnabled(false);
                        }

                        public void onFinish() {
                            tvCode.setText("获取验证码");
                            tvCode.setEnabled(true);
                        }
                    }.start();
                    reqCode();
                }
                break;
            case R.id.ll_dsf_login:
                DsfLogin dsfLogin = new DsfLogin(this);
                dsfLogin.setOnChooseListener(new DsfLogin.OnChooseListener() {
                    @Override
                    public void choose(int payment) {
                        switch (payment) {
                            case DsfLogin.FLAG_ALIPAY:
                                logByWX();
                                break;

                        }
                    }
                });
                DialogUtil.showDialog(this, dsfLogin).show();

                break;
        }
    }

    /**
     * 微信账号登录
     *
     * @param
     */
    public void logByWX() {
        LG.e("第三方登录", "isClick=" + isClick);
        if (isClick) {
            return;
        }
        isClick = true;
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        platform.setPlatformActionListener(this);
        platform.showUser(null);
        ToastUtils.show(mActivity, "登录中...");
    }

    /**
     * 输入的基本验证
     */
    private boolean checkInput() {
        usernameStr = etName.getText().toString();
        passwordStr = etPwd.getText().toString();

        if (TextUtils.isEmpty(usernameStr)) {
            ToastUtils.show(this, "请输入用户名");
            return false;
        }
        if (TextUtils.isEmpty(passwordStr)) {
            ToastUtils.show(this, "请输入密码");
            return false;
        }
        if (!MyCheck.isTel(usernameStr)) {
            ToastUtils.show(this, "输入的用户名格式不正确");
            return false;
        }
        if (!MyCheck.isPsw(passwordStr)) {
            ToastUtils.show(this, "输入的密码格式不正确");
            return false;
        }
        return true;
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
    private boolean checkPhoneCode() {
        usernameStr = etName.getText().toString();
        passwordStr = etPwd.getText().toString();

        if (TextUtils.isEmpty(usernameStr)) {
            ToastUtils.show(this, "请输入手机号");
            return false;
        }

        if (!MyCheck.isTel(usernameStr)) {
            ToastUtils.show(this, "输入的手机号格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(passwordStr)) {
            ToastUtils.show(this, "请输入验证码");
            return false;
        }
        return true;
    }

    //back返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit) {
                //android.os.Process.killProcess(android.os.Process.myPid());
//                AppManager.getAppManager().AppExit(this);
                AppManager.getAppManager().removeAllActivity();
            } else {
                isExit = true;
                ToastUtils.show(this, "2秒内再按一次退出应用");
                Timer tExit = new Timer();
                tExit.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
            }
        }
        return false;
    }

    /**
     * 登录环信
     */
    private void loginHuan() {
        Constant.psw = huanPsw;
        EMClient.getInstance().login("sell" + Constant.userid, huanPsw, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.e(TAG, "login: onSuccess");

                isLogHuanxin = true;
                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
//                Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
//
//                // it's single chat
//                intent.putExtra(ChatConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
//
//                intent.putExtra(ChatConstant.EXTRA_USER_ID, HuanUtils.tochat);
////                intent.putExtra("userNickName", "nickname");
////                intent.putExtra("userHeadImage", "img");
//                Log.e("login: onSuccess", "login: onSuccess  "+HuanUtils.tochat);
//
//                startActivity(intent);

//                Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
//
//                intent.putExtra(Constant.EXTRA_USER_ID, "138127917161212");
//                startActivity(intent);
                finish();
                //LoginActivity.this.finish();
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.e(TAG, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                Log.e(TAG, "login: onError: " + code);
                if(!isLogHuanxin) {
                    isLogHuanxin = true;
                    registerHx();
                }
                else {
                    finish();
                }
            }
        });
    }

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
            mHandler.sendEmptyMessage(120);
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
                    case 15: // 电话打锁失败
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


    /**
     * 取消第三方登录
     *
     * @param platform
     * @param action
     */
    @Override
    public void onCancel(Platform platform, int action) {
        Message msg = new Message();
        msg.what = SSO_CANCLE;
        msg.arg1 = action;
        msg.obj = platform;
        mLogHandler.sendMessage(msg);
        LG.e("三方登录", platform.getName());
    }

    /**
     * 授权成功
     *
     * @param platform
     * @param action
     * @param res
     */
    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
        isComplete = true;
        Message msg = new Message();
        msg.what = SSO_COMPLETE;
        msg.arg1 = action;
        msg.obj = platform;
        try {
            unionid = (String) res.get("unionid");
        } catch (Exception e) {
            unionid = null;
            LG.i("unionid = null");
        }
        mLogHandler.sendMessage(msg);
        LG.e("三方登录", platform.getName());
        LG.i("111" + res);
        LG.i("222" + platform.getDb());
        LG.i("333" + platform.getDb().get("account"));
    }

    /**
     * 授权失败
     *
     * @param platform
     * @param action
     * @param error
     */
    @Override
    public void onError(Platform platform, int action, Throwable error) {
        Message msg = new Message();
        msg.what = SSO_ERROR;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = platform;
        Bundle data = new Bundle();
        if (error.toString().contains("NotExistException")) {
            data.putString("msg", "请先安装微信客户端");
        } else {
            data.putString("msg", "授权失败，请稍后再试");
        }
        msg.setData(data);
        mLogHandler.sendMessage(msg);
        LG.e("三方登录", platform.getName());
    }

    private Handler mLogHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Platform platform = (Platform) msg.obj;
            if (platform != null) {
                switch (msg.what) {
                    case SSO_COMPLETE:
                        platformDb = platform.getDb();
                        if (platformDb != null) {
                            loginother(platformDb);
                        } else {
                            ToastUtils.show(mActivity, "异常");
                        }
                        break;
                    case SSO_CANCLE:
                        ToastUtils.show(mActivity, "登录取消");
                        break;
                    case SSO_ERROR:
                        Bundle bundle = msg.getData();
                        String error = bundle.getString("msg");
                        if (!isComplete) {
                            ToastUtils.show(mActivity, "" + error);
                        }
                        break;
                    default:
                        break;
                }
            }
            isClick = false;
            super.handleMessage(msg);
        }

    };

    /**
     * 自动登录 第三方授权
     *
     * @param
     */
    private void loginother(PlatformDb platformDb) {

        String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_USER_LOGIN_OTHER;
        String name = Emoji.replaceInText(platformDb.getUserName(), true);
        if (TextUtils.isEmpty(name)) {
            name = "^_^";
        }
        try {
            params = utils.getParams(utils.getBasePostStr() + "*" + platformDb.getUserId() + "*" + 1 + "*" + utils.convertChinese(name) + "*" + platformDb.getUserIcon());
            LG.e(TAG, params.toString());
        }
        catch (SecurityException e) {
            mHandler.sendEmptyMessage(120);
            return;
        }

        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                ToastUtils.show(mActivity, "登录成功");
                LG.e(TAG, "onRequestSuccess  login " + response.getData() + " +" + response.toString());
                if (response != null && !response.equals("")) {
                    LoginFlag = 3;
                    loginSuccess(response);
                } else {
                    ToastUtils.show(mActivity, "服务器繁忙");
//                    Platform qqPlatform = ShareSDK.getPlatform(QQ.NAME);
                    Platform wxPlatform = ShareSDK.getPlatform(Wechat.NAME);
//                    Platform sinaPlatform = ShareSDK.getPlatform(SinaWeibo.NAME);
//                    sinaPlatform.removeAccount(true);
//                    qqPlatform.removeAccount(true);
                    wxPlatform.removeAccount(true);
                }
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                // 9,12(用户名过长),13（openid非法）,14(登录类型非法),15(用户打锁失败)，
                switch (response.getErrorcode()) {
                    // 9 11 13  300 301 302 303

                    case 13:
                        ToastUtils.show(mActivity, "用户名字过长");
                        break;
                    case 14:
                        ToastUtils.show(mActivity, "登录类型非法");
                        break;
                    case 9:
                    case 12:
                    default:
                        ToastUtils.show(mActivity, "登录失败");
                        break;
                }
//                Platform qqPlatform = ShareSDK.getPlatform(QQ.NAME);
                Platform wxPlatform = ShareSDK.getPlatform(Wechat.NAME);
//                Platform sinaPlatform = ShareSDK.getPlatform(SinaWeibo.NAME);
//                sinaPlatform.removeAccount(true);
//                qqPlatform.removeAccount(true);
                wxPlatform.removeAccount(true);
                isClick = false;
            }


            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                LG.e(TAG, "code  " + code + " ;  " + msg);
//                Platform qqPlatform = ShareSDK.getPlatform(QQ.NAME);
                Platform wxPlatform = ShareSDK.getPlatform(Wechat.NAME);
//                Platform sinaPlatform = ShareSDK.getPlatform(SinaWeibo.NAME);
//                sinaPlatform.removeAccount(true);
//                qqPlatform.removeAccount(true);
                wxPlatform.removeAccount(true);
                isClick = false;
            }
        });

    }
    private void registerHx() {
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    LG.e("环信注册", "sell"+Constant.userid+"pwd"+huanPsw);
//                    EMClient.getInstance().createAccount("sell"+Constant.userid, huanPsw);
//                    mHandler.sendEmptyMessage(121);
//                }
//                catch (Exception e) {
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
                case 120:
                    //权限被拒绝
                    TipView tipView = new TipView(mActivity);
                    tipView.setTitle("提示");
                    tipView.setMessage("无法获取您手机的识别码，去开启您的短信权限和电话权限？");
                    tipView.setOnClickListenerEnsure(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent= CMethodUtil.getAppDetailSettingIntent(mActivity);
                            startActivity(intent);
                        }
                    });
                    tipView.setOnClickListenerCancel(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                    DialogUtil.showSelfDialog(mActivity, tipView);
                    break;
            }
        }
    };
}
