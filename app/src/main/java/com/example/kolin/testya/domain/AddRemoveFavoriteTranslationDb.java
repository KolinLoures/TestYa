package com.example.kolin.testya.domain;

import com.example.kolin.testya.data.db.TranslationDAO;
import com.example.kolin.testya.domain.model.InternalTranslation;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Created by kolin on 03.04.2017.
 * <p>
 * AddRemoveFavoriteTranslationDb implementation of {@link BaseCompletableUseCase}.
 * Use Case represents work with adding and removing {@link InternalTranslation}
 * object from Data Base.
 */

public class AddRemoveFavoriteTranslationDb extends BaseCompletableUseCase<AddRemoveFavoriteTranslationDb.Params> {

    private TranslationDAO queries;

    @Inject
    public AddRemoveFavoriteTranslationDb(TranslationDAO queries) {
        this.queries = queries;
    }

    @Override
    public Completable createCompletable(final Params params) {
        return params.remove
                ? completableDeleteFavoriteFromDB(params.id)
                : completableAddFavoriteToDB(params.id);
    }

    private Completable completableAddFavoriteToDB(final int id){
        return Completable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                queries.checkEntityToFavorite(id);
                return true;
            }
        });
    }

    private Completable completableDeleteFavoriteFromDB(final int id){
        return Completable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                queries.deleteEntityFromFavorites(id);
                return true;
            }
        });
    }

    /**
     * Parameters class
     */
    public final static class Params {
        private int id;
        private boolean remove;

        public Params(int id, boolean remove) {
            this.id = id;
            this.remove = remove;
        }

        public static Params getParamsObject(int id, boolean remove) {
            return new Params(id, remove);
        }
    }
}
