package com.example.kolin.testya.di.components;

import android.app.Application;
import android.content.Context;

import com.example.kolin.testya.TestYaApp;
import com.example.kolin.testya.data.db.DataBaseHelper;
import com.example.kolin.testya.data.db.TranslationDAO;
import com.example.kolin.testya.data.languages.LanguageProperties;
import com.example.kolin.testya.data.net.NetTranslator;
import com.example.kolin.testya.data.preferences.LanguagePreferencesManager;
import com.example.kolin.testya.di.ActivityBuilder;
import com.example.kolin.testya.di.modules.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by kolin on 15.04.2017.
 */

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        ActivityBuilder.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance AppComponent.Builder application(Application app);
        AppComponent build();
    }

    void inject(TestYaApp app);

    Context getContext();

    DataBaseHelper getDataBaseHelper();

    LanguagePreferencesManager getLangPreferencesManager();

    TranslationDAO provideDAO();

    LanguageProperties getLangProperties();

    OkHttpClient getOkHttpClient();

    Retrofit getRetrofit();

    NetTranslator getNetTranslator();


}
