package com.minfo.carrepair.activity.query;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.activity.login.LoginActivity;
import com.minfo.carrepair.adapter.BaseViewHolder;
import com.minfo.carrepair.adapter.CommonAdapter;
import com.minfo.carrepair.entity.query.ChejiaInfo;
import com.minfo.carrepair.entity.query.ChexingItem;
import com.minfo.carrepair.entity.query.PartsItem;
import com.minfo.carrepair.entity.query.VINChangModel;
import com.minfo.carrepair.entity.query.VINEntity;
import com.minfo.carrepair.entity.query.VINModel;
import com.minfo.carrepair.entity.query.VINNowModel;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.ConstantUrl;
import com.minfo.carrepair.utils.LG;
import com.minfo.carrepair.utils.MyCheck;
import com.minfo.carrepair.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VINQueryActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle, tvPinPai, tvCarXi, tvCarXing, tvChange, tvCar, tvQuery;
    private ImageView ivBack, ivCar;
    private EditText etQuery;
    private LinearLayout llCar;
    private RelativeLayout rlCount, rlOem, rlKind;
    private ListView lvVIN;
    private List<ChejiaInfo> dataList = new ArrayList<>();
    private CommonAdapter commonAdapter;//产品

    private String strQuery;
    private String [] names = {"品牌名称","车型名称","销售名称","车辆类型","17位的车架号VIN","发动机型号","功率/转速(Kw/R)","发动机喷射类型","燃油类型","变速器类型","发动机缸数","气缸形式","排量(L)","生产年份","安全气囊","座位数","车辆级别","车门数","车身形式","厂家名称","档位数","装备质量(KG)","组装厂"};
    private String img;
    private VINChangModel.Result entity;

    private ChexingItem item;

    private Map<String, String> mapTitle = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_vinquery);
    }


    @Override
    protected void findViews() {
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        tvQuery = ((TextView) findViewById(R.id.tv_query));
        etQuery = ((EditText) findViewById(R.id.et_query));
        lvVIN = ((ListView) findViewById(R.id.lv_vin));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        tvPinPai = ((TextView) findViewById(R.id.tv_pinpai));
        tvCarXi = ((TextView) findViewById(R.id.tv_car_xi));
        tvCarXing = ((TextView) findViewById(R.id.tv_car_xing));
        tvChange = ((TextView) findViewById(R.id.tv_change));
        ivCar = ((ImageView) findViewById(R.id.iv_car));

        llCar = ((LinearLayout) findViewById(R.id.ll_car));


    }

    @Override
    protected void initViews() {
        tvTitle.setText("车架号查询");
        tvQuery.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        llCar.setOnClickListener(this);
//        for (int i = 0; i < 20; i++) {
//            dataList.add(new VINEntity());
//        }
        commonAdapter = new CommonAdapter<ChejiaInfo>(this, dataList, R.layout.item_vin) {
            @Override
            public void convert(BaseViewHolder helper, ChejiaInfo item, int position) {
                helper.setText(R.id.tv_title, item.getName());
                helper.setText(R.id.tv_content, item.getContent());
            }
        };
        lvVIN.setAdapter(commonAdapter);
        setListViewHeightBasedOnChildren(lvVIN);
        initHashMap();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_query:
                dataList.clear();
                commonAdapter.notifyDataSetChanged();
//                startActivity(new Intent(VINQueryActivity.this, ProductPurchaseVINActivity.class));
                strQuery = etQuery.getText().toString();
                if(strQuery == null || strQuery.equals("")) {
                    ToastUtils.show(mActivity, "请先输入您的车架号(VIN)");
                    return;
                }
                if(strQuery.length() != 17) {
                    ToastUtils.show(mActivity, "请输入正确的17位车架号(VIN)");
                    return;
                }
                if(isOnline()) {
                    reqQueryByVIN();
                }
                else {
                    ToastUtils.show(mActivity, R.string.no_internet);
                }
                break;
            case R.id.ll_car:
                //startActivity(new Intent(ProductDetailActivity.this,StoreActivity.class));
//                ToastUtils.show(this, "详情");

                break;
        }
    }

    /**
     * 加载数据后，计算listview高度
     *
     * @param listView
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 车架号VIN查询
     */
    private void reqQueryByVIN() {
        Map<String, String> params = utils.getParams(utils.getBasePostStr()+"*"+ Constant.userid+"*"+strQuery);
        String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_QUERY_CHEJIA_VIN;
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                LG.e("配件查询", "response="+response);

                if(response != null) {
                    LG.e("配件查询", "response="+response);
                    dataList.clear();
                    try {
                        doVINModel(response.getData());

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtils.show(mActivity, "请求数据失败，请稍后再试！");
                    }
                    commonAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(lvVIN);
                }
                else {
                    ToastUtils.show(mActivity,"服务器异常");
                }

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                // errorcode为 12,15,16 时，给予对应错误提示；
                switch (errorcode) {
                    case 12:
                        ToastUtils.show(mActivity,"账号异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 15:
                        ToastUtils.show(mActivity,"oem号不能为空");
                        break;
                    case 16:
                        ToastUtils.show(mActivity,"此oem号的商品暂未上架");
                        break;
                    default:
                        ToastUtils.show(mActivity,"服务器异常");
                        LG.e("配件查询", "errorcode="+errorcode);
                        break;

                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity,"服务器异常");
                LG.e("配件查询", "errorcode="+code+"msg="+msg);
            }
        });
    }

    /**
     * 初始化键值对
     */
    private void initHashMap() {
        mapTitle.clear();
        // @{@"Manufacturers":@"厂家名称",@"Brand":@"品牌名称",@"Series":@"车系",
        // @"Models":@"车型",@"SalesName":@"销售名称",@"SalesVersion":@"销售版本",
        // @"Year":@"年款",@"EmissionStandard":@"排放标准",@"VehicleType":@"车辆类型",
        // @"VehicleSize":@"车辆级别",@"ChassisCode":@"代号/底盘号",@"GuidingPrice":@"指导价格（万元）",
        // @"ListingYear":@"上市年份",@"ListingMonth":@"上市月份",
        // @"ProducedYear":@"生产年份",@"IdlingYear":@"停产年份",@"ProductionStatus":@"生产状态",
        // @"SalesStatus":@"销售状态",@"Country":@"国别",@"VehicleAttributes":@"厂家类型(国产,合资,进口)",
        // @"Generation":@"代数",@"Remark":@"备注",@"ModelCode":@"工信部车型代码",@"EngineModel":@"发动机型号",
        // @"EngineManufacturer":@"发动机生产厂家",@"CylinderVolume":@"气缸容积",@"Displacement":@"排量（L）",
        // @"Induction":@"进气形式",@"FuelType":@"燃油类型",@"FuelGrade":@"燃油标号",@"Horsepower":@"最大马力（PS）",
        // @"PowerKw":@"最大功率（kW）",@"CylinderArrangement":@"气缸排列形式",@"Cylinders":@"气缸数（个）",
        // @"ValvesPerCylinder":@"每缸气门数（个）",@"TransmissionType":@"变速器类型",@"TransmissionDescription":@"变速器描述",
        // @"GearNumber":@"档位数",@"FrontBrake":@"前制动器类型",@"RearBrake":@"后制动器类型",@"PowerSteering":@"助力类型",
        // @"EngineLocation":@"发动机位置",@"DriveMode":@"驱动方式",@"Wheelbase":@"轴距（mm）",@"Doors":@"车门数",
        // @"Seats":@"座位数（个）",@"FrontTyre":@"前轮胎规格",@"RearTyre":@"后轮胎规格",@"FrontRim":@"前轮毂规格",
        // @"RimsMaterial":@"轮毂材料",@"SpareWheel":@"备胎规格",@"Sunroof":@"电动天窗",@"PanoramicSunroof":@"全景天窗",
        // @"HidHeadlamp":@"氙气大灯",@"FrontFogLamp":@"前雾灯",@"RearWiper":@"后雨刷",@"AC":@"空调",@"AutoAC":@"自动空调"};
        mapTitle.put("Manufacturers","厂家名称");
        mapTitle.put("Brand","品牌名称");
        mapTitle.put("Series","车系");
        mapTitle.put("Models","车型");
        mapTitle.put("SalesName","销售名称");
        mapTitle.put("SalesVersion","销售版本");
        mapTitle.put("Year","年款");
        mapTitle.put("EmissionStandard","排放标准");
        mapTitle.put("VehicleType","车辆类型");
        mapTitle.put("VehicleSize","车辆级别");
        mapTitle.put("ChassisCode","代号/底盘号");
        mapTitle.put("GuidingPrice","指导价格（万元）");
        mapTitle.put("ListingYear","上市年份");
        mapTitle.put("ListingMonth","上市月份");
        mapTitle.put("ProducedYear","生产年份");
        mapTitle.put("IdlingYear","停产年份");
        mapTitle.put("ProductionStatus","生产状态");
        mapTitle.put("SalesStatus","销售状态");
        mapTitle.put("Country","国别");
        mapTitle.put("VehicleAttributes","厂家类型(国产,合资,进口)");
        mapTitle.put("Generation","代数");
        mapTitle.put("Remark","备注");
        mapTitle.put("ModelCode","工信部车型代码");
        mapTitle.put("EngineModel","发动机型号");
        mapTitle.put("EngineManufacturer","发动机生产厂家");
        mapTitle.put("CylinderVolume","气缸容积");
        mapTitle.put("Displacement","排量（L）");
        mapTitle.put("Induction","进气形式");
        mapTitle.put("FuelType","燃油类型");
        mapTitle.put("FuelGrade","燃油标号");
        mapTitle.put("Horsepower","最大马力（PS）");
        mapTitle.put("PowerKw","最大功率（kW）");
        mapTitle.put("CylinderArrangement","气缸排列形式");
        mapTitle.put("Cylinders","气缸数（个）");
        mapTitle.put("ValvesPerCylinder","每缸气门数（个）");
        mapTitle.put("TransmissionType","变速器类型");
        mapTitle.put("TransmissionDescription","变速器描述");
        mapTitle.put("GearNumber","档位数");
        mapTitle.put("FrontBrake","前制动器类型");
        mapTitle.put("RearBrake","后制动器类型");
        mapTitle.put("PowerSteering","助力类型");
        mapTitle.put("EngineLocation","发动机位置");
        mapTitle.put("DriveMode","驱动方式");
        mapTitle.put("Wheelbase","轴距（mm）");
        mapTitle.put("Doors","车门数");
        mapTitle.put("Seats","座位数");
        mapTitle.put("FrontTyre","前轮胎规格");
        mapTitle.put("RearTyre","后轮胎规格");
        mapTitle.put("FrontRim","前轮毂规格");
        mapTitle.put("RearRim","后轮毂规格");
        mapTitle.put("RimsMaterial","轮毂材料");
        mapTitle.put("SpareWheel","备胎规格");
        mapTitle.put("Sunroof","电动天窗");
        mapTitle.put("PanoramicSunroof","全景天窗");
        mapTitle.put("HidHeadlamp","氙气大灯");
        mapTitle.put("FrontFogLamp","前雾灯");
        mapTitle.put("RearWiper","后雨刷");
        mapTitle.put("LevelId","LevelId");
        mapTitle.put("AC","空调");
        mapTitle.put("AutoAC","自动空调");
    }

    private void doVINModel(String data) throws JSONException {

        JSONObject jsonObject = new JSONObject(data);
        int biaoshi = jsonObject.getInt("biaoshi");
        switch (biaoshi) {
            case 0:
                break;
            case 1:
                img = jsonObject.getString("cimage");
                break;
        }
        JSONObject che = jsonObject.getJSONObject("che");
        if(che != null) {
            try {
                JSONArray results = che.getJSONArray("Result");
                if (results != null) {
                    JSONObject result = results.getJSONObject(0);
                    Iterator<String> iterator = result.keys();
                    item = new ChexingItem();
//                String pinpai = result.getString("Brand");
//                String chexi = result.getString("Series");
//                String chexing = result.getString("Models");
                    item.setPinpai(result.getString("Brand"));
                    item.setChexi(result.getString("Series"));
                    item.setName(result.getString("Models"));
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        String vaule = result.getString(key);
                        String title = mapTitle.get(key);
                        if (!MyCheck.isEmpty(vaule) && !MyCheck.isEmpty(title)) {
                            ChejiaInfo info = new ChejiaInfo();
                            info.setName(title);
                            info.setContent(vaule);
                            dataList.add(info);
                        }

                    }

                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                LG.e("车架号查询", "解析result异常");
            }
            JSONObject info = che.getJSONObject("Info");
//                * Success : true
//                        * Desc : 正常
            if(info != null) {
                boolean isSuccess = info.getBoolean("Success");
                if (!isSuccess) {
                    String desc = info.getString("Desc");
                    ToastUtils.show(mActivity, "" + desc);
                }
            }
            else {
                LG.e("车架号查询", "info=null");
            }

//            JSONObject vinInfo = che.getJSONObject("Additional");
//            if(vinInfo != null) {
//                String vin = vinInfo.getString("Vin");
//                String vinYear = vinInfo.getString("VinYear");
//                LG.e("车架号查询", "vin="+vin+", vinYear="+vinYear);
//            }
//            else {
//                LG.e("车架号查询", "vinInfo=null");
//            }

        }
        else {
            LG.e("车架号查询", "che=null");
            ToastUtils.show(mActivity, "请求数据失败，请稍后再试");
        }
    }
}
