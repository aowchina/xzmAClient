package com.minfo.carrepair.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.adapter.WLAdapter;
import com.minfo.carrepair.entity.order.WLEntity;
import com.minfo.carrepair.utils.ToastUtils;

import java.util.List;

/**
 * Created by MinFo021 on 17/6/19.
 */

public class SendGoodView extends BaseDialogView {
    private TextView mTextEnsure, mTextCancel;
    private EditText etWlCode; // 快递单号

    //	private LinearLayout mLayoutEnsure;
    private OnClickListener mClickListenerCancle; // 取消按钮事件

    private OnClickEnsureListener mClickListenerEnsure; // 确定按钮相应事件 发货
    private String strCode; // 物流单号
    private String wlId = "0";// 物流id

    private WLAdapter padapter;// 物流适配器

    private Spinner province;// 物流名称 spinner
    private List<WLEntity> list;


    public SendGoodView(Context context,List<WLEntity> list) {
        super(context);
        this.list = list;

        initView();
    }

//    public void setData(List<WLEntity> list) {
//        padapter.notifyDataSetChanged();
//
//    }

    public SendGoodView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        removeAllViews();
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(context)
                .inflate(R.layout.dialog_send_good, this, false);
        etWlCode = (EditText) layout.findViewById(R.id.et_code);
        mTextEnsure = (TextView) layout.findViewById(R.id.tv_ensure);
        mTextCancel = (TextView) layout.findViewById(R.id.tv_cancle);

        province = ((Spinner) layout.findViewById(R.id.wl));
        //城市选择部分
        province.setPrompt("物流");
        mTextEnsure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				ToastUtils.showShort(context, "功能待开放");
                strCode = etWlCode.getText().toString();


                if (strCode == null || strCode.equals("")) {
                    ToastUtils.show(context, "请输入快递单号");
                    return;
                }

                if (mClickListenerEnsure != null) {
                    mClickListenerEnsure.ensure(wlId, strCode);
                }

                if (mExitDialog != null) {
                    mExitDialog.exitDialog();
                }
            }
        });

        mTextCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mClickListenerCancle != null) {
                    mClickListenerCancle.onClick(v);
                }
                if (mExitDialog != null) {
                    mExitDialog.exitDialog();
                }
            }
        });
        padapter = new WLAdapter(context, list);
        province.setAdapter(padapter);

        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wlId = list.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.addView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    /**
     * 取消按钮点击事件
     */
    public SendGoodView setOnClickListenerCancle(OnClickListener clickListener) {
        mClickListenerCancle = clickListener;
        return this;
    }

    /**
     * 确定按钮点击事件
     */
    public SendGoodView setOnClickEnsureListener(OnClickEnsureListener clickListener) {
        mClickListenerEnsure = clickListener;

        return this;

    }


    /**
     * 确定按钮自定义相应接口
     * wlName 输入物流名称
     * wlCode 快递单号
     */
    public interface OnClickEnsureListener {
        public void ensure(String wlName, String wlCode);
    }

}
