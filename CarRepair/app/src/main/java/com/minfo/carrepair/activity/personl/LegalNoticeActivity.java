package com.minfo.carrepair.activity.personl;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.activity.login.LoginActivity;
import com.minfo.carrepair.entity.personl.HelpItem;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.ConstantUrl;
import com.minfo.carrepair.utils.LG;
import com.minfo.carrepair.utils.ToastUtils;

import java.util.Map;

/**
 * 法律声明界面
 */
public class LegalNoticeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack; // 返回
    private TextView tvTitle; // 标题
    private TextView tvNotice; // 法律声明
    private HelpItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_legal_notice);
//        setContentView(R.layout.activity_legal_notice);
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
        tvNotice = (TextView) findViewById(R.id.tv_notice);

    }

    @Override
    protected void initViews() {
        ivBack.setOnClickListener(this);
        tvTitle.setText("法律声明");

        reqData();
    }

    /**
     * 请求数据
     */
    private void reqData() {
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" +2



        );

        final String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_CENTER_TIAOWEN;
        httpClient.post(url, params, new RequestListener() {
            @Override public void onPreRequest() {

            }


            @Override public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if(response != null) {
                    LG.e("法律声明", "response="+response);
                    item = response.getObj(HelpItem.class);
                    tvNotice.setText(item.getUrl()+"");
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
