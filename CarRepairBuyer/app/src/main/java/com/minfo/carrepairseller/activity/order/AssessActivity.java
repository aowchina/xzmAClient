package com.minfo.carrepairseller.activity.order;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.activity.login.LoginActivity;
import com.minfo.carrepairseller.entity.order.OrderDetailEntity;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.minfo.carrepairseller.widget.slefratingbar.XLHRatingBar;

import java.util.Map;

public class AssessActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;

    private TextView tvDes;
    private TextView tvWl;
    private TextView tvFw;

    private XLHRatingBar rbDes;
    private XLHRatingBar rbWl;
    private XLHRatingBar rbFw;
    private TextView tvSubmit;
    private EditText etContent;
    private String contentStr;//评价内容
    private OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
    private String describeStr="0";//描述评分
    private String serviceStr="0";//服务评分
    private String wlStr="0";//物流评分


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
            case R.id.tv_apply:
                if (isOnline()) {
                    if (checkInput()){

                        reqSubmitCommit();
                    }

                } else {
                    ToastUtils.show(this, "暂无网络,请重新连接");
                }
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
        tvSubmit = ((TextView) findViewById(R.id.tv_apply));
        etContent = ((EditText) findViewById(R.id.et_content));
    }

    @Override
    protected void initViews() {
        Bundle bundle = new Bundle();
        bundle = getIntent().getBundleExtra("info");
        if (bundle != null) {
            orderDetailEntity = (OrderDetailEntity) bundle.getSerializable("wlinfo");


        }
        ivBack.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        tvTitle.setText("发表评论");

        rbDes.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                tvDes.setText(countSelected + "");
                describeStr=countSelected + "";
            }
        });
        rbWl.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                tvWl.setText(countSelected + "");
                wlStr=countSelected + "";

            }
        });
        rbFw.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                tvFw.setText(countSelected + "");
                serviceStr=countSelected + "";
            }
        });
    }
    /**
     * @funcation 发表评论接口
     **/
    public void reqSubmitCommit() {
        String url = getResources().getString(R.string.api_baseurl) + "user/addPinjia.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" +orderDetailEntity.getGoods().get(0).getGoodid()+ "*" +describeStr+ "*" +wlStr+ "*" +serviceStr+ "*" +utils.convertChinese(contentStr)+ "*" +orderDetailEntity.getOrderid());
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                ToastUtils.show(mActivity, "评论发布成功");
                setResult(RESULT_OK);
                finish();

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if (errorcode == 12 ) {
                    ToastUtils.show(mActivity, "账号异常，请重新登录");
                    utils.jumpTo(mActivity, LoginActivity.class);
                }  else if (errorcode == 63) {
                    ToastUtils.show(mActivity, "描述分数不符合要求");
                }else if (errorcode == 64) {
                    ToastUtils.show(mActivity, "物流分数不符合要求");
                }else if (errorcode == 65) {
                    ToastUtils.show(mActivity, "服务分数不符合要求");
                }else if (errorcode == 66) {
                    ToastUtils.show(mActivity, "评价内容不能为空");
                }else {
                    ToastUtils.show(mActivity, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity, "连接服务器失败，请检查你的网络");
            }
        });
    }

    /**
     * 检查输入信息
     *
     * @return
     */
    private boolean checkInput() {
        contentStr = etContent.getText().toString().trim();
        if (TextUtils.isEmpty(contentStr)) {
            ToastUtils.show(this, "评论内容不能为空");
            return false;
        }
        if (describeStr.equals("0")){
            ToastUtils.show(this, "请选择描述评分");
            return false;
        }
        if (serviceStr.equals("0") ){
            ToastUtils.show(this, "请选择服务评分");
            return false;
        }
        if (wlStr.equals("0")){
            ToastUtils.show(this, "请选择物流评分");
            return false;
        }
        return true;
    }
}
