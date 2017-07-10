package com.example.kolin.testya.di.modules;

import android.content.Context;

import com.example.kolin.testya.data.db.DataBaseHelper;
import com.example.kolin.testya.data.db.Queries;
import com.example.kolin.testya.data.db.TranslationDAO;
import com.example.kolin.testya.data.languages.LanguageProperties;
import com.example.kolin.testya.data.net.NetTranslator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kolin on 15.04.2017.
 */
@Singleton
@Module
public class DataModule {

    @Provides
    @Singleton
    DataBaseHelper providesDataBaseHelper(Context context){
        return new DataBaseHelper(context);
    }


    @Provides
    @Singleton
    TranslationDAO providesDao(DataBaseHelper dataBaseHelper){
        return new Queries(dataBaseHelper);
    }

    @Provides
    @Singleton
    LanguageProperties providesLanguageProperties(Context context){
        return new LanguageProperties(context);
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {

        HttpLoggingInterceptor loggingInterceptor =
                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(NetTranslator.BASE_URL_TRNSL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public NetTranslator provideTranslator(Retrofit retrofit) {
        return retrofit.create(NetTranslator.class);
    }


}
