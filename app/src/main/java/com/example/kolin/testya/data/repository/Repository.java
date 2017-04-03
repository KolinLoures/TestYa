package com.example.kolin.testya.data.repository;

import com.example.kolin.testya.data.entity.Translation;
import com.example.kolin.testya.data.entity.dictionary.Def;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by kolin on 31.03.2017.
 */

public interface Repository {

    Observable<Translation> getPhraseTranslation(String text, String lang);

    Observable<List<Def>> getTranslationOptions(String text, String lang);
}
