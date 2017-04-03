package com.example.kolin.testya.data.repository;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.data.db.Queries;
import com.example.kolin.testya.data.entity.Translation;
import com.example.kolin.testya.data.entity.dictionary.Def;
import com.example.kolin.testya.data.entity.dictionary.Dictionary;
import com.example.kolin.testya.data.net.NetSingleton;
import com.example.kolin.testya.data.net.NetTranslator;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by kolin on 31.03.2017.
 */

public class RepositoryImpl implements Repository {

    private NetTranslator netTranslator;
    private Queries queries;


    public RepositoryImpl() {
        netTranslator = NetSingleton.providenetSingleton().provideTranslator();
        queries = new Queries();
    }

    @Override
    public Observable<Translation> getPhraseTranslation(String text, String lang) {
        return netTranslator.getTranslation(
                NetTranslator.API_KEY_TRNSL,
                text,
                lang).doOnNext(new Consumer<Translation>() {
            @Override
            public void accept(@NonNull Translation translation) throws Exception {
                queries.addTranslation(translation, TypeSaveTranslation.HISTORY);
            }
        });
    }

    @Override
    public Observable<List<Def>> getTranslationOptions(String text, String lang) {
        return netTranslator.getTranslationOptions(
                NetTranslator.API_KEY_DICT,
                text,
                lang,
                "ru")
                .flatMap(new Function<Dictionary, ObservableSource<List<Def>>>() {
                    @Override
                    public ObservableSource<List<Def>> apply(@NonNull Dictionary dictionary) throws Exception {
                        return Observable.just(dictionary.getDef());
                    }
                });
    }
}
