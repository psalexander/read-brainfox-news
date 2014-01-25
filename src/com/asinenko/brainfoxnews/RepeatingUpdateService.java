package com.asinenko.brainfoxnews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class RepeatingUpdateService extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "It's Service Time!", Toast.LENGTH_LONG).show();
	}
}
