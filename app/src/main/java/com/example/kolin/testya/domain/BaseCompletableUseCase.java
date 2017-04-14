package com.example.kolin.testya.domain;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kolin on 14.04.2017.
 */

public abstract class BaseCompletableUseCase<RequestParams> {

    private CompositeDisposable compositeDisposable;

    public BaseCompletableUseCase() {
        this.compositeDisposable = new CompositeDisposable();
    }


    public abstract Completable createCompletable(RequestParams params);


    public void execute(DisposableCompletableObserver obs, RequestParams params){

        final DisposableCompletableObserver observer = createCompletable(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(obs);

        compositeDisposable.add(observer);
    }

    public void dispose(){
        if (!compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
    }

    public void clearDisposableObservers(){
        if (!compositeDisposable.isDisposed()){
            compositeDisposable.clear();
        }
    }
}
