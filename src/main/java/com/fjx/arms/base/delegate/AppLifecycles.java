package com.fjx.arms.base.delegate;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

/**
 * @author guanzhirui
 * @date 2022/7/29 17:11
 */
public interface AppLifecycles {

    void attachBaseContext(@NonNull Context base);

    void onCreate(@NonNull Application application);

    void onTerminate(@NonNull Application application);

}
