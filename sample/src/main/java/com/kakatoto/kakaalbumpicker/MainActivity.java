package com.kakatoto.kakaalbumpicker;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.kakatoto.kakaalbumpicker.adapter.RecyclerAdapter;
import com.kakatoto.kakaalbumpicker.databinding.ActivityMainBinding;
import com.kakatoto.kakaalbumpicker.presenter.MainContract;
import com.kakatoto.kakaalbumpicker.presenter.MainPresenter;


public class MainActivity extends AppCompatActivity implements MainContract.View {
    private final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setPresenter(new MainPresenter(this));
        binding.getPresenter().attatch(this);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);

        RecyclerAdapter adapter = new RecyclerAdapter(this);
        binding.getPresenter().setAdapter(adapter);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.getPresenter().detach();
    }

    @Override
    public void setImeageContent(Uri uri) {
        binding.imageView.setVisibility(View.VISIBLE);
        binding.imageView.setImageURI(uri);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
