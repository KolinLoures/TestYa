package com.example.kolin.testya.domain;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by kolin on 10.07.2017.
 */

public abstract class BaseCompletableUseCase<RequestParams> extends BaseObservableUseCase<Void, RequestParams> {


    public abstract Completable createCompletable(RequestParams params);

    @Override
    public final Observable<Void> createObservable(RequestParams params) {
        return createCompletable(params).toObservable();
    }

    public void execute(final DisposableCompletableObserver observer, RequestParams params) {

        final DisposableObserver<Void> obs = new DisposableObserver<Void>() {
            @Override
            public void onNext(Void aVoid) {
            }

            @Override
            public void onError(Throwable e) {
                observer.onError(e);
            }

            @Override
            public void onComplete() {
                observer.onComplete();
            }
        };

        super.execute(obs, params);
    }
}
