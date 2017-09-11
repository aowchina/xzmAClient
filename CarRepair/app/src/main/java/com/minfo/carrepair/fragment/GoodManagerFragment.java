package com.minfo.carrepair.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.login.LoginActivity;
import com.minfo.carrepair.activity.publish.PartsPublishActivity;
import com.minfo.carrepair.adapter.BaseViewHolder;
import com.minfo.carrepair.adapter.CommonAdapter;
import com.minfo.carrepair.dialog.TipView;
import com.minfo.carrepair.entity.ProductAll;
import com.minfo.carrepair.entity.ProductItem;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.ConstantUrl;
import com.minfo.carrepair.utils.DialogUtil;
import com.minfo.carrepair.utils.ToastUtils;
import com.minfo.carrepair.utils.UniversalImageUtils;
import com.minfo.carrepair.widget.PullToRefreshView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import minfo.com.albumlibrary.utils.LG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GoodManagerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GoodManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 * 商品管理fragment
 * type（1:审核通过出售中 2:审核中 3 :已下架0:审核不通过 ）
 */
public class GoodManagerFragment extends BaseFragment implements View.OnClickListener {

    private ListView lvGood;

    private PullToRefreshView ptrGood;
    private CommonAdapter<ProductItem> commonAdapter;
    private List<ProductItem> list = new ArrayList<>();
    private List<ProductItem> tempList = new ArrayList<>();
    private TextView tvTitle;
    private ImageView ivBack;
    private int page = 1;
    private int type;

    private boolean isrefresh = false;
    private boolean isload = false;
    private ProductAll productAll;
    private int[] types = {1, 3, 2, 0};
    private RelativeLayout rlNoMessage;
    private RelativeLayout rlNoMessageClick;
    private LinearLayout llGoodList;
    private boolean isVisibleToUser = true;

    @Override
    protected View initViews() {
        Bundle bundle = getArguments();
        type = bundle.getInt(Constant.GOOD_STATE, 1);

        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_good_manager, null);

        lvGood = (ListView) view.findViewById(R.id.lv_good);
        ptrGood = (PullToRefreshView) view.findViewById(R.id.ptr_good);
        rlNoMessage = (RelativeLayout) view.findViewById(R.id.rl_no_message);
        rlNoMessageClick = (RelativeLayout) view.findViewById(R.id.rl_no_message_click);
        llGoodList = (LinearLayout) view.findViewById(R.id.ll_good_list);

//        tvTitle = (TextView) view.findViewById(R.id.tv_title);
//        ivBack = (ImageView) view.findViewById(R.id.iv_back);
//
////        tvTitle.setVisibility(View.VISIBLE);
////        ivBack.setVisibility(View.VISIBLE);
//        tvTitle.setText("我的订单");

//        ivBack.setOnClickListener(this);
        rlNoMessageClick.setOnClickListener(this);
        ptrGood.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {

                if (isOnline() && !isload) {
                    isload = true;
                    page++;
                    reqData();
                }
            }
        });
        ptrGood.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {

                if (isOnline() && !isrefresh) {
                    isrefresh = true;
                    page = 1;
                    reqData();
                }
            }
        });
        ptrGood.setLastUpdated(new Date().toLocaleString());
        ptrGood.setHorizontalScrollBarEnabled(true);

        initAdapter();

        lvGood.setAdapter(commonAdapter);


        lvGood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(mActivity, ProductDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("details", list.get(position));
//                    intent.putExtra("info", bundle);
//                    startActivity(intent);
//                if (type == 1) {
//                    Intent intent = new Intent(mActivity, OrderSureActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("ordeDetails", list.get(position));
//                    intent.putExtra("info", bundle);
//                    intent.putExtra("orderid", list.get(position).getOrderNum());
//                    startActivityForResult(intent, 0);
//                } else {
//                    Intent intent = new Intent(mActivity, OrderDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("ordeDetails", list.get(position));
//                    intent.putExtra("info", bundle);
//                    intent.putExtra("type", type);
//
//                    startActivityForResult(intent, 0);
//                }

            }
        });

        if (isOnline()) {
            if (!isrefresh) {
                page = 1;
                if(isVisibleToUser) showWait();
                isrefresh = true;
                reqData();

            }
        } else {
            if(isVisibleToUser) ToastUtils.show(mActivity, "网络异常");
        }

        return view;
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        commonAdapter = new CommonAdapter<ProductItem>(mActivity, list, R.layout.content_good_manager) {
            @Override
            public void convert(BaseViewHolder helper, ProductItem item, final int position) {

                TextView tvLeft = helper.getView(R.id.tv_left);
                TextView tvCenter = helper.getView(R.id.tv_center);
                TextView tvRight = helper.getView(R.id.tv_right);
                ImageView ivGood = helper.getView(R.id.iv_good);
                View view = helper.getView(R.id.view_line);
                View viewLeft = helper.getView(R.id.v_left);

                helper.setText(R.id.tv_name, item.getName());
                helper.setText(R.id.tv_time, item.getCar_name());
                helper.setText(R.id.tv_price, "￥" + item.getPrice());
//                helper.setText(R.id.tv_labe, item.getCar_name());

                UniversalImageUtils.displayImageUseDefOptions(item.getImg(), ivGood);
                int t = types[type];
                switch (t) {
                    case 0: // 审核不通过
                        tvLeft.setText("编辑");
                        tvCenter.setText("审核失败");
                        tvRight.setText("删除");
                        tvRight.setVisibility(View.VISIBLE);
                        view.setVisibility(View.VISIBLE);
                        tvRight.setClickable(true);
                        break;
                    case 1: // 出售中
                        tvLeft.setText("编辑");
                        tvCenter.setText("下架");
                        tvRight.setText("删除");
                        tvRight.setVisibility(View.GONE);
                        view.setVisibility(View.GONE);
                        tvLeft.setVisibility(View.GONE);
                        viewLeft.setVisibility(View.GONE);
                        tvRight.setClickable(false);
                        break;

                    case 2: // 审核中
                        tvLeft.setText("编辑");
                        tvCenter.setText("审核中");
                        tvRight.setText("删除");
                        tvRight.setVisibility(View.VISIBLE);
                        view.setVisibility(View.VISIBLE);
                        tvLeft.setVisibility(View.VISIBLE);
                        viewLeft.setVisibility(View.VISIBLE);
                        tvRight.setClickable(true);
                        break;
                    case 3: // 已下架
                        tvLeft.setText("编辑");
                        tvCenter.setText("上架");
                        tvRight.setText("删除");
                        tvRight.setVisibility(View.GONE);
                        view.setVisibility(View.GONE);
                        tvLeft.setVisibility(View.GONE);
                        viewLeft.setVisibility(View.GONE);
                        tvRight.setClickable(false);
                        break;
                }

                tvLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isVisibleToUser) ToastUtils.show(mActivity, "编辑");
                        Intent intent = new Intent(mActivity, PartsPublishActivity.class);
                        Log.e("idProduct", list.get(position).getGoodid());

                        intent.putExtra("id", list.get(position).getGoodid());
                        startActivityForResult(intent, 110);

                    }
                });
                tvCenter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int t = types[type];
                        switch (t) {
                            case 1: // 下架接口
//                                reqshangxiaGood(position, 0);
                                showEnsureUpDown(position, 0);
                                break;
                            case 2: // 上架接口
//                                reqshangxiaGood(position, 1);

                                break;
                            case 3: // 上架接口
                                showEnsureUpDown(position, 1);
                                break;
                            case 0: // 重新审核接口
                                break;
                        }
                    }
                });
                tvRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 删除
                        showEnsureCancle(position, "");
                    }
                });
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 110:
                if (isOnline()) {
                    if (!isrefresh) {
                        page = 1;
                        isrefresh = true;
                        reqData();
                    }
                } else {
                    if(isVisibleToUser) ToastUtils.show(mActivity, "网络异常");
                }
            break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                mActivity.finish();
                break;
            case R.id.rl_no_message_click:
                if (!isrefresh) {
                    page = 1;
                    isrefresh = true;
                    reqData();
                }
                break;
        }
    }

    /**
     * 请求商品列表数据
     * 上行参数: 8段 * userid *
     */
    private void reqData() {
//        for(int i = 0; i < 5; i ++) {
//            ProductItem entity = new ProductItem();
//            entity.setName("ddd"+i);
//            entity.setPrice(1*5+1+"");
//
//            list.add(entity);
//        }
//        commonAdapter.notifyDataSetChanged();
//        if(true) {
//            cleanWait();
//            return;
//        }

        String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_GOOD_MANAGER;
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + types[type] + "*" + page);
        httpClient.post(url, params, new RequestListener() {

            @Override
            public void onPreRequest() {
                if(isVisibleToUser) showWait();
            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                Log.e("GoodManger", response.toString());
                cleanWait();
                if (isrefresh) {
                    list.clear();
                    isrefresh = false;
                    ptrGood.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                    if(isVisibleToUser) ToastUtils.show(mActivity, "刷新成功");
                }
                if (isload) {
                    isload = false;
                    ptrGood.onFooterRefreshComplete();
                }
                productAll = response.getObj(ProductAll.class);
                if (productAll != null)
                    tempList = productAll.getGoods();
                if (tempList != null) {
                    list.addAll(tempList);
                }
                if (list.size() > 0) {
                    rlNoMessage.setVisibility(View.GONE);
                    llGoodList.setVisibility(View.VISIBLE);

                } else {
                    rlNoMessage.setVisibility(View.VISIBLE);
                    llGoodList.setVisibility(View.GONE);
                }
                ptrGood.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                ptrGood.onFooterRefreshComplete();
                if (page > 1) {
                    if (tempList!=null&&tempList.size() != 0) {
                        commonAdapter.notifyDataSetChanged();
                        if(isVisibleToUser) ToastUtils.show(mActivity, "加载成功");
                    } else {
                        if(isVisibleToUser) ToastUtils.show(mActivity, "无更多内容");
                    }
                }

                commonAdapter.notifyDataSetChanged();

//                tempList.clear();
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                isrefresh=false;
                isload=false;
                ptrGood.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                ptrGood.onFooterRefreshComplete();
                int errorcode = response.getErrorcode();
                if (errorcode == 12) {
                    if(isVisibleToUser) ToastUtils.show(mActivity, "账号异常，请重新登录");
                    utils.jumpTo(mActivity, LoginActivity.class);
                } else if(errorcode == 56) {

                }
                else {
                    if(isVisibleToUser) ToastUtils.show(mActivity, "服务器繁忙");
                }
                if (list.size() > 0) {
                    rlNoMessage.setVisibility(View.GONE);
                    llGoodList.setVisibility(View.VISIBLE);

                } else {
                    rlNoMessage.setVisibility(View.VISIBLE);
                    llGoodList.setVisibility(View.GONE);
                }
                ptrGood.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                ptrGood.onFooterRefreshComplete();
            }


            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                isrefresh=false;
                isload=false;
                if(isVisibleToUser) ToastUtils.show(mActivity, "链接服务器失败，请检查您的网络！");
                ptrGood.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                ptrGood.onFooterRefreshComplete();
                if (list.size() > 0) {
                    rlNoMessage.setVisibility(View.GONE);
                    llGoodList.setVisibility(View.VISIBLE);

                } else {
                    rlNoMessage.setVisibility(View.VISIBLE);
                    llGoodList.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 上下架商品提示弹框
     */
    private void showEnsureUpDown(final int pos, final int type) {
        TipView tipView = new TipView(mActivity);
        String strTitle = type == 1 ? "是否下架商品？" : "是否上架商品？";
        tipView.setMessage("是否上架商品？").setOnClickListenerEnsure(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()) {
                    reqshangxiaGood(pos, type);
                }
                else {
                    if(isVisibleToUser) ToastUtils.show(mActivity, R.string.no_internet);
                }

            }
        });
        DialogUtil.showSelfDialog(mActivity, tipView);
    }

    private void showEnsureCancle(final int pos, String orderId) {
        TipView tipView = new TipView(mActivity);
        tipView.setMessage("是否要删除商品" + orderId + "?").setOnClickListenerEnsure(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()) {
                    reqDelGood(pos);
                }
                else {
                    if(isVisibleToUser) ToastUtils.show(mActivity, R.string.no_internet);
                }

            }
        });
        DialogUtil.showSelfDialog(mActivity, tipView);
    }

    /**
     *
     **/
    public void reqOrderSure(final int position) {
        ProductItem ordeDetails = list.get(position);
        String url = getResources().getString(R.string.api_baseurl) + "order/FinishBtn.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + type);
//        LG.e(TAG, ordeDetails.getOrderNum().toString() + " " + Constant.userid + "*" + ordeDetails.getOrderNum() + "*" + type);
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                if(isVisibleToUser) ToastUtils.show(mActivity, "操作成功");
                list.remove(position);
                commonAdapter.notifyDataSetChanged();
//                finish();


            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if (errorcode == 12 || errorcode == 13) {
                    if(isVisibleToUser) ToastUtils.show(mActivity, "账号异常，请重新登录");
                    utils.jumpTo(mActivity, LoginActivity.class);
                } else if (errorcode == 34 || errorcode == 35) {
                    if(isVisibleToUser) ToastUtils.show(mActivity, "该订单不存在");
                } else {
                    if(isVisibleToUser) ToastUtils.show(mActivity, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
            }
        });
    }

    /**
     * @funcation 删除商品接口
     **/
    public void reqDelGood(final int position) {
        ProductItem ordeDetails = list.get(position);
        String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_GOOD_MANAGER_DEL_GOOD;
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + ordeDetails.getGoodid());
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                LG.e("555  ", response.getData());
                if(isVisibleToUser) ToastUtils.show(mActivity, "删除成功");
                list.remove(position);
                if (list.size() > 0) {
                    rlNoMessage.setVisibility(View.GONE);
                    llGoodList.setVisibility(View.VISIBLE);

                } else {
                    rlNoMessage.setVisibility(View.VISIBLE);
                    llGoodList.setVisibility(View.GONE);
                }
                commonAdapter.notifyDataSetChanged();
//                finish();
//                Data = response.getObj(OrdeDetails_Entity.class);
//                binData(Data);
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                // 9 10 11 50(商品id非法) 56(店铺id是否为空) 300 301 302
                switch (errorcode) {
                    case 10:
                    case 11:
                    case 12:
                        if(isVisibleToUser) ToastUtils.show(mActivity, "账号异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 50:
                        if(isVisibleToUser) ToastUtils.show(mActivity, "商品异常");
                        break;
                    case 26:
                        if(isVisibleToUser) ToastUtils.show(mActivity, "商品不支持该操作");
                        break;
                    case 30:
                        if(isVisibleToUser) ToastUtils.show(mActivity, "商品审核不通过或商品不存在");
                        break;
                    case 61:
                    case 67:
                    default:
                        if(isVisibleToUser) ToastUtils.show(mActivity, "服务器繁忙");
                        break;
                }

            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
            }
        });
    }

    /**
     * @param type     0下架1上架
     * @param position
     * @funcation 上下架接口
     */
    public void reqshangxiaGood(final int position, final int type) {
        ProductItem ordeDetails = list.get(position);
        String url = getResources().getString(R.string.api_baseurl) + "goods/Sj.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + ordeDetails.getGoodid() + "*" + type);
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                LG.e("555  ", response.getData());
                String msg = type == 0 ? "下架成功" : "上架成功";
                if(isVisibleToUser) ToastUtils.show(mActivity, msg);
                list.remove(position);
                if (list.size() > 0) {
                    rlNoMessage.setVisibility(View.GONE);
                    llGoodList.setVisibility(View.VISIBLE);

                } else {
                    rlNoMessage.setVisibility(View.VISIBLE);
                    llGoodList.setVisibility(View.GONE);
                }
                commonAdapter.notifyDataSetChanged();

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                // 9   10     11  13     26   30   	300   301   302
                // 13  商品id非法  26 状态非法   30  商品审核不通过或商品不存在3
                switch (errorcode) {
                    case 10:
                    case 11:
                    case 12:
                        if(isVisibleToUser) ToastUtils.show(mActivity, "账号异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 13:
                        if(isVisibleToUser) ToastUtils.show(mActivity, "商品异常");
                        break;
                    case 26:
                        if(isVisibleToUser) ToastUtils.show(mActivity, "商品不支持该操作");
                        break;
                    case 30:
                        if(isVisibleToUser) ToastUtils.show(mActivity, "商品审核不通过或商品不存在");
                        break;
                    case 61:
                    case 67:
                    default:
                        if(isVisibleToUser) ToastUtils.show(mActivity, "服务器繁忙");
                        break;
                }
                if (list.size() > 0) {
                    rlNoMessage.setVisibility(View.GONE);
                    llGoodList.setVisibility(View.VISIBLE);

                } else {
                    rlNoMessage.setVisibility(View.VISIBLE);
                    llGoodList.setVisibility(View.GONE);
                }

            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                if(isVisibleToUser) ToastUtils.show(mActivity, "服务器繁忙");
                if (list.size() > 0) {
                    rlNoMessage.setVisibility(View.GONE);
                    llGoodList.setVisibility(View.VISIBLE);

                } else {
                    rlNoMessage.setVisibility(View.VISIBLE);
                    llGoodList.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
    }
}
