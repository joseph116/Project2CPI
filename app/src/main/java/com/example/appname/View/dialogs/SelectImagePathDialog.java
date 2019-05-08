package com.example.appname.View.dialogs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appname.Model.Explorer;
import com.example.appname.R;
import com.example.appname.View.folders.FolderAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectImagePathDialog extends DialogFragment implements FolderAdapter.FolderListener{

    private static final String TAG = "SelectImagePathDialog";


    public interface OnPathSelected{
        void sendPath(String path);
    }
    public OnPathSelected mOnPathSelected;

    // Widgets
    private TextView mCurrentPath;
    private Button mActionSelect;
    private Button mActionCancel;
    private Button mActionBack;
    private RecyclerView mRecyclerView;
    private String mPath;

    private Explorer mExplorer;
    private FolderAdapter mFolderAdapter;
    private static List<File> mFoldersList = new ArrayList<>();
    private FolderAdapter.FolderListener mFolderListener = this;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_image_path_dialog, container, false);
        mCurrentPath = view.findViewById(R.id.currentpath);
        mActionSelect = view.findViewById(R.id.action_select);
        mActionCancel = view.findViewById(R.id.action_cancel);
        mActionBack = view.findViewById(R.id.action_back);
        mRecyclerView = view.findViewById(R.id.recycler_view_dialog);


        ////////////////////////////////////////////////////////////////////////////////////////////
        // EXPLORER PART
        ////////////////////////////////////////////////////////////////////////////////////////////

        mExplorer = new Explorer(getContext());
        mFoldersList = mExplorer.getFolders();
        int s = mExplorer.getCurrentPath().length();
        mCurrentPath.setText("/Home");


        mFolderAdapter = new FolderAdapter(getContext(), mFoldersList, mFolderListener);
        int spanCountFolder = getResources().getDisplayMetrics().widthPixels / (250);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCountFolder));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mFolderAdapter);

        ////////////////////////////////////////////////////////////////////////////////////////////
        mActionSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Select Image Path
                OnPathSelected activity = (OnPathSelected) getActivity();
                activity.sendPath(mExplorer.getCurrentPath());
                getDialog().dismiss();
                Toast.makeText(getContext(), "Path:Home"+mExplorer.getCurrentPath().substring(44),Toast.LENGTH_SHORT).show();
            }
        });

        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancel and the path of image is:Unsorted Pictures
                getDialog().dismiss();
            }
        });
        mActionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        return view;
    }

    //==============================================================================================
    //  LISTENERS FUNCTIONS
    //==============================================================================================

    //click on a folder
    @Override
    public void onClick(File file) {
        mExplorer.openFolder(file);
        mFolderAdapter.updateFolders(mExplorer.getFolders());
        mCurrentPath.setText("/Home"+mExplorer.getCurrentPath().substring(44));
        mPath = mExplorer.getCurrentPath();
    }

    //long click on a folder
    @Override
    public void onLongClick(File file) {

    }

    //click on the add button
    @Override
    public void onClickAdd() {

    }

    // click back button
    private void onBackPressed(){
        if (mExplorer.goBack()) {
            mFolderAdapter.updateFolders(mExplorer.getFolders());
            mCurrentPath.setText("/Home"+mExplorer.getCurrentPath().substring(44));
        }
    }
}
