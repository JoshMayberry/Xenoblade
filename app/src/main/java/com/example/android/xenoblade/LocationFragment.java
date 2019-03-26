package com.example.android.xenoblade;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class LocationFragment extends GenericFragment<Location> {
    public LocationFragment() {
        super();
        position = 1;
    }

    @Override
    List<Location> getOffline(Context context) {
        List<Location> locationList = new ArrayList<>();

        locationList.add(new Location()
                .setTitle(context.getString(R.string.location_offline_1_title))
                .setSubText(context.getString(R.string.location_offline_1_subText)));

        locationList.add(new Location()
                .setTitle(context.getString(R.string.location_offline_2_title))
                .setSubText(context.getString(R.string.location_offline_2_subText)));

        locationList.add(new Location()
                .setTitle(context.getString(R.string.location_offline_3_title))
                .setSubText(context.getString(R.string.location_offline_3_subText)));

        locationList.add(new Location()
                .setTitle(context.getString(R.string.location_offline_4_title))
                .setSubText(context.getString(R.string.location_offline_4_subText)));

        locationList.add(new Location()
                .setTitle(context.getString(R.string.location_offline_5_title))
                .setSubText(context.getString(R.string.location_offline_5_subText)));

        locationList.add(new Location()
                .setTitle(context.getString(R.string.location_offline_6_title))
                .setSubText(context.getString(R.string.location_offline_6_subText)));

        locationList.add(new Location()
                .setTitle(context.getString(R.string.location_offline_7_title))
                .setSubText(context.getString(R.string.location_offline_7_subText)));

        locationList.add(new Location()
                .setTitle(context.getString(R.string.location_offline_8_title))
                .setSubText(context.getString(R.string.location_offline_8_subText)));

        locationList.add(new Location()
                .setTitle(context.getString(R.string.location_offline_9_title))
                .setSubText(context.getString(R.string.location_offline_9_subText)));

        locationList.add(new Location()
                .setTitle(context.getString(R.string.location_offline_10_title))
                .setSubText(context.getString(R.string.location_offline_10_subText)));

        return locationList;
    }
}
