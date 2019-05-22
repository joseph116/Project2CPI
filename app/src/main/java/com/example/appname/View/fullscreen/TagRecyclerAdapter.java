package com.example.appname.View.fullscreen;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appname.Model.Tag;
import com.example.appname.R;
import java.util.ArrayList;
import java.util.List;

public class TagRecyclerAdapter extends RecyclerView.Adapter<TagRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<Tag> mTags;
    private TagClickListener mListener;

    public TagRecyclerAdapter(Context context, TagClickListener listener) {
        mListener = listener;
        mContext = context;
    }

    public void setTags(List<Tag> tags) {
        mTags = tags;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.getTag().setText(mTags.get(position).getTitle());
        holder.getTag().setBackgroundTintList(ColorStateList.valueOf(mTags.get(position).getColor()));
        holder.getTag().setTextColor(autoTextColor(mTags.get(position).getColor()));
    }

    @Override
    public int getItemCount() {
        return (mTags != null)? mTags.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTag;

        public TextView getTag() {
            return mTag;
        }

        public void setTag(TextView tag) {
            mTag = tag;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTag = itemView.findViewById(R.id.tagTitle);
        }

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

    public interface TagClickListener{

        void onClickTag(int color, int position);

    }
}
