package com.asinenko.brainfoxnews.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.asinenko.brainfoxnews.R;
import com.asinenko.brainfoxnews.Urls;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ImageActivity extends Activity {

	private ImageView imageView;
	private String imageUrl;
	private ProgressBar progressBar;
	private Bitmap image = null;
	private boolean isImageDownloaded = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_image);
		imageView = (ImageView)findViewById(R.id.oneImageView);
		imageView.setAdjustViewBounds(true);
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		imageUrl = getIntent().getExtras().getString("url");
		new RequestTask().execute(Urls.URL_GET_IMAGE + imageUrl);
	}

	class RequestTask extends AsyncTask<String, String, String>{
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
			if(image != null){
				imageView.setImageBitmap(image);
				isImageDownloaded = true;
			}
			progressBar.setVisibility(ProgressBar.INVISIBLE);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.image, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_save_image:
			if(isImageDownloaded && image != null){
				String storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
				String dir = storage + "/brainfox/";
				File dirfile = new File(dir);
				if(!dirfile.exists()){
					dirfile.mkdir();
				}
				String path = dir + "/" + String.valueOf(System.currentTimeMillis()) + ".jpg";
				OutputStream fOut = null;
				File file = new File(path);
				try {
					fOut = new FileOutputStream(file);
					image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
					Toast.makeText(this, "Изображение сохранено: " + path, Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(this, "Ошибка при сохранении", Toast.LENGTH_LONG).show();
				}finally{
					try {
						if(fOut != null){
							fOut.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else{
				Toast.makeText(this, "Изображение не загружено. Сохранение возможно только после загрузки.", Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
