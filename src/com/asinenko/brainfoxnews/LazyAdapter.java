package com.asinenko.brainfoxnews;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class LazyAdapter extends BaseAdapter{
	private Activity activity;
	private String[] data;
	private static LayoutInflater inflater=null;
	public ImageLoader imageLoader;

	public LazyAdapter(Activity a, String[] d) {
		activity = a;
		data=d;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(activity.getApplicationContext());
	}

	public int getCount() {
		return data.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		if(convertView==null){
			vi = inflater.inflate(R.layout.image_item, null);
		}
		ImageView image=(ImageView)vi.findViewById(R.id.imageRowView);
		image.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageLoader.DisplayImage(data[position], image);
		return vi;
	}
}
