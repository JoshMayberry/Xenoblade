package com.example.android.xenoblade;

import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
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
//
//        ArrayList<Blade> bladeList = QueryUtilities.getBlades();
//        for (Blade blade: bladeList) {
//            Log.e("MainActivity", "blade: " + blade);
//        }
//
//        binding.list.setAdapter(new BladeAdapter(this, bladeList));





//        ArrayList<Location> locationList = QueryUtilities.getLocations();
//        for (Location location: locationList) {
//            Log.e("MainActivity", "location: " + location);
//        }















        //See: Tabs and ViewPager (Android Development Patterns Ep 9) - https://www.youtube.com/watch?v=zQekzaAgIlQ
        ViewPager viewPager = findViewById(R.id.viewpager);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), this);
//        binding.viewPager.setAdapter(adapter);

        //See: https://www.youtube.com/watch?v=zQekzaAgIlQ
        //See: https://guides.codepath.com/android/google-play-style-tabs-using-tablayout#sliding-tabs-layout
        //See: https://android-developers.googleblog.com/2015/05/android-design-support-library.html?utm_source=udacity&utm_medium=course&utm_campaign=android_basics
        //See: https://developer.android.com/topic/libraries/support-library/?utm_campaign=android_basics&utm_medium=course&utm_source=udacity
        //See: https://android-developers.googleblog.com/?utm_source=udacity&utm_medium=course&utm_campaign=android_basics
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
