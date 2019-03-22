package com.example.android.xenoblade;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.util.Patterns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Blade extends BaseContainer<Blade> {
    public Blade() {
        super();
        LOG_TAG = Blade.class.getName();
    }

    @Override
    String getJsonUrlDetails() {
        if (indexPage == -1) {
            return null;
        }
        return "https://xenoblade.fandom.com/api.php?action=query&format=json&pageids=" + indexPage + "&prop=pageprops";
    }

    /**
     * Parses JSON data from https://xenoblade.fandom.com/api.php?action=query&format=json&pageids=" + pageIndex + "&prop=pageprops"
     * See: http://www.tutorialspoint.com/android/android_json_parser.htm
     */
    boolean parseInfoData(String jsonResponse) throws JSONException {
        Log.e(LOG_TAG, "@parseInfoData 2: " + this);
        if (jsonResponse == null) {
            Log.e(LOG_TAG, "Get JSON error");
            return false;
        }
        JSONObject pages = new JSONObject(jsonResponse)
                .getJSONObject("query")
                .getJSONObject("pages");

        JSONObject pageItem = pages.getJSONObject(pages.keys().next());
        if (!pageItem.has("pageprops")) {
            return false;
        }
        JSONObject pageprops = pageItem.getJSONObject("pageprops");
        if (!pageprops.has("infoboxes")) {
            return false;
        }
        String infoBoxesRaw = pageprops.getString("infoboxes");
        if (infoBoxesRaw == null || infoBoxesRaw.length() < 1) {
            return false;
        }
        JSONArray data = new JSONArray(infoBoxesRaw)
                .getJSONObject(0)
                .getJSONArray("data");

        for (int i = 0; i < data.length(); i++) {
            JSONObject dataItem = data.getJSONObject(i);
            switch (dataItem.getString("type")) {
                case "image":
                    setImage(dataItem.getJSONArray("data")
                            .getJSONObject(0)
                            .getString("url"));
                    continue;

                case "group":
                    JSONArray subDataList = dataItem.getJSONObject("data")
                            .getJSONArray("value");
                    for (int j = 0; j < subDataList.length(); j++) {
                        JSONObject value = subDataList.getJSONObject(j)
                                .getJSONObject("data");

                        //See: https://stackoverflow.com/questions/6384240/how-to-parse-a-url-from-a-string-in-android/26426891#26426891
                        Matcher matcher = Patterns.WEB_URL.matcher(value.getString("value"));
                        if (matcher.find() && (value.getString("source").equals("element"))) {
                            setSubImage(matcher.group());
                            break;
                        }
                    }
            }
        }
        return (!urlSubImage.isEmpty()) && (!urlImage.isEmpty());
    }
}
