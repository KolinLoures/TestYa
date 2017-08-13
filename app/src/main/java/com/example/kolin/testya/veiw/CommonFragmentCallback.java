package com.example.kolin.testya.veiw;

import com.example.kolin.testya.domain.model.HistoryFavoriteModel;

/**
 * Created by kolin on 13.08.2017.
 */

public interface CommonFragmentCallback {

    void showTranslationInFavorite(HistoryFavoriteModel model);

    void removeTranslationInFavorite(HistoryFavoriteModel model);

    void updateTranslationInHistory(HistoryFavoriteModel model);

    void setVisibilityToDeleteButton(boolean visible);
}
