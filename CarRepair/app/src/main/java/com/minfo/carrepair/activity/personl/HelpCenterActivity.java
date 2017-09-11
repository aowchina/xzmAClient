package com.minfo.carrepair.activity.personl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.activity.login.LoginActivity;
import com.minfo.carrepair.adapter.BaseViewHolder;
import com.minfo.carrepair.adapter.CommonAdapter;
import com.minfo.carrepair.entity.personl.HelpItem;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.ConstantUrl;
import com.minfo.carrepair.utils.LG;
import com.minfo.carrepair.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 帮助中心界面
 */
public class HelpCenterActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack; // 返回按钮
    private TextView tvTitle; // 标题


    private ListView listView;

    private List<HelpItem> list = new ArrayList<>();
    private CommonAdapter<HelpItem> commonAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_help_center);
//        setContentView(R.layout.activity_help_center);
    }

    @Override
    protected void findViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        listView = (ListView) findViewById(R.id.lv_help);

    }

    @Override
    protected void initViews() {
        tvTitle.setText("帮助中心");
        ivBack.setOnClickListener(this);

        commonAdapter = new CommonAdapter<HelpItem>(mActivity, list, R.layout.content_help_center) {
            @Override
            public void convert(BaseViewHolder helper, HelpItem item, int position) {
//                helper.setText(R.id.tv_week, item.getTime1());
//                helper.setText(R.id.tv_date, item.getTime2());
//                helper.setText(R.id.tv_tixian_to, item.getInfo());
//                helper.setText(R.id.tv_money, item.getMoney());
                helper.setText(R.id.tv_name, item.getName());

            }
        };
        listView.setAdapter(commonAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, HelpDetailActivity.class);
                intent.putExtra("question", list.get(position).getName());
                intent.putExtra("answer", list.get(position).getUrl());
                startActivity(intent);
            }
        });
//        initData();
        //reqData();
        if (isOnline()) {
            showWait();
            reqData();

        } else {
            ToastUtils.show(mActivity, "暂无网络,请重新连接");


        }
    }

    private void initData() {
        list.clear();
        for (int i = 0; i < 10; i++) {
            HelpItem item = new HelpItem();
            list.add(item);
        }

        commonAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }

    /**
     * 请求数据
     */
    private void reqData() {
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" +1);

        final String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_CENTER_TIAOWEN;
        httpClient.post(url, params, new RequestListener() {
            @Override public void onPreRequest() {

            }


            @Override public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if(response != null) {
                    list.clear();
                    LG.e("帮助中心", "response="+response);
                    List<HelpItem> items = response.getList(HelpItem.class);
                    if(items != null && items.size()>0) {
                        list.addAll(items);
                    }
                    else {
                        ToastUtils.show(mActivity, "未找到相关信息");
                    }
                    commonAdapter.notifyDataSetChanged();
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
                int errorcode = response.getErrorcode();
                // 11，12，19,23，24，25
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
