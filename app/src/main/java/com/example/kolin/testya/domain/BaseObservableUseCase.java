package com.example.kolin.testya.domain;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kolin on 31.03.2017.
 *
 * Abstract class for a Use Case (In terms of Clean Architecture).
 *
 * By convention each UseCase implementation will return the result using a {@link DisposableObserver}
 * that will execute its job in a background thread and will post the result in the UI thread.
 */

public abstract class BaseObservableUseCase<T, RequestParams> {

    private CompositeDisposable compositeDisposable;

    public BaseObservableUseCase() {
        this.compositeDisposable = new CompositeDisposable();
    }

    /**
     * Create {@link Observable} source which will be execute.
     */
    public abstract Observable<T> createObservable(RequestParams params);


    /**
     * Executes the current use case.
     *
     * @param obs  {@link DisposableObserver} which will be listening to the observable build
     *              by {@link #createObservable(RequestParams)} ()} method.
     *
     * @param params Parameters used to build/execute this use case.
     */
    public void execute(DisposableObserver<T> obs, RequestParams params){

        final DisposableObserver<T> observer = createObservable(params)
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
