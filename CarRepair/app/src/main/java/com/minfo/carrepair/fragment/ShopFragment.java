package com.minfo.carrepair.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.login.LoginActivity;
import com.minfo.carrepair.activity.shop.ProductDetailActivity;
import com.minfo.carrepair.adapter.shop.ProductAdapter;
import com.minfo.carrepair.entity.ProductItem;
import com.minfo.carrepair.entity.shop.AdvertItem;
import com.minfo.carrepair.entity.shop.Product;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.ToastUtils;
import com.minfo.carrepair.widget.ImageCycleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends BaseFragment {
    private int page = 1;
    private ProductAdapter adapter;
    private List<ProductItem> productList = new ArrayList<>();
    private List<ProductItem> tempList = new ArrayList<>();
    private boolean isRefresh;
    private boolean isLoad;

    private ImageCycleView icv_advert;
    private PtrClassicFrameLayout ptrLayout;
    private ListView listView;
    String[] urls = {"http://woall.b0.upaiyun.com/woall/sowimg/422942732cc314607ed8a7e59b281b20.jpg",
            "http://woall.b0.upaiyun.com/woall/sowimg/99141f6478f1f6ea11b98512f4d01994.jpg",
            "http://woall.b0.upaiyun.com/woall/sowimg/75d2f5e840c9fd3f65b6e274dc72b466.jpg"};
    private List<AdvertItem> advertItems = new ArrayList<>();

    public ShopFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initViews() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_shop, null);
        ptrLayout = ((PtrClassicFrameLayout) view.findViewById(R.id.ptr_layout));

        icv_advert = (ImageCycleView) view.findViewById(R.id.icv_advert);
        listView = ((ListView) view.findViewById(R.id.lv_shop));
        int width = utils.getScreenWidth();
        int height = width / 2;
        ViewGroup.LayoutParams params = icv_advert.getLayoutParams();
        params.width = width;
        params.height = height;
        icv_advert.setLayoutParams(params);
//        falseData(icv_advert);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        for (int i = 0; i <3 ; i++) {
            productList.add(new ProductItem());

        }
        adapter = new ProductAdapter(mActivity, productList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("details", productList.get(position));
                intent.putExtra("info", bundle);
                startActivity(intent);
//                startActivity(new Intent(mActivity, ProductDetailActivity.class));
            }
        });
        if (isOnline()) {
            reqProduct();
        } else {
            ToastUtils.show(mActivity, "暂无网络");
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

    private void falseData(ImageCycleView cycleView) {
//        advertItems.clear();
//        categoryItems.clear();
//        seckillItems.clear();
//        if(isRefresh) {
//            isRefresh = false;
//            recommentItems.clear();
//        }
//        isLoad = false;
//
        for (int i = 0; i < urls.length; i++) {
            AdvertItem item = new AdvertItem();
            item.setUrl(urls[i]);
            item.setLinkUrl(urls[i]);
            advertItems.add(item);
        }
//        for(int i = 0; i < 10; i++) {
//            CategoryItem item = new CategoryItem();
//            item.setResId(R.mipmap.ic_launcher);
//            item.setName("类别"+i);
//            categoryItems.add(item);
//        }
//        for(int i = 0; i < 10; i ++) {
//            SeckillItem item = new SeckillItem();
//            item.setResId(R.drawable.false_seckill_icon);
//            item.setPrice(10*(i+1)+".00");
//            item.setSeckillPrice(10*(i+1)+".00");
//            seckillItems.add(item);
//        }
//        for(int i = 0; i < 5; i ++) {
//            RecommentItem item = new RecommentItem();
//            item.setResId(R.drawable.false_recomment_icon);
//            item.setName("商品"+i);
//            item.setInfo("这是一个歪果商品");
//            recommentItems.add(item);
//        }
        cycleView.setImageResources(advertItems, new ImageCycleView.ImageCycleViewListener() {
            @Override
            public void displayImage(String imageURL, ImageView imageView) {
                //UniversalImageUtils.displayImage(imageURL, imageView);
            }

            @Override
            public void onImageClick(int position, View imageView) {
                ToastUtils.show(mActivity, "position=" + position);
            }
        });
//
//        bindCategoryView();
//
    }


    /****
     * @funcation 请求商城列表接口
     ****/
    public void reqProduct() {

        String url = getResources().getString(R.string.api_baseurl) + "goods/List.php";
        Map<String, String>  params = utils.getParams(hdUtils.getBasePostStr() + "*" + Constant.userid + "*" + page);

        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
//                loading.dismiss();
                cleanWait();
                Log.e("shop",response.getData());
                if (isRefresh) {
                    isRefresh = false;
                    //newProductList.clear();
                }
//                tempNewProductList = response.getList(HomeProductDetails.class);
//                newProductList.addAll(tempNewProductList);
//
//                ptvNewProduct.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
//                ptvNewProduct.onFooterRefreshComplete();
//                npadapter.notifyDataSetChanged();
//                if (page > 1) {
//                    if (tempNewProductList.size() > 0) {
//                        npadapter.notifyDataSetChanged();
//                        ToastUtils.show(mContext, "加载成功");
//                    } else {
//                        ToastUtils.show(mContext, "无更多内容");
//                    }
//                }
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
//                loading.dismiss();
                cleanWait();
                int errorcode = response.getErrorcode();
                isRefresh = false;
                if(errorcode == 10) {
                    utils.jumpTo(mActivity, LoginActivity.class);
                    return;
                }
//                ptvNewProduct.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
//                ptvNewProduct.onFooterRefreshComplete();
//                int errorcode = response.getErrorcode();
//                if (errorcode == 11 || errorcode == 13) {
//                    Utils.intent2Class(getActivity(), Login.class);
//                } else {
//                    ToastUtils.show(getActivity(), "服务器繁忙");
//                }
            }

            @Override
            public void onRequestError(int code, String msg) {
//                loading.dismiss();
                cleanWait();
//                ptvNewProduct.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
//                ptvNewProduct.onFooterRefreshComplete();
            }
        });
    }
}
