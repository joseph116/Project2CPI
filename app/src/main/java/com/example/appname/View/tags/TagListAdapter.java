package com.example.appname.View.tags;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appname.Model.Image;
import com.example.appname.R;
import com.example.appname.View.folders.ImageAdapter;

import java.util.ArrayList;

public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.ViewHolder> implements ImageAdapter.ImageListener {

    private Context mContext;
    private ArrayList<TagList> mTagLists;

    public TagListAdapter(Context context, ArrayList<TagList> tagLists) {
        mContext = context;
        mTagLists = tagLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tagTextView.setText(mTagLists.get(position).getTag().getTitle());
        holder.tagTextView.setBackgroundTintList(ColorStateList.valueOf(mTagLists.get(position).getTag().getColor()));
        holder.tagTextView.setTextColor(autoTextColor(mTagLists.get(position).getTag().getColor()));

        holder.imageAdapter = new ImageAdapter(mContext, mTagLists.get(position).getImages(),this);
        holder.tagImagesRecycler.setAdapter(holder.imageAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        holder.tagImagesRecycler.setLayoutManager(gridLayoutManager);

        holder.tagMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return (mTagLists == null) ? 0 : mTagLists.size();
    }

    private int autoTextColor(int color) {
        int r = (color >> 16) & 0xff;
        int g = (color >>  8) & 0xff;
        int b = (color      ) & 0xff;
        double luminance = ( 0.299 * r + 0.587 * g + 0.114 * b)/255;
        if (luminance > 0.5) {
            return ContextCompat.getColor(mContext, R.color.black);
        } else {
            return ContextCompat.getColor(mContext, R.color.white);
        }
    }

    @Override
    public void onClickImage(int position) {

    }

    @Override
    public boolean onLongClickImage(Image image) {
        return false;
    }

    @Override
    public void onChecked(Image image, boolean isChecked) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tagTextView;
        ImageButton tagMenu;
        RecyclerView tagImagesRecycler;
        ImageAdapter imageAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tagTextView = itemView.findViewById(R.id.tagTitleInTags);
            tagMenu = itemView.findViewById(R.id.tagMenu);
            tagImagesRecycler = itemView.findViewById(R.id.tagRecyclerView);
        }
    }
}
