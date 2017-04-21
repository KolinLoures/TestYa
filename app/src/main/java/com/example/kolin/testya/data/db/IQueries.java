package com.example.kolin.testya.data.db;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.domain.model.InternalTranslation;

import java.util.List;

/**
 * Interface that represents basic queries for getting history/favorite
 * translation {@link InternalTranslation} data from data base.
 */

public interface IQueries {


    /**
     * Add/Update or Remove {@link InternalTranslation} data in DB.
     *
     * @param translation The {@link InternalTranslation} object that you want to add/update or remove.
     *
     *
     * @param type The {@link TypeSaveTranslation} object represent
     *             in what category do you want add/update or remove object.
     *
     *
     * @param remove boolean flag that means to remove or to add/update object into DB.
     *               True - remove translation and call {@link #removeTranslation(InternalTranslation, String)};
     *               False - update or add translation and call {@link #addOrUpdateTranslation(InternalTranslation, String)};
     *
     *
     * @return {@link Boolean} success of accomplish method
     */
    boolean addOrRemoveTranslation(InternalTranslation translation,
                                   @TypeSaveTranslation.TypeName String type,
                                   boolean remove);


    /**
     * Add/Update {@link InternalTranslation} data in DB.
     *
     * @param translation The {@link InternalTranslation} object that you want to add/update.
     * @param type The {@link TypeSaveTranslation} object represent
     *             in what category do you want add/update or remove object.
     * @return {@link Boolean} success of accomplish method
     */
    boolean addOrUpdateTranslation(InternalTranslation translation,
                                   @TypeSaveTranslation.TypeName String type);

    /**
     * Method get all {@link InternalTranslation} data from DB in according with type.
     *
     * @param type The {@link TypeSaveTranslation} object represent
     *             what category do you get. The Method takes NULL in
     *             type in that case will return all objects from data base.
     * @return {@link List} of {@link InternalTranslation} in according with type.
     */
    List<InternalTranslation> getTranslations(@TypeSaveTranslation.TypeName String type);

    /**
     * Remove object of {@link InternalTranslation} from DB.
     *
     * @param translation The {@link InternalTranslation} object that you want to remove.
     * @param type The {@link TypeSaveTranslation} object represent
     *             in what category do you remove.
     * @return {@link Boolean} success of accomplish method.
     */
    boolean removeTranslation(InternalTranslation translation,
                              @TypeSaveTranslation.TypeName String type);

    /**
     *  Method delete all {@link InternalTranslation} data from DB in according with type.
     *
     * @param type The {@link TypeSaveTranslation} object represent
     *             what type do you want to delete.
     * @return {@link Boolean} success of accomplish method.
     */
    boolean deleteAllType(@TypeSaveTranslation.TypeName
                               String type);

    /**
     * Check is favorite {@link InternalTranslation} object in data base.
     *
     * @param translation represents {@link InternalTranslation} object that you want to check.
     * @return True - object is favorite / False - object is not favorite.
     */
    boolean isFavorite(InternalTranslation translation);
}
