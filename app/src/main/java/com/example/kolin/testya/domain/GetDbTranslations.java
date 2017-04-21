package com.example.kolin.testya.domain;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.data.db.IQueries;
import com.example.kolin.testya.domain.model.InternalTranslation;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by kolin on 06.04.2017.
 *
 * GetDbTranslations implementation of {@link BaseCompletableUseCase}.
 * Use Case represents work with getting {@link InternalTranslation} object
 * from data base in according with type {@link TypeSaveTranslation}.
 */

public class GetDbTranslations extends BaseObservableUseCase<List<InternalTranslation>,
        GetDbTranslations.GetTranslationsDbParams> {

    private IQueries iQueries;

    @Inject
    GetDbTranslations(IQueries queries) {
        this.iQueries = queries;
    }

    @Override
    public Observable<List<InternalTranslation>> createObservable(final GetTranslationsDbParams params) {
        return Observable
                .fromCallable(new Callable<List<InternalTranslation>>() {
                    @Override
                    public List<InternalTranslation> call() throws Exception {
                        return iQueries.getTranslations(params.type);
                    }
                })
                .map(new Function<List<InternalTranslation>, List<InternalTranslation>>() {
                    @Override
                    public List<InternalTranslation> apply(@NonNull List<InternalTranslation> translations) throws Exception {
                        Collections.reverse(translations);
                        return translations;
                    }
                })
                .doOnNext(new Consumer<List<InternalTranslation>>() {
                    @Override
                    public void accept(@NonNull List<InternalTranslation> translations) throws Exception {
                        for (InternalTranslation t: translations){
                            if (t.getType().equals(TypeSaveTranslation.HISTORY))
                                t.setFavorite(iQueries.isFavorite(t));
                        }
                    }
                });
    }

    /**
     * Parameters class
     */
    public static class GetTranslationsDbParams {
        private String type;


        private GetTranslationsDbParams(String type) {
            this.type = type;
        }

        /**
         * Get Parameters object for {@link GetDbTranslations}
         *
         * @return Parameters object {@link GetDbTranslations.GetTranslationsDbParams}
         */
        public static GetTranslationsDbParams getParamsObj(@TypeSaveTranslation.TypeName
                                                                   String type) {
            return new GetTranslationsDbParams(type);
        }
    }
}
