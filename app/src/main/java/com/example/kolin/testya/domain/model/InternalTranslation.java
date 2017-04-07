package com.example.kolin.testya.domain.model;

import com.example.kolin.testya.data.TypeSaveTranslation;

/**
 * Created by kolin on 03.04.2017.
 */

public class InternalTranslation {

    private int code;
    private String lang;
    private String textTo;
    private String textFrom;

    private boolean isFavorite;
    @TypeSaveTranslation.TypeName
    private String type;

    public InternalTranslation() {
    }

    public int getCode() {
        return code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTextTo() {
        return textTo;
    }

    public void setTextTo(String textTo) {
        this.textTo = textTo;
    }

    public String getTextFrom() {
        return textFrom;
    }

    public void setTextFrom(String textFrom) {
        this.textFrom = textFrom;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
