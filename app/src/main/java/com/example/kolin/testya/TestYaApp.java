package com.example.kolin.testya;

import android.app.Application;
import android.content.Context;

import com.example.kolin.testya.data.db.DataBaseHelper;

/**
 * Created by kolin on 02.04.2017.
 */

public class TestYaApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        DataBaseHelper.getDataBaseHelper(getApplicationContext());
    }
}
