package com.example.appname.View.folders;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.appname.Model.Image;
import com.example.appname.R;
import com.example.appname.View.dialogs.ConfirmDeleteDialog;
import com.example.appname.View.dialogs.MoveToDialog;
import com.example.appname.View.dialogs.NewFolderDialog;
import com.example.appname.View.dialogs.RenameDialog;
import com.example.appname.View.dialogs.UpdateItemDialog;
import com.example.appname.View.fullscreen.DisplayImageActivity;
import com.example.appname.View.main.MainActivity;
import com.example.appname.Model.Explorer;
import com.example.appname.ViewModel.ImageViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.snatik.storage.Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FoldersFragment extends Fragment implements FolderAdapter.FolderListener,
        ImageAdapter.ImageListener,
        MainActivity.BackPressedListener,
        NewFolderDialog.DialogListener,
        UpdateItemDialog.DialogListener,
        ConfirmDeleteDialog.ConfirmListener,
        RenameDialog.RenameListener{

    //==============================================================================================
    //  ATTRIBUTES
    //==============================================================================================

    public static final String ARGS_CURRENT_IMAGES = "ARGS_CURRENT_IMAGES";
    public static final String ARGS_IMAGE_POSITION = "ARGS_IMAGE_POSITION";

    public static final String BUNDLE_CURRENT_PATH = "BUNDLE_CURRENT_PATH";
    public static final String BUNDLE_TREE_STEPS = "BUNDLE_TREE_STEPS";

    private Explorer mExplorer;
    private RecyclerView mFolderRecyclerView;
    private RecyclerView mImageRecyclerView;
    private FolderAdapter mFolderAdapter;
    private ImageAdapter mImageAdapter;
    private static FileAdapter sFileAdapter;
    private List<Image> mSelectedImages;
    private ActionMode mActionMode;
    private ImageViewModel mViewModel;
    private int mTreeStep;
    private String mCurrentPath;

    private static Dialog mDialog;
    private static String selectedPath;
    private static List<String> PathList = new ArrayList<>();
    private static List<File> FilesList = new ArrayList<>();



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
        mDialog = new Dialog(Objects.requireNonNull(getContext()));
        mDialog.setContentView(R.layout.dialog_layout);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mExplorer = new Explorer(getContext());
        mViewModel = ViewModelProviders.of(getActivity()).get(ImageViewModel.class);
        mSelectedImages = new ArrayList<>();
        initRecyclers();
        ((MainActivity)getActivity()).setBackListener(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(BUNDLE_CURRENT_PATH, mExplorer.getCurrentPath());
        outState.putInt(BUNDLE_TREE_STEPS, mExplorer.getTreeSteps());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mExplorer.setCurrentPath(savedInstanceState.getString(BUNDLE_CURRENT_PATH));
            mExplorer.setTreeSteps(savedInstanceState.getInt(BUNDLE_TREE_STEPS));
        }
    }

    //==============================================================================================
    //  INIT FUNCTIONS
    //==============================================================================================

    private void initRecyclers() {
        //folders recycler
        mFolderRecyclerView = getView().findViewById(R.id.folderRecyclerView);
        mFolderAdapter = new FolderAdapter(getContext(), mExplorer.getFolders(), this);
        mFolderRecyclerView.setAdapter(mFolderAdapter);
        int spanCountFolder = getResources().getDisplayMetrics().widthPixels / (300);
        int spanCountImage = getResources().getDisplayMetrics().widthPixels / (300);
        mFolderRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCountFolder));
        //image recycler
        mImageRecyclerView = getView().findViewById(R.id.imageRecyclerView);
        mImageAdapter = new ImageAdapter(getContext(), mExplorer.getImages(), this);
        mImageRecyclerView.setAdapter(mImageAdapter);
        mImageRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCountImage));

        String currentPath = mExplorer.getCurrentPath();
        getActivity().setTitle(currentPath.substring(currentPath.lastIndexOf(File.separator) + 1));
    }

    //==============================================================================================
    //  FUNCTIONS
    //==============================================================================================

    private void removeImagesFromFolder(String path) {
        for (Image image : mExplorer.getImages(path)) {
            mViewModel.remove(image);
            mExplorer.deleteImage(image);
            if (!mExplorer.getFolders(path).isEmpty()) {
                for (File file : mExplorer.getFolders(path)) {
                    removeImagesFromFolder(file.getPath());
                }
            }
        }
    }

    //==============================================================================================
    //  LISTENERS FUNCTIONS
    //==============================================================================================

    //click on a folder
    @Override
    public void onClick(File file) {
        mExplorer.openFolder(file);
        mFolderAdapter.updateFolders(mExplorer.getFolders());
        mImageAdapter.updateImageList(mExplorer.getImages());
        String currentPath = mExplorer.getCurrentPath();
        getActivity().setTitle(currentPath.substring(currentPath.lastIndexOf(File.separator) + 1));
    }

    //long click on a folder
    @Override
    public void onLongClick(File file) {
        UpdateItemDialog.newInstance(file.getAbsolutePath(), this).show(getFragmentManager(), "update_item");
    }

    //click on the add button
    @Override
    public void onClickAdd() {
        NewFolderDialog dialog = new NewFolderDialog(this);
        dialog.show(getFragmentManager(), "new folder dialog");
    }

    @Override
    public void onClickImage(int position) {
        Intent intent = new Intent(getActivity(), DisplayImageActivity.class);
        intent.putParcelableArrayListExtra(ARGS_CURRENT_IMAGES, mExplorer.getImages());
        intent.putExtra(ARGS_IMAGE_POSITION, position);
        startActivity(intent);
    }

    //long click on an image
    @Override
    public boolean onLongClickImage(Image image) {
        if (mImageAdapter.isSelectionMode()) {
            mImageAdapter.setSelectionMode(false);
            mActionMode.finish();
        } else {
            mSelectedImages.clear();
            mImageAdapter.setSelectionMode(true);
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(mCallback);
        }
        return true;
    }

    //select an image in multiple selection mode
    @Override
    public void onChecked(Image image, boolean isChecked) {
        if (isChecked) {
            if (!mSelectedImages.contains(image)){
                mSelectedImages.add(image);
            }
        } else {
            mSelectedImages.remove(image);
        }
        mActionMode.setTitle(mSelectedImages.size() + "/" + mImageAdapter.getItemCount() + " selected");
    }

    //click back button
    @Override
    public void onBackPressed() {
        if (mExplorer.goBack()) {
            mFolderAdapter.updateFolders(mExplorer.getFolders());
            mImageAdapter.updateImageList(mExplorer.getImages());
            String currentPath = mExplorer.getCurrentPath();
            getActivity().setTitle(currentPath.substring(currentPath.lastIndexOf(File.separator) + 1));
        }
    }

    @Override
    public void onNewFolder(String name) {
        mExplorer.newFolder(name);
        mFolderAdapter.addFolder(mExplorer.getCurrentPath() + File.separator + name);
    }

    //click on a folder option
    @Override
    public void onOptionClick(int which, String path) {
        switch (which) {
            case R.id.delete:
                ConfirmDeleteDialog.newInstance(path, this).show(getFragmentManager(), "confirm_delete");
                break;
            case R.id.rename:
                RenameDialog.newInstance(path, this).show(getFragmentManager(), "rename");
                break;
        }
    }

    @Override
    public void onConfirmDelete(String path) {
        removeImagesFromFolder(path);
        mExplorer.deleteFolder(path);
        mFolderAdapter.removeFolder(path);
    }

    @Override
    public void onRename(String fromPath, String toPath) {
        mExplorer.renameFolder(fromPath, toPath);
        mFolderAdapter.renameFolder(fromPath, toPath);
    }

    private ConfirmDeleteDialog.ConfirmListener confirmDeleteImages = new ConfirmDeleteDialog.ConfirmListener() {
        @Override
        public void onConfirmDelete(String path) {
            for (Image image : mSelectedImages) {
                mViewModel.remove(image);
                mExplorer.deleteImage(image);
                mImageAdapter.removeImage(image);
            }
            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
        }
    };

    //==============================================================================================
    //  ACTION MODE
    //==============================================================================================

    private ActionMode.Callback mCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.menu_multiple_image_selection, menu);
            actionMode.setTitle(mSelectedImages.size() + "/" + mImageAdapter.getItemCount() + " selected");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.move_option:
                    //CallDialog(getContext());
                    MoveToDialog.newInstance((ArrayList<Image>) mSelectedImages).show(getFragmentManager(), "move_to");
                    actionMode.finish();
                    break;
                case R.id.delete_option:
                    //Delete Selection
                    ConfirmDeleteDialog.newInstance(null, confirmDeleteImages).show(getFragmentManager(), "confirm_delete");
                    actionMode.finish();
                    break;
                case R.id.select_all_option:
                    mImageAdapter.setAllChecked(true);
                    break;
                default:
                    actionMode.finish();
                    break;
            }

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mImageAdapter.setSelectionMode(false);
            mImageAdapter.setAllChecked(false);
            mActionMode = null;
        }
    };


    //==============================================================================================
    //to show dialog
    //==============================================================================================

    public void CallDialog(final Context context){

        //the elements of the dialog box here=======================================================
        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final TextInputLayout inputName = mDialog.findViewById(R.id.input_name);
        final RecyclerView PopRecycler = mDialog.findViewById(R.id.pop_recycler);
        final Storage storage = new Storage(context);
        Button SelectToMoveButton = mDialog.findViewById(R.id.close_pop);
        Button GoParent = mDialog.findViewById(R.id.go_parent);
        Button newFolder = mDialog.findViewById(R.id.new_folder);
        RecyclerView.LayoutManager layoutManagerMini = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);

        // To populate the recycler with a list of items============================================
        selectedPath = Environment.getExternalStorageDirectory().getPath() +  "/Pictures/Sorted Pictures";
        FilesList = storage.getFiles(selectedPath);
        for(int i = 0; i < FilesList.size() ; i++){
            PathList.add(FilesList.get(i).getPath());
        }

        //the click listener for every item of the recycler=========================================
        FileClickListener fileListener = new FileClickListener() {
            @Override
            public void onClick(View v, int position) {
                selectedPath = FilesList.get(position).getPath();
                Storage storage = new Storage(context);
                if(FilesList.get(position).isDirectory()){
                    ShowFiles(selectedPath , storage , PopRecycler);
                } }
        };

        //Binds the view for the adapter ===========================================================
        sFileAdapter = new FileAdapter(context , PathList , fileListener);
        PopRecycler.setHasFixedSize(false);
        PopRecycler.setLayoutManager(layoutManagerMini);
        PopRecycler.setAdapter(sFileAdapter);

        //The move button action
        SelectToMoveButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                //DO the operation of move here ====================================================


                //==================================================================================

                //what happens after the operation is finished
                mDialog.dismiss();

            }
        });

        //the Go to parent action
        GoParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!selectedPath.equals(Environment.getExternalStorageDirectory().getPath() + "/Pictures/Sorted Pictures")){
                    selectedPath = selectedPath.substring(0 , selectedPath.lastIndexOf("/"));
                    ShowFiles(selectedPath , new Storage(context) , PopRecycler);
                }else{
                    Toast.makeText(context , "Can not go to system directory for safety reasons" , Toast.LENGTH_LONG).show();
                }
            }
        });

        //the new folder action
        newFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage.createDirectory(selectedPath + "/" +  inputName.getEditText().getText().toString());
                ShowFiles(selectedPath , storage , PopRecycler);
                inputName.clearFocus();
                mFolderAdapter.updateFolders(mExplorer.getFolders());
                Toast.makeText(context , "Folder created" , Toast.LENGTH_SHORT).show();
            }
        });

        //after everything is set we finally can show the dialog box "a long road until here XD"
        mDialog.show();

        //what happens when the dialog is dismissed
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //i need you to refresh your folders recycler here
                PathList.clear();
                FilesList = null;
            }
        });

    }


    //method that shows the sFileAdapter inside the dialog box ============================================
    public static void ShowFiles(String path, Storage storage, RecyclerView popRecycle){
        FilesList =null;
        FilesList = storage.getFiles(path);
        PathList.clear();
        for(int i = 0; i < FilesList.size() ; i++){
            PathList.add(FilesList.get(i).getPath());
        }
        popRecycle.swapAdapter(sFileAdapter, true);
    }


}
