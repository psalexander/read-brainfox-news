package com.asinenko.brainfoxnews.items;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonParser {

	// JSON Node names
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_SHORTTEXT = "shorttext";
	private static final String TAG_TIMESTAMP = "ts";
	private static final String TAG_DATA = "data";
	private static final String TAG_DATE = "date";
	private static final String TAG_ERROR_CODE = "error_code";

	public static List<NewsItemJSON> parseJSONtoNewsItemJSON(String jsonStr){
		List<NewsItemJSON> list = new ArrayList<NewsItemJSON>();
		list.clear();
		if (jsonStr != null) {
			try {
				JSONObject jsonObj = new JSONObject(jsonStr);
				NewsItemJSON.errorcode = jsonObj.getString(TAG_ERROR_CODE);
				NewsItemJSON.timestamp = jsonObj.getString(TAG_TIMESTAMP);

				JSONArray data = jsonObj.getJSONArray(TAG_DATA);
				for (int i = 0; i < data.length(); i++) {
					JSONObject c = data.getJSONObject(i);
					String id = c.getString(TAG_ID);
					String name = c.getString(TAG_NAME);
					String shorttext = c.getString(TAG_SHORTTEXT);
					String date = c.getString(TAG_DATE);
					NewsItemJSON it = new NewsItemJSON(id, name, shorttext, date);
					list.add(it);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}
		return list;
	}

	public static List<NewsListItem> parseJSONtoNewsItem(String jsonStr){
		List<NewsListItem> list = new ArrayList<NewsListItem>();
		list.clear();
		if (jsonStr != null) {
			try {
				JSONObject jsonObj = new JSONObject(jsonStr);
				NewsListItem.errorcode = jsonObj.getString(TAG_ERROR_CODE);
				NewsListItem.timestamp = jsonObj.getString(TAG_TIMESTAMP);

				JSONArray data = jsonObj.getJSONArray(TAG_DATA);
				for (int i = 0; i < data.length(); i++) {
					JSONObject c = data.getJSONObject(i);
					String id = c.getString(TAG_ID);
					String name = c.getString(TAG_NAME);
					String shorttext = c.getString(TAG_SHORTTEXT);
					String date = c.getString(TAG_DATE);
					NewsListItem it = new NewsListItem(id, name, shorttext, date);
					list.add(it);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}
		return list;
	}
}
