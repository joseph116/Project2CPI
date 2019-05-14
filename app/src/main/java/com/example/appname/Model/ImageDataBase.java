package com.example.appname.Model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Image.class, Note.class, Tag.class}, version = 1)
public abstract class ImageDataBase extends RoomDatabase {

    private static ImageDataBase instance;

    public abstract ImageDao imageDao();

    public static synchronized ImageDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ImageDataBase.class, "image_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
