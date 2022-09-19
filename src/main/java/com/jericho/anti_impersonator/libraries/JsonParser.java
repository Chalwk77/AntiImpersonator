// JSON Parser using org.json libraries
// Copyright (c) 2022, Jericho Crosby <jericho.crosby227@gmail.com>

package com.jericho.anti_impersonator.libraries;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JsonParser {

    public static JSONObject getSettings() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/settings.json"));
        String line = reader.readLine();
        StringBuilder stringBuilder = new StringBuilder();
        while (line != null) {
            stringBuilder.append(line);
            line = reader.readLine();
        }
        String content = stringBuilder.toString();
        return new JSONObject(content);
    }

    static JSONObject settings;

    static {
        try {
            settings = getSettings();
        } catch (IOException e) {
            System.out.print("ERROR: Could not read settings.json file.");
            throw new RuntimeException(e);
        }
    }

    public static String[] getWhitelistedNames() throws IOException {
        JSONArray jsonArray = settings.getJSONArray("members");
        String[] whitelist = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            whitelist[i] = jsonArray.getString(i);
        }
        return whitelist;
    }

    public static String getToken() {
        return settings.getString("token");
    }
}
