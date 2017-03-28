package com.example.kolin.testya.net;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

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


}
