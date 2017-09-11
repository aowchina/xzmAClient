package com.minfo.carrepair.activity.address;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.activity.login.LoginActivity;
import com.minfo.carrepair.adapter.address.CityAdapter;
import com.minfo.carrepair.adapter.address.DistrictAdapter;
import com.minfo.carrepair.adapter.address.ProvinceAdapter;
import com.minfo.carrepair.entity.address.AddressItem;
import com.minfo.carrepair.entity.address.City;
import com.minfo.carrepair.entity.address.District;
import com.minfo.carrepair.entity.address.Province;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.ConstantUrl;
import com.minfo.carrepair.utils.LG;
import com.minfo.carrepair.utils.MyCheck;
import com.minfo.carrepair.utils.ToastUtils;
import com.minfo.carrepair.widget.CityData;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class EditAddressActivity extends BaseActivity implements View.OnClickListener{


    private ImageView ivBack;
    private TextView tvSave;
    //    private TextView tvService; // 客服
//    private ImageView ivAdd;
    private TextView tvTitle;
    private EditText etName;
    private EditText etPhone;
    private EditText etAddress;
    private String ProvinceId = "0";
    private String CityId = "0";
    private String DistrictId = "0";
    private ProvinceAdapter padapter;
    private CityAdapter cadapter;
    private DistrictAdapter dadapter;
    private List<Province> provinceList;
    private int provincePos;
    private int cityPos;
    private Spinner province;
    private Spinner city;
    private Spinner district;
    private String strAddress;
    private String userName;
    private String phone;
    private String addressStr;
    private EditHandler editHandler = new EditHandler(this);
    private BaseResponse editBase;
    private String address;
    private String phone1;
    private String name;
    //    private TextView tvLocal;
    private String pid;
    private String cid;
    private String qid;
    private List<City> cityList;
    private List<District> districtList;
    List<City> tempCity;
    List<District> tempDistrict;
    private int proPos;
    private int disPos;
    private boolean firstProvince = true, firstCity = true, firstDistrict = true;
    private boolean isEdit = false;
    private AddressItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_edit_address);
//        setContentView(R.layout.activity_edit_address);
    }

    @Override
    protected void findViews() {
        Intent intent = getIntent();
        if(intent != null) {
            isEdit = intent.getBooleanExtra("isedit", false);
            LG.e("添加收货地址", "isEdit="+isEdit);
            if(isEdit) {
                item = (AddressItem) intent.getSerializableExtra("address");
            }
        }

        ivBack = ((ImageView) findViewById(R.id.iv_back));
        ivBack.setVisibility(View.VISIBLE);
//        tvService = (TextView) findViewById(R.id.tv_service);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSave = (TextView) findViewById(R.id.tv_save);

//        tvLocal = ((TextView) findViewById(R.id.tv_product));
//        ivAdd = ((ImageView) findViewById(R.id.iv_save));
        etName = ((EditText) findViewById(R.id.et_username));
        etPhone = ((EditText) findViewById(R.id.et_phone));
        etAddress = ((EditText) findViewById(R.id.et_address));
        province = ((Spinner) findViewById(R.id.province));
        city = ((Spinner) findViewById(R.id.city));
        district = ((Spinner) findViewById(R.id.district));
    }
    @Override
    protected void initViews() {
        ivBack.setOnClickListener(this);
        tvSave.setOnClickListener(this);
//        tvService.setOnClickListener(this);
//        ivAdd.setOnClickListener(this);

        //城市选择部分
        provinceList = CityData.readProvinces(this);//获取本地数据
        province.setPrompt("省");
        city.setPrompt("城市");
        district.setPrompt("地区");


        padapter = new ProvinceAdapter(this, provinceList);
        province.setAdapter(padapter);

        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initCity(position);
                provincePos = position;
                ProvinceId = provinceList.get(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                City city = provinceList.get(provincePos).getCity().get(position);
                cityPos = position;
                if (city.getArea() != null) {
                    initDistrict(city.getArea());
                    CityId = city.getId();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                District district = provinceList.get(provincePos).getCity().get(cityPos).getArea().get(position);
                DistrictId = district.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if(isEdit) {
            setData();
            tvTitle.setText("编辑收货地址");
        }
        else {
            tvTitle.setText("添加收货地址");
        }
    }

    //填充接收数据
    private void setData() {
        addressStr = item.getId();
        address = item.getAddress();
        phone1 = item.getTel();
        name = item.getName();
        pid = item.getPid()+"";
        cid = item.getCid()+"";
        qid = item.getDid()+"";
//        addressStr = getIntent().getStringExtra("addressid");
//        address = getIntent().getStringExtra("address");
//        phone1 = getIntent().getStringExtra("phone");
//        name = getIntent().getStringExtra("name");
//        pid = getIntent().getStringExtra("pid");
//        cid = getIntent().getStringExtra("cid");
//        qid = getIntent().getStringExtra("qid");
        if (addressStr != null && !addressStr.equals("")) {
            etPhone.setText(phone1);
            etName.setText(name);
            etAddress.setText(address);
//            tvLocal.setText("编辑收货地址");
            proPos = 0;
            //匹配省id
            for (int i = 0; i < provinceList.size(); i++) {
                if (provinceList.get(i).getId().equals(pid)) {
                    proPos = i;
                }
            }
            if (firstProvince) {
                firstProvince = false;
                province.setSelection(proPos, true);
            }
            cityPos = 0;
            //匹配市id

            tempCity = provinceList.get(proPos).getCity();

            if (tempCity != null) {
                for (int i = 0; i < tempCity.size(); i++) {
                    if (tempCity.get(i).getId().equals(cid)) {
                        cityPos = i;
                    }
                }
            }
//
            tempDistrict = tempCity.get(cityPos).getArea();
            disPos = 0;
            //匹配地区id
            if (tempDistrict != null) {
                for (int i = 0; i < tempDistrict.size(); i++) {
                    if (tempDistrict.get(i).getId().equals(qid)) {
                        disPos = i;
                    }
                }

            }
//
        } else {

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_save:
                if (checkInput()) {
                    if (isOnline()) {
                        showWait();
                        if(isEdit) {
                            reqEdit();
                        }
                        else {
                            reqChangeAddress();
                        }
//                        if (addressStr != null && !addressStr.equals("")) {
//                            reqEdit();
//                        } else {
//                        }
                    } else {
                        ToastUtils.show(EditAddressActivity.this, "暂无网络");
                    }
                }
                break;
//            case R.id.tv_service:
//                startActivity(new Intent(mActivity, KefuActivity.class));
//                break;
        }
    }
    //城市初始化

    public void initCity(int position) {
        cadapter = new CityAdapter(this, provinceList.get(position).getCity());
        city.setAdapter(cadapter);
        if (firstCity) {
            firstCity = false;
            city.setSelection(cityPos, true);
        }
    }
    //区初始化
    public void initDistrict(List<District> d) {
        dadapter = new DistrictAdapter(this, d);
        district.setAdapter(dadapter);
        if (firstDistrict) {
            firstDistrict = false;
            district.setSelection(disPos, true);
        }
    }


    /**
     * 检查输入信息
     *
     * @return
     */
    private boolean checkInput() {
        userName = etName.getText().toString().trim();

        phone = etPhone.getText().toString().trim();
        strAddress = etAddress.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            ToastUtils.show(this, "收货人不能为空");
            return false;
        }

        if (!MyCheck.isName(userName)) {
            ToastUtils.show(this, "收货人格式不对");
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(this, "手机号不能为空");
            return false;
        }
        if (!MyCheck.isTel(phone)) {
            ToastUtils.show(this, "手机号格式不对");
            return false;
        }
        if (TextUtils.isEmpty(strAddress)) {
            ToastUtils.show(this, "详细地址不能为空");
            return false;
        }
        if (!MyCheck.isIntro(strAddress)) {
            ToastUtils.show(this, "详细地址格式不对");
            return false;
        }
        return true;
    }


    //异步提醒
    private class EditHandler extends Handler {
        private final WeakReference<Activity> sActivity;
        public EditHandler(Activity activity) {
            this.sActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            cleanWait();
            switch (msg.what) {
                case 0:
                    if (editBase != null) {
                        ToastUtils.show(mActivity, "修改成功！");
                        EditAddressActivity.this.finish();
                    }
                    break;
                case 10:
                    startActivity(new Intent(mActivity, LoginActivity.class));
                    break;
                case 13:
                    ToastUtils.show(mActivity, "手机号格式不符合要求！");
                    break;
                case 36:
                    ToastUtils.show(mActivity, "姓名格式不符合要求！");
                    break;
                case 37:
                    ToastUtils.show(mActivity, "详细地址不符合要求！");
                    break;
                case 38:
                    ToastUtils.show(mActivity, "地址存在重复记录！");
                    break;
                case 101:
                case 102:
                case 103:
                case 104:
                    ToastUtils.show(mActivity, "网络异常！");
                    break;
                case 105:
                    ToastUtils.show(mActivity, "服务器繁忙！");
                    break;
            }
        }
    }

    //请求接口
    private void reqEdit() {
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + utils.convertChinese(userName) + "*" + phone + "*" + ProvinceId + "*" + CityId + "*" + DistrictId + "*" + utils.convertChinese(strAddress) + "*" + addressStr);
        String postUrl = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_ADDRESS_EDIT;
        httpClient.post(postUrl, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                if (response != null) {
                    ToastUtils.show(mActivity, "修改成功！");
                    EditAddressActivity.this.finish();
                }
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                switch (response.getErrorcode()) {
                    case 10:
                        startActivity(new Intent(mActivity, LoginActivity.class));
                        break;
                    case 13:
                        ToastUtils.show(mActivity, "手机号格式不符合要求！");
                        break;
                    case 36:
                        ToastUtils.show(mActivity, "姓名格式不符合要求！");
                        break;
                    case 37:
                        ToastUtils.show(mActivity, "详细地址不符合要求！");
                        break;
                    case 38:
                        ToastUtils.show(mActivity, "地址存在重复记录！");
                        break;
                    case 101:
                    case 102:
                    case 103:
                    case 104:
                        ToastUtils.show(mActivity, "网络异常！");
                        break;
                    case 105:
                        ToastUtils.show(mActivity, "服务器繁忙！");
                        break;
                }
            }

            @Override
            public void onRequestError(int code, String msg) {

            }
        });

    }

    /**
     * 请求添加收货地址接口
     */
    private void reqChangeAddress() {
        // 8段 * userid * 姓名(需转) * 手机号 * 省id * 市id * 区id * 详细地址(需转)
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + utils.convertChinese(userName) + "*" + phone + "*" + ProvinceId + "*" + CityId + "*" + DistrictId + "*" + utils.convertChinese(strAddress));

        final String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_ADDRESS_ADD;
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                ToastUtils.show(mActivity,  "地址添加成功");
                finish();
            }


            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(EditAddressActivity.this, msg);
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                // 9,36,13,14,15,16,37,10,11,38(记录重复)
                switch(errorcode) {
                    // 36,13,62,63,64,37,12,38
                    case 12:
                        ToastUtils.show(mActivity,   "账号异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 13:
                        ToastUtils.show(mActivity,   "联系电话不符合要求");
                        break;
                    case 36:
                        ToastUtils.show(mActivity,   "联系人不符合要求");
                        break;
                    case 37:
                        ToastUtils.show(mActivity,   "详细地址不符合要求");
                        break;
                    case 38:
                        ToastUtils.show(mActivity,   "地址已存在，请勿重复录入");
                        break;
                    case 62:
                        ToastUtils.show(mActivity,   "请选择省份");
                        break;
                    case 63:
                        ToastUtils.show(mActivity,   "请选择所在城市");
                        break;
                    case 64:
                        ToastUtils.show(mActivity,   "请选择所在区");
                        break;
                    default:
                        ToastUtils.show(mActivity,   "服务器繁忙");
                        break;
                }

            }
        });
    }

}
