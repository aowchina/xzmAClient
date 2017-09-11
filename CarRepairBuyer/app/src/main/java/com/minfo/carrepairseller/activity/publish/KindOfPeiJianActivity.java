package com.minfo.carrepairseller.activity.publish;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.adapter.BaseViewHolder;
import com.minfo.carrepairseller.adapter.CommonAdapter;
import com.minfo.carrepairseller.entity.publish.ProductKind;
import com.minfo.carrepairseller.entity.publish.ProductKindFirst;
import com.minfo.carrepairseller.entity.publish.ProductKindSecond;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 上传选择配件类别
 */
public class KindOfPeiJianActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView ivBack;
    private ListView lvFirst, lvSecond;//lvFirst 一级类别列表    lvSecond   二级类别列表
    private List<ProductKindFirst> productKindFirst = new ArrayList<ProductKindFirst>();// 一级类别列表集合
    private List<ProductKindSecond> productKindSecond = new ArrayList<ProductKindSecond>();// 二级类别列表集合

    private CommonAdapter commonFirstAdapter, commonSecondAdapter;//commonFirstAdapter  一级类别列表适配器 commonSecondAdapter  二级类别列表适配器
    private ProductKind productKind;// 请求接口返回总数据实体类
    private ProductKindSecond kindSecond;//二级类别数据实体类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_kind_of_pei_jian);
    }


    @Override
    protected void findViews() {
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        lvFirst = ((ListView) findViewById(R.id.lv_first));
        lvSecond = ((ListView) findViewById(R.id.lv_second));

    }

    @Override
    protected void initViews() {

        tvTitle.setText("选择配件");
        ivBack.setOnClickListener(this);

        commonFirstAdapter = new CommonAdapter(this, productKindFirst, R.layout.item_kind_peijian) {
            @Override
            public void convert(BaseViewHolder helper, Object item, int position) {
                helper.setText(R.id.tv_name, productKindFirst.get(position).getName());
            }
        };
        lvFirst.setAdapter(commonFirstAdapter);
        lvFirst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productKindSecond.clear();
                productKindSecond.addAll(productKindFirst.get(position).getChild());
                commonSecondAdapter.notifyDataSetChanged();

            }
        });

        commonSecondAdapter = new CommonAdapter(this, productKindSecond, R.layout.item_kind_peijian) {
            @Override
            public void convert(BaseViewHolder helper, Object item, int position) {
                helper.setText(R.id.tv_name, productKindSecond.get(position).getName());

            }
        };
        lvSecond.setAdapter(commonSecondAdapter);
        lvSecond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                kindSecond = productKindSecond.get(position);
                //上传配件界面返回类别id
                String kindID=kindSecond.getId();
                String name=kindSecond.getName();
                Intent intent = new Intent();
                intent.putExtra("id", kindID);
                intent.putExtra("name", name);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
        //请求数据
        if (isOnline()) {
            showWait();
            productKindFirst.clear();
            productKindSecond.clear();
            reqProductKind();
        } else {
            ToastUtils.show(mActivity, "暂无网络");
        }
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
     * @funcation 请求配件分类接口
     ****/
    public void reqProductKind() {

        String url = getResources().getString(R.string.api_baseurl) + "goods/PjType.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid);

        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                loading.dismiss();
                if (response != null) {
                    productKind = response.getObj(ProductKind.class);
                    if (productKind != null) {
                        productKindFirst.addAll(productKind.getType());
                        productKindSecond.addAll(productKindFirst.get(0).getChild());
                        commonFirstAdapter.notifyDataSetChanged();
                        commonSecondAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                loading.dismiss();
                ToastUtils.show(KindOfPeiJianActivity.this, "服务器繁忙");

            }

            @Override
            public void onRequestError(int code, String msg) {
                loading.dismiss();

                ToastUtils.show(KindOfPeiJianActivity.this, "服务器繁忙");

            }
        });
    }
}
