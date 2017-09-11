package com.minfo.carrepairseller.activity.address;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.activity.login.LoginActivity;
import com.minfo.carrepairseller.adapter.BaseViewHolder;
import com.minfo.carrepairseller.adapter.CommonAdapter;
import com.minfo.carrepairseller.entity.address.AddressItem;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ConstantUrl;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.minfo.carrepairseller.widget.swipemenulistview.SwipeMenu;
import com.minfo.carrepairseller.widget.swipemenulistview.SwipeMenuCreator;
import com.minfo.carrepairseller.widget.swipemenulistview.SwipeMenuItem;
import com.minfo.carrepairseller.widget.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddressListActivity extends BaseActivity implements View.OnClickListener{


    private String TAG = "Shipping_Address";
    private SwipeMenuListView smlv;

//    private TextView right_text;
    private TextView center_title_text;
    private ImageView left_back_btn;

    private TextView tvAddAddress; // 添加地址

    private List<AddressItem> addressList = new ArrayList<>();
    private CommonAdapter<AddressItem> aAdapter;

    private int jumpWhere;// 值为1的时候设置完就关闭地址列表Activity


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_address_list);
//        setContentView(R.layout.activity_address_list);
    }
    @Override
    protected void findViews() {
        jumpWhere = getIntent().getIntExtra("jumpWhere", 2);

        smlv = (SwipeMenuListView) findViewById(R.id.smlv_addresslist);
        center_title_text = (TextView) findViewById(R.id.tv_title);
        center_title_text.setText("收货地址");
//        right_text = (TextView) findViewById(R.id.tv_right);
        left_back_btn = (ImageView) findViewById(R.id.iv_back);
        tvAddAddress = (TextView) findViewById(R.id.tv_add_address);

        left_back_btn.setVisibility(View.VISIBLE);
//        right_text.setVisibility(View.VISIBLE);
//        right_text.setText("编辑");


    }


    public void getData() {
        if (isOnline()) {
            showWait();
            reqAddressList();
        }
    }


    @Override protected void onResume() {
        super.onResume();
        getData();
    }


    @Override public void initViews() {


//        right_text.setOnClickListener(this);
        left_back_btn.setOnClickListener(this);
        tvAddAddress.setOnClickListener(this);



        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFF, 0xFF, 0xFF)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.mipmap.skin_shanchu);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        smlv.setMenuCreator(creator);
        smlv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                AddressItem item = addressList.get(position);
                switch (index) {
                    case 0:
                        //请求删除收货地址接口
                        showWait();
                        reqDelete(item.getId(), position);

                        break;
                }
                return false;
            }
        });
        // set SwipeListener
        smlv.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override public void onSwipeStart(int position) {
                // swipe start
            }


            @Override public void onSwipeEnd(int position) {
                // swipe end
            }
        });


        aAdapter = new CommonAdapter<AddressItem>(mActivity, addressList, R.layout.content_address_list) {
            @Override
            public void convert(BaseViewHolder helper, AddressItem item, int position) {
                LinearLayout llAddress = helper.getView(R.id.address);
                CheckBox checkBox = helper.getView(R.id.address_choose);
                helper.setText(R.id.tv_username, item.getName());
                helper.setText(R.id.tv_phone, item.getTel());
                helper.setText(R.id.address_text, item.getAddress());
                final int isDefault = item.getState();
                final AddressItem address = item;
                checkBox.setChecked(isDefault == 1 ? true : false);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isDefault == 1) {
                            aAdapter.notifyDataSetChanged();
                        } else {
                            for (AddressItem address : addressList) {
                                address.setState(0);
                            }
                            reqSetDefault(address);
                        }
                    }
                });
                if(jumpWhere == 1) {
                    llAddress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isDefault == 1) {
                                aAdapter.notifyDataSetChanged();
                            } else {
                                for (AddressItem address : addressList) {
                                    address.setState(0);
                                }
                                reqSetDefault(address);
                            }
                        }
                    });
                }
                else {
                    llAddress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(mActivity, EditAddressActivity.class);
                            intent.putExtra("isedit", true);
                            intent.putExtra("address", address);
                            startActivity(intent);
                        }
                    });
                }
            }
        };

        smlv.setAdapter(aAdapter);
        smlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddressItem item = addressList.get(position);
                Intent intent = new Intent(mActivity, EditAddressActivity.class);
                intent.putExtra("isedit", true);
                intent.putExtra("address", item);
                startActivity(intent);
            }
        });
        getData();
    }


    /**
     * 请求删除收货地址
     */
    public void reqDelete(String addressId, final int position) {
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + addressId);

        final String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_ADDRESS_DELETE;
        httpClient.post(url, params, new RequestListener() {
            @Override public void onPreRequest() {

            }


            @Override public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                addressList.remove(position);
                aAdapter.notifyDataSetChanged();
                ToastUtils.show(mActivity, "删除成功");
            }


            @Override public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity, "请求数据失败");
            }


            @Override public void onRequestNoData(BaseResponse response) {
                cleanWait();
                // 24，12，39
                int errorcode = response.getErrorcode();
                switch (errorcode) {
                    case 12:
                        ToastUtils.show(mActivity, "账号异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 24:
                        ToastUtils.show(mActivity, "服务器繁忙，请稍后重试！");
                        break;
                    case 39:
                        ToastUtils.show(mActivity, "地址不存在或已被删除");
                        break;
                    default:
                        ToastUtils.show(mActivity, "服务器繁忙");
                        break;
                }

            }
        });
    }


    //点击事件
    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
//            case R.id.tv_right:
//                utils.jumpTo(mActivity, EditAddressActivity.class);
//                break;
            case R.id.tv_add_address:
                utils.jumpTo(mActivity, EditAddressActivity.class);
                break;
        }
    }


    /**
     * 请求收货地址列表接口
     */
    private void reqAddressList() {
        boolean isDebug = false;
        if(isDebug) {
            cleanWait();
            addressList.clear();
            for(int i = 0; i < 5; i++) {
                AddressItem item = new AddressItem();
                item.setId(""+(i+5));
                item.setName("小"+i);
                item.setAddress("dagdsgkjrsdhkgjdsg");
                item.setTel("13519191919");
                addressList.add(item);
            }
            aAdapter.notifyDataSetChanged();
            return;
        }

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid);

        final String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_ADDRESS_LIST;
        httpClient.post(url, params, new RequestListener() {
            @Override public void onPreRequest() {

            }


            @Override public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                addressList.clear();
                List<AddressItem> addresses = response.getList(AddressItem.class);
                //适配数据
                if (addresses != null && addresses.size()>0) {
                    addressList.addAll(addresses);

                }
                aAdapter.notifyDataSetChanged();
            }


            @Override public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity, "请求数据失败");
            }


            @Override public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if (errorcode == 12||errorcode == 18) {
                    ToastUtils.show(mActivity,"账号异常，请重新登录");
//                    utils.jumpTo(mActivity, LoginActivity.class);
                }else if (errorcode == 15) {
                    ToastUtils.show(mActivity,"该地址不存在");
//                    utils.jumpTo(mActivity, LoginActivity.class);
                }
                else {
                    ToastUtils.show(mActivity,"服务器繁忙");
                }
            }
        });
    }

    /**
     * 设置默认的收货地址
     * */
    public void reqSetDefault(final AddressItem address) {
//        if(true)
//            return;
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + address.getId());
        final String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_ADDRESS_DEFAULT;
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                address.setState(1);
                aAdapter.notifyDataSetChanged();
                ToastUtils.show(mActivity, "设置成功");
                if (jumpWhere == 1) {
                    Intent intent = new Intent();
                    intent.putExtra("addressId", address.getId());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity, "请求数据失败");
                aAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if (errorcode == 11|| errorcode == 13) {
                    ToastUtils.show(mActivity, "账号异常，请重新登录");
                    utils.jumpTo(mActivity,  LoginActivity.class);
                } else if (errorcode == 15) {
                    ToastUtils.show(mActivity, "地址不存在或已被删除");
                } else {
                    ToastUtils.show(mActivity, "服务器繁忙");
                }
                aAdapter.notifyDataSetChanged();
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    //点击home键
    @Override public void onBackPressed() {
        super.onBackPressed();
//        utils.CommitPageFlagToShared(mActivity, 5);
    }
}
