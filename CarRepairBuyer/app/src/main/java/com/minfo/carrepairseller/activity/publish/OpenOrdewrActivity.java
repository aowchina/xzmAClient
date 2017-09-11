package com.minfo.carrepairseller.activity.publish;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.activity.address.AddressListActivity;
import com.minfo.carrepairseller.adapter.BaseViewHolder;
import com.minfo.carrepairseller.adapter.CommonAdapter;
import com.minfo.carrepairseller.adapter.query.EditGoodPhotoUploadAdapter;
import com.minfo.carrepairseller.entity.query.PictureItem;
import com.minfo.carrepairseller.entity.shop.Product;

import java.util.ArrayList;
import java.util.List;

import minfo.com.albumlibrary.TreeConstant;
import minfo.com.albumlibrary.activity.PhotosActivity;
import minfo.com.albumlibrary.utils.LG;
import minfo.com.albumlibrary.widget.MyGridView;

public class OpenOrdewrActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final int ALBUM_CHOOSE_PHOTOS_CODE = 1000;

    private TextView tvTitle, tvName, tvPhone, tvAddress, tvPrice, tv_order_detail, tv_open_order, tv_sure;
    private ImageView ivBack, ivCar;
    private LinearLayout llCar;
    private RelativeLayout rlCount, rlOem, rlKind;
    private EditText etPrice, et_order_detail;
    private MyGridView mgvPhoto;
    private EditGoodPhotoUploadAdapter mAdapter;
    private LinearLayout llAddressContiner;
    private boolean userIsSeller = false;//买家false 卖价 ture
    private GridView gvImg;
    private CommonAdapter commonAdapter;//产品
    private List<Product> productList = new ArrayList<>();
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_open_ordewr);
    }

    @Override
    protected void findViews() {
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        ivBack = ((ImageView) findViewById(R.id.iv_back));

        tvName = ((TextView) findViewById(R.id.tv_name));
        tvPhone = ((TextView) findViewById(R.id.tv_phone));
        tvAddress = ((TextView) findViewById(R.id.tv_address));
        tv_open_order = ((TextView) findViewById(R.id.tv_open_order));
        tvPrice = ((TextView) findViewById(R.id.tv_price));
        tv_order_detail = ((TextView) findViewById(R.id.tv_order_detail));
        tv_sure = ((TextView) findViewById(R.id.tv_sure));

        etPrice = ((EditText) findViewById(R.id.et_price));
        et_order_detail = ((EditText) findViewById(R.id.et_order_detail));
        mgvPhoto = ((MyGridView) findViewById(R.id.mgv_photo));
        view = ((View) findViewById(R.id.view));
        llAddressContiner = ((LinearLayout) findViewById(R.id.ll_continer_address));
        gvImg = ((GridView) findViewById(R.id.gv_img));


    }

    @Override
    protected void initViews() {
        for (int i = 0; i < 5; i++) {
            productList.add(new Product());

        }
        ivBack.setOnClickListener(this);
        llAddressContiner.setOnClickListener(this);
        mAdapter = new EditGoodPhotoUploadAdapter(this);
        mgvPhoto.setAdapter(mAdapter);
        mgvPhoto.setOnItemClickListener(this);
        //判断卖家还是买家
        if (userIsSeller) {//卖jia ture
            tvTitle.setText("开单");
            llAddressContiner.setVisibility(View.GONE);
            tv_order_detail.setVisibility(View.GONE);
            tvPrice.setVisibility(View.GONE);
            gvImg.setVisibility(View.GONE);
            tv_sure.setText("立即发送");
        } else {//买家false
            tvTitle.setText("付款");
            view.setVisibility(View.GONE);
            tv_open_order.setVisibility(View.GONE);
            etPrice.setVisibility(View.GONE);
            et_order_detail.setVisibility(View.GONE);
            mgvPhoto.setVisibility(View.GONE);
            tv_sure.setText("立即支付");
        }
        commonAdapter = new CommonAdapter(this, productList, R.layout.item_order_img) {
            @Override
            public void convert(BaseViewHolder helper, Object item, int position) {
                TextView textView = helper.getView(R.id.tv_name);
//                ImageView imageView = helper.getView(R.id.iv_car);
//                int size = (utils.getScreenWidth() - utils.dp2px(130)) / 4;
//                imageView.setLayoutParams(new LinearLayout.LayoutParams(size, size));
//                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            }
        };
        gvImg.setAdapter(commonAdapter);
        gvImg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(mActivity, ProductDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("product", productList.get(position));
//                intent.putExtra("info", bundle);
//                startActivity(intent);
                //startActivity(new Intent(OpenOrdewrActivity.this, ProductDetailActivity.class));
            }
        });
        setListViewHeightBasedOnChildren(gvImg);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_sure:
                //startActivity(new Intent(OpenOrdewrActivity.this,AddressListActivity.class));

                break;
            case R.id.ll_continer_address:
                startActivity(new Intent(OpenOrdewrActivity.this, AddressListActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAdapter.getItem(position).isAdd()) {
            Intent intent = new Intent(OpenOrdewrActivity.this,
                    PhotosActivity.class);
            intent.putExtra(TreeConstant.ALBUM_SELECTED_NUM,
                    mAdapter.getCount());
            startActivityForResult(intent,
                    ALBUM_CHOOSE_PHOTOS_CODE);
        } else {
            minfo.com.albumlibrary.utils.ToastUtils.showShort(OpenOrdewrActivity.this, "跳转界面");

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            switch (requestCode) {

//                    case CameraUtils.REQUEST_CODE_CROP_PICTURE2:
//                        // uploadLogo(CameraUtils.mCurrentFile);
//                        List<PictureItem> list2 = new ArrayList<PictureItem>();
//                        PictureItem issue2 = new PictureItem();
//                        issue2.setImgUrl(CameraUtils.mCurrentPhotoPath);
//                        issue2.setAdd(false);
//                        issue2.setNet(false);
//                        list2.add(issue2);
//                        mAdapter2.addAll(list2, mAdapter2.getCount() - 1);
//                        if (mAdapter2.getCount() > 8) {
//                            mAdapter2.remove(mAdapter2.getCount() - 1);
//                        }
//                        break;
                case ALBUM_CHOOSE_PHOTOS_CODE:
                    if (data.getStringArrayListExtra(TreeConstant.ALBUM_CHOOSE_PHOTOS_PATH) != null) {
                        List<String> photos = data
                                .getStringArrayListExtra(TreeConstant.ALBUM_CHOOSE_PHOTOS_PATH);
                        List<PictureItem> list22 = new ArrayList<>();
                        for (String path : photos) {
                            PictureItem issue22 = new PictureItem();
                            LG.i("item2 url=" + path);
                            issue22.setImgUrl(path);
                            issue22.setAdd(false);
                            issue22.setNet(false);
                            list22.add(issue22);
                        }
                        mAdapter.addAll(list22, mAdapter.getCount() - 1);
                        if (mAdapter.getCount() > 8) {
                            mAdapter.remove(mAdapter.getCount() - 1);
                        }

                    }
                    break;
            }

        }

    }

    /**
     * 加载数据后，计算gridview高度
     *
     * @param gridView
     */
    private void setListViewHeightBasedOnChildren(GridView gridView) {

        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int height = listAdapter.getCount() % 3 == 0 ? listAdapter.getCount() / 3 : listAdapter.getCount() / 3 + 1;
        for (int i = 0; i < height; i++) {
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight
                + (gridView.getVerticalSpacing() * (height - 1));
        gridView.setLayoutParams(params);
    }
}
