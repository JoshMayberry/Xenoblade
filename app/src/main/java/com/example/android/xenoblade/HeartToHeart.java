package com.example.android.xenoblade;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class HeartToHeart extends BaseContainer<HeartToHeart> {
    private static final Pattern regexGetCategory = Pattern.compile("Category:(.*)");

    public HeartToHeart() {
        super();
        LOG_TAG = HeartToHeart.class.getSimpleName();
    }

    @Override
    String getJsonUrlDetails() {
        Matcher matcher = regexGetCategory.matcher(urlPage);

        if (!matcher.find()) {
            return null;
        }

        return "https://xenoblade.fandom.com/api/v1/Articles/List?category=" + matcher.group(1) + "&limit=1000";
    }

    @Override
    boolean parseListData(JSONObject root, JSONObject number) throws JSONException {
        String url = number.getString("url");
        if (!url.contains("Category:")) {
            return false;
        }

        String title = number.getString("title");
        if (title.contains(".")) {
            return false;
        }

        setIndexPage(number.getInt("id"));
        setTitle(title);
        setPage(root.getString("basepath") + url);
        return (indexPage != -1) && (!urlPage.isEmpty());
    }

    @Override
    boolean parseInfoData(String jsonResponse) throws JSONException {
        if (jsonResponse == null) {
            Log.e(LOG_TAG, "Get JSON error");
            return false;
        }

        JSONObject numbers = new JSONObject(jsonResponse);

        //Use: https://stackoverflow.com/questions/6118708/determine-whether-json-is-a-jsonobject-or-jsonarray/31111381#31111381
        JSONArray itemsArray = numbers.optJSONArray("items");
        if (itemsArray != null) {
            setSubText("Events: " + itemsArray.length());
        } else {
            JSONObject itemsObject = numbers.optJSONObject("items");
            if (itemsObject == null) {
                return false;
            }
            setSubText("Events: " + itemsObject.length());
        }
        return true;
    }
}

