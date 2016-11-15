package com.gaokao366.gaokao366touser.model.framework.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDataBaseHelper extends SQLiteOpenHelper {

	// 数据库名称，开启注释把数据库放到Sdcard上
	private static final String DB_NAME = "kaisa.db";
	/**
	 * 数据库版本，升级时修改
	 */
	private static final int DB_VERSION = 1;

	private AppDataBaseHelper(Context context) {
		super(context,DB_NAME, null,DB_VERSION);
	}

	private static AppDataBaseHelper dbOpenHelper = null;

	/**
	 * 得到数据库实例
	 * @param context
	 * @return 数据库的SQLiteOpenHelper对象
	 */
	public static synchronized AppDataBaseHelper getInstance(Context context) {
		if (dbOpenHelper == null) {
			dbOpenHelper = new AppDataBaseHelper(context);
			return dbOpenHelper;
		} else {
			return dbOpenHelper;
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	

}
