package com.minfo.carrepairseller.adapter.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.entity.purchase.PurchaseList;
import com.minfo.carrepairseller.utils.UniversalImageUtils;
import com.minfo.carrepairseller.utils.Utils;

import java.util.List;

/**
 * Created by min-fo-012 on 17/5/19.
 */
public class PurchaseAdapter extends BaseAdapter {
    private Context context;
    private List<PurchaseList> list;
    private LayoutInflater inflater;
    private Utils utils;
    public PurchaseAdapter(Context context, List<PurchaseList> list) {
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
        PurchaseList item = list.get(position);
        if (convertView == null) {
            hodler = new ViewHodler();
            convertView = inflater.inflate(R.layout.item_purchase, null);
            hodler.ivIcon = (ImageView) convertView.findViewById(R.id.iv_product);
            hodler.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            hodler.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            hodler.tvQuery = (TextView) convertView.findViewById(R.id.tv_query);
            hodler.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
            hodler.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
            hodler.tvXiCar = (TextView) convertView.findViewById(R.id.tv_xi);


            convertView.setTag(hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag();
        }

        UniversalImageUtils.displayImageUseDefOptions(item.getPicture(), hodler.ivIcon);
        hodler.tvName.setText(item.getJname());
        hodler.tvNumber.setText(item.getBname());

        hodler.tvXiCar.setText(item.getSname());
        hodler.tvAddress.setText(item.getCname());
        hodler.tvQuery.setText(item.getCount()+"人报价");

        hodler.tvPrice.setText(item.getType());
        return convertView;
    }

    class ViewHodler {
        private ImageView ivIcon;
        private TextView tvName,tvPrice,tvQuery,tvNumber,tvAddress,tvXiCar;
    }
}
