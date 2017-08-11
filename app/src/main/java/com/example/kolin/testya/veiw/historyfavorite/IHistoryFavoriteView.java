package com.example.kolin.testya.veiw.historyfavorite;

import com.example.kolin.testya.domain.model.HistoryFavoriteModel;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.historyfavorite.HistoryFavoriteFragment;
import com.example.kolin.testya.veiw.historyfavorite.HistoryFavoritePresenter;

import java.util.List;

/**
 * Created by kolin on 13.04.2017.
 *
 * Interface for interaction between {@link HistoryFavoritePresenter} and
 * {@link HistoryFavoriteFragment}
 *
 */

public interface IHistoryFavoriteView {

    void showLoadedFavorites(List<HistoryFavoriteModel> data);

}
