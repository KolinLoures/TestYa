package com.example.kolin.testya.domain;

import com.example.kolin.testya.data.db.TranslationDAO;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Created by kolin on 11.07.2017.
 */

public class DeleteHistoryEntityFromDB extends BaseCompletableUseCase<DeleteHistoryEntityFromDB.Params> {

    private TranslationDAO queries;

    @Inject
    public DeleteHistoryEntityFromDB(TranslationDAO queries) {
        this.queries = queries;
    }

    @Override
    public Completable createCompletable(final Params params) {
        return Completable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                queries.deleteEntityFromHistory(params.id);
                return true;
            }
        });
    }

    public static final class Params{
        private int id;

        private Params(int id) {
            this.id = id;
        }

        public static Params getParamsObject(int id){
            return new Params(id);
        }
    }
}
