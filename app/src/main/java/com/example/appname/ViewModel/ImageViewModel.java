package com.example.appname.ViewModel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.appname.Model.Image;
import com.example.appname.Model.ImageRepository;
import com.example.appname.Model.LoadUnsortedImagesTask;
import com.example.appname.Model.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ImageViewModel extends AndroidViewModel implements LoadUnsortedImagesTask.OnLoadCompleteListener {


    private ImageRepository mRepository;
    private LiveData<List<Image>> mSortedImages;
    private ArrayList<Image> mUnsortedImages;

    public ImageViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ImageRepository(application);
        mSortedImages = mRepository.getAllImages();
    }


    //==============================================================================================
    // SORTED FUNCTIONS
    //==============================================================================================

    public void add(Image image){
        mRepository.add(image);
    }

    public void update(Image image){
        mRepository.update(image);
    }

    public void remove(Image image){
        mRepository.remove(image);
    }

    public LiveData<List<Image>> getSortedImages() {
        return mSortedImages;
    }

    public LiveData<List<Note>> getNotes(long imageId) {
        return mRepository.getNotes(imageId);
    }

    public void insertNote(Note note) {
        mRepository.insertNote(note);
    }

    public void updateNote(Note note) { mRepository.updateNote(note);}

    public void deleteNote(Note note) {
        mRepository.deleteNote(note);
    }

    public LiveData<List<Note>> getAllNotes() {
        return mRepository.getAllNotes();
    }

    //==============================================================================================
    //  UNSORTED FUNCTIONS
    //==============================================================================================

    public ArrayList<Image> getUnsortedImages() {
        return mUnsortedImages;
    }


    public void startLoading() {
        new LoadUnsortedImagesTask(getApplication(), this).execute();
    }

    @Override
    public void loadFinished(ArrayList<Image> images) {
        mUnsortedImages = images;
    }
}
