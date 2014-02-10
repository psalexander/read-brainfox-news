package com.asinenko.brainfoxnews.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class RepeatingAlarmGetNewsService extends Service {

	public static final int INTERVAL = 10000; // 10 sec
	public static final int FIRST_RUN = 5000; // 5 seconds
	public int REQUEST_CODE = 11223344;

	private AlarmManager alarmManager;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v(this.getClass().getName(), "onCreate(..)");
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.v(this.getClass().getName(), "onBind(..)");
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v(this.getClass().getName(), "onStartCommand(..)");
		startService();
		return Service.START_NOT_STICKY;
	}
	@Override
	public void onDestroy() {
		if (alarmManager != null) {
			Intent intent = new Intent(this, RepeatingUpdateService.class);
			alarmManager.cancel(PendingIntent.getBroadcast(this, REQUEST_CODE, intent, 0));
		}
		Toast.makeText(this, "Service Stopped!", Toast.LENGTH_LONG).show();
		Log.v(this.getClass().getName(), "Service onDestroy(). Stop AlarmManager at " + new java.sql.Timestamp(System.currentTimeMillis()).toString());
	}

	private void startService() {
		Intent intent = new Intent(this, RepeatingUpdateService.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, 0);

		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + FIRST_RUN, INTERVAL, pendingIntent);

		Toast.makeText(this, "Service Started.", Toast.LENGTH_LONG).show();
		Log.v(this.getClass().getName(), "AlarmManger started at " + new java.sql.Timestamp(System.currentTimeMillis()).toString());
	}
}
