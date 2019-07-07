package com.example.appname.View.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.appname.Model.Image;
import com.example.appname.Model.Tag;
import com.example.appname.R;
import com.example.appname.View.folders.ImageAdapter;
import com.example.appname.View.fullscreen.DisplayImageActivity;
import com.example.appname.ViewModel.ImageViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements ImageAdapter.ImageListener{

    public static final String ARGS_CURRENT_IMAGES = "ARGS_CURRENT_IMAGES";
    public static final String ARGS_IMAGE_POSITION = "ARGS_IMAGE_POSITION";


    private Toast mToast = null;
    private Toolbar mToolbar;
    private SearchView mSearchView;
    private ImageViewModel mViewModel;
    private ImageAdapter mImageAdapter;
    private RecyclerView mRecyclerView;
    private List<Image> mSearchedImages;
    private List<Tag> mSearchedTags;
    private List<String> mSelecteditems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setTitle("");
        mToolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewModel = ViewModelProviders.of(SearchActivity.this).get(ImageViewModel.class);
        initViews();
        getImageTagsFromDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mImageAdapter.getFilter().filter(newText);
                return false;
            }
        });
        mSearchView.setQueryHint("Search for images...");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_filter:
                if (!mSearchedTags.isEmpty())
                SelectTags();
                else showToast("No tags found...");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////


    private void initViews(){

        mRecyclerView = findViewById(R.id.search_recycler_view2);
        mRecyclerView.setHasFixedSize(false);

        mImageAdapter = new ImageAdapter(getApplicationContext(),this);
        mRecyclerView.setAdapter(mImageAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
    }

    private void getImageTagsFromDB(){
        mViewModel.getSortedImages().observe(SearchActivity.this, new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                mSearchedImages = images;
                //mList = images;
                mImageAdapter.setImageList(images);
            }
        });

        mViewModel.getAllTags().observe(SearchActivity.this, new Observer<List<Tag>>() {
            @Override
            public void onChanged(List<Tag> tags) {
                mSearchedTags = tags;
            }
        });
    }

    private void SelectTags(){

        final String[] tags = new String[mSearchedTags.size()];
        //tags[0] = "All tags";
        for (int i=0;i<tags.length;i++)
            tags[i]=mSearchedTags.get(i).getTitle();

        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setTitle("Select tags...");
        builder.setMultiChoiceItems(tags, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked){
                    mSelecteditems.add(tags[which]);
                }
                else if(mSelecteditems.contains(tags[which])){
                    mSelecteditems.remove(tags[which]);
                }
            }
        });
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!mSelecteditems.isEmpty()) {
                    List<String> list = new ArrayList<>();
                    for (int j=0;j<mSelecteditems.size();j++) {
                        for (int i=0;i<mSearchedTags.size();i++) {
                            if (mSearchedTags.get(i).getTitle().equalsIgnoreCase(mSelecteditems.get(j)))
                                list.add(Integer.toString(mSearchedTags.get(i).getId()));
                        }
                    }
                    mImageAdapter.filter(list);
                    mSelecteditems.clear();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelecteditems.clear();
            }
        });

        builder.show();
    }


    private void showToast(String message) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////


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
    public void onClickImage(int position) {
        Intent intent = new Intent(SearchActivity.this, DisplayImageActivity.class);
        if (mImageAdapter.getFiltredImages().isEmpty())
            intent.putParcelableArrayListExtra(ARGS_CURRENT_IMAGES, (ArrayList<Image>) mSearchedImages);
        else
            intent.putParcelableArrayListExtra(ARGS_CURRENT_IMAGES, (ArrayList<Image>) mImageAdapter.getFiltredImages());
        intent.putExtra(ARGS_IMAGE_POSITION, position);
        startActivity(intent);
    }

    @Override
    public boolean onLongClickImage(Image image) {
        return false;
    }

    @Override
    public void onChecked(Image image, boolean isChecked) {

    }
}
