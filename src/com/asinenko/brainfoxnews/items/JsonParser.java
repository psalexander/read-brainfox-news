package com.asinenko.brainfoxnews.items;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonParser {

	// JSON Node names
	private static final String TAG_ID = "id";
	private static final String TAG_TITLE = "name";
	private static final String TAG_SHORTTEXT = "shorttext";
	private static final String TAG_TIMESTAMP = "ts";
	private static final String TAG_DATA = "data";
	private static final String TAG_DATE = "date";
	private static final String TAG_ERROR_CODE = "error_code";
	private static final String TAG_TYPE = "type";

	private static final String TAG_TEXT = "text";
	private static final String TAG_IMAGES = "images";

	public static List<NewsItemJSON> parseJSONtoNewsItemJSON(String jsonStr){
		List<NewsItemJSON> list = new ArrayList<NewsItemJSON>();
		if (jsonStr != null) {
			try {
				JSONObject jsonObj = new JSONObject(jsonStr);
				NewsItemJSON.errorcode = String.valueOf(jsonObj.getInt(TAG_ERROR_CODE));
				NewsItemJSON.timestamp = jsonObj.getString(TAG_TIMESTAMP);

				JSONArray data = jsonObj.getJSONArray(TAG_DATA);
				for (int i = 0; i < data.length(); i++) {
					JSONObject c = data.getJSONObject(i);
					String id = c.getString(TAG_ID);
					String name = c.getString(TAG_TITLE);
					String shorttext = c.getString(TAG_SHORTTEXT);
					String date = c.getString(TAG_DATE);
					String type = c.getString(TAG_TYPE);
					NewsItemJSON it = new NewsItemJSON(id, name, shorttext, date, type);
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
		if (jsonStr != null) {
			try {
				JSONObject jsonObj = new JSONObject(jsonStr);
				NewsListItem.errorcode = jsonObj.getString(TAG_ERROR_CODE);
				NewsListItem.timestamp = jsonObj.getString(TAG_TIMESTAMP);
				JSONArray data = jsonObj.getJSONArray(TAG_DATA);
				for (int i = 0; i < data.length(); i++) {
					JSONObject c = data.getJSONObject(i);
					String id = c.getString(TAG_ID);
					String name = c.getString(TAG_TITLE);
					String shorttext = c.getString(TAG_SHORTTEXT);
					String date = c.getString(TAG_DATE);
					String type = c.getString(TAG_TYPE);
					NewsListItem it = new NewsListItem(id, name, shorttext, date, type);
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

	public static NewsItem parseNewsItem(String id, String jsonStr){
		NewsItem item = null;
		if (jsonStr != null) {
			try {
				JSONObject jsonObj = new JSONObject(jsonStr);
				NewsItem.errorcode = jsonObj.getString(TAG_ERROR_CODE);
				String title = jsonObj.getString(TAG_TITLE);
				String shorttext = jsonObj.getString(TAG_SHORTTEXT);
				String mainText = jsonObj.getString(TAG_TEXT);
				String date = jsonObj.getString(TAG_DATE);
				item = new NewsItem(id, title, shorttext, mainText, date);
				JSONArray data = jsonObj.getJSONArray(TAG_IMAGES);
				List<String> imageList = new LinkedList<String>();
				for (int i = 0; i < data.length(); i++) {
					imageList.add(String.valueOf(data.getInt(i)));
				}
				item.setImages(imageList);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}
		return item;
	}
}
