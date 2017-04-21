package com.example.kolin.testya.di.components;

import com.example.kolin.testya.di.ActivityScope;
import com.example.kolin.testya.veiw.fragment.HistoryFavoriteFragment;
import com.example.kolin.testya.veiw.fragment.TranslatorFragment;

import dagger.Component;

/**
 * Created by kolin on 15.04.2017.
 */
@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ViewComponent {

    void inject(TranslatorFragment translatorFragment);

    void inject(HistoryFavoriteFragment favoriteFragment);

}
