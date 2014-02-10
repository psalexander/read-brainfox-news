package com.asinenko.brainfoxnews.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.asinenko.brainfoxnews.R;
import com.asinenko.brainfoxnews.db.NewsDataSQLHelper;
import com.asinenko.brainfoxnews.db.NewsDataSource;
import com.asinenko.brainfoxnews.items.JsonParser;
import com.asinenko.brainfoxnews.items.NewsListItem;
import com.asinenko.brainfoxnews.items.NewsSQLItem;
import com.asinenko.brainfoxnews.services.RepeatingAlarmGetNewsService;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ListView listview;
	private List<NewsListItem> list;
	private Activity activity;
	private ProgressBar progressBar;
	private ClientCursorAdapter cursorAdapter;
	private NewsDataSource dataSources;
	private Cursor cursor;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listview = (ListView) findViewById(R.id.listview);
		progressBar = (ProgressBar) findViewById(R.id.progressBarDownload);
		progressBar.setVisibility(ProgressBar.INVISIBLE);

		activity = this;
		list = new ArrayList<NewsListItem>();

		dataSources = new NewsDataSource(activity);
		dataSources.open();
		cursor = dataSources.getNewsCursor();
		cursorAdapter = new ClientCursorAdapter(activity, R.layout.listview_item, cursor, true);
		listview.setAdapter(cursorAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				Cursor cursor = (Cursor) listview.getItemAtPosition(position);
				String newsid = cursor.getString(cursor.getColumnIndexOrThrow(NewsDataSQLHelper.NEWS_COLUMN_DB_ID));
				String title = cursor.getString(cursor.getColumnIndexOrThrow(NewsDataSQLHelper.NEWS_COLUMN_TITLE));
				String shorttext = cursor.getString(cursor.getColumnIndexOrThrow(NewsDataSQLHelper.NEWS_COLUMN_SHORTTEXT));
				String date = cursor.getString(cursor.getColumnIndexOrThrow(NewsDataSQLHelper.NEWS_COLUMN_DATE));

				Intent intent = new Intent(MainActivity.this, NewsActivity.class);
				intent.putExtra("newsid", newsid);
				intent.putExtra("title", title);
				intent.putExtra("short", shorttext);
				intent.putExtra("date", date);
				startActivity(intent);
			}
		});

		String r = "http://baklikov.ru/ttt.php?action=list&ts=" + dataSources.getLastRequestTime();
		new RequestTask().execute(r);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private String getPhoneNumber(){
		TelephonyManager tMgr =(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		return tMgr.getLine1Number();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh:
			String r = "http://baklikov.ru/ttt.php?action=list&ts=" + dataSources.getLastRequestTime();
			new RequestTask().execute(r);
			break;
		case R.id.action_start_sevice:
			intent = new Intent(this, RepeatingAlarmGetNewsService.class);
			startService(intent);
			break;
		case R.id.action_stop_sevice:
			stopService(intent);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	class RequestTask extends AsyncTask<String, String, String>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setVisibility(ProgressBar.VISIBLE);
		}

		String responseString = null;
		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			try {
				response = httpclient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = response.getStatusLine();
				if(statusLine.getStatusCode() == HttpStatus.SC_OK){
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else{
					//Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				//TODO Handle problems
			} catch (IOException e) {
				//TODO Handle problems
			}

			list = JsonParser.parseJSONtoNewsItem(responseString);
			if(NewsListItem.errorcode.equals("0")){
				dataSources.updateLastRequestTime(NewsListItem.timestamp);
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//Do anything with response
			// парсим и обновляем список
			if(!NewsListItem.errorcode.equals("0")){
				Toast.makeText(getApplicationContext(), "Error code is " + NewsListItem.errorcode, Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), String.valueOf(list.size()), Toast.LENGTH_SHORT).show();
			}
			for (NewsListItem it : list) {
				NewsSQLItem n = new NewsSQLItem();
				n.setDbId(it.getId());
				n.setDate(it.getDate());
				n.setTitle(it.getName());
				n.setShorttext(it.getText());
				dataSources.createNewsItem(n);
			}
			cursor.requery();
			cursorAdapter.notifyDataSetChanged();
			progressBar.setVisibility(ProgressBar.INVISIBLE);
		}
	}

	public class ClientCursorAdapter extends ResourceCursorAdapter {
		private LayoutInflater inflater=null;
		private Cursor mCursor;
		private Context mContext;

		public ClientCursorAdapter(Context context, int layout, Cursor c, boolean flags) {
			super(context, layout, c, flags);
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mCursor = c;
			mContext = context;
		}
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return inflater.inflate(R.layout.row_item, parent, false);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView title = (TextView)view.findViewById(R.id.title);
			TextView date = (TextView)view.findViewById(R.id.date);
			TextView text = (TextView)view.findViewById(R.id.text);
			TextView id = (TextView)view.findViewById(R.id.id);
			if(cursor != null){
				title.setText(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_TITLE)));
				date.setText(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_DATE)));
				text.setText(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_SHORTTEXT)));
				id.setText(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_DB_ID)));
			}
			notifyDataSetChanged();
		}
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
		public NewsListItem getItem(int position) {
			return data.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View vi=convertView;
			if(convertView==null){
				vi = inflater.inflate(R.layout.row_item, null);
			}

			TextView title = (TextView)vi.findViewById(R.id.title);
			TextView date = (TextView)vi.findViewById(R.id.date);
			TextView text = (TextView)vi.findViewById(R.id.text);

			NewsListItem it = data.get(position);
			title.setText(it.getName());
			date.setText(it.getDate());
			text.setText(it.getText());
			return vi;
		}
	}
}
