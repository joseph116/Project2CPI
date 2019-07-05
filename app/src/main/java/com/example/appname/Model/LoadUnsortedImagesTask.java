package com.example.appname.Model;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.appname.ViewModel.ImageViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoadUnsortedImagesTask extends AsyncTask<Void, Void, ArrayList<Image>> {


    private Context mContext;
    private OnLoadCompleteListener mLoadFinish;
    private ImageViewModel mViewModel;
    private ArrayList<Image> mTrashImages;
    private static final String[] IMAGE_PROJECTION =
            new String[]{
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.DATE_MODIFIED,
                    MediaStore.Images.ImageColumns.MIME_TYPE,
                    MediaStore.Images.ImageColumns.ORIENTATION,
            };

    public LoadUnsortedImagesTask(Context context, OnLoadCompleteListener listener) {
        mContext = context;
        mLoadFinish = listener;
    }

    @Override
    protected ArrayList<Image> doInBackground(Void... voids) {
        Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = mContext.getContentResolver().query(
                contentUri, IMAGE_PROJECTION,
                null, null,
                MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC");
        ArrayList<Image> images = new ArrayList<>();
        //if there is no image
        if (cursor == null) return images;

        //else get the images
        try {
            final int idColNum = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
            final int pathColNum = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA);
            final int dateTakenColNum = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN);
            final int dateModifiedColNum = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_MODIFIED);
            final int mimeTypeColNum = cursor.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE);
            final int orientationColNum = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);

            while (cursor.moveToNext()) {
                String path = cursor.getString(pathColNum);
                if (!path.contains(Environment.DIRECTORY_PICTURES + File.separator + "Sorted Pictures")) {
                    long id = cursor.getLong(idColNum);
                    long dateTaken = cursor.getLong(dateTakenColNum);
                    String mimeType = cursor.getString(mimeTypeColNum);
                    long dateModified = cursor.getLong(dateModifiedColNum);
                    int orientation = cursor.getInt(orientationColNum);

                    images.add(new Image(id, Uri.withAppendedPath(contentUri, Long.toString(id)),
                            path, mimeType, dateTaken, dateModified, orientation));

                }
            }
        } finally {
            cursor.close();
        }

        return images;
    }

    @Override
    protected void onPostExecute(ArrayList<Image> images) {
        mLoadFinish.loadFinished(images);
    }

    public interface OnLoadCompleteListener {
        void loadFinished(ArrayList<Image> images);
    }
}
