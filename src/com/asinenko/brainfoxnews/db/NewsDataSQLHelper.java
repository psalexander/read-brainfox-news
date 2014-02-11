package com.asinenko.brainfoxnews.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsDataSQLHelper extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "news.db";
	private static final int DATABASE_VERSION = 22;

	//Settings table
	public static final String TABLE_SETTINGS = "settings";
	public static final String SETTINGS_COLUMN_ID = "_id";
	public static final String SETTINGS_COLUMN_NAME = "name";
	public static final String SETTINGS_COLUMN_VALUE = "value";

	private static final String DATABASE_CREATE_SETTINGS = "create table "
			+ TABLE_SETTINGS + "(" + SETTINGS_COLUMN_ID + " integer primary key autoincrement, "
			+ SETTINGS_COLUMN_NAME + " text not null, "
			+ SETTINGS_COLUMN_VALUE + " text not null);";

	//News table
	public static final String TABLE_NEWS = "news";
	public static final String NEWS_COLUMN_ID = "_id";
	public static final String NEWS_COLUMN_DB_ID = "dbid";
	public static final String NEWS_COLUMN_TITLE = "title";
	public static final String NEWS_COLUMN_SHORTTEXT = "shorttext";
	public static final String NEWS_COLUMN_DATE = "date";
	public static final String NEWS_COLUMN_TYPE = "type";
	public static final String NEWS_COLUMN_IMAGE = "image";

	private static final String DATABASE_CREATE_NEWS = "create table "
			+ TABLE_NEWS + "(" + NEWS_COLUMN_ID + " integer primary key autoincrement, "
			+ NEWS_COLUMN_DB_ID + " integer, "
			+ NEWS_COLUMN_TITLE + " text not null, "
			+ NEWS_COLUMN_SHORTTEXT + " text not null, "
			+ NEWS_COLUMN_DATE + " text not null, "
			+ NEWS_COLUMN_TYPE + " text, "
			+ NEWS_COLUMN_IMAGE + " text);";

	public NewsDataSQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE_SETTINGS);
		db.execSQL(DATABASE_CREATE_NEWS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
		onCreate(db);
	}
}
