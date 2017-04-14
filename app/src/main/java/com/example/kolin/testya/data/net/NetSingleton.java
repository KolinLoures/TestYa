package com.example.kolin.testya.data.net;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kolin on 28.03.2017.
 */

public class NetSingleton {

    private static NetSingleton netSingleton = null;

    private NetSingleton() {
    }



    private OkHttpClient provideOkHttpClient() {

        HttpLoggingInterceptor loggingInterceptor =
                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }


    private Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(NetTranslator.BASE_URL_TRNSL)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public NetTranslator provideTranslator() {
        return provideRetrofit().create(NetTranslator.class);
    }

    public static NetSingleton provideneSingleton() {
        if (netSingleton == null) {
            netSingleton = new NetSingleton();
        }
        return netSingleton;
    }

}
