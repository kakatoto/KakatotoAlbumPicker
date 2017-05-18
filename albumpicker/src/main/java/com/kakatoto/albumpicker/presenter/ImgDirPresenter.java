package com.kakatoto.albumpicker.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kakatoto.albumpicker.R;
import com.kakatoto.albumpicker.data.SelInfo;
import com.kakatoto.albumpicker.domain.ImageInfo;
import com.kakatoto.albumpicker.ui.AlbumActivity;
import com.kakatoto.albumpicker.ui.ImgGridFragment;
import com.kakatoto.albumpicker.adapter.ImgDirGridRecyclerAdapter;
import com.kakatoto.albumpicker.domain.DirInfo;
import com.kakatoto.albumpicker.util.CommonUtil;
import com.kakatoto.albumpicker.util.otto.BusProvider;
import com.kakatoto.albumpicker.util.otto.EventImageUpload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by ohyowan on 2017. 4. 28..
 */

public class ImgDirPresenter implements ImgDirContract.Presenter {
    public static final int REQUEST_SELECT_CAMERA = 0x01;
    public static final String DIR_INFO = "dirInfo";
    private final String TAG = ImgDirPresenter.class.getSimpleName();
    private ImgDirContract.View view;
    private Context context;
    private ImgDirGridRecyclerAdapter adapter;
    private File mCameraFile;

    public ImgDirPresenter(Context context) {
        this.context = context;
        this.mCameraFile = null;
    }

    @Override
    public void attatch(ImgDirContract.View view) {
        this.view = view;
        this.view.setTitle(AlbumActivity.title);
    }

    @Override
    public void deatch() {
        this.view = null;
    }


    @Override
    public void setAdapter(ImgDirGridRecyclerAdapter adapter) {
        this.adapter = adapter;
        this.adapter.setPresenter(this);
        this.adapter.setData();
    }

    @Override
    public void onBackClick() {
        this.view.back();
    }

    @Override
    public void onCameraClick() {
        new TedPermission(context)
                .setPermissionListener(cameraPermissionListener)
                .setDeniedMessage(context.getString(R.string.need_permission))
                .setPermissions(Manifest.permission.CAMERA)
                .check();
    }

    PermissionListener cameraPermissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                mCameraFile = null;
                try {
                    mCameraFile = CommonUtil.createImageFile(context);
                } catch (IOException ex) {
                    Log.i(TAG, ex.toString());
                }
                if (mCameraFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", mCameraFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    view.startActivityResult(takePictureIntent, REQUEST_SELECT_CAMERA);
                }
            }
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            view.showToast(context.getString(R.string.not_allowed));
        }
    };

    @Override
    public void onActivityResult(int requestCode, Intent data) {
        switch (requestCode) {
            case ImgDirPresenter.REQUEST_SELECT_CAMERA:
                if (!CommonUtil.isNull(mCameraFile)) {
                    Uri piuUri = Uri.fromFile(mCameraFile);
                    if (piuUri != null) {
                        SelInfo.getInstance().addSel(new ImageInfo(null, piuUri, false));
                        BusProvider.getInstance().post(new EventImageUpload());
                        view.back();
                    }
                }
                break;
        }
    }

    @Override
    public void onItemClick(DirInfo info) {
        ImgGridFragment fragment = new ImgGridFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DIR_INFO, info);
        fragment.setArguments(bundle);
        view.showFragment(fragment);
    }
}
