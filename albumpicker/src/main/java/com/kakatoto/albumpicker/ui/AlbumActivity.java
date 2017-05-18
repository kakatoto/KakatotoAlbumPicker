package com.kakatoto.albumpicker.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.kakatoto.albumpicker.R;
import com.kakatoto.albumpicker.util.otto.BusProvider;


/**
 * Created by USER on 2017-01-31.
 */

public class AlbumActivity extends AppCompatActivity {
    private static final String TAG = AlbumActivity.class.getSimpleName();
    public static final String TITLE = "title";
    public static final String IS_MULTI = "isMulti";
    public static String title = "";
    public static Boolean isMulti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
        setContentView(R.layout.activity_album_picker);

        title = getIntent().getStringExtra(TITLE);
        isMulti = getIntent().getBooleanExtra(IS_MULTI, false);

        Bundle bundle = new Bundle();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ImgDirGridListFragment fragment = new ImgDirGridListFragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.detatch();
    }
}