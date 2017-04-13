package com.example.kolin.testya.data.entity.dictionary;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kolin on 01.04.2017.
 */

public class Syn implements Parcelable {

    private String text;

    protected Syn(Parcel in) {
        text = in.readString();
    }

    public static final Creator<Syn> CREATOR = new Creator<Syn>() {
        @Override
        public Syn createFromParcel(Parcel in) {
            return new Syn(in);
        }

        @Override
        public Syn[] newArray(int size) {
            return new Syn[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
