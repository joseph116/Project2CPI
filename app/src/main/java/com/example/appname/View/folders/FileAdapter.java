package com.example.appname.View.folders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appname.R;
import com.snatik.storage.Storage;

import java.io.File;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    Context context;
    List<String> pathList;
    FileClickListener fileClickListener;
    File file;

    public FileAdapter(Context contxt, List<String> lista , FileClickListener files) {
        this.context = contxt;
        this.pathList = lista;
        this.fileClickListener = files;

    }

    @NonNull
    @Override
    public FileAdapter.FileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //The layout used is file_item.xml
        View V = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.file_item ,viewGroup , false);
        return new FileViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull FileAdapter.FileViewHolder fileViewHolder, int i) {

        String pathName = pathList.get(i);
        Storage storage = new Storage(context);
        file = storage.getFile(pathName);
        ImageView imageView = fileViewHolder.fileImage;

        //Title is the name of the file
        TextView title = fileViewHolder.fileTitle ;
        title.setText(file.getName());
        //To set the apropriate resource
        if(file.isDirectory())
            imageView.setImageResource(R.drawable.ic_folder_open);
        else{
            String extension = file.getPath().substring(file.getPath().lastIndexOf("."));
            if(extension.equals( ".jpg") ||extension.equals( ".png")||extension.equals( ".jpeg") ){
                Glide.with(context).load(file).into(imageView);
                imageView.refreshDrawableState();

            }
            else{
                imageView.setImageResource(R.drawable.ic_new_image);
                imageView.refreshDrawableState();
            }
        }


    }

    @Override
    public int getItemCount() {
        return pathList == null ? 0 : pathList.size();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView fileImage;
        TextView fileTitle;
        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileImage = (ImageView)itemView.findViewById(R.id.file_icon);
            fileTitle = (TextView)itemView.findViewById(R.id.file_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            fileClickListener.onClick(v , getAdapterPosition());
        }
    }
}
