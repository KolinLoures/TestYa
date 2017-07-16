package com.example.kolin.testya.data.languages;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.ArrayMap;

import com.example.kolin.testya.domain.model.Language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kolin on 09.04.2017.
 *
 * Class that works with languages assets
 */

public class LanguageProperties {

    private Context context;

    public LanguageProperties(Context context) {
        this.context = context;
    }

    public List<Language> getSupportLanguages() throws IOException {

        List<Language> temp = new ArrayList<>();
        String line;

        AssetManager assetManager = context.getAssets();

        InputStream assertOpen = assetManager.open("languages-en.txt", Context.MODE_PRIVATE);
        BufferedReader reader = new BufferedReader(new InputStreamReader(assertOpen, "UTF-8"));

        while ((line = reader.readLine()) != null) {
            String[] split = line.split("=");
            temp.add(new Language(split[1], split[0]));
        }


        reader.close();

        return temp;
    }


}
