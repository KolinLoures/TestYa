package com.example.kolin.testya.data.entity.dictionary;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kolin on 01.04.2017.
 */

public class Mean implements Parcelable {

    private String text;

    protected Mean(Parcel in) {
        text = in.readString();
    }

    public static final Creator<Mean> CREATOR = new Creator<Mean>() {
        @Override
        public Mean createFromParcel(Parcel in) {
            return new Mean(in);
        }

        @Override
        public Mean[] newArray(int size) {
            return new Mean[size];
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
