package com.minfo.carrepairseller.activity.query;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.activity.login.LoginActivity;
import com.minfo.carrepairseller.adapter.BaseViewHolder;
import com.minfo.carrepairseller.adapter.CommonAdapter;
import com.minfo.carrepairseller.entity.query.ChexingItem;
import com.minfo.carrepairseller.entity.query.EPCDetailItem;
import com.minfo.carrepairseller.entity.query.EPCItem;
import com.minfo.carrepairseller.entity.query.EPCTop;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ConstantUrl;
import com.minfo.carrepairseller.utils.LG;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.minfo.carrepairseller.utils.UniversalImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * EPC详情
 */
public class EPCDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView ivBack, ivEPC;
    private ListView lvOEM;
    private CommonAdapter<EPCDetailItem> commonAdapter;//产品
    private List<EPCDetailItem> dataList = new ArrayList<>();
//    private String strEpcId;
    private EPCItem item;
    private ChexingItem itemChexing; // 车型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_epcdetail);
    }

    @Override
    protected void findViews() {
        Intent intent = getIntent();
        if(intent.getSerializableExtra("epc") != null) {
            item = (EPCItem) intent.getSerializableExtra("epc");
            itemChexing = (ChexingItem) intent.getSerializableExtra(Constant.CAR_TYPE);
        }

        if(item == null) {
            ToastUtils.show(mActivity, "获取数据失败");
            finish();
            return;
        }
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        View viewTop = LayoutInflater.from(mActivity).inflate(R.layout.activity_epcdetail_top, null);
        ivEPC = ((ImageView) viewTop.findViewById(R.id.iv_epc));
        lvOEM = ((ListView) findViewById(R.id.lv_oem));
        lvOEM.addHeaderView(viewTop);
    }

    @Override
    protected void initViews() {
        tvTitle.setText(item.getName());
        ivBack.setOnClickListener(this);
//        for (int i = 0; i < 3; i++) {
//            dataList.add(new EPCEntity());
//        }
        UniversalImageUtils.displayImageUseDefOptions(item.getUrl(), ivEPC);

        commonAdapter = new CommonAdapter<EPCDetailItem>(this, dataList, R.layout.item_epc_detail_oem) {
            @Override
            public void convert(BaseViewHolder helper, EPCDetailItem item, int position) {
                helper.setText(R.id.tv_position, item.getPosition());
                helper.setText(R.id.tv_name, item.getName());
                helper.setText(R.id.tv_oem, item.getOem());
            }
        };
        lvOEM.setAdapter(commonAdapter);
        lvOEM.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {
                    Intent intent = new Intent(EPCDetailActivity.this, OEMDetailActivity.class);
                    intent.putExtra("epcid", item.getId());
                    intent.putExtra("oemid", dataList.get(position-1).getId());
                    intent.putExtra("title", dataList.get(position-1).getName());
                    intent.putExtra(Constant.CAR_TYPE, itemChexing);
                    startActivity(intent);
                }
            }
        });
        if(utils.isOnLine()) {
            reqData();
        }
        else {
            ToastUtils.show(mActivity, R.string.no_internet);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

        }
    }

    /**
     * 数据
     */
    private void reqData() {
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + item.getId());

        final String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_QUERY_EPC_DETAIL;
        httpClient.post(url, params, new RequestListener() {
            @Override public void onPreRequest() {

            }


            @Override public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if(response != null) {
//                    dList.clear();
//                    leftList.clear();
//                    epcItems.clear();
                    LG.e("EPC详情", "response="+response);
                    List<EPCDetailItem> list = response.getList(EPCDetailItem.class);
                    if(list != null && list.size() > 0) {
                        dataList.addAll(list);
                    }
                    else {
                        LG.e("EPC列表右边", "无数据");
                        ToastUtils.show(mActivity, "没有该车型下的数据");
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
                // 12,19,20
                int errorcode = response.getErrorcode();
                switch (errorcode) {
                    case 12:
                        ToastUtils.show(mActivity,"账号异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 19:
                    case 20:
                    case 22:
                        ToastUtils.show(mActivity,"此epc结构图详情暂未发布");
                        break;
                    default:
                        ToastUtils.show(mActivity,"服务器繁忙");
                        break;
                }

            }
        });
    }
}
