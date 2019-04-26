package com.example.appname.controller.sort;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appname.R;
import java.io.File;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder>{

    //==============================================================================================
    //  ATTRIBUTES
    //==============================================================================================

    private Context mContext;
    private List<File> mFolders;
    private OnFileItemListener mListener;
    private boolean mEnableInsert = true;

    //==============================================================================================
    //  CONSTRUCTORS
    //==============================================================================================

    public FolderAdapter(Context c, List<File> folders, OnFileItemListener listener) {
        mContext = c;
        mListener = listener;
        mFolders = folders;
    }

    //==============================================================================================
    //  ADAPTER FUNCTIONS
    //==============================================================================================

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.folder_item_sort_part, viewGroup, false);
        View addView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sort_add_folder, viewGroup, false);
        ViewHolder holder = new ViewHolder((getItemViewType(i) == 1)? addView : view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        if (getItemViewType(i) == 1) {
            viewHolder.getInsertButton().setImageResource(R.drawable.ic_plus_white);
            viewHolder.getTextView().setText("Add Folder");
            viewHolder.getInsertButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add button click
                    mListener.onClickAdd();
                }

            });

        } else {
            final File file = mFolders.get(i-1);

            viewHolder.getTextView().setText(mFolders.get(i-1).getName());

            viewHolder.getTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(file);
                }

            });
            viewHolder.getTextView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onLongClick(file);
                    return false;
                }
            });
            viewHolder.getInsertButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onInsert(file);
                }
            });

            if (!mEnableInsert) viewHolder.getInsertButton().setEnabled(false);

        }
    }

    @Override
    public int getItemCount() {
        return mFolders != null ? mFolders.size()+1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 1;
        else return 2;
    }

    public void setEnableInsert(boolean enableInsert) {
        mEnableInsert = enableInsert;
    }

    //==============================================================================================
    //  UPDATE FUNCTIONS
    //==============================================================================================

    public void updateFolders(List<File> folders) {
        int oldSize = mFolders.size();
        mFolders = folders;
        notifyItemRangeRemoved(1, oldSize);
        notifyItemRangeInserted(1, mFolders.size());
    }

    public void addFolder(String path) {
        File folder = new File(path);
        mFolders.add(0, folder);
        notifyItemInserted(1);
    }

    public void removeFolder(String path) {
        File folder = new File(path);
        int position = mFolders.indexOf(folder);
        mFolders.remove(position);
        notifyItemRemoved(position + 1);
    }

    //==============================================================================================
    //  VIEW HOLDER CLASS
    //==============================================================================================

    public class ViewHolder extends RecyclerView.ViewHolder {


        private ImageButton mInsertButton;
        private TextView mTextView;

        public ImageButton getInsertButton() {
            return mInsertButton;
        }

        public TextView getTextView() {
            return mTextView;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mInsertButton = itemView.findViewById(R.id.insert_button);
            mTextView = itemView.findViewById(R.id.folder_name_sort_part);

        }
    }


    //==============================================================================================
    //  INTERFACES
    //==============================================================================================

    public interface OnFileItemListener {
        void onClick(File file);

        void onLongClick(File file);

        void onClickAdd();

        void onInsert(File file);
    }
}
