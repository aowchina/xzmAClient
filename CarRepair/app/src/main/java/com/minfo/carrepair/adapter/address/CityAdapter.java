package com.minfo.carrepair.adapter.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.entity.address.City;

import java.util.List;

/**
 * Created by fei on 16/2/22.
 */
public class CityAdapter extends BaseAdapter {

    private Context context;
    private List<City> myList;
    private LayoutInflater inflater;

    public CityAdapter(Context context, List<City> myList) {
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
        City myListItem = myList.get(position);
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
