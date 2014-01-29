package com.asinenko.brainfoxnews;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class LastRequestTimeSQLHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "settings.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_SETTINGS = "settings";

	public static final String SETTINGS_COLUMN_ID = "_id";
	public static final String SETTINGS_COLUMN_NAME = "name";
	public static final String SETTINGS_COLUMN_VALUE = "value";

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
		+ TABLE_SETTINGS + "(" + SETTINGS_COLUMN_ID + " integer primary key autoincrement, "
		+ SETTINGS_COLUMN_NAME + " text not null, "
		+ SETTINGS_COLUMN_VALUE + " text not null);";

	public LastRequestTimeSQLHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
		onCreate(db);
	}
}
