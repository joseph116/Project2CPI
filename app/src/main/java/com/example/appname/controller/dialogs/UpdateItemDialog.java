package com.example.appname.controller.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.appname.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.snatik.storage.Storage;

public class UpdateItemDialog extends DialogFragment {

    private final static String PATH = "path";
    private DialogListener mListener;

    public static UpdateItemDialog newInstance(String path, DialogListener listener) {
        UpdateItemDialog fragment = new UpdateItemDialog(listener);
        Bundle args = new Bundle();
        args.putString(PATH, path);
        fragment.setArguments(args);
        return fragment;
    }

    public UpdateItemDialog(DialogListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog = new BottomSheetDialog(getActivity(), getTheme());
        final String path = getArguments().getString(PATH);
        boolean isDirectory = new Storage(getActivity()).getFile(path).isDirectory();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.update_item_dialog, null);
        dialog.setContentView(view);
        dialog.setCancelable(true);

        View rename = view.findViewById(R.id.rename);
        View delete = view.findViewById(R.id.delete);

        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mListener.onOptionClick(R.id.rename, path);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mListener.onOptionClick(R.id.delete, path);
            }
        });

        // control dialog width on different devices
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogINterface) {
                int width = (int) getResources().getDimension(R.dimen.bottom_sheet_dialog_width);
                dialog.getWindow().setLayout(
                        width == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : width,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            }
        });

        return dialog;
    }

    public interface DialogListener {
        void onOptionClick(int which, String path);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
