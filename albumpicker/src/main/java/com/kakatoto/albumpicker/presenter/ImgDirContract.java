package com.kakatoto.albumpicker.presenter;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.kakatoto.albumpicker.adapter.ImgDirGridRecyclerAdapter;
import com.kakatoto.albumpicker.domain.DirInfo;
import com.kakatoto.albumpicker.domain.ImageInfo;

/**
 * Created by ohyowan on 2017. 4. 28..
 */

public interface ImgDirContract {
    interface View{
        void back();
        void setTitle(String title);
        void showFragment(Fragment fragment);
        void showToast(String msg);
        void startActivityResult(Intent intent, int flag);
    }

    interface Presenter{
        void attatch(View view);
        void deatch();
        void setAdapter(ImgDirGridRecyclerAdapter adapter);
        void onBackClick();
        void onCameraClick();
        void onActivityResult(int requestCode, Intent data);
        void onItemClick(DirInfo info);
    }
}
