package com.example.appname.controller.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.snatik.storage.Storage;
import java.io.File;

public class ConfirmDeleteDialog extends DialogFragment {

    private final static String PATH = "path";
    private ConfirmListener mListener;
    private String mPath;

    public static ConfirmDeleteDialog newInstance(String path, ConfirmListener listener) {
        ConfirmDeleteDialog fragment = new ConfirmDeleteDialog(listener);
        Bundle args = new Bundle();
        args.putString(PATH, path);
        fragment.setArguments(args);
        return fragment;
    }

    public ConfirmDeleteDialog(ConfirmListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String msg;
        final String path = getArguments().getString(PATH);
        Storage storage = new Storage(getActivity());
        File file = storage.getFile(path);
        if (file.isDirectory()) {
            msg = "You are about to delete the folder with all it's content for real.";
        } else {
            msg = "You are about to delete the file";
        }
        builder.setMessage(msg);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onConfirmDelete(path);
            }
        });
        builder.setNegativeButton("Cancel", null);
        return builder.create();
    }

    public interface ConfirmListener {
        void onConfirmDelete(String path);
    }
}

