package com.asinenko.brainfoxnews;

import android.content.Context;
import android.net.ConnectivityManager;

public class CheckConnection {

	public static boolean isHostActive(Context context){
//		String cs = Context.CONNECTIVITY_SERVICE;
//		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(cs);
//		cm.requestRouteToHost(context.TYPE_WIFI, int hostAddress);
		return false;
	}

	public static boolean isOnline(Context context) {
		String cs = Context.CONNECTIVITY_SERVICE;
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(cs);
		if (cm.getActiveNetworkInfo() == null) {
			return false;
		}
		return cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}

	public static boolean isWiFiActive(Context context) {
		String cs = Context.CONNECTIVITY_SERVICE;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(cs);
		return cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
	}

	public static boolean isMobileInetActive(Context context) {
		String cs = Context.CONNECTIVITY_SERVICE;
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(cs);
		return cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
	}
}
