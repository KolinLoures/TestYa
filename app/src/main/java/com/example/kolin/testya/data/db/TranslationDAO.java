package com.example.kolin.testya.data.db;

import com.example.kolin.testya.domain.model.HistoryFavoriteModel;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.fragment.HistoryFavoriteFragment;

import java.util.List;

/**
 * Created by kolin on 08.07.2017.
 */

public interface TranslationDAO {

    List<HistoryFavoriteModel> getAllHistory();

    List<HistoryFavoriteModel> getAllFavorites();

    InternalTranslation getEntityById(int id);

    boolean isFavorite(int id);

    void checkEntityToFavorite(int id);

    void deleteAllFavorites();

    void deleteAllHistory();

    void deleteEntityFromFavorites(int id);

    void deleteEntityFromHistory(int id);

    void clearUselessData();

    void addToTable(String textFrom,
                    String textTo,
                    String lang,
                    String dictionary);

    InternalTranslation getEntity(String textFrom,
                                  String textTo,
                                  String lang);


}
