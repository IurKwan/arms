package com.fjx.arms.base.delegate;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.CacheMemoryUtils;
import com.fjx.arms.di.component.AppComponent;
import com.fjx.arms.integration.cache.Cache;

/**
 * @author guanzhirui
 * @date 2022/8/1 9:27
 */
public interface IActivity {

    @NonNull
    Cache<String, Object> provideCache();

    void setupActivityComponent(@NonNull AppComponent appComponent);

}
