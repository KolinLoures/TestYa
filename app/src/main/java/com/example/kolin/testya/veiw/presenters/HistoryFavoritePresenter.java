package com.example.kolin.testya.veiw.presenters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.di.ActivityScope;
import com.example.kolin.testya.domain.AddRemoveTranslationDb;
import com.example.kolin.testya.domain.DeleteTypeDb;
import com.example.kolin.testya.domain.GetDbTranslations;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.HistoryFavoriteFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by kolin on 13.04.2017.
 */
@ActivityScope
public class HistoryFavoritePresenter extends BaseFavoritePresenter<HistoryFavoriteFragment> {

    private static final String TAG = HistoryFavoritePresenter.class.getSimpleName();

    private static final String KEY_HISTORY = "history_data";
    private static final String KEY_FAVORITE = "favorite_data";

    private GetDbTranslations getDbTranslations;
    private DeleteTypeDb deleteTypeDb;


    private List<InternalTranslation> currentHistoryData;
    private List<InternalTranslation> currentFavoriteData;

    private int currentSpinnerPos;
    private String currentTypeLoad;

    @Inject
    HistoryFavoritePresenter(AddRemoveTranslationDb addRemoveTranslationDb,
                             GetDbTranslations getDbTranslations,
                             DeleteTypeDb deleteTypeDb) {
        super(addRemoveTranslationDb);

        this.getDbTranslations = getDbTranslations;
        this.deleteTypeDb = deleteTypeDb;
    }

    @Override
    public void attacheView(@NonNull HistoryFavoriteFragment view) {
        super.attacheView(view);


        currentHistoryData = new ArrayList<>();
        currentFavoriteData = new ArrayList<>();
    }

    public void loadTranslationFromDb(@TypeSaveTranslation.TypeName String type, int spinnerPos) {

        currentTypeLoad = type;
        currentSpinnerPos = spinnerPos;

        if (type == null) {
            currentFavoriteData.clear();
            currentHistoryData.clear();
        }


        getDbTranslations.clearDisposableObservers();

        getDbTranslations.execute(new TranslationDbObserver(),
                GetDbTranslations.GetTranslationsDbParams.getParamsObj(type));
    }

    public void deleteFromDb(int spinnerPos) {

        currentTypeLoad = null;
        currentSpinnerPos = spinnerPos;

        if (!isHistoryTypeSpinSelection(spinnerPos)) {
            for (InternalTranslation translation : currentHistoryData) {
                translation.setFavorite(false);
            }

            currentFavoriteData.clear();
        } else
            currentHistoryData.clear();

        deleteTypeDb.clearDisposableObservers();

        deleteTypeDb.execute(new DeleteObserver(),
                DeleteTypeDb.DeleteRequestParams.getParamsObj(TypeSaveTranslation.getTypeById(spinnerPos)));
    }

    public void addRemoveFavoriteDb(InternalTranslation internalTranslation, boolean check) {
        super.addRemoveFavoriteTranslation(internalTranslation, check);


        for (InternalTranslation translation : currentHistoryData) {
            if (translation.equals(internalTranslation)) {
                translation.setFavorite(!check);
            }
        }

        currentFavoriteData.clear();
        loadTranslationFromDb(TypeSaveTranslation.FAVORITE, currentSpinnerPos);
    }

    public void showLoadedDataForPosition(int posSpinner) {

        if (posSpinner == -1)
            return;

        currentSpinnerPos = posSpinner;

        if (currentTypeLoad == null) {
            showLoadedCurrentData();
            return;
        }

        if (!isHistoryTypeSpinSelection(currentSpinnerPos) && currentTypeLoad.equals(TypeSaveTranslation.FAVORITE))
            getAttachView().showLoadedData(currentFavoriteData);
        else
            showLoadedCurrentData();
    }

    public void showLoadedCurrentData() {

        if (!isViewAttach()) {
            Log.e(TAG, "View is detached");
            return;
        }

        if (isHistoryTypeSpinSelection(currentSpinnerPos))
            getAttachView().showLoadedData(currentHistoryData);
        else
            getAttachView().showLoadedData(currentFavoriteData);
    }

    private boolean isHistoryTypeSpinSelection(int posSpinner) {
        return TypeSaveTranslation.getTypeById(posSpinner).equals(TypeSaveTranslation.HISTORY);
    }

    private void divideToCategories(List<InternalTranslation> data) {

        for (InternalTranslation t : data)
            if (t.getType().equals(TypeSaveTranslation.HISTORY))
                currentHistoryData.add(t);
            else
                currentFavoriteData.add(t);
    }

    @Override
    public void detachView() {
        super.detachView();

        getDbTranslations.dispose();
        deleteTypeDb.dispose();

        currentHistoryData.clear();
        currentHistoryData = null;

        currentFavoriteData.clear();
        currentFavoriteData = null;
    }

    @Override
    public void restoreStateData(Bundle savedInstateState) {
        ArrayList<InternalTranslation> dataHistory = savedInstateState.getParcelableArrayList(KEY_HISTORY);
        if (dataHistory != null)
            currentHistoryData.addAll(dataHistory);

        ArrayList<InternalTranslation> dataFavorite = savedInstateState.getParcelableArrayList(KEY_FAVORITE);
        if (dataFavorite != null)
            currentFavoriteData.addAll(dataFavorite);
    }

    @Override
    public void prepareForChangeState(Bundle outSate) {
        outSate.putParcelableArrayList(KEY_HISTORY, new ArrayList<Parcelable>(currentHistoryData));
        outSate.putParcelableArrayList(KEY_FAVORITE, new ArrayList<Parcelable>(currentFavoriteData));
    }

    @Override
    public void onCompleteAddToFavoriteDb() {
        //stub
    }

    private final class TranslationDbObserver extends DisposableObserver<List<InternalTranslation>> {

        @Override
        public void onNext(List<InternalTranslation> translations) {
            divideToCategories(translations);
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "DeleteObserver: ", e);
        }

        @Override
        public void onComplete() {
            showLoadedDataForPosition(currentSpinnerPos);
        }
    }

    private final class DeleteObserver extends DisposableCompletableObserver {

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "DeleteObserver: ", e);
        }

        @Override
        public void onComplete() {
            showLoadedCurrentData();
        }
    }
}
