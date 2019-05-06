package com.example.appname.View.fullscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appname.Model.Image;
import com.example.appname.Model.Note;
import com.example.appname.R;
import com.example.appname.View.dialogs.AddNoteDialog;
import com.example.appname.View.dialogs.ChangeNoteTextDialog;
import com.example.appname.ViewModel.ImageViewModel;

import java.util.ArrayList;
import java.util.List;

public class DisplayImageActivity extends AppCompatActivity
        implements ImageAdapter.ImageListener,
        AddNoteDialog.AddNoteListener,
        ChangeNoteTextDialog.ChangeNoteTextListener {

    private static final String TAG = "DisplayImageActivity";

    private ImageViewModel mViewModel;
    private List<Image> mImages;
    private List<Note> mNotes;
    private List<View> mNoteViews;
    private int mFirstPosition;
    private ViewPager mViewPager;
    private ImageAdapter mAdapter;
    private Toolbar mTopBar;
    private Toolbar mBottomBar;
    private ActionMode mActionMode;
    private ConstraintLayout mLayout;
    private ConstraintSet showBars = new ConstraintSet();
    private ConstraintSet hideBars = new ConstraintSet();
    private boolean mBarsVisible = true;

    //for notes
    private ConstraintSet show_text = new ConstraintSet();
    private ConstraintSet hide_text = new ConstraintSet();
    private boolean isNoteVisible = false;
    private ConstraintLayout mNoteLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        mImages = getIntent().getParcelableArrayListExtra("ARGS_CURRENT_IMAGES");
        mFirstPosition = getIntent().getIntExtra("ARGS_IMAGE_POSITION", 0);
        mViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        initViews();
    }

    private void initViews() {
        mNotes = new ArrayList<>();
        mNoteViews = new ArrayList<>();
        mViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                mNotes = notes;
            }
        });
        mLayout = findViewById(R.id.display_parent);
        showBars.clone(mLayout);
        hideBars.clone(this, R.layout.activity_display_image_hide);
        mNoteLayout = mLayout.findViewById(R.id.note_layout);
        hide_text.clone(DisplayImageActivity.this, R.layout.note_view_1);
        show_text.clone(DisplayImageActivity.this, R.layout.note_view_2);

        mViewPager = findViewById(R.id.display_image_view_pager);
        mAdapter = new ImageAdapter(this, mImages, this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mFirstPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (Note note : mNotes) {
                    //mNoteViews.clear();
                    removeNoteView(note);
                    //if (note.getImageId() == mImages.get(mViewPager.getCurrentItem()).getRowId()) {
                    //    addNoteView(note, hide_text);
                    //}
                }
                isNoteVisible = false;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mTopBar = findViewById(R.id.top_bar);
        mBottomBar = findViewById(R.id.bottom_bar);
        mBottomBar.inflateMenu(R.menu.display_menu);
        mBottomBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_note_option:
                        //AddNoteDialog dialog = new AddNoteDialog(DisplayImageActivity.this);
                        //dialog.show(getSupportFragmentManager(), "add note");
                        break;
                    case R.id.delete_display_option:
                        break;
                }
                return true;
            }
        });

        setSupportActionBar(mTopBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onClickImage() {
        TransitionManager.beginDelayedTransition(mLayout);
        if (mBarsVisible) {
            hideBars.applyTo(mLayout);
            mBarsVisible = false;
        } else {
            showBars.applyTo(mLayout);
            mBarsVisible = true;
        }
    }

    @Override
    public void onDoubleClick() {
        TransitionManager.beginDelayedTransition(mLayout);
        for (Note note : mNotes) {
            if (note.getImageId() == mImages.get(mViewPager.getCurrentItem()).getRowId()) {
            if (!isNoteVisible) {
                addNoteView(note, hide_text);
            } else {
                removeNoteView(note);
            }
        }
        }
        isNoteVisible = !isNoteVisible;
    }

    @Override
    public void onLongClickImage(MotionEvent event) {
        AddNoteDialog.newInstance(event.getX(), event.getY(), this).show(getSupportFragmentManager(), "add note");
    }

    @Override
    public void onAddNote(String text, float x, float y) {
        int position = mViewPager.getCurrentItem();
        Note note = new Note(mImages.get(position).getRowId(), text, x, y);
        mViewModel.insertNote(note);
        //show notes
        addNoteView(note, show_text);
    }


    private ActionMode.Callback mCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Tap where to add the note");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

    private void addNoteView(final Note note, ConstraintSet visibility) {
        final View viewNote = getLayoutInflater().inflate(R.layout.note_view_1, null);
        ImageView bubble = viewNote.findViewById(R.id.bubble);
        TextView noteTexte = viewNote.findViewById(R.id.noteText);
        noteTexte.setText(note.getText());
        final ConstraintLayout noteLayout = viewNote.findViewById(R.id.note_layout);
        bubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(mLayout);
                if (!note.isTextVisible()) {
                    show_text.applyTo(noteLayout);
                    note.setTextVisible(true);
                } else {
                    hide_text.applyTo(noteLayout);
                    note.setTextVisible(false);
                }
            }
        });
        noteTexte.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ChangeNoteTextDialog.newInstance(note.getText(), note.getId(), DisplayImageActivity.this).show(getSupportFragmentManager(), "change note txt");
                return true;
            }
        });
        bubble.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });
        viewNote.setX(note.getX());
        viewNote.setY(note.getY());
        visibility.applyTo(noteLayout);
        //mNoteViews.add(viewNote);
        note.setLayoutId(noteLayout.getId());
        mLayout.addView(viewNote);
    }

    private void removeNoteView(Note note) {
        if (note.getLayoutId() != 0) {
            ConstraintLayout noteLayout = mLayout.findViewById(note.getLayoutId());
            mLayout.removeView(noteLayout);
            note.setLayoutId(0);
        }
    }

    private void removeNoteViews() {
        for (View view : mNoteViews) {
            mLayout.removeView(view);
            mNoteViews.remove(view);
        }
    }

    @Override
    public void onChangeNoteText(String newText, long noteId) {
        int index = 0;
        while (mNotes.get(index).getId() != noteId) {
            index++;
        }
        Note note = mNotes.get(index);
        note.setText(newText);
        mViewModel.updateNote(note);
        for (Note n : mNotes) {
            removeNoteView(n);
            if (n.getImageId() == mImages.get(mViewPager.getCurrentItem()).getRowId()) {
                addNoteView(n, (n.isTextVisible()) ? show_text : hide_text);
            }
        }
    }

    private void setNoteVisibility(int layoutId, ConstraintSet visibility) {
        ConstraintLayout layout = mLayout.findViewById(layoutId);
        if (layout != null) {
            visibility.applyTo(layout);
        }
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sharedPreferences.getString("theme", "1");
        switch (theme) {
            case "1":
                setTheme(R.style.Theme1);
                break;
            case "2":
                setTheme(R.style.Theme2);
            default:
                break;
        }
    }
}
