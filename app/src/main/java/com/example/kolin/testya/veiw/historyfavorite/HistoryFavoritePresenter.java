package com.example.kolin.testya.veiw.historyfavorite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.kolin.testya.data.TypeOfTranslation;
import com.example.kolin.testya.di.ActivityScope;
import com.example.kolin.testya.domain.AddRemoveFavoriteTranslationDb;
import com.example.kolin.testya.domain.DeleteTypeDb;
import com.example.kolin.testya.domain.GetHistoryFavoriteTranslationFromDb;
import com.example.kolin.testya.domain.model.HistoryFavoriteModel;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.presenters.BaseFavoritePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by kolin on 13.04.2017.
 *
 * Presenter for {@link HistoryFavoriteFragment}
 */
@ActivityScope
public class HistoryFavoritePresenter extends BaseFavoritePresenter<HistoryFavoriteFragment> {

    private static final String TAG = HistoryFavoritePresenter.class.getSimpleName();

    private GetHistoryFavoriteTranslationFromDb getHistoryFavoriteTranslationFromDb;

    @Inject
    HistoryFavoritePresenter(AddRemoveFavoriteTranslationDb addRemoveTranslationDb,
                             GetHistoryFavoriteTranslationFromDb getDbTranslations,
                             DeleteTypeDb deleteTypeDb) {
        super(addRemoveTranslationDb);

        this.getHistoryFavoriteTranslationFromDb = getDbTranslations;
    }

    public void loadData(){
        getHistoryFavoriteTranslationFromDb.execute(new DisposableObserver<List<HistoryFavoriteModel>>() {
            public void onNext(List<HistoryFavoriteModel> model) { showLoadedData(model, TypeOfTranslation.HISTORY);}
            public void onError(Throwable e) { e.printStackTrace(); }
            public void onComplete() { }
        }, GetHistoryFavoriteTranslationFromDb.Params.getParamsObj(TypeOfTranslation.HISTORY));
    }

    public void showLoadedData(List<HistoryFavoriteModel> data,
                               @TypeOfTranslation.TypeName String type){
        if (getView() == null) {
            Log.e(TAG, "Presenter detached from view!");
            return;
        }

            getView().showLoadedFavorites(data);
    }



    @Override
    public void onCompleteAddToFavoriteDb() {

    }

    @Override
    public void restoreStateData(Bundle savedInstateState) {

    }

    @Override
    public void prepareForChangeState(Bundle outSate) {

    }


}
