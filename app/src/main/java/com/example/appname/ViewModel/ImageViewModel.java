package com.example.appname.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.appname.Model.Image;
import com.example.appname.Model.ImageRepository;
import com.example.appname.Model.LoadUnsortedImagesTask;
import com.example.appname.Model.Note;
import com.example.appname.Model.Tag;

import java.util.ArrayList;
import java.util.List;

public class ImageViewModel extends AndroidViewModel implements LoadUnsortedImagesTask.OnLoadCompleteListener {


    private ImageRepository mRepository;
    private LiveData<List<Image>> mSortedImages;
    private ArrayList<Image> mUnsortedImages;
    private LiveData<List<Tag>> mTags;

    public ImageViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ImageRepository(application);
        mSortedImages = mRepository.getAllImages();
        mTags = mRepository.getAllTags();
    }


    //==============================================================================================
    // SORTED FUNCTIONS
    //==============================================================================================

    public void add(Image image){
        mRepository.add(image);
    }

    public void update(Image image){
        mRepository.updateTag(image);
    }

    public void remove(Image image){
        mRepository.remove(image);
    }

    public LiveData<List<Image>> getSortedImages() {
        return mSortedImages;
    }

    public LiveData<List<Image>> getImagesByPath(String path) {
        return mRepository.getImagesByPath(path);
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

    public void insertTag(Tag tag) {
        mRepository.insertTag(tag);
    }

    public void deleteTag(Tag tag) {
        mRepository.deleteTag(tag);
    }

    public void updateTag(Tag tag) {
        mRepository.updateTag(tag);
    }

    public LiveData<List<Tag>> getAllTags() {return mRepository.getAllTags();}

    //==============================================================================================
    //  UNSORTED FUNCTIONS
    //==============================================================================================

    public ArrayList<Image> getUnsortedImages() {
        return mUnsortedImages;
    }


    public void startLoading(LoadUnsortedImagesTask.OnLoadCompleteListener listener) {
        new LoadUnsortedImagesTask(getApplication(), listener).execute();
    }

    @Override
    public void loadFinished(ArrayList<Image> images) {
        mUnsortedImages = images;
    }

    //==============================================================================================
    //  TRASH FUNCTIONS
    //==============================================================================================


    public LiveData<List<Image>> getTrashImages() {
        return mRepository.getAllImagesInTrash();
    }

}
