package com.example.appname.controller.folders;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appname.R;
import com.example.appname.model.Explorer;

import java.io.File;


public class FoldersFragment extends Fragment implements FolderAdapter.FolderListener,
        ImageAdapter.ImageListener {

    //==============================================================================================
    //  ATTRIBUTES
    //==============================================================================================

    private Explorer mExplorer;
    private RecyclerView mFolderRecyclerView;
    private RecyclerView mImageRecyclerView;
    private FolderAdapter mFolderAdapter;
    private ImageAdapter mImageAdapter;


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
        initBackButton();
    }

    //==============================================================================================
    //  INIT FUNCTIONS
    //==============================================================================================

    private void initRecyclers() {
        //folders recycler
        mFolderRecyclerView = getView().findViewById(R.id.folderRecyclerView);
        mFolderAdapter = new FolderAdapter(getContext(), mExplorer.getFolders(), this);
        mFolderRecyclerView.setAdapter(mFolderAdapter);
        int spanCount = getResources().getDisplayMetrics().widthPixels / (300);
        mFolderRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        //image recycler
        mImageRecyclerView = getView().findViewById(R.id.imageRecyclerView);
        mImageAdapter = new ImageAdapter(getContext(), mExplorer.getImages(), this);
        mImageRecyclerView.setAdapter(mImageAdapter);
        mImageRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
    }

    private void initBackButton() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    onBackClick();
                    return true;
                }
                return false;
            }
        });
    }

    //==============================================================================================
    //  FUNCTIONS
    //==============================================================================================


    //==============================================================================================
    //  LISTENERS FUNCTIONS
    //==============================================================================================

    //click on a folder
    @Override
    public void onClick(File file) {
        mExplorer.openFolder(file);
        mFolderAdapter.updateFolders(mExplorer.getFolders());
        mImageAdapter.updateImageList(mExplorer.getImages());
    }

    //long click on a folder
    @Override
    public void onLongClick(File file) {

    }

    //click on the add button
    @Override
    public void onClickAdd() {

    }

    //long click on an image
    @Override
    public boolean onLongClickImage(Uri image) {
        return false;
    }

    //select an image in multiple selection mode
    @Override
    public void onChecked(Uri image, boolean isChecked) {

    }

    //click back button
    private void onBackClick() {
        mExplorer.goBack();
        mFolderAdapter.updateFolders(mExplorer.getFolders());
        mImageAdapter.updateImageList(mExplorer.getImages());
    }
}
