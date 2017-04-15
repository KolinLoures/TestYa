package com.example.kolin.testya.di;

import android.content.Context;

import com.example.kolin.testya.data.db.DataBaseHelper;
import com.example.kolin.testya.data.db.IQueries;
import com.example.kolin.testya.data.languages.LanguageProperties;
import com.example.kolin.testya.data.net.NetTranslator;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by kolin on 15.04.2017.
 */

@Singleton
@Component(modules = {AppModule.class, DataModule.class})
public interface AppComponent {

    Context getContext();

    DataBaseHelper getDataBaseHelper();

    IQueries provideQueriesImpl();

    LanguageProperties getLangProperties();

    OkHttpClient getOkHttpClient();

    Retrofit getRetrofit();

    NetTranslator getNetTranslator();

}
