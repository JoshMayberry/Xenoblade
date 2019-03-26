package com.example.android.xenoblade;

import android.util.Log;
import android.util.Patterns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;

/**
 * A container for information about Blades (companion characters that lend you power).
 */
public class Blade extends GenericContainer<Blade> {
    public Blade() {
        super();
        LOG_TAG = Blade.class.getName();
    }

    /**
     * Use the info box of the page to gather details.
     */
    @Override
    String getJsonUrlDetails() {
        if (indexPage == -1) {
            return null;
        }
        return "https://xenoblade.fandom.com/api.php?action=query&format=json&pageids=" + indexPage + "&prop=pageprops";
    }

    /**
     * Parses the info box for what element the blade is.
     * See: http://www.tutorialspoint.com/android/android_json_parser.htm
     */
    @Override
    boolean parseDetailData(String jsonResponse) throws JSONException {
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
