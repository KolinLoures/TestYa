package com.example.kolin.testya.domain;

import com.example.kolin.testya.data.TypeOfTranslation;
import com.example.kolin.testya.data.db.TranslationDAO;
import com.example.kolin.testya.domain.model.HistoryFavoriteModel;
import com.example.kolin.testya.domain.model.InternalTranslation;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by kolin on 06.04.2017.
 * <p>
 * GetHistoryFavoriteTranslationFromDb implementation of {@link BaseCompletableUseCase}.
 * Use Case represents work with getting {@link InternalTranslation} object
 * from data base in according with type {@link TypeOfTranslation}.
 */

public class GetHistoryFavoriteTranslationFromDb extends BaseObservableUseCase<HistoryFavoriteModel,
        GetHistoryFavoriteTranslationFromDb.Params> {

    private TranslationDAO queries;

    @Inject
    GetHistoryFavoriteTranslationFromDb(TranslationDAO queries) {
        this.queries = queries;
    }

    @Override
    public Observable<HistoryFavoriteModel> createObservable(final Params params) {
        return params.type.equals(TypeOfTranslation.FAVORITE)
                ? getFavoritesFromDb()
                : getHistoriesFromDb();
    }

    private Observable<HistoryFavoriteModel> getHistoriesFromDb() {
        return Observable
                .fromCallable(new Callable<List<HistoryFavoriteModel>>() {
                    @Override
                    public List<HistoryFavoriteModel> call() throws Exception {
                        return queries.getAllHistory();
                    }
                })
                .flatMap(new Function<List<HistoryFavoriteModel>, ObservableSource<HistoryFavoriteModel>>() {
                    @Override
                    public ObservableSource<HistoryFavoriteModel> apply(@NonNull List<HistoryFavoriteModel> historyFavoriteModels) throws Exception {
                        return Observable.fromIterable(historyFavoriteModels);
                    }
                });
    }

    private Observable<HistoryFavoriteModel> getFavoritesFromDb() {
        return Observable
                .fromCallable(new Callable<List<HistoryFavoriteModel>>() {
                    @Override
                    public List<HistoryFavoriteModel> call() throws Exception {
                        return queries.getAllFavorites();
                    }
                })
                .flatMap(new Function<List<HistoryFavoriteModel>, ObservableSource<HistoryFavoriteModel>>() {
                    @Override
                    public ObservableSource<HistoryFavoriteModel> apply(@NonNull List<HistoryFavoriteModel> historyFavoriteModels) throws Exception {
                        return Observable.fromIterable(historyFavoriteModels);
                    }
                });
    }



    /**
     * Parameters class
     */
    public static class Params {
        @TypeOfTranslation.TypeName
        private String type;


        private Params(String type) {
            this.type = type;
        }

        /**
         * Get Parameters object for {@link GetHistoryFavoriteTranslationFromDb}
         *
         * @return Parameters object {@link Params}
         */
        public static Params getParamsObj(@TypeOfTranslation.TypeName String type) {
            return new Params(type);
        }
    }
}
