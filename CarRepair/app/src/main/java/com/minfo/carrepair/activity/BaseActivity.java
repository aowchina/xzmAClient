package com.minfo.carrepair.activity;

/**
 * Created by liujing on 15/9/6.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.minfo.carrepair.CommonInterface.PermissionListener;
import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.login.InitActivity;
import com.minfo.carrepair.http.VolleyHttpClient;
import com.minfo.carrepair.utils.AppManager;
import com.minfo.carrepair.utils.AppStatusManager;
import com.minfo.carrepair.utils.FindViewById;
import com.minfo.carrepair.utils.HdImg;
import com.minfo.carrepair.utils.HdUtils;
import com.minfo.carrepair.utils.Utils;
import com.minfo.carrepair.widget.LoadingDialog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * liujing
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected VolleyHttpClient httpClient;
    protected Utils utils;
    protected String TAG = "";
    protected LoadingDialog loading;
    protected Activity mActivity;
    protected AppManager appManager;
    protected int sw, sh;
    protected Context con;
    protected Activity act;

    protected HdImg mfi;
    protected HdUtils mfu;
    private PermissionListener mlistener;

//    static {
//        System.loadLibrary("BOILWORLD");
//    }

    protected void onCreate(Bundle savedInstanceState, int layoutResID) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        appManager = AppManager.getAppManager();
        appManager.addActivity(this);
        setContentView(layoutResID);
        mActivity = this;
        TAG = getClass().getSimpleName();
        utils = new Utils(this);
        loading = new LoadingDialog(this);
        initvars(this, this);
        byIdViews();
        findViews();
        initViews();

    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected void initvars(Context con, Activity act) {
        this.con = con;
        this.act = act;
        mfi = new HdImg(con);
        mfu = new HdUtils(con);

        httpClient = new VolleyHttpClient(con);
        loading = new LoadingDialog(con);

        sw = mfi.getScreenWidth();
        sh = mfi.getScreenHeight();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            RelativeLayout rlTitle = (RelativeLayout) findViewById(R.id.titles);
            if(rlTitle!=null) {
                rlTitle.setPadding(0, mfu.dip2px(this, 22), 0, 0);
            }
        }
    }

    private void byIdViews() {
        Field[] fields = getClass().getDeclaredFields();
        if (null != fields && fields.length > 0) {
            for (Field field : fields) {
                if (field.isAnnotationPresent(FindViewById.class)) {
                    FindViewById injectView = field.getAnnotation(FindViewById.class);
                    int id = injectView.id();
                    if (id != FindViewById.DEFAULT_ID) {
                        try {
                            field.setAccessible(true);
                            field.set(this, findViewById(id));
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    String method = injectView.click();
                    if (!method.equals(FindViewById.DEFAULT_METHOD)) {
                        setViewClickListener(this, field, method);
                    }
                }
            }
        }
    }
    private void setViewClickListener(Object injectedSource, Field field, String clickMethod) {
        try {
            Object obj = field.get(injectedSource);
            if (obj instanceof View) {
                ((View) obj).setOnClickListener(new EventListener(injectedSource).click(clickMethod));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class EventListener implements View.OnClickListener {
        public Object obj;
        public String clickMethod;

        public EventListener(Object obj) {
            this.obj = obj;
        }
        public EventListener click(String clickMethod) {
            this.clickMethod = clickMethod;
            return this;
        }
        @Override
        public void onClick(View v) {
            invokeClickMethod(obj, clickMethod, v);
        }
        private Object invokeClickMethod(Object obj, String methodName, Object... params) {
            if (obj == null) {
                return null;
            }
            Method method = null;
            try {
                method = obj.getClass().getDeclaredMethod(methodName, View.class);
                if (method != null) {
                    method.setAccessible(true);
                    return method.invoke(obj, params);
                } else {
                    throw new Exception("no such method:" + methodName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    /**
     * 获取布局控件
     */
    protected abstract void findViews();

    /**
     * 初始化view的一些数据
     */
    protected abstract void initViews();

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
    /**
     * 是否联网
     *
     * @return
     */
    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }
    /**
     * 清除等待框
     * author wangrui
     */
    protected void cleanWait() {
        if(!mActivity.isFinishing()) {
            loading.dismiss();
        }
    }
    /**
     * 显示等待框
     * author wangrui
     */
    protected void showWait() {

//        loading.setMessage("加载中.....");
        loading.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appManager.removeActivity(this);
    }

    /**
     * 权限申请
     * @param permissions 待申请的权限集合
     * @param listener  申请结果监听事件
     */
    protected void requestRunTimePermission(String[] permissions,PermissionListener listener){
        this.mlistener = listener;

        //用于存放为授权的权限
        List<String> permissionList = new ArrayList<>();
        //遍历传递过来的权限集合
        for (String permission : permissions) {
            //判断是否已经授权
            if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                //未授权，则加入待授权的权限集合中
                permissionList.add(permission);
            }
        }

        //判断集合
        if (!permissionList.isEmpty()){  //如果集合不为空，则需要去授权
            ActivityCompat.requestPermissions(this,permissionList.toArray(new String[permissionList.size()]),1);
        }else{  //为空，则已经全部授权
            listener.onGranted();
        }
    }

    /**
     * 权限申请结果
     * @param requestCode  请求码
     * @param permissions  所有的权限集合
     * @param grantResults 授权结果集合
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0){
                    //被用户拒绝的权限集合
                    List<String> deniedPermissions = new ArrayList<>();
                    //用户通过的权限集合
                    List<String> grantedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        //获取授权结果，这是一个int类型的值
                        int grantResult = grantResults[i];

                        if (grantResult == PackageManager.PERMISSION_GRANTED){ //用户拒绝授权的权限
                            //用户同意的权限
                            String permission = permissions[i];
                            grantedPermissions.add(permission);
                        }else{
                            String permission = permissions[i];
                            deniedPermissions.add(permission);

                        }
                    }

                    if (deniedPermissions.isEmpty()){  //用户拒绝权限为空
                        mlistener.onGranted();
                    }else {  //不为空
                        //回调授权成功的接口
                        mlistener.onDenied(deniedPermissions);
                        //回调授权失败的接口
                        mlistener.onGranted(grantedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }

    protected void checkAppStatus() {
        if(AppStatusManager.getInstance().getAppStatus()==AppStatusManager.AppStatusConstant.APP_FORCE_KILLED) {
            //应用启动入口，走重启流程
            Intent intent = new Intent(this, InitActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
