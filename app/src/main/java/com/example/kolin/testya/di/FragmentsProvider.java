package com.example.kolin.testya.di;

import com.example.kolin.testya.veiw.common.CommonFragment;
import com.example.kolin.testya.veiw.language.LanguageDialogFragment;
import com.example.kolin.testya.veiw.translator.TranslatorFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by kolin on 23.08.2017.
 */
@Module
public abstract class FragmentsProvider {

    @ContributesAndroidInjector(modules = FragmentsModule.class)
    abstract TranslatorFragment provideTranslatorFragmentProvider();

    @ContributesAndroidInjector(modules = FragmentsModule.class)
    abstract CommonFragment provideCommonFragmentProvider();

    @ContributesAndroidInjector(modules = FragmentsModule.class)
    abstract LanguageDialogFragment provideLanDialogFragment();
}
