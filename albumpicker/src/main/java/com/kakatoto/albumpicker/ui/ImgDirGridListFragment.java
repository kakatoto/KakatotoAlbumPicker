package com.kakatoto.albumpicker.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kakatoto.albumpicker.R;
import com.kakatoto.albumpicker.adapter.ImgDirGridRecyclerAdapter;
import com.kakatoto.albumpicker.databinding.FragmentAlbumDirListBinding;
import com.kakatoto.albumpicker.presenter.ImgDirContract;
import com.kakatoto.albumpicker.presenter.ImgDirPresenter;
import com.kakatoto.albumpicker.util.CommonUtil;
import com.kakatoto.albumpicker.util.recycler.RecyclerDecorationGrid;


public class ImgDirGridListFragment extends Fragment implements ImgDirContract.View {
    private final String TAG = ImgDirGridListFragment.class.getSimpleName();
    private FragmentAlbumDirListBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_album_dir_list, container, false);
        View view = binding.getRoot();
        binding.setPresenter(new ImgDirPresenter(getActivity()));
        binding.getPresenter().attatch(this);
        setAdapter();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.getPresenter().deatch();
    }

    @Override
    public void back() {
        getActivity().finish();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setTitle(String title) {
        binding.txtTitle.setText(title);
    }


    public void setAdapter() {
        binding.recyclerView.addItemDecoration(new RecyclerDecorationGrid(3, CommonUtil.convertDpToPx(getActivity(), 2), false));
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        ImgDirGridRecyclerAdapter adapter = new ImgDirGridRecyclerAdapter(getActivity());
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.header_img_dir_grid, binding.recyclerView, false);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.getPresenter().onCameraClick();
            }
        });
        adapter.setHeaderView(header);
        binding.getPresenter().setAdapter(adapter);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment).addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void startActivityResult(Intent intent, int flag) {
        startActivityForResult(intent, flag);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            binding.getPresenter().onActivityResult(requestCode, data);
        }
    }
}