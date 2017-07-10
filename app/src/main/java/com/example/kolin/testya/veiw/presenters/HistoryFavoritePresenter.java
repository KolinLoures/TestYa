package com.example.kolin.testya.veiw.presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.kolin.testya.data.TypeOfTranslation;
import com.example.kolin.testya.di.ActivityScope;
import com.example.kolin.testya.domain.AddRemoveFavoriteTranslationDb;
import com.example.kolin.testya.domain.DeleteTypeDb;
import com.example.kolin.testya.domain.GetHistoryFavoriteTranslationFromDb;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.fragment.HistoryFavoriteFragment;

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

    //TAG for logging
    private static final String TAG = HistoryFavoritePresenter.class.getSimpleName();

    //Key for bundles
    private static final String KEY_HISTORY = "history_data";
    private static final String KEY_FAVORITE = "favorite_data";

    //Use cases
    private GetHistoryFavoriteTranslationFromDb getDbTranslations;
    private DeleteTypeDb deleteTypeDb;

    //Current data history (data for spinner position 0)
    private List<InternalTranslation> currentHistoryData;
    //Current data favorite (data for spinner position 1)
    private List<InternalTranslation> currentFavoriteData;

    //Current spinner position
    private int currentSpinnerPos;
    //Current type loading
    private String currentTypeLoad;

    @Inject
    HistoryFavoritePresenter(AddRemoveFavoriteTranslationDb addRemoveTranslationDb,
                             GetHistoryFavoriteTranslationFromDb getDbTranslations,
                             DeleteTypeDb deleteTypeDb) {
        super(addRemoveTranslationDb);

        this.getDbTranslations = getDbTranslations;
        this.deleteTypeDb = deleteTypeDb;
    }

    @Override
    public void attachView(@NonNull HistoryFavoriteFragment view) {
        super.attachView(view);

        //initialize lists of current data
        currentHistoryData = new ArrayList<>();
        currentFavoriteData = new ArrayList<>();
    }

    /**
     * Executing {@link GetHistoryFavoriteTranslationFromDb} use case.
     *
     * @param spinnerPos current spinner position
     */
    public void loadTranslationFromDb(@TypeOfTranslation.TypeName String type, int spinnerPos) {

        //Setting current type and spinner position
        currentTypeLoad = type;
        currentSpinnerPos = spinnerPos;

        //if we load all data from db we need to clear both lists
        if (type == null) {
            currentFavoriteData.clear();
            currentHistoryData.clear();
        }


        getDbTranslations.clearDisposableObservers();

        //execute use case
//        getDbTranslations.execute(new TranslationDbObserver(),
//                GetHistoryFavoriteTranslationFromDb.Params.getParamsObj(type));
    }

    /**
     * Executing {@link DeleteTypeDb} use case.
     *
     * @param spinnerPos current spinner position
     */
    public void deleteFromDb(int spinnerPos) {

        //set current data
        currentTypeLoad = null;
        currentSpinnerPos = spinnerPos;

        //update data in history list if it necessary
        if (!isHistoryTypeSpinSelection(spinnerPos)) {
            for (InternalTranslation translation : currentHistoryData) {
                translation.setFavorite(false);
            }


            currentFavoriteData.clear();
        } else
            currentHistoryData.clear();

        deleteTypeDb.clearDisposableObservers();

        //execute delete use case
        deleteTypeDb.execute(new DeleteObserver(),
                DeleteTypeDb.Params.getParamsObject(TypeOfTranslation.getTypeById(spinnerPos)));
    }

    /**
     * Executing {@link AddRemoveFavoriteTranslationDb} use case.
     */
    public void addRemoveFavoriteDb(InternalTranslation internalTranslation, boolean check) {
        super.addRemoveFavoriteTranslation(internalTranslation.getId(), check);

        //update data in history list
        for (InternalTranslation translation : currentHistoryData) {
            if (translation.equals(internalTranslation)) {
                translation.setFavorite(!check);
            }
        }

        currentFavoriteData.clear();
        loadTranslationFromDb(TypeOfTranslation.FAVORITE, currentSpinnerPos);
    }

    /**
     * Show loaded data in according with spinner position
     *
     * @param posSpinner spinner position
     */
    public void showLoadedDataForPosition(int posSpinner) {

        if (!isViewAttach()) {
            Log.e(TAG, "View is detached");
            return;
        }

        if (posSpinner == -1)
            return;

        //set current spinner position
        currentSpinnerPos = posSpinner;

        //show if current type NULL
        if (currentTypeLoad == null) {
            showLoadedCurrentData();
            return;
        }

        //show favorite data
        //this condition allow update only favorite part, because we do not need to update all time
        //history data (main case to update history data is delete all from history)
        if (!isHistoryTypeSpinSelection(currentSpinnerPos) && currentTypeLoad.equals(TypeOfTranslation.FAVORITE))
            getView().showLoadedData(currentFavoriteData);
        else
            showLoadedCurrentData();
    }

    /**
     * Show loaded data if we do not need spinner position
     */
    public void showLoadedCurrentData() {

        if (!isViewAttach()) {
            Log.e(TAG, "View is detached");
            return;
        }
        if (isHistoryTypeSpinSelection(currentSpinnerPos))
            getView().showLoadedData(currentHistoryData);
        else
            getView().showLoadedData(currentFavoriteData);
    }

    /**
     * Check if spinner position accord {@link TypeOfTranslation#HISTORY} type.
     *
     * @param posSpinner spinner position
     * @return is history section
     */
    private boolean isHistoryTypeSpinSelection(int posSpinner) {
        return TypeOfTranslation.getTypeById(posSpinner).equals(TypeOfTranslation.HISTORY);
    }

    /**
     * Divide loaded data into two categories
     *
     * @param data loaded data
     */
    private void divideToCategories(List<InternalTranslation> data) {

//        for (InternalTranslation t : data)
//            //check type of object
//            if (t.getIsFavorite().equals(TypeOfTranslation.HISTORY))
//                currentHistoryData.add(t);
//            else
//                currentFavoriteData.add(t);
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
//        ArrayList<InternalTranslation> dataHistory = savedInstateState.getParcelableArrayList(KEY_HISTORY);
//        if (dataHistory != null)
//            currentHistoryData.addAll(dataHistory);
//
//        ArrayList<InternalTranslation> dataFavorite = savedInstateState.getParcelableArrayList(KEY_FAVORITE);
//        if (dataFavorite != null)
//            currentFavoriteData.addAll(dataFavorite);
    }

    @Override
    public void prepareForChangeState(Bundle outSate) {
//        outSate.putParcelableArrayList(KEY_HISTORY, new ArrayList<Parcelable>(currentHistoryData));
//        outSate.putParcelableArrayList(KEY_FAVORITE, new ArrayList<Parcelable>(currentFavoriteData));
    }

    @Override
    public void onCompleteAddToFavoriteDb() {
        //stub
    }

    //Observer for GetHistoryFavoriteTranslationFromDb use case
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

    //Observer for DeleteTypeDb use case
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
