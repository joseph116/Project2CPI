package com.example.appname.View.sort;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.appname.Model.Image;
import com.example.appname.R;
import com.example.appname.View.dialogs.NewFolderDialog;
import com.example.appname.View.main.MainActivity;
import com.example.appname.Model.Explorer;
import com.example.appname.ViewModel.ImageViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SortFragment extends Fragment implements FolderAdapter.OnFileItemListener,
        MainActivity.BackPressedListener,
        NewFolderDialog.DialogListener,
        ImagePagerAdapter.PagerViewListener {

    //==============================================================================================
    //  ATTRIBUTES
    //==============================================================================================

    private static final String ARG_UNSORTED_LIST = "unsorted list";

    private ImageViewModel mViewModel;
    private RecyclerView mFolderRecyclerView;
    private ViewPager mViewPager;
    private ImagePagerAdapter mImagePagerAdapter;
    private Explorer mExplorer;
    private FolderAdapter mFolderAdapter;
    private TextView mPathTextView;
    private ImageButton mBackButton;
    private ImageButton mBackToRoot;
    private List<Image> mUnsortedImages;


    //==============================================================================================
    //  CONSTRUCTORS
    //==============================================================================================

    public SortFragment() {
        // Required empty public constructor
    }

    public static SortFragment newInstance(ArrayList<Image> list) {
        SortFragment fragment = new SortFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_UNSORTED_LIST, list);
        fragment.setArguments(args);
        return fragment;
    }


    //==============================================================================================
    //  STATE FUNCTIONS
    //==============================================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUnsortedImages = getArguments().getParcelableArrayList(ARG_UNSORTED_LIST);
        }
        mViewModel = ViewModelProviders.of(getActivity()).get(ImageViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sort, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mExplorer = new Explorer(getContext());
        initViews();
        ((MainActivity) getActivity()).setBackListener(this);
    }


    //==============================================================================================
    //  INIT FUNCTIONS
    //==============================================================================================

    private void initViews() {
        //Recycler View
        mFolderRecyclerView = getView().findViewById(R.id.sortRecyclerView);
        mFolderAdapter = new FolderAdapter(getActivity(), mExplorer.getFolders(), this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mFolderRecyclerView.setLayoutManager(gridLayoutManager);
        mFolderRecyclerView.setAdapter(mFolderAdapter);

        //Pager View
        mViewPager = getView().findViewById(R.id.sortViewPager);
        mImagePagerAdapter = new ImagePagerAdapter(getContext(), mViewModel.getUnsortedImages(), this);
        mViewPager.setAdapter(mImagePagerAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mViewModel.getUnsortedImages().size()) {
                    mFolderAdapter.hideInsertButton(true);
                } else {
                    mFolderAdapter.hideInsertButton(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Path TextView
        mPathTextView = getView().findViewById(R.id.sortPathText);
        String currentPath = mExplorer.getCurrentPath();
        String rootPath = mExplorer.getRootPath();
        mPathTextView.setText(currentPath.replace(rootPath, "Sorted Pictures"));

        //Back Button
        mBackButton = getView().findViewById(R.id.sortBackButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToParent();
            }
        });

        //Back to root button
        mBackToRoot = getView().findViewById(R.id.sortBackToRoot);
        mBackToRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToRoot();
            }
        });
    }

    //==============================================================================================
    //  FUNCTIONS
    //==============================================================================================


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
    public void onClickAdd() {
        NewFolderDialog dialog = new NewFolderDialog(this);
        dialog.show(getFragmentManager(), "new folder dialog");
    }

    @Override
    public void onInsert(File file) {
        if (!mViewModel.getUnsortedImages().isEmpty()) {
            int position = mViewPager.getCurrentItem();
            Image image = mViewModel.getUnsortedImages().get(position);
            String oldPath = image.getPath();
            String newPath = file.getPath() + oldPath.substring(oldPath.lastIndexOf(File.separator));
            if (mExplorer.move(oldPath, newPath)) {
                image.setPath(newPath);
                mViewModel.add(image);
                mViewModel.getUnsortedImages().remove(image);
                mImagePagerAdapter.removeImage(image);
                mFolderAdapter.notifyDataSetChanged();
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
}