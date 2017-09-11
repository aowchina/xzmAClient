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
import com.minfo.carrepairseller.entity.order.EvaluteEntity;
import com.minfo.carrepairseller.entity.order.EvaluteList;
import com.minfo.carrepairseller.entity.order.OrderDetailEntity;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.minfo.carrepairseller.utils.UniversalImageUtils;
import com.minfo.carrepairseller.widget.PullToRefreshView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EvaluateActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ListView listView;
    private ImageView ivBack;
    private PullToRefreshView ptrLayout;
    private boolean isRefresh;
    private boolean isLoad;
    private int page = 1;
    private CommonAdapter<EvaluteEntity> commonAdapter;
    private List<EvaluteEntity> list = new ArrayList<>();
    private List<EvaluteEntity> templist = new ArrayList<>();

    private OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
    private EvaluteList evaluteList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_evaluate);
//        setContentView(R.layout.activity_assess);
    }

    @Override
    protected void findViews() {
        listView = ((ListView) findViewById(R.id.lv_evalute));
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        ptrLayout = (PullToRefreshView) findViewById(R.id.ptr_layout);

    }

    @Override
    protected void initViews() {
        Bundle bundle = new Bundle();
        bundle = getIntent().getBundleExtra("info");
        if (bundle != null) {
            orderDetailEntity = (OrderDetailEntity) bundle.getSerializable("wlinfo");


        }
        if (isOnline()) {
            showWait();
            reqEvalute();
        } else {
            ToastUtils.show(mActivity, "暂无网络");
        }
        ivBack.setOnClickListener(this);
        tvTitle.setText("评论");

        commonAdapter = new CommonAdapter<EvaluteEntity>(mActivity, list, R.layout.item_evalute) {
            @Override
            public void convert(BaseViewHolder helper, EvaluteEntity item, int position) {

                ImageView iv = helper.getView(R.id.iv_icon);
                helper.setText(R.id.tv_name, item.getName());
                helper.setText(R.id.tv_content, item.getContent());
                helper.setText(R.id.tv_time, item.getAddtime());
                UniversalImageUtils.displayImageUseDefOptions(item.getPicture(), iv);
            }
        };
        listView.setAdapter(commonAdapter);
        ptrLayout.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                //加载更多功能的代码
                if (isOnline()) {
                    page++;
                    isLoad = true;

                    showWait();
                    reqEvalute();
                } else {
                    ToastUtils.show(mActivity, "暂无网络");
                }

            }
        });
        ptrLayout.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {

                if (isOnline()) {
                    isRefresh = true;
                    page = 1;
                    list.clear();
                    showWait();
                    reqEvalute();
                } else {
                    ToastUtils.show(mActivity, "暂无网络");
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
        }
    }

    /****
     * @funcation 请求评价列表接口
     ****/
    public void reqEvalute() {

        String url = getResources().getString(R.string.api_baseurl) + "user/PinjiaList.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" +orderDetailEntity.getGoods().get(0).getGoodid()+ "*" + page);

        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                Log.e("reqEvalute", response.getData());
                if (response!=null){
                    evaluteList=response.getObj(EvaluteList.class);
                    if (evaluteList!=null){
                        templist=evaluteList.getPinjia();
                    }
                    list.addAll(templist);
                    if (isRefresh) {
                        isRefresh = false;

                    }
                }

                commonAdapter.notifyDataSetChanged();
                ptrLayout.onHeaderRefreshComplete();
                ptrLayout.onFooterRefreshComplete();
                ptrLayout.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());


                if (page > 1) {
                    if (templist != null && templist.size() > 0) {
                        commonAdapter.notifyDataSetChanged();
                        ToastUtils.show(EvaluateActivity.this, "加载成功");
                    } else {
                        ToastUtils.show(EvaluateActivity.this, "无更多内容");
                    }
                }
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                isRefresh = false;
                ptrLayout.onHeaderRefreshComplete();
                ptrLayout.onFooterRefreshComplete();
                ptrLayout.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
//
                int errorcode = response.getErrorcode();
                if (errorcode == 12) {
                    utils.jumpTo(mActivity, LoginActivity.class);
                } else if (errorcode ==67) {
                    ToastUtils.show(EvaluateActivity.this, "该商品没有评价");

                }else {
                    ToastUtils.show(EvaluateActivity.this, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
//                ptvNewProduct.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
//                ptvNewProduct.onFooterRefreshComplete();
                ToastUtils.show(EvaluateActivity.this, "服务器繁忙");
                ptrLayout.onHeaderRefreshComplete();
                ptrLayout.onFooterRefreshComplete();
                ptrLayout.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
            }
        });
    }
}
