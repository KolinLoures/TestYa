package com.example.kolin.testya.veiw.presenters;

import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.util.Log;

import com.example.kolin.testya.data.entity.dictionary.Def;
import com.example.kolin.testya.domain.GetDictionary;
import com.example.kolin.testya.domain.GetLanguages;
import com.example.kolin.testya.domain.GetTranslation;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.fragment.TranslatorFragment;

import java.util.ArrayList;
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


    private String currentLangFrom;
    private String currentLangTo;
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
        getAttachView().showDetermineLang(false);


        if (!text.isEmpty() && !text.equals("")) {
            getTranslation.clearDisposableObservers();

            getAttachView().showLoadingProgress(true);

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


        if (currentLangFrom == null){

            getAttachView().showDetermineLang(true);

            String value = translation.getLang().split("-")[0].toLowerCase();
            currentLangFrom = getNameLang(value);
            getAttachView().setDetermineLanguage(currentLangFrom);
        }


        getAttachView().showTranslationCard(true);
        getAttachView().showError(false);

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

        languages.clear();
        languages = null;
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

    public List<String> getListLanguages() {

        List<String> values = new ArrayList<>(languages.keySet());

        return languages != null && !languages.isEmpty() ? values : null;
    }

    private String buildLangString(String langFrom, String langTo) {
        currentLangFrom = getCodeLang(langFrom);
        currentLangTo = getCodeLang(langTo);
        if (currentLangFrom == null)
            return currentLangTo;
        else
            return String.format("%s%s%s", currentLangFrom, "-", currentLangTo);

    }

    public String getCodeLang(String langName){
        return !languages.isEmpty()
                ? languages.get(langName.toLowerCase())
                : null;
    }

    public String getNameLang (String langCode){
        return !languages.isEmpty()
                ? languages.keyAt(new ArrayList<>(languages.values()).indexOf(langCode))
                : null;
    }

    private final class TranslatorObserver extends DisposableObserver<InternalTranslation> {
        @Override
        public void onNext(InternalTranslation translation) {
            showTranslationResult(translation);
        }

        @Override
        public void onError(Throwable e) {
            getAttachView().showLoadingProgress(false);
            getAttachView().showError(true);


            Log.e(TAG, "TranslatorObservable: ", e);
        }

        @Override
        public void onComplete() {
            getAttachView().showLoadingProgress(false);
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
            Log.e(TAG, "DictionaryObservable: ", e);
        }

        public void onComplete() {}
    }


    /**
    * TODO: refactor LanguageObserver. Must set languages from SharedPreferences as last picked langs
    */
    private final class LanguageObserver extends DisposableObserver<ArrayMap<String, String>> {

        @Override
        public void onNext(ArrayMap<String, String> stringStringArrayMap) {
            languages.putAll(stringStringArrayMap);
            getAttachView().setLanguagesToButtons(getNameLang("ru"), getNameLang("en"));
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "LanguageObserver: ", e);
        }

        @Override
        public void onComplete() {}
    }
}
