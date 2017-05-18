package com.kakatoto.kakaalbumpicker.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kakatoto.kakaalbumpicker.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //Data
    private List<Uri> itemList;
    private Context context;


    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setItemList(List<Uri> itemList) {
        this.itemList = itemList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_image, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int pos) {

        final ViewHolder holder = (ViewHolder) viewHolder;

        Uri uri = itemList.get(pos);

        Glide.with(context).load(uri)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView)
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
