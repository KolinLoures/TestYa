package com.example.kolin.testya.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kolin on 09.07.2017.
 */

public class HistoryFavoriteModel implements Parcelable {

    private int id;
    private String textFrom;
    private String textTo;
    private String lang;

    private boolean isFavorite;

    public HistoryFavoriteModel() {
    }

    protected HistoryFavoriteModel(Parcel in) {
        id = in.readInt();
        textFrom = in.readString();
        textTo = in.readString();
        lang = in.readString();
        isFavorite = in.readByte() != 0;
    }

    public static final Creator<HistoryFavoriteModel> CREATOR = new Creator<HistoryFavoriteModel>() {
        @Override
        public HistoryFavoriteModel createFromParcel(Parcel in) {
            return new HistoryFavoriteModel(in);
        }

        @Override
        public HistoryFavoriteModel[] newArray(int size) {
            return new HistoryFavoriteModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(textFrom);
        dest.writeString(textTo);
        dest.writeString(lang);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }
}
