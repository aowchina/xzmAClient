package com.minfo.carrepair.activity.personl;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.activity.login.LoginActivity;
import com.minfo.carrepair.entity.personl.HelpItem;
import com.minfo.carrepair.entity.query.ChexingEntity;
import com.minfo.carrepair.entity.query.ChexingItem;
import com.minfo.carrepair.entity.query.ChexingModel;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.ConstantUrl;
import com.minfo.carrepair.utils.LG;
import com.minfo.carrepair.utils.ToastUtils;

import java.util.List;
import java.util.Map;

public class AboutUsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack; // 返回
    private TextView tvTitle; // 标题
    private TextView tvVersion; // 版本
    private TextView tvAbout; // 关于内容
    private HelpItem item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_about_us);
//        setContentView(R.layout.activity_about_us);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void findViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        tvAbout = (TextView) findViewById(R.id.tv_about);

    }

    @Override
    protected void initViews() {
        ivBack.setOnClickListener(this);
        tvTitle.setText("关于我们");

        reqData();
    }

    /**
     * 请求数据
     */
    private void reqData() {
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" +3);

        final String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_CENTER_TIAOWEN;
        httpClient.post(url, params, new RequestListener() {
            @Override public void onPreRequest() {

            }


            @Override public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if(response != null) {
                    LG.e("关于我们", "response="+response);
                    item = response.getObj(HelpItem.class);
                    //tvVersion.setText("版本: "+item.getNum());
                    tvAbout.setText(item.getUrl()+"");
                }
                else {
                    ToastUtils.show(mActivity, "服务器异常，请稍后再试");
                }
            }


            @Override public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity, msg);
            }


            @Override public void onRequestNoData(BaseResponse response) {
                cleanWait();
                // 11，12，19,23，24，25
                int errorcode = response.getErrorcode();
                switch (errorcode) {
                    case 11:
                    case 12:
                        ToastUtils.show(mActivity,"账号异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 23: // 帮助中心暂时没有数据
                        ToastUtils.show(mActivity,"帮助中心暂时没有数据");
                        break;
                    case 24: // 法律中心暂时没有数据
                        ToastUtils.show(mActivity,"法律中心暂时没有数据");
                        break;
                    case 25: // 关于我们暂时没有数据
                        ToastUtils.show(mActivity,"关于我们暂时没有数据");
                        break;
                    case 19:
                    default:
                        ToastUtils.show(mActivity,"服务器异常");
                        break;
                }

            }
        });
    }
}
