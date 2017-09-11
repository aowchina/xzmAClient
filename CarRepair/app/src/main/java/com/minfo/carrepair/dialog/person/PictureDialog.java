package com.minfo.carrepair.dialog.person;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.dialog.BaseDialogView;

/**
 * Created by min-fo-012 on 17/5/20.
 */
public class PictureDialog extends BaseDialogView implements View.OnClickListener {
    private OnChooseListener chooseListener; // 自定义点击事件相应
    public static final int FLAG_ALBUME = 0; // 相册
    public static final int FLAG_CAMARE = 1; // 相ji
    private TextView tvAlbume,tvCamare;

    public PictureDialog(Context context) {
        super(context);
        initView();

    }

    public PictureDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_picture, null);

        tvAlbume = (TextView) view.findViewById(R.id.tv_album);
        tvCamare = (TextView) view.findViewById(R.id.tv_camare);


        tvAlbume.setOnClickListener(this);
        tvCamare.setOnClickListener(this);
        addView(view);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_camare:
                if (chooseListener != null) {
                    chooseListener.choose(FLAG_CAMARE);
                }

                break;
            case R.id.tv_album:
                if (chooseListener != null) {
                    chooseListener.choose(FLAG_ALBUME);
                }

                break;

        }
        if (mExitDialog != null) {
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