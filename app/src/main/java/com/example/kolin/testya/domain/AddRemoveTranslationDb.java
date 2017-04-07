package com.example.kolin.testya.domain;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.data.db.IQueries;
import com.example.kolin.testya.data.db.QueriesImpl;
import com.example.kolin.testya.domain.model.InternalTranslation;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kolin on 03.04.2017.
 */

public class AddRemoveTranslationDb extends BaseUseCase<Boolean, AddRemoveTranslationDb.AddTranslationParams> {


    private IQueries queries;

    public AddRemoveTranslationDb() {
        this.queries = new QueriesImpl();
    }

    @Override
    public Observable<Boolean> createObservable(final AddTranslationParams addTranslationParams) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return queries.addOrRemoveTranslation(
                        addTranslationParams.obj,
                        addTranslationParams.remove
                );
            }
        });
    }


    public final static class AddTranslationParams {
        private final InternalTranslation obj;
        private final boolean remove;

        private AddTranslationParams(InternalTranslation obj, boolean remove) {
            this.obj = obj;
            this.remove = remove;
        }

        public static AddTranslationParams getParamsObj(InternalTranslation obj, boolean remove){
            return new AddTranslationParams(obj, remove);
        }
    }
}
