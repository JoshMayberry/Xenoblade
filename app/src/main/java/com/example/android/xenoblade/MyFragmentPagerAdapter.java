package com.example.android.xenoblade;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    public static final String LOG_TAG = MyFragmentPagerAdapter.class.getName();

    public MyFragmentPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return ContainerUtilities.orderList.get(position).fragment.setPosition(position);
    }

    @Override
    public int getCount() {
        return ContainerUtilities.orderList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(ContainerUtilities.orderList.get(position).title);
    }
}
