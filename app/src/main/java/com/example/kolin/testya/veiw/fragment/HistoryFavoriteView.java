package com.example.kolin.testya.veiw.fragment;

import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.presenters.HistoryFavoritePresenter;

import java.util.List;

/**
 * Created by kolin on 13.04.2017.
 *
 * Interface for interaction between {@link HistoryFavoritePresenter} and
 * {@link HistoryFavoriteFragment}
 *
 */

public interface HistoryFavoriteView {

    void showLoadedData(List<InternalTranslation> data);
}
