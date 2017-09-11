package com.minfo.carrepairseller.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.carrepairseller.R;

/**
 * Created by MinFo021 on 17/4/11.
 * 选择支付方式
 */

public class ChoosePaymentView extends BaseDialogView implements View.OnClickListener{
    public static final int FLAG_ALIPAY = 0; // 支付宝支付
    public static final int FLAG_YUE = 1; // 微信支付
    public static final int FLAG_WECHAT = 2; // 微信支付

    private LinearLayout llAlipay; // 支付宝按钮
    private LinearLayout llYue; // 余额按钮
    private LinearLayout llWechat; // 微信按钮
    private TextView tvCancle; // 取消

    private OnChooseListener chooseListener; // 自定义点击事件相应

    public ChoosePaymentView(Context context) {
        super(context);
        initView();
    }

    public ChoosePaymentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        removeAllViews();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_payment_choose_bw, null);
        llAlipay = (LinearLayout) view.findViewById(R.id.ll_alipay);
        llYue = (LinearLayout) view.findViewById(R.id.ll_yue);
        llWechat = (LinearLayout) view.findViewById(R.id.ll_wechat);
        tvCancle = (TextView) view.findViewById(R.id.tv_cancel);

        llAlipay.setOnClickListener(this);
        llYue.setOnClickListener(this);
        llWechat.setOnClickListener(this);
        tvCancle.setOnClickListener(this);
        addView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_alipay:
                if(chooseListener != null) {
                    chooseListener.choose(FLAG_ALIPAY);
                }
                break;
            case R.id.ll_wechat:
                if(chooseListener != null) {
                    chooseListener.choose(FLAG_WECHAT);
                }
                break;
            case R.id.ll_yue:
                if(chooseListener != null) {
                    chooseListener.choose(FLAG_YUE);
                }
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
     * 点击支付方式相应事件
     */
    public interface OnChooseListener {
        public void choose(int payment);
    }
}
