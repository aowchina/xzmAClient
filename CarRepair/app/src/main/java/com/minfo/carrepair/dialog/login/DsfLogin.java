package com.minfo.carrepair.dialog.login;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.dialog.BaseDialogView;

/**
 * Created by min-fo-012 on 17/5/18.
 */
public class DsfLogin extends BaseDialogView implements View.OnClickListener{
    private ImageView ivDsfLogin;
    private OnChooseListener chooseListener; // 自定义点击事件相应
    public static final int FLAG_ALIPAY = 0; // 微信登录

    public DsfLogin(Context context) {
        super(context);
        initView();

    }

    public DsfLogin(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

    }
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_dsflogin, null);

        ivDsfLogin = (ImageView) view.findViewById(R.id.iv_dslogin);

        ivDsfLogin.setOnClickListener(this);
        addView(view);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_dslogin:
                if(chooseListener != null) {
                    chooseListener.choose(FLAG_ALIPAY);
                }

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
         void choose(int payment);
    }
}
