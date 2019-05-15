package com.example.appname.View.sort;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.appname.Model.Image;
import com.example.appname.R;

import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Image> mImages;
    private PagerViewListener mListener;

    public ImagePagerAdapter(Context context, List<Image> images, PagerViewListener listener) {
        mContext = context;
        mImages = images;
        mListener = listener;
    }

    public void setImages(List<Image> images) {
        mImages = images;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (mImages == null) ? 0 : mImages.size() + 1;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        if (position == mImages.size()) {
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.sort_finish_message, container, false);
            TextView textView = view.findViewById(R.id.unsorted_number);
            textView.setText((mImages.isEmpty()) ? "(You sorted all of them!)" : "(  " + mImages.size() + " images are not sorted yet)");
            Button button = view.findViewById(R.id.sort_button);
            if (mImages.isEmpty()) button.setVisibility(View.GONE);
            else {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onClickSortButton();
                    }
                });
            }
            container.addView(view, 0);
            return view;
        }
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Glide
                .with(mContext)
                .load(mImages.get(position).getPath())
                .into(imageView);
        container.addView(imageView, 0);
        return imageView;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


    public void removeImage(Image image) {
        mImages.remove(image);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public interface PagerViewListener {
        void onClickSortButton();

        void onClickImage(Image image);
    }
}
