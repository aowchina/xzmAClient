package com.minfo.carrepair.activity.publish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.activity.query.ChoseCarActivity;
import com.minfo.carrepair.utils.ToastUtils;

/**
 * 配件求购界面
 *
 */
public class AskToBuyActivity extends BaseActivity implements View.OnClickListener{
    private static final int FOR_RESULT_FLAG = 120;
    private ImageView ivBack; // 返回按钮
    private TextView tvTitle; // 标题

    private LinearLayout llManual; // 手动选择
    private TextView tvVin; // vin匹配

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_ask_to_buy);
//        setContentView(R.layout.activity_ask_to_buy);
    }

    @Override
    protected void findViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        llManual = (LinearLayout) findViewById(R.id.ll_manual);
        tvVin = (TextView) findViewById(R.id.tv_vin);

    }

    @Override
    protected void initViews() {
        tvTitle.setText("配件求购");
        ivBack.setOnClickListener(this);

        llManual.setOnClickListener(this);
        tvVin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_vin:
                startActivity(new Intent(mActivity, VinSearchActivity.class));
                break;
            case R.id.ll_manual:
                startActivityForResult(new Intent(mActivity, ChoseCarActivity.class), FOR_RESULT_FLAG);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FOR_RESULT_FLAG:
                if(resultCode == RESULT_OK) {
                    ToastUtils.show(mActivity, "处理返回结果");
                }
                else {
                    ToastUtils.show(mActivity, "不处理返回结果");
                }
                break;
        }
    }
}
