package com.example.kolin.testya.domain;

import android.support.annotation.NonNull;

import com.example.kolin.testya.data.entity.Translation;
import com.example.kolin.testya.data.repository.Repository;
import com.example.kolin.testya.data.repository.RepositoryImpl;

import io.reactivex.Observable;

/**
 * Created by kolin on 31.03.2017.
 */

public class GetTranslation extends BaseUseCase<Translation, GetTranslation.TranslationParams> {

    private Repository repository;

    public GetTranslation() {
        this.repository = new RepositoryImpl();
    }

    @Override
    public Observable<Translation> createObservable(@NonNull TranslationParams translationParams) {
        return repository.getPhraseTranslation(
                translationParams.text,
                translationParams.lang
        );
    }


    public static final class TranslationParams {

        private final String text;
        private final String lang;

        public TranslationParams(String text, String lang) {
            this.text = text;
            this.lang = lang;
        }

        public static TranslationParams getEntity(String text, String lang) {
            return new TranslationParams(text, lang);
        }
    }

}
