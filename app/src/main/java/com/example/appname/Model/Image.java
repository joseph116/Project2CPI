package com.example.appname.Model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * THIS IS THE DATA UNIT CLASS
 */
@Entity(tableName = "image_table")
public class Image implements Parcelable, Comparable<Image> {

    //==============================================================================================
    //  ATTRIBUTES
    //==============================================================================================

    @PrimaryKey
    private long rowId;
    @Ignore
    private Uri uri;
    private String mimeType;
    private long dateModified;
    private int orientation;
    private long dateTaken;
    private String path;
    private String oldPath;
    private String parent;
    private String tags;
    private boolean inTrash;

    @Ignore
    private int mImageViewId;

    //==============================================================================================
    //  CONSTRUCTORS
    //==============================================================================================

    public Image(){
    }

    public Image(long rowId, Uri uri, String path, String mimeType, long dateTaken, long dateModified,
          int orientation) {
        this.rowId = rowId;
        this.uri = uri;
        this.path = path;
        this.parent = path.substring(0, path.lastIndexOf(File.separator));
        this.dateModified = dateModified;
        this.mimeType = mimeType;
        this.orientation = orientation;
        this.dateTaken = dateTaken;
        this.tags = "";
        this.inTrash = false;
    }

    private Image(Parcel in) {
        rowId = in.readLong();
        //uri = Uri.parse(in.readString());
        path = in.readString();
        oldPath = in.readString();
        parent = in.readString();
        mimeType = in.readString();
        dateTaken = in.readLong();
        dateModified = in.readLong();
        orientation = in.readInt();
        tags = in.readString();
    }

    //==============================================================================================
    //  GETTERS AND SETTERS
    //==============================================================================================

    public Uri getUri() {
        return uri;
    }

    public long getDateTaken() {
        return dateTaken;
    }

    public long getRowId() {
        return rowId;
    }

    public String getMimeType() {
        return mimeType;
    }

    public long getDateModified() {
        return dateModified;
    }

    public int getOrientation() {
        return orientation;
    }

    public String getPath() {
        return path;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void setDateTaken(long dateTaken) {
        this.dateTaken = dateTaken;
    }

    public void setPath(String path) {
        this.path = path;
        this.parent = path.substring(0, path.lastIndexOf(File.separator));
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public int getImageViewId() {
        return mImageViewId;
    }

    public void setImageViewId(int imageViewId) {
        mImageViewId = imageViewId;
    }

    public String getOldPath() {
        return oldPath;
    }

    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }

    public boolean isInTrash() {
        return inTrash;
    }

    public void setInTrash(boolean inTrash) {
        this.inTrash = inTrash;
    }

    //==============================================================================================
    //  FOR PARCELABLE
    //==============================================================================================

    @Ignore
    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel parcel) {
            return new Image(parcel);
        }

        @Override
        public Image[] newArray(int i) {
            return new Image[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(rowId);
        //parcel.writeString(uri.toString());
        parcel.writeString(path);
        parcel.writeString(oldPath);
        parcel.writeString(parent);
        parcel.writeString(mimeType);
        parcel.writeLong(dateTaken);
        parcel.writeLong(dateModified);
        parcel.writeInt(orientation);
        parcel.writeString(tags);
    }

    @Override
    public String toString() {
        return "Image{"
                + "rowId=" + rowId
                + ", uri=" + uri
                + ", path=" + path
                + ", mimeType='" + mimeType + '\''
                + ", dateModified=" + dateModified
                + ", orientation=" + orientation
                + ", dateTaken=" + dateTaken
                + '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Image) {
            if (this.rowId == ((Image) obj).rowId) {
                return true;
            }
        }
        return false;
    }

    //==============================================================================================
    //  FOR COMPARE
    //==============================================================================================

    @Override
    public int compareTo(Image o) {
        return Comparators.ID.compare(this, o);
    }

    public static class Comparators {

        public static Comparator<Image> ID = new Comparator<Image>() {
            @Override
            public int compare(Image o1, Image o2) {
                if ((o1.getRowId() - o2.getRowId()) > 0) {
                    return 1;
                } else if ((o1.getRowId() - o2.getRowId()) < 0) {
                    return -1;
                } else
                    return 0;
            }
        };
        public static Comparator<Image> DATE_MODIFIED = new Comparator<Image>() {
            @Override
            public int compare(Image o1, Image o2) {
                if ((o1.getDateModified() - o2.getDateModified()) > 0) {
                    return 1;
                } else if ((o1.getDateModified() - o2.getDateModified()) < 0) {
                    return -1;
                } else
                    return 0;
            }
        };
        public static Comparator<Image> DATE_TAKEN = new Comparator<Image>() {
            @Override
            public int compare(Image o1, Image o2) {
                if ((o1.getDateTaken() - o2.getDateTaken()) > 0) {
                    return 1;
                } else if ((o1.getDateTaken() - o2.getDateTaken()) < 0) {
                    return -1;
                } else
                    return 0;
            }
        };

    }
}
