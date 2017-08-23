package com.example.kolin.testya.di;

import com.example.kolin.testya.data.preferences.LanguagePreferencesManager;
import com.example.kolin.testya.domain.AddRemoveFavoriteTranslationDb;
import com.example.kolin.testya.domain.CheckFavoriteIs;
import com.example.kolin.testya.domain.DeleteHistoryEntityFromDB;
import com.example.kolin.testya.domain.DeleteTypeDb;
import com.example.kolin.testya.domain.GetEntityFromDB;
import com.example.kolin.testya.domain.GetHistoryFavoriteTranslationFromDb;
import com.example.kolin.testya.domain.GetLanguages;
import com.example.kolin.testya.domain.GetTranslation;
import com.example.kolin.testya.veiw.common.CommonPresenter;
import com.example.kolin.testya.veiw.language.LanguageDialogFragment;
import com.example.kolin.testya.veiw.language.LanguageDialogPresenter;
import com.example.kolin.testya.veiw.translator.TranslatorPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kolin on 23.08.2017.
 */
@Module
public class FragmentsModule {

    @Provides
    TranslatorPresenter provideTranslatorPresenter(AddRemoveFavoriteTranslationDb addRemoveTranslationDb,
                                                   GetTranslation getTranslationUseCase,
                                                   GetLanguages getLanguagesUseCase,
                                                   CheckFavoriteIs checkFavoriteIsUseCase,
                                                   GetEntityFromDB getEntityFromDB,
                                                   LanguagePreferencesManager prefManager
    ) {
        return new TranslatorPresenter(
                addRemoveTranslationDb,
                getTranslationUseCase,
                getLanguagesUseCase,
                checkFavoriteIsUseCase,
                getEntityFromDB,
                prefManager
        );
    }

    @Provides
    CommonPresenter provideCommonPresenter(AddRemoveFavoriteTranslationDb addRemoveTranslationDb,
                                           GetHistoryFavoriteTranslationFromDb favoriteTranslationFromDb,
                                           DeleteHistoryEntityFromDB deleteHistoryEntityFromDB,
                                           DeleteTypeDb deleteTypeDb,
                                           AddRemoveFavoriteTranslationDb addRemoveFavoriteTranslationDb) {

        return new CommonPresenter(
                addRemoveTranslationDb,
                favoriteTranslationFromDb,
                deleteHistoryEntityFromDB,
                deleteTypeDb,
                addRemoveFavoriteTranslationDb);
    }

    @Provides
    LanguageDialogPresenter provideLanguageDialogPresenter(GetLanguages getLanguagesUseCase){
        return new LanguageDialogPresenter(getLanguagesUseCase);
    }
}
