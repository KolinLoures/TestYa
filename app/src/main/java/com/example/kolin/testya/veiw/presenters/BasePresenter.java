package com.example.kolin.testya.veiw.presenters;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * Created by kolin on 31.03.2017.
 */

public abstract class BasePresenter<V> {

    private WeakReference<V> viewWeakReference = null;

    @CallSuper
    public void attacheView(@NonNull V view){
        this.viewWeakReference = new WeakReference<>(view);
    }

    @CallSuper
    public void detachView(){
        if (viewWeakReference != null){
            viewWeakReference.clear();
            viewWeakReference = null;
        }
    }

    protected boolean isViewAttach(){
        return viewWeakReference.get() != null && viewWeakReference != null;
    }

    protected V getAttachView(){
        return isViewAttach() ? viewWeakReference.get() : null;
    }

    public abstract void restoreStateData(Bundle savedInstateState);

    public abstract void prepareForChangeState(Bundle outSate);

}
