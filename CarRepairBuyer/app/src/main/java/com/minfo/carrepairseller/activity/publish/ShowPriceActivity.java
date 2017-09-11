package com.minfo.carrepairseller.activity.publish;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.activity.login.LoginActivity;
import com.minfo.carrepairseller.activity.order.OrderSureActivity;
import com.minfo.carrepairseller.activity.order.OrderSureQGActivity;
import com.minfo.carrepairseller.entity.publish.XiadanQGModel;
import com.minfo.carrepairseller.entity.shop.XiadanModel;
import com.minfo.carrepairseller.entity.showprice.ShowPriceDetail;
import com.minfo.carrepairseller.entity.showprice.ShowPriceDetailInfo;
import com.minfo.carrepairseller.entity.showprice.ShowPriceList;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ConstantUrl;
import com.minfo.carrepairseller.utils.LG;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.minfo.carrepairseller.utils.UniversalImageUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 报价详情界面  用户根据配件商提供价格选择购买价格
 * Created by min-fo-012 on 17/6/15.
 */
public class ShowPriceActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private TextView tvTitle, tvPinPai, tvCarXi, tvCarXing, tvCar, tv_vin, tv_name_pro, tv_number, tv_yuanchang_buy, tv_chaiche_buy, tv_pinpaiprice_buy, tv_other_buy, tv_total_price, tv_tobuy;
    private ImageView ivBack, ivCar;
    private CheckBox cb_yuanchang_buy, cb_chaiche_buy, cb_pinpai_buy, cb_other_buy;//四种价格 checkbox
    private List<CheckBox> checkList = new ArrayList<>();// checkbox 存放集合
    private ShowPriceList showPriceList;

    private String yuanPrice = "";//配件价格
    private String otherPrice = "";//其他价格
    private String pinpaiPrice = "";//品牌价格
    private String chaiPrice = "";//拆车价格
    private String pinzhiStr = "";//品质选取
    private String priceStr = "";//价格选取
    private ShowPriceDetailInfo showPriceDetailInfo;
    private ShowPriceDetail showPriceDetail;
    private TextView tvPhone;
    private String bjId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_show_price);
    }

    @Override
    protected void findViews() {
        //公用控件
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        tvPinPai = ((TextView) findViewById(R.id.tv_pinpai));
        tvCarXi = ((TextView) findViewById(R.id.tv_car_xi));
        tvCarXing = ((TextView) findViewById(R.id.tv_car_xing));
        ivCar = ((ImageView) findViewById(R.id.iv_car));
        tv_vin = ((TextView) findViewById(R.id.tv_vin));
        tv_name_pro = ((TextView) findViewById(R.id.tv_name_pro));
        tv_number = ((TextView) findViewById(R.id.tv_number));
        //买家控件

        tv_yuanchang_buy = ((TextView) findViewById(R.id.tv_yuanchang_buy));
        tv_chaiche_buy = ((TextView) findViewById(R.id.tv_chaiche_buy));
        tv_pinpaiprice_buy = ((TextView) findViewById(R.id.tv_pinpaiprice_buy));
        tv_other_buy = ((TextView) findViewById(R.id.tv_other_buy));
        tv_total_price = ((TextView) findViewById(R.id.tv_total_price));
        tv_tobuy = ((TextView) findViewById(R.id.tv_tobuy));
        tvPhone = ((TextView) findViewById(R.id.tv_phone));
        cb_yuanchang_buy = ((CheckBox) findViewById(R.id.cb_yuanchang_buy));
        cb_chaiche_buy = ((CheckBox) findViewById(R.id.cb_chaiche_buy));
        cb_pinpai_buy = ((CheckBox) findViewById(R.id.cb_pinpai_buy));
        cb_other_buy = ((CheckBox) findViewById(R.id.cb_other_buy));


    }

    @Override
    protected void initViews() {
        tvTitle.setText("报价详情");
        ivBack.setOnClickListener(this);
        tv_tobuy.setOnClickListener(this);
        cb_yuanchang_buy.setOnCheckedChangeListener(this);
        cb_chaiche_buy.setOnCheckedChangeListener(this);

        cb_pinpai_buy.setOnCheckedChangeListener(this);

        cb_other_buy.setOnCheckedChangeListener(this);

        checkList.clear();
        checkList.add(cb_yuanchang_buy);
        checkList.add(cb_chaiche_buy);

        checkList.add(cb_pinpai_buy);

        checkList.add(cb_other_buy);

        bjId = getIntent().getStringExtra("id");
        if (bjId != null && !bjId.equals("")) {

            if (isOnline()) {
                showWait();
                reqShowPriceDetail();
            } else {
                ToastUtils.show(ShowPriceActivity.this, "暂无网络");
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.tv_tobuy://立即购买
                if (checkInput()) {
                    reqxiadan();
                }
                break;

        }
    }

    /**
     * 选中价格处理
     *
     * @return
     */

    private boolean checkInput() {

        pinzhiStr = "";
        priceStr = "";

        String[] arr = new String[4];
        if (cb_yuanchang_buy.isChecked()) {
            arr[0] = "0";
        } else {
            arr[0] = "";
        }
        if (cb_chaiche_buy.isChecked()) {
            arr[1] = "1";
        } else {
            arr[1] = "";
        }
        if (cb_pinpai_buy.isChecked()) {
            arr[2] = "2";
        } else {
            arr[2] = "";
        }
        if (cb_other_buy.isChecked()) {
            arr[3] = "3";
        } else {
            arr[3] = "";
        }

        if (arr[0] != null && !arr[0].equals("")) {
            pinzhiStr = "0";
            priceStr = yuanPrice;

        }
        if (arr[1] != null && !arr[1].equals("")) {
            if(pinzhiStr.equals("")) {
                pinzhiStr = "1";
            }
            else {
                pinzhiStr += ",1";
            }
            if (priceStr.equals("")) {
                priceStr = chaiPrice;
            }
            else {
                priceStr += "," + chaiPrice;
            }

        }
        if (arr[2] != null && !arr[2].equals("")) {
            if(pinzhiStr.equals("")) {
                pinzhiStr = "2";
            }
            else {
                pinzhiStr += ",2";

            }
            if (priceStr.equals("")) {
                priceStr = pinpaiPrice;
            }
            else {
                priceStr += "," + pinpaiPrice;
            }
        }
        if (arr[3] != null && !arr[3].equals("")) {
            if(pinzhiStr.equals("")) {
                pinzhiStr = "3";
            }
            else {
                pinzhiStr += ",3";
            }
            if (priceStr.equals("")) {
                priceStr = otherPrice;
            }
            else {
                priceStr += "," + otherPrice;
            }

        }
        if(priceStr.equals("")||pinzhiStr.equals("")) {
            ToastUtils.show(mActivity, "请选择您要买的配件");
            return false;
        }
        return true;
    }

    /**
     * 请求报价详情接口
     */
    private void reqShowPriceDetail() {
        String url = getResources().getString(R.string.api_baseurl) + "goods/SetMoneyDetail.php";

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + bjId);

        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();

                if (response != null && !response.equals("")) {
                    Log.e("response", response.getData());
                    showPriceDetailInfo = response.getObj(ShowPriceDetailInfo.class);
                    if (showPriceDetailInfo != null) {
                        showPriceDetail = showPriceDetailInfo.getInfo();
                        if (showPriceDetail != null) {
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
                    case 10:
                    case 11:
                        ToastUtils.show(mActivity,"账号异常，请重新登录");
                        utils.jumpToLoginActivity(mActivity);
                        break;
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int count = 0;
        float total_money = 0.00f;
        if (showPriceDetail.getTpdetail() != null && showPriceDetail.getTpdetail().size() > 0) {
            for (int i = 0; i < checkList.size(); i++) {
                if (checkList.get(i).isChecked()) {
                    try {
                        for(int j = 0; j < showPriceDetail.getTpdetail().size(); j ++) {
                            if(showPriceDetail.getTpdetail().get(j).getType().equals(""+i)) {
                                float temp = Float.parseFloat(showPriceDetail.getTpdetail().get(j).getPrice());
                                total_money += Float.parseFloat(new DecimalFormat("##0.00").format(temp));
                                count++;
                                break;
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        LG.e("报价详情", "价格异常");
                    }
                }

            }
            LG.e("报价详情", "total_money="+total_money);
            tv_total_price.setText("¥ " + total_money);
            tv_number.setText("数量：" + count);
        }


    }

    /**
     * 绑定详情数据
     */
    private void bindData() {
        tvPinPai.setText(showPriceDetail.getBname());
        tvCarXi.setText(showPriceDetail.getSname());
        tvCarXing.setText(showPriceDetail.getCname());
        tv_vin.setText(showPriceDetail.getVin());
        tv_name_pro.setText(showPriceDetail.getJname());
        //tvPhone.setText("联系方式：" + showPriceList.getTel());
        UniversalImageUtils.displayImageUseDefOptions(showPriceDetail.getImg(), ivCar);
        if (showPriceDetail.getTpdetail() != null && showPriceDetail.getTpdetail().size() > 0) {
            for (int i = 0; i < showPriceDetail.getTpdetail().size(); i++) {
                if (showPriceDetail.getTpdetail().get(i).getType().equals("0")) {
                    tv_yuanchang_buy.setText(showPriceDetail.getTpdetail().get(i).getPrice());
                    yuanPrice = showPriceDetail.getTpdetail().get(i).getPrice();
                    cb_yuanchang_buy.setChecked(true);
                    cb_yuanchang_buy.setClickable(true);
                } else if (showPriceDetail.getTpdetail().get(i).getType().equals("1")) {
                    chaiPrice = showPriceDetail.getTpdetail().get(i).getPrice();
                    tv_chaiche_buy.setText(showPriceDetail.getTpdetail().get(i).getPrice());
                    cb_chaiche_buy.setChecked(true);
                    cb_chaiche_buy.setClickable(true);
                } else if (showPriceDetail.getTpdetail().get(i).getType().equals("2")) {
                    tv_pinpaiprice_buy.setText(showPriceDetail.getTpdetail().get(i).getPrice());
                    pinpaiPrice = showPriceDetail.getTpdetail().get(i).getPrice();
                    cb_pinpai_buy.setChecked(true);
                    cb_pinpai_buy.setClickable(true);
                } else if (showPriceDetail.getTpdetail().get(i).getType().equals("3")) {
                    tv_other_buy.setText(showPriceDetail.getTpdetail().get(i).getPrice());
                    otherPrice = showPriceDetail.getTpdetail().get(i).getPrice();
                    cb_other_buy.setChecked(true);
                    cb_other_buy.setClickable(true);
                }

            }
        }


    }

    /**
     * 商品下单接口
     */
    private void reqxiadan() {
        String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_QG_ORDER_ADD;

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + bjId+"*"+pinzhiStr+"*"+priceStr);
        Log.e(TAG, params.toString());

        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();

                if (response != null && !response.equals("")) {
                    Log.e("response", response.getData());
                    XiadanQGModel model = response.getObj(XiadanQGModel.class);
                    if(model != null) {
                        Intent intent = new Intent(mActivity, OrderSureQGActivity.class);
                        intent.putExtra("orderid", model.getOrderid());
                        intent.putExtra("addtime", model.getTime());
                        startActivity(intent);
                    }
                    //                    ProductDetailAll productDetailAll = response.getObj(ProductDetailAll.class);
//                    productDetail = productDetailAll.getInfo();
//                    if (productDetail != null) {
//                        bindData();
//                    }
                }
                else {
                    ToastUtils.show(mActivity, "服务器异常，请稍后再试");
                }
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                // 11，12,31，32，33，34，35
                switch (errorcode) {
                    case 11:
                    case 12:
                        ToastUtils.show(mActivity, "用户信息异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 26:
                        ToastUtils.show(mActivity, "商品信息获取失败");
                        break;
                    case 300:
                    case 301:
                    case 9:
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
