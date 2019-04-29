package com.example.appname.View.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appname.Model.Image;
import com.example.appname.R;
import com.example.appname.View.fullscreen.DisplayImageActivity;
import com.example.appname.View.folders.ImageAdapter;
import com.example.appname.ViewModel.ImageViewModel;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements ImageAdapter.ImageListener {

    public static final String ARGS_CURRENT_IMAGES = "ARGS_CURRENT_IMAGES";
    public static final String ARGS_IMAGE_POSITION = "ARGS_IMAGE_POSITION";

    private RecyclerView mSortedRecyclerView;
    private ImageAdapter mImageAdapter;
    private ImageViewModel mViewModel;
    private List<Image> mSortedImages;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        mViewModel.getSortedImages().observe(getActivity(), new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                mSortedImages = images;
                mImageAdapter.setImageList(images);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        mSortedRecyclerView = getView().findViewById(R.id.sorted_recycler);
        mImageAdapter = new ImageAdapter(getContext(), this);
        mSortedRecyclerView.setAdapter(mImageAdapter);
        mSortedRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
    }

    @Override
    public void onClickImage(int position) {
        Intent intent = new Intent(getActivity(), DisplayImageActivity.class);
        intent.putParcelableArrayListExtra(ARGS_CURRENT_IMAGES, (ArrayList<Image>) mSortedImages);
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
