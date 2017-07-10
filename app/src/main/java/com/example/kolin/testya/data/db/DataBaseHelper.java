package com.example.kolin.testya.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

/**
 * Data base helper that create main data base for app.
 * Contains one table for history/favorites translations.
 * <p>
 * Table
 * |           |         |      |      |         |
 * | TEXT_FROM | TEXT_TO | LANG | TYPE |  TIME   |
 * |___________|_________|______|______|_________|
 * |           |         |      |      |         |
 * <p>
 * todo: add time stamp to table
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    //DB info
    private static final String DB_NAME = "translator.db_history_favorite";
    private static final int DB_VERSION = 1;


    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FavoriteHistoryTableDB.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + FavoriteHistoryTableDB.TABLE_NAME);
            this.onCreate(db);
        }
    }

    public Cursor getCursor(String sql) {
        return this.getReadableDatabase().rawQuery(sql, null);
    }

    public Cursor getCursor(String sql,
                            @NonNull String[] selectionArgs) {
        return this.getReadableDatabase().rawQuery(sql, selectionArgs);
    }

    public void writeEntityToTable(ContentValues contentValues, int id){
        SQLiteDatabase db = this.getWritableDatabase();

        if (id == -1)
            db.insert(FavoriteHistoryTableDB.TABLE_NAME, null, contentValues);
        else
            db.update(FavoriteHistoryTableDB.TABLE_NAME, contentValues, "ID = ?", new String[]{Integer.toString(id)});
    }

    public void execSQL(String sql){
        this.getWritableDatabase().execSQL(sql);
    }


}
