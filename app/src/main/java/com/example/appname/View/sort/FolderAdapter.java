package com.example.appname.View.sort;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appname.R;
import com.example.appname.ViewModel.ImageViewModel;

import java.io.File;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    //==============================================================================================
    //  ATTRIBUTES
    //==============================================================================================

    private FragmentActivity mFragmentActivity;
    private ImageViewModel mViewModel;
    private List<File> mFolders;
    private OnFileItemListener mListener;
    private boolean mHideInsert;

    //==============================================================================================
    //  CONSTRUCTORS
    //==============================================================================================

    public FolderAdapter(FragmentActivity fragmentActivity, List<File> folders, OnFileItemListener listener) {
        mListener = listener;
        mFolders = folders;
        mViewModel = ViewModelProviders.of(fragmentActivity).get(ImageViewModel.class);
    }

    //==============================================================================================
    //  ADAPTER FUNCTIONS
    //==============================================================================================

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.folder_item_sort_part, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        if (getItemViewType(i) == 1) {
            viewHolder.getInsertButton().setImageResource(R.drawable.ic_plus);
            viewHolder.getTextView().setText("Add Folder");
            viewHolder.getInsertButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add button click
                    mListener.onClickAdd();
                }

            });

        } else {
            final File file = mFolders.get(i - 1);

            viewHolder.getTextView().setText(mFolders.get(i - 1).getName());

            viewHolder.getTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(file);
                }

            });
            viewHolder.getTextView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onLongClick(file);
                    return false;
                }
            });

            if (!mHideInsert) {
                viewHolder.mInsertButton.setVisibility(View.VISIBLE);
                viewHolder.getInsertButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onInsert(file);
                    }
                });
            } else {
                viewHolder.mInsertButton.setVisibility(View.INVISIBLE);
            }


        }
    }

    @Override
    public int getItemCount() {
        return mFolders != null ? mFolders.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 1;
        else return 2;
    }

    //==============================================================================================
    //  UPDATE FUNCTIONS
    //==============================================================================================

    public void updateFolders(List<File> folders) {
        int oldSize = mFolders.size();
        mFolders = folders;
        notifyItemRangeRemoved(1, oldSize);
        notifyItemRangeInserted(1, mFolders.size());
    }

    public void addFolder(String path) {
        File folder = new File(path);
        mFolders.add(0, folder);
        notifyItemInserted(1);
    }

    public void hideInsertButton(boolean hide) {
        mHideInsert = hide;
        notifyDataSetChanged();
    }

    //==============================================================================================
    //  VIEW HOLDER CLASS
    //==============================================================================================

    public class ViewHolder extends RecyclerView.ViewHolder {


        private ImageButton mInsertButton;
        private TextView mTextView;

        public ImageButton getInsertButton() {
            return mInsertButton;
        }

        public TextView getTextView() {
            return mTextView;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mInsertButton = itemView.findViewById(R.id.insert_button);
            mTextView = itemView.findViewById(R.id.folder_name_sort_part);

        }
    }


    //==============================================================================================
    //  INTERFACES
    //==============================================================================================

    public interface OnFileItemListener {
        void onClick(File file);

        void onLongClick(File file);

        void onClickAdd();

        void onInsert(File file);
    }
}
