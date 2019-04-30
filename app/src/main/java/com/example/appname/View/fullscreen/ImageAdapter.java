package com.example.appname.View.fullscreen;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.appname.Model.Image;

import java.io.File;
import java.util.List;

public class ImageAdapter extends PagerAdapter {

    private AppCompatActivity mContext;
    private List<Image> mImages;
    private ImageListener mListener;

    public ImageAdapter(AppCompatActivity context, List<Image> images, ImageListener listener) {
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
        return (mImages == null) ? 0 : mImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        String path = mImages.get(position).getPath();
        mContext.setTitle(path.substring(path.lastIndexOf(File.separator) + 1));
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

    public interface ImageListener {

        void onClickImage(Image image);
    }
}
