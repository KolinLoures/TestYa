package com.example.kolin.testya.domain;

import com.example.kolin.testya.data.db.IQueries;
import com.example.kolin.testya.data.db.QueriesImpl;

import java.util.concurrent.Callable;

import io.reactivex.Completable;

/**
 * Created by kolin on 08.04.2017.
 */

public class DeleteTypeDb extends BaseCompletableUseCase<DeleteTypeDb.DeleteRequestParams> {

    private IQueries queries;

    public DeleteTypeDb() {
        this.queries = new QueriesImpl();
    }

    @Override
    public Completable createCompletable(final DeleteRequestParams deleteRequestParams) {
        return Completable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return queries.deleteAllType(deleteRequestParams.type);
            }
        });
    }


    public static class DeleteRequestParams {
        private final String type;

        private DeleteRequestParams(String type) {
            this.type = type;
        }

        public static DeleteRequestParams getParamsObj(String type) {
            return new DeleteRequestParams(type);
        }
    }
}
