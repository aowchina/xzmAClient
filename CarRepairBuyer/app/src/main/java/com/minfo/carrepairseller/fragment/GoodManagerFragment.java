package com.minfo.carrepairseller.fragment;

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
import com.minfo.carrepairseller.activity.login.LoginActivity;
import com.minfo.carrepairseller.activity.publish.PartsPublishActivity;
import com.minfo.carrepairseller.adapter.BaseViewHolder;
import com.minfo.carrepairseller.adapter.CommonAdapter;
import com.minfo.carrepairseller.dialog.TipView;
import com.minfo.carrepairseller.entity.ProductAll;
import com.minfo.carrepairseller.entity.ProductItem;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ConstantUrl;
import com.minfo.carrepairseller.utils.DialogUtil;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.minfo.carrepairseller.utils.UniversalImageUtils;
import com.minfo.carrepairseller.widget.PullToRefreshView;

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
 * type（0出售中，1已下架，2审核中，3审核不通过
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

    private boolean isrefresh;
    private boolean isload;
    private ProductAll productAll;

    @Override
    protected View initViews() {
        Bundle bundle = getArguments();
        type = bundle.getInt(Constant.GOOD_STATE, 0);

        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_good_manager, null);

        lvGood = (ListView) view.findViewById(R.id.lv_good);
        ptrGood = (PullToRefreshView) view.findViewById(R.id.ptr_good);

//        tvTitle = (TextView) view.findViewById(R.id.tv_title);
//        ivBack = (ImageView) view.findViewById(R.id.iv_back);
//
////        tvTitle.setVisibility(View.VISIBLE);
////        ivBack.setVisibility(View.VISIBLE);
//        tvTitle.setText("我的订单");

//        ivBack.setOnClickListener(this);
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
            showWait();
            reqData();
        } else {
            ToastUtils.show(mActivity, "网络异常");
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

                helper.setText(R.id.tv_name, item.getName());
                helper.setText(R.id.tv_time, item.getCar_name());
                helper.setText(R.id.tv_price, "$" + item.getPrice());
                helper.setText(R.id.tv_labe, item.getCar_name());

                UniversalImageUtils.displayImageUseDefOptions(item.getImg(), ivGood);

                switch (type) {
                    case 0: // 审核不通过
                        tvLeft.setText("编辑");
                        tvCenter.setText("重新审核");
                        tvRight.setText("删除");
                        break;
                    case 1: // 出售中
                        tvLeft.setText("编辑");
                        tvCenter.setText("下架");
                        tvRight.setText("删除");
                        break;

                    case 2: // 审核中
                        tvLeft.setText("编辑");
                        tvCenter.setText("审核中");
                        tvRight.setText("删除");
                        break;
                    case 3: // 已下架
                        tvLeft.setText("编辑");
                        tvCenter.setText("上架");
                        tvRight.setText("删除");
                        break;
                }

                tvLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.show(mActivity,"编辑");
                        Intent intent=new Intent(mActivity, PartsPublishActivity.class);
                        Log.e("idProduct",list.get(position).getGoodid());

                        intent.putExtra("id",list.get(position).getGoodid());
                        startActivity(intent);

                    }
                });
                tvCenter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (type) {
                            case 0: // 下架接口
                                reqshangxiaGood(position, 0);
                                break;
                            case 1: // 上架接口
                                reqshangxiaGood(position, 1);
                                break;
                            case 2: // 审核中不可点
                                break;
                            case 3: // 重新审核接口
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


        if (isOnline()) {
            page = 1;
            isrefresh = true;
            showWait();
            reqData();
        } else {
            ToastUtils.show(mActivity, "网络异常");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                mActivity.finish();
                break;
        }
    }

    /**
     * 请求订单列表数据
     * 上行参数: 8段 * userid * 订单状态(0待付款，1待发货，2收获，3已完成) * 页码
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
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + type + "*" + page);
        httpClient.post(url, params, new RequestListener() {

            @Override
            public void onPreRequest() {
                showWait();
            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                Log.e("GoodManger", response.toString());
                cleanWait();
                if (isrefresh) {
                    list.clear();
                    isrefresh = false;
                    ptrGood.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                    ToastUtils.show(mActivity, "刷新成功");
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
                ptrGood.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                ptrGood.onFooterRefreshComplete();
                if (page > 1) {
                    if (tempList.size() != 0) {
                        commonAdapter.notifyDataSetChanged();
                        ToastUtils.show(mActivity, "加载成功");
                    } else {
                        ToastUtils.show(mActivity, "无更多内容");
                    }
                } else {
                    commonAdapter.notifyDataSetChanged();
                }
                tempList.clear();
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                ptrGood.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                ptrGood.onFooterRefreshComplete();
                int errorcode = response.getErrorcode();
                if (errorcode == 12) {
                    ToastUtils.show(mActivity, "账号异常，请重新登录");
                    utils.jumpTo(mActivity, LoginActivity.class);
                } else {
                    ToastUtils.show(mActivity, "服务器繁忙");
                }
            }


            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ptrGood.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                ptrGood.onFooterRefreshComplete();
            }
        });
    }

    /**
     * 上下架商品提示
     */
    private void showEnsureGone(final int pos) {
        TipView tipView = new TipView(mActivity);
        tipView.setMessage("是否上架商品？").setOnClickListenerEnsure(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reqOrderSure(pos);

            }
        });
        DialogUtil.showSelfDialog(mActivity, tipView);
    }

    private void showEnsureCancle(final int pos, String orderId) {
        TipView tipView = new TipView(mActivity);
        tipView.setMessage("是否要删除商品" + orderId + "?").setOnClickListenerEnsure(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reqDelGood(pos);

            }
        });
        DialogUtil.showSelfDialog(mActivity, tipView);
    }

    /**
     * @funcation上下架商品
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
                ToastUtils.show(mActivity, "操作成功");
                list.remove(position);
                commonAdapter.notifyDataSetChanged();
//                finish();


            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if (errorcode == 12 || errorcode == 13) {
                    ToastUtils.show(mActivity, "账号异常，请重新登录");
                    utils.jumpTo(mActivity, LoginActivity.class);
                } else if (errorcode == 34 || errorcode == 35) {
                    ToastUtils.show(mActivity, "该订单不存在");
                } else {
                    ToastUtils.show(mActivity, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
            }
        });
    }

    /**
     * @funcation 取消订单接口
     **/
    public void reqDelGood(final int position) {
        ProductItem ordeDetails = list.get(position);
        String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_GOOD_MANAGER_DEL_GOOD;
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid );
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                LG.e("555  ", response.getData());
                ToastUtils.show(mActivity, "取消订单成功");
                list.remove(position);
                commonAdapter.notifyDataSetChanged();
//                finish();
//                Data = response.getObj(OrdeDetails_Entity.class);
//                binData(Data);
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                // 9,35(订单非法),34(订单不存在),61(订单状态不能取消),12
                switch (errorcode) {
                    case 12:
                        ToastUtils.show(mActivity, "账号异常，请重新登录");
//                        Utils.intent2Class(mActivity, Login.class);
                        break;
                    case 34:
                    case 35: // 订单ID非法
                        ToastUtils.show(mActivity, "订单已失效");
                        break;
                    case 61:
                    case 67: // 已支付的订单不能取消
                        ToastUtils.show(mActivity, "已支付商品，不能取消订单");
                        break;
                    default:
                        ToastUtils.show(mActivity, "服务器繁忙");
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
     *@funcation 上下架接口
     *@param type 0下架1上架
     *@param position
     */
    public void reqshangxiaGood(final int position, final int type) {
        ProductItem ordeDetails = list.get(position);
        String url = getResources().getString(R.string.api_baseurl) + "goods/Sj.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid +"*" + ordeDetails.getGoodid() +"*"+type);
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                LG.e("555  ", response.getData());
                String msg = type == 0 ? "下架成功":"上架成功";
                ToastUtils.show(mActivity, msg);
                list.remove(position);
                commonAdapter.notifyDataSetChanged();
//                finish();
//                Data = response.getObj(OrdeDetails_Entity.class);
//                binData(Data);
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                // 9,35(订单非法),34(订单不存在),61(订单状态不能取消),12
                switch (errorcode) {
                    case 12:
                        ToastUtils.show(mActivity, "账号异常，请重新登录");
//                        Utils.intent2Class(mActivity, Login.class);
                        break;
                    case 34:
                    case 35: // 订单ID非法
                        ToastUtils.show(mActivity, "订单已失效");
                        break;
                    case 61:
                    case 67: // 已支付的订单不能取消
                        ToastUtils.show(mActivity, "已支付商品，不能取消订单");
                        break;
                    default:
                        ToastUtils.show(mActivity, "服务器繁忙");
                        break;
                }

            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
            }
        });
    }
}
