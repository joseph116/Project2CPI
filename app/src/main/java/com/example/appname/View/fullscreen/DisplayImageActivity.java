package com.example.appname.View.fullscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.appname.Model.Image;
import com.example.appname.R;

import java.util.ArrayList;
import java.util.List;

public class DisplayImageActivity extends AppCompatActivity implements ImageAdapter.ImageListener {

    private List<Image> mImages;
    private int mFirstPosition;
    private ViewPager mViewPager;
    private ImageAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        mImages = getIntent().getParcelableArrayListExtra("ARGS_CURRENT_IMAGES");
        mFirstPosition = getIntent().getIntExtra("ARGS_IMAGE_POSITION", 0);
        initViews();
    }

    private void initViews() {
        mViewPager = findViewById(R.id.display_image_view_pager);
        mAdapter = new ImageAdapter(this, mImages, this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mFirstPosition);
    }

    @Override
    public void onClickImage(Image image) {

    }
}
