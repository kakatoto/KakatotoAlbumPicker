package com.kakatoto.albumpicker.domain;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by USER on 2017-01-31.
 */

public class DirInfo implements Serializable, Parcelable {

    private String bucketName;
    private String bucketPath;

    public String getBucketPath() {
        return bucketPath;
    }

    public void setBucketPath(String bucketPath) {
        this.bucketPath = bucketPath;
    }

    private Uri imageUri;
    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public DirInfo() {
    }


    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
