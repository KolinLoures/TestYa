package com.example.kolin.testya.veiw.presenters;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * Created by kolin on 31.03.2017.
 *
 * Abstract Presenter class
 *
 * @param <V> view.
 */

public abstract class BasePresenter<V> {

    private WeakReference<V> weakReference = null;

    @CallSuper
    public void attachView(@NonNull V view){
        weakReference = new WeakReference<>(view);
    }

    @CallSuper
    public void detachView(){
        if (weakReference != null) {
            weakReference.clear();
            weakReference = null;
        }
    }

    public boolean isViewAttach(){
        return weakReference.get() != null && weakReference != null;
    }

    public V getView(){
        return isViewAttach() ? weakReference.get() : null;
    }

    public abstract void restoreStateData(Bundle savedInstateState);

    public abstract void prepareForChangeState(Bundle outSate);

}
