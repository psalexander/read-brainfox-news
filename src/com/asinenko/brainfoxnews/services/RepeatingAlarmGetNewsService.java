package com.asinenko.brainfoxnews.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

public class RepeatingAlarmGetNewsService extends Service {

	public static final int INTERVAL = 30 * 60 * 1000;
	public static final int FIRST_RUN = 30000; // 30 seconds
	public int REQUEST_CODE = 11223344;

	private AlarmManager alarmManager;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startService();
		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (alarmManager != null) {
			Intent intent = new Intent(this, RepeatingUpdateService.class);
			alarmManager.cancel(PendingIntent.getBroadcast(this, REQUEST_CODE, intent, 0));
		}
		//Toast.makeText(this, "Service Stopped!", Toast.LENGTH_LONG).show();
	}

	private void startService() {
		Intent intent = new Intent(this, RepeatingUpdateService.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, 0);
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + FIRST_RUN, INTERVAL, pendingIntent);
		//Toast.makeText(this, "Service Started.", Toast.LENGTH_LONG).show();
	}
}
