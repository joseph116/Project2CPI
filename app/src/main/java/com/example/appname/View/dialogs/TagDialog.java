package com.example.appname.View.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class TagDialog extends AppCompatDialogFragment {

    private TagDialogListener mListener;

    public TagDialog(TagDialogListener listener) {
        mListener = listener;
    }

    public static TagDialog newInstance(TagDialogListener listener) {

        Bundle args = new Bundle();

        TagDialog fragment = new TagDialog(listener);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    public interface TagDialogListener {

    }
}
