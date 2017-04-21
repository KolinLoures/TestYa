package com.example.kolin.testya.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kolin on 03.04.2017.
 *
 * InternalTranslation class used in the all layers in applications.
 *
 */

public class InternalTranslation implements Parcelable {

    private int code;
    private String lang;
    private String textTo;
    private String textFrom;

    private boolean isFavorite;
    private String type;

    public InternalTranslation() {
    }

    protected InternalTranslation(Parcel in) {
        code = in.readInt();
        lang = in.readString();
        textTo = in.readString();
        textFrom = in.readString();
        isFavorite = in.readByte() != 0;
        type = in.readString();
    }

    public static final Creator<InternalTranslation> CREATOR = new Creator<InternalTranslation>() {
        @Override
        public InternalTranslation createFromParcel(Parcel in) {
            return new InternalTranslation(in);
        }

        @Override
        public InternalTranslation[] newArray(int size) {
            return new InternalTranslation[size];
        }
    };

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InternalTranslation that = (InternalTranslation) o;

        if (!lang.equals(that.lang)) return false;
        if (!textTo.equals(that.textTo)) return false;
        return textFrom.equals(that.textFrom);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(lang);
        dest.writeString(textTo);
        dest.writeString(textFrom);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeString(type);
    }
}
