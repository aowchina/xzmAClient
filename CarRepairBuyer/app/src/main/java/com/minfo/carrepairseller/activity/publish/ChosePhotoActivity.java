package com.minfo.carrepairseller.activity.publish;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.adapter.query.EditGoodPhotoUploadAdapter;
import com.minfo.carrepairseller.entity.query.PictureItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import minfo.com.albumlibrary.TreeConstant;
import minfo.com.albumlibrary.activity.PhotosActivity;
import minfo.com.albumlibrary.utils.LG;
import minfo.com.albumlibrary.widget.MyGridView;

/**
 * 配件图片选择界面
 */
public class ChosePhotoActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final int ALBUM_CHOOSE_PHOTOS_CODE2 = 1010;
    private TextView tvTitle, tvSure;//tvTitle 标题   tvSure  确定
    private ImageView ivBack;
    private MyGridView mgvPhoto;
    private EditGoodPhotoUploadAdapter mAdapter;
    private ArrayList<String> imgs = new ArrayList<String>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_chose_photo);
    }


    @Override
    protected void findViews() {
        mgvPhoto = ((MyGridView) findViewById(R.id.mgv_photo));
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        tvSure = ((TextView) findViewById(R.id.tv_right));

    }

    @Override
    protected void initViews() {
        tvSure.setText("确认");
        tvTitle.setText("选择图片");
        tvSure.setVisibility(View.VISIBLE);

        ivBack.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        imgs = getIntent().getStringArrayListExtra("img");

        mAdapter = new EditGoodPhotoUploadAdapter(this);
        mgvPhoto.setAdapter(mAdapter);
        mgvPhoto.setOnItemClickListener(this);
        EditGoodPhotoUploadAdapter.imgs.clear();
        List<PictureItem> list = new ArrayList<>();
        for (String path : imgs) {
            PictureItem issue = new PictureItem();
            LG.i("item2 url=" + path);
            issue.setImgUrl(path);
            issue.setAdd(false);
            issue.setNet(true);
            list.add(issue);
        }
        mAdapter.addAll(list, mAdapter.getCount() - 1);
        if (mAdapter.getCount() > 8) {
            mAdapter.remove(mAdapter.getCount() - 1);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAdapter.getItem(position).isAdd()) {
            Intent intent = new Intent(ChosePhotoActivity.this,
                    PhotosActivity.class);
            intent.putExtra(TreeConstant.ALBUM_SELECTED_NUM,
                    mAdapter.getCount());
            startActivityForResult(intent,
                    ALBUM_CHOOSE_PHOTOS_CODE2);
        } else {
            minfo.com.albumlibrary.utils.ToastUtils.showShort(ChosePhotoActivity.this, "跳转界面");

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            switch (requestCode) {


                case ALBUM_CHOOSE_PHOTOS_CODE2:
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_right:
                getPhotoPath();
                break;
        }
    }

    /**
     * 获取选择图片路径
     */
    private void getPhotoPath() {
        Intent intent = new Intent();
        List<PictureItem> selectPhoto = new ArrayList<>();
        for (int i = 0; i < mAdapter.getmObjects().size(); i++) {
            if (!mAdapter.getmObjects().get(i).isAdd()) {
                if (!mAdapter.getmObjects().get(i).isNet()) {
                    selectPhoto.add(mAdapter.getmObjects().get(i));

                }
            }
        }
        intent.putExtra("photo", (Serializable) selectPhoto);


        setResult(RESULT_OK, intent);
        finish();
    }
}
