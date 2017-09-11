package com.minfo.carrepairseller.adapter.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.entity.address.Province;

import java.util.List;

/**
 * Created by fei on 16/2/22.
 */
public class ProvinceAdapter extends BaseAdapter {

    private Context context;
    private List<Province> myList;
    private LayoutInflater inflater;

    public ProvinceAdapter(Context context, List<Province> myList) {
        this.context = context;
        this.myList = myList;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return myList.size();
    }

    public Object getItem(int position) {
        return myList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Province myListItem = myList.get(position);
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_city_spinner_bw,null);
            holder.name = (TextView) convertView.findViewById(R.id.cityname);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.name.setText(myListItem.getName());
        return convertView;
    }
    class ViewHolder{
        TextView name;
    }
}
