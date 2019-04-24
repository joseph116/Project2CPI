package com.example.appname;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

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
                        .badgeTitle("NTB")
                        .build()
        );
        tabs.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_folder),
                        Color.parseColor(colors[0])
                ).title("Folders")
                        .badgeTitle("with")
                        .build()
        );
        tabs.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_sort),
                        Color.parseColor(colors[0])
                ).title("Sort")
                        .badgeTitle("state")
                        .build()
        );
        tabs.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_settings),
                        Color.parseColor(colors[0])
                ).title("Settings")
                        .badgeTitle("icon")
                        .build()
        );

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
        BottomNavigationBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
                switch (index) {
                    case 1:
                        //show home
                        break;
                    case 2:
                        //show folder
                        break;
                    case 3:
                        //show sort
                        break;
                    case 4:
                        //show settings
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onEndTabSelected(NavigationTabBar.Model model, int index) {

            }
        });
    }

}
