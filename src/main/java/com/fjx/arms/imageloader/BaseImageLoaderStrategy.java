package com.fjx.arms.imageloader;

import android.content.Context;

import androidx.annotation.Nullable;

/**
 * @author IurKwan
 * @date 2022/9/11
 */
public interface BaseImageLoaderStrategy<T extends ImageConfig> {

    void loadImage(Context context, T config);

    void clear(Context context, T config);

}
