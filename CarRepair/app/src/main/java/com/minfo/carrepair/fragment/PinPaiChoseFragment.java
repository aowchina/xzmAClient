package com.minfo.carrepair.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.login.LoginActivity;
import com.minfo.carrepair.activity.query.EPCDetailActivity;
import com.minfo.carrepair.adapter.BaseViewHolder;
import com.minfo.carrepair.adapter.CommonAdapter;
import com.minfo.carrepair.adapter.query.PinpaiTitleAdapter;
import com.minfo.carrepair.entity.query.PinpaiEntity;
import com.minfo.carrepair.entity.query.PinpaiItem;
import com.minfo.carrepair.entity.query.PinpaiModel;
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

/**
 * A simple {@link Fragment} subclass.
 * 品牌列表fragment
 */
public class PinPaiChoseFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private ListView lvTitle, lvPinPai;
    //种类
    private PinpaiTitleAdapter kindAdapter; // 种类

    private PtrClassicFrameLayout ptrLayout;

    private List<PinpaiEntity> dList = new ArrayList<PinpaiEntity>();

    private List<PinpaiEntity> tempList;
    private CommonAdapter<PinpaiEntity> commonAdapter; // 产品
    private JumpFragmentListener jumpFragmentListener;
    public interface JumpFragmentListener {
        void jumpFragment(PinpaiItem PinpaiEntity);
    }
//    public void setOnJumpFragmentListener(){
//        jumpFragmentListener.jumpFragment();
//    }
    public PinPaiChoseFragment() {
        // Required empty public constructor
    }

    public static PinPaiChoseFragment newInstance() {
        PinPaiChoseFragment fragment = new PinPaiChoseFragment();
        return fragment;
    }
    @Override
    protected View initViews() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_pin_pai_chose, null);
        //ptrLayout = ((PtrClassicFrameLayout) view.findViewById(R.id.ptr_layout));
        jumpFragmentListener=(JumpFragmentListener) mActivity;
        lvTitle = ((ListView) view.findViewById(R.id.lv_title));
        lvPinPai = ((ListView) view.findViewById(R.id.lv_pinpai));


        kindAdapter = new PinpaiTitleAdapter(mActivity, dList);
        lvTitle.setAdapter(kindAdapter);
        lvTitle.setOnItemClickListener(this);

        commonAdapter = new CommonAdapter<PinpaiEntity>(mActivity, dList, R.layout.item_pinpai_car) {
            @Override
            public void convert(BaseViewHolder helper, PinpaiEntity item, int position) {
                final int pos = position;
                TextView tvKindName = helper.getView(R.id.tv_kind_product);
                tvKindName.setText(item.getName());
                GridView gridView = helper.getView(R.id.gr_gridview);
                List<PinpaiItem> pinpaiItems = new ArrayList<>();
                List<PinpaiItem> items = dList.get(position).getList();
                pinpaiItems.clear();
                if(items != null && items.size()>0){
                    pinpaiItems.addAll(items);
                }
                CommonAdapter adapter = new CommonAdapter<PinpaiItem>(mActivity, pinpaiItems, R.layout.item_xing_car) {
                    @Override
                    public void convert(BaseViewHolder helper, PinpaiItem item, int position) {
                        TextView textView = helper.getView(R.id.tv_name);
                        ImageView imageView = helper.getView(R.id.iv_car);
                        int size = (utils.getScreenWidth() - utils.dp2px(130)-utils.dp2px(15)) / 4;
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(size, size));
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        textView.setText(item.getName());
                        UniversalImageUtils.displayImageUseDefOptions(item.getIcon(), imageView);
                    }
                };
                gridView.setAdapter(adapter);
                setListViewHeightBasedOnChildren(gridView);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        ToastUtils.show(mActivity, position + " " + pos);
                        jumpFragmentListener.jumpFragment(dList.get(pos).getList().get(position));
                    }
                });

            }
        };
        lvPinPai.setAdapter(commonAdapter);
        lvPinPai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(mActivity, EPCDetailActivity.class));

            }
        });
        if(isOnline()) {
            reqPinpaiList();
        }
        else {
            ToastUtils.show(mActivity, R.string.no_internet);
        }
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        kindAdapter.setSelectedPosition(position);
        lvPinPai.setSelection(position);
    }

    /**
     * 加载数据后，计算gridview高度
     *
     * @param gridView
     */
    private void setListViewHeightBasedOnChildren(GridView gridView) {

        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int height = listAdapter.getCount() % 4 == 0 ? listAdapter.getCount() / 4 : listAdapter.getCount() / 4 + 1;
        for (int i = 0; i < height; i++) {
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight
                + (utils.dp2px(5) * (height - 1));
        gridView.setLayoutParams(params);
    }

    /**
     * 请求品牌列表接口
     */
    private void reqPinpaiList() {
        boolean isDebug = false;
        if(isDebug) {
            cleanWait();
            dList.clear();
            for(int i = 0; i < 5; i++) {
                PinpaiEntity item = new PinpaiEntity();
                item.setName(""+(i+5));

                dList.add(item);
            }
            commonAdapter.notifyDataSetChanged();
            return;
        }

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid);

        final String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_CAR_PINPAI_LIST;
        httpClient.post(url, params, new RequestListener() {
            @Override public void onPreRequest() {
                showWait();
            }


            @Override public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if(response != null) {
                    dList.clear();
                    PinpaiModel model = response.getObj(PinpaiModel.class);
                    if(model != null) {
                        List<PinpaiEntity> entity = model.getList();
                        //适配数据
                        if (entity != null && entity.size() > 0) {
                            dList.addAll(entity);

                        }
                        kindAdapter.notifyDataSetChanged();
                        commonAdapter.notifyDataSetChanged();
                    }
                    else {
                        LG.e("品牌", "数据解析失败");
                    }
                }
                else {
                    ToastUtils.show(mActivity,"请求数据失败");
                }
            }


            @Override public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity, R.string.network_error);
            }


            @Override public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                // 9 10 11 300 301
                switch (errorcode) {

                    case 10:
                    case 11:
                        ToastUtils.show(mActivity,"账号异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 9:
                    case 300:
                    case 301:
                    default:
                        LG.e("车系列表", "errorcode="+errorcode);
                        break;
                }

            }
        });
    }
}
