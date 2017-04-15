package com.example.kolin.testya.domain;

import android.util.ArrayMap;

import com.example.kolin.testya.data.languages.LanguageProperties;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by kolin on 09.04.2017.
 */

public class GetLanguages extends BaseObservableUseCase<ArrayMap<String, String>,
        GetLanguages.GetLanguageParams> {

    private LanguageProperties languageProperties;

    @Inject
    GetLanguages(LanguageProperties languageProperties) {
        this.languageProperties = languageProperties;
    }

    @Override
    public Observable<ArrayMap<String, String>> createObservable(GetLanguageParams getLanguageParams) {
        return Observable.fromCallable(new Callable<ArrayMap<String, String>>() {
            @Override
            public ArrayMap<String, String> call() throws Exception {
                return languageProperties.getSupportLanguage();
            }
        });
    }


    public static class GetLanguageParams {
        private final boolean refresh;

        private GetLanguageParams(boolean refresh) {
            this.refresh = refresh;
        }

        public static GetLanguageParams getParamsObj(boolean refresh) {
            return new GetLanguageParams(refresh);
        }
    }
}
