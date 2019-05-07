package com.example.appname.View.sort;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.appname.Model.Image;
import com.example.appname.Model.LoadUnsortedImagesTask;
import com.example.appname.R;
import com.example.appname.View.dialogs.NewFolderDialog;
import com.example.appname.Model.Explorer;
import com.example.appname.View.main.MainActivity;
import com.example.appname.ViewModel.ImageViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.appname.View.main.MainActivity.EXTRA_UNSORTED_LIST;


public class SortActivity extends AppCompatActivity implements FolderAdapter.OnFileItemListener,
        NewFolderDialog.DialogListener,
        ImagePagerAdapter.PagerViewListener,
        LoadUnsortedImagesTask.OnLoadCompleteListener {

    //==============================================================================================
    //  ATTRIBUTES
    //==============================================================================================

    private RecyclerView mFolderRecyclerView;
    private ViewPager mViewPager;
    private ImagePagerAdapter mImagePagerAdapter;
    private Explorer mExplorer;
    private FolderAdapter mFolderAdapter;
    private TextView mPathTextView;
    private ImageButton mBackButton;
    private ImageButton mBackToRoot;
    private Toolbar mToolbar;
    private List<Image> mUnsortedList;
    private ImageViewModel mViewModel;
    private Toast mToast;

    //==============================================================================================
    //  STATE FUNCTIONS
    //==============================================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        loadPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sort);
        if (getIntent().getExtras() != null) {
            mUnsortedList = getIntent().getParcelableArrayListExtra(EXTRA_UNSORTED_LIST);
        }
        mViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        mViewModel.startLoading(this);
        mExplorer = new Explorer(this);
        initViews();
    }

    //==============================================================================================
    //  INIT FUNCTIONS
    //==============================================================================================

    private void initViews() {
        //Recycler View
        mFolderRecyclerView = findViewById(R.id.sortRecyclerView);
        mFolderAdapter = new FolderAdapter(this, mExplorer.getFolders(), this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mFolderRecyclerView.setLayoutManager(gridLayoutManager);
        mFolderRecyclerView.setAdapter(mFolderAdapter);

        //Pager View
        mViewPager = findViewById(R.id.sortViewPager);
        mImagePagerAdapter = new ImagePagerAdapter(this,this);
        mViewPager.setAdapter(mImagePagerAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mUnsortedList.size()) {
                    mFolderAdapter.hideInsertButton(true);
                } else {
                    mFolderAdapter.hideInsertButton(false);
                }
                getSupportActionBar().setTitle(mViewPager.getCurrentItem() + "/" + mUnsortedList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Path TextView
        mPathTextView = findViewById(R.id.sortPathText);
        String currentPath = mExplorer.getCurrentPath();
        String rootPath = mExplorer.getRootPath();
        mPathTextView.setText(currentPath.replace(rootPath, "Sorted Pictures"));

        //Back Button
        mBackButton = findViewById(R.id.sortBackButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToParent();
            }
        });

        //Back to root button
        mBackToRoot = findViewById(R.id.sortBackToRoot);
        mBackToRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToRoot();
            }
        });

        //New folder button
        ImageButton newFolderButton = findViewById(R.id.newFolderButtonSort);
        newFolderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewFolderDialog dialog = new NewFolderDialog(SortActivity.this);
                dialog.show(getSupportFragmentManager(), "new folder dialog");
            }
        });

        mToolbar = findViewById(R.id.toolbar_sort);
        mToolbar.inflateMenu(R.menu.sort_menu);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (mUnsortedList != null) {
            getSupportActionBar().setTitle(mViewPager.getCurrentItem() + "/" + mUnsortedList.size());
        } else {
            getSupportActionBar().setTitle("Loading...");
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_delete:
                if (!mUnsortedList.isEmpty()) {
                    int position = mViewPager.getCurrentItem();
                    Image image = mUnsortedList.get(position);
                    image.setInTrash(true);
                    mViewModel.add(image);
                    mUnsortedList.remove(position);
                    mImagePagerAdapter.removeImage(image);
                    mFolderAdapter.notifyDataSetChanged();
                    mViewPager.setCurrentItem(position);

                    getSupportActionBar().setTitle(mViewPager.getCurrentItem() + "/" + mUnsortedList.size());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
    public void onClick(File file) {
        mExplorer.openFolder(file);
        mFolderAdapter.updateFolders(mExplorer.getFolders());
        mPathTextView.setText(mExplorer.getCurrentPath().replace(mExplorer.getRootPath(), "Sorted Pictures"));
    }

    @Override
    public void onLongClick(File file) {

    }

    @Override
    public void onInsert(File file) {
        if (!mUnsortedList.isEmpty()) {
            int position = mViewPager.getCurrentItem();
            Image image = mUnsortedList.get(position);
            String oldPath = image.getPath();
            String newPath = file.getPath() + oldPath.substring(oldPath.lastIndexOf(File.separator));
            if (mExplorer.move(oldPath, newPath)) {
                image.setPath(newPath);
                image.setOldPath(oldPath);
                mViewModel.add(image);
                mUnsortedList.remove(image);
                mImagePagerAdapter.removeImage(image);
                mFolderAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(position);
                getSupportActionBar().setTitle(mViewPager.getCurrentItem() + "/" + mUnsortedList.size());

            } else {
                showToast("Can't move the image");
            }
        }
    }

    @Override
    public void onBackPressed() {
        backToParent();
    }

    private void backToParent() {
        if (mExplorer.goBack()) {
            mFolderAdapter.updateFolders(mExplorer.getFolders());
            mPathTextView.setText(mExplorer.getCurrentPath().replace(mExplorer.getRootPath(), "Sorted Pictures"));
        }
    }

    private void backToRoot() {
        if (mExplorer.backToRoot()) {
            mFolderAdapter.updateFolders(mExplorer.getFolders());
            mPathTextView.setText(mExplorer.getCurrentPath().replace(mExplorer.getRootPath(), "Sorted Pictures"));
        }
    }

    @Override
    public void onNewFolder(String name) {
        mExplorer.newFolder(name);
        mFolderAdapter.addFolder(mExplorer.getCurrentPath() + File.separator + name);
    }


    @Override
    public void onClickSortButton() {
        mViewPager.setCurrentItem(0, true);
    }


    @Override
    public void onClickImage(Image image) {

    }

    @Override
    public void loadFinished(final ArrayList<Image> loadedImages) {
        mViewModel.getTrashImages().observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                Iterator<Image> iterator = images.iterator();
                while (iterator.hasNext()) {
                    Image image = iterator.next();
                    for (Image i : images) {
                        if (i.getRowId() == image.getRowId()) {
                            loadedImages.remove(image);
                        }
                    }
                    iterator.remove();
                }
                mUnsortedList = loadedImages;
                mImagePagerAdapter.setImages(mUnsortedList);
                mViewPager.setAdapter(mImagePagerAdapter);
                getSupportActionBar().setTitle(mViewPager.getCurrentItem() + "/" + mUnsortedList.size());

            }
        });

    }
}