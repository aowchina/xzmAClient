package com.minfo.carrepairseller.fragment;


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

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.MainActivity;
import com.minfo.carrepairseller.activity.login.LoginActivity;
import com.minfo.carrepairseller.activity.shop.ProductDetailActivity;
import com.minfo.carrepairseller.activity.shop.WebViewActivity;
import com.minfo.carrepairseller.adapter.shop.ProductAdapter;
import com.minfo.carrepairseller.entity.ProductItem;
import com.minfo.carrepairseller.entity.shop.AdvertItem;
import com.minfo.carrepairseller.entity.shop.ShopModel;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.LG;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.minfo.carrepairseller.utils.UniversalImageUtils;
import com.minfo.carrepairseller.widget.ImageCycleView;
import com.minfo.carrepairseller.widget.PullToRefreshView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * 商场界面
 */
public class ShopFragment extends BaseFragment implements View.OnClickListener{
    private int page = 1;
    private ProductAdapter adapter;
    private List<ProductItem> productList = new ArrayList<>();
    private List<ProductItem> tempList = new ArrayList<>();
    private boolean isRefresh;
    private boolean isLoad;

    private ImageCycleView icv_advert;
    //    private PtrClassicFrameLayout ptrLayout;
    private PullToRefreshView ptrLayout;
    private ListView listView;
    private ImageView ivSearch;
//    String[] urls = {"http://woall.b0.upaiyun.com/woall/sowimg/422942732cc314607ed8a7e59b281b20.jpg",
//            "http://woall.b0.upaiyun.com/woall/sowimg/99141f6478f1f6ea11b98512f4d01994.jpg",
//            "http://woall.b0.upaiyun.com/woall/sowimg/75d2f5e840c9fd3f65b6e274dc72b466.jpg"};
    private List<AdvertItem> advertItems = new ArrayList<>();

    public ShopFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initViews() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_shop, null);
//        ptrLayout = ((PtrClassicFrameLayout) view.findViewById(R.id.ptr_layout));
        ptrLayout = (PullToRefreshView) view.findViewById(R.id.ptr_layout);
        listView = ((ListView) view.findViewById(R.id.lv_shop));
        ivSearch = (ImageView) view.findViewById(R.id.iv_seach);

        View viewTop = LayoutInflater.from(mActivity).inflate(R.layout.fragment_shop_top, null);
        icv_advert = (ImageCycleView) viewTop.findViewById(R.id.icv_advert);

        int width = utils.getScreenWidth();
        int height = (int) (width / 5.0 * 3);
        ViewGroup.LayoutParams params = icv_advert.getLayoutParams();
        params.width = width;
        params.height = height;
        icv_advert.setLayoutParams(params);
        //falseData(icv_advert);
        listView.addHeaderView(viewTop);
        adapter = new ProductAdapter(mActivity, productList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("details", productList.get(position - 1));
                    intent.putExtra("info", bundle);
                    startActivity(intent);
                }
//                startActivity(new Intent(mActivity, ProductDetailActivity.class));
            }
        });
        ptrLayout.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                //加载更多功能的代码
                if (isOnline()) {
                    page++;
                    isLoad = true;
                    reqProduct();
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
                    reqProduct();
                } else {
                    ToastUtils.show(mActivity, "暂无网络");
                }
            }
        });

        ivSearch.setOnClickListener(this);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();


        if (isOnline()) {
            isRefresh = true;
            reqProduct();
        } else {
            ToastUtils.show(mActivity, "暂无网络");
        }
//        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                isRefresh = true;
//                page = 1;
//                if (isOnline()) {
//
//                    reqProduct();
//                } else {
//                    ToastUtils.show(mActivity, "暂无网络");
//                }
//            }
//
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return super.checkCanDoRefresh(frame, listView, header);
//            }
//        });
//
//        //上拉加载数据
//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                // 当不滚动时
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
//                    // 判断是否滚动到底部
//                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
//                        //加载更多功能的代码
//                        if (isOnline()) {
//                            page++;
//                            isLoad = true;
//                            reqProduct();;
//                        } else {
//                            ToastUtils.show(mActivity, "暂无网络");
//                        }
//
//                    }
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//            }
//        });
    }

    private void falseData(ImageCycleView cycleView) {

//        for (int i = 0; i < urls.length; i++) {
//            AdvertItem item = new AdvertItem();
//            item.setUrl(urls[i]);
//            item.setLinkUrl(urls[i]);
//            advertItems.add(item);
//        }

        cycleView.setImageResources(advertItems, new ImageCycleView.ImageCycleViewListener() {
            @Override
            public void displayImage(String imageURL, ImageView imageView) {
                //UniversalImageUtils.displayImage(imageURL, imageView);
                UniversalImageUtils.displayImage(imageURL, imageView);
            }

            @Override
            public void onImageClick(int position, View imageView) {

                String goodid = advertItems.get(position).getGoodid();
                if ("0".equals(goodid)) {
                    ToastUtils.show(mActivity, "跳转链接position=" + position);
                } else {
                    Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                    Bundle bundle = new Bundle();
                    ProductItem item = new ProductItem();
                    item.setGoodid(goodid);
                    bundle.putSerializable("details", item);
                    intent.putExtra("info", bundle);
                    startActivity(intent);
                }
            }
        });

    }


    /****
     * @funcation 请求商城列表接口
     ****/
    public void reqProduct() {

        String url = getResources().getString(R.string.api_baseurl) + "goods/List.php";
        Map<String, String> params = utils.getParams(hdUtils.getBasePostStr() + "*" + Constant.userid + "*" + page);

        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                Log.e("shop", response.getData());
                if (isRefresh) {
                    isRefresh = false;
                    //newProductList.clear();
                    productList.clear();
//                     ptrLayout.refreshComplete();


                }
                if (isLoad) {
                    isLoad = false;
                }
                ShopModel model = response.getObj(ShopModel.class);
                if (model != null) {
                    final List<AdvertItem> items = model.getAd();
                    List<ProductItem> goods = model.getGoods();
                    if (items != null && items.size() > 0) {
                        advertItems.clear();
                        advertItems.addAll(items);
                        icv_advert.setImageResources(advertItems, new ImageCycleView.ImageCycleViewListener() {
                            @Override
                            public void displayImage(String imageURL, ImageView imageView) {
                                //UniversalImageUtils.displayImage(imageURL, imageView);
                                UniversalImageUtils.displayImage(imageURL, imageView);
                            }

                            @Override
                            public void onImageClick(int position, View imageView) {
                                if (items.get(position).getGoodid().equals("0")) {
                                    Intent intent = new Intent(mActivity, WebViewActivity.class);
                                    intent.putExtra("info", items.get(position).getLink());
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                                    Bundle bundle = new Bundle();
                                    ProductItem productItem=new ProductItem();
                                    productItem.setGoodid(items.get(position).getGoodid());
                                    bundle.putSerializable("details", productItem);
                                    intent.putExtra("info", bundle);
                                    startActivity(intent);
                                }

                            }
                        });
                    }
                    if (goods != null && goods.size() > 0) {
                        productList.addAll(goods);
                    }

                    adapter.notifyDataSetChanged();
                    ptrLayout.onFooterRefreshComplete();
                    ptrLayout.onHeaderRefreshComplete();
                    ptrLayout.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());

                } else {
                    ToastUtils.show(mActivity, "服务器异常");
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
                cleanWait();
                isRefresh = false;
                ptrLayout.onFooterRefreshComplete();
                ptrLayout.onHeaderRefreshComplete();
                ptrLayout.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
//                ptvNewProduct.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
//                ptvNewProduct.onFooterRefreshComplete();
                int errorcode = response.getErrorcode();
                if (errorcode == 11 || errorcode == 13||errorcode == 10) {
                    utils.jumpTo(mActivity, LoginActivity.class);
                } else {
                    ToastUtils.show(getActivity(), "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                LG.e("商城", "code=" + code + "msg=" + msg);
//                ptvNewProduct.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
//                ptvNewProduct.onFooterRefreshComplete();
                ToastUtils.show(getActivity(), "服务器繁忙");
                ptrLayout.onFooterRefreshComplete();
                ptrLayout.onHeaderRefreshComplete();
                ptrLayout.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        icv_advert.startImageCycle();

    }

    @Override
    public void onPause() {
        super.onPause();
        icv_advert.pushImageCycle();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_seach:
                ((MainActivity)mActivity).setSelect(1);
                break;
        }
    }
}
