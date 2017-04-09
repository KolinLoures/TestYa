package com.example.kolin.testya.veiw.presenters;

import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.util.Log;

import com.example.kolin.testya.data.entity.dictionary.Def;
import com.example.kolin.testya.domain.GetLanguages;
import com.example.kolin.testya.domain.GetTranslation;
import com.example.kolin.testya.domain.GetDictionary;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.fragment.TranslatorFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by kolin on 31.03.2017.
 */

public class TranslatorPresenter extends BaseFavoritePresenter<TranslatorFragment> {
    private static final String TAG = TranslatorPresenter.class.getSimpleName();

    private GetTranslation getTranslation;
    private GetDictionary getDictionary;
    private GetLanguages getLanguages;


    private InternalTranslation currentTranslation;
    private ArrayMap<String, String> languages;

    @Override
    public void attacheView(@NonNull TranslatorFragment view) {
        super.attacheView(view);

        languages = new ArrayMap<>();

        getTranslation = new GetTranslation();
        getDictionary = new GetDictionary();
        getLanguages = new GetLanguages(view.getContext());

        getLanguages.execute(new LanguageObserver(), GetLanguages.GetLanguageParams.getParamsObj(true));
    }

    @Override
    public void onNextAddingToDb() {
    }

    @Override
    public void onCompleteAddingToDb() {
    }

    public void addRemoveTranslationDb(boolean remove) {
        super.addRemoveFavoriteTranslation(currentTranslation, remove);
    }

    public void loadTranslation(String text, String langFrom, String langTo) {

        if (!isViewAttach()) {
            Log.e(TAG, "View is detached");
            return;
        }

        getAttachView().showTranslationCard(false);
        getAttachView().showDictionaryCard(false);

        getTranslation.clearDisposableObservers();

        if (!text.isEmpty() && !text.equals("")) {
            getTranslation.execute(new TranslatorObserver(),
                    GetTranslation.TranslationParams.getEntity(text, buildLangString(langFrom, langTo)));
        }
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

        getAttachView().showTranslationCard(true);

        getAttachView().showTranslationResult(translation);
    }

    private void showDictionaryResult(List<Def> defList) {

        if (!isViewAttach()) {
            Log.e(TAG, "View is detached");
            return;
        }
        getAttachView().showDictionaryCard(true);

        getAttachView().showDictionary(defList);
    }

    @Override
    public void detachView() {
        super.detachView();

        getTranslation.dispose();
        getLanguages.dispose();
        getDictionary.dispose();
    }

    public void clearDisposables() {
        getLanguages.clearDisposableObservers();
        getTranslation.clearDisposableObservers();
        getDictionary.clearDisposableObservers();
    }

    public boolean equalsTranslationToCurrent(InternalTranslation internalTranslation) {
        return currentTranslation != null && currentTranslation.equals(internalTranslation);
    }

    public List<String> getListLanguages(){

        List<String> values = new ArrayList<>(languages.values());

        return languages != null && !languages.isEmpty() ? values : null;
    }

    public String buildLangString(String langFrom, String langTo){
        List<String> values = new ArrayList<>(languages.values());
        String keyFrom = languages.keyAt(values.indexOf(langFrom));
        String keyTo = languages.keyAt(values.indexOf(langTo));
        if (keyFrom.trim().isEmpty()) {
            return keyTo;
        }
        else {
            return String.format("%s%s%s",
                    keyFrom,
                    "-",
                    keyTo);
        }
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
            if (defList != null && defList.size() != 0)
                showDictionaryResult(defList);
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.toString());
        }

        public void onComplete() {      }
    }

    private final class LanguageObserver extends DisposableObserver<ArrayMap<String, String>> {

        @Override
        public void onNext(ArrayMap<String, String> stringStringArrayMap) {
            languages.putAll(stringStringArrayMap);
        }

        @Override
        public void onError(Throwable e) {}

        @Override
        public void onComplete() {}
    }
}
