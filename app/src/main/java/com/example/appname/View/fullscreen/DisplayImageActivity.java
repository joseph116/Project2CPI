package com.example.appname.View.fullscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appname.Model.Image;
import com.example.appname.Model.Note;
import com.example.appname.Model.Tag;
import com.example.appname.R;
import com.example.appname.View.dialogs.AddNoteDialog;
import com.example.appname.View.dialogs.ChangeNoteTextDialog;
import com.example.appname.View.dialogs.NewTagDialog;
import com.example.appname.ViewModel.ImageViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisplayImageActivity extends AppCompatActivity
        implements ImageAdapter.ImageListener,
        AddNoteDialog.AddNoteListener,
        ChangeNoteTextDialog.ChangeNoteTextListener,
        NewTagDialog.AddTagListener,
        TagRecyclerAdapter.TagClickListener {

    private static final String TAG = "DisplayImageActivity";

    private ImageViewModel mViewModel;
    private List<Image> mImages;
    private List<Note> mNotes;
    private List<Tag> mTags;
    private List<Integer> mNoteLayoutIds;
    private int mFirstPosition;
    private ViewPager mViewPager;
    private ImageAdapter mAdapter;
    private Toolbar mTopBar;
    private Toolbar mBottomBar;
    private ActionMode mActionMode;
    private ConstraintLayout mLayout;
    private RecyclerView mAllTagRecycler;
    private TagRecyclerAdapter mAllTagsAdapter;
    private ImageButton mNewTagButton;
    private RecyclerView mImageTagsRecycler;
    private TagRecyclerAdapter mImageTagsAdapter;
    private Toast mToast;

    private boolean isNoteVisible = false;
    private boolean mBarsVisible = true;
    private boolean isTagsVisible = false;

    //Constraint sets
    private ConstraintSet show_text = new ConstraintSet();
    private ConstraintSet hide_text = new ConstraintSet();
    private ConstraintSet hide_bubble = new ConstraintSet();
    private ConstraintSet showBars = new ConstraintSet();
    private ConstraintSet hideBars = new ConstraintSet();
    private ConstraintSet showTags = new ConstraintSet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadPreferences();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        mImages = getIntent().getParcelableArrayListExtra("ARGS_CURRENT_IMAGES");
        mFirstPosition = getIntent().getIntExtra("ARGS_IMAGE_POSITION", 0);
        mViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        initViews();
        initTags();
    }

    private void initViews() {
        mNotes = new ArrayList<>();
        mNoteLayoutIds = new ArrayList<>();
        mTags = new ArrayList<>();
        mViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                mNotes = notes;
                removeAllNotes();
                initNotes();
            }
        });
        mLayout = findViewById(R.id.display_parent);

        //init constraint sets
        showBars.clone(mLayout);
        hideBars.clone(this, R.layout.activity_display_image_hide);
        hide_bubble.clone(DisplayImageActivity.this, R.layout.note_view_0);
        hide_text.clone(DisplayImageActivity.this, R.layout.note_view_1);
        show_text.clone(DisplayImageActivity.this, R.layout.note_view_2);
        showTags.clone(DisplayImageActivity.this, R.layout.activity_display_image_show_tags);

        mViewPager = findViewById(R.id.display_image_view_pager);
        mAdapter = new ImageAdapter(this, mImages, this );
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mFirstPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                removeAllNotes();
                initNotes();
                Image image = mImages.get(position);
                mImageTagsAdapter.setTags(getImageTags(image));

                //test
                //View view = mViewPager.findViewWithTag(mImages.get(position).getImageViewId());
                //ImageView v = (view instanceof ImageView) ? (ImageView) view : null;
                //if (v != null) {
                //    Drawable drawable = v.getDrawable();
                //}
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
                    case R.id.add_tag_display_option:
                        TransitionManager.beginDelayedTransition(mLayout);
                        if (!isTagsVisible) {
                            //showTags.applyTo(mLayout);
                            mAllTagRecycler.setVisibility(View.VISIBLE);
                            mNewTagButton.setVisibility(View.VISIBLE);
                        } else {
                            //showBars.applyTo(mLayout);
                            mAllTagRecycler.setVisibility(View.GONE);
                            mNewTagButton.setVisibility(View.GONE);
                        }
                        isTagsVisible = !isTagsVisible;
                        break;
                }
                return true;
            }
        });

        setSupportActionBar(mTopBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initNotes() {
        for (Note note : mNotes) {
            if (note.getImageId() == mImages.get(mViewPager.getCurrentItem()).getRowId()) {
                addNoteView(note, (isNoteVisible)? hide_text : hide_bubble);
            }
        }
    }

    private void initTags() {
        //all tags
        mAllTagRecycler = findViewById(R.id.allTagsRecycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mAllTagRecycler.setLayoutManager(gridLayoutManager);
        mAllTagsAdapter = new TagRecyclerAdapter(DisplayImageActivity.this, DisplayImageActivity.this);
        mAllTagRecycler.setAdapter(mAllTagsAdapter);
        mViewModel.getAllTags().observe(this, new Observer<List<Tag>>() {
            @Override
            public void onChanged(List<Tag> tags) {
                Image image = mImages.get(mViewPager.getCurrentItem());
                mAllTagsAdapter.setTags(tags);
                mTags = tags;
                mImageTagsAdapter.setTags(getImageTags(image));
            }
        });
        mNewTagButton = findViewById(R.id.newTagButton);
        mNewTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewTagDialog dialog = new NewTagDialog(DisplayImageActivity.this);
                dialog.show(getSupportFragmentManager(), "new tag");
            }
        });
        //image tags
        mImageTagsRecycler = findViewById(R.id.currentTagsRecycler);
        GridLayoutManager grid = new GridLayoutManager(this, 1);
        grid.setOrientation(RecyclerView.HORIZONTAL);
        mImageTagsRecycler.setLayoutManager(grid);
        mImageTagsAdapter = new TagRecyclerAdapter(DisplayImageActivity.this, imageTagsOnClickListener);
        mImageTagsRecycler.setAdapter(mImageTagsAdapter);
    }

    @Override
    public void onClickImage() {
        TransitionManager.beginDelayedTransition(mLayout);
        if (mBarsVisible) {
            hideBars.applyTo(mLayout);
            mBarsVisible = false;
            isTagsVisible = false;
        } else {
            showBars.applyTo(mLayout);
            mBarsVisible = true;
        }
    }

    @Override
    public void onDoubleClick() {
        TransitionManager.beginDelayedTransition(mLayout);
        for (int id : mNoteLayoutIds) {
            if (isNoteVisible) {
                setNoteVisibility(id, hide_bubble);
            } else {
                setNoteVisibility(id, hide_text);
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
        addNoteView(note, (isNoteVisible)? show_text : hide_bubble);
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

        viewNote.setId(mNoteLayoutIds.size());
        mNoteLayoutIds.add(viewNote.getId());
        note.setLayoutId(viewNote.getId());

        mLayout.addView(viewNote);
    }

    private void removeNoteView(Note note) {
        if (note.getLayoutId() != 0) {
            ConstraintLayout noteLayout = mLayout.findViewById(note.getLayoutId());
            mLayout.removeView(noteLayout);
            note.setLayoutId(0);
        }
    }

    private void removeNoteView(int id) {
        ConstraintLayout layout = mLayout.findViewById(id);
        if (layout != null) {
            mLayout.removeView(layout);
        }
    }

    private void removeAllNotes() {
        for (int id : mNoteLayoutIds) {
            ConstraintLayout layout = mLayout.findViewById(id);
            mLayout.removeView(layout);
        }
        mNoteLayoutIds.clear();
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
        removeNoteView(note.getLayoutId());
        addNoteView(note, show_text);
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

    @Override
    public void onNewTag(String title, int color) {
        Tag tag = new Tag(title, color);
        mViewModel.insertTag(tag);
    }

    @Override
    public void onClickTag(Tag tag) {
        Image image = mImages.get(mViewPager.getCurrentItem());
        String tags = image.getTags();
        if (!tags.contains(Integer.toString(tag.getId()))) {
            tags = (tags.equals(""))? Integer.toString(tag.getId()) : tags + "§" + tag.getId();
            image.setTags(tags);
            mImageTagsAdapter.addTag(tag);
        }
        mViewModel.update(image);
    }

    private TagRecyclerAdapter.TagClickListener imageTagsOnClickListener = new TagRecyclerAdapter.TagClickListener() {
        @Override
        public void onClickTag(Tag tag) {
            if (mAllTagRecycler.getVisibility() == View.VISIBLE) {
                mImageTagsAdapter.removeTag(tag);
                Image image = mImages.get(mViewPager.getCurrentItem());
                ArrayList<String> tagList = new ArrayList<>(Arrays.asList(image.getTags().split("§")));
                if (tagList.isEmpty() && !image.getTags().equals("")){
                    tagList.add(image.getTags());
                }
                String tagId = Integer.toString(tag.getId());
                tagList.remove(tagId);
                String tags = "";
                if (!tagList.isEmpty()) {
                    tags = tagList.get(0);
                }
                for (int i = 1; i < tagList.size(); i++){
                    tags = tags + "§" + tagList.get(i);
                }
                image.setTags(tags);
                mViewModel.update(image);
            }
        }
    };

    private ArrayList<Tag> getImageTags(Image image) {
        String[] tagIds = image.getTags().split("§");
        ArrayList<Tag> tags = new ArrayList<>();
        if (!mTags.isEmpty()) {
            for (String s : tagIds) {
                int i = 0;
                while (!s.equals(Integer.toString(mTags.get(i).getId())) && (i < mTags.size())) {
                    i++;
                    if (i == mTags.size()) break;
                }
                if (i < mTags.size()) {
                    tags.add(mTags.get(i));
                }
            }
        }
        return tags;
    }

    private void showToast(String message) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

}
