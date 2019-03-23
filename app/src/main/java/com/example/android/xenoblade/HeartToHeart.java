package com.example.android.xenoblade;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A container for information about Heart-to-Heart Events.
 */
class HeartToHeart extends BaseContainer<HeartToHeart> {
    private static final Pattern regexGetCategory = Pattern.compile("Category:(.*)");

    public HeartToHeart() {
        super();
        LOG_TAG = HeartToHeart.class.getSimpleName();
    }

    /**
     * A list-style {@link JSONObject} will be used instead of a detail-style one.
     */
    @Override
    String getJsonUrlDetails() {
        Matcher matcher = regexGetCategory.matcher(urlPage);

        if (!matcher.find()) {
            return null;
        }

        return "https://xenoblade.fandom.com/api/v1/Articles/List?category=" + matcher.group(1) + "&limit=1000";
    }

    /**
     * Category pages are desired instead of detail pages.
     */
    @Override
    boolean parseListData(JSONObject root, JSONObject number) throws JSONException {
        String url = number.getString("url");
        if (!url.contains("Category:")) {
            return false;
        }

        setIndexPage(number.getInt("id"));
        setTitle(title);
        setPage(root.getString("basepath") + url);
        return (indexPage != -1) && (!urlPage.isEmpty());
    }

    /**
     * Use the list-style {@link JSONObject} to determine how many Heart-to-Heart events the category has.
     */
    @Override
    boolean parseDetailData(String jsonResponse) throws JSONException {
        if (jsonResponse == null) {
            Log.e(LOG_TAG, "Get JSON error");
            return false;
        }

        JSONObject numbers = new JSONObject(jsonResponse);

        //If a page has only one event, an array is not returned
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

