package com.minfo.carrepairseller.fragment;


import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.login.LoginActivity;
import com.minfo.carrepairseller.adapter.BaseViewHolder;
import com.minfo.carrepairseller.adapter.CommonAdapter;
import com.minfo.carrepairseller.entity.query.ChexiEntity;
import com.minfo.carrepairseller.entity.query.ChexiItem;
import com.minfo.carrepairseller.entity.query.PinpaiEntity;
import com.minfo.carrepairseller.entity.query.PinpaiItem;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ConstantUrl;
import com.minfo.carrepairseller.utils.LG;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.minfo.carrepairseller.utils.UniversalImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class XiChoseFragment extends BaseFragment {

    private List<ChexiItem> dList = new ArrayList<ChexiItem>();
    private List<ChexiItem> tempList;
    private CommonAdapter commonAdapter;//产品
    private TextView tvNameCar;
    private GridView gridView;
    private JumpFromXiFragmentListener jumpFragmentListener;
//    PinpaiEntity paiEntity; // 选中的品牌
    PinpaiItem paiEntity; // 选中的品牌
    private boolean isFirst = true;

    public interface JumpFromXiFragmentListener {
        void jumpFromXiFragment(ChexiItem item);
    }
    public XiChoseFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initViews() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_xi_chose, null);
        jumpFragmentListener=(JumpFromXiFragmentListener) mActivity;

        tvNameCar = ((TextView) view.findViewById(R.id.tv_name_car));
        gridView = ((GridView) view.findViewById(R.id.gr_gridview));
//        for (int i = 0; i < 5; i++) {
//            ChexiItem ChexiItem=new ChexiItem();
//            ChexiItem.setName("玛莎拉蒂"+i);
//            dList.add(ChexiItem);
//
//        }
        commonAdapter = new CommonAdapter<ChexiItem>(mActivity, dList, R.layout.item_xing_car) {
            @Override
            public void convert(BaseViewHolder helper, ChexiItem item, int position) {
                TextView textView = helper.getView(R.id.tv_name);
                ImageView imageView = helper.getView(R.id.iv_car);
                int size = (utils.getScreenWidth() - utils.dp2px(50)-utils.dp2px(15)) / 4;
                imageView.setLayoutParams(new LinearLayout.LayoutParams(size, size));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                textView.setText(item.getName());
                UniversalImageUtils.displayImageUseDefOptions(item.getIcon(), imageView);

            }
        };
        gridView.setAdapter(commonAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtils.show(mActivity, position+"");
                jumpFragmentListener.jumpFromXiFragment(dList.get(position));
            }
        });


        return view;
    }

    /**
     * 设置车系
     * @param pinPaiEntity
     */
    public void setPinpaiItem(PinpaiItem pinPaiEntity) {
        this.paiEntity = pinPaiEntity;
//        reqData();
    }

    /**
     * 请求品牌下的车系
     */
    private void reqData() {
        if(paiEntity == null) {
            ToastUtils.show(mActivity, "请先选择品牌");
            tvNameCar.setText("请先选择品牌");
            dList.clear();
            commonAdapter.notifyDataSetChanged();
            return;
        }

        boolean isDebug = false;
        if(isDebug) {
            cleanWait();
            dList.clear();
            for(int i = 0; i < 5; i++) {
                ChexiItem item = new ChexiItem();
                item.setName(""+(i+5));

                dList.add(item);
            }
            commonAdapter.notifyDataSetChanged();
            return;
        }

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid+"*"+paiEntity.getId());

        final String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_CAR_CHEXI_LIST;
        httpClient.post(url, params, new RequestListener() {
            @Override public void onPreRequest() {
                showWait();
            }


            @Override public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if(response != null) {
                    dList.clear();
                    ChexiEntity entity = response.getObj(ChexiEntity.class);
                    if(entity != null ) {
                        List<ChexiItem> items = entity.getList();
                        tvNameCar.setText(entity.getName());
                        //适配数据
                        if (items != null && items.size() > 0) {
                            dList.addAll(items);
                        }
                    }
                    else {
                        LG.e("车系", "数据解析异常");
                    }
                    if(dList.size() == 0) {
                        ToastUtils.show(mActivity, "该品牌下暂无车系信息");
                    }
                    commonAdapter.notifyDataSetChanged();
                }
                else {
                    ToastUtils.show(mActivity,"请求服务器异常");
                }
            }


            @Override public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity,"请求服务器异常");
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
                        ToastUtils.show(mActivity,"服务器繁忙");
                        LG.e("车系列表", "errorcode="+errorcode);
                        break;
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        LG.e("车系", "isFirst="+isFirst);
        if(isFirst) {
            isFirst = false;
        }
        else {
            if(utils.isOnLine()) {
                reqData();
            }
            else {
                ToastUtils.show(mActivity, R.string.no_internet);
            }
        }
    }

}