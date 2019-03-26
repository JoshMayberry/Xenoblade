package com.example.android.xenoblade;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class BladeFragment extends BaseFragment<Blade> {
    public BladeFragment() {
        super();
        position = 0;
    }

    @Override
    List<Blade> getOffline(Context context) {
        List<Blade> bladeList = new ArrayList<>();

        bladeList.add(new Blade()
                .setImage(R.drawable.image_offline_1)
                .setTitle(context.getString(R.string.blade_offline_1_title)));

        bladeList.add(new Blade()
                .setImage(R.drawable.image_offline_2)
                .setTitle(context.getString(R.string.blade_offline_2_title)));

        bladeList.add(new Blade()
                .setImage(R.drawable.image_offline_3)
                .setTitle(context.getString(R.string.blade_offline_3_title)));

        bladeList.add(new Blade()
                .setImage(R.drawable.image_offline_4)
                .setTitle(context.getString(R.string.blade_offline_4_title)));

        bladeList.add(new Blade()
                .setImage(R.drawable.image_offline_5)
                .setTitle(context.getString(R.string.blade_offline_5_title)));

        bladeList.add(new Blade()
                .setImage(R.drawable.image_offline_6)
                .setTitle(context.getString(R.string.blade_offline_6_title)));

        bladeList.add(new Blade()
                .setImage(R.drawable.image_offline_7)
                .setTitle(context.getString(R.string.blade_offline_7_title)));

        bladeList.add(new Blade()
                .setImage(R.drawable.image_offline_8)
                .setTitle(context.getString(R.string.blade_offline_8_title)));

        return bladeList;
    }
}
