package com.example.kolin.testya.domain;

import android.support.annotation.NonNull;

import com.example.kolin.testya.data.entity.dictionary.Def;
import com.example.kolin.testya.data.repository.Repository;
import com.example.kolin.testya.data.repository.RepositoryImpl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Created by kolin on 01.04.2017.
 */

public class GetDictionary extends BaseUseCase<List<Def>, GetDictionary.DictionaryParams> {

    //Delay for start dictionary search
    private static final int DELAY_MILLISECONDS = 750;

    private Repository repository;

    public GetDictionary() {
        this.repository = new RepositoryImpl();
    }

    @Override
    public Observable<List<Def>> createObservable(@NonNull DictionaryParams dictionaryParams) {
        return repository
                .getTranslationOptions(dictionaryParams.text, dictionaryParams.lang)
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
