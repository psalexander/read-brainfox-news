package com.asinenko.brainfoxnews.activity;

import com.asinenko.brainfoxnews.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends Activity implements android.view.View.OnClickListener{

	private Button callButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		callButton = (Button)findViewById(R.id.callButton);
		callButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.callButton){
			Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+79853837742"));
			startActivity(dialIntent);
		}
	}
}
