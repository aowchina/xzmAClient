package com.minfo.carrepairseller.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.query.ChoseCarActivity;
import com.minfo.carrepairseller.activity.query.EPCQueryActivity;
import com.minfo.carrepairseller.activity.query.ProductQueryActivity;
import com.minfo.carrepairseller.activity.query.VINQueryActivity;
import com.minfo.carrepairseller.entity.query.ChexingItem;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.LG;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.minfo.carrepairseller.utils.UniversalImageUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class QueryFragment extends BaseFragment implements View.OnClickListener {
    private final static int FLAG_SELECTOR_CAR_TYPE = 10003;
    private ImageView ivBack,ivLogo;
    private TextView tvProduct,tvEPC,tvCar,tvOEM,tvTitle;
    private ChexingItem item;

    public QueryFragment() {
    }


    @Override
    protected View initViews() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_query, null);
        ivBack = ((ImageView) view.findViewById(R.id.iv_back));
        ivLogo = ((ImageView) view.findViewById(R.id.iv_logo));

        tvProduct = ((TextView) view.findViewById(R.id.tv_product));
        tvEPC = ((TextView) view.findViewById(R.id.tv_epc));
        tvCar = ((TextView) view.findViewById(R.id.tv_car));
        tvOEM = ((TextView) view.findViewById(R.id.tv_oem));
        tvTitle = ((TextView) view.findViewById(R.id.tv_title));

        ivBack.setVisibility(View.GONE);
        tvTitle.setText("查查");
        tvProduct.setOnClickListener(this);
        tvOEM.setOnClickListener(this);
        tvEPC.setOnClickListener(this);
        tvCar.setOnClickListener(this);
        ivLogo.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_product:
                Intent intent = new Intent(mActivity, ProductQueryActivity.class);
                intent.putExtra(Constant.QUERY_TYPE, 0);
                startActivity(intent);
                break;
            case R.id.tv_oem:
                Intent intent1 = new Intent(mActivity, ProductQueryActivity.class);
                intent1.putExtra(Constant.QUERY_TYPE, 1);
                startActivity(intent1);
                break;
            case R.id.tv_epc:
                if(item ==null) {
                    ToastUtils.show(mActivity, "请先选择车型");
                    return;
                }
                Intent intent2 = new Intent(mActivity, EPCQueryActivity.class);
                intent2.putExtra(Constant.CAR_TYPE, item);
                startActivity(intent2);
//                startActivity(new Intent(mActivity, EPCQueryActivity.class));
                break;
            case R.id.tv_car:
                startActivity(new Intent(mActivity, VINQueryActivity.class));
                break;
            case R.id.iv_logo:
                startActivityForResult(new Intent(mActivity, ChoseCarActivity.class), FLAG_SELECTOR_CAR_TYPE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FLAG_SELECTOR_CAR_TYPE:
                if(resultCode == mActivity.RESULT_OK) {
                    LG.e("查查", "返回果");
                    if(data != null) {
                        item = (ChexingItem) data.getSerializableExtra(Constant.CAR_TYPE);
                        UniversalImageUtils.displayImageUseDefOptions(item.getIcon(), ivLogo);
                    }
                    else {
                        ToastUtils.show(mActivity, "选择车型异常");
                    }

                }
                break;
        }
    }
}
