package com.example.appname.View.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.appname.R;
import com.example.appname.View.folders.FoldersFragment;
import com.example.appname.View.home.HomeFragment;
import com.example.appname.View.settings.SettingsFragment;
import com.example.appname.View.sort.SortFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    //==============================================================================================
    //  ATTRIBUTES
    //==============================================================================================

    private ViewPager mViewPagerNavigation;
    private Toast mToast = null;
    private BackPressedListener mBackPressedListener;

    //==============================================================================================
    //  STATE FUNCTIONS
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //request for permissions
        if (!canAccessExternalSd()){
            Intent askForPerms = new Intent(MainActivity.this, RequestForPermissionActivity.class);
            startActivity(askForPerms);
        }

        setContentView(R.layout.activity_main);
        initNavBar();
    }

    //==============================================================================================
    //  INIT FUNCTIONS
    //==============================================================================================


    private void initNavBar() {
        BottomNavigationView bottomNavBar = findViewById(R.id.bottomNavBar);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                new HomeFragment()).commit();
        bottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragmennt = null;

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragmennt = new HomeFragment();
                        break;
                    case R.id.nav_folders:
                        selectedFragmennt = new FoldersFragment();
                        break;
                    case R.id.nav_sort:
                        selectedFragmennt = new SortFragment();
                        break;
                    case R.id.nav_settings:
                        selectedFragmennt = new SettingsFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        selectedFragmennt).commit();
                return true;
            }
        });
    }

    //==============================================================================================
    //  FUNCTIONS
    //==============================================================================================

    private void showToast(String message) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    //==============================================================================================
    //  LISTENERS FUNCTIONS
    //==============================================================================================


    @Override
    public void onBackPressed() {
        int currentItem = mViewPagerNavigation.getCurrentItem();
        switch (currentItem) {
            case 0:
                showToast("Backed in home page!");
                break;
            case 1:
                mBackPressedListener.onBackPressed();
                break;
            case 2:
                mBackPressedListener.onBackPressed();
                break;
            case 3:
                break;
            default:
                super.onBackPressed();
                break;
        }
    }

    public void setBackListener(BackPressedListener listener){
        mBackPressedListener = listener;
    }

    public interface BackPressedListener{
        void onBackPressed();
    }

    //==============================================================================================
    //  FOR PERMISSION
    //==============================================================================================

    public boolean canAccessExternalSd() {
        return (hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));
    }

}
