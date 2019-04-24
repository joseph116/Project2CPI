package com.example.appname.controller.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;

import com.example.appname.R;
import com.example.appname.controller.folders.FoldersFragment;
import com.example.appname.controller.home.HomeFragment;
import com.example.appname.controller.settings.SettingsFragment;
import com.example.appname.controller.sort.SortFragment;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends AppCompatActivity {

    //==============================================================================================
    //  ATTRIBUTES
    //==============================================================================================



    //==============================================================================================
    //  STATE FUNCTIONS
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigationBar();
    }

    //==============================================================================================
    //  INIT FUNCTIONS
    //==============================================================================================
    private void initNavigationBar() {
        final NavigationTabBar BottomNavigationBar = findViewById(R.id.BottomNavigationBar);
        final ArrayList<NavigationTabBar.Model> tabs = new ArrayList<>();
        final String[] colors = getResources().getStringArray(R.array.custom_colors);
        tabs.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_home),
                        Color.parseColor(colors[0])
                ).title("Home")
                        .badgeTitle("News")
                        .build()
        );
        tabs.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_folder),
                        Color.parseColor(colors[0])
                ).title("Folders")
                        .badgeTitle("none")
                        .build()
        );
        tabs.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_sort),
                        Color.parseColor(colors[0])
                ).title("Sort")
                        .badgeTitle("new images")
                        .build()
        );
        tabs.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_settings),
                        Color.parseColor(colors[0])
                ).title("Settings")
                        .badgeTitle("updates")
                        .build()
        );

        ViewPager viewPager = findViewById(R.id.viewpager_navigation);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position)
                {
                    case 0:
                        return new HomeFragment();
                    case 1:
                        return new FoldersFragment();
                    case 2:
                        return new SortFragment();
                    case 3:
                        return new SettingsFragment();
                }
                return null;
            }

            @Override
            public int getCount() {
                return 4;
            }
        });
        BottomNavigationBar.setViewPager(viewPager);

        BottomNavigationBar.setModels(tabs);
        BottomNavigationBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);
        BottomNavigationBar.setBadgeGravity(NavigationTabBar.BadgeGravity.BOTTOM);
        BottomNavigationBar.setBadgePosition(NavigationTabBar.BadgePosition.CENTER);
        BottomNavigationBar.setTypeface("fonts/custom_font.ttf");
        BottomNavigationBar.setIsBadged(true);
        BottomNavigationBar.setIsTitled(true);
        BottomNavigationBar.setIsTinted(true);
        BottomNavigationBar.setIsBadgeUseTypeface(true);
        BottomNavigationBar.setBadgeBgColor(Color.RED);
        BottomNavigationBar.setBadgeTitleColor(Color.WHITE);
        BottomNavigationBar.setIsSwiped(true);
        BottomNavigationBar.setBgColor(Color.WHITE);
        BottomNavigationBar.setBadgeSize(10);
        BottomNavigationBar.setTitleSize(24);
        BottomNavigationBar.setIconSizeFraction((float) 0.5);

    }

}
