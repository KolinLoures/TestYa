package com.example.kolin.testya.veiw;

import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.domain.model.InternalTranslation;

/**
 * Created by kolin on 06.04.2017.
 */

public interface ICommonView {

    void showLoadedData(InternalTranslation translation);

    void clearViewPagerFragment(@TypeSaveTranslation.TypeName String type);

    void check(InternalTranslation translation, boolean check);
}
