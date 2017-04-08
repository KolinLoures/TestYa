package com.example.kolin.testya.domain;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.data.db.IQueries;
import com.example.kolin.testya.data.db.QueriesImpl;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by kolin on 08.04.2017.
 */

public class DeleteTypeDb extends BaseUseCase<Void, DeleteTypeDb.DeleteRequestParams> {

    private IQueries queries;

    public DeleteTypeDb() {
        this.queries = new QueriesImpl();
    }

    @Override
    public Observable<Void> createObservable(final DeleteRequestParams deleteRequestParams) {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(ObservableEmitter<Void> e) throws Exception {
                queries.deleteAllType(deleteRequestParams.type);
                e.onComplete();
            }
        });
    }

    public static class DeleteRequestParams{
        private final String type;

        private DeleteRequestParams(String type) {
            this.type = type;
        }

        public static DeleteRequestParams getParamsObj(@TypeSaveTranslation.TypeName
                                                       String type){
            return new DeleteRequestParams(type);
        }
    }
}
