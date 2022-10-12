package com.fjx.arms.di.module;

import android.app.Application;
import android.os.Environment;

import androidx.annotation.Nullable;

import com.fjx.arms.http.BaseUrl;
import com.fjx.arms.http.GlobalHttpHandler;
import com.fjx.arms.http.subscriber.ResponseErrorImpl;
import com.fjx.arms.imageloader.BaseImageLoaderStrategy;
import com.fjx.arms.integration.IRepositoryManager;
import com.fjx.arms.integration.cache.Cache;
import com.fjx.arms.integration.cache.CacheType;
import com.fjx.arms.integration.cache.IntelligentCache;
import com.fjx.arms.integration.cache.LruCache;
import com.fjx.arms.log.DefaultFormatPrinter;
import com.fjx.arms.log.FormatPrinter;
import com.fjx.arms.log.RequestInterceptor;
import com.fjx.arms.rxerror.handler.listener.ResponseErrorListener;
import com.fjx.arms.util.ArmExecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;

/**
 * @author guanzhirui
 * @date 2022/8/1 11:11
 */
@Module
public class GlobalConfigModule {
    private final BaseUrl mBaseUrl;
    private final HttpUrl mApiUrl;
    private BaseImageLoaderStrategy mLoaderStrategy;
    private final GlobalHttpHandler mHandler;
    private final List<Interceptor> mInterceptors;
    private final ResponseErrorListener mErrorListener;
    private final File mCacheFile;
    private final ClientModule.RetrofitConfiguration mRetrofitConfiguration;
    private final ClientModule.OkhttpConfiguration mOkhttpConfiguration;
    private final AppModule.GsonConfiguration mGsonConfiguration;
    private final RequestInterceptor.Level mPrintHttpLogLevel;
    private final FormatPrinter mFormatPrinter;
    private final Cache.Factory mCacheFactory;
    private final ThreadPoolExecutor mThreadPoolExecutor;
    private IRepositoryManager.ObtainServiceDelegate mObtainServiceDelegate;

    private GlobalConfigModule(Builder builder) {
        this.mApiUrl = builder.apiUrl;
        this.mBaseUrl = builder.baseUrl;
        this.mLoaderStrategy = builder.loaderStrategy;
        this.mHandler = builder.handler;
        this.mInterceptors = builder.interceptors;
        this.mErrorListener = builder.responseErrorListener;
        this.mCacheFile = builder.cacheFile;
        this.mRetrofitConfiguration = builder.retrofitConfiguration;
        this.mOkhttpConfiguration = builder.okhttpConfiguration;
        this.mGsonConfiguration = builder.gsonConfiguration;
        this.mPrintHttpLogLevel = builder.printHttpLogLevel;
        this.mFormatPrinter = builder.formatPrinter;
        this.mCacheFactory = builder.cacheFactory;
        this.mThreadPoolExecutor = builder.threadPoolExecutor;
        this.mObtainServiceDelegate = builder.obtainServiceDelegate;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Singleton
    @Provides
    @Nullable
    List<Interceptor> provideInterceptors() {
        return mInterceptors;
    }

    @Singleton
    @Provides
    HttpUrl provideBaseUrl() {
        return mApiUrl;
    }

    @Singleton
    @Provides
    @Nullable
    BaseImageLoaderStrategy provideImageLoaderStrategy() {
        return mLoaderStrategy;
    }

    @Singleton
    @Provides
    @Nullable
    GlobalHttpHandler provideGlobalHttpHandler() {
        return mHandler;
    }

    @Singleton
    @Provides
    File provideCacheFile(Application application) {
        if (mCacheFile == null) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return application.getExternalCacheDir();
            } else {
                return application.getCacheDir();
            }
        }
        return mCacheFile;
    }

    @Singleton
    @Provides
    ResponseErrorListener provideResponseErrorListener() {
        return mErrorListener == null ? new ResponseErrorImpl() : mErrorListener;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.RetrofitConfiguration provideRetrofitConfiguration() {
        return mRetrofitConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.OkhttpConfiguration provideOkhttpConfiguration() {
        return mOkhttpConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    AppModule.GsonConfiguration provideGsonConfiguration() {
        return mGsonConfiguration;
    }

    @Singleton
    @Provides
    RequestInterceptor.Level providePrintHttpLogLevel() {
        return mPrintHttpLogLevel == null ? RequestInterceptor.Level.ALL : mPrintHttpLogLevel;
    }

    @Singleton
    @Provides
    FormatPrinter provideFormatPrinter() {
        return mFormatPrinter == null ? new DefaultFormatPrinter() : mFormatPrinter;
    }

    @Singleton
    @Provides
    Cache.Factory provideCacheFactory(Application application) {
        return mCacheFactory == null ? type -> {
            //若想自定义 LruCache 的 size, 或者不想使用 LruCache, 想使用自己自定义的策略
            //使用 GlobalConfigModule.Builder#cacheFactory() 即可扩展
            switch (type.getCacheTypeId()) {
                //Activity、Fragment 以及 Extras 使用 IntelligentCache (具有 LruCache 和 可永久存储数据的 Map)
                case CacheType.EXTRAS_TYPE_ID:
                case CacheType.ACTIVITY_CACHE_TYPE_ID:
                    return new IntelligentCache(type.calculateCacheSize(application));
                default:
                    return new LruCache(type.calculateCacheSize(application));
            }
        } : mCacheFactory;
    }

    @Singleton
    @Provides
    ThreadPoolExecutor provideExecutorService() {
        return mThreadPoolExecutor == null ? ArmExecutor.INSTANCE.instance() : mThreadPoolExecutor;
    }

    @Singleton
    @Provides
    @Nullable
    IRepositoryManager.ObtainServiceDelegate provideObtainServiceDelegate() {
        return mObtainServiceDelegate;
    }

    public static final class Builder {
        private HttpUrl apiUrl;
        private BaseUrl baseUrl;
        private BaseImageLoaderStrategy loaderStrategy;
        private GlobalHttpHandler handler;
        private List<Interceptor> interceptors;
        private ResponseErrorListener responseErrorListener;
        private File cacheFile;
        private ClientModule.RetrofitConfiguration retrofitConfiguration;
        private ClientModule.OkhttpConfiguration okhttpConfiguration;
        private AppModule.GsonConfiguration gsonConfiguration;
        private RequestInterceptor.Level printHttpLogLevel;
        private FormatPrinter formatPrinter;
        private Cache.Factory cacheFactory;
        private ThreadPoolExecutor threadPoolExecutor;
        private IRepositoryManager.ObtainServiceDelegate obtainServiceDelegate;

        private Builder() {
        }

        public Builder baseurl(String baseUrl) {
            this.apiUrl = HttpUrl.parse(baseUrl);
            return this;
        }

        public Builder baseurl(BaseUrl baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder imageLoaderStrategy(BaseImageLoaderStrategy loaderStrategy) {
            this.loaderStrategy = loaderStrategy;
            return this;
        }

        public Builder globalHttpHandler(GlobalHttpHandler handler) {
            this.handler = handler;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            if (interceptors == null) {
                interceptors = new ArrayList<>();
            }
            this.interceptors.add(interceptor);
            return this;
        }

        public Builder responseErrorListener(ResponseErrorListener listener) {
            this.responseErrorListener = listener;
            return this;
        }

        public Builder cacheFile(File cacheFile) {
            this.cacheFile = cacheFile;
            return this;
        }

        public Builder retrofitConfiguration(ClientModule.RetrofitConfiguration retrofitConfiguration) {
            this.retrofitConfiguration = retrofitConfiguration;
            return this;
        }

        public Builder okhttpConfiguration(ClientModule.OkhttpConfiguration okhttpConfiguration) {
            this.okhttpConfiguration = okhttpConfiguration;
            return this;
        }

        public Builder gsonConfiguration(AppModule.GsonConfiguration gsonConfiguration) {
            this.gsonConfiguration = gsonConfiguration;
            return this;
        }

        public Builder printHttpLogLevel(RequestInterceptor.Level printHttpLogLevel) {
            this.printHttpLogLevel = printHttpLogLevel;
            return this;
        }

        public Builder formatPrinter(FormatPrinter formatPrinter) {
            this.formatPrinter = formatPrinter;
            return this;
        }

        public Builder executorService(ThreadPoolExecutor threadPoolExecutor) {
            this.threadPoolExecutor = threadPoolExecutor;
            return this;
        }

        public Builder obtainServiceDelegate(IRepositoryManager.ObtainServiceDelegate obtainServiceDelegate) {
            this.obtainServiceDelegate = obtainServiceDelegate;
            return this;
        }

        public Builder cacheFactory(Cache.Factory cacheFactory) {
            this.cacheFactory = cacheFactory;
            return this;
        }

        public GlobalConfigModule build() {
            return new GlobalConfigModule(this);
        }
    }
}
