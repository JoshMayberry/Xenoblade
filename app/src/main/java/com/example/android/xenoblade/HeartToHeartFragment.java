package com.example.android.xenoblade;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class HeartToHeartFragment extends GenericFragment<HeartToHeart> {
    public HeartToHeartFragment() {
        super();
        position = 2;
    }

    @Override
    List<HeartToHeart> getOffline(Context context) {
        List<HeartToHeart> eventList = new ArrayList<>();

        eventList.add(new HeartToHeart()
                .setTitle(context.getString(R.string.event_offline_1_title))
                .setSubText(context.getString(R.string.event_offline_1_subText)));

        eventList.add(new HeartToHeart()
                .setTitle(context.getString(R.string.event_offline_2_title))
                .setSubText(context.getString(R.string.event_offline_2_subText)));

        eventList.add(new HeartToHeart()
                .setTitle(context.getString(R.string.event_offline_3_title))
                .setSubText(context.getString(R.string.event_offline_3_subText)));

        eventList.add(new HeartToHeart()
                .setTitle(context.getString(R.string.event_offline_4_title))
                .setSubText(context.getString(R.string.event_offline_4_subText)));

        eventList.add(new HeartToHeart()
                .setTitle(context.getString(R.string.event_offline_5_title))
                .setSubText(context.getString(R.string.event_offline_5_subText)));

        eventList.add(new HeartToHeart()
                .setTitle(context.getString(R.string.event_offline_6_title))
                .setSubText(context.getString(R.string.event_offline_6_subText)));

        eventList.add(new HeartToHeart()
                .setTitle(context.getString(R.string.event_offline_7_title))
                .setSubText(context.getString(R.string.event_offline_7_subText)));

        eventList.add(new HeartToHeart()
                .setTitle(context.getString(R.string.event_offline_8_title))
                .setSubText(context.getString(R.string.event_offline_8_subText)));

        return eventList;
    }
}
