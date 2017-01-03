package com.cyy.filemanager.file;

import android.text.TextUtils;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by study on 17/1/3.
 *
 * 图片预览
 */

public class ImagePreview {

    private String[] supports = {
            ".jpg",
            ".jpeg",
            ".png",
    };


    ///这是不是一个支持可预览的图片
    public boolean isSupportPreviewImage(File file){
        if (file==null){
            return false;
        }
        if (file.isFile()){
            String suffix = file.getName().substring(file.getName().lastIndexOf("."));
            for (String support:supports) {
                if (TextUtils.equals(support , suffix.toLowerCase())){
                    return true;
                }
            }

        }
        return false;
    }

    ///加载图片到ImageView
    public void loadImage(final ImageView imageView){

    }
}
