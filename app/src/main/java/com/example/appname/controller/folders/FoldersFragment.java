package com.example.appname.controller.folders;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appname.R;
import com.example.appname.model.Explorer;


public class FoldersFragment extends Fragment {

    //==============================================================================================
    //  ATTRIBUTES
    //==============================================================================================

    private Explorer mExplorer;
    private RecyclerView mFolderRecyclerView;
    private RecyclerView mImageRecyclerView;


    //==============================================================================================
    //  CONSTRUCTORS
    //==============================================================================================

    public FoldersFragment() {
        // Required empty public constructor
    }

    //==============================================================================================
    //  STATE FUNCTIONS
    //==============================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_folders, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mExplorer = new Explorer(getContext());
        initRecyclers();
    }

    //==============================================================================================
    //  INIT FUNCTIONS
    //==============================================================================================

    private void initRecyclers(){
        //folders recycler
        mFolderRecyclerView = getView().findViewById(R.id.folderRecyclerView);
        mFolderRecyclerView.setAdapter(new FolderAdapter(getContext(), mExplorer.getFolders(mExplorer.getRootPath())));
        int spanCount = getResources().getDisplayMetrics().widthPixels / (300);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), spanCount);
        mFolderRecyclerView.setLayoutManager(gridLayoutManager);
        //image recycler
    }

    //==============================================================================================
    //  FUNCTIONS
    //==============================================================================================



}
