package com.example.kolin.testya.veiw.fragment;

import com.example.kolin.testya.domain.model.InternalTranslation;

/**
 * Created by kolin on 07.04.2017.
 */

public interface Updatable {
    void update();

    void remove(InternalTranslation translation, boolean check);
}
