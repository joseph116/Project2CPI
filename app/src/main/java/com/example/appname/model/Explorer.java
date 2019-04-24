package com.example.appname.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

import com.snatik.storage.Storage;
import com.snatik.storage.helpers.OrderType;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Explorer {

    //==============================================================================================
    //  ATTRIBUTES
    //==============================================================================================

    private Context mContext;
    private Storage mStorage;
    private String mCurrentPath;
    private String mRootPath; // "ExternalStoragePath/Pictures/SortedPictures"
    private int mTreeSteps;

    //for MediaStore query
    private static final String[] IMAGE_PROJECTION =
            new String[] {
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.DATE_MODIFIED,
                    MediaStore.Images.ImageColumns.MIME_TYPE,
                    MediaStore.Images.ImageColumns.ORIENTATION,
            };

    //==============================================================================================
    //  CONSTRUCTORS
    //==============================================================================================

    public Explorer(Context context) {
        mContext = context;
        mStorage = new Storage(mContext);
        mRootPath = mStorage.getExternalStorageDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "Sorted Pictures";
        mCurrentPath = mRootPath;
        mTreeSteps = 0;

    }

    //==============================================================================================
    //  GETTERS AND SETTERS
    //==============================================================================================

    public Storage getStorage() {
        return mStorage;
    }

    public String getCurrentPath() {
        return mCurrentPath;
    }


    public String getRootPath() {
        return mRootPath;
    }

    //==============================================================================================
    //  FUNCTIONS
    //==============================================================================================

    public ArrayList<File> getFolders(String newPath) {
        List<File> files = mStorage.getFiles(mCurrentPath);
        ArrayList<File> folders = new ArrayList<>();
        for (File f : files) {
            if (f.isDirectory()) {
                folders.add(f);
            }
        }
        Collections.sort(folders, OrderType.NAME.getComparator());
        return folders;
    }

    public ArrayList<MediaStoreData> getImages(String newPath) {
        Cursor cursor = mContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                null, null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        ArrayList<MediaStoreData> images = new ArrayList<>();
        //if there is no image
        if (cursor == null) return images;

        //else get the images

        return images;
    }

    public String getPreviousPath() {
        String path = mCurrentPath;
        int lastIndexOf = path.lastIndexOf(File.separator);
        if (lastIndexOf < 0) {
            return mCurrentPath;
        }
        return path.substring(0, lastIndexOf);
    }
}
