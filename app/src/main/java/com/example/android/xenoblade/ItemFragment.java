package com.example.android.xenoblade;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ItemFragment extends BaseFragment<Item> {
    public ItemFragment() {
        super();
        position = 3;
    }

    @Override
    List<Item> getOffline(Context context) {
        List<Item> itemList = new ArrayList<>();

        itemList.add(new Item()
                .setTitle(context.getString(R.string.item_offline_1_title))
                .setSubText(context.getString(R.string.item_offline_1_subText)));

        itemList.add(new Item()
                .setTitle(context.getString(R.string.item_offline_2_title))
                .setSubText(context.getString(R.string.item_offline_2_subText)));

        itemList.add(new Item()
                .setTitle(context.getString(R.string.item_offline_3_title))
                .setSubText(context.getString(R.string.item_offline_3_subText)));

        itemList.add(new Item()
                .setTitle(context.getString(R.string.item_offline_4_title))
                .setSubText(context.getString(R.string.item_offline_4_subText)));

        itemList.add(new Item()
                .setTitle(context.getString(R.string.item_offline_5_title))
                .setSubText(context.getString(R.string.item_offline_5_subText)));

        itemList.add(new Item()
                .setTitle(context.getString(R.string.item_offline_6_title))
                .setSubText(context.getString(R.string.item_offline_6_subText)));

        itemList.add(new Item()
                .setTitle(context.getString(R.string.item_offline_7_title))
                .setSubText(context.getString(R.string.item_offline_7_subText)));

        itemList.add(new Item()
                .setTitle(context.getString(R.string.item_offline_8_title))
                .setSubText(context.getString(R.string.item_offline_8_subText)));

        itemList.add(new Item()
                .setTitle(context.getString(R.string.item_offline_9_title))
                .setSubText(context.getString(R.string.item_offline_9_subText)));

        itemList.add(new Item()
                .setTitle(context.getString(R.string.item_offline_10_title))
                .setSubText(context.getString(R.string.item_offline_10_subText)));

        return itemList;
    }
}
