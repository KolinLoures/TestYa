package com.example.kolin.testya.domain;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.data.db.IQueries;
import com.example.kolin.testya.domain.model.InternalTranslation;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Created by kolin on 03.04.2017.
 */

public class AddRemoveTranslationDb extends BaseCompletableUseCase<AddRemoveTranslationDb.AddTranslationParams> {


    private IQueries queries;

    @Inject
    public AddRemoveTranslationDb(IQueries queries) {
        this.queries = queries;
    }

    @Override
    public Completable createCompletable(final AddTranslationParams addTranslationParams) {
        return Completable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return queries.addOrRemoveTranslation(
                        addTranslationParams.obj,
                        addTranslationParams.type,
                        addTranslationParams.remove
                );
            }
        });
    }


    public final static class AddTranslationParams {
        private final InternalTranslation obj;
        @TypeSaveTranslation.TypeName
        private final String type;
        private final boolean remove;

        private AddTranslationParams(InternalTranslation obj,
                                     @TypeSaveTranslation.TypeName String type,
                                     boolean remove) {
            this.obj = obj;
            this.remove = remove;
            this.type = type;
        }

        public static AddTranslationParams getParamsObj(InternalTranslation obj,
                                                        @TypeSaveTranslation.TypeName String type,
                                                        boolean remove
        ) {
            return new AddTranslationParams(obj, type, remove);
        }
    }
}
