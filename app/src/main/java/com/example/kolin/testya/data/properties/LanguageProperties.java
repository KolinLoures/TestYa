package com.example.kolin.testya.data.properties;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.ArrayMap;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Set;
import java.util.zip.InflaterInputStream;

/**
 * Created by kolin on 09.04.2017.
 */

public class LanguageProperties {

    private Properties properties;
    private Context context;

    private LanguageProperties languageProperties = null;

    public LanguageProperties(Context context) {
        this.context = context;
        properties = new Properties();
    }

    public ArrayMap<String, String> getSupportLanguage() throws IOException {

        ArrayMap<String, String> temp = new ArrayMap<>();

        AssetManager assetManager = context.getAssets();

        InputStream stream = assetManager.open("language_ru.properties", Context.MODE_PRIVATE);
        InputStreamReader isr = new InputStreamReader(stream, "UTF-8");

        properties.load(isr);

        stream.close();
        isr.close();

        return propertiesToMap(properties);
    }

    private ArrayMap<String, String> propertiesToMap(Properties properties){
        Set<String> names = properties.stringPropertyNames();
        ArrayMap<String, String> temp = new ArrayMap<>();

        for (String n: names)
            temp.put(n, properties.getProperty(n));

        return temp;
    }
}
