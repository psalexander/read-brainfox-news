package com.asinenko.brainfoxnews.db;

import java.util.LinkedList;
import java.util.List;

import com.asinenko.brainfoxnews.items.NewsSQLItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class NewsDataSource {
	private SQLiteDatabase database;
	private NewsDataSQLHelper dbHelper;

	private static String LASTREQUEST = "lastreq";

	private String[] allNewsColumns = { NewsDataSQLHelper.NEWS_COLUMN_ID,
										NewsDataSQLHelper.NEWS_COLUMN_DB_ID,
										NewsDataSQLHelper.NEWS_COLUMN_TITLE,
										NewsDataSQLHelper.NEWS_COLUMN_SHORTTEXT,
										NewsDataSQLHelper.NEWS_COLUMN_DATE,
										NewsDataSQLHelper.NEWS_COLUMN_TYPE,
										NewsDataSQLHelper.NEWS_COLUMN_IMAGE};

	private String[] allSettingsColumns = { NewsDataSQLHelper.SETTINGS_COLUMN_ID,
											NewsDataSQLHelper.SETTINGS_COLUMN_NAME,
											NewsDataSQLHelper.SETTINGS_COLUMN_VALUE};

	public NewsDataSource(Context context) {
		dbHelper = new NewsDataSQLHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public long createNewsItem(NewsSQLItem item){
		ContentValues values = new ContentValues();
		values.put(NewsDataSQLHelper.NEWS_COLUMN_ID, item.getId());
		values.put(NewsDataSQLHelper.NEWS_COLUMN_DB_ID, item.getDbId());
		values.put(NewsDataSQLHelper.NEWS_COLUMN_TITLE, item.getTitle());
		values.put(NewsDataSQLHelper.NEWS_COLUMN_SHORTTEXT, item.getShorttext());
		values.put(NewsDataSQLHelper.NEWS_COLUMN_DATE, item.getDate());
		values.put(NewsDataSQLHelper.NEWS_COLUMN_TYPE, item.getType());
		values.put(NewsDataSQLHelper.NEWS_COLUMN_IMAGE, item.getImage());
		long insertId = database.insert(NewsDataSQLHelper.TABLE_NEWS, null, values);
		return insertId;
	}

	public void addLastReq() {
		ContentValues values = new ContentValues();
		values.put(NewsDataSQLHelper.SETTINGS_COLUMN_NAME, LASTREQUEST); // Contact Name
		values.put(NewsDataSQLHelper.SETTINGS_COLUMN_VALUE, "0"); // Contact Phone Number
		database.insert(NewsDataSQLHelper.TABLE_SETTINGS, null, values);
	}

	public String getLastRequestTime(){
		String resp = "";
		String getLastQuery = "SELECT * FROM " + NewsDataSQLHelper.TABLE_SETTINGS + " WHERE name='" + LASTREQUEST + "'";
		Cursor cursor = database.rawQuery(getLastQuery, null);
		if(cursor == null || cursor.getCount() == 0){
			addLastReq();
		}else{
			cursor.moveToFirst();
			resp = cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.SETTINGS_COLUMN_VALUE));//2
		}
		cursor.close();
		return resp;
	}

	public void deleteNews(String id) {
		database.delete(NewsDataSQLHelper.TABLE_NEWS, NewsDataSQLHelper.NEWS_COLUMN_DB_ID + " = " + id, null);
	}

	public int updateLastRequestTime(String time) {
		ContentValues values = new ContentValues();
		values.put(NewsDataSQLHelper.SETTINGS_COLUMN_VALUE, time);
		return database.update(NewsDataSQLHelper.TABLE_SETTINGS, values, NewsDataSQLHelper.SETTINGS_COLUMN_NAME + " = ?", new String[] { LASTREQUEST });
	}

	public int getNewsCount(){
		String countQuery = "SELECT  * FROM " + NewsDataSQLHelper.TABLE_NEWS;
		Cursor cursor = database.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}

	public List<NewsSQLItem> getAllItems(){
		List<NewsSQLItem> list = new LinkedList<NewsSQLItem>();
		Cursor cursor = database.query(NewsDataSQLHelper.TABLE_NEWS, allNewsColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			NewsSQLItem item = cursorToNews(cursor);
			list.add(item);
			cursor.moveToNext();
		}
		return list;
	}

	private NewsSQLItem cursorToNews(Cursor cursor){
		NewsSQLItem item = new NewsSQLItem();
		item.setId(String.valueOf(cursor.getLong(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_ID))));
		item.setDbId(String.valueOf(cursor.getLong(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_DB_ID))));
		item.setTitle(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_TITLE)));
		item.setShorttext(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_SHORTTEXT)));
		item.setType(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_TYPE)));
		item.setDate(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_DATE)));
		item.setImage(cursor.getString(cursor.getColumnIndex(NewsDataSQLHelper.NEWS_COLUMN_IMAGE)));
		return item;
	}

	public Cursor getNewsCursor(){
		Cursor cursor = database.query(NewsDataSQLHelper.TABLE_NEWS, allNewsColumns, null, null, null, null, NewsDataSQLHelper.NEWS_COLUMN_DB_ID + " DESC");
		return cursor;
	}
}
