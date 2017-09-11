package com.minfo.carrepair.activity.query;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.activity.login.LoginActivity;
import com.minfo.carrepair.adapter.BaseViewHolder;
import com.minfo.carrepair.adapter.CommonAdapter;
import com.minfo.carrepair.adapter.query.ClassificationTitleAdapter;
import com.minfo.carrepair.entity.query.ChexingItem;
import com.minfo.carrepair.entity.query.EPCItem;
import com.minfo.carrepair.entity.query.EPCTitleItem;
import com.minfo.carrepair.entity.query.EPCTitleModel;
import com.minfo.carrepair.entity.query.EPCTop;
import com.minfo.carrepair.entity.query.Product_Details_Entity;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.ConstantUrl;
import com.minfo.carrepair.utils.LG;
import com.minfo.carrepair.utils.ToastUtils;
import com.minfo.carrepair.utils.UniversalImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 配件图
 * 需传 车型
 */
public class EPCQueryActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private final static int FLAG_SELECTOR_CAR_TYPE1 = 10004;
    private PtrClassicFrameLayout ptrLayout;
    private ListView lvTitle,lvEPC;
    private ImageView ivBack,ivCar;
    private TextView tvTitle,tvPinPai,tvCarXi,tvCarXing,tvChange,tvCar;
    //种类
    private ClassificationTitleAdapter kindAdapter;//种类

    List<EPCTitleItem> leftList = new ArrayList<>();
//    private List<Product_Details_Entity> dList = new ArrayList<Product_Details_Entity>();
    private List<EPCItem> epcItems = new ArrayList<>(); // EPC 列表
    private List<Product_Details_Entity> tempList;
    private CommonAdapter commonAdapter;//产品

    private ChexingItem item; // 车型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_epcquery);
    }


    @Override
    protected void findViews() {
        Intent intent = getIntent();
        if(intent == null) {
            ToastUtils.show(mActivity, "请先选择车型");
            finish();
            return;
        }
        item = (ChexingItem) intent.getSerializableExtra(Constant.CAR_TYPE);
        if(item == null) {
            ToastUtils.show(mActivity, "请先选择车型");
            finish();
            return;
        }
        ptrLayout = ((PtrClassicFrameLayout) findViewById(R.id.ptr_layout));
        tvTitle = (TextView) findViewById(R.id.tv_title);
        lvTitle= ((ListView) findViewById(R.id.lv_title));
        lvEPC= ((ListView) findViewById(R.id.lv_epc));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        tvPinPai = ((TextView) findViewById(R.id.tv_pinpai));
        tvCarXi = ((TextView) findViewById(R.id.tv_car_xi));
        tvCarXing = ((TextView) findViewById(R.id.tv_car_xing));
        tvChange = ((TextView) findViewById(R.id.tv_change));
        ivCar = ((ImageView) findViewById(R.id.iv_car));



    }

    @Override
    protected void initViews() {
//        List<EPCTitleItem> type = new ArrayList<>();
//        type.add(new EPCTitleItem());
//        type.add(new EPCTitleItem());
        tvTitle.setText("配件图");
        ivBack.setOnClickListener(this);
        tvChange.setOnClickListener(this);
        kindAdapter = new ClassificationTitleAdapter(this, leftList);
        lvTitle.setAdapter(kindAdapter);
        lvTitle.setOnItemClickListener(this);
        if(item != null) {
            tvPinPai.setText(item.getPinpai());
            tvCarXi.setText(item.getChexi());
            tvCarXing.setText(item.getName());

            UniversalImageUtils.displayImageUseDefOptions(item.getIcon(), ivCar);
        }
//
//        dList.add(new Product_Details_Entity());
//        dList.add(new Product_Details_Entity());
//
//        dList.add(new Product_Details_Entity());
//
//        dList.add(new Product_Details_Entity());

        commonAdapter=new CommonAdapter<EPCItem>(this,epcItems, R.layout.item_epc_detail) {
            @Override
            public void convert(BaseViewHolder helper, EPCItem item, int position) {
                ImageView icon = helper.getView(R.id.iv_epc);
                helper.setText(R.id.tv_title, item.getName());
                UniversalImageUtils.displayImageUseDefOptions(item.getUrl(), icon);
            }
        };
        lvEPC.setAdapter(commonAdapter);
        lvEPC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EPCQueryActivity.this, EPCDetailActivity.class);
                intent.putExtra("epc", epcItems.get(position));
                startActivity(intent);

            }
        });
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrLayout.refreshComplete();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, lvEPC, header);
            }
        });
        if(isOnline()) {
            reqTitleData();
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
            case R.id.tv_change:
                startActivityForResult(new Intent(mActivity, ChoseCarActivity.class), FLAG_SELECTOR_CAR_TYPE1);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        kindAdapter.setSelectedPosition(position);
        epcItems.clear();
        commonAdapter.notifyDataSetChanged();
        if(isOnline()) {
            reqContentData(leftList.get(position).getId());
        }
        else {
            ToastUtils.show(mActivity, R.string.no_internet);
        }
        LG.e("EPC列表", " left id="+leftList.get(position).getId());

    }

    /**
     * 左边栏数据
     */
    private void reqTitleData() {
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + item.getId());

        final String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_QUERY_EPC_LEFT;
        httpClient.post(url, params, new RequestListener() {
            @Override public void onPreRequest() {

            }


            @Override public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if(response != null) {
                    Log.e("EPC列表", "response=" + response);

//                    dList.clear();

                    EPCTitleModel model = response.getObj(EPCTitleModel.class);
                    List<EPCTitleItem> list = model.getList();
                    EPCTop top = model.getObject();
                    if(top != null) {
                        tvPinPai.setText(top.getPinpai());
                        tvCarXi.setText(top.getChexi());
                        tvCarXing.setText(top.getChexing());

                        UniversalImageUtils.displayImageUseDefOptions(top.getIcon(), ivCar);
                    }
                    if(list != null && list.size() > 0) {
                        leftList.addAll(list);
                        kindAdapter.setSelectedPosition(0);
//                        tvPinPai.setText(model.getObject().);
                        if(isOnline()) {
                            reqContentData(list.get(0).getId());
                        }
                        else {
                            ToastUtils.show(mActivity, R.string.no_internet);
                        }
                        LG.e("EPC列表", "id="+list.get(0).getId());
                    }
                    else {
                        ToastUtils.show(mActivity, "未找到相关数据");
                    }

                }
                else {
                    ToastUtils.show(mActivity, "网路异常，请稍后再试");
                }
            }


            @Override public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity, "网路异常，请稍后再试");
            }


            @Override public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                // 12,19,20
                switch (errorcode) {
                    case 12:
                        ToastUtils.show(mActivity,"账号异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 19:
                    case 20:
                    case 22:
                        ToastUtils.show(mActivity,"该车款的信息找不到");
                        break;
                    default:
                        ToastUtils.show(mActivity,"服务器繁忙");
                        break;
                }

            }
        });
    }

    /**
     * 右边栏数据
     * @param id
     */
    private void reqContentData(String id) {
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + id);

        final String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_QUERY_EPC_CONTETN;
        httpClient.post(url, params, new RequestListener() {
            @Override public void onPreRequest() {
                epcItems.clear();
                commonAdapter.notifyDataSetChanged();
            }


            @Override public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if(response != null) {
                    LG.e("EPC列表右边", "response="+response);

//                    dList.clear();
//                    leftList.clear();

                    List<EPCItem> list = response.getList(EPCItem.class);
                    if(list != null && list.size() > 0) {
                        epcItems.addAll(list);
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
//                        epcItems.clear();
//                        commonAdapter.notifyDataSetChanged();

                        ToastUtils.show(mActivity,"该车款的信息找不到");
                        break;
                    default:
//                        epcItems.clear();
//                        commonAdapter.notifyDataSetChanged();
                        ToastUtils.show(mActivity,"服务器繁忙");
                        break;
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FLAG_SELECTOR_CAR_TYPE1:
                if(resultCode == mActivity.RESULT_OK) {
                    if(data != null) {

                        item = (ChexingItem) data.getSerializableExtra(Constant.CAR_TYPE);
                        UniversalImageUtils.displayImageUseDefOptions(item.getIcon(), ivCar);
                        leftList.clear();
                        epcItems.clear();
                        kindAdapter.notifyDataSetChanged();
                        commonAdapter.notifyDataSetChanged();
                        if(isOnline()) {
                            reqTitleData();
                        }
                        else {
                            ToastUtils.show(mActivity, R.string.no_internet);
                        }
                    }
                    else {
                        ToastUtils.show(mActivity, "选择车型异常");
                    }

                }
                break;
        }
    }
}
