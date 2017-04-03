package com.example.kolin.testya.data.entity;

import android.os.Parcel;

import java.util.List;

/**
 * Created by kolin on 28.03.2017.
 */

public class Translation{

    private int code;
    private String lang;
    private List<String> text = null;

    protected Translation(Parcel in) {
        code = in.readInt();
        lang = in.readString();
        text = in.createStringArrayList();
    }



    public int getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

}
