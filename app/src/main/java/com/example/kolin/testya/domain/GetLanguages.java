package com.example.kolin.testya.domain;

import android.util.ArrayMap;
import android.util.Log;

import com.example.kolin.testya.data.languages.LanguageProperties;
import com.example.kolin.testya.domain.model.Language;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by kolin on 09.04.2017.
 * <p>
 * GetLanguages implementation of {@link BaseObservableUseCase}.
 * Use Case to get support languages from asset folder.
 */

public class GetLanguages extends BaseObservableUseCase<Language, GetLanguages.Params> {

    private LanguageProperties languageProperties;

    @Inject
    GetLanguages(LanguageProperties languageProperties) {
        this.languageProperties = languageProperties;
    }

    @Override
    public Observable<Language> createObservable(final GetLanguages.Params params) {

        final Observable<Language> languageObservable = Observable
                .fromCallable(new Callable<List<Language>>() {
                    @Override
                    public List<Language> call() throws Exception {
                        return languageProperties.getSupportLanguages();
                    }
                }).flatMap(new Function<List<Language>, ObservableSource<Language>>() {
                    @Override
                    public ObservableSource<Language> apply(@NonNull List<Language> languages) throws Exception {
                        return Observable.fromIterable(languages);
                    }
                });


        return params == null
                ? languageObservable
                : languageObservable.filter(new Predicate<Language>() {
                    @Override
                    public boolean test(@NonNull Language language) throws Exception {
                        return language.getCode().equals(params.codeLang);
                    }
                });
    }


    public static final class Params {
        private final String codeLang;

        private Params(String codeLang) {
            this.codeLang = codeLang;
        }

        public static Params getParamsObj(String codeLang) {
            return new Params(codeLang);
        }
    }


}
