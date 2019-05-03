package com.example.appname.View.fullscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appname.Model.Image;
import com.example.appname.R;

import java.util.List;

public class DisplayImageActivity extends AppCompatActivity implements ImageAdapter.ImageListener {

    private List<Image> mImages;
    private int mFirstPosition;
    private ViewPager mViewPager;
    private ImageAdapter mAdapter;
    private Toolbar mTopBar;
    private Toolbar mBottomBar;

    //for notes
    private ConstraintLayout parent;
    private ConstraintLayout noteLayout;
    private ImageView bubble;
    private TextView noteTexte;
    private ConstraintSet hide_all = new ConstraintSet();
    private ConstraintSet show_all = new ConstraintSet();
    private ConstraintSet show_bubble_only = new ConstraintSet();
    private boolean isBubblesVisible = false;
    private boolean isNoteVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        mImages = getIntent().getParcelableArrayListExtra("ARGS_CURRENT_IMAGES");
        mFirstPosition = getIntent().getIntExtra("ARGS_IMAGE_POSITION", 0);
        initViews();
        initNotes();
    }

    private void initViews() {
        mViewPager = findViewById(R.id.display_image_view_pager);
        mAdapter = new ImageAdapter(this, mImages, this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mFirstPosition);

        mTopBar = findViewById(R.id.top_bar);
        mBottomBar = findViewById(R.id.bottom_bar);

        setSupportActionBar(mTopBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initNotes() {
        parent = findViewById(R.id.display_parent);
        View note = getLayoutInflater().inflate(R.layout.note_view_0, null);
        bubble = note.findViewById(R.id.bubble);
        noteTexte = note.findViewById(R.id.noteText);
        noteLayout = note.findViewById(R.id.note_layout_0);
        hide_all.clone(noteLayout);
        show_bubble_only.clone(this, R.layout.note_view_1);
        show_all.clone(this, R.layout.note_view_2);
        bubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(noteLayout);
                if (!isNoteVisible) {
                    show_all.applyTo(noteLayout);
                    isNoteVisible = true;
                } else {
                    show_bubble_only.applyTo(noteLayout);
                    isNoteVisible = false;
                }
            }
        });
        note.setX(100);
        note.setY(200);
        parent.addView(note);
    }

    @Override
    public void onClickImage(Image image) {
        TransitionManager.beginDelayedTransition(noteLayout);
        if (!isBubblesVisible) {
            show_bubble_only.applyTo(noteLayout);
            isBubblesVisible = true;
        } else {
            hide_all.applyTo(noteLayout);
            isBubblesVisible = false;
            isNoteVisible = false;
        }
    }

}
