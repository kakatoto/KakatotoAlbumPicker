package com.kakatoto.albumpicker.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CommonUtil {
    private static final String TAG = CommonUtil.class.getSimpleName();
    public static final String DATE_FORMAT = "yyyyMMdd_HHmmss";

    public static boolean isNull(String str) {
        return (str == null || "".equals(str))? true:false;
    }


    public static boolean isNull(Object str) {
        return (str == null || "".equals(str))? true:false;
    }

    public static int convertDpToPx(Context ctx, float dp) {
        float d = ctx.getResources().getDisplayMetrics().density;
        return (int) (d * dp);
    }

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

}
