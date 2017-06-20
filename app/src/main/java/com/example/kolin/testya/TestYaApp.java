package com.example.kolin.testya;

import android.app.Application;

import com.example.kolin.testya.di.components.AppComponent;
import com.example.kolin.testya.di.components.DaggerAppComponent;
import com.example.kolin.testya.di.modules.AppModule;


/**
 * Created by kolin on 02.04.2017.
 */

public class TestYaApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();



        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();

    }

    public AppComponent getAppComponent(){
        return appComponent;
    }
}
