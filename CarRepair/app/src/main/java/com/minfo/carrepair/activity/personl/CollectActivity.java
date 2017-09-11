package com.minfo.carrepair.activity.personl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.activity.shop.ProductDetailActivity;
import com.minfo.carrepair.adapter.BaseViewHolder;
import com.minfo.carrepair.adapter.CommonAdapter;
import com.minfo.carrepair.entity.shop.Product;
import com.minfo.carrepair.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class CollectActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle;
    private ImageView ivBack;
    private ListView listView;
    private PtrClassicFrameLayout ptrLayout;
    private CommonAdapter commonAdapter;//产品
    private List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_collect);
    }

    @Override
    protected void findViews() {
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        listView = ((ListView) findViewById(R.id.lv_collect));
        ptrLayout = ((PtrClassicFrameLayout) findViewById(R.id.ptr_layout));

    }

    @Override
    protected void initViews() {
        ivBack.setOnClickListener(this);
        tvTitle.setText("收藏");
        for (int i = 0; i <3 ; i++) {
            productList.add(new Product());

        }

        commonAdapter = new CommonAdapter(this, productList, R.layout.item_product_collect) {
            @Override
            public void convert(BaseViewHolder helper, Object item, int position) {
                TextView textView = helper.getView(R.id.tv_name);
//                ImageView imageView = helper.getView(R.id.iv_car);
//                int size = (utils.getScreenWidth() - utils.dp2px(130)) / 4;
//                imageView.setLayoutParams(new LinearLayout.LayoutParams(size, size));
//                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            }
        };
        listView.setAdapter(commonAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(mActivity, ProductDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("product", productList.get(position));
//                intent.putExtra("info", bundle);
//                startActivity(intent);
                startActivity(new Intent(CollectActivity.this, ProductDetailActivity.class));
            }
        });
        if (isOnline()) {
            //showWait();
            //reqData();
        } else {
            ToastUtils.show(this, "暂无网络");
        }
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

//                isRefresh = true;
//                page = 1;
//                if (isOnline()) {
//                    showWait();
//                    reqData();
//                } else {
//                    ToastUtils.show(mActivity, "暂无网络");
//                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, listView, header);
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
}
