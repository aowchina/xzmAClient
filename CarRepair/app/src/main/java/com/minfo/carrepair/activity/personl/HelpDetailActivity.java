package com.minfo.carrepair.activity.personl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.utils.ToastUtils;


/**
 * 帮助详情界面
 */
public class HelpDetailActivity extends BaseActivity implements View.OnClickListener{
    private ImageView ivBack; // 返回按钮
    private TextView tvTitle; // 标题
    private TextView tvContent; // 帮助内容
    private TextView tvQuestion; // 问题

    private String strQuestion; // 问题
    private String strAnswer; // 答案

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_help_detail);
//        setContentView(R.layout.activity_help_detail);
    }

    @Override
    protected void findViews() {
        Intent intent = getIntent();
        if(intent != null) {
            strQuestion = intent.getStringExtra("question");
            strAnswer = intent.getStringExtra("answer");
        }
        if(strQuestion == null || "".equals(strQuestion)) {
            ToastUtils.show(mActivity, "获取数据失败");
            return;
        }
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvContent = (TextView) findViewById(R.id.tv_detail);
        tvQuestion = (TextView) findViewById(R.id.tv_question);
    }

    @Override
    protected void initViews() {
        tvTitle.setText("帮助详情");

        ivBack.setOnClickListener(this);
        tvQuestion.setText(strQuestion);
        tvContent.setText(strAnswer);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
