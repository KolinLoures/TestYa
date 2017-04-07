package com.example.kolin.testya.data.db;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.domain.model.InternalTranslation;

import java.util.List;

/**
 * Created by kolin on 03.04.2017.
 */

public interface IQueries {

    boolean addOrRemoveTranslation(InternalTranslation translation,
                                   boolean remove);


    boolean addOrUpdateTranslation(InternalTranslation translation);

    List<InternalTranslation> getTranslations();

    boolean removeTranslation(InternalTranslation translation);

    void deleteAllType(@TypeSaveTranslation.TypeName
                               String type);

    boolean isFavorite(InternalTranslation translation);
}
