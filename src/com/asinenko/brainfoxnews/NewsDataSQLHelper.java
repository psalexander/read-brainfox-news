package com.asinenko.brainfoxnews;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsDataSQLHelper extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "news.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_LOCATION = "news";	//
	public static final String COLUMN_ID = "_id";				// integer
	public static final String COLUMN_TITLE = "title";	// real
	public static final String COLUMN_LONGITUDE = "longitude";	// real
	public static final String COLUMN_ACCURACY = "accuracy";	// real
	public static final String COLUMN_ALTITUDE = "altitude";	// real
	public static final String COLUMN_BEARING = "bearing";		// real
	public static final String COLUMN_SPEED = "speed";			// real
	public static final String COLUMN_PROVIDER = "provider";	// text
	public static final String COLUMN_ISUP = "up";				// integer
	public static final String COLUMN_TIME = "time";			// text
	
	public NewsDataSQLHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
