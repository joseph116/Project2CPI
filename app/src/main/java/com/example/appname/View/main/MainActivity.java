package com.example.appname.View.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;
import com.example.appname.R;
import com.example.appname.View.folders.FoldersFragment;
import com.example.appname.View.home.HomeFragment;
import com.example.appname.View.settings.SettingsFragment;
import com.example.appname.View.sort.SortFragment;
import com.example.appname.ViewModel.ImageViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity{

    //==============================================================================================
    //  ATTRIBUTES
    //==============================================================================================

    private Toolbar mToolbar;
    private Toast mToast = null;
    private BackPressedListener mBackPressedListener;
    private BottomNavigationView mBottomNavBar;
    private ImageViewModel mImageViewModel;
    private Fragment mSelectedFragment;


    //==============================================================================================
    //  STATE FUNCTIONS
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadPreferences();
        super.onCreate(savedInstanceState);
        //request for permissions
        if (!canAccessExternalSd()){
            Intent askForPerms = new Intent(MainActivity.this, RequestForPermissionActivity.class);
            startActivity(askForPerms);
        }

        //inits
        setContentView(R.layout.activity_main);
        initToolBar();
        initNavBar();
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (canAccessExternalSd()) {
            loadUnsortedImages();
        }
    }

    //==============================================================================================
    //  INIT FUNCTIONS
    //==============================================================================================


    private void initNavBar() {
        mBottomNavBar = findViewById(R.id.bottomNavBar);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                new HomeFragment()).commit();
        mBottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        if (!(mSelectedFragment instanceof HomeFragment)){
                            mSelectedFragment = new HomeFragment();
                        }
                        break;
                    case R.id.nav_folders:
                        if (!(mSelectedFragment instanceof FoldersFragment)){
                            mSelectedFragment = new FoldersFragment();
                        }
                        break;
                    case R.id.nav_sort:
                        if (!(mSelectedFragment instanceof SortFragment)){
                            mSelectedFragment = new SortFragment();
                        }
                        break;
                    case R.id.nav_settings:
                        if (!(mSelectedFragment instanceof SettingsFragment)){
                            mSelectedFragment = new SettingsFragment();
                        }
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        mSelectedFragment).addToBackStack("back stack").commit();
                return true;
            }
        });
    }

    private void loadUnsortedImages() {
        mImageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        mImageViewModel.startLoading();
    }

    private void initToolBar() {
        mToolbar = findViewById(R.id.app_bar);
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sharedPreferences.getString("theme", "1");
        showToast("theme: " + theme );
        switch (theme) {
            case "1":
                setTheme(R.style.MainTheme);
                break;
            case "2":
                setTheme(R.style.MainThemeOld);
            default:
                break;
        }
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
        int currentItemId = mBottomNavBar.getSelectedItemId();
        switch (currentItemId) {
            case R.id.nav_home:
                showToast("Backed in home page!");
                break;
            case R.id.nav_folders:
                mBackPressedListener.onBackPressed();
                break;
            case R.id.nav_sort:
                mBackPressedListener.onBackPressed();
                break;
            case R.id.nav_settings:
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
