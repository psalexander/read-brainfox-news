package com.asinenko.brainfoxnews;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.res.Configuration;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NewsActivity extends Activity {

	private static final String TAG_ID = "id";
	private static final String TAG_TITLE = "title";
	private static final String TAG_SHORTTEXT = "shorttext";
	private static final String TAG_TIMESTAMP = "ts";
	private static final String TAG_UTIME = "utime";
	private static final String TAG_DATA = "data";
	private static final String TAG_DATE = "date";
	private static final String TAG_ERROR_CODE = "error_code";

	private TextView titleTextView;
	private TextView shortTextView;
	private TextView mainTextView;
	private TextView dateTextView;
	private LinearLayout imageLayout;
	private ProgressBar progressBar;

	private String id;
	private String title;
	private String firsttext;
	private String mainText;
	private String date;
	private int imageCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);

		titleTextView = (TextView)findViewById(R.id.titleTextView);
		shortTextView = (TextView)findViewById(R.id.shortTextView);
		mainTextView = (TextView)findViewById(R.id.mainTextView);
		dateTextView = (TextView)findViewById(R.id.dateTextView);
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		progressBar.setVisibility(ProgressBar.INVISIBLE);
		imageLayout = (LinearLayout)findViewById(R.id.imageLayout);

	//	imageLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,200));

		id = getIntent().getExtras().getString("id");
		title = getIntent().getExtras().getString("title");
		firsttext = getIntent().getExtras().getString("short");
		date = getIntent().getExtras().getString("date");

		titleTextView.setText(title);
		shortTextView.setText(firsttext);
		mainTextView.setText("sdgdfgdfgdfgsdfgdsfgрапрапрапррsdfg\nsdgdfgdfgdfgsdfgdsfgsdfg\nsdgdfgdfgdfgsdfпарапрапрgdsfgsdfg\nsdgdfgdfgdfgsdfгшнгшнгшнгшgdsfgsdfg\nsdgdfgdfgdfgsdfgdsfgsdfg\n");
		dateTextView.setText(date);

//		ImageView im = new ImageView(this);
//		im.setImageResource(R.drawable.marussia);
//		imageLayout.addView(im);
//		
//		ImageView im2 = new ImageView(this);
//		im2.setImageResource(R.drawable.marcell_sebestyen);
//		imageLayout.addView(im2);
	}

	class RequestTask extends AsyncTask<String, String, String>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			progressBar.setVisibility(ProgressBar.VISIBLE);
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
				//TODO Handle problems..
			} catch (IOException e) {
				//TODO Handle problems..
			}
//			parseJSON(responseString);
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
//			progressBar.setVisibility(ProgressBar.INVISIBLE);
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
}
