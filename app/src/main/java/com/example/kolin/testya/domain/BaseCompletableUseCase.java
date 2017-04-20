package com.example.kolin.testya.domain;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kolin on 14.04.2017.
 *
 * Abstract class for a Use Case (In terms of Clean Architecture).
 *
 * By convention each UseCase implementation will return the result using a {@link DisposableCompletableObserver}
 * that will execute its job in a background thread and will post the result in the UI thread.
 */

public abstract class BaseCompletableUseCase<RequestParams> {

    private CompositeDisposable compositeDisposable;

    public BaseCompletableUseCase() {
        this.compositeDisposable = new CompositeDisposable();
    }


    /**
     * Create {@link Completable} source which will be execute.
     */
    public abstract Completable createCompletable(RequestParams params);

    /**
     * Executes the current use case.
     *
     * @param obs  {@link DisposableCompletableObserver} which will be listening to the observable build
     *              by {@link #createCompletable(RequestParams)} ()} method.
     *
     * @param params Parameters used to build/execute this use case.
     */
    public void execute(DisposableCompletableObserver obs, RequestParams params){

        final DisposableCompletableObserver observer = createCompletable(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(obs);

        compositeDisposable.add(observer);
    }

    /**
     * Dispose all observers
     */
    public void dispose(){
        if (!compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
    }

    /**
     * Clear all observers
     */
    public void clearDisposableObservers(){
        if (!compositeDisposable.isDisposed()){
            compositeDisposable.clear();
        }
    }
}
