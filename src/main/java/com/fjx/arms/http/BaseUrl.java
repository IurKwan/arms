package com.fjx.arms.http;

import androidx.annotation.NonNull;

import okhttp3.HttpUrl;

/**
 * @author guanzhirui
 * @date 2022/8/9 10:35
 */
public interface BaseUrl {

    @NonNull
    HttpUrl url();

}
