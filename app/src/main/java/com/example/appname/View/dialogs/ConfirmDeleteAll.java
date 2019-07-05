package com.example.appname.View.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ConfirmDeleteAll extends DialogFragment {

    private ConfirmListener mListener;

    public ConfirmDeleteAll(ConfirmListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("You are about to delete the images from the phone");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onConfirmDeleteAll();
            }
        });
        builder.setNegativeButton("Cancel", null);
        return builder.create();
    }

    public interface ConfirmListener {
        void onConfirmDeleteAll();
    }
}
