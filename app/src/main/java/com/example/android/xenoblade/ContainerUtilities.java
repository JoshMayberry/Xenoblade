package com.example.android.xenoblade;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * A Utility class that is used to organize and get pre-initialization information
 * about children of {@link BaseContainer}.
 */
class ContainerUtilities {
    private static final String LOG_TAG = QueryUtilities.class.getSimpleName();

    private ContainerUtilities() {
    }

    /**
     * Contains information needed before an instance is created.
     * Instances of this are stored in the {@link #orderList}.
     */
    static class Group {
        int title = R.string.unknown_error;
        int error = R.string.unknown_error;
        BaseFragment fragment = null;
        String urlJsonList = null;
        Class cls = null;

        Group setTitle(int title) {
            this.title = title;
            return this;
        }

        Group setError(int error) {
            this.error = error;
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
            } catch (IllegalAccessException | InstantiationException error) {
                Log.e(LOG_TAG, "New Instance Error", error);
                return null;
            }
        }
    }

    /**
     * This list is used throughout the program to access the {@link Group} instances.
     * To add another category to the TabLayout, add it to this list.
     * The list order will define the tab order.
     */
    static final List<Group> orderList = new ArrayList<Group>() {
        {
            add(new Group()
                    .setCls(Blade.class)
                    .setTitle(R.string.blade_title)
                    .setError(R.string.blade_error)
                    .setFragment(new BaseFragment<Blade>())
                    .setUrlJsonList("https://xenoblade.fandom.com/api/v1/Articles/List?category=Blades&limit=1000"));

            add(new Group()
                    .setCls(Location.class)
                    .setTitle(R.string.location_title)
                    .setError(R.string.location_error)
                    .setFragment(new BaseFragment<Location>())
                    .setUrlJsonList("https://xenoblade.fandom.com/api/v1/Articles/List?category=XC2_Locations&limit=1000"));

            add(new Group()
                    .setCls(HeartToHeart.class)
                    .setTitle(R.string.heart_title)
                    .setError(R.string.heart_error)
                    .setFragment(new BaseFragment<HeartToHeart>())
                    .setUrlJsonList("https://xenoblade.fandom.com/api/v1/Articles/List?category=XC2_Heart-to-Hearts_by_Character&limit=1000"));

            add(new Group()
                    .setCls(Item.class)
                    .setTitle(R.string.item_title)
                    .setError(R.string.item_error)
                    .setFragment(new BaseFragment<Item>())
                    .setUrlJsonList("https://xenoblade.fandom.com/api/v1/Articles/List?category=XC2_Pouch_Items&limit=1000"));
        }
    };
}
