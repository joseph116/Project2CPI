package com.example.appname.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * THIS IS THE DATA UNIT CLASS
 */
public class MediaStoreData implements Parcelable {

    //==============================================================================================
    //  ATTRIBUTES
    //==============================================================================================

    final long rowId;
    final Uri uri;
    final String mimeType;
    final long dateModified;
    final int orientation;
    private final Type type;
    final long dateTaken;
    private String path;

    //==============================================================================================
    //  CONSTRUCTORS
    //==============================================================================================

    MediaStoreData(long rowId, Uri uri, String path, String mimeType, long dateTaken, long dateModified,
                   int orientation, Type type) {
        this.rowId = rowId;
        this.uri = uri;
        this.path = path;
        this.dateModified = dateModified;
        this.mimeType = mimeType;
        this.orientation = orientation;
        this.type = type;
        this.dateTaken = dateTaken;
    }

    private MediaStoreData(Parcel in) {
        rowId = in.readLong();
        uri = Uri.parse(in.readString());
        path = in.readString();
        mimeType = in.readString();
        dateTaken = in.readLong();
        dateModified = in.readLong();
        orientation = in.readInt();
        type = Type.valueOf(in.readString());
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

    public static final Creator<MediaStoreData> CREATOR = new Creator<MediaStoreData>() {
        @Override
        public MediaStoreData createFromParcel(Parcel parcel) {
            return new MediaStoreData(parcel);
        }

        @Override
        public MediaStoreData[] newArray(int i) {
            return new MediaStoreData[i];
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
        parcel.writeString(type.name());
    }

    @Override
    public String toString() {
        return "MediaStoreData{"
                + "rowId=" + rowId
                + ", uri=" + uri
                + ", path=" + path
                + ", mimeType='" + mimeType + '\''
                + ", dateModified=" + dateModified
                + ", orientation=" + orientation
                + ", type=" + type
                + ", dateTaken=" + dateTaken
                + '}';
    }

    /**
     * The type of data.
     */
    public enum Type {
        VIDEO,
        IMAGE,
    }
}
