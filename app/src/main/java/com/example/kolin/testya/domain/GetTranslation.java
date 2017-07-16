package com.example.kolin.testya.domain;

import android.text.TextUtils;

import com.example.kolin.testya.data.db.TranslationDAO;
import com.example.kolin.testya.data.entity.Translation;
import com.example.kolin.testya.data.entity.dictionary.Dictionary;
import com.example.kolin.testya.data.net.NetTranslator;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;

/**
 * Created by kolin on 31.03.2017.
 * <p>
 * GetTranslation implementation of {@link BaseObservableUseCase}.
 * Use Case to get translation from net.
 */

public class GetTranslation extends BaseObservableUseCase<InternalTranslation, GetTranslation.TranslationParams> {

    // Delay for getting response from net and adding translation as history to data base.
    // Add this delay, cause I do not want to overload my DB with responses, when user typing word.
    private static final int DELAY = 500;

    private NetTranslator netTranslator;
    private TranslationDAO queries;
    private static final Gson gson = new Gson();

    @Inject
    GetTranslation(TranslationDAO queries, NetTranslator netTranslator) {
        this.netTranslator = netTranslator;
        this.queries = queries;
    }

    @Override
    public Observable<InternalTranslation> createObservable(final TranslationParams translationParams) {
        return Observable
                .zip(
                        netTranslator.getTranslation(
                                NetTranslator.API_KEY_TRNSL,
                                translationParams.text,
                                translationParams.lang)
                                .delay(DELAY, TimeUnit.MILLISECONDS),
                        netTranslator.getTranslationOptions(
                                NetTranslator.API_KEY_DICT,
                                translationParams.text,
                                translationParams.lang,
                                "en")
                                .delay(DELAY, TimeUnit.MILLISECONDS),
                        new BiFunction<Translation, Dictionary, InternalTranslation>() {
                            @Override
                            public InternalTranslation apply(
                                    @NonNull Translation translation,
                                    @NonNull Dictionary dictionary) throws Exception {
                                return saveTranslationToDB(
                                        translationParams.text,
                                        translation,
                                        dictionary
                                );
                            }
                        }
                );
    }

    private InternalTranslation saveTranslationToDB(String textFrom, Translation translation, Dictionary dictionary) {

        String jsonDictionary = "";
        String textTo = TextUtils.join(" ", translation.getText());

        if (dictionary != null)
            jsonDictionary = gson.toJson(dictionary, Dictionary.class);


        queries.addToTable(
                textFrom,
                textTo,
                translation.getLang(),
                jsonDictionary);

        return queries.getEntity(textFrom, textTo, translation.getLang());
    }

    /**
     * Parameters class
     */
    public static final class TranslationParams {

        private final String text;
        private final String lang;

        private TranslationParams(String text, String lang) {
            this.text = text;
            this.lang = lang;
        }

        public static TranslationParams getParamsObj(String text, String lang) {
            return new TranslationParams(text, lang);
        }
    }

}
