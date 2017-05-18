package com.kakatoto.albumpicker.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.kakatoto.albumpicker.R;
import com.kakatoto.albumpicker.domain.DirInfo;
import com.kakatoto.albumpicker.presenter.ImgDirPresenter;
import com.kakatoto.albumpicker.util.CommonUtil;

import java.io.File;
import java.util.ArrayList;


public class ImgDirGridRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static String TAG  = ImgDirGridRecyclerAdapter.class.getSimpleName();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;

    private View mHeaderView = null;
    private ArrayList<DirInfo> mDirList = new ArrayList<>();
    private ImgDirPresenter presenter;

    private Context mContext;

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    public void setPresenter(ImgDirPresenter presenter) {
        this.presenter = presenter;
    }

    public ImgDirGridRecyclerAdapter(Context context) {
        this.mContext = context;

    }

    public void setData(){
        ImageDirAsyncTask imageDir = new ImageDirAsyncTask();
        if (Build.VERSION.SDK_INT >= 11) {
            imageDir.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            imageDir.execute();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ImageViewHolder(mHeaderView);
        } else if (viewType == TYPE_NORMAL) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_img_dir_grid, parent, false);
            return new ImageViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;
        if (viewHolder instanceof ImageViewHolder) {
            final ImageViewHolder VHBase = (ImageViewHolder) viewHolder;
            final int pos = getRealPosition(VHBase);
            final DirInfo data = mDirList.get(pos);

            if (!CommonUtil.isNull(data.getBucketName())) {
                VHBase.fraCountInfo.setVisibility(View.VISIBLE);
                VHBase.txtTitle.setText(data.getBucketName());
            } else {
                VHBase.fraCountInfo.setVisibility(View.GONE);
            }

            if (!CommonUtil.isNull(data.getCount())) {
                VHBase.txtImageCount.setText("(" + data.getCount() + ")");
            }

            if (!CommonUtil.isNull(data.getImageUri())) {
                Glide.with(mContext)
                        .load(data.getImageUri())
                        .thumbnail(0.1f)
                        .override(500, 500)
                        .dontAnimate()
                        .centerCrop()
                        .into(VHBase.imgCardCover);
            }

            if (presenter == null) return;
            VHBase.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onItemClick(data);
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }


    @Override
    public int getItemCount() {
        return mHeaderView == null ? mDirList.size() : mDirList.size() + 1;
    }



    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout fraCountInfo;
        public ImageView imgCardCover;
        public TextView txtTitle;
        public TextView txtImageCount;

        public ImageViewHolder(View view) {
            super(view);
            fraCountInfo = (FrameLayout) view.findViewById(R.id.fraCountInfo);
            imgCardCover = (ImageView) view.findViewById(R.id.img_card_cover);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtImageCount = (TextView) view.findViewById(R.id.txtImgCount);
        }
    }

    public class ImageDirAsyncTask extends AsyncTask<Void, Void, Void> {
        final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        final String[] projection = {"distinct " + MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        String[] projection2 = {
                "distinct replace(" + MediaStore.Images.Media.DATA + ", " + MediaStore.Images.Media.DISPLAY_NAME + ", '')",
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                "count(" + MediaStore.Images.Media._ID + ")",
        };
        final String selection = "";
        final String[] selectionArgs = null;
        final String orderBy = MediaStore.Images.Media.DATE_ADDED + " ASC";

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //디렉토리 구성
            Cursor dirCursor = mContext.getContentResolver().query(contentUri, projection, selection, selectionArgs, orderBy);
            if ((dirCursor != null) && (dirCursor.moveToFirst())) {
                do {
                    final String bucketName = dirCursor.getString(0);
                    Cursor dirCurosr2 = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection2, MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "=?", new String[]{bucketName}, MediaStore.Images.Media.DATE_ADDED + " DESC limit 1");
                    if (dirCurosr2 != null && dirCurosr2.getCount() > 0) {
                        dirCurosr2.moveToFirst();
                        while (!dirCurosr2.isAfterLast()) {
                            if (!"".contentEquals(dirCurosr2.getString(0)) && !"".contentEquals(dirCurosr2.getString(1)) && !"".contentEquals(dirCurosr2.getString(2)) && !"".contentEquals(dirCurosr2.getString(3)) && !"".contentEquals(dirCurosr2.getString(4))) {
                                try {
                                    final String _bucketPath = dirCurosr2.getString(0);
                                    final String _id = dirCurosr2.getString(1);
                                    final String _bucketName = dirCurosr2.getString(2);
                                    final String imgUri = dirCurosr2.getString(3);
                                    final String count = dirCurosr2.getString(4);
                                    Log.d(TAG, "_id " + _id + "bucketName : " + _bucketPath + " imgUri : " + imgUri + " count : " + count);
                                    DirInfo info = new DirInfo();
                                    info.setBucketName(_bucketName);
                                    info.setBucketPath(_bucketPath);
                                    info.setImageUri(Uri.fromFile(new File(imgUri)));

                                    info.setCount(count);
                                    mDirList.add(info);
                                } catch (Exception e) {
                                }
                            }
                            dirCurosr2.moveToNext();
                        }
                        dirCurosr2.close();
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
