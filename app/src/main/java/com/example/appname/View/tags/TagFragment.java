package com.example.appname.View.tags;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appname.Model.Image;
import com.example.appname.Model.Tag;
import com.example.appname.R;
import com.example.appname.ViewModel.ImageViewModel;

import java.util.ArrayList;
import java.util.List;


public class TagFragment extends Fragment {

    private ImageViewModel mViewModel;
    private RecyclerView mTagRecycler;
    private ArrayList<Tag> mTags;
    private ArrayList<Image> mImages;
    private ArrayList<TagList> mTagLists;
    private TagListAdapter mTagListAdapter;

    public TagFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(ImageViewModel.class);
        init();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tag, container, false);
    }

    private void init(){

        getActivity().setTitle("Tags");

        mViewModel.getAllTags().observe(this, new Observer<List<Tag>>() {
            @Override
            public void onChanged(List<Tag> tags) {
                mTags = (ArrayList<Tag>) tags;
                initTagList();

            }
        });

        mViewModel.getSortedImages().observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                mImages = (ArrayList<Image>) images;

            }
        });



    }

    private void initTagList() {
        mTagLists = new ArrayList<>();
        for (Tag tag : mTags) {
            mTagLists.add(new TagList(tag));
        }
        for (Image image : mImages) {
            String[] tagIds = image.getTags().split("§");
            if (tagIds.length > 0) {
                for (String tagId : tagIds) {
                    if (tagId != "") {
                        for (Tag tag : mTags) {
                            if (Integer.parseInt(tagId) == tag.getId()) {
                                int position = mTags.indexOf(tag);
                                mTagLists.get(position).getImages().add(image);
                            }
                        }
                    }

                }
            }
        }

        mTagRecycler = getView().findViewById(R.id.tagList);
        mTagListAdapter = new TagListAdapter(getContext(), mTagLists);
        mTagRecycler.setAdapter(mTagListAdapter);
        mTagRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

    }

}
