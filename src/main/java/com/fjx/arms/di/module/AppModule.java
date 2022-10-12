package com.fjx.arms.di.module;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fjx.arms.integration.ActivityLifecycle;
import com.fjx.arms.integration.IRepositoryManager;
import com.fjx.arms.integration.RepositoryManager;
import com.fjx.arms.integration.cache.Cache;
import com.fjx.arms.integration.cache.CacheType;
import com.fjx.arms.integration.lifecycle.ActivityLifecycleForRxLifecycle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * @author guanzhirui
 * @date 2022/7/29 15:11
 */
@Module
public abstract class AppModule {

    @Singleton
    @Provides
    static Gson provideGson(Application application, @Nullable GsonConfiguration configuration) {
        GsonBuilder builder = new GsonBuilder();
        if (configuration != null) {
            configuration.configGson(application, builder);
        }
        return new GsonBuilder().create();
    }

    @Singleton
    @Provides
    static Cache<String, Object> provideExtras(Cache.Factory cacheFactory) {
        return cacheFactory.build(CacheType.EXTRAS);
    }
    @Binds
    abstract IRepositoryManager bindRepositoryManager(RepositoryManager repositoryManager);

    @Binds
    @Named("ActivityLifecycle")
    abstract Application.ActivityLifecycleCallbacks bindActivityLifecycle(ActivityLifecycle activityLifecycle);

    @Binds
    @Named("ActivityLifecycleForRxLifecycle")
    abstract Application.ActivityLifecycleCallbacks bindActivityLifecycleForRxLifecycle(ActivityLifecycleForRxLifecycle activityLifecycleForRxLifecycle);

    public interface GsonConfiguration {
        void configGson(@NonNull Context context, @NonNull GsonBuilder builder);
    }

}
