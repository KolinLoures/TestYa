package com.example.kolin.testya.data.db;

import android.database.sqlite.SQLiteDatabase;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.data.entity.Translation;

/**
 * Created by kolin on 03.04.2017.
 */

public class Queries {

    private DataBaseHelper helper;

    public Queries() {
        this.helper = DataBaseHelper.getDataBaseHelper();
    }

    public boolean addTranslation(Translation translation,
                                   @TypeSaveTranslation.TypeName String type){

        String query = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES ('%s', '%s', '%s', '%s')",
                DataBaseHelper.TABLE,
                DataBaseHelper.TEXT_FROM,
                DataBaseHelper.TEXT_TO,
                DataBaseHelper.LANG,
                DataBaseHelper.TYPE,
                translation.getText().toString(),
                translation.getLang(),
                type);

        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(query);
        return true;
    }
}
