package com.kakatoto.albumpicker.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.kakatoto.albumpicker.R;
import com.kakatoto.albumpicker.adapter.ImgGridRecyclerAdapter;
import com.kakatoto.albumpicker.data.SelInfo;
import com.kakatoto.albumpicker.domain.DirInfo;
import com.kakatoto.albumpicker.domain.ImageInfo;
import com.kakatoto.albumpicker.ui.AlbumActivity;
import com.kakatoto.albumpicker.util.CommonUtil;
import com.kakatoto.albumpicker.util.otto.BusProvider;
import com.kakatoto.albumpicker.util.otto.EventImageUpload;

/**
 * Created by darong on 2017. 5. 17..
 */

public class ImgPresenter implements ImgContract.Presenter{
    private final static String TAG = ImgPresenter.class.getSimpleName();
    public static final String DIR_INFO = "dirInfo";
    private ImgContract.View view;
    private Context context;
    private DirInfo dirInfo;

    private ImgGridRecyclerAdapter adapter;
    public ImgPresenter(Context context, Bundle bundle) {
        this.context = context;
        this.dirInfo = (DirInfo) bundle.getSerializable(DIR_INFO);
    }

    @Override
    public void attach(ImgContract.View view) {
        this.view = view;
        this.view.setTitle(AlbumActivity.title);
        this.view.setSelectButton();
    }

    @Override
    public void deatch() {
        this.view = null;
    }

    @Override
    public void setAdapter(ImgGridRecyclerAdapter adapter) {
        this.adapter = adapter;
        this.adapter.setBucketName(dirInfo.getBucketName());
        this.adapter.setPresenter(this);
        this.adapter.setData();
    }

    @Override
    public void onBackClick() {
        this.view.back();
    }

    @Override
    public void onSelect() {
        if (SelInfo.getInstance().size() != 0) {
            BusProvider.getInstance().post(new EventImageUpload());
            view.finish();
        }else{
            this.view.showToast(context.getString(R.string.select_image));
        }
    }

    @Override
    public void onItemClick(ImageInfo info) {
        if (CommonUtil.isNull(info))
            return;
        SelInfo.getInstance().addSel(info);
        BusProvider.getInstance().post(new EventImageUpload());
        view.finish();
    }
}
