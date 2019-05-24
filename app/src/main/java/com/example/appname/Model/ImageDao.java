package com.example.appname.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ImageDao {

    @Insert
    void add(Image image);

    @Update
    void update(Image image);

    @Delete
    void remove(Image image);

    @Query("SELECT * FROM image_table ORDER BY dateModified DESC")
    LiveData<List<Image>> getAllImages();

    @Query("SELECT * FROM image_table WHERE parent LIKE :path")
    LiveData<List<Image>> getImagesByPath(String path);

    // NOTES

    @Insert
    void insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("SELECT * FROM note_table")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM note_table WHERE imageId = :imageId")
    LiveData<List<Note>> getNotes(long imageId);

    // TAGS

    @Insert
    void insertTag(Tag tag);

    @Delete
    void deleteTag(Tag tag);

    @Update
    void updateTag(Tag tag);

    @Query("SELECT * FROM tag_table ORDER BY title")
    LiveData<List<Tag>> getAllTags();
}
