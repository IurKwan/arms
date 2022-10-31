package com.fjx.arms.mvp;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ActivityUtils;

/**
 * @author guanzhirui
 * @date 2022/8/17 17:14
 */
public interface IView {

    default void showLoading(String text) {

    }

    default void hideLoading() {

    }

    default void killMyself() {

    }

    default void success(@NonNull String message){

    }

    default void error(@NonNull String message){

    }

    default void launchActivity(@NonNull Intent intent) {
        ActivityUtils.startActivity(intent);
    }

}
