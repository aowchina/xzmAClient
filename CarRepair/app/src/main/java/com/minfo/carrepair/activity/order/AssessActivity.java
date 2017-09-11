package com.minfo.carrepair.activity.order;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.utils.ToastUtils;
import com.minfo.carrepair.widget.slefratingbar.XLHRatingBar;

public class AssessActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;

    private TextView tvDes;
    private TextView tvWl;
    private TextView tvFw;

    private XLHRatingBar rbDes;
    private XLHRatingBar rbWl;
    private XLHRatingBar rbFw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_assess);
//        setContentView(R.layout.activity_assess);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void findViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        rbDes = (XLHRatingBar) findViewById(R.id.rb_des);
        rbWl = (XLHRatingBar) findViewById(R.id.rb_wl);
        rbFw = (XLHRatingBar) findViewById(R.id.rb_fw);

        tvDes = (TextView) findViewById(R.id.tv_des);
        tvWl = (TextView) findViewById(R.id.tv_wl);
        tvFw = (TextView) findViewById(R.id.tv_fw);

    }

    @Override
    protected void initViews() {
        ivBack.setOnClickListener(this);
        tvTitle.setText("发表评论");

        rbDes.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                ToastUtils.show(mActivity, "countSelected="+countSelected);
                tvDes.setText(countSelected + "");
            }
        });
        rbWl.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                ToastUtils.show(mActivity, "countSelected="+countSelected);
                tvWl.setText(countSelected + "");
            }
        });
        rbFw.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                ToastUtils.show(mActivity, "countSelected="+countSelected);
                tvFw.setText(countSelected + "");
            }
        });
    }
}
