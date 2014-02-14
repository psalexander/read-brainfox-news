package com.asinenko.brainfoxnews.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.asinenko.brainfoxnews.CheckConnection;
import com.asinenko.brainfoxnews.R;
import com.asinenko.brainfoxnews.Urls;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String TYPE_WARNING = "warning";
	private static final String TYPE_WARNING2 = "warning2";
	private static final String TYPE_INFO = "info";
	private static final String TYPE_QUESTION = "question";
	private static final String TYPE_CHAT = "chat";
	private static final String TYPE_ERROR = "error";
	private static final String TYPE_PHOTOS = "photos";

	private ListView listview;
	private List<NewsListItem> list;
	private Activity activity;
	private ProgressBar progressBar;
	private ClientCursorAdapter cursorAdapter;
	private NewsDataSource dataSources;
	private Cursor cursor;

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
				Intent intent = new Intent(MainActivity.this, NewsActivity.class);
				intent.putExtra("newsid", newsid);
				startActivity(intent);
			}
		});

		if(CheckConnection.isOnline(this)){
			String r = Urls.URL_GET_NEWS_LIST + dataSources.getLastRequestTime();
			new RequestTask().execute(r);
		}else{
			Toast.makeText(this, "Отсутствует соединение с сетью интернет.", Toast.LENGTH_LONG).show();
		}
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh:
			if(CheckConnection.isOnline(this)){
				String r = Urls.URL_GET_NEWS_LIST + dataSources.getLastRequestTime();
				new RequestTask().execute(r);
			}else{
				Toast.makeText(this, "Отсутствует соединение с сетью интернет.", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.action_start_sevice:
			startService(new Intent(this, RepeatingAlarmGetNewsService.class));
			break;
		case R.id.action_stop_sevice:
			stopService(new Intent(MainActivity.this, RepeatingAlarmGetNewsService.class));
			break;
		case R.id.action_exit:
			stopService(new Intent(MainActivity.this, RepeatingAlarmGetNewsService.class));
			super.onDestroy();
			System.exit(0);
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

		@Override
		protected String doInBackground(String... uri) {
			String responseString = null;
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
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				Toast.makeText(activity, "При загрузке данных произошла ошибка", Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				Toast.makeText(activity, "При загрузке данных произошла ошибка.", Toast.LENGTH_LONG).show();
			}
			list = JsonParser.parseJSONtoNewsItem(responseString);
			if(NewsListItem.errorcode != null && NewsListItem.errorcode.equals("0")){
				dataSources.updateLastRequestTime(NewsListItem.timestamp);
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(NewsListItem.errorcode != null && !NewsListItem.errorcode.equals("0")){
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
				n.setType(it.getType());
				dataSources.createNewsItem(n);
			}
			cursor = dataSources.getNewsCursor();
			cursorAdapter.changeCursor(cursor);
			progressBar.setVisibility(ProgressBar.INVISIBLE);
		}
	}

	public class ClientCursorAdapter extends ResourceCursorAdapter {

		private LayoutInflater inflater = null;

		public ClientCursorAdapter(Context context, int layout, Cursor c, boolean flags) {
			super(context, layout, c, flags);
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			ImageView image = (ImageView)view.findViewById(R.id.list_image);
			if(cursor != null){
				title.setText(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_TITLE)));
				date.setText(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_DATE)));
				text.setText(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_SHORTTEXT)));
				id.setText(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_DB_ID)));

				if(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_TYPE)) == null){
					image.setImageResource(R.drawable.info);
				}else if(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_TYPE)).toLowerCase().equals(TYPE_WARNING)){
					image.setImageResource(R.drawable.danger);
				}else if(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_TYPE)).toLowerCase().equals(TYPE_QUESTION)){
					image.setImageResource(R.drawable.question);
				}else if(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_TYPE)).toLowerCase().equals(TYPE_CHAT)){
					image.setImageResource(R.drawable.chat);
				}else if(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_TYPE)).toLowerCase().equals(TYPE_WARNING2)){
					image.setImageResource(R.drawable.danger2);
				}else if(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_TYPE)).toLowerCase().equals(TYPE_ERROR)){
					image.setImageResource(R.drawable.error);
				}else if(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_TYPE)).toLowerCase().equals(TYPE_INFO)){
					image.setImageResource(R.drawable.info);
				}else if(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_TYPE)).toLowerCase().equals(TYPE_PHOTOS)){
					image.setImageResource(R.drawable.photos);
				}else{
					image.setImageResource(R.drawable.info);
				}
			}
			notifyDataSetChanged();
		}
	}
}
