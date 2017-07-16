package com.example.kolin.testya.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.Pair;

import javax.inject.Inject;

/**
 * Created by kolin on 15.07.2017.
 */

public class LanguagePreferencesManager {

    private static final String FILE_NAME = "TestYaPreferences";

    private static final String KEY_FROM = "lang_from";
    private static final String KEY_TO = "lang_to";

    private SharedPreferences preferences = null;

    @Inject
    public LanguagePreferencesManager(Context context) {
        preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public void saveLangsPreferences(String langFrom, String langTo){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_FROM, langFrom);
        editor.putString(KEY_TO, langTo);
        editor.apply();
    }

    public Pair<String, String> getLangFromPreferences(){
        String langFrom = preferences.getString(KEY_FROM, "en");
        String langTo = preferences.getString(KEY_TO, "ru");

        return new Pair<>(langFrom, langTo);
    }

}
