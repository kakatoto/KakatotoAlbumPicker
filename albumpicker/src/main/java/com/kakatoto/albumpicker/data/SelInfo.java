package com.kakatoto.albumpicker.data;

import android.net.Uri;
import android.util.Log;

import com.kakatoto.albumpicker.domain.ImageInfo;
import com.kakatoto.albumpicker.util.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by darong on 2017. 5. 17..
 */

public class SelInfo {
    private final static String TAG = SelInfo.class.getSimpleName();
    private HashMap<String, ImageInfo> selMap = new HashMap<>();
    private static SelInfo instance;

    public static SelInfo getInstance() {
        if (instance == null)
            instance = new SelInfo();
        return instance;
    }


    public boolean getChecked(String id){
        ImageInfo info = selMap.get(id);
        if(CommonUtil.isNull(info))
            return false;
        return info.getCheckYn();
    }

    public void addSel(ImageInfo imageInfo) {
        selMap.put(imageInfo.getId(), imageInfo);
    }

    public void removeSel(String id){
        selMap.remove(id);
    }

    public ArrayList<Uri> getUriList(){
        ArrayList<Uri> uriList = new ArrayList();
        for(HashMap.Entry<String, ImageInfo> entry : selMap.entrySet()) {
            ImageInfo value = entry.getValue();
            uriList.add(value.getImageUri());
        }

        return uriList;
    }

    public int size(){
        return selMap.size();
    }

    public void clear(){
        selMap.clear();
    }
}
