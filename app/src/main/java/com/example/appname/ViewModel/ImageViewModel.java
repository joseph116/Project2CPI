package com.example.appname.ViewModel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.appname.Model.Image;
import com.example.appname.Model.LoadUnsortedImagesTask;

import java.util.ArrayList;

public class ImageViewModel extends AndroidViewModel implements LoadUnsortedImagesTask.OnLoadCompleteListener {

    private ArrayList<Image> mUnsortedImages;

    public ImageViewModel(@NonNull Application application) {
        super(application);
        Toast.makeText(getApplication(), "ViewModel created!", Toast.LENGTH_SHORT).show();
        new LoadUnsortedImagesTask(getApplication(), this);
    }

    public ArrayList<Image> getUnsortedImages() {
        return mUnsortedImages;
    }

    public void startLoading() {
        new LoadUnsortedImagesTask(getApplication(), this).execute();
    }

    @Override
    public void loadFinished(ArrayList<Image> images) {
        mUnsortedImages = images;
        Toast.makeText(getApplication(), "Load complete! " + images.size()
                + " unsorted pics found", Toast.LENGTH_LONG).show();
    }

}
