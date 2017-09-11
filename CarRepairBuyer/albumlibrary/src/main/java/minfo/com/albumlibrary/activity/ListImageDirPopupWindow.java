package minfo.com.albumlibrary.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import minfo.com.albumlibrary.R;
import minfo.com.albumlibrary.adapter.BasePopupWindowForListView;
import minfo.com.albumlibrary.adapter.CommonAdapter;
import minfo.com.albumlibrary.adapter.ViewHolder;
import minfo.com.albumlibrary.bean.ImageFloder;


public class ListImageDirPopupWindow extends BasePopupWindowForListView<ImageFloder>
{
	private ListView mListDir;
	private int choosePosition = -1;
	private CommonAdapter<ImageFloder> adapter;

	public ListImageDirPopupWindow(int width, int height,
								   List<ImageFloder> datas, View convertView)
	{
		super(convertView, width, height, true, datas);
	}

	@Override
	public void initViews()
	{
		mListDir = (ListView) findViewById(R.id.id_list_dir);
		adapter = new CommonAdapter<ImageFloder>(context, mDatas,
				R.layout.activity_album_choose_photo_pop_item)
		{
			@Override
			public void convert(ViewHolder helper, ImageFloder item)
			{
				helper.setText(R.id.id_dir_item_name, item.getName());
				helper.setImageByUrl(R.id.id_dir_item_image,
						item.getFirstImagePath());
				helper.setText(R.id.id_dir_item_count, item.getCount() + "张");
				ImageView imageView = helper.getView(R.id.id_dir_item_choose);
				if(mDatas.indexOf(item) == choosePosition) {
					imageView.setVisibility(View.VISIBLE);
				}
				else {
					imageView.setVisibility(View.INVISIBLE);
				}
			}
		};
		mListDir.setAdapter(adapter);
	}

	public interface OnImageDirSelected
	{
		void selected(ImageFloder floder);
	}

	private OnImageDirSelected mImageDirSelected;

	public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected)
	{
		this.mImageDirSelected = mImageDirSelected;
	}

	@Override
	public void initEvents()
	{
		mListDir.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id)
			{
				choosePosition = position;
				adapter.notifyDataSetChanged();
				if (mImageDirSelected != null)
				{
					mImageDirSelected.selected(mDatas.get(position));
				}
			}
		});
	}

	@Override
	public void init()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void beforeInitWeNeedSomeParams(Object... params)
	{
		// TODO Auto-generated method stub
	}

}
