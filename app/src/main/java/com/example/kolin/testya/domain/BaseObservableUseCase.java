package com.example.kolin.testya.domain;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kolin on 31.03.2017.
 */

public abstract class BaseObservableUseCase<T, RequestParams> {

    private CompositeDisposable compositeDisposable;

    public BaseObservableUseCase() {
        this.compositeDisposable = new CompositeDisposable();
    }


    public abstract Observable<T> createObservable(RequestParams params);


    public void execute(DisposableObserver<T> obs, RequestParams params){

        final DisposableObserver<T> observer = createObservable(params)
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
