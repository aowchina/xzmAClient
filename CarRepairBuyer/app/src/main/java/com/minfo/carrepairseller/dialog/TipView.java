package com.minfo.carrepairseller.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.minfo.carrepairseller.R;


/**
 * Created by MinFo021 on 17/4/13.
 * 温馨提示对话框视图
 * 标题：温馨提示
 * 提示信息：message可设置
 * 提示按钮：确定、取消可设置
 * 确认事件可传递
 */

public class TipView extends BaseDialogView implements View.OnClickListener{
    private OnClickListener clickEnsure; // 确定时间
    private OnClickListener clickCancel; // 取消事件
    private TextView tvTitle;
    private TextView tvMessage;
    private TextView tvEnsure;
    private TextView tvCancel;

    public TipView(Context context) {
        super(context);
        initView();
    }

    public TipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    private void initView() {
        removeAllViews();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_tip_bw, null);

        tvEnsure = (TextView) view.findViewById(R.id.tv_ensure);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvMessage = (TextView) view.findViewById(R.id.tv_message);
//        tvMessage.setText(message);

        tvEnsure.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        addView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ensure:
                if(clickEnsure != null) {
                    clickEnsure.onClick(v);
                }
                break;
            case R.id.tv_cancel:
                if(clickCancel != null) {
                    clickCancel.onClick(v);
                }
                break;
        }
        if(mExitDialog != null) {
            mExitDialog.exitDialog();
        }
    }
    /**
     * 设置提示标题
     * @param message
     * @return
     */
    public TipView setTitle (String message) {
        if(message != null)
            tvMessage.setText(message);
        return this;
    }

    /**
     * 设置提示信息
     * @param message
     * @return
     */
    public TipView setMessage (String message) {
        if(message != null)
            tvMessage.setText(message);
        return this;
    }

    public TipView setButtonMessage (String ensure, String cancle) {
        if(ensure != null)
            tvEnsure.setText(ensure);
        if(cancle != null)
            tvCancel.setText(cancle);
        return this;
    }

    /**
     * 设置确认按钮点击事件
     * @param onClickListener
     * @return
     */
    public TipView setOnClickListenerEnsure(OnClickListener onClickListener) {
        clickEnsure = onClickListener;
        return this;
    }

    /**
     * 设置取消按钮点击事件
     * @param onClickListener
     * @return
     */
    public TipView setOnClickListenerCancel(OnClickListener onClickListener) {
        clickCancel = onClickListener;
        return this;
    }
}
