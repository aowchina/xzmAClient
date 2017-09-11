package com.minfo.carrepairseller.activity.order;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.activity.login.LoginActivity;
import com.minfo.carrepairseller.adapter.BaseViewHolder;
import com.minfo.carrepairseller.adapter.CommonAdapter;
import com.minfo.carrepairseller.entity.order.OrderDetailEntity;
import com.minfo.carrepairseller.entity.wl.WLInfo;
import com.minfo.carrepairseller.entity.wl.WLInfoEntity;
import com.minfo.carrepairseller.entity.wl.WLInfoList;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ConstantUrl;
import com.minfo.carrepairseller.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by min-fo-012 on 17/6/20.
 */
public class WLInfoActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle, tvWLName, tvWLNum;
    private ImageView ivBack;
    private ListView listView;
    private OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
    private CommonAdapter<WLInfoList> orderAdapter;
    private List<WLInfoList> orderList = new ArrayList<>();
    private WLInfoEntity wLInfoEntity;
    private WLInfo wLInfo;
    private boolean isQiugou = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_wlinfo);
    }

    @Override
    protected void findViews() {
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        tvWLName = ((TextView) findViewById(R.id.tv_wl_name));

        tvWLNum = ((TextView) findViewById(R.id.tv_wl_num));

        ivBack = ((ImageView) findViewById(R.id.iv_back));
        listView = ((ListView) findViewById(R.id.lv_logistics));

    }

    @Override
    protected void initViews() {
        tvTitle.setText("物流详情");
        Bundle bundle = new Bundle();
        bundle = getIntent().getBundleExtra("info");
        isQiugou = getIntent().getBooleanExtra("isqiugou", false);
        if (bundle != null) {
            orderDetailEntity = (OrderDetailEntity) bundle.getSerializable("wlinfo");
            tvWLName.setText(orderDetailEntity.getWlname());
            tvWLNum.setText(orderDetailEntity.getKuaidih());

        }
        ivBack.setOnClickListener(this);
        if (orderDetailEntity != null) {
            if (isOnline()) {
                showWait();
                reqWLInfo();
            } else {
                ToastUtils.show(this, "暂无网络");

            }
        }
        orderAdapter = new CommonAdapter<WLInfoList>(mActivity, orderList, R.layout.item_logistics) {
            @Override
            public void convert(BaseViewHolder helper, WLInfoList item, final int position) {
                ImageView img = helper.getView(R.id.iv_logistics);

                if (position != 0) {
                    img.setImageResource(R.mipmap.img_tiao2);
                } else {
                    img.setImageResource(R.mipmap.img_tiao1);
                }
                helper.setText(R.id.tv_time, item.getTime());
                helper.setText(R.id.tv_name, item.getContext());

            }
        };
        listView.setAdapter(orderAdapter);

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
     * @funcation 查看物流接口
     **/
    public void reqWLInfo() {
        String url;
        Map<String, String> params;
        if(isQiugou) {
            url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_QG_ORDER_WL;
            params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + orderDetailEntity.getQgorderid());
        }
        else {
            url = getResources().getString(R.string.api_baseurl) + "order/SeeWl.php";
            params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + orderDetailEntity.getOrderid());
        }
//        qgorder/SeeWl.php



        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                if (response != null) {
                    Log.e("查看物流接口", response.getData());
                    try {
                        wLInfo = response.getObj(WLInfo.class);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.show(mActivity, "该物流信息未找到");
                        return;
                    }
                    if (wLInfo != null) {
                        wLInfoEntity = wLInfo.getList();
                        if (wLInfoEntity != null) {
                            if (wLInfoEntity.getStatus() != null) {
                                if (wLInfoEntity.getStatus().equals("1")) {
                                    List<WLInfoList> tempList = wLInfoEntity.getData();
                                    setWuliuList(tempList);
                                } else {
                                    ToastUtils.show(WLInfoActivity.this, "" + wLInfoEntity.getMessage());

                                }
                            }

                        }
                    }


                }


            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if (errorcode == 12 || errorcode == 13) {
                    ToastUtils.show(mActivity, "账号异常，请重新登录");
                    utils.jumpTo(mActivity, LoginActivity.class);
                } else if (errorcode == 34 || errorcode == 35) {
                    ToastUtils.show(mActivity, "该订单不存在");
                } else {
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

    // 倒序物流
    private void setWuliuList(List<WLInfoList> list) {
        orderList.clear();
        int i = list.size() - 1;
        while (i >= 0) {
            orderList.add(list.get(i));
            i--;
        }
        orderAdapter.notifyDataSetChanged();
    }
}
