package com.example.kolin.testya.veiw.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.kolin.testya.data.models.dictionary.Def;
import com.example.kolin.testya.domain.GetTranslation;
import com.example.kolin.testya.data.models.Translation;
import com.example.kolin.testya.domain.GetTranslationOptions;
import com.example.kolin.testya.veiw.fragment.TranslatorFragment;

import java.util.List;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by kolin on 31.03.2017.
 */

public class TranslatorPresenter extends AbstractPresenter<TranslatorFragment> {
    private static final String TAG = TranslatorPresenter.class.getSimpleName();

    private GetTranslation getTranslation;
    private GetTranslationOptions getTranslationOptions;


    private String currentText;
    private String currnetLang;

    @Override
    public void attacheView(@NonNull TranslatorFragment view) {
        super.attacheView(view);

        getTranslation = new GetTranslation();
        getTranslationOptions = new GetTranslationOptions();
    }

    public void loadTranslation(String text, String lang) {

        currentText = text;
        currnetLang = lang;

        getTranslation.clearDisposableObservers();

        getTranslation.execute(new TranslatorObserver(),
                GetTranslation.TranslationParams.getEntity(text, lang));

    }

    public void loadTranslationOptions(String text, String lang) {
        getTranslationOptions.clearDisposableObservers();

        getTranslationOptions.execute(new DictionaryObserver(),
                GetTranslationOptions.DictionaryParams.getEntity(text, lang));
    }

    private void showTranslationResult(String translation) {

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

    private final class TranslatorObserver extends DisposableObserver<Translation> {
        @Override
        public void onNext(Translation translation) {
            showTranslationResult(translation.getText().toString());
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "TranslatorObservable: ", e);
        }

        @Override
        public void onComplete() {
            loadTranslationOptions(currentText, currnetLang);
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
