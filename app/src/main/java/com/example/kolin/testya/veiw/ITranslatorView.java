package com.example.kolin.testya.veiw;

import com.example.kolin.testya.data.entity.dictionary.Def;

import java.util.List;

/**
 * Created by kolin on 31.03.2017.
 */

public interface ITranslatorView {

    void showTranslationResult(String translation);

    void showTranslationOptions(List<Def> defList);
}
