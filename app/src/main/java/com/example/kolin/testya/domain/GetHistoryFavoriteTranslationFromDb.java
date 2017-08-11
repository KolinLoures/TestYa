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

public class GetHistoryFavoriteTranslationFromDb extends BaseObservableUseCase<List<HistoryFavoriteModel>,
        GetHistoryFavoriteTranslationFromDb.Params> {

    private TranslationDAO queries;

    @Inject
    GetHistoryFavoriteTranslationFromDb(TranslationDAO queries) {
        this.queries = queries;
    }

    @Override
    public Observable<List<HistoryFavoriteModel>> createObservable(final Params params) {
        return params.type.equals(TypeOfTranslation.FAVORITE)
                ? getFavoritesFromDb()
                : getHistoriesFromDb();
    }

    private Observable<List<HistoryFavoriteModel>> getHistoriesFromDb() {
        return Observable
                .fromCallable(new Callable<List<HistoryFavoriteModel>>() {
                    @Override
                    public List<HistoryFavoriteModel> call() throws Exception {
                        return queries.getAllHistory();
                    }
                });
    }

    private Observable<List<HistoryFavoriteModel>> getFavoritesFromDb() {
        return Observable
                .fromCallable(new Callable<List<HistoryFavoriteModel>>() {
                    @Override
                    public List<HistoryFavoriteModel> call() throws Exception {
                        return queries.getAllFavorites();
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
