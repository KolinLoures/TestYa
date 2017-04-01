package com.example.kolin.testya.veiw.presenters;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * Created by kolin on 31.03.2017.
 */

public class AbstractPresenter<V> {

    private WeakReference<V> viewWeakReference = null;

    @CallSuper
    public void attacheView(@NonNull V view){
        this.viewWeakReference = new WeakReference<>(view);
    }

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
}
