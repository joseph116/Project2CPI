package com.example.appname.Model;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.lifecycle.LiveData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageRepository {

    private ImageDao mImageDao;
    private LiveData<List<Image>> mAllImages;

    public ImageRepository(Application applicationt) {
        ImageDataBase imageDataBase = ImageDataBase.getInstance(applicationt);
        mImageDao = imageDataBase.imageDao();
        mAllImages = mImageDao.getAllImages();
    }

    public void add(Image image){
        new AddImageAsyncTask(mImageDao).execute(image);
    }

    public void update(Image image){
        new UpdateImageAsyncTask(mImageDao).execute(image);
    }

    public void remove(Image image){
        new RemoveImageAsyncTask(mImageDao).execute(image);
    }

    public LiveData<List<Image>> getAllImages(){
        return mAllImages;
    }

    public LiveData<List<Note>> getAllNotes() {
        return mImageDao.getAllNotes();
    }

    public LiveData<List<Note>> getNotes(long imageId) {
        return mImageDao.getNotes(imageId);
    }

    public void insertNote(Note note) {
        new InsertNoteAsyncTask(mImageDao).execute(note);
    }

    public void updateNote(Note note) {
        new UpdateNoteAsyncTask(mImageDao).execute(note);
    }

    public void deleteNote(Note note) {
        new DeleteNoteAsyncTask(mImageDao).execute(note);
    }

    private static class AddImageAsyncTask extends AsyncTask<Image, Void, Void>{

        private ImageDao mImageDao;

        private AddImageAsyncTask(ImageDao imageDao){
            mImageDao = imageDao;
        }

        @Override
        protected Void doInBackground(Image... images) {
            mImageDao.add(images[0]);
            return null;
        }
    }

    private static class UpdateImageAsyncTask extends AsyncTask<Image, Void, Void>{

        private ImageDao mImageDao;

        private UpdateImageAsyncTask(ImageDao imageDao){
            mImageDao = imageDao;
        }

        @Override
        protected Void doInBackground(Image... images) {
            mImageDao.update(images[0]);
            return null;
        }
    }

    private static class RemoveImageAsyncTask extends AsyncTask<Image, Void, Void>{

        private ImageDao mImageDao;

        private RemoveImageAsyncTask(ImageDao imageDao){
            mImageDao = imageDao;
        }

        @Override
        protected Void doInBackground(Image... images) {
            mImageDao.remove(images[0]);
            return null;
        }
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void>{

        private ImageDao mImageDao;

        private InsertNoteAsyncTask(ImageDao imageDao){
            mImageDao = imageDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mImageDao.insertNote(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void>{

        private ImageDao mImageDao;

        private UpdateNoteAsyncTask(ImageDao imageDao){
            mImageDao = imageDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mImageDao.updateNote(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void>{

        private ImageDao mImageDao;

        private DeleteNoteAsyncTask(ImageDao imageDao){
            mImageDao = imageDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mImageDao.deleteNote(notes[0]);
            return null;
        }
    }
}
