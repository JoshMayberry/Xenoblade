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
    private String title = "";
    private String textAbstract = "";
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
    private static final String SAMPLE_LIST_BLADES = "{\"items\":[{\"id\":72205,\"title\":\"Adenine\",\"url\":\"\\/wiki\\/Adenine\",\"ns\":0},{\"id\":73021,\"title\":\"Aegaeon\",\"url\":\"\\/wiki\\/Aegaeon\",\"ns\":0},{\"id\":71698,\"title\":\"Aegis\",\"url\":\"\\/wiki\\/Aegis\",\"ns\":0},{\"id\":73022,\"title\":\"Agate\",\"url\":\"\\/wiki\\/Agate\",\"ns\":0},{\"id\":74132,\"title\":\"Akatsuki\",\"url\":\"\\/wiki\\/Akatsuki\",\"ns\":0},{\"id\":71979,\"title\":\"Azami\",\"url\":\"\\/wiki\\/Azami\",\"ns\":0},{\"id\":30684,\"title\":\"Blade\",\"url\":\"\\/wiki\\/Blade\",\"ns\":0},{\"id\":81842,\"title\":\"Blade (enemy)\",\"url\":\"\\/wiki\\/Blade_(enemy)\",\"ns\":0},{\"id\":72499,\"title\":\"Boreas\",\"url\":\"\\/wiki\\/Boreas\",\"ns\":0},{\"id\":72536,\"title\":\"Brighid\",\"url\":\"\\/wiki\\/Brighid\",\"ns\":0},{\"id\":73496,\"title\":\"Cole\",\"url\":\"\\/wiki\\/Cole\",\"ns\":0},{\"id\":71978,\"title\":\"Dagas\",\"url\":\"\\/wiki\\/Dagas\",\"ns\":0},{\"id\":73193,\"title\":\"Dahlia\",\"url\":\"\\/wiki\\/Dahlia\",\"ns\":0},{\"id\":82908,\"title\":\"Dolmes\",\"url\":\"\\/wiki\\/Dolmes\",\"ns\":0},{\"id\":70766,\"title\":\"Dromarch\",\"url\":\"\\/wiki\\/Dromarch\",\"ns\":0},{\"id\":72083,\"title\":\"Electra\",\"url\":\"\\/wiki\\/Electra\",\"ns\":0},{\"id\":73493,\"title\":\"Fan la Norne\",\"url\":\"\\/wiki\\/Fan_la_Norne\",\"ns\":0},{\"id\":71716,\"title\":\"Finch\",\"url\":\"\\/wiki\\/Finch\",\"ns\":0},{\"id\":74065,\"title\":\"Flesh Eater\",\"url\":\"\\/wiki\\/Flesh_Eater\",\"ns\":0},{\"id\":71896,\"title\":\"Floren\",\"url\":\"\\/wiki\\/Floren\",\"ns\":0},{\"id\":75806,\"title\":\"Giga Rosa\",\"url\":\"\\/wiki\\/Giga_Rosa\",\"ns\":0},{\"id\":72545,\"title\":\"Godfrey\",\"url\":\"\\/wiki\\/Godfrey\",\"ns\":0},{\"id\":72330,\"title\":\"Gorg\",\"url\":\"\\/wiki\\/Gorg\",\"ns\":0},{\"id\":72552,\"title\":\"Herald\",\"url\":\"\\/wiki\\/Herald\",\"ns\":0},{\"id\":72109,\"title\":\"Jin\",\"url\":\"\\/wiki\\/Jin\",\"ns\":0},{\"id\":73089,\"title\":\"Kasandra\",\"url\":\"\\/wiki\\/Kasandra\",\"ns\":0},{\"id\":72386,\"title\":\"Kora\",\"url\":\"\\/wiki\\/Kora\",\"ns\":0},{\"id\":73265,\"title\":\"KOS-MOS\",\"url\":\"\\/wiki\\/KOS-MOS\",\"ns\":0},{\"id\":92977,\"title\":\"Krogane\",\"url\":\"\\/wiki\\/Krogane\",\"ns\":0},{\"id\":73930,\"title\":\"Lila\",\"url\":\"\\/wiki\\/Lila\",\"ns\":0},{\"id\":79283,\"title\":\"Llonya\",\"url\":\"\\/wiki\\/Llonya\",\"ns\":0},{\"id\":68460,\"title\":\"Mythra\",\"url\":\"\\/wiki\\/Mythra\",\"ns\":0},{\"id\":72242,\"title\":\"Newt\",\"url\":\"\\/wiki\\/Newt\",\"ns\":0},{\"id\":68505,\"title\":\"Nia\",\"url\":\"\\/wiki\\/Nia\",\"ns\":0},{\"id\":72024,\"title\":\"Nim\",\"url\":\"\\/wiki\\/Nim\",\"ns\":0},{\"id\":85627,\"title\":\"Olua\",\"url\":\"\\/wiki\\/Olua\",\"ns\":0},{\"id\":72534,\"title\":\"Pandoria\",\"url\":\"\\/wiki\\/Pandoria\",\"ns\":0},{\"id\":71807,\"title\":\"Perceval\",\"url\":\"\\/wiki\\/Perceval\",\"ns\":0},{\"id\":72143,\"title\":\"Perun\",\"url\":\"\\/wiki\\/Perun\",\"ns\":0},{\"id\":77506,\"title\":\"Pneuma\",\"url\":\"\\/wiki\\/Pneuma\",\"ns\":0},{\"id\":72743,\"title\":\"Poppi QT\",\"url\":\"\\/wiki\\/Poppi_QT\",\"ns\":0},{\"id\":74092,\"title\":\"Poppi QT\\u03c0\",\"url\":\"\\/wiki\\/Poppi_QT%CF%80\",\"ns\":0},{\"id\":71347,\"title\":\"Poppi \\u03b1\",\"url\":\"\\/wiki\\/Poppi_%CE%B1\",\"ns\":0},{\"id\":72789,\"title\":\"Praxis\",\"url\":\"\\/wiki\\/Praxis\",\"ns\":0},{\"id\":68458,\"title\":\"Pyra\",\"url\":\"\\/wiki\\/Pyra\",\"ns\":0},{\"id\":71348,\"title\":\"Roc\",\"url\":\"\\/wiki\\/Roc\",\"ns\":0},{\"id\":73999,\"title\":\"Rosa\",\"url\":\"\\/wiki\\/Rosa\",\"ns\":0},{\"id\":72947,\"title\":\"Sheba\",\"url\":\"\\/wiki\\/Sheba\",\"ns\":0},{\"id\":72793,\"title\":\"Theory\",\"url\":\"\\/wiki\\/Theory\",\"ns\":0},{\"id\":73195,\"title\":\"Ursula\",\"url\":\"\\/wiki\\/Ursula\",\"ns\":0},{\"id\":72518,\"title\":\"Vale\",\"url\":\"\\/wiki\\/Vale\",\"ns\":0},{\"id\":72463,\"title\":\"Vess\",\"url\":\"\\/wiki\\/Vess\",\"ns\":0},{\"id\":72550,\"title\":\"Wulfric\",\"url\":\"\\/wiki\\/Wulfric\",\"ns\":0},{\"id\":74056,\"title\":\"Yachik\",\"url\":\"\\/wiki\\/Yachik\",\"ns\":0},{\"id\":72556,\"title\":\"Zenobia\",\"url\":\"\\/wiki\\/Zenobia\",\"ns\":0},{\"id\":73797,\"title\":\"Adenine\",\"url\":\"\\/wiki\\/Category:Adenine\",\"ns\":14},{\"id\":73798,\"title\":\"Aegaeon\",\"url\":\"\\/wiki\\/Category:Aegaeon\",\"ns\":14},{\"id\":73799,\"title\":\"Agate\",\"url\":\"\\/wiki\\/Category:Agate\",\"ns\":14},{\"id\":85178,\"title\":\"Akhos\",\"url\":\"\\/wiki\\/Category:Akhos\",\"ns\":14},{\"id\":73800,\"title\":\"Azami\",\"url\":\"\\/wiki\\/Category:Azami\",\"ns\":14},{\"id\":81843,\"title\":\"Blade Enemies\",\"url\":\"\\/wiki\\/Category:Blade_Enemies\",\"ns\":14},{\"id\":73801,\"title\":\"Boreas\",\"url\":\"\\/wiki\\/Category:Boreas\",\"ns\":14},{\"id\":80954,\"title\":\"Brighid\",\"url\":\"\\/wiki\\/Category:Brighid\",\"ns\":14},{\"id\":91918,\"title\":\"Corvin\",\"url\":\"\\/wiki\\/Category:Corvin\",\"ns\":14},{\"id\":85186,\"title\":\"Cressidus\",\"url\":\"\\/wiki\\/Category:Cressidus\",\"ns\":14},{\"id\":90973,\"title\":\"Crossette\",\"url\":\"\\/wiki\\/Category:Crossette\",\"ns\":14},{\"id\":73802,\"title\":\"Dagas\",\"url\":\"\\/wiki\\/Category:Dagas\",\"ns\":14},{\"id\":80365,\"title\":\"Dahlia\",\"url\":\"\\/wiki\\/Category:Dahlia\",\"ns\":14},{\"id\":86860,\"title\":\"DLC Blades\",\"url\":\"\\/wiki\\/Category:DLC_Blades\",\"ns\":14},{\"id\":73792,\"title\":\"Dromarch\",\"url\":\"\\/wiki\\/Category:Dromarch\",\"ns\":14},{\"id\":73803,\"title\":\"Electra\",\"url\":\"\\/wiki\\/Category:Electra\",\"ns\":14},{\"id\":29492,\"title\":\"Elma\",\"url\":\"\\/wiki\\/Category:Elma\",\"ns\":14},{\"id\":73804,\"title\":\"Finch\",\"url\":\"\\/wiki\\/Category:Finch\",\"ns\":14},{\"id\":16897,\"title\":\"Fiora\",\"url\":\"\\/wiki\\/Category:Fiora\",\"ns\":14},{\"id\":73805,\"title\":\"Floren\",\"url\":\"\\/wiki\\/Category:Floren\",\"ns\":14},{\"id\":73806,\"title\":\"Godfrey\",\"url\":\"\\/wiki\\/Category:Godfrey\",\"ns\":14},{\"id\":73807,\"title\":\"Gorg\",\"url\":\"\\/wiki\\/Category:Gorg\",\"ns\":14},{\"id\":91238,\"title\":\"Haze\",\"url\":\"\\/wiki\\/Category:Haze\",\"ns\":14},{\"id\":73808,\"title\":\"Herald\",\"url\":\"\\/wiki\\/Category:Herald\",\"ns\":14},{\"id\":91237,\"title\":\"Jin\",\"url\":\"\\/wiki\\/Category:Jin\",\"ns\":14},{\"id\":73809,\"title\":\"Kasandra\",\"url\":\"\\/wiki\\/Category:Kasandra\",\"ns\":14},{\"id\":73810,\"title\":\"Kora\",\"url\":\"\\/wiki\\/Category:Kora\",\"ns\":14},{\"id\":73811,\"title\":\"KOS-MOS\",\"url\":\"\\/wiki\\/Category:KOS-MOS\",\"ns\":14},{\"id\":85182,\"title\":\"Mikhail\",\"url\":\"\\/wiki\\/Category:Mikhail\",\"ns\":14},{\"id\":91419,\"title\":\"Minoth\",\"url\":\"\\/wiki\\/Category:Minoth\",\"ns\":14},{\"id\":73795,\"title\":\"Mythra\",\"url\":\"\\/wiki\\/Category:Mythra\",\"ns\":14},{\"id\":73812,\"title\":\"Newt\",\"url\":\"\\/wiki\\/Category:Newt\",\"ns\":14},{\"id\":73789,\"title\":\"Nia\",\"url\":\"\\/wiki\\/Category:Nia\",\"ns\":14},{\"id\":73813,\"title\":\"Nim\",\"url\":\"\\/wiki\\/Category:Nim\",\"ns\":14},{\"id\":85183,\"title\":\"Obrona\",\"url\":\"\\/wiki\\/Category:Obrona\",\"ns\":14},{\"id\":73796,\"title\":\"Pandoria\",\"url\":\"\\/wiki\\/Category:Pandoria\",\"ns\":14},{\"id\":85181,\"title\":\"Patroka\",\"url\":\"\\/wiki\\/Category:Patroka\",\"ns\":14},{\"id\":73814,\"title\":\"Perceval\",\"url\":\"\\/wiki\\/Category:Perceval\",\"ns\":14},{\"id\":85185,\"title\":\"Perdido\",\"url\":\"\\/wiki\\/Category:Perdido\",\"ns\":14},{\"id\":73815,\"title\":\"Perun\",\"url\":\"\\/wiki\\/Category:Perun\",\"ns\":14},{\"id\":73793,\"title\":\"Poppi\",\"url\":\"\\/wiki\\/Category:Poppi\",\"ns\":14},{\"id\":86833,\"title\":\"Poppibuster\",\"url\":\"\\/wiki\\/Category:Poppibuster\",\"ns\":14},{\"id\":73816,\"title\":\"Praxis\",\"url\":\"\\/wiki\\/Category:Praxis\",\"ns\":14},{\"id\":73794,\"title\":\"Pyra\",\"url\":\"\\/wiki\\/Category:Pyra\",\"ns\":14},{\"id\":80791,\"title\":\"Roc\",\"url\":\"\\/wiki\\/Category:Roc\",\"ns\":14},{\"id\":85184,\"title\":\"Sever\",\"url\":\"\\/wiki\\/Category:Sever\",\"ns\":14},{\"id\":80953,\"title\":\"Sheba\",\"url\":\"\\/wiki\\/Category:Sheba\",\"ns\":14},{\"id\":16896,\"title\":\"Shulk\",\"url\":\"\\/wiki\\/Category:Shulk\",\"ns\":14},{\"id\":86639,\"title\":\"T-elos\",\"url\":\"\\/wiki\\/Category:T-elos\",\"ns\":14},{\"id\":73817,\"title\":\"Theory\",\"url\":\"\\/wiki\\/Category:Theory\",\"ns\":14},{\"id\":80366,\"title\":\"Ursula\",\"url\":\"\\/wiki\\/Category:Ursula\",\"ns\":14},{\"id\":73818,\"title\":\"Vale\",\"url\":\"\\/wiki\\/Category:Vale\",\"ns\":14},{\"id\":73819,\"title\":\"Vess\",\"url\":\"\\/wiki\\/Category:Vess\",\"ns\":14},{\"id\":73820,\"title\":\"Wulfric\",\"url\":\"\\/wiki\\/Category:Wulfric\",\"ns\":14},{\"id\":73821,\"title\":\"Zenobia\",\"url\":\"\\/wiki\\/Category:Zenobia\",\"ns\":14},{\"id\":78688,\"title\":\"AdeninePortrait.png\",\"url\":\"\\/wiki\\/File:AdeninePortrait.png\",\"ns\":6},{\"id\":95840,\"title\":\"AegaeonNiall.png\",\"url\":\"\\/wiki\\/File:AegaeonNiall.png\",\"ns\":6},{\"id\":79171,\"title\":\"Beary Ursula 2.PNG\",\"url\":\"\\/wiki\\/File:Beary_Ursula_2.PNG\",\"ns\":6},{\"id\":79172,\"title\":\"Beary Ursula 3.PNG\",\"url\":\"\\/wiki\\/File:Beary_Ursula_3.PNG\",\"ns\":6},{\"id\":79173,\"title\":\"Beary Ursula 4.PNG\",\"url\":\"\\/wiki\\/File:Beary_Ursula_4.PNG\",\"ns\":6},{\"id\":79170,\"title\":\"Beary ursula.PNG\",\"url\":\"\\/wiki\\/File:Beary_ursula.PNG\",\"ns\":6},{\"id\":86640,\"title\":\"DLC Blades.PNG\",\"url\":\"\\/wiki\\/File:DLC_Blades.PNG\",\"ns\":6},{\"id\":95844,\"title\":\"Fan.png\",\"url\":\"\\/wiki\\/File:Fan.png\",\"ns\":6},{\"id\":78450,\"title\":\"FlorenPortrait.png\",\"url\":\"\\/wiki\\/File:FlorenPortrait.png\",\"ns\":6},{\"id\":70999,\"title\":\"Fudor pic.png\",\"url\":\"\\/wiki\\/File:Fudor_pic.png\",\"ns\":6},{\"id\":78628,\"title\":\"GodfreyPortrait.png\",\"url\":\"\\/wiki\\/File:GodfreyPortrait.png\",\"ns\":6},{\"id\":90061,\"title\":\"Gorg Salt Bae.jpg\",\"url\":\"\\/wiki\\/File:Gorg_Salt_Bae.jpg\",\"ns\":6},{\"id\":95845,\"title\":\"HazeLora.png\",\"url\":\"\\/wiki\\/File:HazeLora.png\",\"ns\":6},{\"id\":95846,\"title\":\"HazeLoraDriver.png\",\"url\":\"\\/wiki\\/File:HazeLoraDriver.png\",\"ns\":6},{\"id\":95833,\"title\":\"HugoBlades.png\",\"url\":\"\\/wiki\\/File:HugoBlades.png\",\"ns\":6},{\"id\":70997,\"title\":\"Kirim pic.png\",\"url\":\"\\/wiki\\/File:Kirim_pic.png\",\"ns\":6},{\"id\":93968,\"title\":\"KosMosTelosWorld-tree.png\",\"url\":\"\\/wiki\\/File:KosMosTelosWorld-tree.png\",\"ns\":6},{\"id\":95106,\"title\":\"PneumaAwaken.png\",\"url\":\"\\/wiki\\/File:PneumaAwaken.png\",\"ns\":6},{\"id\":95110,\"title\":\"PneumaConcept.jpg\",\"url\":\"\\/wiki\\/File:PneumaConcept.jpg\",\"ns\":6},{\"id\":95109,\"title\":\"PneumaFace.png\",\"url\":\"\\/wiki\\/File:PneumaFace.png\",\"ns\":6},{\"id\":95107,\"title\":\"PneumaRex.png\",\"url\":\"\\/wiki\\/File:PneumaRex.png\",\"ns\":6},{\"id\":95108,\"title\":\"PneumaRex2.png\",\"url\":\"\\/wiki\\/File:PneumaRex2.png\",\"ns\":6},{\"id\":71352,\"title\":\"Poppi alpha.png\",\"url\":\"\\/wiki\\/File:Poppi_alpha.png\",\"ns\":6},{\"id\":71353,\"title\":\"Poppi.png\",\"url\":\"\\/wiki\\/File:Poppi.png\",\"ns\":6},{\"id\":70996,\"title\":\"Pyra pic.png\",\"url\":\"\\/wiki\\/File:Pyra_pic.png\",\"ns\":6},{\"id\":71349,\"title\":\"Roc.png\",\"url\":\"\\/wiki\\/File:Roc.png\",\"ns\":6},{\"id\":81277,\"title\":\"Theory and Praxis.png\",\"url\":\"\\/wiki\\/File:Theory_and_Praxis.png\",\"ns\":6},{\"id\":73145,\"title\":\"VasaraPortrait.png\",\"url\":\"\\/wiki\\/File:VasaraPortrait.png\",\"ns\":6},{\"id\":71799,\"title\":\"XC2-Artwork-Dromarch.png\",\"url\":\"\\/wiki\\/File:XC2-Artwork-Dromarch.png\",\"ns\":6},{\"id\":72778,\"title\":\"XC2-Brighid.jpg\",\"url\":\"\\/wiki\\/File:XC2-Brighid.jpg\",\"ns\":6},{\"id\":71793,\"title\":\"XC2-Common-Blade-1.jpg\",\"url\":\"\\/wiki\\/File:XC2-Common-Blade-1.jpg\",\"ns\":6},{\"id\":71794,\"title\":\"XC2-Common-Blade-2.jpg\",\"url\":\"\\/wiki\\/File:XC2-Common-Blade-2.jpg\",\"ns\":6},{\"id\":71795,\"title\":\"XC2-Common-Blade-3.jpg\",\"url\":\"\\/wiki\\/File:XC2-Common-Blade-3.jpg\",\"ns\":6},{\"id\":83743,\"title\":\"XC2-Dolmes Icon.png\",\"url\":\"\\/wiki\\/File:XC2-Dolmes_Icon.png\",\"ns\":6},{\"id\":71780,\"title\":\"XC2-Finch-in-action.jpg\",\"url\":\"\\/wiki\\/File:XC2-Finch-in-action.jpg\",\"ns\":6},{\"id\":71779,\"title\":\"XC2-Finch.jpg\",\"url\":\"\\/wiki\\/File:XC2-Finch.jpg\",\"ns\":6},{\"id\":71895,\"title\":\"XC2-Hotaru.jpg\",\"url\":\"\\/wiki\\/File:XC2-Hotaru.jpg\",\"ns\":6},{\"id\":82561,\"title\":\"XC2-Sever Icon.png\",\"url\":\"\\/wiki\\/File:XC2-Sever_Icon.png\",\"ns\":6},{\"id\":71806,\"title\":\"XC2-Vasara.jpg\",\"url\":\"\\/wiki\\/File:XC2-Vasara.jpg\",\"ns\":6}],\"basepath\":\"https:\\/\\/xenoblade.fandom.com\"}";
    //https://xenoblade.fandom.com/api.php?action=query&format=json&pageids=72205&prop=pageprops
    private static final String SAMPLE_BLADE_INFO = "{\"query\":{\"pages\":{\"72205\":{\"pageid\":72205,\"ns\":0,\"title\":\"Adenine\",\"pageprops\":{\"infoboxes\":\"[{\\\"parser_tag_version\\\":2,\\\"data\\\":[{\\\"type\\\":\\\"title\\\",\\\"data\\\":{\\\"value\\\":\\\"Adenine\\\",\\\"item-name\\\":null,\\\"source\\\":\\\"name\\\"}},{\\\"type\\\":\\\"image\\\",\\\"data\\\":[{\\\"url\\\":\\\"https:\\\\/\\\\/vignette.wikia.nocookie.net\\\\/xenoblade\\\\/images\\\\/5\\\\/5c\\\\/Adenine_Portrait.png\\\\/revision\\\\/latest?cb=20180315203211\\\",\\\"name\\\":\\\"Adenine Portrait.png\\\",\\\"key\\\":\\\"Adenine_Portrait.png\\\",\\\"alt\\\":null,\\\"caption\\\":\\\"Adenine in Xenoblade Chronicles 2\\\",\\\"isVideo\\\":false,\\\"item-name\\\":null,\\\"source\\\":\\\"image\\\"}]},{\\\"type\\\":\\\"group\\\",\\\"data\\\":{\\\"value\\\":[{\\\"type\\\":\\\"data\\\",\\\"data\\\":{\\\"label\\\":\\\"\\\",\\\"value\\\":\\\"\\u003Ca href=\\\\\\\"https:\\\\/\\\\/vignette.wikia.nocookie.net\\\\/xenoblade\\\\/images\\\\/d\\\\/d0\\\\/XC2-element-wind.png\\\\/revision\\\\/latest?cb=20180425030959\\\\\\\" \\\\tclass=\\\\\\\"image image-thumbnail\\\\\\\" \\\\t \\\\t \\\\t\\u003E\\u003Cimg src=\\\\\\\"data:image\\\\/gif;base64,R0lGODlhAQABAIABAAAAAP\\\\/\\\\/\\\\/yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D\\\\\\\" \\\\t alt=\\\\\\\"XC2-element-wind\\\\\\\"  \\\\tclass=\\\\\\\"lzy lzyPlcHld \\\\\\\" \\\\t \\\\tdata-image-key=\\\\\\\"XC2-element-wind.png\\\\\\\" \\\\tdata-image-name=\\\\\\\"XC2-element-wind.png\\\\\\\" \\\\t data-src=\\\\\\\"https:\\\\/\\\\/vignette.wikia.nocookie.net\\\\/xenoblade\\\\/images\\\\/d\\\\/d0\\\\/XC2-element-wind.png\\\\/revision\\\\/latest\\\\/scale-to-width-down\\\\/35?cb=20180425030959\\\\\\\"  \\\\t width=\\\\\\\"35\\\\\\\"  \\\\t height=\\\\\\\"35\\\\\\\"  \\\\t \\\\t \\\\t onload=\\\\\\\"if(typeof ImgLzy===\\u0026#039;object\\u0026#039;){ImgLzy.load(this)}\\\\\\\"  \\\\t\\u003E\\u003Cnoscript\\u003E\\u003Cimg src=\\\\\\\"https:\\\\/\\\\/vignette.wikia.nocookie.net\\\\/xenoblade\\\\/images\\\\/d\\\\/d0\\\\/XC2-element-wind.png\\\\/revision\\\\/latest\\\\/scale-to-width-down\\\\/35?cb=20180425030959\\\\\\\" \\\\t alt=\\\\\\\"XC2-element-wind\\\\\\\"  \\\\tclass=\\\\\\\"\\\\\\\" \\\\t \\\\tdata-image-key=\\\\\\\"XC2-element-wind.png\\\\\\\" \\\\tdata-image-name=\\\\\\\"XC2-element-wind.png\\\\\\\" \\\\t \\\\t width=\\\\\\\"35\\\\\\\"  \\\\t height=\\\\\\\"35\\\\\\\"  \\\\t \\\\t \\\\t \\\\t\\u003E\\u003C\\\\/noscript\\u003E\\u003C\\\\/a\\u003E\\\",\\\"span\\\":1,\\\"layout\\\":null,\\\"item-name\\\":null,\\\"source\\\":\\\"element\\\"}},{\\\"type\\\":\\\"data\\\",\\\"data\\\":{\\\"label\\\":\\\"\\\",\\\"value\\\":\\\"\\u003Ca href=\\\\\\\"https:\\\\/\\\\/vignette.wikia.nocookie.net\\\\/xenoblade\\\\/images\\\\/2\\\\/22\\\\/Rarity2.png\\\\/revision\\\\/latest?cb=20180404020721\\\\\\\" \\\\tclass=\\\\\\\"image image-thumbnail\\\\\\\" \\\\t \\\\t \\\\t\\u003E\\u003Cimg src=\\\\\\\"data:image\\\\/gif;base64,R0lGODlhAQABAIABAAAAAP\\\\/\\\\/\\\\/yH5BAEAAAEALAAAAAABAAEAQAICTAEAOw%3D%3D\\\\\\\" \\\\t alt=\\\\\\\"Rarity2\\\\\\\"  \\\\tclass=\\\\\\\"lzy lzyPlcHld \\\\\\\" \\\\t \\\\tdata-image-key=\\\\\\\"Rarity2.png\\\\\\\" \\\\tdata-image-name=\\\\\\\"Rarity2.png\\\\\\\" \\\\t data-src=\\\\\\\"https:\\\\/\\\\/vignette.wikia.nocookie.net\\\\/xenoblade\\\\/images\\\\/2\\\\/22\\\\/Rarity2.png\\\\/revision\\\\/latest\\\\/scale-to-width-down\\\\/40?cb=20180404020721\\\\\\\"  \\\\t width=\\\\\\\"40\\\\\\\"  \\\\t height=\\\\\\\"30\\\\\\\"  \\\\t \\\\t \\\\t onload=\\\\\\\"if(typeof ImgLzy===\\u0026#039;object\\u0026#039;){ImgLzy.load(this)}\\\\\\\"  \\\\t\\u003E\\u003Cnoscript\\u003E\\u003Cimg src=\\\\\\\"https:\\\\/\\\\/vignette.wikia.nocookie.net\\\\/xenoblade\\\\/images\\\\/2\\\\/22\\\\/Rarity2.png\\\\/revision\\\\/latest\\\\/scale-to-width-down\\\\/40?cb=20180404020721\\\\\\\" \\\\t alt=\\\\\\\"Rarity2\\\\\\\"  \\\\tclass=\\\\\\\"\\\\\\\" \\\\t \\\\tdata-image-key=\\\\\\\"Rarity2.png\\\\\\\" \\\\tdata-image-name=\\\\\\\"Rarity2.png\\\\\\\" \\\\t \\\\t width=\\\\\\\"40\\\\\\\"  \\\\t height=\\\\\\\"30\\\\\\\"  \\\\t \\\\t \\\\t \\\\t\\u003E\\u003C\\\\/noscript\\u003E\\u003C\\\\/a\\u003E\\\",\\\"span\\\":1,\\\"layout\\\":null,\\\"item-name\\\":null,\\\"source\\\":\\\"rarity\\\"}}],\\\"layout\\\":\\\"horizontal\\\",\\\"collapse\\\":null,\\\"row-items\\\":null,\\\"item-name\\\":null}},{\\\"type\\\":\\\"data\\\",\\\"data\\\":{\\\"label\\\":\\\"Gender\\\",\\\"value\\\":\\\"Female\\\",\\\"span\\\":1,\\\"layout\\\":null,\\\"item-name\\\":null,\\\"source\\\":\\\"gender\\\"}},{\\\"type\\\":\\\"data\\\",\\\"data\\\":{\\\"label\\\":\\\"Type\\\",\\\"value\\\":\\\"Humanoid\\\",\\\"span\\\":1,\\\"layout\\\":null,\\\"item-name\\\":null,\\\"source\\\":\\\"type\\\"}},{\\\"type\\\":\\\"data\\\",\\\"data\\\":{\\\"label\\\":\\\"Weapon\\\",\\\"value\\\":\\\"Knuckle Claws\\\",\\\"span\\\":1,\\\"layout\\\":null,\\\"item-name\\\":null,\\\"source\\\":\\\"weapon\\\"}},{\\\"type\\\":\\\"data\\\",\\\"data\\\":{\\\"label\\\":\\\"Role\\\",\\\"value\\\":\\\"Healer\\\",\\\"span\\\":1,\\\"layout\\\":null,\\\"item-name\\\":null,\\\"source\\\":\\\"role\\\"}},{\\\"type\\\":\\\"data\\\",\\\"data\\\":{\\\"label\\\":\\\"Source\\\",\\\"value\\\":\\\"Random \\u003Ca href=\\\\\\\"\\\\/wiki\\\\/Core_Crystal\\\\\\\" title=\\\\\\\"Core Crystal\\\\\\\"\\u003ECore Crystal\\u003C\\\\/a\\u003E\\\",\\\"span\\\":1,\\\"layout\\\":null,\\\"item-name\\\":null,\\\"source\\\":\\\"source\\\"}},{\\\"type\\\":\\\"data\\\",\\\"data\\\":{\\\"label\\\":\\\"Merc Group Title\\\",\\\"value\\\":\\\"Contrarians\\\",\\\"span\\\":1,\\\"layout\\\":null,\\\"item-name\\\":null,\\\"source\\\":\\\"mercgroup\\\"}},{\\\"type\\\":\\\"data\\\",\\\"data\\\":{\\\"label\\\":\\\"Voice actor (EN)\\\",\\\"value\\\":\\\"Laurel Lefkow\\\",\\\"span\\\":1,\\\"layout\\\":null,\\\"item-name\\\":null,\\\"source\\\":\\\"voiceactoreng\\\"}},{\\\"type\\\":\\\"data\\\",\\\"data\\\":{\\\"label\\\":\\\"Voice actor (JP)\\\",\\\"value\\\":\\\"Mamiko Noto\\\",\\\"span\\\":1,\\\"layout\\\":null,\\\"item-name\\\":null,\\\"source\\\":\\\"voiceactorjp\\\"}},{\\\"type\\\":\\\"data\\\",\\\"data\\\":{\\\"label\\\":\\\"Appearances\\\",\\\"value\\\":\\\"\\u003Ci\\u003E\\u003Ca href=\\\\\\\"\\\\/wiki\\\\/Xenoblade_Chronicles_2\\\\\\\" title=\\\\\\\"Xenoblade Chronicles 2\\\\\\\"\\u003EXenoblade Chronicles 2\\u003C\\\\/a\\u003E\\u003C\\\\/i\\u003E\\\",\\\"span\\\":1,\\\"layout\\\":null,\\\"item-name\\\":null,\\\"source\\\":\\\"appearances\\\"}},{\\\"type\\\":\\\"data\\\",\\\"data\\\":{\\\"label\\\":\\\"Illustrator\\\",\\\"value\\\":\\\"K\\\\u014dji Ogata\\\",\\\"span\\\":1,\\\"layout\\\":null,\\\"item-name\\\":null,\\\"source\\\":\\\"illustrator\\\"}}],\\\"metadata\\\":[{\\\"type\\\":\\\"title\\\",\\\"sources\\\":{\\\"name\\\":{\\\"label\\\":\\\"\\\",\\\"primary\\\":true}}},{\\\"type\\\":\\\"image\\\",\\\"sources\\\":{\\\"image\\\":{\\\"label\\\":\\\"\\\",\\\"primary\\\":true},\\\"caption\\\":{\\\"label\\\":\\\"\\\"},\\\"name\\\":{\\\"label\\\":\\\"\\\"}}},{\\\"type\\\":\\\"group\\\",\\\"metadata\\\":[{\\\"type\\\":\\\"data\\\",\\\"sources\\\":{\\\"element\\\":{\\\"label\\\":\\\"\\\",\\\"primary\\\":true}}},{\\\"type\\\":\\\"data\\\",\\\"sources\\\":{\\\"rarity\\\":{\\\"label\\\":\\\"\\\",\\\"primary\\\":true}}}]},{\\\"type\\\":\\\"data\\\",\\\"sources\\\":{\\\"gender\\\":{\\\"label\\\":\\\"Gender\\\",\\\"primary\\\":true}}},{\\\"type\\\":\\\"data\\\",\\\"sources\\\":{\\\"type\\\":{\\\"label\\\":\\\"Type\\\",\\\"primary\\\":true}}},{\\\"type\\\":\\\"data\\\",\\\"sources\\\":{\\\"weapon\\\":{\\\"label\\\":\\\"Weapon\\\",\\\"primary\\\":true}}},{\\\"type\\\":\\\"data\\\",\\\"sources\\\":{\\\"role\\\":{\\\"label\\\":\\\"Role\\\",\\\"primary\\\":true}}},{\\\"type\\\":\\\"data\\\",\\\"sources\\\":{\\\"source\\\":{\\\"label\\\":\\\"Source\\\",\\\"primary\\\":true}}},{\\\"type\\\":\\\"data\\\",\\\"sources\\\":{\\\"mercgroup\\\":{\\\"label\\\":\\\"Merc Group Title\\\",\\\"primary\\\":true}}},{\\\"type\\\":\\\"data\\\",\\\"sources\\\":{\\\"voiceactoreng\\\":{\\\"label\\\":\\\"Voice actor (EN)\\\",\\\"primary\\\":true}}},{\\\"type\\\":\\\"data\\\",\\\"sources\\\":{\\\"voiceactorjp\\\":{\\\"label\\\":\\\"Voice actor (JP)\\\",\\\"primary\\\":true}}},{\\\"type\\\":\\\"data\\\",\\\"sources\\\":{\\\"appearances\\\":{\\\"label\\\":\\\"Appearances\\\",\\\"primary\\\":true}}},{\\\"type\\\":\\\"data\\\",\\\"sources\\\":{\\\"illustrator\\\":{\\\"label\\\":\\\"Illustrator\\\",\\\"primary\\\":true}}}]}]\",\"words_count\":\"1804\"}}}}}";
    //https://xenoblade.fandom.com/api/v1/Articles/Details?ids=72205
    private static final String SAMPLE_BLADE = "{\"items\":{\"72205\":{\"id\":72205,\"title\":\"Adenine\",\"ns\":0,\"url\":\"\\/wiki\\/Adenine\",\"revision\":{\"id\":406665,\"user\":\"Drahnian\",\"user_id\":24967994,\"timestamp\":\"1552422832\"},\"comments\":7,\"type\":\"article\",\"abstract\":\"Adenine (Japanese: \\u30b7\\u30ad, Shiki) is a Rare Blade in Xenoblade Chronicles 2. She can grant...\",\"thumbnail\":\"https:\\/\\/vignette.wikia.nocookie.net\\/xenoblade\\/images\\/f\\/f7\\/XC2_Adenine_Artwork.png\\/revision\\/latest\\/window-crop\\/width\\/200\\/x-offset\\/0\\/y-offset\\/0\\/window-width\\/768\\/window-height\\/768?cb=20190203040017\",\"original_dimensions\":{\"width\":768,\"height\":1250}}},\"basepath\":\"https:\\/\\/xenoblade.fandom.com\"}";


    @Override
    public String toString() {
        return "Blade{" +
                "title='" + title + '\'' +
                ", textAbstract='" + textAbstract + '\'' +
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

    public String getTitle() {
        return title;
    }

    Blade setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTextAbstract() {
        return textAbstract;
    }

    Blade setTextAbstract(String textAbstract) {
        this.textAbstract = textAbstract;
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

    Blade parse_sample() {
        return parse(SAMPLE_BLADE_INFO, SAMPLE_BLADE);
    }

    /**
     * Finds the image from the url and returns it.
     * @return A drawable to use for the portrait
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

    //See: http://www.tutorialspoint.com/android/android_json_parser.htm
    Blade parse(String rawInfo, String rawDetails) {
        try {
            JSONObject root = new JSONObject(rawDetails);
            JSONObject items = root.getJSONObject("items");
            JSONObject number = items.getJSONObject(items.keys().next());
            setTitle(number.getString("title"));
            setTextAbstract(number.getString("abstract"));
            setUrlArt(number.getString("thumbnail"));
            setUrlPage(root.getString("basepath") + number.getString("url"));

            JSONObject pages = new JSONObject(rawInfo)
                    .getJSONObject("query")
                    .getJSONObject("pages");
            JSONArray data = new JSONArray(
                    pages.getJSONObject(pages.keys().next())
                            .getJSONObject("pageprops")
                            .getString("infoboxes"))
                    .getJSONObject(0)
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
        } catch (
                JSONException e) {
            Log.e("QueryUtils", "Problem parsing the Blade JSON results", e);
        }
        return this;
    }
}
