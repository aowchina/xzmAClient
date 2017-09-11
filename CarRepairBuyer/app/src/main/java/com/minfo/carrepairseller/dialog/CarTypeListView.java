package com.minfo.carrepairseller.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.adapter.BaseViewHolder;
import com.minfo.carrepairseller.adapter.CommonAdapter;
import com.minfo.carrepairseller.entity.shop.CarItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MinFo021 on 17/6/20.
 * 选择车型弹框
 *
 */
public class CarTypeListView extends BaseDialogView {
    private ImageView ivClose;
    private ListView listView;
    private CommonAdapter<CarItem> commonAdapter;
    private List<CarItem> list = new ArrayList<>();
    private Button btEnsure;

    public CarTypeListView(Context context) {
        super(context);
        initView();
    }

    public CarTypeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        removeAllViews();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_cartype_list, null);
        ivClose = (ImageView) view.findViewById(R.id.iv_close);
        btEnsure = (Button) view.findViewById(R.id.bt_ensure);
        listView = (ListView) view.findViewById(R.id.lv_car_type);

        commonAdapter = new CommonAdapter<CarItem>(context, list, R.layout.item_dialog_cartype) {
            @Override
            public void convert(BaseViewHolder helper, CarItem item, int position) {
                helper.setText(R.id.tv_car_type, item.getCname());
            }
        };

        listView.setAdapter(commonAdapter);
        ivClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mExitDialog != null) {
                    mExitDialog.exitDialog();
                }
            }
        });

        btEnsure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mExitDialog != null) {
                    mExitDialog.exitDialog();
                }
            }
        });

        addView(view);
    }

    public void setData(List<CarItem> list) {
        this.list.clear();
        if(list != null && list.size() > 0) {

            this.list.addAll(list);
        }
        commonAdapter.notifyDataSetChanged();
    }
}
