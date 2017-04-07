package com.example.kolin.testya.veiw.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.domain.GetTranslationsDb;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.CommonFragment;
import com.example.kolin.testya.veiw.HistoryFavoriteFragment;
import com.example.kolin.testya.veiw.adapter.HistoryFavoriteFilter;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by kolin on 06.04.2017.
 */

public class CommonPresenter extends BaseFavoritePresenter<HistoryFavoriteFragment> {

    private static final String TAG = CommonPresenter.class.getSimpleName();

    private GetTranslationsDb getTranslationsDb;

    @Override
    public void attacheView(@NonNull HistoryFavoriteFragment view) {
        super.attacheView(view);

        getTranslationsDb = new GetTranslationsDb();
    }

    @Override
    public void onNextAddingToDb() {
    }

    @Override
    public void onCompleteAddingToDb() {
        loadTranslationDb();
    }

    public void addRemoveFavoriteTranslationDb(InternalTranslation translation, boolean remove){
        super.addRemoveFavoriteTranslation(translation, remove);
    }

    public void loadTranslationDb(){
        if (!isViewAttach()) {
            Log.e(TAG, "View is detached");
            return;
        }

        getAttachView().clearAdapter();


        getTranslationsDb.execute(new TranslationDbObserver(),
                GetTranslationsDb.GetTranslationsDbParams.getParamsObj(null));
    }

    public void showLoadedDbData(InternalTranslation translation){

        if (!isViewAttach()) {
            Log.e(TAG, "View is detached");
            return;
        }


        getAttachView().showLoadedData(translation);
    }

    @Override
    public void detachView() {
        super.detachView();

        getTranslationsDb.dispose();
    }

    public final class TranslationDbObserver extends DisposableObserver<InternalTranslation>{

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
