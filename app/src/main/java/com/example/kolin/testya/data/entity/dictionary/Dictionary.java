package com.example.kolin.testya.data.entity.dictionary;

import java.util.List;

/**
 * Created by kolin on 01.04.2017.
 *
 * Data class for Dictionary results.
 */

public class Dictionary {

    private List<Def> def = null;

    public List<Def> getDef() {
        return def;
    }

    public void setDef(List<Def> def) {
        this.def = def;
    }
}
