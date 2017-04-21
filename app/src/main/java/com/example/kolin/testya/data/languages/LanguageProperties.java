package com.example.kolin.testya.data.languages;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.ArrayMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

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

    /**
     *  Get support languages of translations and their codes from assets files
     *  in translation with current system lang.
     *
     *
     * @param lang Current system lang
     * @return {@link ArrayMap} where key (language) {@link String}
     *         and value (language code) {@link String}.
     *         If file with lang not exist will return default EN file.
     *
     * @throws IOException
     */
    public ArrayMap<String, String> getSupportLanguage(String lang) throws IOException {

        ArrayMap<String, String> temp = new ArrayMap<>();
        String line;

        AssetManager assetManager = context.getAssets();

        String fileName = String.format("languages-%s.txt", lang);

        if (!Arrays.asList(assetManager.list("")).contains(fileName))
            fileName = "languages-en.txt";

        InputStream assertOpen = assetManager.open(fileName, Context.MODE_PRIVATE);
        BufferedReader reader = new BufferedReader(new InputStreamReader(assertOpen, "UTF-8"));

        while ((line = reader.readLine()) != null) {
            String[] split = line.split("=");
            temp.put(split[0], split[1]);
        }


        reader.close();

        return temp;
    }


}
