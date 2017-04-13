package com.example.kolin.testya.data.entity.dictionary;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by kolin on 01.04.2017.
 */

public class Ex implements Parcelable {
    private String text;
    private List<Tr> tr = null;

    protected Ex(Parcel in) {
        text = in.readString();
        tr = in.createTypedArrayList(Tr.CREATOR);
    }

    public static final Creator<Ex> CREATOR = new Creator<Ex>() {
        @Override
        public Ex createFromParcel(Parcel in) {
            return new Ex(in);
        }

        @Override
        public Ex[] newArray(int size) {
            return new Ex[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Tr> getTr() {
        return tr;
    }

    public void setTr(List<Tr> tr) {
        this.tr = tr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeTypedList(tr);
    }
}
