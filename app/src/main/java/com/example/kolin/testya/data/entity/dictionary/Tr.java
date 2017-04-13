package com.example.kolin.testya.data.entity.dictionary;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by kolin on 01.04.2017.
 */

public class Tr implements Parcelable {

    private String text;
    private String pos;
    private List<Syn> syn = null;
    private List<Mean> mean = null;
    private List<Ex> ex = null;

    protected Tr(Parcel in) {
        text = in.readString();
        pos = in.readString();
    }

    public static final Creator<Tr> CREATOR = new Creator<Tr>() {
        @Override
        public Tr createFromParcel(Parcel in) {
            return new Tr(in);
        }

        @Override
        public Tr[] newArray(int size) {
            return new Tr[size];
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

    public List<Syn> getSyn() {
        return syn;
    }

    public void setSyn(List<Syn> syn) {
        this.syn = syn;
    }

    public List<Mean> getMean() {
        return mean;
    }

    public void setMean(List<Mean> mean) {
        this.mean = mean;
    }

    public List<Ex> getEx() {
        return ex;
    }

    public void setEx(List<Ex> ex) {
        this.ex = ex;
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
