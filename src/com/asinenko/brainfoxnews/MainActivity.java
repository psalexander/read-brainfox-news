package com.asinenko.brainfoxnews;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// JSON Node names
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_SHORTTEXT = "shorttext";
	private static final String TAG_TIMESTAMP = "ts";
	private static final String TAG_UTIME = "utime";
	private static final String TAG_DATA = "data";
	private static final String TAG_DATE = "date";
	private static final String TAG_ERROR_CODE = "error_code";

	JSONArray data = null;
	ArrayList<HashMap<String, String>> dataList;
	List<NewsListItem> listItems;
	private ListView listview;
	private ArrayList<NewsListItem> list;
	private Activity activity;
	private ProgressBar progressBar;

	private void parseJSON(String jsonStr){
		/*
		 * {
		 * 	"error_code":0,
		 * "ts":23142340,
		 * 	"data":
		 * 		[
		 * 			{
		 * 				"id":"4",
		 * 				"name":null,
		 * 				"shorttext":null,
		 * 				"date":"2014-01-22 08:54:14"
		 * 			},
		 * 			{
		 * 				"id":"3",
		 * 				"name":null,
		 * 				"shorttext":null,
		 * 				"date":"2014-01-22 08:54:14"
		 * 			}
		 * 		]
		 * }
		 */
		list.clear();
		if (jsonStr != null) {
			try {
				JSONObject jsonObj = new JSONObject(jsonStr);

				// Getting JSON Array node
				String error = jsonObj.getString(TAG_ERROR_CODE);
				data = jsonObj.getJSONArray(TAG_DATA);
				String timestamp = jsonObj.getString(TAG_TIMESTAMP);
				// looping through All Contacts
				for (int i = 0; i < data.length(); i++) {
					JSONObject c = data.getJSONObject(i);

					String id = c.getString(TAG_ID);
					String name = c.getString(TAG_NAME);
					String shorttext = c.getString(TAG_SHORTTEXT);
					String date = c.getString(TAG_DATE);
				//	String utime = c.getString(TAG_UTIME);

					NewsListItem it = new NewsListItem(id, name, date,shorttext);
					list.add(it);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listview = (ListView) findViewById(R.id.listview);
		progressBar = (ProgressBar) findViewById(R.id.progressBarDownload);
		progressBar.setVisibility(ProgressBar.INVISIBLE);

		activity = this;
		list = new ArrayList<NewsListItem>();
//		list.add(new NewsListItem("7", "Name1", "Date1", "text1 text text text text text text texttext text text text text text texttext text text text text text texttext text text text text text text text text"));
//		list.add(new NewsListItem("8", "Name2", "Date2", "text2 text text text text text text text text text text text text text text text text text text text text text text text text"));
//		list.add(new NewsListItem("9", "Name3", "Date3", "text3 text text text text text text text text text text text text text text text text text text"));
//		list.add(new NewsListItem("10", "Name4", "Date4", "text4 text text text text text text text text text text text text text text text text text text"));
//		list.add(new NewsListItem("11", "Name5", "Date5", "text5 text text text text text text text text text text text text text text text text text text text text text text text text text text text text text text text text text"));
//		list.add(new NewsListItem("12", "Name6", "Date6", "text6 text text text text text text text text text text text text text text text text text text text text text text text text text text text"));

		final NewsArrayAdapter adapter = new NewsArrayAdapter(this, R.layout.listview_item, list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				final NewsListItem item = (NewsListItem) parent.getItemAtPosition(position);
				Toast.makeText (getApplicationContext(), item.getName() + "\n" + item.getDate() + "\n" + item.getText(), Toast.LENGTH_LONG).show ();
			}
		});

		String mPhoneNumber = getPhoneNumber();
		if(mPhoneNumber != null){
			Toast.makeText (getApplicationContext(), mPhoneNumber, Toast.LENGTH_LONG).show ();
		}

		dataList = new ArrayList<HashMap<String, String>>();
		listItems = new LinkedList<NewsListItem>();
		new RequestTask().execute("http://baklikov.ru/ttt.php?action=list");
	}

	private String getHTTPPage(String url) throws ClientProtocolException, IOException{
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = httpclient.execute(new HttpGet(url));
		StatusLine statusLine = response.getStatusLine();
		String responseString = null;
		if(statusLine.getStatusCode() == HttpStatus.SC_OK){
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			response.getEntity().writeTo(out);
			out.close();
			responseString = out.toString();
			//more logic
		} else{
			//Closes the connection.
			response.getEntity().getContent().close();
			throw new IOException(statusLine.getReasonPhrase());
		}
		return responseString;
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
			new RequestTask().execute("http://baklikov.ru/ttt.php?action=list");
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	//new RequestTask().execute("http://stackoverflow.com");
	class RequestTask extends AsyncTask<String, String, String>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
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
			//Do anything with response..
			// парсим и обновляем список.

			final NewsArrayAdapter adapter = new NewsArrayAdapter(activity, R.layout.listview_item, list);
			listview.setAdapter(adapter);
			listview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
					final NewsListItem item = (NewsListItem) parent.getItemAtPosition(position);
					Toast.makeText (getApplicationContext(), item.getName() + "\n" + item.getDate() + "\n" + item.getText(), Toast.LENGTH_LONG).show ();
				}
			});
			progressBar.setVisibility(ProgressBar.INVISIBLE);
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
