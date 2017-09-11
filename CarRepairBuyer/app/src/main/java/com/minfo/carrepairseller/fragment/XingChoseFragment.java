package com.minfo.carrepairseller.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.adapter.BaseViewHolder;
import com.minfo.carrepairseller.adapter.CommonAdapter;
import com.minfo.carrepairseller.adapter.query.ChexingTitleAdapter;
import com.minfo.carrepairseller.entity.query.ChexiItem;
import com.minfo.carrepairseller.entity.query.ChexingEntity;
import com.minfo.carrepairseller.entity.query.ChexingItem;
import com.minfo.carrepairseller.entity.query.ChexingModel;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ConstantUrl;
import com.minfo.carrepairseller.utils.LG;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.minfo.carrepairseller.utils.UniversalImageUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class XingChoseFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    //种类
    private ChexingTitleAdapter kindAdapter; // 种类
    private ListView lvTitle, lvXing;
    private TextView tvTitle;
    private TextView tvYear;
    private List<ChexingEntity> dList = new ArrayList<>();
    private List<ChexingItem> items = new ArrayList<>();
    private List<ChexingItem> checkedItems = new LinkedList<>();
    private CommonAdapter commonAdapter;//产品
    private ChexiItem item;
    private boolean isFirst = true;
    private boolean isPurchase = false;

    public XingChoseFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initViews() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            isPurchase = bundle.getBoolean("isPurchase", false);
        }
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_xing_chose, null);

        tvTitle = (TextView) view.findViewById(R.id.tv_xi_car);
        tvYear = (TextView) view.findViewById(R.id.tv_year);
        lvTitle = ((ListView) view.findViewById(R.id.lv_title));
        lvXing = ((ListView) view.findViewById(R.id.lv_xing_car));


        kindAdapter = new ChexingTitleAdapter(mActivity, dList);

        lvTitle.setAdapter(kindAdapter);
        lvTitle.setOnItemClickListener(this);



        commonAdapter = new CommonAdapter<ChexingItem>(mActivity, items, R.layout.item_xingcar) {
            @Override
            public void convert(BaseViewHolder helper, ChexingItem item, final int position) {
                ImageView imageView = helper.getView(R.id.iv_car);
                helper.setText(R.id.tv_name,item.getIssuedate()+" "+ item.getName());
                helper.setText(R.id.tv_nprice, item.getPrice());
                UniversalImageUtils.displayImageUseDefOptions(item.getIcon(), imageView);

            }
        };
        lvXing.setAdapter(commonAdapter);

        lvXing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //startActivity(new Intent(mActivity, EPCDetailActivity.class));
                if (!isPurchase) {
                    Intent intent = new Intent();

                    intent.putExtra(Constant.CAR_TYPE, items.get(position));
                    mActivity.setResult(mActivity.RESULT_OK, intent);
                    mActivity.finish();

                } else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("car", items.get(position));
                    intent.putExtra("info", bundle);
                    mActivity.setResult(mActivity.RESULT_OK, intent);
                    mActivity.finish();
//
                }
            }
        });


        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        items.clear();
//        checkedItems.clear();
        ChexingEntity entity = dList.get(position);
        if (entity != null) {
            tvYear.setText(entity.getName());
            List<ChexingItem> its = entity.getList();
            if (its != null && its.size() > 0) {
                for(ChexingItem item : its) {
                    if(checkedItems.contains(item)) {
                        item.setChoseFalg(true);
                    }
                    else {
                        item.setChoseFalg(false);
                    }
                    items.add(item);
                }

            }
        }
        kindAdapter.setSelectedPosition(position);
        commonAdapter.notifyDataSetChanged();

    }

    /**
     * 设置车系
     *
     * @param chexiItem
     */
    public void setChexiItem(ChexiItem chexiItem) {
        this.item = chexiItem;
//        reqData();
    }

    /**
     * 请求品牌下的车系
     */
    private void reqData() {
        if (item == null) {
            ToastUtils.show(mActivity, "请先选择车系");
//            tvNameCar.setText("请先选择品牌");
            return;
        }
        boolean isDebug = false;
        if (isDebug) {
            cleanWait();
            dList.clear();
            for (int i = 0; i < 5; i++) {
                ChexingEntity item = new ChexingEntity();
                item.setName("" + (i + 5));
                List<ChexingItem> its = new ArrayList<>();
                for (int j = 0; j < i; i++) {
                    ChexingItem item1 = new ChexingItem();
                    its.add(item1);
                }
                item.setList(its);
                dList.add(item);
            }
            kindAdapter.notifyDataSetChanged();
            commonAdapter.notifyDataSetChanged();
            return;
        }

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + item.getId());

        final String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_CAR_CHEXING_LIST;
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                Log.e("xing ",response.getData());
                if (response != null) {
                    dList.clear();
                    items.clear();
                    checkedItems.clear();
                    ChexingModel model = response.getObj(ChexingModel.class);
                    if (model != null) {
                        tvTitle.setText(model.getName());
                        List<ChexingEntity> ites = model.getList();
                        //适配数据
                        if (ites != null && ites.size() > 0) {
                            dList.addAll(ites);
                            ChexingEntity entity = dList.get(0);
                            if (entity != null) {
                                tvYear.setText(entity.getName());
                                List<ChexingItem> its = entity.getList();
                                if (its != null && its.size() > 0) {
                                    items.addAll(its);
                                    kindAdapter.setSelectedPosition(0);
                                }
                            }

                        }
                    } else {
                        LG.e("车型列表", "数据解析失败");
                    }
                    if(items.size() == 0) {
                        ToastUtils.show(mActivity, "暂无该车系的车款信息");
                    }
                    commonAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.show(mActivity, "服务器异常，请稍后再试");
                }
            }


            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity, msg);
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if (errorcode == 12 || errorcode == 18) {
                    ToastUtils.show(mActivity, "账号异常，请重新登录");
//                    utils.jumpTo(mActivity, LoginActivity.class);
                } else {
                    ToastUtils.show(mActivity, "服务器繁忙");
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        LG.e("车型", "isFirst=" + isFirst);
        if (isFirst) {
            isFirst = false;
        } else {
            if(utils.isOnLine()) {
                reqData();
            }
            else {
                ToastUtils.show(mActivity, R.string.no_internet);
            }
        }
    }

    /**
     * 获取上传配件 车型信息
     */
    public void getCheckId() {
        String id = "";
        if (checkedItems.size() == 0) {
            ToastUtils.show(mActivity, "请选择车型");
            return;
        }
        for (int i = 0; i < checkedItems.size(); i++) {
            id += checkedItems.get(i).getId();
            if (i < checkedItems.size() - 1) {
                id += ",";
            }
        }
//        ToastUtils.show(mActivity, "id=" + id);
        Intent intent = new Intent();
        intent.putExtra("id", id);
        intent.putExtra("count", checkedItems.size() + "");
        Log.e("count id  ", id + " " + checkedItems.size());
        mActivity.setResult(mActivity.RESULT_OK, intent);
        mActivity.finish();
    }
}
