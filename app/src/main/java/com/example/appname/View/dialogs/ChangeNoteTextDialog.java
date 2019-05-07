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


public class ChangeNoteTextDialog extends AppCompatDialogFragment {

    public static final String ARGS_CURRENT_TEXT = "CURRENT_TEXT";
    public static final String ARGS_NOTE_ID = "NOTE_ID";

    private ChangeNoteTextListener mListener;

    public ChangeNoteTextDialog(ChangeNoteTextListener listener) {
        mListener = listener;
    }

    public static ChangeNoteTextDialog newInstance(String text, long noteId, ChangeNoteTextListener listener) {
        ChangeNoteTextDialog dialog = new ChangeNoteTextDialog(listener);
        Bundle args = new Bundle();
        args.putString(ARGS_CURRENT_TEXT, text);
        args.putLong(ARGS_NOTE_ID, noteId);
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
        text.setText(getArguments().getString(ARGS_CURRENT_TEXT));
        text.selectAll();

        builder.setTitle("Edit note");
        builder.setView(view);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onChangeNoteText(text.getText().toString(), getArguments().getLong(ARGS_NOTE_ID));
            }
        });
        builder.setNegativeButton("Cancel", null);

        final AlertDialog dialog = builder.create();

        // show soft keyboard
        text.requestFocus();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dialog;
    }

    public interface ChangeNoteTextListener {

        void onChangeNoteText(String newText, long noteId);
    }
}
