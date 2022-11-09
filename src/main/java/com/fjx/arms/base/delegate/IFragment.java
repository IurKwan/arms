package com.fjx.arms.base.delegate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fjx.arms.di.component.AppComponent;
import com.fjx.arms.integration.cache.Cache;

/**
 * @author guanzhirui
 * @date 2022/11/9 9:30
 */
public interface IFragment {

    @NonNull
    Cache<String, Object> provideCache();

    void setupFragmentComponent(@NonNull AppComponent appComponent);

    void initData(@Nullable Bundle savedInstanceState);

    void setData(@Nullable Object data);

}
