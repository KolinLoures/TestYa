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
    static final String TABLE = "history_favorite";

    //Table columns
    static final String TEXT_FROM = "text_from";
    static final String TEXT_TO = "text_to";
    static final String LANG = "lang";
    static final String TYPE = "type";


    private DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE +
                "(" +
                        TEXT_FROM + " TEXT," +
                        TEXT_TO + " TEXT," +
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
