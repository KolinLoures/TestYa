package com.example.kolin.testya.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kolin on 02.04.2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static DataBaseHelper dataBaseHelper = null;

    //DB info
    private static final String DB_NAME = "db_history_favorite";
    private static final int DB_VERSION = 1;

    //Table
    public static final String TABLE = "history_favorite";

    //Table columns
    public static final String COUNT = "count";
    public static final String TEXT = "text";
    public static final String LANG = "lang";
    public static final String TYPE = "type";


    private DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE +
                "(" +
                        COUNT + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        TEXT + " TEXT," +
                        LANG + " LANG," +
                        TYPE + " TYPE" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE);
            this.onCreate(db);
        }
    }


    public static synchronized DataBaseHelper getDataBaseHelper(Context context){
        if (dataBaseHelper == null){
            dataBaseHelper = new DataBaseHelper(context);
        }

        return dataBaseHelper;
    }

    public static DataBaseHelper getDataBaseHelper() {
        return dataBaseHelper;
    }
}
