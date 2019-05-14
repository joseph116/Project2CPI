package com.example.appname.View.main;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.appname.Model.Image;
import com.example.appname.R;
import com.example.appname.View.dialogs.ConfirmDeleteDialog;
import com.example.appname.View.dialogs.MoveToDialog;
import com.example.appname.View.folders.ImageAdapter;
import com.example.appname.ViewModel.ImageViewModel;

import java.util.ArrayList;
import java.util.HashSet;

public class TrashFragment extends Fragment implements ImageAdapter.ImageListener {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ImageViewModel mViewModel;
    private Button mDeleteAllButton;
    private Button mRestoreAllButton;
    private ArrayList<Image> mSelectedImages;
    private ActionMode mActionMode;

    public TrashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trash, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(ImageViewModel.class);
        initViews();
        getActivity().setTitle("Trash (" + mViewModel.getTrashImages().size() + ")");
    }

    private void initViews() {
        mRecyclerView = getView().findViewById(R.id.trash_recycler);
        mAdapter = new ImageAdapter(getActivity(), mViewModel.getTrashImages(), this);
        mRecyclerView.setAdapter(mAdapter);
        int spanCountImage = getResources().getDisplayMetrics().widthPixels / (300);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), spanCountImage));

        mDeleteAllButton = getView().findViewById(R.id.delete_all_button);
        mDeleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionMode != null) {

                } else {

                }
            }
        });
        mRestoreAllButton = getView().findViewById(R.id.restore_all_button);
        mRestoreAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionMode != null) {

                } else {

                }
            }
        });

        mSelectedImages = new ArrayList<>();
    }

    @Override
    public void onClickImage(int position) {

    }

    @Override
    public boolean onLongClickImage(Image image) {
        if (mAdapter.isSelectionMode()) {
            mAdapter.setSelectionMode(false);
            mActionMode.finish();
        } else {
            mSelectedImages.clear();
            mAdapter.setSelectionMode(true);
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(mCallback);
        }
        return true;
    }

    @Override
    public void onChecked(Image image, boolean isChecked) {
        if (isChecked) {
            if (!mSelectedImages.contains(image)){
                mSelectedImages.add(image);
            }
        } else {
            mSelectedImages.remove(image);
        }
        mActionMode.setTitle(mSelectedImages.size() + "/" + mAdapter.getItemCount() + " selected");
    }

    private ActionMode.Callback mCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.menu_multiple_image_selection, menu);
            actionMode.setTitle(mSelectedImages.size() + "/" + mAdapter.getItemCount() + " selected");
            mDeleteAllButton.setText("Delete selection");
            mRestoreAllButton.setText("Restore selection");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.select_all_option:
                    mAdapter.setAllChecked(true);
                    break;
                default:
                    actionMode.finish();
                    break;
            }

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mAdapter.setSelectionMode(false);
            mAdapter.setAllChecked(false);
            mActionMode = null;
            mDeleteAllButton.setText("Delete all");
            mRestoreAllButton.setText("Restore all");
        }
    };
}
