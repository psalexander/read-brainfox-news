package com.asinenko.brainfoxnews;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class NewsDataSource {
	private SQLiteDatabase database;
	private NewsDataSQLHelper dbHelper;

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

	public long createGPSLocation(NewsSQLItem item){
		ContentValues values = new ContentValues();
		
		values.put(NewsDataSQLHelper.NEWS_COLUMN_ID, item.getId());
		values.put(NewsDataSQLHelper.NEWS_COLUMN_DB_ID, item.getDbId());
		values.put(NewsDataSQLHelper.NEWS_COLUMN_TITLE, item.getTitle());
		values.put(NewsDataSQLHelper.NEWS_COLUMN_SHORTTEXT, item.getShorttext());
		values.put(NewsDataSQLHelper.NEWS_COLUMN_TYPE, item.getType());
		values.put(NewsDataSQLHelper.NEWS_COLUMN_DATE, item.getDate());
		values.put(NewsDataSQLHelper.NEWS_COLUMN_IMAGE, item.getImage());

		long insertId = database.insert(NewsDataSQLHelper.TABLE_NEWS, null, values);
		return insertId;
	}
}
