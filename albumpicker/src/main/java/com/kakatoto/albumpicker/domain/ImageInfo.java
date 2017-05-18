package com.kakatoto.albumpicker.domain;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by USER on 2017-01-31.
 */

public class ImageInfo implements Serializable {
    public ImageInfo(String id, Uri imageUri, Boolean checkYn) {
        this.id = id;
        this.imageUri = imageUri;
        this.checkYn = checkYn;
    }

    private String id;
    private Uri imageUri;
    private Boolean checkYn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Boolean getCheckYn() {
        return checkYn;
    }

    public void setCheckYn(Boolean checkYn) {
        this.checkYn = checkYn;
    }
}
