package com.example.kolin.testya.veiw.translator;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.util.Log;

import com.example.kolin.testya.data.preferences.LanguagePreferencesManager;
import com.example.kolin.testya.domain.AddRemoveFavoriteTranslationDb;
import com.example.kolin.testya.domain.GetLanguages;
import com.example.kolin.testya.domain.GetTranslation;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.domain.model.Language;
import com.example.kolin.testya.veiw.presenters.BaseFavoritePresenter;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by kolin on 31.03.2017.
 * <p>
 * Presenter for {@link TranslatorFragment}
 */
public class TranslatorPresenter extends BaseFavoritePresenter<TranslatorFragment> {

    //TAG for logging
    private static final String TAG = TranslatorPresenter.class.getSimpleName();

    //Use cases
    private GetTranslation getTranslationUseCase;
    private GetLanguages getLanguagesUseCase;
    private LanguagePreferencesManager prefManager;

    private Language langFrom;
    private Language langTo;

    private InternalTranslation currentTranslation;


    @Inject
    TranslatorPresenter(AddRemoveFavoriteTranslationDb addRemoveTranslationDb,
                        GetTranslation getTranslationUseCase,
                        GetLanguages getLanguagesUseCase,
                        LanguagePreferencesManager prefManager) {
        super(addRemoveTranslationDb);

        this.getLanguagesUseCase = getLanguagesUseCase;
        this.getTranslationUseCase = getTranslationUseCase;
        this.prefManager = prefManager;
    }

    public void loadLangFromPreferences() {
        Pair<String, String> langFromPreferences = prefManager.getLangFromPreferences();

        getLanguagesUseCase.execute(new DisposableObserver<Language>() {
            public void onNext(Language language) {
                setLangBtnFrom(language);
            }

            public void onError(Throwable e) {
                e.printStackTrace();
            }

            public void onComplete() {
            }
        }, GetLanguages.Params.getParamsObj(langFromPreferences.first));

        getLanguagesUseCase.execute(new DisposableObserver<Language>() {
            public void onNext(Language language) {
                setLangBtnTo(language);
            }

            public void onError(Throwable e) {
                e.printStackTrace();
            }

            public void onComplete() {
            }
        }, GetLanguages.Params.getParamsObj(langFromPreferences.second));
    }

    public void saveLangToPreferences() {
        prefManager.saveLangsPreferences(langFrom.getCode(), langTo.getCode());
    }

    public void setLangBtnFrom(Language lang) {
        if (getView() == null) {
            Log.e(TAG, "Presenter detached from view!");
            return;
        }

        Language old = langFrom;

        this.langFrom = lang;

        getView().setLangFrom(lang.getDef());

        if (lang.equals(langTo))
            setLangBtnTo(old);
    }

    public void setLangBtnTo(Language lang) {
        if (getView() == null) {
            Log.e(TAG, "Presenter detached from view!");
            return;
        }

        Language old = langTo;

        this.langTo = lang;

        getView().setLangTo(lang.getDef());

        if (lang.equals(langFrom))
            setLangBtnFrom(old);
    }

    public void reverseLanguages() {
        setLangBtnFrom(langTo);

        if (getView() == null) {
            Log.e(TAG, "Presenter detached from view!");
            return;
        }

        if (currentTranslation != null)
            getView().setTranslatableText(currentTranslation.getTextTo());
    }

    public void loadTranslation(String text) {

        currentTranslation = null;
        getTranslationUseCase.clearDisposableObservers();

        showLoading(true);
        showDictionaryTranslationCard(false);

        getTranslationUseCase.execute(
                new GetTranslationObserver(),
                GetTranslation.TranslationParams
                        .getParamsObj(text, generateLangString(
                                this.langFrom.getCode(),
                                this.langTo.getCode())));
    }

    public void addFavorite(boolean check) {
        super.addRemoveFavoriteTranslation(currentTranslation.getId(), check);
    }

    private void showLoading(boolean show) {
        if (getView() == null) {
            Log.e(TAG, "Presenter detached from view!");
            return;
        }

        getView().showLoadingProgress(show);
    }

    private void showDictionaryTranslationCard(boolean show) {
        if (getView() == null) {
            Log.e(TAG, "Presenter detached from view!");
            return;
        }

        getView().showTranslationDictionaryCard(show);
    }

    public void showTranslation(InternalTranslation translation) {
        if (getView() == null) {
            Log.e(TAG, "Presenter detached from view!");
            return;
        }

        currentTranslation = translation;
        getView().showTranslationResult(translation);
    }

    private String generateLangString(String langFrom, String langTo) {
        return langFrom + "-" + langTo;
    }

    @Override
    public void onCompleteAddToFavoriteDb() {
        //stub
    }

    @Override
    public void restoreStateData(Bundle savedInstateState) {

    }

    @Override
    public void prepareForChangeState(Bundle outSate) {

    }

    private final class GetTranslationObserver extends DisposableObserver<InternalTranslation> {

        @Override
        public void onNext(InternalTranslation translation) {
            showTranslation(translation);
        }

        @Override
        public void onError(Throwable e) {
            showLoading(false);
        }

        @Override
        public void onComplete() {
            showLoading(false);
        }
    }
}
