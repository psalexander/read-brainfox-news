package com.asinenko.brainfoxnews;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Configuration;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewsActivity extends Activity {

	private TextView titleTextView;
	private TextView shortTextView;
	private TextView mainTextView;
	private TextView dateTextView;
	private LinearLayout imageLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);

		titleTextView = (TextView)findViewById(R.id.titleTextView);
		shortTextView = (TextView)findViewById(R.id.shortTextView);
		mainTextView = (TextView)findViewById(R.id.mainTextView);
		dateTextView = (TextView)findViewById(R.id.dateTextView);

		imageLayout = (LinearLayout)findViewById(R.id.imageLayout);
	//	imageLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,200));

		titleTextView.setText(getIntent().getExtras().getString("title"));
		shortTextView.setText(getIntent().getExtras().getString("short"));
		//mainTextView.setText(getIntent().getExtras().getString("id"));
		mainTextView.setText("sdgdfgdfgdfgsdfgdsfgрапрапрапррsdfg\nsdgdfgdfgdfgsdfgdsfgsdfg\nsdgdfgdfgdfgsdfпарапрапрgdsfgsdfg\nsdgdfgdfgdfgsdfгшнгшнгшнгшgdsfgsdfg\nsdgdfgdfgdfgsdfgdsfgsdfg\n");
		dateTextView.setText(getIntent().getExtras().getString("date"));
		
//		ImageView im = new ImageView(this);
//		im.setImageResource(R.drawable.marussia);
//		imageLayout.addView(im);
//		
//		ImageView im2 = new ImageView(this);
//		im2.setImageResource(R.drawable.marcell_sebestyen);
//		imageLayout.addView(im2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news, menu);
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
