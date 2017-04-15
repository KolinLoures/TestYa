package com.example.kolin.testya.di;

import android.content.Context;

import com.example.kolin.testya.data.db.DataBaseHelper;
import com.example.kolin.testya.data.db.IQueries;
import com.example.kolin.testya.data.db.QueriesImpl;
import com.example.kolin.testya.data.languages.LanguageProperties;
import com.example.kolin.testya.data.net.NetSingleton;
import com.example.kolin.testya.data.net.NetTranslator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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
    IQueries providesQueriesImpl(DataBaseHelper dataBaseHelper){
        return new QueriesImpl(dataBaseHelper);
    }

    @Provides
    @Singleton
    LanguageProperties providesLanguageProperties(Context context){
        return new LanguageProperties(context);
    }

    @Provides
    @Singleton
    NetSingleton providesNetSingleton(){
        return new NetSingleton();
    }

    @Provides
    @Singleton
    NetTranslator providesNetTranslator(NetSingleton netSingleton){
        return netSingleton.provideTranslator();
    }


}
