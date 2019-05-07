package com.example.appname.View.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appname.Model.Explorer;
import com.example.appname.Model.Image;
import com.example.appname.R;
import com.example.appname.View.folders.MoveToAdapter;

import java.io.File;
import java.util.ArrayList;

public class MoveToDialog extends AppCompatDialogFragment implements MoveToAdapter.MoveToListener {

    public static final String ARGS_IMAGES = "CURRENT_TEXT";

    private RecyclerView mRecyclerView;
    private MoveToAdapter mAdapter;
    private Explorer mExplorer;

    public MoveToDialog() {
    }

    public static MoveToDialog newInstance(ArrayList<Image> images) {
        MoveToDialog dialog = new MoveToDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARGS_IMAGES, images);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        mExplorer = new Explorer(getContext());
        mAdapter = new MoveToAdapter(getContext(), mExplorer.getFolders(), mExplorer.getImages(), this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.move_to_dialog, (ViewGroup) getView(), false);

        final TextView pathText = view.findViewById(R.id.moveToPathText);
        final ImageView addFolder = view.findViewById(R.id.moveToAddFolder);
        final ImageView backButton = view.findViewById(R.id.moveToBack);
        mRecyclerView = view.findViewById(R.id.moveToRecycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        builder.setTitle("Select a folder");
        builder.setView(view);
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("Cancel", null);

        final AlertDialog dialog = builder.create();

        return dialog;
    }

    @Override
    public void onClickFolder(File folder) {
        mExplorer.openFolder(folder);
        mAdapter.setFolders(mExplorer.getFolders());
        mAdapter.setImages(mExplorer.getImages());
    }
}
