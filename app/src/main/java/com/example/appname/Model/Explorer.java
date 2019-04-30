package com.example.appname.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.example.appname.ViewModel.ImageViewModel;
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
    private Toast mToast;

    //for MediaStore query
    private static final String[] IMAGE_PROJECTION =
            new String[] {
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATA,
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


    public String getCurrentPath() {
        return mCurrentPath;
    }

    public String getRootPath() {
        return mRootPath;
    }

    //==============================================================================================
    //  FUNCTIONS
    //==============================================================================================

    public ArrayList<File> getFolders() {
        List<File> files = mStorage.getFiles(mCurrentPath);
        ArrayList<File> folders = new ArrayList<>();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    folders.add(f);
                }
            }
            Collections.sort(folders, OrderType.DATE.getComparator());
        }

        return folders;
    }

    public ArrayList<File> getFolders(String ofPath) {
        List<File> files = mStorage.getFiles(ofPath);
        ArrayList<File> folders = new ArrayList<>();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    folders.add(f);
                }
            }
            Collections.sort(folders, OrderType.DATE.getComparator());
        }

        return folders;
    }

    public ArrayList<Image> getImages() {
        Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = mContext.getContentResolver().query(
                contentUri, IMAGE_PROJECTION,
                null, null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER);
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
                if (path.substring(0, path.lastIndexOf(File.separator)).equals(mCurrentPath)) {
                    long id = cursor.getLong(idColNum);
                    long dateTaken = cursor.getLong(dateTakenColNum);
                    String mimeType = cursor.getString(mimeTypeColNum);
                    long dateModified = cursor.getLong(dateModifiedColNum);
                    int orientation = cursor.getInt(orientationColNum);

                    images.add(new Image(id, Uri.withAppendedPath(contentUri, Long.toString(id)),
                            path,mimeType, dateTaken, dateModified, orientation));
                }
            }
        } finally {
            cursor.close();
        }

        return images;
    }

    public ArrayList<Image> getImages(String ofPath) {
        Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = mContext.getContentResolver().query(
                contentUri, IMAGE_PROJECTION,
                null, null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER);
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
                if (path.substring(0, path.lastIndexOf(File.separator)).equals(ofPath)) {
                    long id = cursor.getLong(idColNum);
                    long dateTaken = cursor.getLong(dateTakenColNum);
                    String mimeType = cursor.getString(mimeTypeColNum);
                    long dateModified = cursor.getLong(dateModifiedColNum);
                    int orientation = cursor.getInt(orientationColNum);

                    images.add(new Image(id, Uri.withAppendedPath(contentUri, Long.toString(id)),
                            path,mimeType, dateTaken, dateModified, orientation));
                }
            }
        } finally {
            cursor.close();
        }

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

    public void openFolder(File file) {
        if (file.isDirectory()) {
            mTreeSteps++;
            mCurrentPath = file.getPath();
        }
    }

    public boolean goBack() {
        if (mTreeSteps > 0) {
            mCurrentPath = getPreviousPath();
            mTreeSteps--;
            return true;
        }
        return false;
    }

    public void newFolder(String name) {
        if (name.length() > 0) {
            boolean created = mStorage.createDirectory(mCurrentPath + File.separator + name);
            if (created) {
                //Toast.makeText(mContext, "New folder created: " + name, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Failed create folder: " + name, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void deleteFolder(String path) {
        if (mStorage.getFile(path).isDirectory()) {
            mStorage.deleteDirectory(path);
            Toast.makeText(mContext, "Folder deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Can't delete", Toast.LENGTH_SHORT).show();
        }
    }

    public void renameFolder(String fromPath, String toPath) {
        mStorage.rename(fromPath, toPath);
        Toast.makeText(mContext, "Renamed", Toast.LENGTH_SHORT).show();
    }

    public void showToast(String message) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public boolean backToRoot() {
        if (!mCurrentPath.equals(mRootPath)) {
            mCurrentPath = mRootPath;
            mTreeSteps = 0;
            return true;
        }
        return false;
    }

    public boolean move(String oldPath, String newPath) {
        if (mStorage.move(oldPath, newPath)) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, newPath);
            boolean successMediaStore = mContext.getContentResolver().update(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values,
                    MediaStore.MediaColumns.DATA + "='" + oldPath + "'", null) == 1;
            return true;
        }
        return false;
    }

    public void deleteImage(Image image) {
        mStorage.deleteFile(image.getPath());
        mContext.getContentResolver().delete(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.MediaColumns.DATA + "='" + image.getPath() + "'", null
        );
    }
}
