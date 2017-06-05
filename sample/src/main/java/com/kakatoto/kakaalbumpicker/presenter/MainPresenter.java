package com.kakatoto.kakaalbumpicker.presenter;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kakatoto.albumpicker.AlbumPickerBuilder;
import com.kakatoto.kakaalbumpicker.R;
import com.kakatoto.kakaalbumpicker.adapter.RecyclerAdapter;

import java.util.ArrayList;

public class MainPresenter implements MainContract.Presenter {
    private final String TAG = MainPresenter.class.getSimpleName();
    private MainContract.View view;
    private Context context;
    private RecyclerAdapter adapter;

    private Uri singleContent = null;
    private ArrayList<Uri> multiContents = new ArrayList<>();

    public MainPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void attatch(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public void setAdapter(RecyclerAdapter adapter) {
        this.adapter = adapter;
        this.adapter.setItemList(multiContents);

    }

    public void onSinglePicker() {
        new TedPermission(context)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        AlbumPickerBuilder albumPickerBuilder = new AlbumPickerBuilder.Builder(context).setTitle("").
                                setOnImageSelectedListener(new AlbumPickerBuilder.OnImageSingleSelectedListener() {
                                    @Override
                                    public void onSelected(ArrayList<Uri> imageList) {
                                        singleContent = imageList.get(0);
                                        view.setImeageContent(singleContent);
                                    }

                                }).build();
                        albumPickerBuilder.create();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        view.showToast(context.getString(R.string.not_allowed));
                    }
                })
                .setDeniedMessage(context.getString(R.string.need_permission))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

    }

    public void onMultiPicker() {
        new TedPermission(context)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        AlbumPickerBuilder albumPickerBuilder = new AlbumPickerBuilder.Builder(context).setTitle("").isMulti(true).
                                setOnImageSelectedListener(new AlbumPickerBuilder.OnImageSingleSelectedListener() {
                                    @Override
                                    public void onSelected(ArrayList<Uri> imageList) {
                                        multiContents.clear();
                                        for (Uri uri : imageList)
                                            multiContents.add(uri);
                                        adapter.notifyDataSetChanged();
                                    }

                                }).build();
                        albumPickerBuilder.create();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        view.showToast(context.getString(R.string.not_allowed));
                    }
                })
                .setDeniedMessage(context.getString(R.string.need_permission))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }
}
