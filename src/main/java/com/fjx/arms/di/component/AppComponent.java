package com.fjx.arms.di.component;

import android.app.Application;

import com.blankj.utilcode.util.CacheMemoryUtils;
import com.fjx.arms.base.delegate.AppDelegate;
import com.fjx.arms.di.module.AppModule;
import com.fjx.arms.di.module.ClientModule;
import com.fjx.arms.di.module.GlobalConfigModule;
import com.fjx.arms.imageloader.ImageLoader;
import com.fjx.arms.integration.IRepositoryManager;
import com.fjx.arms.integration.cache.Cache;
import com.fjx.arms.rxerror.core.RxErrorHandler;
import com.google.gson.Gson;

import java.io.File;
import java.util.concurrent.ThreadPoolExecutor;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * @author guanzhirui
 * @date 2022/7/29 15:04
 */
@Singleton
@Component(modules = {AppModule.class, ClientModule.class, GlobalConfigModule.class})
public interface AppComponent {

    Application application();

    IRepositoryManager repositoryManager();

    RxErrorHandler rxErrorHandler();

    ImageLoader imageLoader();

    OkHttpClient okHttpClient();

    Gson gson();

    File cacheFile();

    Cache<String, Object> extras();

    Cache.Factory cacheFactory();

    ThreadPoolExecutor executorService();

    void inject(AppDelegate delegate);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        Builder globalConfigModule(GlobalConfigModule globalConfigModule);

        AppComponent build();

    }

}
