package com.example.kolin.testya.domain;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.data.db.IQueries;
import com.example.kolin.testya.data.db.QueriesImpl;
import com.example.kolin.testya.domain.model.InternalTranslation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by kolin on 06.04.2017.
 */

public class GetTranslationsDb extends BaseUseCase<InternalTranslation,
        GetTranslationsDb.GetTranslationsDbParams> {

    private IQueries iQueries;

    public GetTranslationsDb() {
        this.iQueries = new QueriesImpl();
    }

    @Override
    public Observable<InternalTranslation> createObservable(
            final GetTranslationsDbParams params) {
        return Observable
                .fromCallable(new Callable<List<InternalTranslation>>() {
                    @Override
                    public List<InternalTranslation> call() throws Exception {
                        return iQueries.getTranslation(params.type);
                    }
                })
                .flatMap(new Function<List<InternalTranslation>, ObservableSource<InternalTranslation>>() {
                    @Override
                    public ObservableSource<InternalTranslation> apply(@NonNull List<InternalTranslation> translations) throws Exception {
                        Collections.reverse(translations);
                        return Observable.fromIterable(translations);
                    }
                })
                .doOnNext(new Consumer<InternalTranslation>() {
                    @Override
                    public void accept(@NonNull InternalTranslation translation) throws Exception {
                        translation
                                .setFavorite(
                                        iQueries.isAddedToTable(translation, TypeSaveTranslation.FAVORITE));
                    }
                });
    }

    public static class GetTranslationsDbParams {
        private String type;

        private GetTranslationsDbParams(String type) {
            this.type = type;
        }

        public static GetTranslationsDbParams getParamsObj(@TypeSaveTranslation.TypeName
                                                                   String type) {
            return new GetTranslationsDbParams(type);
        }
    }
}
