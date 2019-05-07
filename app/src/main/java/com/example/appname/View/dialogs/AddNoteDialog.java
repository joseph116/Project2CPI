package com.example.appname.View.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.appname.R;

public class AddNoteDialog extends AppCompatDialogFragment {

    public static final String ARGS_X = "X";
    public static final String ARGS_Y = "Y";
    private AddNoteListener mListener;

    public AddNoteDialog(AddNoteListener listener) {
        mListener = listener;
    }

    public static AddNoteDialog newInstance(float x, float y, AddNoteListener listener) {
        AddNoteDialog dialog = new AddNoteDialog(listener);
        Bundle args = new Bundle();
        args.putFloat(ARGS_X, x);
        args.putFloat(ARGS_Y, y);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.add_note_dialog, (ViewGroup) getView(), false);

        final EditText text = view.findViewById(R.id.add_note_edit_text);

        builder.setTitle("Add note");
        builder.setView(view);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onAddNote(text.getText().toString(), getArguments().getFloat(ARGS_X), getArguments().getFloat(ARGS_Y));
            }
        });
        builder.setNegativeButton("Cancel", null);

        final AlertDialog dialog = builder.create();

        // show soft keyboard
        text.requestFocus();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dialog;
    }

    public interface AddNoteListener {

        void onAddNote(String text, float x, float y);
    }
}
