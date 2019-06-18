package com.example.appname.View.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.appname.Model.Explorer;
import com.example.appname.Model.Image;
import com.example.appname.R;
import com.example.appname.View.folders.FoldersFragment;
import com.example.appname.View.home.HomeFragment;
import com.example.appname.View.sort.SortActivity;
import com.example.appname.ViewModel.ImageViewModel;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{

    //==============================================================================================
    //  ATTRIBUTES
    //==============================================================================================

    public static final int  SETTINGS_ACTION = 1;
    public static final String EXTRA_UNSORTED_LIST = "EXTRA_UNSORTED_LIST";

    private Toolbar mToolbar;
    private Toast mToast = null;
    private BackPressedListener mBackPressedListener;
    private ImageViewModel mImageViewModel;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private SearchView mSearchView;
    private Explorer mExplorer;

    private static final String[] IMAGE_PROJECTION =
            new String[] {
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.DATE_MODIFIED,
                    MediaStore.Images.ImageColumns.MIME_TYPE,
                    MediaStore.Images.ImageColumns.ORIENTATION,
            };

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
        mImageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        //inits
        setContentView(R.layout.activity_main);
        initViews();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
            mNavigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (canAccessExternalSd()) {
            loadUnsortedImages();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view:
                //open change view dialog here
                break;
            case R.id.action_scan:
                //call explorer scan function
                new scanImagesAsyncTask().execute(mExplorer.getRootPath());
                break;
            case R.id.action_search:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SETTINGS_ACTION) {
            if (resultCode == SettingsFragment.RESULT_CODE_THEME_UPDATED) {
                finish();
                startActivity(getIntent());
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_bar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setQueryHint("Search in home...");
        return true;
    }

    //==============================================================================================
    //  INIT FUNCTIONS
    //==============================================================================================

    private void loadUnsortedImages() {
        //mImageViewModel.startLoading();
    }

    private void initViews() {
        mToolbar = findViewById(R.id.app_bar);
        mToolbar.inflateMenu(R.menu.app_bar_menu);
        setSupportActionBar(mToolbar);

        mExplorer = new Explorer(this);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_folders:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new FoldersFragment()).commit();
                        mSearchView.setQueryHint("Search in folders...");
                        break;
                    case R.id.nav_settings:
                        Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivityForResult(settings, SETTINGS_ACTION);
                        break;
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
                        mSearchView.setQueryHint("Search in home...");
                        break;
                    case R.id.nav_sort:
                        Intent intent = new Intent(MainActivity.this, SortActivity.class);
                        intent.putParcelableArrayListExtra(EXTRA_UNSORTED_LIST, mImageViewModel.getUnsortedImages());
                        startActivity(intent);
                        break;
                    case R.id.nav_trash:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new TrashFragment()).commit();
                        mSearchView.setQueryHint("Search in trash...");
                        break;
                    case R.id.nav_tag:
                        showToast("Working on...");
                        break;

                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sharedPreferences.getString("theme", "1");
        switch (theme) {
            case "1":
                setTheme(R.style.Theme1);
                break;
            case "2":
                setTheme(R.style.Theme2);
                break;
            case "3":
                setTheme(R.style.Theme3);
                break;
            case "4":
                setTheme(R.style.Theme4);
                break;
            case "5":
                setTheme(R.style.Theme5);
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

    private class scanImagesAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = getContentResolver().query(
                    contentUri, IMAGE_PROJECTION,
                    null, null,
                    MediaStore.Images.Media.DEFAULT_SORT_ORDER);

            //if there is no image
            if (cursor == null) return null;

            //else get the images
            try {
                final int idColNum = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
                final int pathColNum = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA);
                final int dateTakenColNum = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN);
                final int dateModifiedColNum = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_MODIFIED);
                final int mimeTypeColNum = cursor.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE);
                final int orientationColNum = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);

                while (cursor.moveToNext()) {
                    String path = cursor.getString(pathColNum);
                    //path.substring(0, path.lastIndexOf(File.separator)).equals(strings[0])
                    if (path.contains(strings[0])) {
                        long id = cursor.getLong(idColNum);
                        long dateTaken = cursor.getLong(dateTakenColNum);
                        String mimeType = cursor.getString(mimeTypeColNum);
                        long dateModified = cursor.getLong(dateModifiedColNum);
                        int orientation = cursor.getInt(orientationColNum);

                        Image image = new Image(id, Uri.withAppendedPath(contentUri, Long.toString(id)),
                                path,mimeType, dateTaken, dateModified, orientation);

                        mImageViewModel.add(image);
                    }
                }
            } finally {
                cursor.close();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showToast("scan finished");
        }
    }

    //==============================================================================================
    //  LISTENERS FUNCTIONS
    //==============================================================================================
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        mBackPressedListener.onBackPressed();
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
