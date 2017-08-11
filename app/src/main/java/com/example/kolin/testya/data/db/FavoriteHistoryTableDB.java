package com.example.kolin.testya.data.db;

import android.content.ContentValues;

import java.util.Calendar;

/**
 * Created by kolin on 09.07.2017.
 */

public class FavoriteHistoryTableDB {

    public static final String TABLE_NAME = FavoriteHistoryTableDB.class.getSimpleName();

    //Table columns
    static final String ID = "id";
    static final String TEXT_FROM = "text_from";
    static final String TEXT_TO = "text_to";
    static final String LANG = "lang";
    static final String DICTIONARY = "dictionary";
    static final String IS_HISTORY = "is_history";
    static final String IS_FAVORITE = "is_favorite";
    static final String TIME = "time_stamp";


    public static String createTable() {
        return "CREATE TABLE "
                + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY,"
                + TEXT_FROM + " TEXT,"
                + TEXT_TO + " TEXT,"
                + LANG + " TEXT,"
                + DICTIONARY + " TEXT,"
                + IS_HISTORY + " INTEGER,"
                + IS_FAVORITE + " INTEGER DEFAULT 0,"
                + TIME + " LONG,"
                + " UNIQUE ("
                + TEXT_FROM + ","
                + TEXT_TO + ","
                + LANG + ")"
                + ");";
    }

    public static String getAllFavorites() {
        return "SELECT * FROM " + TABLE_NAME
                + " WHERE " + IS_FAVORITE + " = 1"
                + " ORDER BY " + TIME + " DESC";
    }

    public static String getAllHistory() {
        return "SELECT * FROM " + TABLE_NAME
                + " WHERE " + IS_HISTORY + " = 1"
                + " ORDER BY " + TIME + " DESC";
    }


    public static String getEntityById(int id) {
        return "SELECT * FROM " + TABLE_NAME
                + " WHERE " + ID + " = " + id;
    }

    public static String getIsFavorite(int id) {
        return "SELECT " + IS_FAVORITE
                + " FROM " + TABLE_NAME
                + " WHERE " + ID + " = " + id;
    }

    public static String deleteAllFavorites(){
        return "UPDATE " + TABLE_NAME
                + " SET " + IS_FAVORITE + " = 0"
                + " WHERE " + IS_FAVORITE + " = 1";
    }

    public static String deleteAllHistory(){
        return "UPDATE " + TABLE_NAME
                + " SET " + IS_HISTORY + " = 0"
                + " WHERE " + IS_HISTORY + " = 1";
    }

    public static String addEntityToFavorites(int id){
        return "UPDATE " + TABLE_NAME
                + " SET " + IS_FAVORITE + " = 1"
                + " WHERE " + ID + " = " + id;
    }

    public static String deleteEntityFromFavorite(int id){
        return "UPDATE " + TABLE_NAME
                + " SET " + IS_FAVORITE + " = 0"
                + " WHERE " + ID + " = " + id;
    }

    public static String deleteEntityFromHistory(int id){
        return "UPDATE " + TABLE_NAME
                + " SET " + IS_HISTORY + " = 0"
                + " WHERE " + ID + " = " + id;
    }

    public static String clearData(){
        return "DELETE FROM " + TABLE_NAME
                + " WHERE " + IS_HISTORY + " = 0"
                + " AND " + IS_FAVORITE + " = 0";
    }


    public static String getEntity(String textFrom,
                                   String textTo,
                                   String lang){
        return "SELECT * FROM " + TABLE_NAME
                + " WHERE " + TEXT_FROM + " = '" + textFrom + "'"
                + " AND " + TEXT_TO + " = '" + textTo + "'"
                + " AND " + LANG + " = '" + lang + "'";
    }

    public static String getId(String textFrom,
                               String textTo,
                               String lang){
        return "SELECT " + ID + " FROM " + TABLE_NAME
                + " WHERE " + TEXT_FROM + " = '" + textFrom + "'"
                + " AND " + TEXT_TO + " = '" + textTo + "'"
                + " AND " + LANG + " = '" + lang + "'";
    }

    public static ContentValues contentValues(String textFrom,
                                              String textTo,
                                              String lang,
                                              String dictionary){
        ContentValues contentValues = new ContentValues();

        contentValues.put(TEXT_FROM, textFrom);
        contentValues.put(TEXT_TO, textTo);
        contentValues.put(LANG, lang);
        contentValues.put(DICTIONARY, dictionary);
        contentValues.put(IS_HISTORY, 1);
        contentValues.put(TIME, Calendar.getInstance().getTimeInMillis());

        return contentValues;
    }



}
