package com.minfo.carrepairseller.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.http.VolleyHttpClient;
import com.minfo.carrepairseller.utils.AppManager;
import com.minfo.carrepairseller.utils.HdImg;
import com.minfo.carrepairseller.utils.HdUtils;
import com.minfo.carrepairseller.utils.Utils;
import com.minfo.carrepairseller.widget.LoadingDialog;


/**
 * Created by liujing on 15/9/8.
 */
public abstract class BaseFragment extends Fragment {

    public Activity mActivity;
    public LayoutInflater inflater;
    public VolleyHttpClient httpClient;
    public Utils utils;
    public String TAG;
    protected LoadingDialog loading;
    public AppManager mAppManager;
    public HdUtils hdUtils;
    public HdImg hdImg;
    /**
     * Fragment创建
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        inflater = LayoutInflater.from(mActivity);
        httpClient = new VolleyHttpClient(mActivity);
        TAG = getClass().getSimpleName();
        utils = new Utils(mActivity);
        mAppManager = AppManager.getAppManager();
        loading = new LoadingDialog(mActivity);
        hdUtils = new HdUtils(mActivity);
        hdImg = new HdImg(mActivity);
    }

    /**
     * 处理activity的布局
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initViews();
    }

    /**
     * 初始化布局
     * @return
     */
    protected abstract View initViews();

    /**
     * 依附的activity创建完成
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            RelativeLayout rlTitle = (RelativeLayout) view.findViewById(R.id.titles);
            if(rlTitle!=null) {
                rlTitle.setPadding(0, hdUtils.dip2px(mActivity, 22), 0, 0);
            }
        }
    }
    protected  void initData(){

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
    /**
     * 是否联网
     *
     * @return
     */
    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }
}
