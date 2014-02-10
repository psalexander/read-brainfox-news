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
import com.asinenko.brainfoxnews.LazyAdapter;
import com.asinenko.brainfoxnews.R;
import com.asinenko.brainfoxnews.Urls;
import com.asinenko.brainfoxnews.items.JsonParser;
import com.asinenko.brainfoxnews.items.NewsItem;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class NewsActivity extends Activity {

	private HorizontalListView listView;
	private TextView titleTextView;
	private TextView shortTextView;
	private TextView mainTextView;
	private TextView dateTextView;
	private ProgressBar progressBar;
	private ImageView imageView;

	private Activity activity;

	private String id;
	private String title;
	private String firsttext;
	private String mainText;
	private String date;

	private Bitmap image;
	private NewsItem newsItem;

	private int displayWidth;
	private int displayHeigth;
	private int imageWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		activity = this;

		listView = (HorizontalListView) findViewById(R.id.imageListView);
		imageView = (ImageView) findViewById(R.id.oneImageNewsView);

		titleTextView = (TextView)findViewById(R.id.titleTextView);
		shortTextView = (TextView)findViewById(R.id.shortTextView);
		mainTextView = (TextView)findViewById(R.id.mainTextView);
		dateTextView = (TextView)findViewById(R.id.dateTextView);
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		progressBar.setVisibility(ProgressBar.INVISIBLE);


		Display display =  ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		displayWidth = size.x;
		displayHeigth = size.y;
		imageWidth = displayWidth;
		if(displayHeigth < displayWidth)
			imageWidth = displayHeigth;

		id = getIntent().getExtras().getString("newsid");
		title = getIntent().getExtras().getString("title");
		firsttext = getIntent().getExtras().getString("short");
		date = getIntent().getExtras().getString("date");

		new RequestTask().execute(Urls.URL_GET_NEWS_ITEM + id);
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
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				//TODO Handle problems..
			} catch (IOException e) {
				//TODO Handle problems..
			}
			newsItem = JsonParser.parseNewsItem(id, responseString);
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			titleTextView.setText(newsItem.getName());
			shortTextView.setText(newsItem.getShorttext());
			mainTextView.setText(newsItem.getText());
			dateTextView.setText(newsItem.getDate());

			if(newsItem.getImages().size() == 0){
				listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0));
			}else if(newsItem.getImages().size() == 1){
				listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0));

				imageView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(NewsActivity.this, ImageActivity.class);
						intent.putExtra("url", newsItem.getImages().get(0));
						startActivity(intent);
					}
				});
				new RequestOneImageTask().execute(Urls.URL_GET_IMAGE + newsItem.getImages().get(0) + "&w=" + String.valueOf(imageWidth));
			}else{
				listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,300));
				List<String> list = new ArrayList<String>();
				for(int i = 0; i < newsItem.getImages().size(); i++){
					String id = newsItem.getImages().get(i);
					String url = Urls.URL_GET_IMAGE + id + "&h=300";
					list.add(url);
				}
				String[] array = list.toArray(new String[list.size()]);
				final LazyAdapter ladapter=new LazyAdapter(activity, array);
				listView.setAdapter(ladapter);
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
						Intent intent = new Intent(NewsActivity.this, ImageActivity.class);
						intent.putExtra("url", newsItem.getImages().get(position));
						startActivity(intent);
					}
				});
			}
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

	class RequestOneImageTask extends AsyncTask<String, String, String>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setVisibility(ProgressBar.VISIBLE);
		}

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			try {
				response = httpclient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = response.getStatusLine();
				if(statusLine.getStatusCode() == HttpStatus.SC_OK){
					image = BitmapFactory.decodeStream(response.getEntity().getContent());
				} else{
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				//TODO Handle problems
			} catch (IOException e) {
				//TODO Handle problems
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			imageView.setVisibility(ImageView.VISIBLE);
			if(image != null){
				imageView.setImageBitmap(image);
				imageView.invalidate();
			}
			progressBar.setVisibility(ProgressBar.INVISIBLE);
		}
	}
}
