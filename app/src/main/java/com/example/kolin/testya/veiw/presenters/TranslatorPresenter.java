package com.example.kolin.testya.veiw.presenters;

import android.support.annotation.NonNull;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.kolin.testya.data.models.dictionary.Def;
import com.example.kolin.testya.domain.GetTranslation;
import com.example.kolin.testya.data.models.Translation;
import com.example.kolin.testya.domain.GetTranslationOptions;
import com.example.kolin.testya.veiw.TranslatorFragment;

import java.util.List;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by kolin on 31.03.2017.
 */

public class TranslatorPresenter extends AbstractPresenter<TranslatorFragment>{
    private static final String TAG = TranslatorPresenter.class.getSimpleName();

    private GetTranslation getTranslation;
    private GetTranslationOptions getTranslationOptions;

    @Override
    public void attacheView(@NonNull TranslatorFragment view) {
        super.attacheView(view);

        getTranslation = new GetTranslation();
    }

    public void loadTranslation(String text, String lang){
        getTranslation.clearDisposableObservers();

        getTranslation.execute(new TranslatorObserver(),
                GetTranslation.TranslationParams.getEntity(text, lang));

    }

    private void showTranslationResult(String translation){

        if (!isViewAttach()){
            Log.e(TAG, "View is detached");
            return;
        }

        getAttachView().showTranslationResult(translation);
    }

    @Override
    public void detachView() {
        super.detachView();

        getTranslation.dispose();
    }

    private final class TranslatorObserver extends DisposableObserver<Translation>{
        @Override
        public void onNext(Translation translation) {
            showTranslationResult(translation.getText().toString());
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "TranslatorObservable - ", e);
        }

        @Override
        public void onComplete() {}
    }

    private final class DictionaryObsever extends DisposableObserver<List<Def>>{

        @Override
        public void onNext(List<Def> defList) {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }
}
