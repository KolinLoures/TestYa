package com.example.kolin.testya.data.models.dictionary;

import java.util.List;

/**
 * Created by kolin on 01.04.2017.
 */

public class Def {

    private String text;
    private String pos;
    private List<Tr> tr = null;

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
}
