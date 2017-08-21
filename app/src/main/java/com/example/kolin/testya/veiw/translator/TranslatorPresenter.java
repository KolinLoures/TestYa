package com.example.kolin.testya.veiw.translator;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.util.Pair;
import android.util.Log;

import com.example.kolin.testya.data.preferences.LanguagePreferencesManager;
import com.example.kolin.testya.domain.AddRemoveFavoriteTranslationDb;
import com.example.kolin.testya.domain.CheckFavoriteIs;
import com.example.kolin.testya.domain.GetEntityFromDB;
import com.example.kolin.testya.domain.GetLanguages;
import com.example.kolin.testya.domain.GetTranslation;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.domain.model.Language;
import com.example.kolin.testya.veiw.presenters.BaseFavoritePresenter;

import java.net.SocketException;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by kolin on 31.03.2017.
 * <p>
 * Presenter for {@link TranslatorFragment}
 */
public class TranslatorPresenter extends BaseFavoritePresenter<TranslatorFragment> {

    //TAG for logging
    private static final String TAG = TranslatorPresenter.class.getSimpleName();

    private static final String KEY_STATE = "current_translation";

    //Use cases
    private GetTranslation getTranslationUseCase;
    private GetLanguages getLanguagesUseCase;
    private CheckFavoriteIs checkFavoriteIsUseCase;
    private GetEntityFromDB getEntityFromDB;
    private LanguagePreferencesManager prefManager;

    private Language langFrom;
    private Language langTo;

    private InternalTranslation currentTranslation;


    @Inject
    TranslatorPresenter(AddRemoveFavoriteTranslationDb addRemoveTranslationDb,
                        GetTranslation getTranslationUseCase,
                        GetLanguages getLanguagesUseCase,
                        CheckFavoriteIs checkFavoriteIsUseCase,
                        GetEntityFromDB getEntityFromDB,
                        LanguagePreferencesManager prefManager) {
        super(addRemoveTranslationDb);

        this.getLanguagesUseCase = getLanguagesUseCase;
        this.getTranslationUseCase = getTranslationUseCase;
        this.checkFavoriteIsUseCase = checkFavoriteIsUseCase;
        this.getEntityFromDB = getEntityFromDB;
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
            setTranslatableText(currentTranslation.getTextTo(), false);
    }

    public void setTranslatableText(String text, boolean blockTextWatcher) {
        if (getView() == null) {
            Log.e(TAG, "Presenter detached from view!");
            return;
        }

        getView().setTranslatableText(text, blockTextWatcher);
    }

    public void loadTranslation(String text) {

        currentTranslation = null;
        getTranslationUseCase.clearDisposableObservers();

        showLoading(false);
        showError(false);
        showDictionaryTranslationCard(false);

        if (!text.isEmpty()) {
            showLoading(true);

            getTranslationUseCase.execute(
                    new GetTranslationObserver(),
                    GetTranslation.TranslationParams
                            .getParamsObj(text, generateLangString(
                                    this.langFrom.getCode(),
                                    this.langTo.getCode())));

        }

    }

    public void loadEntityById(int id){
        currentTranslation = null;
        getTranslationUseCase.clearDisposableObservers();

        showLoading(true);
        showError(false);
        showDictionaryTranslationCard(false);

        getEntityFromDB.execute(new DisposableObserver<InternalTranslation>() {
            public void onNext(InternalTranslation translation) {
                setTranslatableText(translation.getTextFrom(), true);
                showTranslation(translation);
            }
            public void onError(Throwable e) {
                showLoading(false);
                showError(true);
            }
            public void onComplete() {
                showLoading(false);
            }
        }, GetEntityFromDB.Params.getParamsObject(id));
    }

    public void addFavorite(boolean check) {
        super.addRemoveFavoriteTranslation(currentTranslation.getId(), check);
    }

    public void setFavoriteCheck(boolean check) {
        if (getView() == null) {
            Log.e(TAG, "Presenter detached from view!");
            return;
        }

        getView().setFavoriteCheckBox(check);
    }

    public void checkFavoriteIs() {
        if (currentTranslation != null)
            checkFavoriteIsUseCase.execute(new DisposableObserver<Boolean>() {
                public void onNext(Boolean aBoolean) {
                    currentTranslation.setFavorite(aBoolean);
                    setFavoriteCheck(aBoolean);
                }
                public void onError(Throwable e) {}
                public void onComplete() {}
            }, CheckFavoriteIs.Params.getParamsObj(currentTranslation.getId()));
    }

    private void showLoading(boolean show) {
        if (getView() == null) {
            Log.e(TAG, "Presenter detached from view!");
            return;
        }

        getView().showLoadingProgress(show);
    }

    private void showError(boolean show) {
        if (getView() == null) {
            Log.e(TAG, "Presenter detached from view!");
            return;
        }

        getView().showError(show);
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
        Parcelable parcelable = savedInstateState.getParcelable(KEY_STATE);
        if (parcelable != null) {
            InternalTranslation translation = (InternalTranslation) parcelable;
            setTranslatableText(translation.getTextFrom(), true);
            showTranslation(translation);
        }
    }

    @Override
    public void prepareForChangeState(Bundle outSate) {
        outSate.putParcelable(KEY_STATE, currentTranslation);
    }

    public void disposeAll() {
        checkFavoriteIsUseCase.dispose();
        getTranslationUseCase.dispose();
        getLanguagesUseCase.dispose();
    }

    private final class GetTranslationObserver extends DisposableObserver<InternalTranslation> {

        @Override
        public void onNext(InternalTranslation translation) {
            showTranslation(translation);
        }

        @Override
        public void onError(Throwable e) {
            showLoading(false);
            showError(true);
        }

        @Override
        public void onComplete() {
            showLoading(false);
        }
    }
}
