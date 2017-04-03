package com.example.kolin.testya;

import android.app.Application;

import com.example.kolin.testya.data.db.DataBaseHelper;
import com.facebook.stetho.Stetho;

/**
 * Created by kolin on 02.04.2017.
 */

public class TestYaApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        DataBaseHelper.getDataBaseHelper(getApplicationContext());
    }
}
