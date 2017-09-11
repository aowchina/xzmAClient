package com.minfo.carrepair.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.publish.AskToBuyActivity;
import com.minfo.carrepair.activity.publish.PartsPublishActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PublishFragment extends BaseFragment implements View.OnClickListener{
    ImageView ivPublish; // 发布安钮
    ImageView ivBuy; // 求购按钮

    public PublishFragment() {
        // Required empty public constructor
    }

    /**
     * 初始化布局
     * @return
     */
    @Override
    protected View initViews() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_publish, null);
        ivPublish = (ImageView) view.findViewById(R.id.iv_seller);
        ivBuy = (ImageView) view.findViewById(R.id.iv_buyer);

        ivPublish.setOnClickListener(this);
        ivBuy.setOnClickListener(this);
        return view;
    }


    /**
     * 按钮点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_seller:
                mActivity.startActivity(new Intent(mActivity, PartsPublishActivity.class));
                break;
            case R.id.iv_buyer:
                mActivity.startActivity(new Intent(mActivity, AskToBuyActivity.class));
                break;
        }
    }
}
