package com.minfo.carrepair.activity.publish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.activity.order.OrderDetailActivity;
import com.minfo.carrepair.activity.order.OrderSureActivity;
import com.minfo.carrepair.activity.query.ProductQueryActivity;
import com.minfo.carrepair.activity.shop.ProductDetailActivity;
import com.minfo.carrepair.adapter.BaseViewHolder;
import com.minfo.carrepair.adapter.CommonAdapter;
import com.minfo.carrepair.entity.shop.Product;
import com.minfo.carrepair.utils.ToastUtils;
import com.minfo.carrepair.widget.PullToRefreshView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Vin查询
 */
public class VinSearchActivity extends BaseActivity implements View.OnClickListener{
    private int page = 1;
    private ImageView ivBack;
    private TextView tvTitle;
    private List<Product> productList = new ArrayList<>();
    private List<Product> tempList = new ArrayList<>();
    private boolean isrefresh;
    private boolean isload;
    private PullToRefreshView ptrLayout;
    private ListView listView;
    public CommonAdapter<Product> adapter;
    private EditText etQuery;
    private TextView tvQuery;
    private LinearLayout llQuery;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_vin_search);
//        setContentView(R.layout.activity_vin_search);
    }

    @Override
    protected void findViews() {
//        ptrLayout = ((PtrClassicFrameLayout) findViewById(R.id.ptr_layout));
        ptrLayout = (PullToRefreshView) findViewById(R.id.ptr_product);

        listView = (ListView) findViewById(R.id.lv_product);
        llQuery = (LinearLayout) findViewById(R.id.ll_query);
        tvQuery = (TextView) findViewById(R.id.tv_query);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        ivBack.setOnClickListener(this);
        tvTitle.setText("车架号查询");
    }

    @Override
    protected void initViews() {
        for (int i = 0; i < 3; i++) {
            productList.add(new Product());

        }
        initAdapter();

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(mActivity, ProductDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("product", productList.get(position));
//                intent.putExtra("info", bundle);
//                startActivity(intent);
                startActivity(new Intent(mActivity, ProductDetailActivity.class));
            }
        });
        if (isOnline()) {
            //showWait();
            //reqData();
        } else {
            ToastUtils.show(mActivity, "暂无网络");
        }

//        ptrLayout = (PullToRefreshView) findViewById(R.id.ptr_order);

        ptrLayout.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {

                if (isOnline()&&!isload) {
                    isload = true;
                    page++;
//                    reqData();
                }
            }
        });
        ptrLayout.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {

                if (isOnline()&&!isrefresh) {
                    isrefresh = true;
                    page = 1;
//                    reqData();
                }
            }
        });
        ptrLayout.setLastUpdated(new Date().toLocaleString());
        ptrLayout.setHorizontalScrollBarEnabled(true);

    }

    private void initAdapter() {
        adapter = new CommonAdapter<Product>(this, productList, R.layout.item_vin_search) {
            @Override
            public void convert(BaseViewHolder helper, Product item, int position) {
//                TextView tvNum = (TextView) helper.getView(R.id.tv_num);
//                tvNum.setText(position+1+"");
//                TextView tvState = (TextView) helper.getView(R.id.tv_state);
//                String status=item.getStatus();
//                if (status.equals("0")){
//                    tvState.setText("待处理");
//                }else  if (status.equals("1")){
//                    tvState.setText("已批准");
//                }else  if (status.equals("3")){
//                    tvState.setText("已拒绝");
//                }
////                else  if (status.equals("2")){
////                    tvState.setText("已结算");
////                }
//                helper.setText(R.id.tv_price,item.getMoney());
//                helper.setText(R.id.tv_time,item.getIntime());


            }
        };
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                finish();
                break;
            case R.id.tv_query: // 查询

                break;

        }
    }
}
