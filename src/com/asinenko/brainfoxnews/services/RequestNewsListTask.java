package com.asinenko.brainfoxnews.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.asinenko.brainfoxnews.db.NewsDataSource;
import com.asinenko.brainfoxnews.items.JsonParser;
import com.asinenko.brainfoxnews.items.NewsListItem;
import com.asinenko.brainfoxnews.items.NewsSQLItem;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

public class RequestNewsListTask  extends AsyncTask<String, String, String>{

	private ProgressBar progressBar;
	private String responseString = null;
	private List<NewsListItem> list = null;
	private NewsDataSource dataSources;
	private Context context;

	public RequestNewsListTask(Context con, ProgressBar progressBar) {
		super();
		this.progressBar = progressBar;
		this.context = con;
		dataSources = new NewsDataSource(context);
		dataSources.open();
	}

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
		return responseString;
	}

	@Override
	protected void onPostExecute(String result) {
		progressBar.setVisibility(ProgressBar.INVISIBLE);
		//Toast.makeText(getApplicationContext(), String.valueOf(list.size()), Toast.LENGTH_SHORT).show();
		for (NewsListItem it : list) {
			NewsSQLItem n = new NewsSQLItem();
			n.setDbId(it.getId());
			n.setDate(it.getDate());
			n.setTitle(it.getName());
			n.setShorttext(it.getText());
			dataSources.createNewsItem(n);
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		progressBar.setVisibility(ProgressBar.VISIBLE);
		super.onPreExecute();
	}

}
