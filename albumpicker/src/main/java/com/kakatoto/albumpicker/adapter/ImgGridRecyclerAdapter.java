package com.kakatoto.albumpicker.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kakatoto.albumpicker.R;
import com.kakatoto.albumpicker.data.SelInfo;
import com.kakatoto.albumpicker.domain.ImageInfo;
import com.kakatoto.albumpicker.presenter.ImgPresenter;
import com.kakatoto.albumpicker.ui.AlbumActivity;
import com.kakatoto.albumpicker.util.CommonUtil;

import java.io.File;
import java.util.ArrayList;

public class ImgGridRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static String TAG = ImgGridRecyclerAdapter.class.getSimpleName();
    private ImgPresenter presenter;
    private View mHeaderView = null;
    private ArrayList<ImageInfo> mUriList = new ArrayList<>();
    private String bucketName;
    private Context mContext;
    private boolean isMulti = AlbumActivity.isMulti;

    public ImgGridRecyclerAdapter(Context context) {
        this.mContext = context;
        this.bucketName = "";
    }

    public void setPresenter(ImgPresenter presenter) {
        this.presenter = presenter;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setData() {
        ImageAsyncTask imageLoader = new ImageAsyncTask();
        if (Build.VERSION.SDK_INT >= 11) {
            imageLoader.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            imageLoader.execute();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_img_grid, parent, false);
        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ImageViewHolder) {
            final ImageViewHolder VHBase = (ImageViewHolder) viewHolder;
            final ImageInfo data = mUriList.get(position);
            final Uri uri = data.getImageUri();

            if (!CommonUtil.isNull(uri)) {
                Glide.with(mContext)
                        .load(uri)
                        .override(500, 500)
                        .thumbnail(0.1f)
                        .dontAnimate()
                        .centerCrop()
                        .into(VHBase.imgCardCover);
            }

            if(isMulti) {
                if (data.getCheckYn()) {
                    VHBase.imgChecked.setVisibility(View.VISIBLE);
                } else {
                    VHBase.imgChecked.setVisibility(View.GONE);
                }
            }

            if (presenter == null)
                return;
            VHBase.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isMulti) {
                        presenter.onItemClick(data);
                    }else{
                        if (data.getCheckYn()) {
                            mUriList.get(position).setCheckYn(false);
                            SelInfo.getInstance().removeSel(data.getId());
                            notifyItemChanged(position);
                        } else {
                            mUriList.get(position).setCheckYn(true);
                            SelInfo.getInstance().addSel(data);
                            notifyItemChanged(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mUriList.size() : mUriList.size() + 1;
    }

    public interface OnItemClickListener {
        void onItemClick(ImageInfo imageInfo);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgChecked;
        public ImageView imgCardCover;

        public ImageViewHolder(View view) {
            super(view);
            imgCardCover = (ImageView) view.findViewById(R.id.img_card_cover);
            imgChecked = (ImageView) view.findViewById(R.id.iv_check);
        }
    }

    public class ImageAsyncTask extends AsyncTask<Void, Void, Void> {
        final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        final String[] projection = {MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA};
        final String selection = MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME + " = ?";
        String selectionArgs[] = {bucketName};
        final String orderBy = MediaStore.Images.Media.DATE_ADDED + " desc";

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Cursor dirCursor = mContext.getContentResolver().query(contentUri, projection, selection, selectionArgs, orderBy);
            if ((dirCursor != null) && (dirCursor.moveToFirst())) {
                do {
                    String id = dirCursor.getString(0);
                    String imgPath = dirCursor.getString(1);
                    try {
                        boolean isChecked = SelInfo.getInstance().getChecked(id);
                        mUriList.add(new ImageInfo(id, Uri.fromFile(new File(imgPath)), isChecked));
                    } catch (Exception e) {

                    }
                } while (dirCursor.moveToNext());
            }
            dirCursor.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            notifyDataSetChanged();
        }
    }
}
