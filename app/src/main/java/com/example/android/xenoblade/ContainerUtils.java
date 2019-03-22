package com.example.android.xenoblade;

import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ContainerUtils {
    private static final String LOG_TAG = QueryUtilities.class.getSimpleName();

    private ContainerUtils() {
    }

    static class Group {
        int title = -1;
        BaseFragment fragment = null;
        String urlJsonList = null;
        Class cls = null;

        Group setTitle(int title) {
            this.title = title;
            return this;
        }

        Group setFragment(BaseFragment fragment) {
            this.fragment = fragment;
            return this;
        }

        Group setUrlJsonList(String urlJsonList) {
            this.urlJsonList = urlJsonList;
            return this;
        }

        Group setCls(Class cls) {
            this.cls = cls;
            return this;
        }

        Object newInstance() {
            try {
                return cls.newInstance();
            } catch (IllegalAccessException|InstantiationException error) {
                Log.e(LOG_TAG, "New Instance Error", error);
                return null;
            }
        }
    }

    static final List<Group> orderList = new ArrayList<Group>() {
        {
            add(new Group()
                    .setCls(Blade.class)
                    .setTitle(R.string.blade_title)
                    .setFragment(new BladeFragment())
                    .setUrlJsonList("https://xenoblade.fandom.com/api/v1/Articles/List?category=Blades&limit=1000"));

            add(new Group()
                    .setCls(Location.class)
                    .setTitle(R.string.location_title)
                    .setFragment(new LocationFragment())
                    .setUrlJsonList("https://xenoblade.fandom.com/api/v1/Articles/List?category=XC2_Locations&limit=1000"));
        }
    };
}
