package com.minfo.carrepair.widget;

import android.content.Context;

import com.minfo.carrepair.entity.address.City;
import com.minfo.carrepair.entity.address.District;
import com.minfo.carrepair.entity.address.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fei on 16/2/22.
 */
public class CityData {
    public static List<Province> arrProvinces = new ArrayList<>();

    /**
     * 读取省市区数据
     */
    public static List<Province> readProvinces(Context context) {
        arrProvinces.clear();
        StringBuffer sb = new StringBuffer();
        try {
            InputStream inputStream = context.getAssets().open("hd_area.txt");
            byte[] buf = new byte[1024];

            while ((inputStream.read(buf)) != -1) {
                sb.append(new String(buf));
                buf = new byte[1024];//重新生成，避免和上次读取的数据重复
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                Province province = new Province();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                JSONArray cityArray = jsonObject.getJSONArray("city");
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                List<City> cityList = new ArrayList<>();
                for (int j = 0; j < cityArray.length(); j++) {
                    City city = new City();
                    JSONObject cityObject = cityArray.getJSONObject(j);
                    JSONArray districtArray = cityObject.getJSONArray("area");
                    String cid = cityObject.getString("id");
                    String cname = cityObject.getString("name");
                    List<District> districtList = new ArrayList<>();
                    for (int k = 0; k < districtArray.length(); k++) {
                        District district = new District();
                        JSONObject districtObject = districtArray.getJSONObject(k);
                        String did = districtObject.getString("id");
                        String dname = districtObject.getString("name");
                        district.setId(did);
                        district.setName(dname);
                        districtList.add(district);
                    }
                    city.setId(cid);
                    city.setName(cname);
                    city.setArea(districtList);
                    cityList.add(city);
                }
                province.setId(id);
                province.setName(name);
                province.setCity(cityList);
                arrProvinces.add(province);

            }
            return arrProvinces;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrProvinces;
    }
}
