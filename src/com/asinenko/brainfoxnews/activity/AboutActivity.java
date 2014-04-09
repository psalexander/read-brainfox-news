package com.asinenko.brainfoxnews.activity;

import com.asinenko.brainfoxnews.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends Activity implements android.view.View.OnClickListener{

	private Button callButton;
	private Button sendMailButton;
	private TextView teacherMail;
	private ImageView imageView;
	private int clickCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		callButton = (Button)findViewById(R.id.callButton);
		callButton.setOnClickListener(this);
		sendMailButton = (Button)findViewById(R.id.sendMailButton);
		sendMailButton.setOnClickListener(this);
		teacherMail = (TextView)findViewById(R.id.teacherMailTextView);
		teacherMail.setOnClickListener(this);
		imageView = (ImageView)findViewById(R.id.brainImageView);
		imageView.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.callButton:
			Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+79853837742"));
			startActivity(dialIntent);
			break;
		case R.id.sendMailButton:
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
			shareIntent.putExtra(Intent.EXTRA_TEXT, "");
			shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { teacherMail.getText().toString() });
			startActivity(Intent.createChooser(shareIntent, "Написать письмо"));
			break;
		case R.id.brainImageView:
			clickCount++;
			if(clickCount > 10 && clickCount < 15){
				//imageView.setImageResource(R.drawable.peter_griffin_finished);
			}else if(clickCount > 15){
				imageView.setImageResource(R.drawable.peter_griffin_naked);
			}
			break;
		default:
				break;
		}
	}
}
