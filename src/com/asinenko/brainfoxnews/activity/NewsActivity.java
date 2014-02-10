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
