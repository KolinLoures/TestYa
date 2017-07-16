package com.example.kolin.testya.domain;

import com.example.kolin.testya.data.db.TranslationDAO;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by kolin on 16.07.2017.
 */

public class CheckFavoriteIs extends BaseObservableUseCase<Boolean, CheckFavoriteIs.Params> {


    private TranslationDAO queries;

    @Inject
    public CheckFavoriteIs(TranslationDAO queries) {
        this.queries = queries;
    }

    @Override
    public Observable<Boolean> createObservable(final Params params) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return queries.isFavorite(params.id);
            }
        });
    }


    public static final class Params{
        int id;

        private Params(int id) {
            this.id = id;
        }

        public static Params getParamsObj(int id){
            return new Params(id);
        }
    }
}
