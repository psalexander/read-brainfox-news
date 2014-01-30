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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NewsActivity extends Activity {

	private static final String TAG_TITLE = "name";
	private static final String TAG_SHORTTEXT = "shorttext";
	private static final String TAG_DATA = "text";
	private static final String TAG_DATE = "date";
	private static final String TAG_ERROR_CODE = "error_code";
	private static final String TAG_IMAGES = "images";

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

	String error;
	JSONArray data = null;

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
//		imageLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,200));

		id = getIntent().getExtras().getString("newsid");
		title = getIntent().getExtras().getString("title");
		firsttext = getIntent().getExtras().getString("short");
		date = getIntent().getExtras().getString("date");

		new RequestTask().execute("http://baklikov.ru/ttt.php?action=details&id=" + id);

//		ImageView im2 = new ImageView(this);
//		im2.setImageResource(R.drawable.marcell_sebestyen);
//		imageLayout.addView(im2);
	}

	private void parseJSON(String jsonStr){
//	{
//		"name":"\u041d\u043e\u0432\u043e\u0441\u0442\u044c",
//		"shotext":"\u043a\u0440430\u0442\u043a"
//		"text":"\u043d\u043e\u0432\u043e\u0441\u0442\u044c",
//		"date":"19.01.2038 05:14:07",
//		"error_code":0,
//		"images":[2,3,14]
//	}

		if (jsonStr != null) {
			try {
				JSONObject jsonObj = new JSONObject(jsonStr);
				error = jsonObj.getString(TAG_ERROR_CODE);
				title = jsonObj.getString(TAG_TITLE);
				firsttext = jsonObj.getString(TAG_SHORTTEXT);
				mainText = jsonObj.getString(TAG_DATA);
				date = jsonObj.getString(TAG_DATE);
				data = jsonObj.getJSONArray(TAG_IMAGES);

//				for (int i = 0; i < data.length(); i++) {
//					JSONObject c = data.getJSONObject(i);
//					String id = c.getString(TAG_ID);
//					String name = c.getString(TAG_NAME);
//					String shorttext = c.getString(TAG_SHORTTEXT);
//					String date = c.getString(TAG_DATE);
//					NewsListItem it = new NewsListItem(id, name, date,shorttext);
//					list.add(it);
//				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}
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
				//TODO Handle problems..
			} catch (IOException e) {
				//TODO Handle problems..
			}
			parseJSON(responseString);
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			titleTextView.setText(title);
			shortTextView.setText(firsttext);
			mainTextView.setText(mainText);
			dateTextView.setText(date);
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
}
