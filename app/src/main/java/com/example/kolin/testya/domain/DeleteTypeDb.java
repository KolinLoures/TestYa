package com.example.kolin.testya.domain;

import com.example.kolin.testya.data.TypeOfTranslation;
import com.example.kolin.testya.data.db.TranslationDAO;
import com.example.kolin.testya.domain.model.InternalTranslation;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Created by kolin on 08.04.2017.
 * <p>
 * DeleteTypeDb implementation of {@link BaseCompletableUseCase}.
 * Use Case represents work with deleting {@link InternalTranslation} object
 * from data base in according with type {@link TypeOfTranslation}.
 */

public class DeleteTypeDb extends BaseCompletableUseCase<DeleteTypeDb.Params> {

    private TranslationDAO queries;

    @Inject
    public DeleteTypeDb(TranslationDAO queries) {
        this.queries = queries;
    }

    @Override
    public Completable createCompletable(final Params params) {
        if (params.type == null)
            return completableClearUselessData();
        else
            return params.type.equals(TypeOfTranslation.FAVORITE)
                    ? completableDeleteAllFavoritesFromDb()
                    : completableDeleteAllHistoryFromDb();
    }

    private Completable completableDeleteAllFavoritesFromDb() {
        return Completable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                queries.deleteAllFavorites();
                return true;
            }
        });
    }

    private Completable completableDeleteAllHistoryFromDb() {
        return Completable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                queries.deleteAllHistory();
                return true;
            }
        });
    }

    private Completable completableClearUselessData() {
        return Completable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                queries.clearUselessData();
                return true;
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
         * Get Parameters object for {@link DeleteTypeDb}
         *
         * @return Parameters object {@link Params}
         */
        public static Params getParamsObject(@TypeOfTranslation.TypeName String type) {
            return new Params(type);
        }
    }
}
