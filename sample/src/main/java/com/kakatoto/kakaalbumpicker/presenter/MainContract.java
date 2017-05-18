package com.kakatoto.kakaalbumpicker.presenter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;

import com.kakatoto.kakaalbumpicker.adapter.RecyclerAdapter;

public interface MainContract {
    interface View{
        void showToast(String msg);
        void setImeageContent(Uri uri);
    }
    interface Presenter{
        void attatch(View view);
        void detach();
        void setAdapter(RecyclerAdapter adapter);
    }
}
