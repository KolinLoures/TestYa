package com.example.kolin.testya.data.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.domain.model.InternalTranslation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolin on 08.07.2017.
 */

public class Queries implements TranslationDAO {

    private static final String TAG = Queries.class.getSimpleName();

    DataBaseHelper helper;

    public Queries(DataBaseHelper helper) {
        this.helper = helper;
    }

    @Override
    public void addDBTranslation(@NonNull InternalTranslation translation) {
        updateDBTranslation(translation);
    }

    @Override
    public List<InternalTranslation> getDBTranslations() {
        Cursor cursor = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        List<InternalTranslation> result = new ArrayList<>();

        String sqlSelect = String.format(
                "SELECT * FROM %s",
                DataBaseHelper.TABLE
        );

        db.beginTransaction();

        try {

            cursor = db.rawQuery(sqlSelect, null);

            if (cursor.moveToFirst()) {
                do {
                    InternalTranslation temp = new InternalTranslation();

                    temp.setType(cursor.getString(cursor.getColumnIndex(DataBaseHelper.TYPE)));
                    temp.setLang(cursor.getString(cursor.getColumnIndex(DataBaseHelper.LANG)));
                    temp.setCode(200);
                    temp.setTextFrom(cursor.getString(cursor.getColumnIndex(DataBaseHelper.TEXT_FROM)));
                    temp.setTextTo(cursor.getString(cursor.getColumnIndex(DataBaseHelper.TEXT_TO)));

                    result.add(temp);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get from database");
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            db.endTransaction();
        }

        return result;
    }

    @Override
    public void updateDBTranslation(@NonNull InternalTranslation translation) {
        Cursor cursor = null;
        SQLiteDatabase db = helper.getWritableDatabase();

        db.beginTransaction();

        try {

            String sqlUpdate = String.format(
                    "UPDATE %s SET %s = '%s' AND %s = 'datetime()' WHERE %s = '%s' AND %s = '%s'",
                    DataBaseHelper.TABLE,
                    DataBaseHelper.TYPE,
                    translation.getType(),
                    DataBaseHelper.TIME,
                    DataBaseHelper.TEXT_FROM,
                    translation.getTextFrom(),
                    DataBaseHelper.LANG,
                    translation.getLang()
            );

            cursor = db.rawQuery(sqlUpdate, null);

            if (cursor.getColumnCount() != 1) {

                String sqlAdd = String.format(
                        "INSERT INTO %s VALUES('%s','%s','%s','%s','datetime()')",
                        DataBaseHelper.TABLE,
                        translation.getTextFrom(),
                        translation.getTextTo(),
                        translation.getLang(),
                        translation.getType()
                );

                db.execSQL(sqlAdd);
                db.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add in database");
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();

            db.endTransaction();
        }
    }

    @Override
    public void deleteDBTranslation(@NonNull InternalTranslation translation) {

        if (translation.getType().equals(TypeSaveTranslation.FAVORITE)) {
            translation.setType(TypeSaveTranslation.HISTORY);
            updateDBTranslation(translation);
            return;
        }

        SQLiteDatabase db = helper.getWritableDatabase();

        db.beginTransaction();

        try {
            String sqlDelete = String.format(
                    "DELETE FROM %s WHERE %s = '%s' AND %s = '%s'",
                    DataBaseHelper.TABLE,
                    DataBaseHelper.TEXT_FROM,
                    translation.getTextFrom(),
                    DataBaseHelper.LANG,
                    translation.getLang()
            );

            db.execSQL(sqlDelete);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying delete from database");
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void clearDBTranslationsHistory() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {

            String sqlClear = String.format(
                    "DELETE FROM %s WHERE %s = '%s'",
                    DataBaseHelper.TABLE,
                    DataBaseHelper.TYPE,
                    TypeSaveTranslation.HISTORY
            );

            db.execSQL(sqlClear);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all histories from data base");
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void clearDBTranslationsFavorite() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {

            String sqlClear = String.format(
                    "UPDATE %s SET %s = '%s' WHERE %s = '%s'",
                    DataBaseHelper.TABLE,
                    DataBaseHelper.TYPE,
                    TypeSaveTranslation.HISTORY,
                    DataBaseHelper.TYPE,
                    TypeSaveTranslation.FAVORITE
            );

            db.execSQL(sqlClear);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all favorites from data base");
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public boolean isFavorite(@NonNull InternalTranslation translation) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String queryCheck = String.format(
                "SELECT * FROM %s WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s'",
                DataBaseHelper.TABLE,
                DataBaseHelper.TEXT_FROM,
                translation.getTextFrom(),
                DataBaseHelper.TEXT_TO,
                translation.getTextTo(),
                DataBaseHelper.LANG,
                translation.getLang(),
                DataBaseHelper.TYPE,
                TypeSaveTranslation.FAVORITE);

        Cursor cursor = db.rawQuery(queryCheck, null);
        try {
            return cursor.moveToFirst();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying check from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return false;
    }
}
