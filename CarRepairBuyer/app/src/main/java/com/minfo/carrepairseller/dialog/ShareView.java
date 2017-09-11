package com.minfo.carrepairseller.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.entity.ShareInfo;
import com.minfo.carrepairseller.utils.MyCheck;
import com.minfo.carrepairseller.utils.ShareUtils;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by MinFo021 on 17/6/5.
 */

public class ShareView extends BaseDialogView implements View.OnClickListener{
    public static final int LOGIN_FRIEND = 0;
    public static final int LOGIN_WEIXIN = 1;
    public static final int LOGIN_QQ = 2;
    public static final int LOGIN_SINA = 3;
//    private LinearLayout llAlipay; // 支付宝按钮
//    private LinearLayout llWechat; // 微信按钮
    private LinearLayout llWechat;
    private LinearLayout llQQ;
    private LinearLayout llSina;
    private LinearLayout llFriend;

    private TextView tvCancle; // 取消

    private OnChooseListener chooseListener; // 自定义点击事件相应
    private String link= "";

    public ShareView(Context context) {
        super(context);
        initView();
    }

    public ShareView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        removeAllViews();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_share, null);

        llWechat = (LinearLayout) view.findViewById(R.id.ll_wx);
        llQQ = (LinearLayout) view.findViewById(R.id.ll_qq);
        llSina = (LinearLayout) view.findViewById(R.id.ll_sina);
        llFriend = (LinearLayout) view.findViewById(R.id.ll_friend);

        tvCancle = (TextView) view.findViewById(R.id.tv_cancel);

        llQQ.setOnClickListener(this);
        llWechat.setOnClickListener(this);
        llSina.setOnClickListener(this);
        llFriend.setOnClickListener(this);
        tvCancle.setOnClickListener(this);
        addView(view);
    }

    @Override
    public void onClick(View v) {
        ShareInfo info = new ShareInfo();
        String url = MyCheck.isEmpty(link) ? "https://www.baidu.com/" : link;

//        info.setContent("心之盟下载链接\n"+url);
        info.setContent(""+url);
        info.setTitle("心之盟下载链接");
        info.setTitleUrl(url);
        info.setContentUrl(url);
        switch (v.getId()) {
            case R.id.ll_wx:
                if(chooseListener != null) {
                    chooseListener.choose(LOGIN_WEIXIN);
                }
                ShareUtils.showShare(context, info, Wechat.NAME);
                break;
            case R.id.ll_qq:
                if(chooseListener != null) {
                    chooseListener.choose(LOGIN_QQ);
                }
                ShareUtils.showShare(context, info, QQ.NAME);
                break;
            case R.id.ll_sina:
                if(chooseListener != null) {
                    chooseListener.choose(LOGIN_SINA);
                }
                ShareUtils.showShare(context, info, SinaWeibo.NAME);
                break;
            case R.id.ll_friend:
                if(chooseListener != null) {
                    chooseListener.choose(LOGIN_FRIEND);
                }
                ShareUtils.showShare(context, info, WechatMoments.NAME);
                break;
            case R.id.tv_cancel:
                break;
        }

        if(mExitDialog != null) {
            mExitDialog.exitDialog();
        }
    }

    public void setOnChooseListener(OnChooseListener chooseListener) {
        this.chooseListener = chooseListener;
    }

    /**
     * 设置传值
     * @param url
     */
    public void setData(String url) {
        link = url;
    }
    /**
     * 点击支付方式相应事件
     */
    public interface OnChooseListener {
        public void choose(int logType);
    }
}
