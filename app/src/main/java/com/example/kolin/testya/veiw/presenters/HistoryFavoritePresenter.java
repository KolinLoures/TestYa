package com.example.kolin.testya.veiw.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.domain.GetTranslationsDb;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.HistoryFavoriteFragment;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by kolin on 06.04.2017.
 */

public class HistoryFavoritePresenter extends BaseFavoritePresenter<HistoryFavoriteFragment> {

    private static final String TAG = HistoryFavoritePresenter.class.getSimpleName();

    private GetTranslationsDb getTranslationsDb;

    private boolean updateHistoryFragment = false;

    @Override
    public void attacheView(@NonNull HistoryFavoriteFragment view) {
        super.attacheView(view);

        getTranslationsDb = new GetTranslationsDb();
    }

    @Override
    public void onNextAddingToDb() {
        //stub
    }

    @Override
    public void onCompleteAddingToDb() {
        if (updateHistoryFragment)
            loadTranslationDb(null);
        else
            loadTranslationDb(TypeSaveTranslation.FAVORITE);
    }

    public void addRemoveFavoriteTranslationDb(InternalTranslation translation, boolean remove) {
        updateHistoryFragment = translation.getType().equals(TypeSaveTranslation.FAVORITE);

        super.addRemoveFavoriteTranslation(translation, remove);
    }

    public void loadTranslationDb(@TypeSaveTranslation.TypeName
                                          String type) {

        if (!isViewAttach()) {
            Log.e(TAG, "View is detached");
            return;
        }

        getAttachView().clearViewPagerFragment(type);

        getTranslationsDb.execute(new TranslationDbObserver(),
                GetTranslationsDb.GetTranslationsDbParams.getParamsObj(type));
    }

    public void showLoadedDbData(InternalTranslation translation) {

        if (!isViewAttach()) {
            Log.e(TAG, "View is detached");
            return;
        }


        getAttachView().updateLoadedData(translation);
    }

    @Override
    public void detachView() {
        super.detachView();

        getTranslationsDb.dispose();
    }

    public final class TranslationDbObserver extends DisposableObserver<InternalTranslation> {

        @Override
        public void onNext(InternalTranslation translation) {
            showLoadedDbData(translation);
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onComplete() {
        }
    }
}
