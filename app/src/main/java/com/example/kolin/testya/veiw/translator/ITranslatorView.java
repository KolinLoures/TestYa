package com.example.kolin.testya.veiw.translator;

import com.example.kolin.testya.domain.model.InternalTranslation;

/**
 * Created by kolin on 31.03.2017.
 *
 * Interface for interaction between {@link TranslatorPresenter} and
 * {@link TranslatorFragment}
 */

public interface ITranslatorView {

    void showTranslationResult(InternalTranslation translation);

    void setLangFrom(String lang);

    void setLangTo(String lang);

    void setTranslatableText(String text, boolean blockTextWatcher);

    void notifyUser(String message);

    void showTranslationDictionaryCard(boolean show);

    void showTranslationCard(boolean show);

    void showDictionaryCard(boolean show);

    void showLoadingProgress(boolean show);

    void showError(boolean show);
}
