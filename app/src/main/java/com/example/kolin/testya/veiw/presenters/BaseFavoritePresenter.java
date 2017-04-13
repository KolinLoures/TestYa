package com.example.kolin.testya.veiw.presenters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.domain.AddRemoveTranslationDb;
import com.example.kolin.testya.domain.model.InternalTranslation;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by kolin on 03.04.2017.
 */

public abstract class BaseFavoritePresenter<V extends Fragment> extends BasePresenter<V> {

    private static final String TAG = BaseFavoritePresenter.class.getSimpleName();

    private AddRemoveTranslationDb addRemoveTranslationDb;

    @Override
    public void attacheView(@NonNull V view) {
        super.attacheView(view);

        addRemoveTranslationDb = new AddRemoveTranslationDb();
    }

    public abstract void onNextAddingToDb();

    public abstract void onCompleteAddingToDb();

    protected void addRemoveFavoriteTranslation(InternalTranslation translation, boolean remove){
        addRemoveTranslationDb.clearDisposableObservers();
        addRemoveTranslationDb.execute(
                new AddFavoriteDbObserver(),
                AddRemoveTranslationDb.AddTranslationParams.getParamsObj(translation,
                        TypeSaveTranslation.FAVORITE,
                        remove)
        );
    }

    @Override
    public void detachView() {
        super.detachView();
        addRemoveTranslationDb.dispose();
    }

    public final class AddFavoriteDbObserver extends DisposableObserver<Boolean>{

        @Override
        public void onNext(Boolean aBoolean) {
            if (aBoolean)
                onNextAddingToDb();
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "AddFavoriteDbObserver - " + e.toString());
        }

        @Override
        public void onComplete() {
            onCompleteAddingToDb();
        }
    }
}
