package com.example.appname.View.tags;

import com.example.appname.Model.Image;
import com.example.appname.Model.Tag;

import java.util.ArrayList;

public class TagList {

    ArrayList<Image> mImages;
    private Tag mTag;

    public TagList(Tag tag) {
        this.mTag = tag;
        this.mImages = new ArrayList<>();
    }

    public TagList(Tag tag, ArrayList<Image> images) {
        this.mTag = tag;
        this.mImages = images;
    }

    public ArrayList<Image> getImages() {
        return mImages;
    }

    public void setImages(ArrayList<Image> images) {
        mImages = images;
    }

    public Tag getTag() {
        return mTag;
    }

    public void setTag(Tag tag) {
        mTag = tag;
    }
}
