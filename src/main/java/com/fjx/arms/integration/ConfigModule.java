package com.fjx.arms.integration;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.fjx.arms.base.delegate.AppLifecycles;
import com.fjx.arms.di.module.GlobalConfigModule;

import java.util.List;

/**
 * @author guanzhirui
 * @date 2022/8/1 15:02
 */
public interface ConfigModule {

    void applyOptions(@NonNull Context context, @NonNull GlobalConfigModule.Builder builder);

    void injectAppLifecycle(@NonNull Context context, @NonNull List<AppLifecycles> lifecycles);

    void injectActivityLifecycle(@NonNull Context context, @NonNull List<Application.ActivityLifecycleCallbacks> lifecycles);

}
