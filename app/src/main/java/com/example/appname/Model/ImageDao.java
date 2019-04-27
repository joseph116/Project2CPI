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
}
