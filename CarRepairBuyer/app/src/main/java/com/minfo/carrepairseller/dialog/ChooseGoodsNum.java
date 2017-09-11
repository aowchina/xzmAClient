package com.minfo.carrepairseller.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.utils.MyCheck;
import com.minfo.carrepairseller.utils.ToastUtils;

/**
 * Created by MinFo021 on 17/6/20.
 * 修改商品数量
 *
 */
public class ChooseGoodsNum extends BaseDialogView {
    private TextView tvPrice; // 显示价格
    private TextView tvNum; // 显示数量
    private ImageView ivClose;  // 关闭弹框
    private ImageView ivAdd; // 商品数量加
    private ImageView ivDel; // 商品数量减
    private Button btEnsure; // 确认按钮
    private int num=1;
    private EnsureOnClick ensureOnClick;

    public ChooseGoodsNum(Context context) {
        super(context);
        initView();
    }

    public ChooseGoodsNum(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        removeAllViews();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_choose_good_num, null);
        tvPrice = (TextView) view.findViewById(R.id.tv_price);
        tvNum = (TextView) view.findViewById(R.id.tv_num);
        ivClose = (ImageView) view.findViewById(R.id.iv_close);
        ivAdd = (ImageView) view.findViewById(R.id.iv_jia);
        ivDel = (ImageView) view.findViewById(R.id.iv_jian);
        btEnsure = (Button) view.findViewById(R.id.bt_ensure);

        ivAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                num ++;
                tvNum.setText(""+num);
            }
        });
        ivDel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num > 1) {
                    num --;
                    tvNum.setText(""+num);
                }
                else {
                    ToastUtils.show(context, "商品数量不能小于1");
                }
            }
        });

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

                if(num < 1) {
                    ToastUtils.show(context, "商品数量不能小于1");
                    return;
                }
                if(ensureOnClick !=null) {
                    ensureOnClick.ensure(num);
                }
                if(mExitDialog != null) {
                    mExitDialog.exitDialog();
                }
            }
        });
        addView(view);
    }

    /**
     * 设置确定按钮接口
     * @param onClick
     */
    public void setEnsureOnclik(EnsureOnClick onClick) {
        this.ensureOnClick = onClick;
    }

    /**
     * 设置价格
     * @param price
     */
    public void setPrice(String price) {
        if(price != null && !price.equals("")) {
            tvPrice.setText("￥"+ MyCheck.priceFormatChange(price));
        }
    }
    /**
     * 设置数量
     * @param num
     */
    public void setNum(int num) {
        if(num > 0) {
            tvNum.setText(""+num);
            this.num = num;
        }
    }
    /**
     * 确定按钮接口
     */
    public interface EnsureOnClick {
        void ensure(int num);
    }
}
