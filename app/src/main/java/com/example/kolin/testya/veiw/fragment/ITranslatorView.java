package com.example.kolin.testya.veiw.fragment;

import com.example.kolin.testya.data.entity.dictionary.Def;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.presenters.TranslatorPresenter;

import java.util.List;

/**
 * Created by kolin on 31.03.2017.
 *
 * Interface for interaction between {@link TranslatorPresenter} and
 * {@link TranslatorFragment}
 */

public interface ITranslatorView {

    void showTranslationResult(InternalTranslation translation);

    void showDictionary(List<Def> defList);

    void setDetermineLanguage(String langFrom);

    void notifyUser(String message);

    void showTranslationCard(boolean show);

    void showDictionaryCard(boolean show);

    void showLoadingProgress(boolean show);

    void showError(boolean show);

    void showDetermineLang(boolean show);

    void setLanguagesToButtons(String langFrom, String langTo);

}
