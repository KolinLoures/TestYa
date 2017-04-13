package com.example.kolin.testya.data.languages;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.ArrayMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by kolin on 09.04.2017.
 */

public class LanguageProperties {

    private Properties properties;
    private Context context;


    public LanguageProperties(Context context) {
        this.context = context;
        properties = new Properties();
    }

    public ArrayMap<String, String> getSupportLanguage() throws IOException {

        ArrayMap<String, String> temp = new ArrayMap<>();
        String line;

        AssetManager assetManager = context.getAssets();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        assetManager.open("languages-ru.txt", Context.MODE_PRIVATE), "UTF-8"));


        while ((line = reader.readLine()) != null) {
            String[] split = line.split("=");
            temp.put(split[0], split[1]);
        }


        reader.close();

        return temp;
    }


}
