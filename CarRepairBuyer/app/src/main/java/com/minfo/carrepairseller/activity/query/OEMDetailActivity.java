package com.minfo.carrepairseller.activity.query;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.activity.login.LoginActivity;
import com.minfo.carrepairseller.activity.publish.PartsPublishActivity;
import com.minfo.carrepairseller.adapter.BaseViewHolder;
import com.minfo.carrepairseller.adapter.CommonAdapter;
import com.minfo.carrepairseller.entity.query.ChexingItem;
import com.minfo.carrepairseller.entity.query.EPCDetailItem;
import com.minfo.carrepairseller.entity.query.EPCTop;
import com.minfo.carrepairseller.entity.query.OEMModel;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ConstantUrl;
import com.minfo.carrepairseller.utils.LG;
import com.minfo.carrepairseller.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OEMDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle, tvName, tvOEM, tvLocation, tvPrice, tvCount;
    private TextView tvFabu;
    private TextView tvQiugou;
    private ImageView ivBack, ivEPC;
    private ListView lvOEM;
    private List<EPCTop> dataList = new ArrayList<>();
    private CommonAdapter<EPCTop> commonAdapter;//产品
    private LinearLayout llContiner;
    private boolean flag;
    private String strOemId;
    private String strEpcId;
    private String strTitle;
    private EPCDetailItem item;
    private ChexingItem itemChexing; // 车型


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_oemdetail);
    }


    @Override
    protected void findViews() {
        Intent intent = getIntent();
        strOemId = intent.getStringExtra("oemid");
        strEpcId = intent.getStringExtra("epcid");
        strTitle = intent.getStringExtra("title");
        itemChexing = (ChexingItem) intent.getSerializableExtra(Constant.CAR_TYPE);
        if(strEpcId == null || strOemId == null) {
            ToastUtils.show(mActivity, "获取配件信息失败");
            return;
        }

        tvTitle = ((TextView) findViewById(R.id.tv_title));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        lvOEM = ((ListView) findViewById(R.id.lv_oem));
        ivEPC = ((ImageView) findViewById(R.id.iv_epc));
        llContiner = ((LinearLayout) findViewById(R.id.ll_continer));

        tvName = ((TextView) findViewById(R.id.tv_name));
        tvOEM = ((TextView) findViewById(R.id.tv_oem));
        tvLocation = ((TextView) findViewById(R.id.tv_location));
        tvPrice = ((TextView) findViewById(R.id.tv_price));
        tvCount = ((TextView) findViewById(R.id.tv_count));
        tvFabu = (TextView) findViewById(R.id.tv_publish);
        tvQiugou = (TextView) findViewById(R.id.tv_purchase);

    }

    @Override
    protected void initViews() {
        tvTitle.setText(strTitle);
        ivBack.setOnClickListener(this);
        llContiner.setOnClickListener(this);
        tvFabu.setOnClickListener(this);
        tvQiugou.setOnClickListener(this);
//        for (int i = 0; i < 3; i++) {
//            dataList.add(new EPCTop());
//        }
        commonAdapter = new CommonAdapter<EPCTop>(this, dataList, R.layout.item_oem_car) {
            @Override
            public void convert(BaseViewHolder helper, EPCTop item, int position) {
                helper.setText(R.id.tv_name, item.getPinpai()+"  "+item.getChexi()+"  "+item.getChexing());
            }
        };
        lvOEM.setAdapter(commonAdapter);
        lvOEM.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //startActivity(new Intent(OEMDetailActivity.this, OEMDetailActivity.class));
            }
        });
        if(utils.isOnLine()) {
            reqData();
        }
        else {
            ToastUtils.show(mActivity, "请检查您的网络");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_continer:
                Log.e("2", flag + "flag");

                if (!flag) {
                    flag = true;
                    lvOEM.setVisibility(View.VISIBLE);
                } else {
                    flag = false;
                    lvOEM.setVisibility(View.GONE);

                }
                break;
            case R.id.tv_publish:
                Intent intent = new Intent(mActivity, PartsPublishActivity.class);
                intent.putExtra("oemname", item.getName());
                intent.putExtra("oemnum", item.getOem());
                startActivity(intent);
                break;
            case R.id.tv_purchase:
                if(item != null) {
                    Intent intent1 = new Intent(mActivity, ProductPurchaseVINActivity.class);
                    intent1.putExtra("oemname", item.getName());
                    intent1.putExtra("oemnum", item.getOem());
                    intent1.putExtra(Constant.CAR_TYPE, itemChexing);
                    startActivity(intent1);
                }
                break;
        }
    }

    /**
     * 数据
     */
    private void reqData() {
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + strEpcId+"*" + strOemId);

        final String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_QUERY_OEM_DETAIL;
        httpClient.post(url, params, new RequestListener() {
            @Override public void onPreRequest() {
                showWait();
            }


            @Override public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if(response != null) {

                    LG.e("EPC详情", "response="+response);
                    OEMModel model = response.getObj(OEMModel.class);
                    if(model != null) {
                        dataList.clear();
                        List<EPCTop> list = model.getList();
                        if(list != null && list.size() > 0) {
                            dataList.addAll(list);
                        }
                        item = model.getObject();
                        bindData();
                        commonAdapter.notifyDataSetChanged();
                    }
                    else {
                        ToastUtils.show(mActivity, "服务器异常");
                    }

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
                // 11，12，19,21，26
                int errorcode = response.getErrorcode();
                switch (errorcode) {
                    case 12:
                        ToastUtils.show(mActivity,"账号异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 21:
                        ToastUtils.show(mActivity,"epc id不能为空");
                        break;
                    case 26:
                        ToastUtils.show(mActivity,"oem号id 不能为空");
                        break;
                    case 19:
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

    /**
     * 绑定头部数据
     */
    private void bindData() {
        tvName.setText(item.getName());
        tvOEM.setText("OEM号: "+item.getOem());
        tvLocation.setText("位置   "+ item.getPosition());
        tvCount.setText("（适用"+dataList.size()+"款车型）");
    }
}
