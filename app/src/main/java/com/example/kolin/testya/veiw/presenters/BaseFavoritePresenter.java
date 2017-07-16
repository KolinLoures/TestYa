package com.example.kolin.testya.veiw.presenters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.kolin.testya.domain.AddRemoveFavoriteTranslationDb;

import io.reactivex.observers.DisposableCompletableObserver;

/**
 * Created by kolin on 03.04.2017.
 *
 * BaseFavoritePresenter implements {@link BasePresenter}.
 *
 * Encapsulates logic of adding or removing favorite translations to data base.
 */

public abstract class BaseFavoritePresenter<V extends Fragment> extends BasePresenter<V> {

    private static final String TAG = BaseFavoritePresenter.class.getSimpleName();

    //Use Case add/remove data base translation
    private AddRemoveFavoriteTranslationDb addRemoveTranslationDb;

    public BaseFavoritePresenter(AddRemoveFavoriteTranslationDb addRemoveTranslationDb) {
        this.addRemoveTranslationDb = addRemoveTranslationDb;
    }

    @Override
    public void attachView(@NonNull V view) {
        super.attachView(view);
    }

    //Abstract Method to notify that AddRemoveFavoriteTranslationDb complete
    public abstract void onCompleteAddToFavoriteDb();

    //execute use case
    protected void addRemoveFavoriteTranslation(int id, boolean remove) {
        addRemoveTranslationDb.clearDisposableObservers();
        addRemoveTranslationDb.execute(
                new AddFavoriteDbObserver(),
                AddRemoveFavoriteTranslationDb.Params.getParamsObject(id, remove)
        );
    }

    @Override
    public void detachView() {
        super.detachView();
        addRemoveTranslationDb.dispose();
    }

    //Observer for AddRemoveFavoriteTranslationDb use case
    public final class AddFavoriteDbObserver extends DisposableCompletableObserver {

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "AddFavoriteDbObserver: ", e);
        }

        @Override
        public void onComplete() {
            onCompleteAddToFavoriteDb();
        }
    }
}
