package com.example.kolin.testya.domain;

import android.text.TextUtils;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.data.db.IQueries;
import com.example.kolin.testya.data.entity.Translation;
import com.example.kolin.testya.data.net.NetTranslator;
import com.example.kolin.testya.domain.model.InternalTranslation;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by kolin on 31.03.2017.
 */

public class GetTranslation extends BaseObservableUseCase<InternalTranslation, GetTranslation.TranslationParams> {

    private static final int DELAY = 500;

    private NetTranslator netTranslator;
    private IQueries queries;

    @Inject
    GetTranslation(IQueries queries, NetTranslator netTranslator) {
        this.netTranslator = netTranslator;
        this.queries = queries;
    }

    @Override
    public Observable<InternalTranslation> createObservable(final TranslationParams translationParams) {

        final Observable<InternalTranslation> observable;

        Observable<InternalTranslation> netObservable = netTranslator
                .getTranslation(
                        NetTranslator.API_KEY_TRNSL,
                        translationParams.text,
                        translationParams.lang)
                .map(new Function<Translation, InternalTranslation>() {
                    @Override
                    public InternalTranslation apply(@io.reactivex.annotations.NonNull
                                                             Translation translation) throws Exception {
                        InternalTranslation temp = new InternalTranslation();
                        temp.setCode(translation.getCode());
                        temp.setFavorite(false);
                        temp.setLang(translation.getLang());
                        temp.setTextTo(TextUtils.join(" ", translation.getText()));
                        temp.setTextFrom(translationParams.text);
                        temp.setType(TypeSaveTranslation.HISTORY);
                        return temp;
                    }
                });

        if (translationParams.loadFromNetworkOnly) {

            Observable<InternalTranslation> dbObservable = Observable
                    .fromCallable(new Callable<List<InternalTranslation>>() {
                        @Override
                        public List<InternalTranslation> call() throws Exception {
                            return queries.getTranslations(null);
                        }
                    })
                    .flatMap(new Function<List<InternalTranslation>, ObservableSource<InternalTranslation>>() {
                        @Override
                        public ObservableSource<InternalTranslation> apply(@io.reactivex.annotations.NonNull List<InternalTranslation> translations) throws Exception {
                            return Observable.fromIterable(translations);
                        }
                    }).filter(new Predicate<InternalTranslation>() {
                        @Override
                        public boolean test(@io.reactivex.annotations.NonNull InternalTranslation internalTranslation) throws Exception {
                            return internalTranslation.getTextFrom().equals(translationParams.text) &&
                                    internalTranslation.getLang().equals(translationParams.lang);
                        }
                    });

            observable = Observable
                    .concat(dbObservable, netObservable)
                    .firstElement()
                    .flatMapObservable(new Function<InternalTranslation, ObservableSource<? extends InternalTranslation>>() {
                        @Override
                        public ObservableSource<? extends InternalTranslation> apply(@io.reactivex.annotations.NonNull InternalTranslation internalTranslation) throws Exception {
                            return Observable.just(internalTranslation);
                        }
                    });

        } else
            observable = netObservable;


        return observable
                .delay(DELAY, TimeUnit.MILLISECONDS)
                .doOnNext(new Consumer<InternalTranslation>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull
                                               InternalTranslation internalTranslation) throws Exception {
                        internalTranslation.setFavorite(
                                queries.isFavorite(
                                        internalTranslation
                                )
                        );

                        queries.addOrUpdateTranslation(internalTranslation, TypeSaveTranslation.HISTORY);
                    }
                });
    }


    public static final class TranslationParams {

        private final String text;
        private final String lang;
        private final boolean loadFromNetworkOnly;

        public TranslationParams(String text, String lang, boolean loadFromNetworkOnly) {
            this.text = text;
            this.lang = lang;
            this.loadFromNetworkOnly = loadFromNetworkOnly;
        }

        public static TranslationParams getEntity(String text, String lang, boolean checkInDb) {
            return new TranslationParams(text, lang, checkInDb);
        }
    }

}
