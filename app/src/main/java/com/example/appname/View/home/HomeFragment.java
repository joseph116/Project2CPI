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
import android.widget.TextView;

import com.example.appname.Model.Image;
import com.example.appname.R;
import com.example.appname.View.fullscreen.DisplayImageActivity;
import com.example.appname.View.folders.ImageAdapter;
import com.example.appname.View.main.CameraActivity;
import com.example.appname.View.main.MainActivity;
import com.example.appname.ViewModel.ImageViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements ImageAdapter.ImageListener,
        MainActivity.BackPressedListener {

    public static final String ARGS_CURRENT_IMAGES = "ARGS_CURRENT_IMAGES";
    public static final String ARGS_IMAGE_POSITION = "ARGS_IMAGE_POSITION";

    private RecyclerView mSortedRecyclerView;
    private ImageAdapter mImageAdapter;
    private ImageViewModel mViewModel;
    private List<Image> mSortedImages;
    private TextView mNumberElements;
    private FloatingActionButton mButtonCamera;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        getActivity().setTitle("Sorted images");
        mNumberElements = getView().findViewById(R.id.number_element_database);
        mViewModel = ViewModelProviders.of(getActivity()).get(ImageViewModel.class);
        initViews();
        mViewModel.getSortedImages().observe(getActivity(), new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                mNumberElements.setText("(" + images.size() + ")");
                mSortedImages = images;
                mImageAdapter.setImageList(images);
            }
        });
        ((MainActivity)getActivity()).setBackListener(this);
    }

    private void initViews() {
        mSortedRecyclerView = getView().findViewById(R.id.sorted_recycler);
        mImageAdapter = new ImageAdapter(getContext(), this);
        mSortedRecyclerView.setAdapter(mImageAdapter);
        mSortedRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        mButtonCamera = getView().findViewById(R.id.buttonCamera);
        mButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openCamera = new Intent(getActivity(), CameraActivity.class);
                getActivity().startActivity(openCamera);
            }
        });

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

    @Override
    public void onBackPressed() {
        getActivity().finish();
    }
}
