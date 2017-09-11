package com.minfo.carrepair.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.utils.ToastUtils;


/**
 * 提现金额输入弹框
 * Created by MinFo021 on 17/3/9.
 */

public class EditMoneyView extends BaseDialogView{
    private TextView mTextEnsure, mTextCancel;
    private EditText etMoney; // 金额输入框
    private EditText etAlipay; // 输入支付宝账号

    //	private LinearLayout mLayoutEnsure;
    private OnClickListener  mClickListenerCancle; // 取消按钮事件
    private OnClickEnsureListener mClickListenerEnsure; // 确定按钮相应事件
    private OnClickEnsureListener2 mClickListenerEnsure2; // 确定按钮相应事件 支付宝
    private float money;
    private boolean isAlipay = false;




    public EditMoneyView(Context context) {
        super(context);
        initView();
    }
    public EditMoneyView(Context context, float money) {
        super(context);
        this.money = money;
        initView();
    }
    public EditMoneyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    public EditMoneyView(Context context, AttributeSet attrs, Boolean isLong) {
        super(context, attrs);

        initView();
    }

    private void initView() {
        removeAllViews();
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(context)
                .inflate(R.layout.dialog_edit_money, this, false);
        etMoney = (EditText) layout.findViewById(R.id.et_money);
        etAlipay = (EditText) layout.findViewById(R.id.et_alipay);
        mTextEnsure = (TextView) layout.findViewById(R.id.tv_ensure);
        mTextCancel = (TextView) layout.findViewById(R.id.tv_cancle);
        mTextEnsure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				ToastUtils.showShort(context, "功能待开放");
                String m = etMoney.getText().toString();
                if(m == null || m.equals("")) {
                    ToastUtils.show(context, "请输入提现金额");
                    return;
                }
                float money1 = Float.valueOf(m);
                if(money1 <= 0) {
                    ToastUtils.show(context, "请输入正确的金额");
                    return;
                }
                if(money > 0 && money1 > money) {
                    ToastUtils.show(context, "超过提现金额上线");
                    return;
                }
                String alipay = etAlipay.getText().toString();
                if(isAlipay) { // 提付宝提现
                    if(alipay == null || alipay.equals("")) {
                        ToastUtils.show(context, "请输入您的支付宝账号");
                        return;
                    }
                    if (mClickListenerEnsure2 != null) {
                        mClickListenerEnsure2.ensure(alipay, money1);
                    }
                }
                else {
                    if (mClickListenerEnsure != null) {
                        mClickListenerEnsure.ensure(money1);
                    }
                }
                if (mExitDialog != null) {
                    mExitDialog.exitDialog();
                }
            }
        });

        mTextCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mClickListenerCancle != null) {
                    mClickListenerCancle.onClick(v);
                }
                if(mExitDialog != null) {
                    mExitDialog.exitDialog();
                }
            }
        });
        this.addView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    /**
     * 确定按钮点击事件
     */
    public EditMoneyView setOnClickEnsureListener(OnClickEnsureListener clickListener) {
        mClickListenerEnsure = clickListener;

        return this;

    }
    /**
     * 确定按钮点击事件
     *
     */
    public EditMoneyView setOnClickEnsureListener(OnClickEnsureListener2 clickListener) {
        mClickListenerEnsure2 = clickListener;

        return this;

    }

    /**
     * 取消按钮点击事件
     */
    public EditMoneyView setOnClickListenerCancle(OnClickListener clickListener) {
        mClickListenerCancle = clickListener;
        return this;
    }

    /**
     * 确定按钮自定义相应接口
     * money 输入框的钱数
     */
    public interface OnClickEnsureListener {
        public void ensure(float money);
    }

    public interface OnClickEnsureListener2 {
        public void ensure(String alipay, float money);
    }

    /**
     * 设置支付宝输入框可见
     */
    public void setEtAlipayVisiable(boolean isShow) {
        isAlipay = isShow;
        if(isShow) {
            etAlipay.setVisibility(VISIBLE);
        }
        else{
            etAlipay.setVisibility(GONE);
        }
        invalidate();
    }
}
