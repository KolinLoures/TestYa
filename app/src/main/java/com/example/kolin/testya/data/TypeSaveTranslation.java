package com.example.kolin.testya.data;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolin on 02.04.2017.
 */

public class TypeSaveTranslation {

    private static List<String> list = new ArrayList<>();


    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            HISTORY,
            FAVORITE
    })
    public @interface TypeName {}

    public static final String HISTORY = "history";
    public static final String FAVORITE = "favorite";

    static {
        list.add(TypeSaveTranslation.HISTORY);
        list.add(TypeSaveTranslation.FAVORITE);
    }

    public static String getTypeById(int id){
        return list.get(id);
    }

}
