package com.example.kolin.testya.veiw.common;

import android.os.Bundle;
import android.util.Log;

import com.example.kolin.testya.data.TypeOfTranslation;
import com.example.kolin.testya.domain.AddRemoveFavoriteTranslationDb;
import com.example.kolin.testya.domain.DeleteHistoryEntityFromDB;
import com.example.kolin.testya.domain.DeleteTypeDb;
import com.example.kolin.testya.domain.GetHistoryFavoriteTranslationFromDb;
import com.example.kolin.testya.domain.model.HistoryFavoriteModel;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.presenters.BaseFavoritePresenter;
import com.example.kolin.testya.veiw.translator.TranslatorFragment;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by kolin on 11.08.2017.
 */

public class CommonPresenter extends BaseFavoritePresenter<CommonFragment> {

    private static final String TAG = CommonPresenter.class.getSimpleName();

    @TypeOfTranslation.TypeName
    private String currentType;

    private GetHistoryFavoriteTranslationFromDb favoriteTranslationFromDb;
    private DeleteHistoryEntityFromDB deleteHistoryEntityFromDB;
    private DeleteTypeDb deleteTypeDb;
    private AddRemoveFavoriteTranslationDb addRemoveFavoriteTranslationDb;

    @Inject
    public CommonPresenter(AddRemoveFavoriteTranslationDb addRemoveTranslationDb,
                           GetHistoryFavoriteTranslationFromDb favoriteTranslationFromDb,
                           DeleteHistoryEntityFromDB deleteHistoryEntityFromDB,
                           DeleteTypeDb deleteTypeDb,
                           AddRemoveFavoriteTranslationDb addRemoveFavoriteTranslationDb) {
        super(addRemoveTranslationDb);

        this.favoriteTranslationFromDb = favoriteTranslationFromDb;
        this.deleteHistoryEntityFromDB = deleteHistoryEntityFromDB;
        this.deleteTypeDb = deleteTypeDb;
        this.addRemoveFavoriteTranslationDb = addRemoveFavoriteTranslationDb;
    }

    public void loadHistoryFavoriteFromDb(@TypeOfTranslation.TypeName String type) {
        if (type != null) {
            this.currentType = type;

            favoriteTranslationFromDb.clearDisposableObservers();

            favoriteTranslationFromDb.execute(new GetHistoryFavorite(),
                    GetHistoryFavoriteTranslationFromDb.Params.getParamsObj(type));
        }
    }

    public void deleteAllTranslation(@TypeOfTranslation.TypeName String type){
        deleteTypeDb.execute(new DisposableCompletableObserver() {
            public void onComplete() { showSnackBar(); }
            public void onError(Throwable e) { e.printStackTrace(); }
        }, DeleteTypeDb.Params.getParamsObject(type));
    }

    private void showSnackBar() {
        if (getView() == null) {
            Log.e(TAG, "Presenter detached from view!");
            return;
        }

        getView().showSnackBar();
    }

    public void deleteHistoryFrom(int id){
        deleteHistoryEntityFromDB.execute(new DisposableCompletableObserver() {
            public void onComplete() {}
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        }, DeleteHistoryEntityFromDB.Params.getParamsObject(id));
    }

    public void showLoadedHistoryFavorites(List<HistoryFavoriteModel> data) {
        if (getView() == null) {
            Log.e(TAG, "Presenter detached from view!");
            return;
        }

        getView().showLoadedData(data);
    }

    public void addRemoveFromFavorite(int id, boolean check) {
        super.addRemoveFavoriteTranslation(id, check);
    }

    @Override
    public void onCompleteAddToFavoriteDb() {
        //stub
    }

    @Override
    public void detachView() {
        super.detachView();

        favoriteTranslationFromDb.dispose();
        deleteHistoryEntityFromDB.dispose();
        deleteTypeDb.dispose();
        addRemoveFavoriteTranslationDb.dispose();
    }

    @Override
    public void restoreStateData(Bundle savedInstateState) {
    }

    @Override
    public void prepareForChangeState(Bundle outSate) {
    }

    private final class GetHistoryFavorite extends DisposableObserver<List<HistoryFavoriteModel>> {

        @Override
        public void onNext(List<HistoryFavoriteModel> historyFavoriteModels) {
            showLoadedHistoryFavorites(historyFavoriteModels);
        }
        public void onError(Throwable e) {
            Log.e(TAG, "GetHistoryFavorite observer error", e);
        }
        public void onComplete() {}
    }
}
