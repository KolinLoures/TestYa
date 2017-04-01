package com.example.kolin.testya.data.models.dictionary;

import java.util.List;

/**
 * Created by kolin on 01.04.2017.
 */

public class Ex {
    private String text;
    private List<Tr> tr = null;

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
}
