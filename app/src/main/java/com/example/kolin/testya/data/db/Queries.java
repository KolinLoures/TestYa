package com.example.kolin.testya.data.db;

import android.database.Cursor;

import com.example.kolin.testya.data.entity.dictionary.Dictionary;
import com.example.kolin.testya.domain.model.HistoryFavoriteModel;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolin on 08.07.2017.
 */

public class Queries implements TranslationDAO {

    private static final Gson gson = new Gson();

    DataBaseHelper helper;

    public Queries(DataBaseHelper helper) {
        this.helper = helper;
    }

    @Override
    public List<HistoryFavoriteModel> getAllHistory() {
        return this.cursorToHistoryFavoriteModel(
                helper.getCursor(FavoriteHistoryTableDB.getAllHistory())
        );
    }

    @Override
    public List<HistoryFavoriteModel> getAllFavorites() {
        return this.cursorToHistoryFavoriteModel(
                helper.getCursor(FavoriteHistoryTableDB.getAllFavorites())
        );
    }

    @Override
    public InternalTranslation getEntityById(int id) {
        return this.cursorToInternalTranslation(
                helper.getCursor(FavoriteHistoryTableDB.getEntityById(id)));
    }

    @Override
    public boolean isFavorite(int id) {
        boolean isFavorite = false;
        Cursor cursor = helper.getCursor(FavoriteHistoryTableDB.getIsFavorite(id));
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int isCol = cursor.getColumnIndex(FavoriteHistoryTableDB.IS_FAVORITE);
                isFavorite = cursor.getInt(isCol) == 1;
            } while (cursor.moveToNext());
        }

        return isFavorite;
    }

    @Override
    public void checkEntityToFavorite(int id) {
        helper.execSQL(FavoriteHistoryTableDB.addEntityToFavorites(id));
    }

    @Override
    public void deleteAllFavorites() {
        helper.execSQL(FavoriteHistoryTableDB.deleteAllFavorites());
    }

    @Override
    public void deleteAllHistory() {
        helper.execSQL(FavoriteHistoryTableDB.deleteAllHistory());
    }

    @Override
    public void deleteEntityFromFavorites(int id) {
        helper.execSQL(FavoriteHistoryTableDB.deleteEntityFromFavorite(id));
    }

    @Override
    public void deleteEntityFromHistory(int id) {
        helper.execSQL(FavoriteHistoryTableDB.deleteEntityFromHistory(id));
    }

    @Override
    public void clearUselessData() {
        helper.execSQL(FavoriteHistoryTableDB.clearData());
    }

    @Override
    public void addToTable(String textFrom, String textTo, String lang, String dictionary) {
        int id = -1;


        Cursor cursor = helper.getCursor(FavoriteHistoryTableDB.getId(textFrom, textTo, lang));
        if (cursor != null && cursor.moveToNext()) {
            do {
                int idCol = cursor.getColumnIndex(FavoriteHistoryTableDB.ID);
                id = cursor.getInt(idCol);
            } while (cursor.moveToNext());

            cursor.close();
        }

        helper.writeEntityToTable(
                FavoriteHistoryTableDB.contentValues(textFrom, textTo, lang, dictionary),
                id
        );
    }

    @Override
    public InternalTranslation getEntity(String textFrom, String textTo, String lang) {
        return this.cursorToInternalTranslation(
                helper.getCursor(FavoriteHistoryTableDB.getEntity(textFrom, textTo, lang)));
    }

    private List<HistoryFavoriteModel> cursorToHistoryFavoriteModel(Cursor cursor) {
        List<HistoryFavoriteModel> temp = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {

            int idCol = cursor.getColumnIndex(FavoriteHistoryTableDB.ID);
            int textFromCol = cursor.getColumnIndex(FavoriteHistoryTableDB.TEXT_FROM);
            int textToCol = cursor.getColumnIndex(FavoriteHistoryTableDB.TEXT_TO);
            int langCol = cursor.getColumnIndex(FavoriteHistoryTableDB.LANG);
            int isCol = cursor.getColumnIndex(FavoriteHistoryTableDB.IS_FAVORITE);

            do {
                HistoryFavoriteModel historyFavoriteModel = new HistoryFavoriteModel();
                historyFavoriteModel.setId(cursor.getInt(idCol));
                historyFavoriteModel.setTextFrom(cursor.getString(textFromCol));
                historyFavoriteModel.setTextTo(cursor.getString(textToCol));
                historyFavoriteModel.setLang(cursor.getString(langCol));
                historyFavoriteModel.setFavorite(cursor.getInt(isCol) == 1);

                temp.add(historyFavoriteModel);

            } while (cursor.moveToNext());

            cursor.close();
        }


        return temp;
    }

    private InternalTranslation cursorToInternalTranslation(Cursor cursor) {

        InternalTranslation translation = new InternalTranslation();

        if (cursor != null && cursor.moveToFirst()) {

            int idCol = cursor.getColumnIndex(FavoriteHistoryTableDB.ID);
            int textFromCol = cursor.getColumnIndex(FavoriteHistoryTableDB.TEXT_FROM);
            int textToCol = cursor.getColumnIndex(FavoriteHistoryTableDB.TEXT_TO);
            int langCol = cursor.getColumnIndex(FavoriteHistoryTableDB.LANG);
            int dicCol = cursor.getColumnIndex(FavoriteHistoryTableDB.DICTIONARY);
            int isCol = cursor.getColumnIndex(FavoriteHistoryTableDB.IS_FAVORITE);

            do {

                translation.setId(cursor.getInt(idCol));
                translation.setTextFrom(cursor.getString(textFromCol));
                translation.setTextTo(cursor.getString(textToCol));
                translation.setLang(cursor.getString(langCol));
                translation.setDef(gson.fromJson(cursor.getString(dicCol), Dictionary.class).getDef());
                translation.setFavorite(cursor.getInt(isCol) == 1);


            } while (cursor.moveToNext());

        }

        return translation;
    }
}
