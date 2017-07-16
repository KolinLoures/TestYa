package com.example.kolin.testya.domain.model;

/**
 * Created by kolin on 15.07.2017.
 */

public class Language {

    private String code;
    private String def;

    public Language() {
    }

    public Language(String code, String def) {
        this.code = code;
        this.def = def;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Language language = (Language) o;

        if (code != null ? !code.equals(language.code) : language.code != null) return false;
        return def != null ? def.equals(language.def) : language.def == null;

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (def != null ? def.hashCode() : 0);
        return result;
    }
}
