package com.minfo.carrepair.activity.publish;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.entity.purchase.PurchaseItem;
import com.minfo.carrepair.entity.purchase.PurchaseList;
import com.minfo.carrepair.entity.purchase.ShowPriceID;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.MyCheck;
import com.minfo.carrepair.utils.ToastUtils;
import com.minfo.carrepair.utils.UniversalImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 报价详情
 *
 * 该界面可以由求购详情和求购列表跳转进来
 * 目前有的问题是列表传值purchaseList
 * 详情传值purchaseItem
 * 这两个类只有picture的字段是不同的
 */
public class ShowPriceActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private TextView tvTitle, tvPinPai, tvCarXi, tvCarXing, tvCar, tvChange, tv_vin, tv_name_pro, tv_number, tv_sure;
    private ImageView ivBack, ivCar;
    private LinearLayout llCar;
    private RelativeLayout rlCount, rlOem, rlKind;
    private LinearLayout ll_sell_continer;
    private EditText et_yuanchang_sell, et_chaiche_sell, et_pinpaiprice_sell, et_other_sell;
    private CheckBox cb_yuanchang_sell, cb_chaiche_sell, cb_pinpai_sell, cb_other_sell;
    private PurchaseItem purchaseItem;
    private PurchaseList purchaseList;

    private String yuanPrice = "";//配件价格
    private String otherPrice = "";//其他价格
    private String pinpaiPrice = "";//品牌价格
    private String chaiPrice = "";//拆车价格
    private String pinzhiStr = "";//品质选取
    private String priceStr = "";//价格选取
    private ShowPriceID showPriceID;
    private List<CheckBox> checkList = new ArrayList<>();
    private String fromchat;

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

        //卖家控件
        tvChange = ((TextView) findViewById(R.id.tv_change));
        tv_sure = ((TextView) findViewById(R.id.tv_sure));

        ll_sell_continer = ((LinearLayout) findViewById(R.id.ll_sell_continer));
        et_yuanchang_sell = ((EditText) findViewById(R.id.et_yuanchang_sell));
        et_chaiche_sell = ((EditText) findViewById(R.id.et_chaiche_sell));
        et_pinpaiprice_sell = ((EditText) findViewById(R.id.et_pinpaiprice_sell));
        et_other_sell = ((EditText) findViewById(R.id.et_other_sell));

        cb_yuanchang_sell = ((CheckBox) findViewById(R.id.cb_yuanchang_sell));
        cb_chaiche_sell = ((CheckBox) findViewById(R.id.cb_chaiche_sell));
        cb_pinpai_sell = ((CheckBox) findViewById(R.id.cb_pinpai_sell));
        cb_other_sell = ((CheckBox) findViewById(R.id.cb_other_sell));


    }

    @Override
    protected void initViews() {
        tvTitle.setText("报价");
        ivBack.setOnClickListener(this);
        tvChange.setOnClickListener(this);
        tv_sure.setOnClickListener(this);
        cb_yuanchang_sell.setOnCheckedChangeListener(this);
        cb_chaiche_sell.setOnCheckedChangeListener(this);

        cb_pinpai_sell.setOnCheckedChangeListener(this);

        cb_other_sell.setOnCheckedChangeListener(this);
        checkList.clear();
        checkList.add(cb_yuanchang_sell);
        checkList.add(cb_chaiche_sell);

        checkList.add(cb_pinpai_sell);

        checkList.add(cb_other_sell);

        Bundle bundle = new Bundle();
        bundle = getIntent().getBundleExtra("info");
        fromchat = getIntent().getStringExtra("fromchat");
        if (bundle != null) {
            purchaseItem = (PurchaseItem) bundle.getSerializable("purchaseitem");
            if (purchaseItem != null) {
                bindData();
            }
            purchaseList = (PurchaseList) bundle.getSerializable("purchaselist");
            if(purchaseList != null) {
                bindDataList();
            }
            if(purchaseItem == null && purchaseList == null) {
                ToastUtils.show(mActivity, "获取数据失败");
                finish();
            }
        }
    }

    //详情数据
    private void bindData() {
        tvPinPai.setText(purchaseItem.getBname());
        tvCarXi.setText(purchaseItem.getSname());
        tvCarXing.setText(purchaseItem.getCname());
        tv_vin.setText(purchaseItem.getVin());
        tv_name_pro.setText(purchaseItem.getJname());
        UniversalImageUtils.displayImageUseDefOptions(purchaseItem.getImg(), ivCar);
    }
    //详情数据
    private void bindDataList() {
        tvPinPai.setText(purchaseList.getBname());
        tvCarXi.setText(purchaseList.getSname());
        tvCarXing.setText(purchaseList.getCname());
        tv_vin.setText(purchaseList.getVin());
        tv_name_pro.setText(purchaseList.getJname());
        UniversalImageUtils.displayImageUseDefOptions(purchaseList.getImg(), ivCar);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_sure://发布报价
                if (checkInput()) {
                    if (isOnline()) {
                        showWait();
                        reqBaoJia();
                    } else {
                        ToastUtils.show(ShowPriceActivity.this, "暂无网络");
                    }
                }
                break;

        }
    }


    /**
     * 检查输入信息
     *
     * @return
     */
    private boolean checkInput() {


        yuanPrice = et_yuanchang_sell.getText().toString().trim();
        chaiPrice = et_chaiche_sell.getText().toString().trim();
        pinpaiPrice = et_pinpaiprice_sell.getText().toString().trim();
        otherPrice = et_other_sell.getText().toString().trim();

        pinzhiStr = "";
        priceStr = "";

        String[] arr = new String[4];
        if (cb_yuanchang_sell.isChecked()) {
            arr[0] = "0";
            if(MyCheck.isEmpty(yuanPrice)) {
                ToastUtils.show(mActivity, "请填写原厂价格");
                return false;
            }
        } else {
            arr[0] = "";

        }
        if (cb_chaiche_sell.isChecked()) {
            arr[1] = "1";
            if(MyCheck.isEmpty(chaiPrice)) {
                ToastUtils.show(mActivity, "请填写拆车价格");
                return false;
            }

        } else {
            arr[1] = "";

        }
        if (cb_pinpai_sell.isChecked()) {
            arr[2] = "2";
            if(MyCheck.isEmpty(pinpaiPrice)) {
                ToastUtils.show(mActivity, "请填写拆车价格");
                return false;
            }

        } else {
            arr[2] = "";

        }
        if (cb_other_sell.isChecked()) {
            arr[3] = "3";
            if(MyCheck.isEmpty(otherPrice)) {
                ToastUtils.show(mActivity, "请填写其他价格");
                return false;
            }

        } else {
            arr[3] = "";

        }
        if (arr[0] != null && !arr[0].equals("")) {
            pinzhiStr = "0";
            priceStr = yuanPrice;

        }
        if (arr[1] != null && !arr[1].equals("")) {
            if(MyCheck.isEmpty(pinzhiStr)) {
                pinzhiStr += "1";
                priceStr += chaiPrice;
            }
            else {
                pinzhiStr += ",1";
                priceStr += "," + chaiPrice;
            }

        }
        if (arr[2] != null && !arr[2].equals("")) {
            if(MyCheck.isEmpty(pinzhiStr)) {
                pinzhiStr += "2";
                priceStr += pinpaiPrice;
            }
            else {
                pinzhiStr += ",2";
                priceStr += "," + pinpaiPrice;
            }

        }
        if (arr[3] != null && !arr[3].equals("")) {
            if(MyCheck.isEmpty(pinzhiStr)) {
                pinzhiStr += "3";
                priceStr += otherPrice;
            }
            else {
                pinzhiStr += ",3";
                priceStr += "," + otherPrice;
            }

        }

        if(MyCheck.isEmpty(priceStr)) {
            ToastUtils.show(mActivity, "请选择配件类型并填写对应价格");

            return false;
        }
        return true;
    }

    /**
     * 请求报价接口
     */
    private void reqBaoJia() {
        String url = getResources().getString(R.string.api_baseurl) + "goods/SetMoney.php";
        Map<String, String> params;
        if(purchaseItem != null) {
            params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + purchaseItem.getBid() + "*" + purchaseItem.getAppuid() + "*" + pinzhiStr + "*" + priceStr);
            Log.e(TAG, Constant.userid + "*" + purchaseItem.getBid() + "*" + purchaseItem.getAppuid() + "*" + pinzhiStr + "*" + priceStr);
        }
        else {
            params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + purchaseList.getBid() + "*" + purchaseList.getAppuid() + "*" + pinzhiStr + "*" + priceStr);
            Log.e(TAG, Constant.userid + "*" + purchaseList.getBid() + "*" + purchaseList.getAppuid() + "*" + pinzhiStr + "*" + priceStr);
        }
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();

                if (response != null && !response.equals("")) {
                    Log.e("response", response.getData());
                    showPriceID = response.getObj(ShowPriceID.class);
                    ToastUtils.show(mActivity, "价格发布成功");
                    if (fromchat != null && fromchat.equals("fromchat")) {
                        Intent intentBackChat = new Intent();
                        if(purchaseItem != null) {
                            intentBackChat.putExtra("name", purchaseItem.getJname());
                            intentBackChat.putExtra("id", showPriceID.getSetMoneyId());

                            intentBackChat.putExtra("nameofcar", purchaseItem.getCname());
                        }
                        else {
                            intentBackChat.putExtra("name", purchaseList.getJname());
                            intentBackChat.putExtra("id", showPriceID.getSetMoneyId());
                            intentBackChat.putExtra("nameofcar", purchaseList.getCname());
                        }

                        setResult(RESULT_OK, intentBackChat);

                    }
                    finish();
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int count = 0;
        for (int i = 0; i < checkList.size(); i++) {
            if (checkList.get(i).isChecked()) {
                count++;
            }

        }
        tv_number.setText("数量：" + count);

    }
}
