package com.example.android.xenoblade;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.xenoblade.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //See: https://codelabs.developers.google.com/codelabs/android-databinding/index.html?index=..%2F..index#4
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //See: Tabs and ViewPager (Android Development Patterns Ep 9) - https://www.youtube.com/watch?v=zQekzaAgIlQ
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), this);
        binding.viewPager.setAdapter(adapter);

        //See: https://www.youtube.com/watch?v=zQekzaAgIlQ
        //See: https://android-developers.googleblog.com/2015/05/android-design-support-library.html
        //See: https://guides.codepath.com/android/google-play-style-tabs-using-tablayout#sliding-tabs-layout
        binding.tabs.setupWithViewPager(binding.viewPager);
    }

    /**
     * Uses {@link ContainerUtilities#orderList} to organize the TabLayout structure.
     */
    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        final String LOG_TAG = MyFragmentPagerAdapter.class.getSimpleName();

        private Context context;

        MyFragmentPagerAdapter(FragmentManager fragmentManager, Context context) {
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
}
