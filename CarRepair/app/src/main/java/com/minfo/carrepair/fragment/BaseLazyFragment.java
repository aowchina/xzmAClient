package com.minfo.carrepair.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minfo.carrepair.http.VolleyHttpClient;
import com.minfo.carrepair.utils.Utils;
import com.minfo.carrepair.widget.LoadingDialog;


/**
 * Created by liujing on 15/9/8.
 */
public abstract class BaseLazyFragment extends Fragment {

    public Activity mActivity;
    public LayoutInflater inflater;
    public VolleyHttpClient httpClient;
    public Utils utils;
    public String TAG;
    protected LoadingDialog loading;
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
        loading = new LoadingDialog(mActivity);
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

    protected  void initData(){

    }
    /**
     * 清除等待框
     * author wangrui
     */
    protected void cleanWait() {
        loading.dismiss();
    }
    /**
     * 显示等待框
     * author wangrui
     */
    protected void showWait() {
        loading.setMessage("加载中.....");
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

    protected boolean isVisible;
    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible(){
        lazyLoad();
    }

    protected abstract void lazyLoad();

    protected void onInvisible(){}
}
