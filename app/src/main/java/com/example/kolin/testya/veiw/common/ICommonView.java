package com.example.kolin.testya.veiw.common;

import com.example.kolin.testya.domain.model.HistoryFavoriteModel;

import java.util.List;

/**
 * Created by kolin on 17.07.2017.
 */

public interface ICommonView {

    void showLoadedData(List<HistoryFavoriteModel> model);

    void deleteData();

}
