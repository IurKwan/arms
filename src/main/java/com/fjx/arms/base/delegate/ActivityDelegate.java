package com.fjx.arms.base.delegate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author guanzhirui
 * @date 2022/8/1 9:53
 */
public interface ActivityDelegate {

    String ACTIVITY_DELEGATE = "ACTIVITY_DELEGATE";

    void onCreate(@Nullable Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(@NonNull Bundle outState);

    void onDestroy();

}
