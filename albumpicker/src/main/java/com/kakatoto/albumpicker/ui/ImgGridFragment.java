package com.kakatoto.albumpicker.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kakatoto.albumpicker.AlbumPickerBuilder;
import com.kakatoto.albumpicker.R;
import com.kakatoto.albumpicker.adapter.ImgGridRecyclerAdapter;
import com.kakatoto.albumpicker.databinding.FragmentAlbumImageListBinding;
import com.kakatoto.albumpicker.presenter.ImgContract;
import com.kakatoto.albumpicker.presenter.ImgPresenter;
import com.kakatoto.albumpicker.util.CommonUtil;
import com.kakatoto.albumpicker.util.otto.BusProvider;
import com.kakatoto.albumpicker.util.otto.EventImageUpload;
import com.kakatoto.albumpicker.util.recycler.RecyclerDecorationGrid;


public class ImgGridFragment extends Fragment implements ImgContract.View {
    private final static String TAG = ImgGridFragment.class.getSimpleName();
    private FragmentAlbumImageListBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_album_image_list, container, false);
        View view = binding.getRoot();
        binding.setPresenter(new ImgPresenter(getActivity(), getArguments()));
        binding.getPresenter().attach(this);
        setAdapter();
        return view;
    }

    @Override
    public void setSelectButton() {
        if(AlbumActivity.isMulti)
            binding.btnSelect.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setTitle(String title) {
        binding.txtTitle.setText(title);
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void back() {
        new Thread(new Runnable() {
            public void run() {
                new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.getPresenter().deatch();
    }

    public void setAdapter() {
        binding.recyclerView.addItemDecoration(new RecyclerDecorationGrid(3, CommonUtil.convertDpToPx(getActivity(), 2), false));
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        ImgGridRecyclerAdapter adapter = new ImgGridRecyclerAdapter(getActivity());
        binding.getPresenter().setAdapter(adapter);
        binding.recyclerView.setAdapter(adapter);
    }
}