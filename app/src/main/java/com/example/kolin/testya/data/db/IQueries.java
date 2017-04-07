package com.example.kolin.testya.data.db;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.domain.model.InternalTranslation;

import java.util.List;

/**
 * Created by kolin on 03.04.2017.
 */

public interface IQueries {

    boolean addOrRemoveTranslation(InternalTranslation translation,
                                   @TypeSaveTranslation.TypeName String type,
                                   boolean remove);


    boolean addOrUpdateTranslation(InternalTranslation translation,
                                   @TypeSaveTranslation.TypeName String type);

    List<InternalTranslation> getTranslation(@TypeSaveTranslation.TypeName
                                                     String type);

    boolean removeTranslation(InternalTranslation translation,
                           @TypeSaveTranslation.TypeName
                                   String type);

    void deleteAllType(@TypeSaveTranslation.TypeName
                               String type);

    boolean isAddedToTable(InternalTranslation translation,
            @TypeSaveTranslation.TypeName
                    String type);
}