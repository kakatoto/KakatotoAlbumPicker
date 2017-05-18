package com.kakatoto.albumpicker.presenter;

import com.kakatoto.albumpicker.adapter.ImgGridRecyclerAdapter;
import com.kakatoto.albumpicker.domain.DirInfo;
import com.kakatoto.albumpicker.domain.ImageInfo;

/**
 * Created by darong on 2017. 5. 17..
 */

public interface ImgContract {
    interface View {
        void back();
        void finish();
        void setTitle(String title);
        void showToast(String msg);
        void setSelectButton();
    }

    interface Presenter {
        void attach(View view);
        void deatch();
        void setAdapter(ImgGridRecyclerAdapter adapter);
        void onBackClick();
        void onItemClick(ImageInfo info);
        void onSelect();
    }
}
