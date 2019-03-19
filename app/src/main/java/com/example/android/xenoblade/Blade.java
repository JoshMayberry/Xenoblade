package com.example.android.xenoblade;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.Patterns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Blade {
    private String LOG_TAG = Blade.class.getSimpleName();

    private int pageIndex = -1;
    private String title = "";
    private String urlArt = "";
    private String urlPage = "";
    private String urlPortrait = "";
    private String urlElement = "";
    private String urlRarity = "";
    private String gender = "";
    private String type = "";
    private String weapon = "";
    private String role = "";
    private String source = "";
    private String mercTitle = "";

    private static final Pattern regexRemoveLink = Pattern.compile("<[^<]*>");

    //https://xenoblade.fandom.com/api/v1/Articles/List?category=Blades&limit=1000
    //https://xenoblade.fandom.com/api.php?action=query&format=json&pageids=72205&prop=pageprops
    //https://xenoblade.fandom.com/api/v1/Articles/Details?ids=72205

    @Override
    public String toString() {
        return "Blade{" +
                "title='" + title + '\'' +
                "pageIndex='" + pageIndex + '\'' +
                ", urlArt='" + urlArt + '\'' +
                ", urlPage='" + urlPage + '\'' +
                ", urlPortrait='" + urlPortrait + '\'' +
                ", urlElement='" + urlElement + '\'' +
                ", urlRarity='" + urlRarity + '\'' +
                ", gender='" + gender + '\'' +
                ", type='" + type + '\'' +
                ", weapon='" + weapon + '\'' +
                ", role='" + role + '\'' +
                ", source='" + source + '\'' +
                ", mercTitle='" + mercTitle + '\'' +
                '}';
    }

    Blade() {
    }

    /**
     * Finds the image from the url and returns it.
     * Use: https://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android/16293557#16293557
     */
    private Bitmap getImageFromURL(String url) {
        try {
            return BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Parses JSON data from https://xenoblade.fandom.com/api/v1/Articles/List?category=Blades&limit=1000
     */
    boolean parseListData(JSONObject root, JSONObject number) throws JSONException, IOException {
        String url = number.getString("url");
        if (url.startsWith("Category")) {
            return false;
        }

        setPageIndex(number.getInt("id"));
        setTitle(number.getString("title"));
        setUrlPage(root.getString("basepath") + url);
        return true;
    }

    /**
     * Parses JSON data from https://xenoblade.fandom.com/api.php?action=query&format=json&pageids=" + pageIndex + "&prop=pageprops"
     * See: http://www.tutorialspoint.com/android/android_json_parser.htm
     */
    boolean parseInfoData(String jsonResponse) throws JSONException {
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
        JSONArray infoboxes = new JSONArray(infoBoxesRaw);
        JSONArray data = infoboxes.getJSONObject(0)
                .getJSONArray("data");

        for (int i = 0; i < data.length(); i++) {
            JSONObject dataItem = data.getJSONObject(i);
            switch (dataItem.getString("type")) {
                case "title":
                    continue;

                case "image":
                    setUrlPortrait(dataItem.getJSONArray("data")
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
                        if (matcher.find()) {
                            switch (value.getString("source")) {
                                case "element":
                                    setUrlElement(matcher.group());
                                    continue;
                                case "rarity":
                                    setUrlRarity(matcher.group());
                            }
                        }
                    }
                    continue;

                case "data":
                    JSONObject subData = dataItem.getJSONObject("data");
                    if (!subData.has("source")) {
                        continue;
                    }
                    switch (subData.getString("source")) {
                        case "gender":
                            setGender(subData.getString("value"));
                            continue;
                        case "type":
                            setType(subData.getString("value"));
                            continue;
                        case "weapon":
                            setWeapon(subData.getString("value"));
                            continue;
                        case "role":
                            setRole(subData.getString("value"));
                            continue;
                        case "source":
                            setSource(regexRemoveLink.matcher(subData.getString("value")).replaceAll(""));
                            continue;
                        case "mercgroup":
                            setMercTitle(subData.getString("value"));
                    }
            }
        }

        return true;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getTitle() {
        return title;
    }

    Blade setTitle(String title) {
        this.title = title;
        return this;
    }

    public Bitmap getArt() {
        return getImageFromURL(urlArt);
    }

    public String getUrlArt() {
        return urlArt;
    }

    Blade setUrlArt(String urlArt) {
        this.urlArt = urlArt;
        return this;
    }

    public String getUrlPage() {
        return urlPage;
    }

    Blade setUrlPage(String urlPage) {
        this.urlPage = urlPage;
        return this;
    }

    public Bitmap getPortrait() {
        return getImageFromURL(urlPortrait);
    }

    public String getUrlPortrait() {
        return urlPortrait;
    }

    Blade setUrlPortrait(String urlPortrait) {
        this.urlPortrait = urlPortrait;
        return this;
    }

    public Bitmap getElement() {
        return getImageFromURL(urlElement);
    }

    public String getUrlElement() {
        return urlElement;
    }

    Blade setUrlElement(String urlElement) {
        this.urlElement = urlElement;
        return this;
    }

    public Bitmap getRarity() {
        return getImageFromURL(urlRarity);
    }

    public String getUrlRarity() {
        return urlRarity;
    }

    Blade setUrlRarity(String urlRarity) {
        this.urlRarity = urlRarity;
        return this;
    }

    public String getGender() {
        return gender;
    }

    Blade setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getType() {
        return type;
    }

    Blade setType(String type) {
        this.type = type;
        return this;
    }

    public String getWeapon() {
        return weapon;
    }

    Blade setWeapon(String weapon) {
        this.weapon = weapon;
        return this;
    }

    public String getRole() {
        return role;
    }

    Blade setRole(String role) {
        this.role = role;
        return this;
    }

    public String getSource() {
        return source;
    }

    Blade setSource(String source) {
        this.source = source;
        return this;
    }

    public String getMercTitle() {
        return mercTitle;
    }

    Blade setMercTitle(String mercTitle) {
        this.mercTitle = mercTitle;
        return this;
    }
}
