package com.example.appname.Model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * THIS IS THE DATA UNIT CLASS
 */
public class Image implements Parcelable {

    //==============================================================================================
    //  ATTRIBUTES
    //==============================================================================================

    final long rowId;
    final Uri uri;
    final String mimeType;
    final long dateModified;
    final int orientation;
    final long dateTaken;
    private String path;

    //==============================================================================================
    //  CONSTRUCTORS
    //==============================================================================================

    Image(long rowId, Uri uri, String path, String mimeType, long dateTaken, long dateModified,
          int orientation) {
        this.rowId = rowId;
        this.uri = uri;
        this.path = path;
        this.dateModified = dateModified;
        this.mimeType = mimeType;
        this.orientation = orientation;
        this.dateTaken = dateTaken;
    }

    private Image(Parcel in) {
        rowId = in.readLong();
        uri = Uri.parse(in.readString());
        path = in.readString();
        mimeType = in.readString();
        dateTaken = in.readLong();
        dateModified = in.readLong();
        orientation = in.readInt();
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

    //==============================================================================================
    //  FOR PARCELABLE
    //==============================================================================================

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
        parcel.writeString(uri.toString());
        parcel.writeString(path);
        parcel.writeString(mimeType);
        parcel.writeLong(dateTaken);
        parcel.writeLong(dateModified);
        parcel.writeInt(orientation);
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

}
