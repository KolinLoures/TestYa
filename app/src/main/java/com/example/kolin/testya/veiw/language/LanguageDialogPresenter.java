package com.example.kolin.testya.veiw.language;

import android.os.Bundle;
import android.util.Log;

import com.example.kolin.testya.domain.GetLanguages;
import com.example.kolin.testya.domain.model.Language;
import com.example.kolin.testya.veiw.presenters.BasePresenter;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by kolin on 16.07.2017.
 */

public class LanguageDialogPresenter extends BasePresenter<LanguageDialogFragment> {

    private static final String TAG = LanguageDialogPresenter.class.getSimpleName();

    private GetLanguages getLanguagesUseCase;

    @Inject
    public LanguageDialogPresenter(GetLanguages getLanguagesUseCase) {
        this.getLanguagesUseCase = getLanguagesUseCase;
    }

    public void loadLanguages(){
        getLanguagesUseCase.clearDisposableObservers();

        getLanguagesUseCase.execute(new LanguageObserver(), null);
    }

    public void showLoadedLanguages(Language language){
        if (getView() == null) {
            Log.e(TAG, "Presenter detached from view!");
            return;
        }

        getView().showLoadedLanguage(language);
    }

    @Override
    public void restoreStateData(Bundle savedInstateState) {

    }

    @Override
    public void prepareForChangeState(Bundle outSate) {

    }

    public void disposeAll(){
        getLanguagesUseCase.dispose();
    }

    private final class LanguageObserver extends DisposableObserver<Language>{

        @Override
        public void onNext(Language language) {
            showLoadedLanguages(language);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }
}
