package com.example.kolin.testya.domain;

import com.example.kolin.testya.data.db.TranslationDAO;
import com.example.kolin.testya.domain.model.InternalTranslation;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by kolin on 11.07.2017.
 */

public class GetEntityFromDB extends BaseObservableUseCase<InternalTranslation,GetEntityFromDB.Params> {

    private TranslationDAO queries;

    @Inject
    public GetEntityFromDB(TranslationDAO queries) {
        this.queries = queries;
    }

    @Override
    public Observable<InternalTranslation> createObservable(final Params params) {
        return Observable.fromCallable(new Callable<InternalTranslation>() {
            @Override
            public InternalTranslation call() throws Exception {
                return queries.getEntityById(params.id);
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
