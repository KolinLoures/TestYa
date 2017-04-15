package com.example.kolin.testya.domain;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.data.db.IQueries;
import com.example.kolin.testya.data.entity.Translation;
import com.example.kolin.testya.data.net.NetTranslator;
import com.example.kolin.testya.domain.model.InternalTranslation;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by kolin on 31.03.2017.
 */

public class GetTranslation extends BaseObservableUseCase<InternalTranslation, GetTranslation.TranslationParams> {

    private static final int DELAY = 500;

    private NetTranslator netTranslator;
    private IQueries queries;

    @Inject
    GetTranslation(IQueries queries, NetTranslator netTranslator) {
        this.netTranslator = netTranslator;
        this.queries = queries;
    }

    @Override
    public Observable<InternalTranslation> createObservable(@NonNull final
                                                                TranslationParams translationParams) {
        return netTranslator
                .getTranslation(
                        NetTranslator.API_KEY_TRNSL,
                        translationParams.text,
                        translationParams.lang)
                .map(new Function<Translation, InternalTranslation>() {
                    @Override
                    public InternalTranslation apply(@io.reactivex.annotations.NonNull
                                                             Translation translation) throws Exception {
                        InternalTranslation temp = new InternalTranslation();
                        temp.setCode(translation.getCode());
                        temp.setFavorite(false);
                        temp.setLang(translation.getLang());
                        temp.setTextTo(TextUtils.join(" ", translation.getText()));
                        temp.setTextFrom(translationParams.text);
                        temp.setType(TypeSaveTranslation.HISTORY);
                        return temp;
                    }
                })
                .delay(DELAY, TimeUnit.MILLISECONDS)
                .doOnNext(new Consumer<InternalTranslation>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull
                                               InternalTranslation internalTranslation) throws Exception {
                        internalTranslation.setFavorite(
                                queries.isFavorite(
                                        internalTranslation
                                )
                        );

                        queries.addOrUpdateTranslation(internalTranslation, TypeSaveTranslation.HISTORY);
                    }
                });
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
