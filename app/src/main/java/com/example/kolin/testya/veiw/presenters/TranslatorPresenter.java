package com.example.kolin.testya.veiw.presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.util.Log;

import com.example.kolin.testya.data.entity.dictionary.Def;
import com.example.kolin.testya.domain.AddRemoveFavoriteTranslationDb;
import com.example.kolin.testya.domain.GetLanguages;
import com.example.kolin.testya.domain.GetTranslation;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.fragment.TranslatorFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by kolin on 31.03.2017.
 *
 * Presenter for {@link TranslatorFragment}
 */
public class TranslatorPresenter extends BaseFavoritePresenter<TranslatorFragment> {

    //TAG for logging
    private static final String TAG = TranslatorPresenter.class.getSimpleName();

    //Keys for bundle
    private static final String KEY_CURR_FROM = "current_lang_from";
    private static final String KEY_CURR_TO = "current_lang_to";
    private static final String KEY_CURR_TRANS = "current_translation";
    private static final String KEY_CURR_DIC = "current_dictionary";
    private static final String KEY_LANGUAGES = "languages";

    //Use cases
    private GetTranslation getTranslation;
    private GetLanguages getLanguages;

    //Current data
    private String currentLangFrom ;
    private String currentLangTo;
    private String currentText;
    private InternalTranslation currentTranslation;
    private List<Def> currentDefList;
    private ArrayMap<String, String> languages;

    @Inject
    TranslatorPresenter(AddRemoveFavoriteTranslationDb addRemoveTranslationDb,
                               GetTranslation getTranslation,
                               GetLanguages getLanguages) {
        super(addRemoveTranslationDb);

        this.getTranslation = getTranslation;
        this.getLanguages = getLanguages;
    }

    @Override
    public void attachView(@NonNull TranslatorFragment view) {
        super.attachView(view);

        this.languages = new ArrayMap<>();
        this.currentDefList = new ArrayList<>();
        this.currentText = "";
    }

    @Override
    public void onCompleteAddToFavoriteDb() {
        //stub
    }

    /**
     * Execute {@link AddRemoveFavoriteTranslationDb} use case.
     */
    public void addRemoveTranslationDb(boolean remove) {
        super.addRemoveFavoriteTranslation(currentTranslation.getId(), remove);
    }

    /**
     * Execute {@link GetLanguages} use case.
     */
    public void loadSupportLanguages() {
        getLanguages.execute(new LanguageObserver(),
                GetLanguages.GetLanguageParams.getParamsObj(Locale.getDefault().getLanguage()));
    }

    /**
     * Execute {@link GetTranslation} use case
     */
    public void loadTranslation(String text, String langFrom, String langTo, boolean loadFromNetworkOnly) {

        if (!isViewAttach()) {
            Log.e(TAG, "View is detached");
            return;
        }

//        //do not load the same
//        if (currentText.equals(text) && currentLangFrom.equals(langFrom) && currentLangTo.equals(langTo)) {
//            return;
//        }

        //hide loading progress view
        getView().showLoadingProgress(false);

        //hide unnecessary parts of layouts
        getView().showTranslationCard(false);
        getView().showDictionaryCard(false);
        getView().showDetermineLang(false);

        //clear observer (we do not need data from old observer)
        getTranslation.clearDisposableObservers();

        //clear current data
        clear();

        //build lang string
        String lang = buildLangString(langFrom, langTo);

        //check if text is not empty
        if (!text.isEmpty() && !text.equals("") && lang != null) {
            getView().showLoadingProgress(true);

            currentText = text;

            //execute getTranslation use case

            getTranslation.execute(new TranslatorObserver(), GetTranslation.TranslationParams.getParamsObj(text, lang));
        }
    }

    /**
     * Show translation result of {@link GetTranslation} use case
     */
    private void showTranslationResult(InternalTranslation translation) {

        //ste current data;
        currentTranslation = translation;
//        currentText = currentTranslation.getTextFrom();

        if (!isViewAttach()) {
            Log.e(TAG, "View is detached");
            return;
        }

        //check if lang from was determined
        if (getCodeLang(currentLangFrom) == null) {
            //retrieve determined lang from translation
            String determinedLang = translation.getLang().split("-")[0].toLowerCase();
            //show determined part of layout
            getView().showDetermineLang(true);

            //set to layout determined lang
            getView().setDetermineLanguage(getNameLang(determinedLang));
        }

        //show other parts of layouts
        getView().showTranslationCard(true);
        getView().showError(false);
        getView().showDictionaryCard(true);
        //show translation result
        getView().showDictionary(translation.getDef());
        getView().showTranslationResult(translation);
    }


    private void showDictionaryResult(List<Def> defList) {
        //check if list is empty
        if (defList != null && defList.size() != 0) {

            if (!isViewAttach()) {
                Log.e(TAG, "View is detached");
                return;
            }

            //set current data
            this.currentDefList.addAll(defList);

            //show necessary part of layout
            getView().showDictionaryCard(true);
            //show data
            getView().showDictionary(defList);
        }
    }

    @Override
    public void detachView() {
        super.detachView();

        languages.clear();
        languages = null;

        currentDefList.clear();
        currentDefList = null;

        getTranslation.dispose();
        getLanguages.dispose();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void restoreStateData(Bundle savedInstateState) {

        currentLangFrom = savedInstateState.getString(KEY_CURR_FROM);
        currentLangTo = savedInstateState.getString(KEY_CURR_TO);

        getView().setLanguagesToButtons(currentLangFrom, currentLangTo);

        Map<String, String> mapLang = (Map<String, String>) savedInstateState.getSerializable(KEY_LANGUAGES);
        if (mapLang != null)
            languages.putAll(mapLang);

        InternalTranslation translation = savedInstateState.getParcelable(KEY_CURR_TRANS);
        if (translation != null) {
            currentText = translation.getTextFrom();
            showTranslationResult(translation);
        }


        ArrayList<Def> def = savedInstateState.getParcelableArrayList(KEY_CURR_DIC);
        if (def != null)
            showDictionaryResult(def);
    }

    @Override
    public void prepareForChangeState(Bundle outSate) {
//        outSate.putString(KEY_CURR_FROM, currentLangFrom);
//        outSate.putString(KEY_CURR_TO, currentLangTo);
//        outSate.putParcelable(KEY_CURR_TRANS, currentTranslation);
//        outSate.putParcelableArrayList(KEY_CURR_DIC, new ArrayList<Parcelable>(currentDefList));
//        outSate.putSerializable(KEY_LANGUAGES, new HashMap<>(languages));
    }

    /**
     * Method clear current data
     */
    public void clear(){
        currentText = "";
        currentTranslation = null;
        currentDefList.clear();
    }

    /**
     * Method clears all use cases
     */
    public void clearDisposables() {
        getLanguages.clearDisposableObservers();
        getTranslation.clearDisposableObservers();
    }

    /**
     * Method check translation equals to current translation
     *
     * @param internalTranslation translation to check
     */
    public boolean equalsTranslationToCurrent(InternalTranslation internalTranslation) {
        return currentTranslation != null && currentTranslation.equals(internalTranslation);
    }

    /**
     * Get current languages list
     * */
    public List<String> getListLanguages() {

        List<String> values = new ArrayList<>(languages.keySet());

        return languages != null && !languages.isEmpty() ? values : null;
    }

    /**
     * Method to build lang parameter for {@link GetTranslation} use case
     */
    private String buildLangString(String langFrom, String langTo) {
        if (langFrom.isEmpty() && langTo.isEmpty()) {
            Log.e(TAG, "buildLangString - empty languages");
            return null;
        }

        currentLangFrom = langFrom;
        currentLangTo = langTo;
        if (getCodeLang(langFrom) == null)
            return getCodeLang(langTo);
        else
            return String.format("%s%s%s", getCodeLang(langFrom), "-", getCodeLang(langTo));

    }

    /**
     * Get code of language
     *
     * @param langName name of language
     */
    public String getCodeLang(String langName) {
        return !languages.isEmpty() && langName != null
                ? languages.get(langName.toLowerCase())
                : null;
    }

    /**
     * Get name of language
     *
     * @param langCode code og language
     */
    public String getNameLang(String langCode) {
        return !languages.isEmpty() && langCode != null
                ? languages.keyAt(new ArrayList<>(languages.values()).indexOf(langCode))
                : null;
    }

    // Observer for GetTranslation use case
    private final class TranslatorObserver extends DisposableObserver<InternalTranslation> {
        @Override
        public void onNext(InternalTranslation translation) {
            showTranslationResult(translation);
        }

        @Override
        public void onError(Throwable e) {

            getView().showLoadingProgress(false);
            getView().showError(true);

            Log.e(TAG, "TranslatorObservable: ", e);
        }

        @Override
        public void onComplete() {
            getView().showLoadingProgress(false);
//            loadDictionaryResult();
        }
    }

    // Observer for GetDictionary use case
    private final class DictionaryObserver extends DisposableObserver<List<Def>> {

        @Override
        public void onNext(List<Def> defList) {
            currentDefList.clear();
            showDictionaryResult(defList);
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "DictionaryObservable: ", e);
        }

        public void onComplete() {
        }
    }



    //TODO: refactor LanguageObserver. Must set languages from SharedPreferences as last picked langs
    //Observer for GetLanguages use case
    private final class LanguageObserver extends DisposableObserver<ArrayMap<String, String>> {

        @Override
        public void onNext(ArrayMap<String, String> stringStringArrayMap) {
            languages.clear();
            languages.putAll(stringStringArrayMap);

            if (currentLangFrom == null && currentLangTo == null) {
                currentLangFrom = getNameLang("en");
                currentLangTo = getNameLang("ru");
            }

            getView().setLanguagesToButtons(currentLangFrom, currentLangTo);
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "LanguageObserver: ", e);
        }

        @Override
        public void onComplete() {
        }
    }
}
