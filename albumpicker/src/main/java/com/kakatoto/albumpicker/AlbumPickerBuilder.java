package com.kakatoto.albumpicker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.kakatoto.albumpicker.data.SelInfo;
import com.kakatoto.albumpicker.ui.AlbumActivity;
import com.kakatoto.albumpicker.util.otto.BusProvider;
import com.kakatoto.albumpicker.util.otto.EventImageUpload;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class AlbumPickerBuilder {
    public static final String TITLE = "title";
    public static final String IS_MULTI = "isMulti";
    private Context context;
    private String title;
    private boolean isMulti;
    private OnImageSingleSelectedListener singleListener;

    private AlbumPickerBuilder(Builder builder){
        this.context = builder.context;
        this.singleListener = builder.onImageSingleSelectedListener;
        this.title = builder.title;
        this.isMulti = builder.isMulti;
        BusProvider.getInstance().register(this);
    }

    public static class Builder {
        private Context context;
        private String title;
        private boolean isMulti;
        private OnImageSingleSelectedListener onImageSingleSelectedListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder isMulti(boolean isMulti) {
            this.isMulti = isMulti;
            return this;
        }

        public Builder setOnImageSelectedListener(OnImageSingleSelectedListener onImageSingleSelectedListener) {
            this.onImageSingleSelectedListener = onImageSingleSelectedListener;
            return this;
        }

        public AlbumPickerBuilder build() {
            return new AlbumPickerBuilder(this);
        }
    }

    @Subscribe
    public void onEventImageUpload(EventImageUpload event){
        singleListener.onSelected(SelInfo.getInstance().getUriList());
    }

    public void create(){
        SelInfo.getInstance().clear();
        if(singleListener == null)
            throw new RuntimeException("You have to use setOnImageSelectedListener() for receive selected Uri");

        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(IS_MULTI, isMulti);
        context.startActivity(intent);
    }

    public interface OnImageSingleSelectedListener {
        void onSelected(ArrayList<Uri> imageList);
    }
}
