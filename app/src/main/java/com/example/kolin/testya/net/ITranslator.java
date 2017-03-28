package com.example.kolin.testya.net;

import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * RestApi for retrieve translation from Server
 */

public interface ITranslator {

    String API_KEY_TRNSL =
            "trnsl.1.1.20170328T192532Z.584959536bdd55f3.8847edda17b6557aede1111bb2f0cf2c8a42c1ed";
    String BASE_URL_TRNSL =
            "https://translate.yandex.net/api/v1.5/tr.json/translate";

    String API_KEY_DICT =
            "dict.1.1.20170328T203832Z.a4dc04d938fbd1fd.75d3dd48e9f7a9e49dc1b4059f6f05a742480058";
    String BASE_URL_DICT =
            "https://dictionary.yandex.net/api/v1/dicservice";




}
