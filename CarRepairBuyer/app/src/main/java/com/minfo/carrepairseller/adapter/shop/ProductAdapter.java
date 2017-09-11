package com.minfo.carrepairseller.adapter.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.entity.ProductItem;
import com.minfo.carrepairseller.utils.MyCheck;
import com.minfo.carrepairseller.utils.UniversalImageUtils;
import com.minfo.carrepairseller.utils.Utils;

import java.util.List;

/**
 * Created by min-fo-012 on 16/7/3.
 */
public class ProductAdapter extends BaseAdapter {
    private Context context;
    private List<ProductItem> list;
    private LayoutInflater inflater;
    private Utils utils;
    public ProductAdapter(Context context, List<ProductItem> list) {
        this.context = context;
        this.list = list;
        utils = new Utils(context);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHodler hodler;
        ProductItem item = list.get(position);
        if (convertView == null) {
            hodler = new ViewHodler();
            convertView = inflater.inflate(R.layout.item_product, null);
            hodler.ivIcon = (ImageView) convertView.findViewById(R.id.iv_product);
            hodler.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            hodler.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
//            hodler.tvQuery = (TextView) convertView.findViewById(R.id.tv_query);
            hodler.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
//            hodler.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);

            convertView.setTag(hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag();
        }

//        int size = (utils.getScreenWidth() - utils.dp2px(24))/2;
//        hodler.ivIcon.setLayoutParams(new LinearLayout.LayoutParams(size, size));
//        hodler.ivIcon.setScaleType(ImageView.ScaleType.FIT_XY);
//
        UniversalImageUtils.displayImageUseDefOptions(item.getImg(), hodler.ivIcon);
        hodler.tvName.setText(item.getName());
        hodler.tvPrice.setText("￥ " + MyCheck.priceFormatChange(item.getPrice()));
        hodler.tvNumber.setText(item.getAmount()+"人付款");
        return convertView;
    }

    class ViewHodler {
        private ImageView ivIcon;
        private TextView tvName,tvPrice,tvQuery,tvNumber,tvAddress;
    }
}
