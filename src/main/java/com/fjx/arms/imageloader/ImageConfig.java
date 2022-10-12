package com.fjx.arms.imageloader;

import android.widget.ImageView;

/**
 * @author IurKwan
 * @date 2022/9/11
 */
public class ImageConfig {

    protected Object res;
    protected ImageView imageView;
    protected int placeholder;
    protected int errorPic;

    public Object getRes() {
        return res;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public int getErrorPic() {
        return errorPic;
    }

}
