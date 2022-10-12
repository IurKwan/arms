package com.fjx.arms.integration;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import retrofit2.Retrofit;

/**
 * @author guanzhirui
 * @date 2022/7/29 17:03
 */
public interface IRepositoryManager {

    @NonNull
    <T> T obtainRetrofitService(@NonNull Class<T> service);

    @NonNull
    Context getContext();

    interface ObtainServiceDelegate {

        @Nullable
        <T> T createRetrofitService(Retrofit retrofit, Class<T> serviceClass);
    }

}
