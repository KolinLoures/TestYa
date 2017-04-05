package com.example.kolin.testya.veiw.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.kolin.testya.data.entity.dictionary.Def;
import com.example.kolin.testya.domain.GetTranslation;
import com.example.kolin.testya.domain.GetDictionary;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.fragment.TranslatorFragment;

import java.util.List;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by kolin on 31.03.2017.
 */

public class TranslatorPresenter extends BaseFavoritePresenter<TranslatorFragment> {
    private static final String TAG = TranslatorPresenter.class.getSimpleName();

    private GetTranslation getTranslation;
    private GetDictionary getDictionary;


    private InternalTranslation currentTranslation;

    @Override
    public void attacheView(@NonNull TranslatorFragment view) {
        super.attacheView(view);

        getTranslation = new GetTranslation();
        getDictionary = new GetDictionary();
    }

    @Override
    public void onNextAddingToDb() {

    }

    @Override
    public void onCompleteAddingToDb() {

    }

    public void addRemoveTranslationDb(boolean remove){
        super.addRemoveFavoriteTranslation(currentTranslation, remove);
    }

    public void loadTranslation(String text, String lang) {

        getTranslation.clearDisposableObservers();

        getTranslation.execute(new TranslatorObserver(),
                GetTranslation.TranslationParams.getEntity(text, lang));

    }

    public void loadDictionaryResult() {
        getDictionary.clearDisposableObservers();

        getDictionary.execute(new DictionaryObserver(),
                GetDictionary.DictionaryParams.getEntity(
                        currentTranslation.getTextFrom(),
                        currentTranslation.getLang()));
    }

    private void showTranslationResult(InternalTranslation translation) {

        currentTranslation = translation;

        if (!isViewAttach()) {
            Log.e(TAG, "View is detached");
            return;
        }

        getAttachView().showTranslationResult(translation);
    }

    private void showDictionaryResult(List<Def> defList) {

        if (!isViewAttach()) {
            Log.e(TAG, "View is detached");
            return;
        }

        getAttachView().showTranslationOptions(defList);
    }

    @Override
    public void detachView() {
        super.detachView();

        getTranslation.dispose();
    }

    private final class TranslatorObserver extends DisposableObserver<InternalTranslation> {
        @Override
        public void onNext(InternalTranslation translation) {
            showTranslationResult(translation);
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "TranslatorObservable: ", e);
        }

        @Override
        public void onComplete() {
            loadDictionaryResult();
        }
    }

    private final class DictionaryObserver extends DisposableObserver<List<Def>> {

        @Override
        public void onNext(List<Def> defList) {
            if (defList != null)
                showDictionaryResult(defList);
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.toString());
        }

        @Override
        public void onComplete() {}
    }
}
