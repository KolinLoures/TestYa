package com.example.kolin.testya.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.domain.model.InternalTranslation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolin on 03.04.2017.
 */

public class QueriesImpl implements IQueries {

    private static final String TAG = QueriesImpl.class.getSimpleName();

    protected DataBaseHelper helper;

    public QueriesImpl(DataBaseHelper dataBaseHelper) {
        this.helper = dataBaseHelper;
    }

    @Override
    public boolean addOrRemoveTranslation(InternalTranslation translation,
                                          @TypeSaveTranslation.TypeName String type,
                                          boolean remove) {
        return remove ?
                removeTranslation(translation, type) :
                addOrUpdateTranslation(translation, type);
    }

    @Override
    public boolean addOrUpdateTranslation(InternalTranslation translation,
                                          @TypeSaveTranslation.TypeName String type) {

        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = getAllContentValues(translation, type);
            String whereClause = String.format(
                    "%s = ? AND %s = ? AND %s = ? AND %s = ?",
                    DataBaseHelper.TEXT_FROM,
                    DataBaseHelper.TEXT_TO,
                    DataBaseHelper.LANG,
                    DataBaseHelper.TYPE);


            int rows = db.update(
                    DataBaseHelper.TABLE,
                    values,
                    whereClause,
                    new String[]{
                            translation.getTextFrom(),
                            translation.getTextTo(),
                            translation.getLang(),
                            type
                    });

            if (rows != 1) {
                db.insertOrThrow(DataBaseHelper.TABLE, null, values);
                db.setTransactionSuccessful();
                return true;
            }

        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update in database");
        } finally {
            db.endTransaction();
        }
        return false;
    }

    @Override
    public List<InternalTranslation> getTranslations(@TypeSaveTranslation.TypeName String type) {
        SQLiteDatabase db = helper.getReadableDatabase();

        List<InternalTranslation> temp = new ArrayList<>();
        String query;

        if (type == null)
            query = String.format("SELECT * FROM %s", DataBaseHelper.TABLE);
        else
            query = String.format(
                    "SELECT * FROM %s WHERE %s = \"%s\"",
                    DataBaseHelper.TABLE, DataBaseHelper.TYPE, type);

        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    InternalTranslation translation = new InternalTranslation();
                    translation.setLang(cursor.getString(cursor.getColumnIndex(DataBaseHelper.LANG)));
                    translation.setTextFrom(cursor.getString(cursor.getColumnIndex(DataBaseHelper.TEXT_FROM)));
                    translation.setTextTo(cursor.getString(cursor.getColumnIndex(DataBaseHelper.TEXT_TO)));
                    translation.setType(cursor.getString(cursor.getColumnIndex(DataBaseHelper.TYPE)));
                    if (translation.getType().equals(TypeSaveTranslation.FAVORITE))
                        translation.setFavorite(true);

                    temp.add(translation);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return temp;
    }


    @Override
    public boolean removeTranslation(InternalTranslation translation,
                                     @TypeSaveTranslation.TypeName String type) {

        SQLiteDatabase db = helper.getWritableDatabase();

        db.beginTransaction();
        try {

            String where = String.format(
                    "%s = ? AND %s = ? AND %s = ?",
                    DataBaseHelper.TEXT_FROM,
                    DataBaseHelper.TEXT_TO,
                    DataBaseHelper.TYPE
            );

            db.delete(DataBaseHelper.TABLE, where,
                    new String[]{
                            translation.getTextFrom(),
                            translation.getTextTo(),
                            TypeSaveTranslation.FAVORITE
                    });

            db.setTransactionSuccessful();
            return true;

        } catch (Exception e) {
            Log.d(TAG, "Error while trying delete from database");
        } finally {
            db.endTransaction();
        }
        return false;
    }

    @Override
    public boolean deleteAllType(@TypeSaveTranslation.TypeName
                                      String type) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(DataBaseHelper.TABLE, DataBaseHelper.TYPE + "= ? ", new String[]{type});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all from data base");
        } finally {
            db.endTransaction();
        }
        return false;
    }

    @Override
    public boolean isFavorite(InternalTranslation translation) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = String.format(
                "SELECT * FROM %s WHERE %s = \"%s\" AND %s = \"%s\" AND %s = \"%s\" AND %s = \"%s\"",
                DataBaseHelper.TABLE,
                DataBaseHelper.TEXT_FROM,
                translation.getTextFrom(),
                DataBaseHelper.TEXT_TO,
                translation.getTextTo(),
                DataBaseHelper.LANG,
                translation.getLang(),
                DataBaseHelper.TYPE,
                TypeSaveTranslation.FAVORITE);

        Cursor cursor = db.rawQuery(query, null);
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

    private ContentValues getAllContentValues(InternalTranslation translation,
                                              @TypeSaveTranslation.TypeName String type) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.TEXT_FROM, translation.getTextFrom());
        values.put(DataBaseHelper.TEXT_TO, translation.getTextTo());
        values.put(DataBaseHelper.LANG, translation.getLang());
        values.put(DataBaseHelper.TYPE, type);
        return values;
    }
}
