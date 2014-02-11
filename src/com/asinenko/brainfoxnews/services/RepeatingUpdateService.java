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

import com.asinenko.brainfoxnews.R;
import com.asinenko.brainfoxnews.Urls;
import com.asinenko.brainfoxnews.activity.MainActivity;
import com.asinenko.brainfoxnews.db.NewsDataSource;
import com.asinenko.brainfoxnews.items.JsonParser;
import com.asinenko.brainfoxnews.items.NewsListItem;
import com.asinenko.brainfoxnews.items.NewsSQLItem;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

public class RepeatingUpdateService extends BroadcastReceiver{

	private NewsDataSource dataSources;
	private List<NewsListItem> list;
	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		dataSources = new NewsDataSource(context);
		dataSources.open();
		String r = Urls.URL_GET_NEWS_LIST + dataSources.getLastRequestTime();
		new RequestTask().execute(r);
		Toast.makeText(context, "It's Service Time!", Toast.LENGTH_LONG).show();
	}

	class RequestTask extends AsyncTask<String, String, String>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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
			if(!NewsListItem.errorcode.equals("0")){
				Toast.makeText(context, "Error code is " + NewsListItem.errorcode, Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(context, String.valueOf(list.size()), Toast.LENGTH_SHORT).show();
			}
			String news = "";
			for (NewsListItem it : list) {
				NewsSQLItem n = new NewsSQLItem();
				n.setDbId(it.getId());
				n.setDate(it.getDate());
				n.setTitle(it.getName());
				n.setShorttext(it.getText());
				n.setType(it.getType());
				dataSources.createNewsItem(n);
				news = news + " " + it.getName();
			}
			if(list.size() > 0){
				Intent intent2 = new Intent(context, MainActivity.class);
				PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent2, 0);
				Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

				Notification n  = new Notification.Builder(context)
					.setContentTitle("Добовление новости 1-А класса")
					.setContentText(news.trim())
					.setContentInfo(String.valueOf(list.size()))
					.setSmallIcon(R.drawable.ic_action_email)
					.setSound(alarmSound)
					.setContentIntent(pIntent)
					.setAutoCancel(true).build();
				NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.notify(0, n);
			}
			dataSources.close();
		}
	}
}
