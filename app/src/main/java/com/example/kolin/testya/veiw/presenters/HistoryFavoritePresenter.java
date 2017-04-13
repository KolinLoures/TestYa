package com.example.kolin.testya.veiw.presenters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.domain.DeleteTypeDb;
import com.example.kolin.testya.domain.GetTranslationsDb;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.HistoryFavoriteFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by kolin on 06.04.2017.
 */

public class HistoryFavoritePresenter extends BaseFavoritePresenter<HistoryFavoriteFragment> {

    private static final String TAG = HistoryFavoritePresenter.class.getSimpleName();
    private static final String KEY_DATA = "current_data";

    private GetTranslationsDb getTranslationsDb;
    private DeleteTypeDb deleteTypeDb;

    private boolean updateHistoryFragment = false;

    private List<InternalTranslation> currentData;

    @Override
    public void attacheView(@NonNull HistoryFavoriteFragment view) {
        super.attacheView(view);

        getTranslationsDb = new GetTranslationsDb();
        deleteTypeDb = new DeleteTypeDb();

        currentData = new ArrayList<>();
    }

    @Override
    public void onNextAddingToDb() {
        //stub
    }

    @Override
    public void onCompleteAddingToDb() {
        updateDataFragments();
    }

    public void addRemoveFavoriteTranslationDb(InternalTranslation translation, boolean remove) {
        setUpdateHistoryFragment(translation.getType());

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

    public void deleteTranslationsByCategory(String type){
        setUpdateHistoryFragment(type);

        if (!isViewAttach()) {
            Log.e(TAG, "View is detached");
            return;
        }

        getAttachView().clearViewPagerFragment(type);

        deleteTypeDb.execute(new DeleteDbObserver(),
                DeleteTypeDb.DeleteRequestParams.getParamsObj(type));
    }

    public void showLoadedDbData(InternalTranslation translation) {

        if (!isViewAttach()) {
            Log.e(TAG, "View is detached");
            return;
        }

        currentData.add(translation);

        getAttachView().updateLoadedData(translation);
    }

    @Override
    public void detachView() {
        super.detachView();

        currentData.clear();
        currentData = null;

        getTranslationsDb.dispose();
    }

    @Override
    public void restoreStateData(Bundle savedInstateState) {
        ArrayList<InternalTranslation> data = savedInstateState.getParcelableArrayList(KEY_DATA);
        if (data != null){
            for (InternalTranslation it: data)
                showLoadedDbData(it);
        }
    }

    @Override
    public void prepareForChangeState(Bundle outSate) {
        outSate.putParcelableArrayList(KEY_DATA, new ArrayList<Parcelable>(currentData));
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

    public final class DeleteDbObserver extends DisposableObserver<Boolean> {

        @Override
        public void onNext(Boolean bool) {
            if (bool)
                getAttachView().notifyUser("Переводов больше нет!");
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {
            updateDataFragments();
        }
    }

    private void setUpdateHistoryFragment(String type){
        updateHistoryFragment = type.equals(TypeSaveTranslation.FAVORITE);
    }

    private void updateDataFragments(){
        if (updateHistoryFragment)
            loadTranslationDb(null);
        else
            loadTranslationDb(TypeSaveTranslation.FAVORITE);
    }
}
