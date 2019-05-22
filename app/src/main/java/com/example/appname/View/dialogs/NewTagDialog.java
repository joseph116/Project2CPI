package com.example.appname.View.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.appname.R;
import com.example.appname.View.fullscreen.ColorRecyclerAdapter;

import java.util.Arrays;

public class NewTagDialog extends AppCompatDialogFragment {

    private AddTagListener mListener;
    private Toast mToast;
    private RecyclerView mColorRecycler;
    private TextView mPreviewTag;
    private ColorRecyclerAdapter mAdapter;

    public NewTagDialog(AddTagListener listener) {
        mListener = listener;
    }

    public static NewTagDialog newInstance(AddTagListener listener) {
        NewTagDialog dialog = new NewTagDialog(listener);
        Bundle args = new Bundle();
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.add_tag_layout, (ViewGroup) getView(), false);

        mPreviewTag = view.findViewById(R.id.tagPreview);
        final EditText text = view.findViewById(R.id.newTagEditText);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPreviewTag.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String[] colors = getResources().getStringArray(R.array.custom_colors);
        mColorRecycler = view.findViewById(R.id.colorRecyclerView);
        mAdapter = new ColorRecyclerAdapter(colors, new ColorRecyclerAdapter.ColorClickListener() {
            @Override
            public void onClickColor(int color, int position) {
                int r = (color >> 16) & 0xff;
                int g = (color >>  8) & 0xff;
                int b = (color      ) & 0xff;
                double luminance = ( 0.299 * r + 0.587 * g + 0.114 * b)/255;
                if (luminance > 0.5) {
                    mPreviewTag.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                } else {
                    mPreviewTag.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                }
                mPreviewTag.setBackgroundTintList(ColorStateList.valueOf(color));
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mColorRecycler.setLayoutManager(gridLayoutManager);
        mColorRecycler.setAdapter(mAdapter);


        builder.setTitle("New tag");
        builder.setView(view);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = text.getText().toString();
                if ((title.length() > 0) && !title.contains("§")) {
                    mListener.onNewTag(text.getText().toString());
                }
            }
        });
        builder.setNegativeButton("Cancel", null);

        final AlertDialog dialog = builder.create();

        // show soft keyboard
        text.requestFocus();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dialog;
    }

    private void showToast(String message) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public interface AddTagListener {

        void onNewTag(String title);
    }
}
