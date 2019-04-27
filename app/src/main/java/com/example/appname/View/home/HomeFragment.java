package com.example.appname.View.home;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appname.R;
import com.example.appname.View.folders.ImageAdapter;
import com.example.appname.ViewModel.ImageViewModel;


public class HomeFragment extends Fragment implements ImageAdapter.ImageListener {

    private RecyclerView mSortedRecyclerView;
    private ImageAdapter mImageAdapter;
    private ImageViewModel mViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
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
        //mImageAdapter = new ImageAdapter(getContext(), mViewModel.getSortedImages(), this);
    }

    @Override
    public boolean onLongClickImage(Uri image) {
        return false;
    }

    @Override
    public void onChecked(Uri image, boolean isChecked) {

    }
}
