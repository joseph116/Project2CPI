package com.example.appname.View.folders;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appname.Model.Image;
import com.example.appname.R;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder>{

    private static final String TAG = "ImageAdapter";

    private Context mContext;
    private List<Image> mImageList = new ArrayList<>();
    private ImageListener mListener;
    private boolean mSelectionMode = false;
    private boolean mAllChecked = false;
    private int mFirstItemSelected;

    public ImageAdapter(Context context, ImageListener listener) {
        mContext = context;
        mListener = listener;
    }

    public ImageAdapter(Context context, List<Image> images, ImageListener listener) {
        mContext = context;
        mImageList = images;
        mListener = listener;
    }

    public void setImageList(List<Image> imageList) {
        mImageList = imageList;
        notifyDataSetChanged();
    }

    public boolean isSelectionMode() {
        return mSelectionMode;
    }

    public boolean isAllChecked() {
        return mAllChecked;
    }

    public void setAllChecked(boolean allChecked) {
        mAllChecked = allChecked;
        notifyDataSetChanged();
    }

    public void setSelectionMode(boolean selectionMode) {
        mSelectionMode = selectionMode;
        notifyDataSetChanged();
    }

    public void setFirstItemSelected(int firstItemSelected) {
        mFirstItemSelected = firstItemSelected;
        notifyDataSetChanged();
    }

    public void updateImageList(ArrayList<Image> imageList) {
        mImageList = imageList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_item_folder_part, viewGroup, false);
        ImageHolder holder = new ImageHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageHolder imageHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: started");

        //final File file = new File(mImageList.getPath());
        Glide
                .with(mContext)
                .load(mImageList.get(i).getPath())
                .override(340,340)
                .centerCrop()
                .into(imageHolder.image);

        imageHolder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mFirstItemSelected = i;
                return mListener.onLongClickImage(mImageList.get(i));
            }
        });
        imageHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSelectionMode) {
                    mListener.onClickImage(i);
                } else {
                    imageHolder.selection.setChecked(!imageHolder.selection.isChecked());
                }
            }
        });
        if (!mSelectionMode){
            imageHolder.selection.setVisibility(View.GONE);

        } else {
            imageHolder.selection.setVisibility(View.VISIBLE);
            imageHolder.selection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mListener.onChecked(mImageList.get(i), isChecked);
                }
            });
            imageHolder.selection.setChecked((i == mFirstItemSelected) || (mAllChecked));

        }

    }

    @Override
    public int getItemCount() {
        return (mImageList == null) ? 0 : mImageList.size();
    }

    public void removeImage(Image image) {
        notifyItemRemoved(mImageList.indexOf(image));
        mImageList.remove(image);
    }


    public class ImageHolder extends RecyclerView.ViewHolder {

        ImageView image;
        CheckBox selection;


        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageViewCheckable);
            selection = itemView.findViewById(R.id.selectionBox);
        }
    }

    public interface ImageListener {

        void onClickImage(int position);

        boolean onLongClickImage(Image image);

        void onChecked(Image image, boolean isChecked);
    }

}
