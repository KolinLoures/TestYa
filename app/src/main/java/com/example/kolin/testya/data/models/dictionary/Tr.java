package com.example.kolin.testya.data.models.dictionary;

import java.util.List;

/**
 * Created by kolin on 01.04.2017.
 */

public class Tr {

    private String text;
    private String pos;
    private List<Syn> syn = null;
    private List<Mean> mean = null;
    private List<Ex> ex = null;

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
}
