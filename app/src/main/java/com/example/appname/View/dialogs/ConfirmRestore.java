package com.example.appname.View.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ConfirmRestore extends DialogFragment {

    private ConfirmListener mListener;

    public ConfirmRestore(ConfirmListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("You are about to restore the images");
        builder.setPositiveButton("Restore", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onConfirmRestore();
            }
        });
        builder.setNegativeButton("Cancel", null);
        return builder.create();
    }

    public interface ConfirmListener{
        void onConfirmRestore();
    }
}
