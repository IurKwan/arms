package com.fjx.arms.integration;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fjx.arms.integration.cache.Cache;
import com.fjx.arms.integration.cache.CacheType;

import java.lang.reflect.Proxy;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import retrofit2.Retrofit;

/**
 * @author guanzhirui
 * @date 2022/7/29 17:22
 */
@Singleton
public class RepositoryManager implements IRepositoryManager {

    @Inject
    Lazy<Retrofit> mRetrofit;
    @Inject
    Application mApplication;
    @Inject
    @Nullable
    ObtainServiceDelegate mObtainServiceDelegate;
    @Inject
    Cache.Factory mCacheFactory;

    private Cache<String, Object> mRetrofitServiceCache;

    @Inject
    public RepositoryManager() {
    }

    @NonNull
    @Override
    public <T> T obtainRetrofitService(@NonNull Class<T> service) {
        if (mRetrofitServiceCache == null) {
            mRetrofitServiceCache = mCacheFactory.build(CacheType.RETROFIT_SERVICE_CACHE);
        }
        T retrofitService = (T) mRetrofitServiceCache.get(service.getCanonicalName());
        if (retrofitService == null) {
            if (mObtainServiceDelegate != null) {
                retrofitService = mObtainServiceDelegate.createRetrofitService(
                        mRetrofit.get(), service);
            }
            if (retrofitService == null) {
                retrofitService = (T) Proxy.newProxyInstance(
                        service.getClassLoader(),
                        new Class[]{service},
                        new RetrofitServiceProxyHandler(mRetrofit.get(), service));
            }
            mRetrofitServiceCache.put(service.getCanonicalName(), retrofitService);
        }
        return retrofitService;
    }

    @NonNull
    @Override
    public Context getContext() {
        return mApplication;
    }

}
