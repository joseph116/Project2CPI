package com.example.appname.Model;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tag_table")
public class Tag {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private int color;

    public Tag(String title, int color) {
        this.title = title;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Tag) {
            return this.id == ((Tag) obj).id;
        }
        return false;
    }
}
