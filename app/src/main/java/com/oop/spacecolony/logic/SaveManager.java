package com.oop.spacecolony.logic;

import android.content.Context;
import android.content.SharedPreferences;

import com.oop.spacecolony.model.ColonyArchive;
import com.oop.spacecolony.model.Crew;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class SaveManager {

    private static final String PREF = "spacecolony";
    private static final String KEY = "archive";
    private static final String FILE_NAME = "crew_data.json";

    private static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Crew.class, new CrewTypeAdapter())
                .create();
    }

    public static void save(Context context, ColonyArchive archive) {
        SharedPreferences prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String json = getGson().toJson(archive);
        editor.putString(KEY, json);
        editor.apply();
    }

    public static ColonyArchive load(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY, null);

        if (json == null || json.isEmpty()) {
            return new ColonyArchive();
        }

        try {
            return getGson().fromJson(json, ColonyArchive.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ColonyArchive();
        }
    }

    public static void saveToFile(Context context, ColonyArchive archive) {
        try {
            String json = getGson().toJson(archive);
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(json.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ColonyArchive loadFromFile(Context context) {
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            reader.close();
            fis.close();

            String json = builder.toString();

            if (json.isEmpty()) {
                return new ColonyArchive();
            }

            return getGson().fromJson(json, ColonyArchive.class);

        } catch (Exception e) {
            e.printStackTrace();
            return new ColonyArchive();
        }
    }
}