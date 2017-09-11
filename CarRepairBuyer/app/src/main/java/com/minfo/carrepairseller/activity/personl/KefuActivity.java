package com.minfo.carrepairseller.activity.personl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.carrepairseller.CommonInterface.PermissionListener;
import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.activity.login.LoginActivity;
import com.minfo.carrepairseller.adapter.BaseViewHolder;
import com.minfo.carrepairseller.adapter.CommonAdapter;
import com.minfo.carrepairseller.chat.ChatActivity;
import com.minfo.carrepairseller.chat.HuanUtils;
import com.minfo.carrepairseller.entity.personl.CollectProductModel;
import com.minfo.carrepairseller.entity.personl.KefuItem;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ConstantUrl;
import com.minfo.carrepairseller.utils.LG;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.minfo.carrepairseller.utils.UniversalImageUtils;
import com.minfo.carrepairseller.widget.PullToRefreshView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class KefuActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout rlNoMessage;
    private RelativeLayout rlNoMessageClick;
    private LinearLayout llContent;

    private TextView tvTitle;
    private ImageView ivBack;
    private ListView listView;
    //    private PtrClassicFrameLayout ptrLayout;
    private PullToRefreshView pullToRefreshView;
    private CommonAdapter<KefuItem> commonAdapter; // 产品
    private List<KefuItem> kefuList = new ArrayList<>();
    private List<KefuItem> tempList = new ArrayList<>();


    private boolean isRefresh = false;
    private boolean isLoad = false;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_kefu);
    }

    @Override
    protected void findViews() {
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        listView = ((ListView) findViewById(R.id.lv_kefu));
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.ptr_kefu);

        rlNoMessage = (RelativeLayout) findViewById(R.id.rl_no_message);
        rlNoMessageClick = (RelativeLayout) findViewById(R.id.rl_no_message_click);
        llContent = (LinearLayout) findViewById(R.id.ll_content);
//        ptrLayout = ((PtrClassicFrameLayout) findViewById(R.id.ptr_layout));

    }

    @Override
    protected void initViews() {
        ivBack.setOnClickListener(this);
        rlNoMessageClick.setOnClickListener(this);
        tvTitle.setText("客服");
//        for (int i = 0; i <3 ; i++) {
//            kefuList.add(new CollectProductModel.ProductItem());
//
//        }

        commonAdapter = new CommonAdapter<KefuItem>(this, kefuList, R.layout.item_kefu) {
            @Override
            public void convert(BaseViewHolder helper, KefuItem item, int position) {

                ImageView imageView = helper.getView(R.id.iv_icon);
                TextView tvLine = helper.getView(R.id.tv_line); // 在线聊天
                TextView tvCall = helper.getView(R.id.tv_call); // 拨打电话

                helper.setText(R.id.tv_name, item.getName());
                helper.setText(R.id.tv_tel, item.getTel());
                UniversalImageUtils.displayImageUseDefOptions(item.getIcon(), imageView);
                final int pos = position;
                tvLine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        jumbToKefu(pos);
                    }
                });
                tvCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callPhone(pos);
                    }
                });


            }
        };
        listView.setAdapter(commonAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(mActivity, ProductDetailActivity.class);
//                Bundle bundle = new Bundle();
//                ProductItem item = new ProductItem();
//                item.setGoodid(kefuList.get(position).getGoodid());
//                bundle.putSerializable("details", item);
//                intent.putExtra("info", bundle);
//                startActivity(intent);
//                startActivity(new Intent(mActivity, ProductDetailActivity.class));
//            }
//        });
        if (isOnline()) {
            //showWait();
//            isRefresh = true;
//            page = 1;
            reqData();
        } else {
            ToastUtils.show(this, "暂无网络");
        }

        pullToRefreshView.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                if(!isRefresh) {
                    if (isOnline()) {
                        page = 1;
                        isRefresh = true;
                        showWait();
                        reqData();
                    } else {
                        ToastUtils.show(mActivity, "暂无网络");
                    }
                }
            }
        });
        pullToRefreshView.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                if(!isLoad) {
                    if (isOnline()) {
                        page ++;
                        isLoad = true;
                        reqData();
                    } else {
                        ToastUtils.show(mActivity, "暂无网络");
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.rl_no_message_click:
                if(!isRefresh) {
                    if (isOnline()) {
                        page = 1;
                        isRefresh = true;
                        reqData();
                    } else {
                        ToastUtils.show(mActivity, "暂无网络");
                    }
                }
                break;

        }
    }

    /**
     * 请求数据
     */
    private void reqData() {

//        if(true) {
//            kefuList.clear();
//            for(int i= 0; i < 5; i++) {
//                KefuItem item = new KefuItem();
//                item.setUserId(i+1+"");
//                item.setName("ming"+i);
//                item.setTel("130110119911");
//                kefuList.add(item);
//            }
//            if(kefuList.size() != 0) {
//                rlNoMessage.setVisibility(View.GONE);
//                llContent.setVisibility(View.VISIBLE);
//            }
//            else {
//                rlNoMessage.setVisibility(View.VISIBLE);
//                llContent.setVisibility(View.GONE);
//            }
//
//            commonAdapter.notifyDataSetChanged();
//            return;
//        }

        String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_USER_KEFU;
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid );
        httpClient.post(url, params, new RequestListener() {
            @Override public void onPreRequest() {
                showWait();
            }


            @Override public void onRequestSuccess(BaseResponse response) {
                Log.e("kefuList", response.toString());
                cleanWait();
                if(response != null) {
                    kefuList.clear();
                    tempList = response.getList(KefuItem.class);

                    if (isRefresh) {
                        isRefresh = false;
                        pullToRefreshView.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                        ToastUtils.show(mActivity, "刷新成功");
                    }
                    if (isLoad) {
                        isLoad = false;
                        pullToRefreshView.onFooterRefreshComplete();
                    }

                    if(tempList != null) {
                        kefuList.addAll(tempList);
                    }
                    if(kefuList.size() != 0) {
                        rlNoMessage.setVisibility(View.GONE);
                        llContent.setVisibility(View.VISIBLE);
                    }
                    else {
                        rlNoMessage.setVisibility(View.VISIBLE);
                        llContent.setVisibility(View.GONE);
                    }
                    pullToRefreshView.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                    pullToRefreshView.onFooterRefreshComplete();
                    if (page > 1) {
                        if (tempList != null && tempList.size() != 0) {
                            commonAdapter.notifyDataSetChanged();
                            ToastUtils.show(mActivity, "加载成功");
                        } else {
                            ToastUtils.show(mActivity, "无更多内容");
                        }
                    } else {
                        commonAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    isRefresh = false;
                    isLoad = false;
                    pullToRefreshView.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                    pullToRefreshView.onFooterRefreshComplete();
                    ToastUtils.show(mActivity, "服务器异常");
                }
            }

            @Override public void onRequestNoData(BaseResponse response) {
                cleanWait();
                isRefresh = false;
                isLoad = false;
                pullToRefreshView.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                pullToRefreshView.onFooterRefreshComplete();
                int errorcode = response.getErrorcode();
                // 11，12，27, 28
                switch (errorcode) {
                    case 11:
                    case 12:
                        ToastUtils.show(mActivity, "账号异常，请重新登录");
//                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 27:
                    case 28:
                    default:
                        ToastUtils.show(mActivity, "服务器繁忙");
                        LG.e("客服", "errorcode="+errorcode);
                        break;
                }

            }


            @Override public void onRequestError(int code, String msg) {
                cleanWait();
                isRefresh = false;
                isLoad = false;
                pullToRefreshView.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                pullToRefreshView.onFooterRefreshComplete();
                ToastUtils.show(mActivity, "连接服务器失败，请检查你的网络");
            }
        });
    }

    /**
     * 跳转客服聊天界面
     * @param position
     */
    private void jumbToKefu(int position) {
        Intent intent = new Intent(mActivity, ChatActivity.class);

        // it's single chat
        intent.putExtra("userNickName", kefuList.get(position).getName());//缺昵称
        intent.putExtra("userHeadImage", kefuList.get(position).getIcon());
        intent.putExtra(HuanUtils.EXTRA_USER_ID,"sell"+kefuList.get(position).getUserId());

        startActivity(intent);
    }

    private void callPhone(final int position) {
        final String tel = kefuList.get(position).getTel();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            jumbToCall(tel);
        }
        else {
            requestRunTimePermission(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA}
                    , new PermissionListener() {
                        @Override
                        public void onGranted() {
                            jumbToCall(tel);
                        }

                        @Override
                        public void onGranted(List<String> grantedPermission) {
//                            reqDate();

                        }

                        @Override
                        public void onDenied(List<String> deniedPermission) {
//                            jumbToPhotosActivity(pos);

                            if(deniedPermission.get(0).equals(Manifest.permission.CALL_PHONE)) {
                                ToastUtils.show(mActivity, "请开启您拨打电话的权限");
                            }

                        }
                    });
        }

    }

    /**
     * 拨打电话
     * @param phone
     */
    private void jumbToCall(String phone) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(mActivity.checkSelfPermission(Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        }else{
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(intent);
        }
    }
}
