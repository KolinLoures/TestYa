package com.example.kolin.testya.data.db;

import android.support.annotation.NonNull;

import com.example.kolin.testya.domain.model.InternalTranslation;

import java.net.InterfaceAddress;
import java.util.List;

/**
 * Created by kolin on 08.07.2017.
 */

public interface TranslationDAO {

    void addDBTranslation(@NonNull InternalTranslation translation);

    List<InternalTranslation> getDBTranslations();

    void updateDBTranslation(@NonNull InternalTranslation translation);

    void deleteDBTranslation(@NonNull InternalTranslation translation);

    void clearDBTranslationsHistory();

    void clearDBTranslationsFavorite();

    boolean isFavorite(@NonNull InternalTranslation internalTranslation);
}
