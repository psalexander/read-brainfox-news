package com.asinenko.brainfoxnews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listview = (ListView) findViewById(R.id.listview);

		final ArrayList<NewsListItem> list = new ArrayList<NewsListItem>();
		list.add(new NewsListItem("Name1", "Date1", "text1 text text text text text text text text text"));
		list.add(new NewsListItem("Name2", "Date2", "text2 text text text text text text text text text"));
		list.add(new NewsListItem("Name3", "Date3", "text3 text text text text text text text text text"));
		list.add(new NewsListItem("Name4", "Date4", "text4 text text text text text text text text text"));
		list.add(new NewsListItem("Name5", "Date5", "text5 text text text text text text text text text"));
		list.add(new NewsListItem("Name6", "Date6", "text6 text text text text text text text text text"));

		final NewsArrayAdapter adapter = new NewsArrayAdapter(this, R.layout.listview_item, list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
//				final String item = (String) parent.getItemAtPosition(position);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class NewsArrayAdapter extends ArrayAdapter<NewsListItem> {

		HashMap<NewsListItem, Integer> mIdMap = new HashMap<NewsListItem, Integer>();

		private LayoutInflater inflater=null;
		private Context context;
		private List<NewsListItem> data;

		public NewsArrayAdapter(Context _context, int textViewResourceId, List<NewsListItem> objects) {
			super(_context, textViewResourceId, objects);
			context = _context;
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
			data = objects;
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public long getItemId(int position) {
			NewsListItem item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View vi=convertView;
			if(convertView==null)
				vi = inflater.inflate(R.layout.listview_item, null);

			TextView title = (TextView)vi.findViewById(R.id.nameLine);
			TextView date = (TextView)vi.findViewById(R.id.dateLine);
			TextView text = (TextView)vi.findViewById(R.id.textLine);

			NewsListItem it = data.get(position);
			title.setText(it.getName());
			date.setText(it.getDate());
			text.setText(it.getText());
			return vi;
		}
	}
}
