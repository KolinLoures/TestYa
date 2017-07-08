package com.example.kolin.testya.data.entity.dictionary;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kolin on 01.04.2017.
 *
 * Data class for meaning and syn texts
 */

public class MeanSyn implements Parcelable {

    private String text;

    protected MeanSyn(Parcel in) {
        text = in.readString();
    }

    public static final Creator<MeanSyn> CREATOR = new Creator<MeanSyn>() {
        @Override
        public MeanSyn createFromParcel(Parcel in) {
            return new MeanSyn(in);
        }

        @Override
        public MeanSyn[] newArray(int size) {
            return new MeanSyn[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
    }
}
