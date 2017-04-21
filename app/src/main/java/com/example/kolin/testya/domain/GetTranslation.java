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
 *
 * GetTranslation implementation of {@link BaseObservableUseCase}.
 * Use Case to get translation from net.
 */

public class GetTranslation extends BaseObservableUseCase<InternalTranslation, GetTranslation.TranslationParams> {

    // Delay for getting response from net and adding translation as history to data base.
    // Add this delay, cause I do not want to overload my DB with responses, when user typing word.
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

    /**
     * Parameters class
     */
    public static final class TranslationParams {

        private final String text;
        private final String lang;
        //if you want to load only from network
        private final boolean loadFromNetworkOnly;

        public TranslationParams(String text, String lang, boolean loadFromNetworkOnly) {
            this.text = text;
            this.lang = lang;
            this.loadFromNetworkOnly = loadFromNetworkOnly;
        }

        /**
         * Get Parameters object for {@link GetLanguages}
         *
         * @param checkInDb set to load only from network
         * @return Parameters object {@link GetLanguages.GetLanguageParams}
         */
        public static TranslationParams getParamsObj(String text, String lang, boolean checkInDb) {
            return new TranslationParams(text, lang, checkInDb);
        }
    }

}
