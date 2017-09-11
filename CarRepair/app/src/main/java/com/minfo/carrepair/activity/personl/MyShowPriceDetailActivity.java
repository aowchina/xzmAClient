package com.minfo.carrepair.activity.personl;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.entity.showprice.PriceDetailEntity;
import com.minfo.carrepair.entity.showprice.PriceDetailInfo;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.ToastUtils;
import com.minfo.carrepair.utils.UniversalImageUtils;

import java.util.Map;

public class MyShowPriceDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle, tvPinPai, tvCarXi, tvCarXing, tvCar, tv_vin, tv_name_pro, tv_number, tv_yuanchang_buy, tv_chaiche_buy, tv_pinpaiprice_buy, tv_other_buy, tv_total_price, tv_tobuy;
    private ImageView ivBack, ivCar;
    private LinearLayout llCar;
    private RelativeLayout rlCount, rlOem, rlKind;
    private LinearLayout ll_buy_continer, ll_buy;
    private CheckBox cb_yuanchang_buy, cb_chaiche_buy, cb_pinpai_buy, cb_other_buy;
    private String idBJ;
    private PriceDetailInfo priceDetailInfo;
    private PriceDetailEntity priceDetailEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_my_show_price_detail);
    }

    @Override
    protected void findViews() {
        // 公用控件
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        tvPinPai = ((TextView) findViewById(R.id.tv_pinpai));
        tvCarXi = ((TextView) findViewById(R.id.tv_car_xi));
        tvCarXing = ((TextView) findViewById(R.id.tv_car_xing));
        ivCar = ((ImageView) findViewById(R.id.iv_car));
        tv_vin = ((TextView) findViewById(R.id.tv_vin));
        tv_name_pro = ((TextView) findViewById(R.id.tv_name_pro));
        tv_number = ((TextView) findViewById(R.id.tv_number));

        tv_yuanchang_buy = ((TextView) findViewById(R.id.tv_yuanchang_buy));
        tv_chaiche_buy = ((TextView) findViewById(R.id.tv_chaiche_buy));
        tv_pinpaiprice_buy = ((TextView) findViewById(R.id.tv_pinpaiprice_buy));
        tv_other_buy = ((TextView) findViewById(R.id.tv_other_buy));

        cb_yuanchang_buy = ((CheckBox) findViewById(R.id.cb_yuanchang_buy));
        cb_chaiche_buy = ((CheckBox) findViewById(R.id.cb_chaiche_buy));
        cb_pinpai_buy = ((CheckBox) findViewById(R.id.cb_pinpai_buy));
        cb_other_buy = ((CheckBox) findViewById(R.id.cb_other_buy));

        ll_buy_continer = ((LinearLayout) findViewById(R.id.ll_buy_continer));

        idBJ = getIntent().getStringExtra("id");
        if (isOnline()) {
            showWait();
            reqShowPriceDetail();
        } else {
            ToastUtils.show(this, "暂无网络,请重新连接");
        }

    }

    //详情数据
    private void bindData() {


        tvPinPai.setText(priceDetailEntity.getBname());
        tvCarXi.setText(priceDetailEntity.getSname());
        tvCarXing.setText(priceDetailEntity.getCname());
        tv_vin.setText(priceDetailEntity.getVin());
        tv_name_pro.setText(priceDetailEntity.getJname());
        UniversalImageUtils.displayImageUseDefOptions(priceDetailEntity.getImg(), ivCar);
        if (priceDetailEntity.getTpdetail() != null && priceDetailEntity.getTpdetail().size() > 0) {
            Log.e("8888/8/888   ", priceDetailEntity.getTpdetail().toString());
            for (int i = 0; i < priceDetailEntity.getTpdetail().size(); i++) {
                if (priceDetailEntity.getTpdetail().get(i).getType().equals("0")) {
                    tv_yuanchang_buy.setText(priceDetailEntity.getTpdetail().get(i).getPrice());
                    cb_yuanchang_buy.setChecked(true);
                } else if (priceDetailEntity.getTpdetail().get(i).getType().equals("1")) {
                    tv_chaiche_buy.setText(priceDetailEntity.getTpdetail().get(i).getPrice());
                    cb_chaiche_buy.setChecked(true);
                } else if (priceDetailEntity.getTpdetail().get(i).getType().equals("2")) {
                    tv_pinpaiprice_buy.setText(priceDetailEntity.getTpdetail().get(i).getPrice());
                    cb_pinpai_buy.setChecked(true);
                } else if (priceDetailEntity.getTpdetail().get(i).getType().equals("3")) {
                    tv_other_buy.setText(priceDetailEntity.getTpdetail().get(i).getPrice());
                    cb_other_buy.setChecked(true);
                }
            }
            tv_number.setText("数量：" + priceDetailEntity.getTpdetail().size());

        }

    }

    @Override
    protected void initViews() {
        tvTitle.setText("报价详情");
        ivBack.setOnClickListener(this);


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
     * 请求报价详情接口
     */
    private void reqShowPriceDetail() {
        String url = getResources().getString(R.string.api_baseurl) + "goods/SetMoneyDetail.php";

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + idBJ);

        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();

                if (response != null && !response.equals("")) {
                    Log.e("response", response.getData());
                    priceDetailInfo = response.getObj(PriceDetailInfo.class);
                    if (priceDetailInfo != null) {
                        priceDetailEntity = priceDetailInfo.getInfo();
                        if (priceDetailEntity != null) {
                            bindData();

                        }
                    }


                }
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                switch (errorcode) {
                    case 30:
                        ToastUtils.show(mActivity, "求购信息不存在");
                        break;
                    case 31:
                        ToastUtils.show(mActivity, "买家与求购信息不匹配");
                        break;
                    case 32:
                        ToastUtils.show(mActivity, "该求购信息您已报价");
                        break;
                    default:
                        ToastUtils.show(mActivity, "服务器异常，请稍后再试");
                        break;
                }

            }


            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
            }
        });
    }
}
