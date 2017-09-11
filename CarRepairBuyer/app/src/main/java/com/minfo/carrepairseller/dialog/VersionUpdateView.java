package com.minfo.carrepairseller.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.minfo.carrepairseller.R;


/**
 * Created by MinFo021 on 17/4/13.
 */

public class VersionUpdateView extends BaseDialogView implements View.OnClickListener{
    private TextView tvMessage0;
    private TextView tvMessage;
    private TextView tvUpdate;
    private TextView tvCancel;

    private String message0 = ""; // 最新版本
    private String message = ""; // 当前版本

    private OnDownListener onDownListener;

    public VersionUpdateView(Context context) {
        super(context);
        initView();
    }

    public VersionUpdateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    private void initView() {
        removeAllViews();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update_bw, null);

        tvUpdate = (TextView) view.findViewById(R.id.tv_update);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvMessage0 = (TextView) view.findViewById(R.id.tv_message0);
        tvMessage = (TextView) view.findViewById(R.id.tv_message);
//        tvMessage.setText(message);

        tvUpdate.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        addView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:

                break;
            case R.id.tv_update:
                if(onDownListener != null) {
                    onDownListener.down();
                }
                break;

        }
        if(mExitDialog != null) {
            mExitDialog.exitDialog();
        }
    }

    public VersionUpdateView setVersionMessage(String current, String old) {
        message0 = current;
        message = old;
        tvMessage0.setText("最新版本: " + message0);
        tvMessage.setText("当前版本: " + message);
        invalidate();
        return this;
    }

    /**
     * 设置下载监听
     * @param onDownListener
     */
    public VersionUpdateView setOnDownListener(OnDownListener onDownListener) {
        this.onDownListener = onDownListener;
        return this;
    }

    public interface OnDownListener {
        public void down();
    }
}
