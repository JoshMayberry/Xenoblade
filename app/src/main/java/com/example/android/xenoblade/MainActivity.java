package com.example.android.xenoblade;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.xenoblade.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //See: https://codelabs.developers.google.com/codelabs/android-databinding/index.html?index=..%2F..index#4
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //See: Tabs and ViewPager (Android Development Patterns Ep 9) - https://www.youtube.com/watch?v=zQekzaAgIlQ
        ViewPager viewPager = findViewById(R.id.viewPager);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), this);
        binding.viewPager.setAdapter(adapter);

        //See: https://www.youtube.com/watch?v=zQekzaAgIlQ
        //See: https://android-developers.googleblog.com/2015/05/android-design-support-library.html
        //See: https://guides.codepath.com/android/google-play-style-tabs-using-tablayout#sliding-tabs-layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
