package com.example.appname.View.folders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appname.Model.Image;
import com.example.appname.R;

import java.io.File;
import java.util.List;

public class MoveToAdapter extends RecyclerView.Adapter<MoveToAdapter.ViewHolder> {

    private Context mContext;
    private List<File> mFolders;
    private List<Image> mImages;
    private MoveToListener mListener;

    public MoveToAdapter(Context context, List<File> folders, List<Image> images, MoveToListener listener) {
        mContext = context;
        mFolders = folders;
        mImages = images;
        mListener = listener;
    }

    public void setFolders(List<File> folders) {
        int oldSize = mFolders.size();
        mFolders = folders;
        notifyItemRangeRemoved(0, oldSize);
        notifyItemRangeInserted(0, mFolders.size());
    }

    public void setImages(List<Image> images) {
        int oldSize = mImages.size();
        mImages = images;
        notifyItemRangeRemoved(0, oldSize);
        notifyItemRangeInserted(0, mImages.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.move_to_folder_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (position < mFolders.size()) {
            holder.mFileName.setText(mFolders.get(position).getName());
            holder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickFolder(mFolders.get(position));
                }
            });
        } else {
            Glide
                    .with(mContext)
                    .load(mImages.get(position - mFolders.size()).getPath())
                    .into(holder.mImage);
            String imagePath = mImages.get(position - mFolders.size()).getPath();
            String imageName = imagePath.substring(imagePath.lastIndexOf(File.separator) + 1);
            holder.mFileName.setText(imageName);
        }
    }

    @Override
    public int getItemCount() {
        return mFolders.size() + mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mFileName;
        ImageView mImage;
        ConstraintLayout mLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mFileName = itemView.findViewById(R.id.moveToFolderName);
            mImage = itemView.findViewById(R.id.moveToImageView);
            mLayout = itemView.findViewById(R.id.moveToItemParent);
        }
    }

    public void addFolder(String path) {
        File folder = new File(path);
        mFolders.add(0, folder);
        notifyItemInserted(1);
    }

    public interface MoveToListener {

        void onClickFolder(File folder);

    }
}
