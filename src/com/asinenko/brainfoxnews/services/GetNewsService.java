package com.asinenko.brainfoxnews.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

public class GetNewsService extends Service{
	private AlarmManager alarmManager;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show(); 

	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();

	}
	public static final int INTERVAL = 10000; // 10 sec
	public static final int FIRST_RUN = 5000; // 5 seconds
	int REQUEST_CODE = 11223344;
	@Override
	public void onStart(Intent intent, int startid) {
		Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
		Intent repeatIntent = new Intent(this, RepeatingUpdateService.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, 0);
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + FIRST_RUN, INTERVAL, pendingIntent);
	}
}
