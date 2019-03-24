package com.example.android.xenoblade;

import android.util.Log;
import android.util.Patterns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A container for information about different pouch items
 */
public class Item extends BaseContainer<Item> {
    private static final Pattern regexGetCategory = Pattern.compile("Category:(.*)");
    private static final Pattern regexGetPrice = Pattern.compile("(\\S*).*title\\W*(\\w*)");

    public Item() {
        super();
        LOG_TAG = Item.class.getSimpleName();
    }

    /**
     * Instead of adding this item, add the items that are linked on its page.
     */
    @Override
    List<Item> getContainerList(JSONObject root, JSONObject number) throws JSONException, IOException {
        String url = number.getString("url");
        if (url.isEmpty()) {
            return null;
        }
        Matcher matcher = regexGetCategory.matcher(url);
        if (!matcher.find()) {
            return null;
        }

        String jsonResponseList = QueryUtilities.makeHttpRequest("https://xenoblade.fandom.com/api/v1/Articles/List?category=" + matcher.group(1) + "&limit=1000");
        if (jsonResponseList == null) {
            Log.e(LOG_TAG, "Get JSON error");
            return null;
        }
        JSONObject subRoot = new JSONObject(jsonResponseList);
        JSONArray subItems = subRoot.getJSONArray("items");

        //Populate sub-items
        List<Item> data = new ArrayList<>();
        for (int i = 0; i < subItems.length(); i++) {
            Item container = new Item();

            List<Item> containerList = container.getContainerList(subRoot, subItems.getJSONObject(i), true);
            if (containerList == null) {
                continue;
            }
            data.addAll(containerList);
        }
        return data;
    }

    /**
     * Allows sub-items to use the default criteria for retrieving a list of usable items.
     */
    List<Item> getContainerList(JSONObject root, JSONObject number, boolean isSubItem) throws IOException, JSONException {
        if (isSubItem) {
            return super.getContainerList(root, number);
        } else {
            return getContainerList(root, number);
        }
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
     * Parses the info box for the item's cost and rarity.
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

                case "data":
                    JSONObject subDataItem = dataItem.getJSONObject("data");
                    switch (subDataItem.getString("label")) {
                        case "Sell Price":
                            Matcher matcher = regexGetPrice.matcher(subDataItem.getString("value"));
                            if (matcher.find()) {
                                setSubText(matcher.group(1) + " " + matcher.group(2));
                            }
                            continue;

                        case "Rarity":
                            //See: https://stackoverflow.com/questions/6384240/how-to-parse-a-url-from-a-string-in-android/26426891#26426891
                            Matcher matcher2 = Patterns.WEB_URL.matcher(subDataItem.getString("value"));
                            if (matcher2.find()) {
                               setSubImage(matcher2.group());
                            }
                    }
            }
        }
        return (!urlSubImage.isEmpty()) && (!urlImage.isEmpty());
    }
}

