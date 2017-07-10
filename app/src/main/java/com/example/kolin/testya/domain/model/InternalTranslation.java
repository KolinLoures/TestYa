package com.example.kolin.testya.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.kolin.testya.data.entity.dictionary.Def;

import java.util.List;

/**
 * Created by kolin on 03.04.2017.
 * <p>
 * InternalTranslation class used in the all layers in applications.
 */

public class InternalTranslation {

    private int id;
    private String lang;
    private String textFrom;
    private String textTo;

    private boolean isFavorite;

    private List<Def> def = null;

    public InternalTranslation() {}

    public int getId() {
        return id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<Def> getDef() {
        return def;
    }

    public void setDef(List<Def> def) {
        this.def = def;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InternalTranslation that = (InternalTranslation) o;

        if (!lang.equals(that.lang)) return false;
        if (!textTo.equals(that.textTo)) return false;
        return textFrom.equals(that.textFrom);

    }
}
