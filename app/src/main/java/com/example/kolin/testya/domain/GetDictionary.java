package com.example.kolin.testya.domain;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.kolin.testya.data.entity.dictionary.Def;
import com.example.kolin.testya.data.entity.dictionary.Dictionary;
import com.example.kolin.testya.data.net.NetSingleton;
import com.example.kolin.testya.data.net.NetTranslator;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by kolin on 01.04.2017.
 */

public class GetDictionary extends BaseObservableUseCase<List<Def>, GetDictionary.DictionaryParams> {

    //Delay for start dictionary search
    private static final int DELAY_MILLISECONDS = 750;

    private NetTranslator netTranslator;

    public GetDictionary(Context context) {
        this.netTranslator = NetSingleton.provideneSingleton().provideTranslator();
    }

    @Override
    public Observable<List<Def>> createObservable(@NonNull DictionaryParams dictionaryParams) {
        return netTranslator.getTranslationOptions(
                NetTranslator.API_KEY_DICT,
                dictionaryParams.text,
                dictionaryParams.lang,
                "ru")
                .flatMap(new Function<Dictionary, ObservableSource<List<Def>>>() {
                    @Override
                    public ObservableSource<List<Def>> apply(@io.reactivex.annotations.NonNull
                                                                     Dictionary dictionary) throws Exception {
                        return Observable.just(dictionary.getDef());
                    }
                })
                .delay(DELAY_MILLISECONDS, TimeUnit.MILLISECONDS);
    }

    public static final class DictionaryParams{
        private final String text;
        private final String lang;
        //TODO: add support of ui param
        private String ui;

        private DictionaryParams(String text, String lang) {
            this.text = text;
            this.lang = lang;
        }

        public static DictionaryParams getEntity(String text, String lang) {
            return new DictionaryParams(text, lang);
        }
    }
}
