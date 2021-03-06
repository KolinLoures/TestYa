package com.example.kolin.testya.data.net;

import com.example.kolin.testya.data.entity.Translation;
import com.example.kolin.testya.data.entity.dictionary.Dictionary;
import com.example.kolin.testya.domain.model.InternalTranslation;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * RestApi for retrieve translation and dictionary from Server
 */

public interface NetTranslator {

    String API_KEY_TRNSL =
            "trnsl.1.1.20170328T192532Z.584959536bdd55f3.8847edda17b6557aede1111bb2f0cf2c8a42c1ed";
    String BASE_URL_TRNSL =
            "https://translate.yandex.net/api/v1.5/tr.json/";

    String API_KEY_DICT =
            "dict.1.1.20170328T203832Z.a4dc04d938fbd1fd.75d3dd48e9f7a9e49dc1b4059f6f05a742480058";
    String BASE_URL_DICT =
            "https://dictionary.yandex.net/api/v1/dicservice.json/lookup";


    /**
     * POST Method to get translation from yandex.
     *
     * @param key api key
     * @param text text to translate
     * @param lang direction of translation. Example "ru-en"
     * @return Observable that emits {@link Translation} object.
     */
    @POST("translate")
    Observable<Translation> getTranslation(@Query("key") String key,
                                           @Query(value = "text", encoded = true) String text,
                                           @Query("lang") String lang);


    /**
     * POST Method to get dictionary option from yandex.
     *
     * @param key api key
     * @param text text for dictionary
     * @param lang direction of language. Example "ru-en"
     * @param ui The language of the user interface on which the names
     *           of parts of speech in the dictionary will be displayed.
     * @return  Observable that emits {@link Dictionary} object.
     */
    @POST(NetTranslator.BASE_URL_DICT)
    Observable<Dictionary> getTranslationOptions(@Query("key") String key,
                                                 @Query(value = "text", encoded = true) String text,
                                                 @Query("lang") String lang,
                                                 @Query("ui") String ui);
}
