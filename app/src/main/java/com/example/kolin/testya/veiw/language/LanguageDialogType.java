package com.example.kolin.testya.veiw.language;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by kolin on 16.07.2017.
 */

public class LanguageDialogType {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            TYPE_FROM,
            TYPE_TO
    })
    public @interface TypeLangDialog {}

    public static final int TYPE_FROM = 0;
    public static final int TYPE_TO = 1;

}
