package com.example.kolin.testya.domain.model;

/**
 * Created by kolin on 09.07.2017.
 */

public class HistoryFavoriteModel {

    private int id;
    private String textFrom;
    private String textTo;
    private String lang;

    private boolean isFavorite;

    public HistoryFavoriteModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTextFrom() {
        return textFrom;
    }

    public void setTextFrom(String textFrom) {
        this.textFrom = textFrom;
    }

    public String getTextTo() {
        return textTo;
    }

    public void setTextTo(String textTo) {
        this.textTo = textTo;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
