package com.example.appname.View.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.appname.R;
import com.snatik.storage.Storage;

import java.io.File;

public class RenameDialog extends AppCompatDialogFragment {

    private final static String PATH = "path";

    private RenameListener mListener;
    private Storage mStorage;

    public static RenameDialog newInstance(String path, RenameListener listener) {
        RenameDialog fragment = new RenameDialog(listener);
        Bundle args = new Bundle();
        args.putString(PATH, path);
        fragment.setArguments(args);
        return fragment;
    }

    public RenameDialog(RenameListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mStorage = new Storage(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.rename_dialog, (ViewGroup) getView(), false);

        String path = getArguments().getString(PATH);

        final File file = mStorage.getFile(path);
        final String parent = file.getParent();

        final EditText newNameText = view.findViewById(R.id.new_name);
        newNameText.setText(file.getName());
        newNameText.selectAll();
        newNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(editable != null &&
                        editable.length() > 0);
            }
        });

        builder.setTitle("Rename");
        builder.setView(view);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newName = newNameText.getText().toString();
                String toPath = (parent == null) ? newName : parent + File.separator + newName;
                mListener.onRename(file.getPath(), toPath);
            }
        });
        builder.setNegativeButton("Cancel", null);
        final AlertDialog dialog = builder.create();
        view.post(new Runnable() {
            @Override
            public void run() {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });
        dialog.setCancelable(false);

        // show soft keyboard
        newNameText.requestFocus();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dialog;
    }

    public interface RenameListener {
        void onRename(String fromPath, String toPath);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
