package com.example.android.xenoblade;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Location {
    private String title = "";
    private String textAbstract = "";
    private String art = "";
    private String urlPage = "";
    private String urlPortrait = "";
    private ArrayList<String> subLocationList = new ArrayList<>();

    private static final Pattern regexGetSubLocations_1 = Pattern.compile("== Regions ==(.*)== Inhabitants ==");
    private static final Pattern regexGetSubLocations_2 = Pattern.compile("\\[\\[([^\\[]*)]]");

    //https://xenoblade.fandom.com/api/v1/Articles/List?category=XC2_Locations&limit=1000
    private static final String SAMPLE_LIST_LOCATIONS = "{\"items\":[{\"id\":71660,\"title\":\"Alrest\",\"url\":\"\\/wiki\\/Alrest\",\"ns\":0},{\"id\":73300,\"title\":\"Ancient Ship\",\"url\":\"\\/wiki\\/Ancient_Ship\",\"ns\":0},{\"id\":71696,\"title\":\"Argentum\",\"url\":\"\\/wiki\\/Argentum\",\"ns\":0},{\"id\":71712,\"title\":\"Argentum Trade Guild\",\"url\":\"\\/wiki\\/Argentum_Trade_Guild\",\"ns\":0},{\"id\":71699,\"title\":\"Azurda\",\"url\":\"\\/wiki\\/Azurda\",\"ns\":0},{\"id\":73299,\"title\":\"C.S.E.V. Maelstrom\",\"url\":\"\\/wiki\\/C.S.E.V._Maelstrom\",\"ns\":0},{\"id\":74225,\"title\":\"Cliffs of Morytha\",\"url\":\"\\/wiki\\/Cliffs_of_Morytha\",\"ns\":0},{\"id\":72389,\"title\":\"Cloud Sea\",\"url\":\"\\/wiki\\/Cloud_Sea\",\"ns\":0},{\"id\":68391,\"title\":\"Elysium\",\"url\":\"\\/wiki\\/Elysium\",\"ns\":0},{\"id\":71697,\"title\":\"Empire of Mor Ardain\",\"url\":\"\\/wiki\\/Empire_of_Mor_Ardain\",\"ns\":0},{\"id\":74301,\"title\":\"First Low Orbit Station\",\"url\":\"\\/wiki\\/First_Low_Orbit_Station\",\"ns\":0},{\"id\":72647,\"title\":\"Fonsett Village\",\"url\":\"\\/wiki\\/Fonsett_Village\",\"ns\":0},{\"id\":72593,\"title\":\"Genbu\",\"url\":\"\\/wiki\\/Genbu\",\"ns\":0},{\"id\":71694,\"title\":\"Gormott\",\"url\":\"\\/wiki\\/Gormott\",\"ns\":0},{\"id\":71701,\"title\":\"Gormott Province\",\"url\":\"\\/wiki\\/Gormott_Province\",\"ns\":0},{\"id\":74030,\"title\":\"Great Void\",\"url\":\"\\/wiki\\/Great_Void\",\"ns\":0},{\"id\":71695,\"title\":\"Indol\",\"url\":\"\\/wiki\\/Indol\",\"ns\":0},{\"id\":71704,\"title\":\"Indoline Praetorium\",\"url\":\"\\/wiki\\/Indoline_Praetorium\",\"ns\":0},{\"id\":83623,\"title\":\"Judicium Titan\",\"url\":\"\\/wiki\\/Judicium_Titan\",\"ns\":0},{\"id\":72541,\"title\":\"Kingdom of Tantal\",\"url\":\"\\/wiki\\/Kingdom_of_Tantal\",\"ns\":0},{\"id\":71690,\"title\":\"Kingdom of Uraya\",\"url\":\"\\/wiki\\/Kingdom_of_Uraya\",\"ns\":0},{\"id\":74216,\"title\":\"Land of Morytha\",\"url\":\"\\/wiki\\/Land_of_Morytha\",\"ns\":0},{\"id\":72590,\"title\":\"Leftherian Archipelago\",\"url\":\"\\/wiki\\/Leftherian_Archipelago\",\"ns\":0},{\"id\":72658,\"title\":\"Leftherian Titans\",\"url\":\"\\/wiki\\/Leftherian_Titans\",\"ns\":0},{\"id\":71692,\"title\":\"Mor Ardain\",\"url\":\"\\/wiki\\/Mor_Ardain\",\"ns\":0},{\"id\":71831,\"title\":\"Nation\",\"url\":\"\\/wiki\\/Nation\",\"ns\":0},{\"id\":72757,\"title\":\"Salvage Point\",\"url\":\"\\/wiki\\/Salvage_Point\",\"ns\":0},{\"id\":74229,\"title\":\"Spirit Crucible Elpys\",\"url\":\"\\/wiki\\/Spirit_Crucible_Elpys\",\"ns\":0},{\"id\":72591,\"title\":\"Temperantia\",\"url\":\"\\/wiki\\/Temperantia\",\"ns\":0},{\"id\":74222,\"title\":\"Torna (Titan)\",\"url\":\"\\/wiki\\/Torna_(Titan)\",\"ns\":0},{\"id\":71689,\"title\":\"Uraya\",\"url\":\"\\/wiki\\/Uraya\",\"ns\":0},{\"id\":71705,\"title\":\"World Tree\",\"url\":\"\\/wiki\\/World_Tree\",\"ns\":0},{\"id\":77431,\"title\":\"Ancient Ship\",\"url\":\"\\/wiki\\/Category:Ancient_Ship\",\"ns\":14},{\"id\":77494,\"title\":\"C.S.E.V. Maelstrom\",\"url\":\"\\/wiki\\/Category:C.S.E.V._Maelstrom\",\"ns\":14},{\"id\":74285,\"title\":\"Cliffs of Morytha\",\"url\":\"\\/wiki\\/Category:Cliffs_of_Morytha\",\"ns\":14},{\"id\":81730,\"title\":\"Collection Points\",\"url\":\"\\/wiki\\/Category:Collection_Points\",\"ns\":14},{\"id\":75076,\"title\":\"First Low Orbit Station\",\"url\":\"\\/wiki\\/Category:First_Low_Orbit_Station\",\"ns\":14},{\"id\":74877,\"title\":\"Land of Morytha\",\"url\":\"\\/wiki\\/Category:Land_of_Morytha\",\"ns\":14},{\"id\":79406,\"title\":\"Salvage Points\",\"url\":\"\\/wiki\\/Category:Salvage_Points\",\"ns\":14},{\"id\":74480,\"title\":\"Spirit Crucible Elpys\",\"url\":\"\\/wiki\\/Category:Spirit_Crucible_Elpys\",\"ns\":14},{\"id\":74038,\"title\":\"Temperantia\",\"url\":\"\\/wiki\\/Category:Temperantia\",\"ns\":14},{\"id\":74924,\"title\":\"World Tree\",\"url\":\"\\/wiki\\/Category:World_Tree\",\"ns\":14},{\"id\":73402,\"title\":\"XC2 Areas\",\"url\":\"\\/wiki\\/Category:XC2_Areas\",\"ns\":14},{\"id\":91349,\"title\":\"XC2 in-game Locations\",\"url\":\"\\/wiki\\/Category:XC2_in-game_Locations\",\"ns\":14},{\"id\":73401,\"title\":\"XC2 Landmarks\",\"url\":\"\\/wiki\\/Category:XC2_Landmarks\",\"ns\":14},{\"id\":72664,\"title\":\"XC2 Nations\",\"url\":\"\\/wiki\\/Category:XC2_Nations\",\"ns\":14},{\"id\":75836,\"title\":\"XC2 Secret Areas\",\"url\":\"\\/wiki\\/Category:XC2_Secret_Areas\",\"ns\":14},{\"id\":73400,\"title\":\"XC2 Shops\",\"url\":\"\\/wiki\\/Category:XC2_Shops\",\"ns\":14}],\"basepath\":\"https:\\/\\/xenoblade.fandom.com\"}";
    //https://xenoblade.fandom.com/api.php?action=query&format=txtfm&pageids=71660&prop=revisions&rvlimit=1&rvprop=content
    private static final String SAMPLE_SUBLIST_LOCATIONS = "Array\\n(\\n    [query] => Array\\n        (\\n            [pages] => Array\\n                (\\n                    [71660] => Array\\n                        (\\n                            [pageid] => 71660\\n                            [ns] => 0\\n                            [title] => Alrest\\n                            [revisions] => Array\\n                                (\\n                                    [0] => Array\\n                                        (\\n                                            [*] => {{Spoiler}}\\n{{Infobox location\\n|name        = Alrest\\n|image       = XC2-Titans.png\\n|caption     = Overview of Alrest's sea of clouds and the Titans\\n|type        = World\\n|located     = \\n|inhabitants = \\n|weather     = \\n|connects to = \\n|music       = \\n}}\\n'''Alrest''' (Japanese: {{lang|ja|アルスト}}, ''Arusuto'') is the setting of ''[[Xenoblade Chronicles 2]]''. It is the name of the world overrun by a [[Cloud Sea|sea of clouds]] where [[Titan]]s live. Each Titan hosts its own distinct civilizations, cultures, wildlife, and environments. The Titans march toward death and the world of Alrest is disappearing. The heroes try to save the world of Alrest by demystifying its cloudy past.\\n== Regions ==\\n* [[Argentum Trade Guild]] ([[nation]])\\n* [[C.S.E.V. Maelstrom]]\\n* [[Ancient Ship]]\\n* [[Gormott Province]] (nation)\\n* [[Kingdom of Uraya]] (nation)\\n* [[Empire of Mor Ardain]] (nation)\\n* [[Temperantia]]\\n* [[Indoline Praetorium]] (nation)\\n* [[Kingdom of Tantal]] (nation)\\n* [[Leftherian Archipelago]] (nation)\\n* [[Spirit Crucible Elpys]]\\n* [[Cliffs of Morytha]]\\n* [[Land of Morytha]]\\n* [[World Tree]]\\n* [[First Low Orbit Station]]\\n== Inhabitants ==\\n* [[Titan]]\\n* [[Blade]]\\n* [[Nopon]]\\n* [[Ardainian]]\\n* [[Urayan]]\\n* [[Gormotti]]\\n* [[Leftherian]]\\n* [[Tantalese]]\\n* [[Indoline]]\\n* [[Tirkin]]\\n* [[Phonex]]\\n== Story ==\\nIn the ending of ''Xenoblade Chronicles 2'', [[Rex]] and company fly past the Cloud Sea on [[Azurda]]'s back, revealing an endless ocean. The Titans then move to this newly revealed ocean and join together to create a gigantic landmass for all the inhabitants of Alrest to live on.\\n== Trivia ==\\n* With the revelation that Alrest was originally Earth, ''Xenoblade Chronicles 2'' marks the first ''Xeno'' game where Earth is the primary setting. A common recurring theme in the ''Xeno'' series is that each game includes humans whose species originated from Earth, but whose story is set in alien environments; \"xeno\" being Greek for \"alien\".\\n{{clr}}\\n== Gallery ==\\n<gallery>\\nBana's Map.jpg|A map of Alrest as seen in the [[Chairman's Room]] in the [[Argentum Trade Guild]]\\nAlrest Map.PNG|The complete in-game map of Alrest\\nEnding Titans.png|The Titans coming together to form a new land\\nNew Alrest.png|Alrest as it is seen at the end of the game, with the Titans forming a new land\\n</gallery>\\n[[de:Alrest]]\\n[[Category:XC2 Locations]]\\n                                        )\\n                                )\\n                        )\\n                )\\n        )\\n    [query-continue] => Array\\n        (\\n            [revisions] => Array\\n                (\\n                    [rvstartid] => 392706\\n                )\\n        )\\n)\\n";
    //https://xenoblade.fandom.com/api/v1/Articles/Details?ids=71660
    private static final String SAMPLE_LOCATIONS = "{\"items\":{\"71660\":{\"id\":71660,\"title\":\"Alrest\",\"ns\":0,\"url\":\"\\/wiki\\/Alrest\",\"revision\":{\"id\":399355,\"user\":\"XenoKT\",\"user_id\":37804788,\"timestamp\":\"1545550157\"},\"comments\":3,\"type\":\"article\",\"abstract\":\"Alrest (Japanese: \\u30a2\\u30eb\\u30b9\\u30c8, Arusuto) is the setting of Xenoblade Chronicles 2. It is the name of the...\",\"thumbnail\":\"https:\\/\\/vignette.wikia.nocookie.net\\/xenoblade\\/images\\/f\\/f0\\/XC2-Titans.png\\/revision\\/latest\\/window-crop\\/width\\/200\\/x-offset\\/421\\/y-offset\\/0\\/window-width\\/1081\\/window-height\\/1080?cb=20170924123201\",\"original_dimensions\":{\"width\":1920,\"height\":1080}}},\"basepath\":\"https:\\/\\/xenoblade.fandom.com\"}";

    public String getTitle() {
        return title;
    }

    Location setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTextAbstract() {
        return textAbstract;
    }

    Location setTextAbstract(String textAbstract) {
        this.textAbstract = textAbstract;
        return this;
    }

    public String getArt() {
        return art;
    }

    Location setArt(String art) {
        this.art = art;
        return this;
    }

    public String getUrlPage() {
        return urlPage;
    }

    Location setUrlPage(String urlPage) {
        this.urlPage = urlPage;
        return this;
    }

    public String getUrlPortrait() {
        return urlPortrait;
    }

    Location setUrlPortrait(String urlPortrait) {
        Log.e("QueryUtils", "urlPortrait: " + urlPortrait);
        this.urlPortrait = urlPortrait;
        return this;
    }

    public ArrayList<String> getSubLocationList() {
        return subLocationList;
    }

    Location setSubLocationList(ArrayList<String> subLocationList) {
        this.subLocationList = subLocationList;
        return this;
    }

    @Override
    public String toString() {
        return "Location{" +
                "title='" + title + '\'' +
                ", textAbstract='" + textAbstract + '\'' +
                ", art='" + art + '\'' +
                ", urlPage='" + urlPage + '\'' +
                ", urlPortrait='" + urlPortrait + '\'' +
                ", subLocationList=" + subLocationList.size() +
                '}';
    }

    Location parse_sample() {
        return parse(SAMPLE_SUBLIST_LOCATIONS, SAMPLE_LOCATIONS);
    }

    //See: http://www.tutorialspoint.com/android/android_json_parser.htm
    Location parse(String rawSubList, String rawDetails) {
        try {
            JSONObject root = new JSONObject(rawDetails);
            Log.e("QueryUtils", "root: " + root);
            JSONObject items = root.getJSONObject("items");
            JSONObject number = items.getJSONObject(items.keys().next());
            setTitle(number.getString("title"));
            setTextAbstract(number.getString("abstract"));
            setArt(number.getString("thumbnail"));
            setUrlPage(root.getString("basepath") + number.getString("url"));

            Matcher listMatcher = regexGetSubLocations_1.matcher(rawSubList);
            if (listMatcher.find()) {
                Matcher matcher = regexGetSubLocations_2.matcher((listMatcher.group(1)));
                while (matcher.find()) {
                    subLocationList.add(matcher.group(1));
                }
            }
        } catch (
                JSONException e) {
            Log.e("QueryUtils", "Problem parsing the Location JSON results", e);
        }
        return this;
    }
}
