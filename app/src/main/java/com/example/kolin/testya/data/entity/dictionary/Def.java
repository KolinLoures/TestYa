package com.example.kolin.testya.data.entity.dictionary;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by kolin on 01.04.2017.
 *
 * Data class for dictionary array
 */

public class Def implements Parcelable {

    private String text;
    private String pos;
    private List<Tr> tr = null;

    protected Def(Parcel in) {
        text = in.readString();
        pos = in.readString();
    }

    public static final Creator<Def> CREATOR = new Creator<Def>() {
        @Override
        public Def createFromParcel(Parcel in) {
            return new Def(in);
        }

        @Override
        public Def[] newArray(int size) {
            return new Def[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
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
        dest.writeString(pos);
    }
}
