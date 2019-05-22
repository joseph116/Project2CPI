package com.example.appname.View.folders;

import android.animation.Animator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.appname.R;

import java.io.File;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    //==============================================================================================
    //  ATTRIBUTES
    //==============================================================================================

    private Context mContext;
    private List<File> mFolders;
    private FolderListener mListener;

    //==============================================================================================
    //  CONSTRUCTORS
    //==============================================================================================

    public FolderAdapter(Context context, List<File> folders, FolderListener listener) {
        mContext = context;
        mFolders = folders;
        mListener = listener;
    }

    //==============================================================================================
    //  ADAPTER FUNCTIONS
    //==============================================================================================

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.folder_item_folders_part, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        if (getItemViewType(i) == 1) {
            Glide.with(mContext)
                    .load(R.drawable.ic_plus)
                    .into(viewHolder.mImage);
            viewHolder.mFileName.setText("Add folder");
            viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add button click
                    mListener.onClickAdd();
                }

            });
            viewHolder.mInfoLayout.setVisibility(View.INVISIBLE);

        } else {
            viewHolder.mInfoLayout.setVisibility(View.VISIBLE);
            final File file = mFolders.get(i-1);

            if (file.isDirectory()) {
                Glide.with(mContext)
                        .load(R.drawable.ic_folder_open)
                        .override(300, 300)
                        .into(viewHolder.mImage);
            }

            viewHolder.mFileName.setText(mFolders.get(i-1).getName());

            viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(file);
                }

            });
            viewHolder.mLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onLongClick(file);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mFolders != null ? mFolders.size()+1 : 1;
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

    public void removeFolder(String path) {
        File folder = new File(path);
        int position = mFolders.indexOf(folder);
        mFolders.remove(position);
        notifyItemRemoved(position + 1);
    }

    public void renameFolder(String fromPath, String toPath) {
        File oldFolder = new File(fromPath);
        File newFolder = new File(toPath);
        int position = mFolders.indexOf(oldFolder);
        mFolders.remove(position);
        mFolders.add(position, newFolder);
        notifyItemChanged(position + 1);
    }

    //==============================================================================================
    //  VIEW HOLDER CLASS
    //==============================================================================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mFileName;
        ImageView mImage;
        LinearLayout mInfoLayout;
        ConstraintLayout mLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mFileName = itemView.findViewById(R.id.textView);
            mImage = itemView.findViewById(R.id.folderImageView);
            mLayout = itemView.findViewById(R.id.folder_item_parent);
            mInfoLayout = itemView.findViewById(R.id.folder_info_layout);
        }
    }

    //==============================================================================================
    //  INTERFACES
    //==============================================================================================

    public interface FolderListener {
        void onClick(File file);

        void onLongClick(File file);

        void onClickAdd();
    }

}
