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

import com.asinenko.brainfoxnews.HorizontalListView;
import com.asinenko.brainfoxnews.ImageActivity;
import com.asinenko.brainfoxnews.LazyAdapter;
import com.asinenko.brainfoxnews.R;
import com.asinenko.brainfoxnews.items.JsonParser;
import com.asinenko.brainfoxnews.items.NewsItem;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class NewsActivity extends Activity {

//	private static final String TAG_TITLE = "name";
//	private static final String TAG_SHORTTEXT = "shorttext";
//	private static final String TAG_DATA = "text";
//	private static final String TAG_DATE = "date";
//	private static final String TAG_ERROR_CODE = "error_code";
//	private static final String TAG_IMAGES = "images";
	private static final String IMAGE_URL = "http://baklikov.ru/ttt.php?action=image&id=";//16&h=150";

	private HorizontalListView listView;
	private TextView titleTextView;
	private TextView shortTextView;
	private TextView mainTextView;
	private TextView dateTextView;
	private ProgressBar progressBar;

	private Activity activity;

	private String id;
	private String title;
	private String firsttext;
	private String mainText;
	private String date;

	private NewsItem newsItem;

//	private String error;
//	private JSONArray data = null;
//	private ImagesArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		activity = this;

		listView = (HorizontalListView) findViewById(R.id.imageListView);

		titleTextView = (TextView)findViewById(R.id.titleTextView);
		shortTextView = (TextView)findViewById(R.id.shortTextView);
		mainTextView = (TextView)findViewById(R.id.mainTextView);
		dateTextView = (TextView)findViewById(R.id.dateTextView);
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		progressBar.setVisibility(ProgressBar.INVISIBLE);

		id = getIntent().getExtras().getString("newsid");
		title = getIntent().getExtras().getString("title");
		firsttext = getIntent().getExtras().getString("short");
		date = getIntent().getExtras().getString("date");

		new RequestTask().execute("http://baklikov.ru/ttt.php?action=details&id=" + id);

//		listView.setAdapter(adapter);
//		listView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
////				final NewsListItem item = (NewsListItem) parent.getItemAtPosition(position);
////				Intent intent = new Intent(MainActivity.this, NewsActivity.class);
////				intent.putExtra("newsid", item.getId());
////				intent.putExtra("title", item.getName());
////				intent.putExtra("short", item.getText());
////				intent.putExtra("date", item.getDate());
////				startActivity(intent);
//				//Toast.makeText (getApplicationContext(), item.getName() + "\n" + item.getDate() + "\n" + item.getText(), Toast.LENGTH_LONG).show ();
//			}
//		});

		//listView.setAdapter(adapter);
//		ImageView im2 = new ImageView(this);
//		im2.setImageResource(R.drawable.marcell_sebestyen);
//		imageLayout.addView(im2);
	}

//	private List<String> imageList = new LinkedList<String>();
//	private void parseJSON(String jsonStr){
//
//		if (jsonStr != null) {
//			try {
//				JSONObject jsonObj = new JSONObject(jsonStr);
//				error = jsonObj.getString(TAG_ERROR_CODE);
//				title = jsonObj.getString(TAG_TITLE);
//				firsttext = jsonObj.getString(TAG_SHORTTEXT);
//				mainText = jsonObj.getString(TAG_DATA);
//				date = jsonObj.getString(TAG_DATE);
//				data = jsonObj.getJSONArray(TAG_IMAGES);
//				imageList.clear();
//
//				for (int i = 0; i < data.length(); i++) {
//					//JSONObject c = data.getJSONObject(i);
//
//					imageList.add(String.valueOf(data.getInt(i)));
////					String id = c.getString(TAG_ID);
////					String name = c.getString(TAG_NAME);
////					String shorttext = c.getString(TAG_SHORTTEXT);
////					String date = c.getString(TAG_DATE);
////					NewsListItem it = new NewsListItem(id, name, date,shorttext);
////					list.add(it);
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		} else {
//			Log.e("ServiceHandler", "Couldn't get any data from the url");
//		}
//	}

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
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				//TODO Handle problems..
			} catch (IOException e) {
				//TODO Handle problems..
			}
			//parseJSON(responseString);
			newsItem = JsonParser.parseNewsItem(id, responseString);

			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
//			titleTextView.setText(title);
//			shortTextView.setText(firsttext);
//			mainTextView.setText(mainText);
//			dateTextView.setText(date);

			titleTextView.setText(newsItem.getName());
			shortTextView.setText(newsItem.getShorttext());
			mainTextView.setText(newsItem.getText());
			dateTextView.setText(newsItem.getDate());

//			List<String> list = new ArrayList<String>();
//			for(int i = 0; i < imageList.size(); i++){
//				String id = imageList.get(i);
//				String url = IMAGE_URL + id + "&h=300";
//				list.add(url);
//			}

			List<String> list = new ArrayList<String>();
			for(int i = 0; i < newsItem.getImages().size(); i++){
				String id = newsItem.getImages().get(i);
				String url = IMAGE_URL + id + "&h=300";
				list.add(url);
			}
			String[] array = list.toArray(new String[list.size()]);
			final LazyAdapter ladapter=new LazyAdapter(activity, array);
			listView.setAdapter(ladapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					Toast.makeText (getApplicationContext(), String.valueOf(position), Toast.LENGTH_LONG).show ();
					Intent intent = new Intent(NewsActivity.this, ImageActivity.class);
					intent.putExtra("url", newsItem.getImages().get(position));
					startActivity(intent);
				}
			});
			progressBar.setVisibility(ProgressBar.INVISIBLE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.news, menu);
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private class ImagesArrayAdapter extends ArrayAdapter<ImageView> {
		private LayoutInflater inflater=null;
		private Context context;
		private List<ImageView> data;

		public ImagesArrayAdapter(Context _context, int textViewResourceId, List<ImageView> objects) {
			super(_context, textViewResourceId, objects);
			context = _context;
			data = objects;
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public void add(ImageView object) {
			data.add(object);
		}

		@Override
		public ImageView getItem(int position) {
			return data.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View vi=convertView;
			if(convertView==null){
				vi = inflater.inflate(R.layout.image_item, null);
			}

			RelativeLayout lay = (RelativeLayout)vi.findViewById(R.id.imageRelativeLayout);
			lay.addView(data.get(position));
			return vi;
		}
	}
}
