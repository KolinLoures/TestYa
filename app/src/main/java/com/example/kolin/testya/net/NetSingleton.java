package com.example.kolin.testya.net;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kolin on 28.03.2017.
 */

public class NetSingleton {

    private NetSingleton netSingleton = null;

    private NetSingleton(){
    }

    private OkHttpClient provideOkHttpClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    private Retrofit provideRetrofit(){
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private ITranslator provideTranslator(){
        return provideRetrofit().create(ITranslator.class);
    }

    public NetSingleton providenetSingleton(){
        if (netSingleton == null){
            netSingleton = new NetSingleton();
        }
        return netSingleton;
    }

}
