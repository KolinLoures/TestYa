package com.example.kolin.testya.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Data base helper that create main data base for app.
 * Contains one table for history/favorites translations.
 *
 * Table
 * |           |         |      |      |
 * | TEXT_FROM | TEXT_TO | LANG | TYPE |
 * |___________|_________|______|______|
 * |           |         |      |      |
 *
 * todo: add time stamp to table
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    //DB info
    private static final String DB_NAME = "db_history_favorite";
    private static final int DB_VERSION = 1;

    //Table name
    static final String TABLE = "history_favorite";

    //Table columns
    static final String TEXT_FROM = "text_from";
    static final String TEXT_TO = "text_to";
    static final String LANG = "lang";
    static final String TYPE = "type";


    public DataBaseHelper(Context context) {
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


}
